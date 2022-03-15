package ast.types;

import static ast.types.CTypeImpl.FINLIN;
import static ast.types.CTypeImpl.FNORET;
import static ast.types.CTypeImpl.QCONST;

import java.util.List;

import ast.builders.TypePrinter;

public class CType {

  public final CTypeKind kind;
  public int qualifiers;
  public int size;
  public int align;
  public CPointerType tpPointer;
  public CArrayType tpArray;
  public CFunctionType tpFunction;
  public CStructType tpStruct;
  public CEnumType tpEnum;
  public CBitfieldType tpBitfield;
  public int offset;
  
  public void applyTqual(int f) {
    qualifiers |= f;
  }

  // for primitives
  public CType(CTypeKind kind) {
    this.kind = kind;
    this.size = TypeSizes.getSize(kind);
    this.align = this.size;

  }

  public CType(CPointerType tpPointer) {
    this.kind = CTypeKind.TP_POINTER_TO;
    this.tpPointer = tpPointer;
    this.size = TypeSizes.getSize(CTypeKind.TP_POINTER_TO);
    this.align = this.size;

  }

  public CType(CFunctionType cFunctionType) {
    this.kind = CTypeKind.TP_FUNCTION;
    this.tpFunction = cFunctionType;
    this.size = TypeSizes.getSize(CTypeKind.TP_FUNCTION);
    this.align = this.size;

  }

  public CType(CArrayType cArrayType) {
    this.kind = CTypeKind.TP_ARRAY_OF;
    this.tpArray = cArrayType;
    this.size = cArrayType.len * cArrayType.subtype.size;
    this.align = cArrayType.subtype.align;

  }

  public CType(CStructType tpStruct, int size, int align) {
    this.kind = (tpStruct.isUnion ? CTypeKind.TP_UNION : CTypeKind.TP_STRUCT);
    this.tpStruct = tpStruct;
    this.size = size;
    this.align = align;

  }

  public CType(CEnumType tpEnum) {
    this.kind = CTypeKind.TP_ENUM;
    this.tpEnum = tpEnum;
    this.size = TypeSizes.getSize(CTypeKind.TP_ENUM);
    this.align = this.size;

  }

  public CType(CBitfieldType tpBitfield) {
    this.kind = CTypeKind.TP_BITFIELD;
    this.tpBitfield = tpBitfield;

    //TODO:
    this.size = tpBitfield.basetype.size;
    this.align = 1;
  }

  public boolean isPrimitive() {
    switch (kind) {
    case TP_POINTER_TO:
    case TP_ARRAY_OF:
    case TP_FUNCTION:
    case TP_STRUCT:
    case TP_ENUM:
    case TP_UNION:
    case TP_BITFIELD:
      return false;
    default:
      return true;
    }
  }

  @Override
  public String toString() {
    if (isPrimitive()) {
      return TypePrinter.primitiveToString(kind);
    } else if (isBitfield()) {
      return tpBitfield.toString();
    } else if (isPointer()) {
      return tpPointer.toString();
    } else if (isArray()) {
      return tpArray.toString();
    } else if (isFunction()) {
      return tpFunction.toString();
    } else if (isStrUnion()) {
      return tpStruct.toString();
    } else if (isEnumeration()) {
      return tpEnum.toString();
    }
    return kind.toString();
  }

  public boolean isStrUnion() {
    return isStruct() || isUnion();
  }

  public boolean isUnion() {
    return kind == CTypeKind.TP_UNION;
  }

  public boolean isFunction() {
    return kind == CTypeKind.TP_FUNCTION;
  }

  public boolean isObject() {
    return isScalar() || isNoScalar();
  }

  public boolean isScalar() {
    return isPointer() || isArithmetic();
  }

  public boolean isNoScalar() {
    return isStruct() || isUnion() || isArray();
  }

  public boolean isStruct() {
    return kind == CTypeKind.TP_STRUCT;
  }

  public boolean isArray() {
    return kind == CTypeKind.TP_ARRAY_OF;
  }

  public boolean isArithmetic() {
    return isInteger() || isFloatingType();
  }

  public boolean isInteger() {
    return isBool()
      || isChar()
      || isUchar()
      || isShort()
      || isUshort()
      || isInt()
      || isUint()
      || isLong()
      || isUlong()
      || isLongLong()
      || isUlongLong()
      || isBitfield()
      || isEnumeration();
  }

  public boolean isBitfield() {
    return kind == CTypeKind.TP_BITFIELD;
  }

  public boolean isPlainBitfield() {
    return isBitfield();
  }

  public boolean isSignedBitfield() {
    return isBitfield() && !tpBitfield.basetype.isUnsigned();
  }

  public boolean isUnsignedBitfield() {
    return isBitfield() && tpBitfield.basetype.isUnsigned();
  }

  public boolean isEnumeration() {
    return kind == CTypeKind.TP_ENUM;
  }

  public boolean isFloatingType() {
    return isFloat() || isDouble() || isLongDouble();
  }

  public boolean isVoid() {
    return kind == CTypeKind.TP_VOID;
  }

  public boolean isIncompleteStruct() {
    return isStruct() && !tpStruct.isComplete;
  }

