package ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import ast.decls.Declaration;
import ast.errors.ParseException;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.symtab.elements.CSymbol;
import ast.types.CFuncParam;
import ast.types.CStructField;
import ast.types.CStructType;
import ast.types.CType;
import ast.types.decl.CDecl;
import ast.types.main.CTypeKind;
import ast.types.parser.ParseBase;
import ast.types.parser.ParseDecl;
import ast.types.util.TypeMerger;
import ast.types.util.TypeSizes;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist; 
import jscan.hashed.Hash_ident;

public class TestCTypeNew {

  private CType parseType(Parse p) {
    return new ParseBase(p).parseBase();
  }

  private CDecl parseDecl(Parse p) {
    return new ParseDecl(p).parseDecl();
  }

  private CType build(CType type, CDecl decl) {
    return TypeMerger.build(type, decl);
  }

  //////////////////////////////////////////////

  @Test
  public void test0() throws IOException {
    final String source = "int *a";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertEquals("a", decl.getName().getName());
    assertEquals(CTypeKind.TP_POINTER_TO, type.getKind());
    assertEquals(2, type.chainLength());
  }

  @Test
  public void test1() throws IOException {
    final String source = "int **a";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertEquals("a", decl.getName().getName());
    assertEquals(CTypeKind.TP_POINTER_TO, type.getKind());
    assertEquals(CTypeKind.TP_POINTER_TO, type.getTpPointer().getPointerTo().getKind());
    assertEquals(CTypeKind.TP_INT, type.getTpPointer().getPointerTo().getTpPointer().getPointerTo().getKind());
    assertEquals(3, type.chainLength());
  }

  @Test
  public void test2() throws IOException {
    final String source = "int (*a)";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertEquals("a", decl.getName().getName());
    assertEquals(CTypeKind.TP_POINTER_TO, type.getKind());
    assertEquals(CTypeKind.TP_INT, type.getTpPointer().getPointerTo().getKind());
    assertEquals(2, type.chainLength());
  }

  @Test
  public void testArray() throws IOException {
    final String source = "int a[]";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertEquals("a", decl.getName().getName());
    assertEquals(CTypeKind.TP_ARRAY_OF, type.getKind());
    assertEquals(CTypeKind.TP_INT, type.getTpArray().getArrayOf().getKind());
    assertEquals(2, type.chainLength());
  }

  @Test
  public void testFunc() throws IOException {
    final String source = "int a()";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertEquals("a", decl.getName().getName());
    assertEquals(CTypeKind.TP_FUNCTION, type.getKind());
    assertEquals(CTypeKind.TP_INT, type.getTpFunction().getReturnType().getKind());
    assertEquals(2, type.chainLength());
  }

  @Test
  public void testFuncWithParams() throws IOException {
    final String source = "int a(int x, int y, int z)";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertEquals("a", decl.getName().getName());
    assertEquals(CTypeKind.TP_FUNCTION, type.getKind());
    assertEquals(CTypeKind.TP_INT, type.getTpFunction().getReturnType().getKind());
    assertEquals(2, type.chainLength());

    final List<CFuncParam> parameters = type.getTpFunction().getParameters();
    assertEquals(3, parameters.size());

    assertEquals(CTypeKind.TP_INT, parameters.get(0).getType().getKind());
    assertEquals("x", parameters.get(0).getName().getName());

    assertEquals(CTypeKind.TP_INT, parameters.get(1).getType().getKind());
    assertEquals("y", parameters.get(1).getName().getName());

    assertEquals(CTypeKind.TP_INT, parameters.get(2).getType().getKind());
    assertEquals("z", parameters.get(2).getName().getName());
  }

  @Test
  public void testFuncWithParamsVariadic() throws IOException {
    final String source = "int a(int x, int y, int z, ...)";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertEquals("a", decl.getName().getName());
    assertEquals(CTypeKind.TP_FUNCTION, type.getKind());
    assertEquals(CTypeKind.TP_INT, type.getTpFunction().getReturnType().getKind());
    assertEquals(2, type.chainLength());
    assertTrue(type.getTpFunction().isVariadic());

    final List<CFuncParam> parameters = type.getTpFunction().getParameters();
    assertEquals(3, parameters.size());

    assertEquals(CTypeKind.TP_INT, parameters.get(0).getType().getKind());
    assertEquals("x", parameters.get(0).getName().getName());

    assertEquals(CTypeKind.TP_INT, parameters.get(1).getType().getKind());
    assertEquals("y", parameters.get(1).getName().getName());

    assertEquals(CTypeKind.TP_INT, parameters.get(2).getType().getKind());
    assertEquals("z", parameters.get(2).getName().getName());
  }

