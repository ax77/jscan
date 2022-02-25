package ast;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ast.expr.sem.TypeApplier;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.types.CArrayType;
import ast.types.CEnumType;
import ast.types.CFuncParam;
import ast.types.CFunctionType;
import ast.types.CPointerType;
import ast.types.CStructType;
import ast.types.CType;
import ast.types.decl.CDecl;
import ast.types.parser.ParseBase;
import ast.types.parser.ParseDecl;
import ast.types.util.TypeMerger;
import jscan.parse.Tokenlist; 

public class TestTypeCompat {

  //  Both types are the same.
  //  
  //  Both are pointer types, with the same type qualifiers, that point to compatible types.
  //  
  //  Both are array types whose elements have compatible types. 
  //  If both specify repetition counts, the repetition counts are equal.
  //  
  //  Both are function types whose return types are compatible. 
  //  If both specify types for their parameters, both declare the same number of parameters (including ellipses) 
  //  and the types of corresponding parameters are compatible. 
  //  Otherwise, at least one does not specify types for its parameters. 
  //  If the other specifies types for its parameters, it specifies only a fixed number of parameters and does not specify 
  //  parameters of type float or of any integer types that change when promoted.
  //  
  //  Both are structure, union, or enumeration types that are declared in different translation units with the same member names. 
  //  Structure members are declared in the same order. 
  //  Structure and union members whose names match are declared with compatible types. 
  //  Enumeration constants whose names match have the same values.

  // TODO: return special error-code with corresponding message, instead of true/false
  //

  private boolean isTypeCompatible(CType lhs, CType rhs) {
    if (lhs.getKind() != rhs.getKind()) {
      if (lhs.isInteger() && rhs.isEnumeration()) {
        return true;
      }
      if (rhs.isEnumeration() && rhs.isInteger()) {
        return true;
      }
      return false;
    }
    if (lhs.isPointer()) {
      return isPointerCompatible(lhs.getTpPointer(), rhs.getTpPointer());
    }
    if (lhs.isArray()) {
      return isArrayCompatible(lhs.getTpArray(), rhs.getTpArray());
    }
    if (lhs.isFunction()) {
      return isFunctionCompatible(lhs.getTpFunction(), rhs.getTpFunction());
    }
    if (lhs.isStrUnion()) {
      return isStructCompatible(lhs.getTpStruct(), rhs.getTpStruct());
    }
    if (lhs.isEnumeration()) {
      return isEnumCompatible(lhs.getTpEnum(), rhs.getTpEnum());
    }
    return true;
  }

  private boolean isEnumCompatible(CEnumType lhs, CEnumType rhs) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean isStructCompatible(CStructType lhs, CStructType rhs) {
    // TODO Auto-generated method stub
    return false;
  }

  //  1)
  //  Both are function types whose return types are compatible. 
  //  2)
  //  If both specify types for their parameters, both declare the same number of parameters (including ellipses) 
  //  and the types of corresponding parameters are compatible. 
  //  3)
  //  Otherwise, at least one does not specify types for its parameters. 
  //  If the other specifies types for its parameters, it specifies only a fixed number of parameters and does not specify 
  //  parameters of type float or of any integer types that change when promoted.

  private boolean isFunctionCompatible(CFunctionType lhs, CFunctionType rhs) {

    // 1)
    if (!isTypeCompatible(lhs.getReturnType(), rhs.getReturnType())) {
      return false;
    }

    // 2)
    if (lhs.getArgc() > 0 && rhs.getArgc() > 0) {
      if (lhs.getArgc() != rhs.getArgc()) {
        return false;
      }
      if (lhs.isVariadic() != rhs.isVariadic()) {
        return false;
      }
      List<CFuncParam> lhsParams = lhs.getParameters();
      List<CFuncParam> rhsParams = rhs.getParameters();
      for (int i = 0; i < lhsParams.size(); i++) {
        CFuncParam lhsParam = lhsParams.get(i);
        CFuncParam rhsParam = rhsParams.get(i);
        if (!isTypeCompatible(lhsParam.getType(), rhsParam.getType())) {
          return false;
        }
      }
    }

    // 3)
    else {

      // I)
      // this ok.
      // int f(int f);
      // int f();

      // but not this.
      // int f(int f, ...); // a parameter list with an ellipsis can't match an empty parameter name list declaration
      // int f();

      if (lhs.isVariadic() || rhs.isVariadic()) {
        return false;
      }

      // II)
      // this ok.
      // int f();
      // int f(int);

      // but not this.
      // int f();
      // int f(char); // an argument type that has a default promotion can't match an empty parameter name list declaration

      if (lhs.getArgc() > 0) {
        if (isHasPromotedParameters(lhs)) {
          return false;
        }
      } else {
        if (isHasPromotedParameters(rhs)) {
          return false;
        }
      }

    }

    return true;
  }

  private boolean isHasPromotedParameters(CFunctionType func) {
    List<CFuncParam> params = func.getParameters();
    for (int i = 0; i < params.size(); i++) {
      CType declaredType = params.get(i).getType();
      CType promotedType = TypeApplier.ipromote(declaredType);
      if (!declaredType.isEqualTo(promotedType)) {
        return true;
      }
    }
    return false;
  }

  private boolean isPointerCompatible(CPointerType lhs, CPointerType rhs) {
    if (lhs.isConst() != rhs.isConst()) {
      return false;
    }
    if (!isTypeCompatible(lhs.getPointerTo(), rhs.getPointerTo())) {
      return false;
    }
    return true;
  }