  public boolean isIncompleteUnion() {
    return isUnion() && !tpStruct.isComplete;
  }

  public boolean isIncompleteArray() {
    return isArray() && !tpArray.isComplete;
  }

  public boolean isIncompleteEnum() {
    return isEnumeration() && !tpEnum.isComplete;
  }

  public boolean isIncomplete() {
    return isVoid() || isIncompleteArray() || isIncompleteStruct() || isIncompleteUnion() || isIncompleteEnum();
  }

  public boolean isEqualTo(CType another) {

    if (kind != another.kind) {
      return false;
    }

    if (isPointer()) {
      return cmpPointers(another.tpPointer);
    }

    if (isFunction()) {
      return cmpFunctions(another.tpFunction);
    }

    if (isArray()) {
      return cmpArrays(another.tpArray);
    }

    return true;
  }

  private boolean cmpPointers(CPointerType another) {

    if (!tpPointer.subtype.isEqualTo(another.subtype)) {
      return false;
    }

    if (tpPointer.isConst) {
      if (!another.isConst) {
        return false;
      }
    }

    return true;
  }

  private boolean cmpArrays(CArrayType another) {

    if (!tpArray.subtype.isEqualTo(another.subtype)) {
      return false;
    }

    if (tpArray.isComplete) {
      if (!another.isComplete) {
        return false;
      }
    }

    if (tpArray.len != another.len) {
      return false;
    }

    return true;
  }

  private boolean cmpFunctions(CFunctionType another) {

    final CType lhsRtype = tpFunction.returnType;
    final CType rhsRtype = another.returnType;
    if (!lhsRtype.isEqualTo(rhsRtype)) {
      return false;
    }

    if (tpFunction.isVariadic()) {
      if (!another.isVariadic()) {
        return false;
      }
    }

    final List<CFuncParam> lhsParams = tpFunction.parameters;
    final List<CFuncParam> rhsParams = another.parameters;
    if (lhsParams.size() != rhsParams.size()) {
      return false;
    }

    for (int i = 0; i < lhsParams.size(); ++i) {
      CFuncParam lhsParam = lhsParams.get(i);
      CFuncParam rhsParam = rhsParams.get(i);
      if (!lhsParam.type.isEqualTo(rhsParam.type)) {
        return false;
      }
    }

    return true;
  }

  public boolean isConst() {
    if (isStrUnion()) {
      return tpStruct.hasConstFields();
    }
    return (qualifiers & QCONST) == QCONST;
  }

  public boolean isHasSignedness() {
    return isInteger();
  }

  public boolean isUnsigned() {
    return isUchar() || isUshort() || isUint() || isUlong() || isUlongLong() || isUnsignedBitfield();
  }

  public boolean isSigned() {
    return isHasSignedness() && !isUnsigned();
  }

  public boolean isInline() {
    return (qualifiers & FINLIN) == FINLIN;
  }

  public boolean isNoreturn() {
    return (qualifiers & FNORET) == FNORET;
  }

//@formatter:off
   public boolean isBool() { return kind == CTypeKind.TP_BOOL; }
   public boolean isChar() { return kind == CTypeKind.TP_CHAR; }
   public boolean isUchar() { return kind == CTypeKind.TP_UCHAR; }
   public boolean isShort() { return kind == CTypeKind.TP_SHORT; }
   public boolean isUshort() { return kind == CTypeKind.TP_USHORT; }
   public boolean isInt() { return kind == CTypeKind.TP_INT; }
   public boolean isUint() { return kind == CTypeKind.TP_UINT; }
   public boolean isLong() { return kind == CTypeKind.TP_LONG; }
   public boolean isUlong() { return kind == CTypeKind.TP_ULONG; }
   public boolean isLongLong() { return kind == CTypeKind.TP_LONG_LONG; }
   public boolean isUlongLong() { return kind == CTypeKind.TP_ULONG_LONG; }
   public boolean isFloat() { return kind == CTypeKind.TP_FLOAT; }
   public boolean isDouble() { return kind == CTypeKind.TP_DOUBLE; }
   public boolean isLongDouble() { return kind == CTypeKind.TP_LONG_DOUBLE; }
//@formatter:on

  public boolean isPointerToCompat(CType lhsRT) {
    // TODO: XXX
    return true;
  }

  public boolean isPointer() {
    return kind == CTypeKind.TP_POINTER_TO;
  }

  public boolean isPointerToFunction() {
    return isPointer() && tpPointer.subtype.isFunction();
  }

  public boolean isPointerToObject() {
    return isPointer() && tpPointer.subtype.isObject();
  }

  public boolean isPointerToIncomplete() {
    return isPointer() && tpPointer.subtype.isIncomplete();
  }

  public boolean isPointerToVoid() {
    return isPointer() && tpPointer.subtype.isVoid();
  }

  public boolean isPointerToStructUnion() {
    return isPointer() && tpPointer.subtype.isStrUnion();
  }

  public boolean isAnObjectExceptBitField() {
    return isObject() && !isBitfield();
  }

  public void setSize(int size) {
    this.size = size;
  }

  public void setAlign(int align) {
    this.align = align;
  }

}