  @Test
  public void testAbstractDecl() throws IOException {
    final String source = "int **";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertTrue(decl.isAstract());
    assertEquals(3, type.chainLength());

    //assertEquals("pointer_to(pointer_to(int))", type.toString());
    //assertEquals("int**", type.toString());
  }

  @Test
  public void testPtr() throws IOException {
    final String source = "int *const *const *a";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertFalse(decl.isAstract());
    assertEquals(4, type.chainLength());

    assertEquals(CTypeKind.TP_POINTER_TO, type.getKind());
    final CType pointerToPointer = type.getTpPointer().getPointerTo();
    assertEquals(CTypeKind.TP_POINTER_TO, pointerToPointer.getKind());

    final CType pointerToPointerToPointer = type.getTpPointer().getPointerTo().getTpPointer().getPointerTo();
    assertEquals(CTypeKind.TP_POINTER_TO, pointerToPointerToPointer.getKind());
  }

  @Test
  public void testPtrToArr() throws IOException {
    final String source = "int (*a)[]";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertFalse(decl.isAstract());
    assertEquals(3, type.chainLength());

    assertEquals(CTypeKind.TP_POINTER_TO, type.getKind());
    assertEquals(CTypeKind.TP_ARRAY_OF, type.getTpPointer().getPointerTo().getKind());
    assertEquals(CTypeKind.TP_INT, type.getTpPointer().getPointerTo().getTpArray().getArrayOf().getKind());

  }

  @Test
  public void testPtrToFn() throws IOException {
    final String source = "int (*a)()";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertFalse(decl.isAstract());
    assertEquals(3, type.chainLength());

    assertEquals(CTypeKind.TP_POINTER_TO, type.getKind());
    assertEquals(CTypeKind.TP_FUNCTION, type.getTpPointer().getPointerTo().getKind());
    assertEquals(CTypeKind.TP_INT, type.getTpPointer().getPointerTo().getTpFunction().getReturnType().getKind());

  }

  @Test
  public void testPrint() throws IOException {
    List<String> tests = new ArrayList<String>();

    tests.add("int *a()");
    tests.add("int (*b)()");
    tests.add("int (*c)[][]");
    tests.add("int *d[]");

    //     pointer_to(array_of(array_of(pointer_to(func_ret(pointer_to(array_of(pointer_to(int))))))))
    //"foo(ctPointerTo(ctArrayOf(ctArrayOf(ctPointerTo(ctFunction(ctPointerTo(ctArrayOf(ctPointerTo()))))))))"
    tests.add("int *(*(*(*foo)[][])())[]");

    tests.add("int *[]");
    tests.add("int *()");
    tests.add("int (*)()");
    tests.add("int (*)[]");

    for (String e : tests) {

      Tokenlist it = new ParserMain(new StringBuilder(e)).preprocess();
      Parse p = new Parse(it);

      CType type = parseType(p);
      CDecl decl = parseDecl(p);
      type = build(type, decl);

      //System.out.println((!decl.isAstract() ? decl.getName().getName() + ": " : "") + type.toString());
    }
  }

  @Test
  public void testMultidimArr() throws IOException {
    final String source = "int a[1][2][3]";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertFalse(decl.isAstract());
    assertEquals(4, type.chainLength());

    assertEquals("a", decl.getName().getName());

    assertEquals(CTypeKind.TP_ARRAY_OF, type.getKind());
    assertEquals(CTypeKind.TP_ARRAY_OF, type.getTpArray().getArrayOf().getKind());
    assertEquals(CTypeKind.TP_ARRAY_OF, type.getTpArray().getArrayOf().getTpArray().getArrayOf().getKind());
    assertEquals(CTypeKind.TP_INT,
        type.getTpArray().getArrayOf().getTpArray().getArrayOf().getTpArray().getArrayOf().getKind());
  }