  private boolean isArrayCompatible(CArrayType lhs, CArrayType rhs) {
    if (!isTypeCompatible(lhs.getArrayOf(), rhs.getArrayOf())) {
      return false;
    }
    final int lhsLen = lhs.getArrayLen();
    final int rhsLen = rhs.getArrayLen();
    if (lhsLen > 0 && rhsLen > 0) {
      if (lhsLen != rhsLen) {
        return false;
      }
    }
    return true;
  }

  //  The translator combines compatible types to form a composite type. 
  //  The composite type is determined in one of the following ways:
  //
  //  For two types that are the same, it is the common type.
  //  
  //  For two pointer types, it is a similarly qualified pointer to the composite type pointed to.
  //  
  //  For two array types, it is an array of elements with the composite of the two element types. 
  //  If one of the array types specifies a repetition count, that type provides the repetition count for the composite type. 
  //  Otherwise, the composite has no repetition count.
  //  
  //  For two function types, it is a function type that returns a composite of the two return types. 
  //  If both specify types for their parameters, each parameter type in the composite type is the composite of the two corresponding parameter types. 
  //  If only one specifies types for its parameters, it determines the parameter types in the composite type. 
  //  Otherwise, the composite type specifies no types for its parameters.
  //  
  //  For two structure, union, or enumeration types, it is the type declared in the current translation unit.
  //
  //
  //  EXAMPLE Given the following two file scope declarations:
  //
  //    int f(int (*)(), double (*)[3]);
  //    int f(int (*)(char *), double (*)[]);
  //
  //    The resulting composite type for the function is:
  //
  //    int f(int (*)(char *), double (*)[3]);
  //
  //
  //  For example, the following two types are compatible:
  //
  //    FILE *openit(char *)     and FILE *openit()
  //    They have the composite type:
  //
  //    FILE *openit(char *)
  //    For a more complex example, the two types:
  //
  //    void (*apf[])(int x)     and void (*apf[20])()
  //    are compatible and have the composite type:
  //
  //    void (*apf[20])(int x)

  private CType composite(CType lhs, CType rhs) {
    return null;
  }

  ///////////////////////////////////////////////////////////////////////////////////////

  private CType parseType(Parse p) {
    return new ParseBase(p).parseBase();
  }

  private CDecl parseDecl(Parse p) {
    return new ParseDecl(p).parseDecl();
  }

  private CType build(CType type, CDecl decl) {
    return TypeMerger.build(type, decl);
  }

  private CType parseOne(String s) throws IOException {
    Tokenlist it = new ParserMain(new StringBuilder(s)).preprocess();
    Parse p = new Parse(it);
    p.pushscope();
    return build(parseType(p), parseDecl(p));
  }

  private CType[] toCompat(String first, String second) throws IOException {
    CType ret[] = new CType[2];
    ret[0] = parseOne(first);
    ret[1] = parseOne(second);
    return ret;
  }

  private boolean thisTwoAreCompatible(CType[] ret) {
    return isTypeCompatible(ret[0], ret[1]);
  }

  @Test
  public void testPointers() throws IOException {

    CType ret[] = toCompat("int *", "int *");
    assertTrue(thisTwoAreCompatible(ret));

    ret = toCompat("int *const", "int *const");
    assertTrue(thisTwoAreCompatible(ret));

    ret = toCompat("int *", "int *const");
    assertFalse(thisTwoAreCompatible(ret));

    ret = toCompat("int *const", "int *");
    assertFalse(thisTwoAreCompatible(ret));

    ret = toCompat("int *", "char *");
    assertFalse(thisTwoAreCompatible(ret));

  }

  @Test
  public void testBuiltin() throws IOException {
    CType ret[] = toCompat("int", "int");
    assertTrue(thisTwoAreCompatible(ret));

    ret = toCompat("int", "char");
    assertFalse(thisTwoAreCompatible(ret));
  }

  @Test
  public void testArray() throws IOException {
    CType ret[] = toCompat("int[]", "int[]");
    assertTrue(thisTwoAreCompatible(ret));

    ret = toCompat("int[1]", "int[1]");
    assertTrue(thisTwoAreCompatible(ret));

    ret = toCompat("int[1]", "int[]");
    assertTrue(thisTwoAreCompatible(ret));

    ret = toCompat("int[]", "int[1]");
    assertTrue(thisTwoAreCompatible(ret));

    // NOT compatible.

    ret = toCompat("int[2]", "int[1]");
    assertFalse(thisTwoAreCompatible(ret));

    ret = toCompat("char[1]", "int[1]");
    assertFalse(thisTwoAreCompatible(ret));
  }

  @Test
  public void testEnumWithInt() throws IOException {
    CType ret[] = toCompat("int", "enum t");
    assertTrue(thisTwoAreCompatible(ret));
  }

  @Test
  public void testFuncEmptyParamList() throws IOException {
    CType ret[] = toCompat("int f()", "int f()");
    assertTrue(thisTwoAreCompatible(ret));
  }

  // but not this.
  // int f(int f, ...); // a parameter list with an ellipsis can't match an empty parameter name list declaration
  // int f();

  @Test
  public void testFuncNotCompatVar() throws IOException {
    CType ret[] = toCompat("int f(int f, ...)", "int f()");
    assertFalse(thisTwoAreCompatible(ret));
  }

  // but not this.
  // int f();
  // int f(char);

  @Test
  public void testFuncNotCompatPromotion() throws IOException {
    CType ret[] = toCompat("int f(char)", "int f()");
    assertFalse(thisTwoAreCompatible(ret));
  }

  // int f();
  // int f(int);

  @Test
  public void testFuncCompat() throws IOException {
    CType ret[] = toCompat("int f()", "int f(int)");
    assertTrue(thisTwoAreCompatible(ret));
  }
}