  @Test
  public void testStruct_0() throws IOException {
    final String source = "struct x { int a,b,c; };";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_STRUCT, type.getKind());
    assertEquals(3, type.getTpStruct().getFields().size());

  }

  @Test
  public void testStructInsideAnotherStruct() throws IOException {
    final String source = "struct x { int a; struct xx { int aa; } fname; };";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_STRUCT, type.getKind());
    assertEquals(2, type.getTpStruct().getFields().size());

  }

  @Test
  public void testStructInsideAnotherStructAnonymous() throws IOException {
    final String source = "struct x { int a; struct { int aa; }; };";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_STRUCT, type.getKind());

    final List<CStructField> fields = type.getTpStruct().getFields();
    assertEquals(2, fields.size());

    assertEquals("a", fields.get(0).getName().getName());
    assertEquals("aa", fields.get(1).getName().getName());

  }

  @Test
  public void testStructInsideAnotherStructAnonymousAndOther() throws IOException {
    final String source = "struct x { int a; struct { int aa; }; struct ntag { int aaa; } field_name; };";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_STRUCT, type.getKind());

    final List<CStructField> fields = type.getTpStruct().getFields();
    assertEquals(3, fields.size());

    assertEquals("a", fields.get(0).getName().getName());
    assertEquals("aa", fields.get(1).getName().getName());

  }

  @Test
  public void testFuncParamsPtr() throws IOException {
    final String source = "int f(int *pi, int **ppi, int ***pppi)";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_FUNCTION, type.getKind());

    List<CFuncParam> params = type.getTpFunction().getParameters();
    assertEquals("pi", params.get(0).getName().getName());
    assertEquals("ppi", params.get(1).getName().getName());
    assertEquals("pppi", params.get(2).getName().getName());

    assertEquals(2, params.get(0).getType().chainLength());
    assertEquals(3, params.get(1).getType().chainLength());
    assertEquals(4, params.get(2).getType().chainLength());

  }

  @Test
  public void testStructFieldsCombinations() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  struct struct_tag_ {   \n");
    sb.append(" /*002*/    int a,b,c;           \n");
    sb.append(" /*003*/    int :1;              \n");
    sb.append(" /*004*/    int d:2;             \n");
    sb.append(" /*005*/    int e;               \n");
    sb.append(" /*006*/  };                     \n");

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_STRUCT, type.getKind());

    final List<CStructField> fields = type.getTpStruct().getFields();
    assertEquals(6, fields.size());

    assertTrue(!fields.get(0).isBitfield());
    assertTrue(!fields.get(1).isBitfield());
    assertTrue(!fields.get(2).isBitfield());
    assertTrue(fields.get(3).isBitfield());
    assertTrue(fields.get(4).isBitfield());
    assertTrue(!fields.get(5).isBitfield());
  }

  @Test
  public void testStructFieldsCombinations2() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  struct struct_tag_ {   \n");
    sb.append(" /*002*/    int a,b,c;           \n");
    sb.append(" /*003*/    int :1;              \n");
    sb.append(" /*004*/    int d:2;             \n");
    sb.append(" /*005*/    int e, :3, f;        \n");
    sb.append(" /*006*/    int g:4, h, :5;      \n");
    sb.append(" /*007*/  };                     \n");

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_STRUCT, type.getKind());

    final List<CStructField> fields = type.getTpStruct().getFields();
    assertEquals(11, fields.size());
  }

  @Test
  public void testStructFieldsCombinations3() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("/*001*/  struct test_anon {                                                                                \n");
    sb.append("/*002*/    int a;                                                                                          \n");
    sb.append("/*003*/    union {                                                                                         \n");
    sb.append("/*004*/      int b;                                                                                        \n");
    sb.append("/*005*/      int c;                                                                                        \n");
    sb.append("/*006*/      int d;                                                                                        \n");
    sb.append("/*007*/      int e;                                                                                        \n");
    sb.append("/*008*/    };                                                                                              \n");
    sb.append("/*009*/    struct tag1 { // has a tag, but no name, is not ANONYMOUS... is NOT change size...              \n");
    sb.append("/*010*/      int f;    // and members is not a members of parent struct... is just a struct-declaration.   \n");
    sb.append("/*011*/      int g;                                                                                        \n");
    sb.append("/*012*/      int a;    // is not redeclarations. is nested struct field.                                   \n");
    sb.append("/*013*/    };                                                                                              \n");
    sb.append("/*014*/    struct {   // has no tag, but has name... is a normal field...                                  \n");
    sb.append("/*015*/      int a; // is not redeclarations. is nested struct field.                                      \n");
    sb.append("/*016*/      int i;                                                                                        \n");
    sb.append("/*017*/      int j;                                                                                        \n");
    sb.append("/*018*/    } h;                                                                                            \n");
    sb.append("/*019*/    struct {      // has no tag and no name... is ANONYM.                                           \n");
    sb.append("/*020*/      // int a; // error: member of anonymous struct redeclares 'a'                                 \n");
    sb.append("/*021*/      int k;                                                                                        \n");
    sb.append("/*022*/      int l;                                                                                        \n");
    sb.append("/*023*/    };                                                                                              \n");
    sb.append("/*024*/  };  \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_STRUCT, type.getKind());

    final List<CStructField> fields = type.getTpStruct().getFields();
    assertEquals(8, fields.size()); // a,b,c,d,e,h,k,l => yes, 8

    String names = "a,b,c,d,e,h,k,l";
    checkStructFieldNames(type.getTpStruct(), names);

  }

  @Test
  public void testStructFieldsCombinations4() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  struct cnum {                                      \n");
    sb.append(" /*002*/      int a;                                         \n");
    sb.append(" /*003*/      int b, c, d;                                   \n");
    sb.append(" /*004*/      int e;                                         \n");
    sb.append(" /*005*/      struct {                                       \n");
    sb.append(" /*006*/          int f;                                     \n");
    sb.append(" /*007*/          int g;                                     \n");
    sb.append(" /*008*/      };                                             \n");
    sb.append(" /*009*/      struct h {                                     \n");
    sb.append(" /*010*/         int i;                                      \n");
    sb.append(" /*011*/         struct j {                                  \n");
    sb.append(" /*012*/            int k, l;                                \n");
    sb.append(" /*013*/            struct {                                 \n");
    sb.append(" /*014*/                int m;                               \n");
    sb.append(" /*015*/                int n;                               \n");
    sb.append(" /*016*/                union {                              \n");
    sb.append(" /*017*/                    int o, p;                        \n");
    sb.append(" /*018*/                    struct  {                        \n");
    sb.append(" /*019*/                       int r;                        \n");
    sb.append(" /*020*/                       int s;                        \n");
    sb.append(" /*021*/                       union {                       \n");
    sb.append(" /*022*/                           int u;                    \n");
    sb.append(" /*023*/                           struct v {                \n");
    sb.append(" /*024*/                               int w;                \n");
    sb.append(" /*025*/                               int x;                \n");
    sb.append(" /*026*/                               struct {              \n");
    sb.append(" /*027*/                                   int y;            \n");
    sb.append(" /*028*/                               };                    \n");
    sb.append(" /*029*/                               struct {              \n");
    sb.append(" /*030*/                                   int aa;           \n");
    sb.append(" /*031*/                                   int bb, cc;       \n");
    sb.append(" /*032*/                                   int dd, ee, ff;   \n");
    sb.append(" /*033*/                               } z;                  \n");
    sb.append(" /*034*/                           };                        \n");
    sb.append(" /*035*/                       };                            \n");
    sb.append(" /*036*/                    } q;                             \n");
    sb.append(" /*037*/                };                                   \n");
    sb.append(" /*038*/            };                                       \n");
    sb.append(" /*039*/         } j;                                        \n");
    sb.append(" /*040*/      } h;                                           \n");
    sb.append(" /*041*/  };                                                 \n");

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_STRUCT, type.getKind());

    final List<CStructField> fields = type.getTpStruct().getFields();
    assertEquals(8, fields.size()); // a,b,c,d,e,f,g,h => yes, 8

    String names = "a,b,c,d,e,f,g,h";
    checkStructFieldNames(type.getTpStruct(), names);
  }

  private void checkStructFieldNames(CStructType type, String names) {
    String split[] = names.split(",");
    for (String s : split) {
      assertTrue(type.isHasField(s));
    }
  }

  @Test
  public void testAbstractParamList() throws IOException {
    final String source = "int f(int*, int*);";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertFalse(decl.isAstract());
    assertEquals(2, type.chainLength());

    List<CFuncParam> params = type.getTpFunction().getParameters();
    assertEquals(2, params.size());
  }

  @Test
  public void testKnRParamList() throws IOException {
    final String source = "int f(a,b,c) int a,b,c; {}";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);

    CType type = parseType(p);
    CDecl decl = parseDecl(p);
    type = build(type, decl);

    assertFalse(decl.isAstract());
    assertEquals(2, type.chainLength());

    List<CFuncParam> params = type.getTpFunction().getParameters();
    assertEquals(3, params.size());
  }

  @Test
  public void testBuildEnumSymbols() throws IOException {
    final String source = "enum { eax = 1, ecx = eax << 1, edx = eax << 2 };";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_ENUM, type.getKind());

    assertNotNull(p.getSym(Hash_ident.getHashedIdent("eax")));
    assertEquals(1, p.getSym(Hash_ident.getHashedIdent("eax")).getEnumvalue());

    assertNotNull(p.getSym(Hash_ident.getHashedIdent("ecx")));
    assertEquals(1 << 1, p.getSym(Hash_ident.getHashedIdent("ecx")).getEnumvalue());

    assertNotNull(p.getSym(Hash_ident.getHashedIdent("edx")));
    assertEquals(1 << 2, p.getSym(Hash_ident.getHashedIdent("edx")).getEnumvalue());

  }

  @Test
  public void testBuildEnumNoExpr() throws IOException {
    final String source = "enum { eax, ecx, edx, };";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_ENUM, type.getKind());

    assertNotNull(p.getSym(Hash_ident.getHashedIdent("eax")));
    assertEquals(0, p.getSym(Hash_ident.getHashedIdent("eax")).getEnumvalue());

    assertNotNull(p.getSym(Hash_ident.getHashedIdent("ecx")));
    assertEquals(1, p.getSym(Hash_ident.getHashedIdent("ecx")).getEnumvalue());

    assertNotNull(p.getSym(Hash_ident.getHashedIdent("edx")));
    assertEquals(2, p.getSym(Hash_ident.getHashedIdent("edx")).getEnumvalue());
  }

  @Test
  public void testBuildArrLen() throws IOException {
    final String source = "int a[1][2][3];";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_ARRAY_OF, type.getKind());
    assertEquals(1, type.getTpArray().getArrayLen());
    assertEquals(2, type.getTpArray().getArrayOf().getTpArray().getArrayLen());
    assertEquals(3, type.getTpArray().getArrayOf().getTpArray().getArrayOf().getTpArray().getArrayLen());

    assertEquals(1 * 2 * 3 * TypeSizes.get(CTypeKind.TP_INT), type.getSize());
  }

  @Test
  public void testBuildArrLen2() throws IOException {
    final String source = "int a[1][2][3][4];";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_ARRAY_OF, type.getKind());
    assertEquals(1, type.getTpArray().getArrayLen());
    assertEquals(2, type.getTpArray().getArrayOf().getTpArray().getArrayLen());
    assertEquals(3, type.getTpArray().getArrayOf().getTpArray().getArrayOf().getTpArray().getArrayLen());

    assertEquals(1 * 2 * 3 * 4 * TypeSizes.get(CTypeKind.TP_INT), type.getSize());
  }

  @Test
  public void testStructLayout() throws IOException {
    StringBuilder sb = new StringBuilder();
    //@formatter:off
    sb.append(" /*001*/  struct cnum {   \n");
    sb.append(" /*002*/    int a;        \n");
    sb.append(" /*003*/    int b;        \n");
    sb.append(" /*004*/    int c;        \n");
    sb.append(" /*005*/    int d, e;     \n");
    sb.append(" /*006*/  };              \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_STRUCT, type.getKind());

    final List<CStructField> fields = type.getTpStruct().getFields();
    assertEquals(5, fields.size());

    assertEquals(5 * TypeSizes.get(CTypeKind.TP_INT), type.getSize());
    assertEquals(0, fields.get(0).getOffset());
    assertEquals(4, fields.get(1).getOffset());
    assertEquals(8, fields.get(2).getOffset());
    assertEquals(12, fields.get(3).getOffset());
    assertEquals(16, fields.get(4).getOffset());
    assertEquals(4, type.getAlign());

    for (CStructField f : fields) {
      //System.out.println("printf(\"%d\\n\", offsetof(struct cnum, " + f.getName().getName() + "));");
    }
  }

  @Test
  public void testUnionLayout() throws IOException {
    StringBuilder sb = new StringBuilder();
    //@formatter:off
    sb.append(" /*001*/  union cnum {   \n");
    sb.append(" /*002*/    int a;       \n");
    sb.append(" /*003*/    int b;       \n");
    sb.append(" /*004*/    int c;       \n");
    sb.append(" /*005*/    int d, e;    \n");
    sb.append(" /*006*/  };             \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    p.pushscope();

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_UNION, type.getKind());

    final List<CStructField> fields = type.getTpStruct().getFields();
    assertEquals(5, fields.size());

    assertEquals(0, fields.get(0).getOffset());
    assertEquals(0, fields.get(1).getOffset());
    assertEquals(0, fields.get(2).getOffset());
    assertEquals(0, fields.get(3).getOffset());
    assertEquals(0, fields.get(4).getOffset());

    assertEquals(TypeSizes.get(CTypeKind.TP_INT), type.getSize());
    assertEquals(TypeSizes.get(CTypeKind.TP_INT), type.getAlign());

    for (CStructField f : fields) {
      //System.out.println("printf(\"%d\\n\", offsetof(struct cnum, " + f.getName().getName() + "));");
    }
  }

  @Test
  public void testBuildArrLenFromEnumConst() throws IOException {
    StringBuilder sb = new StringBuilder();
    //@formatter:off
    sb.append(" enum { one=1, two=2, three }; \n");
    sb.append(" int arr[one][two][three];     \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

    // do it brutality.
    CSymbol var = unit.getExternalDeclarations().get(1).getDeclaration().getVariables().get(0);

    assertTrue(var.getType().isArray());
    assertEquals(1 * 2 * 3 * TypeSizes.get(CTypeKind.TP_INT), var.getType().getSize());
  }

  @Test
  public void testTypedefsMix1() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  typedef int i32, *pi32;                                   \n");
    sb.append(" /*002*/  typedef i32 i32;                                          \n");
    sb.append(" /*003*/  i32 typedef i32;                                          \n");
    sb.append(" /*004*/  int typedef i32;                                          \n");
    sb.append(" /*005*/  i32 typedef word, i32;                                    \n");
    sb.append(" /*006*/  word typedef i32, i32, word;                              \n");
    sb.append(" /*007*/  static i32 x; // storage-class of [x] is STATIC           \n");
    sb.append(" /*008*/  const auto i32 y = 1;  // declare [y] here is ok.         \n");
    sb.append(" /*009*/  extern const i32 z; // storage of [z] is EXTERN           \n");
    sb.append(" /*010*/  i32 i32 = 0; // int i32; storage-class of [i32] is AUTO   \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

    List<CSymbol> varsList1 = unit.getExternalDeclarations().get(0).getDeclaration().getVariables();
    assertEquals(2, varsList1.size());

  }

  @Ignore
  @Test
  public void testKnR_func1() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int f(a,b,c) int a,b,c; {} \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();
  }

  @Test
  public void testTypedefsMix2() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*033*/    // $6.7.8.6                         \n");
    sb.append(" /*034*/    typedef signed int t;               \n");
    sb.append(" /*035*/    typedef int plain;                  \n");
    sb.append(" /*036*/    struct tag {                        \n");
    sb.append(" /*037*/        unsigned t;                     \n");
    sb.append(" /*038*/        const t:5; // unnamed bitfield  \n");
    sb.append(" /*039*/        plain r;                        \n");
    sb.append(" /*040*/    };                                  \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

    assertEquals(3, unit.getExternalDeclarations().size());
  }

  @Test
  public void testBitfields_0() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  struct flags {                    \n");
    sb.append(" /*002*/      unsigned int                  \n");
    sb.append(" /*003*/          cf:1, :1, pf:1, :1,       \n");
    sb.append(" /*004*/          af:1, :1, zf:1, sf:1,     \n");
    sb.append(" /*005*/          tf:1, if:1, df:1, of:1,   \n");
    sb.append(" /*006*/          iopl:2, nt:1, :1;         \n");
    sb.append(" /*007*/      };                            \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

    assertEquals(1, unit.getExternalDeclarations().size());
    Declaration declaration = unit.getExternalDeclarations().get(0).getDeclaration();

    CType type = declaration.getAgregate();
    assertEquals(CTypeKind.TP_STRUCT, type.getKind());
    final List<CStructField> fields = type.getTpStruct().getFields();
    assertEquals(15, fields.size());

    for (CStructField f : fields) {
      assertEquals(CTypeKind.TP_BITFIELD, f.getType().getKind());
    }

  }

  @Test
  public void testBitfields_1() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  struct flags {      \n");
    sb.append(" /*002*/      char  c : 8;    \n");
    sb.append(" /*003*/      short s : 16;   \n");
    sb.append(" /*004*/      int   i : 32;   \n");
    sb.append(" /*005*/      long  l : 64;   \n");
    sb.append(" /*006*/  };                  \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

    assertEquals(1, unit.getExternalDeclarations().size());
    Declaration declaration = unit.getExternalDeclarations().get(0).getDeclaration();

    CType type = declaration.getAgregate();
    assertEquals(CTypeKind.TP_STRUCT, type.getKind());

    final List<CStructField> fields = type.getTpStruct().getFields();
    assertEquals(4, fields.size());

    for (CStructField f : fields) {
      assertEquals(CTypeKind.TP_BITFIELD, f.getType().getKind());
    }

  }

  @Test(expected = ParseException.class)
  public void testBitfields_2() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  struct flags {      \n");
    sb.append(" /*002*/      char  c : 9;    \n"); // !!!
    sb.append(" /*003*/      short s : 16;   \n");
    sb.append(" /*004*/      int   i : 32;   \n");
    sb.append(" /*005*/      long  l : 64;   \n");
    sb.append(" /*006*/  };                  \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

    assertEquals(1, unit.getExternalDeclarations().size());
    Declaration declaration = unit.getExternalDeclarations().get(0).getDeclaration();

    CType type = declaration.getAgregate();
    assertEquals(CTypeKind.TP_STRUCT, type.getKind());

    final List<CStructField> fields = type.getTpStruct().getFields();
    assertEquals(4, fields.size());

    for (CStructField f : fields) {
      assertEquals(CTypeKind.TP_BITFIELD, f.getType().getKind());
    }

  }

  @Test
  public void testConstPointer() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("static int *const *pi; ");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_POINTER_TO, type.getKind());
    assertEquals(3, type.chainLength());
    assertTrue(type.getTpPointer().isConst());

    assertEquals(CTypeKind.TP_POINTER_TO, type.getTpPointer().getPointerTo().getKind());
    assertFalse(type.getTpPointer().getPointerTo().isConst());
  }

  @Ignore
  @Test
  public void testConst() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("static const int pi; ");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_INT, type.getKind());
    assertTrue(type.isConst());
  }

  @Ignore
  @Test
  public void testStructHasConstFields() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  struct s1 {          \n");
    sb.append(" /*002*/    int a;             \n");
    sb.append(" /*003*/    struct s2 {        \n");
    sb.append(" /*004*/      int b;           \n");
    sb.append(" /*005*/      struct s3 {      \n");
    sb.append(" /*006*/        const int c;   \n");
    sb.append(" /*007*/      } c;             \n");
    sb.append(" /*008*/    } b;               \n");
    sb.append(" /*009*/  };                   \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

    assertEquals(1, unit.getExternalDeclarations().size());
    Declaration declaration = unit.getExternalDeclarations().get(0).getDeclaration();

    CType type = declaration.getAgregate();
    assertEquals(CTypeKind.TP_STRUCT, type.getKind());

    assertTrue(type.isConst());
    assertTrue(type.getTpStruct().isHasConstFields());

  }

  @Ignore
  @Test
  public void testFspecs() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("static inline const int f();");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertEquals(CTypeKind.TP_FUNCTION, type.getKind());
    final CType rtype = type.getTpFunction().getReturnType();

    assertTrue(rtype.isConst());
    assertTrue(rtype.isInline());
  }

  @Test
  public void testConstPointer2() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("int *const *pi; ");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);

    CType base = parseType(p);
    CDecl decl = parseDecl(p);
    CType type = build(base, decl);

    assertTrue(type.getTpPointer().isConst());
    assertFalse(type.getTpPointer().getPointerTo().getTpPointer().isConst());
  }

}
