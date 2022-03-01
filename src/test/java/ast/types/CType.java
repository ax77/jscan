package ast.types;

import static ast.types.CTypeImpl.FINLIN;
import static ast.types.CTypeImpl.FNORET;
import static ast.types.CTypeImpl.QCONST;

import java.util.List;

import jscan.utils.AstParseException;

public class CType implements CTypeApi {

  private final CTypeKind kind;
  private int qualifiers;

  private int size;
  private int align;

  private CPointerType tpPointer;
  private CArrayType tpArray;
  private CFunctionType tpFunction;
  private CStructType tpStruct;
  private CEnumType tpEnum;
  private CBitfieldType tpBitfield;

  public void applyTqual(int f) {
    qualifiers |= f;
  }

  // for primitives
  public CType(CTypeKind kind) {
    this.kind = kind;
    this.size = TypeSizes.get(kind);
    this.align = this.size;

  }

  public CType(CPointerType tpPointer) {
    this.kind = CTypeKind.TP_POINTER_TO;
    this.tpPointer = tpPointer;
    this.size = TypeSizes.get(CTypeKind.TP_POINTER_TO);
    this.align = this.size;

  }

  public CType(CFunctionType cFunctionType) {
    this.kind = CTypeKind.TP_FUNCTION;
    this.tpFunction = cFunctionType;
    this.size = TypeSizes.get(CTypeKind.TP_FUNCTION);
    this.align = this.size;

  }

  public CType(CArrayType cArrayType) {
    this.kind = CTypeKind.TP_ARRAY_OF;
    this.tpArray = cArrayType;
    this.size = cArrayType.getArrayLen() * cArrayType.getArrayOf().getSize();
    this.align = cArrayType.getArrayOf().getAlign();

  }

  public CType(CStructType tpStruct, int size, int align) {
    this.kind = (tpStruct.isUnion() ? CTypeKind.TP_UNION : CTypeKind.TP_STRUCT);
    this.tpStruct = tpStruct;
    this.size = size;
    this.align = align;

  }

  public CType(CEnumType tpEnum) {
    this.kind = CTypeKind.TP_ENUM;
    this.tpEnum = tpEnum;
    this.size = TypeSizes.get(CTypeKind.TP_ENUM);
    this.align = this.size;

  }

  public CType(CBitfieldType tpBitfield) {
    this.kind = CTypeKind.TP_BITFIELD;
    this.tpBitfield = tpBitfield;

    //TODO:
    this.size = tpBitfield.getBase().getSize();
    this.align = 1;
  }

  private void assertGetType(CTypeKind need) {
    if (need != kind) {
      throw new AstParseException("internal error: you want get type " + need.toString() + " from " + kind.toString());
    }
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

  public CTypeKind getKind() {
    return kind;
  }

  public CArrayType getTpArray() {
    assertGetType(CTypeKind.TP_ARRAY_OF);
    return tpArray;
  }

  public CFunctionType getTpFunction() {
    assertGetType(CTypeKind.TP_FUNCTION);
    return tpFunction;
  }

  public CStructType getTpStruct() {
    if (!isStrUnion()) {
      throw new AstParseException("you want get fields from something not a struct or union.");
    }
    return tpStruct;
  }

  public CEnumType getTpEnum() {
    assertGetType(CTypeKind.TP_ENUM);
    return tpEnum;
  }

  public int chainLength() {
    int r = 0;
    if (isPrimitive()) {
      r++;
    } else {
      if (kind == CTypeKind.TP_POINTER_TO) {
        r++;
        r += tpPointer.getPointerTo().chainLength();
      }
      if (kind == CTypeKind.TP_ARRAY_OF) {
        r++;
        r += tpArray.getArrayOf().chainLength();
      }
      if (kind == CTypeKind.TP_FUNCTION) {
        r++;
        r += tpFunction.getReturnType().chainLength();
      }
    }
    return r;
  }

  @Override
  public String toString() {

    if (isPrimitive()) {
      return TypePrinter.primitiveToString(kind);
    }

    if (isBitfield()) {
      return tpBitfield.toString();
    }

    else if (isPointer()) {
      return tpPointer.toString();
    }

    else if (isArray()) {
      return tpArray.toString();
    }

    else if (isFunction()) {
      return tpFunction.toString();
    }

    else if (isStrUnion()) {
      return tpStruct.toString();
    }

    else if (isEnumeration()) {
      return tpEnum.toString();
    }

    else {
      throw new AstParseException("Unknown type: " + kind.toString());
    }
  }

  @Override
  public boolean isStrUnion() {
    return isStruct() || isUnion();
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public int getAlign() {
    return align;
  }

  @Override
  public boolean isUnion() {
    return kind == CTypeKind.TP_UNION;
  }

  @Override
  public boolean isFunction() {
    return kind == CTypeKind.TP_FUNCTION;
  }

  @Override
  public boolean isObject() {
    return isScalar() || isNoScalar();
  }

  @Override
  public boolean isScalar() {
    return isPointer() || isArithmetic();
  }

  @Override
  public boolean isNoScalar() {
    return isStruct() || isUnion() || isArray();
  }

  @Override
  public boolean isStruct() {
    return kind == CTypeKind.TP_STRUCT;
  }

  @Override
  public boolean isArray() {
    return kind == CTypeKind.TP_ARRAY_OF;
  }

  @Override
  public boolean isArithmetic() {
    return isInteger() || isFloatingType();
  }

  @Override
  public boolean isInteger() {
    return isBool() || isChar() || isUchar() || isShort() || isUshort() || isInt() || isUint() || isLong() || isUlong()
        || isLongLong() || isUlongLong()
        //
        || isBitfield() || isEnumeration();
  }

  @Override
  public boolean isBitfield() {
    return kind == CTypeKind.TP_BITFIELD;
  }

  @Override
  public boolean isPlainBitfield() {
    return isBitfield();
  }

  @Override
  public boolean isSignedBitfield() {
    return isBitfield() && !tpBitfield.getBase().isUnsigned();
  }

  @Override
  public boolean isUnsignedBitfield() {
    return isBitfield() && tpBitfield.getBase().isUnsigned();
  }

  @Override
  public boolean isEnumeration() {
    return kind == CTypeKind.TP_ENUM;
  }

  @Override
  public boolean isFloatingType() {
    return isFloat() || isDouble() || isLongDouble();
  }

  @Override
  public boolean isVoid() {
    return kind == CTypeKind.TP_VOID;
  }

  @Override
  public boolean isIncompleteStruct() {
    return isStruct() && tpStruct.isIncomplete();
  }

  @Override
  public boolean isIncompleteUnion() {
    return isUnion() && tpStruct.isIncomplete();
  }

  @Override
  public boolean isIncompleteArray() {
    return isArray() && tpArray.isIncomplete();
  }

  public boolean isIncompleteEnum() {
    return isEnumeration() && tpEnum.isIncomplete();
  }

  @Override
  public boolean isIncomplete() {
    return isVoid() || isIncompleteArray() || isIncompleteStruct() || isIncompleteUnion() || isIncompleteEnum();
  }

  @Override
  public boolean isEqualTo(CType another) {

    if (kind != another.getKind()) {
      return false;
    }

    if (isPointer()) {
      return cmpPointers(another.getTpPointer());
    }

    if (isFunction()) {
      return cmpFunctions(another.getTpFunction());
    }

    if (isArray()) {
      return cmpArrays(another.getTpArray());
    }

    return true;
  }

  private boolean cmpPointers(CPointerType another) {

    if (!tpPointer.getPointerTo().isEqualTo(another.getPointerTo())) {
      return false;
    }

    if (tpPointer.isConst()) {
      if (!another.isConst()) {
        return false;
      }
    }

    return true;
  }

  private boolean cmpArrays(CArrayType another) {

    if (!tpArray.getArrayOf().isEqualTo(another.getArrayOf())) {
      return false;
    }

    if (tpArray.isIncomplete()) {
      if (!another.isIncomplete()) {
        return false;
      }
    }

    if (tpArray.getArrayLen() != another.getArrayLen()) {
      return false;
    }

    return true;
  }

  private boolean cmpFunctions(CFunctionType another) {

    final CType lhsRtype = tpFunction.getReturnType();
    final CType rhsRtype = another.getReturnType();
    if (!lhsRtype.isEqualTo(rhsRtype)) {
      return false;
    }

    if (tpFunction.isVariadic()) {
      if (!another.isVariadic()) {
        return false;
      }
    }

    final List<CFuncParam> lhsParams = tpFunction.getParameters();
    final List<CFuncParam> rhsParams = another.getParameters();
    if (lhsParams.size() != rhsParams.size()) {
      return false;
    }

    for (int i = 0; i < lhsParams.size(); ++i) {
      CFuncParam lhsParam = lhsParams.get(i);
      CFuncParam rhsParam = rhsParams.get(i);
      if (!lhsParam.getType().isEqualTo(rhsParam.getType())) {
        return false;
      }
    }

    return true;
  }

  public CPointerType getTpPointer() {
    return tpPointer;
  }

  @Override
  public boolean isConst() {
    if (isStrUnion()) {
      return tpStruct.isHasConstFields();
    }
    return (qualifiers & QCONST) == QCONST;
  }

  @Override
  public boolean isHasSignedness() {
    return isInteger();
  }

  @Override
  public boolean isUnsigned() {
    return isUchar() || isUshort() || isUint() || isUlong() || isUlongLong() || isUnsignedBitfield();
  }

  @Override
  public boolean isSigned() {
    return isHasSignedness() && !isUnsigned();
  }

  @Override
  public boolean isInline() {
    return (qualifiers & FINLIN) == FINLIN;
  }

  @Override
  public boolean isNoreturn() {
    return (qualifiers & FNORET) == FNORET;
  }

//@formatter:off
  @Override public boolean isBool() { return kind == CTypeKind.TP_BOOL; }
  @Override public boolean isChar() { return kind == CTypeKind.TP_CHAR; }
  @Override public boolean isUchar() { return kind == CTypeKind.TP_UCHAR; }
  @Override public boolean isShort() { return kind == CTypeKind.TP_SHORT; }
  @Override public boolean isUshort() { return kind == CTypeKind.TP_USHORT; }
  @Override public boolean isInt() { return kind == CTypeKind.TP_INT; }
  @Override public boolean isUint() { return kind == CTypeKind.TP_UINT; }
  @Override public boolean isLong() { return kind == CTypeKind.TP_LONG; }
  @Override public boolean isUlong() { return kind == CTypeKind.TP_ULONG; }
  @Override public boolean isLongLong() { return kind == CTypeKind.TP_LONG_LONG; }
  @Override public boolean isUlongLong() { return kind == CTypeKind.TP_ULONG_LONG; }
  @Override public boolean isFloat() { return kind == CTypeKind.TP_FLOAT; }
  @Override public boolean isDouble() { return kind == CTypeKind.TP_DOUBLE; }
  @Override public boolean isLongDouble() { return kind == CTypeKind.TP_LONG_DOUBLE; }
//@formatter:on

  @Override
  public boolean isPointerToCompat(CType lhsRT) {
    // TODO: XXX
    return true;
  }

  @Override
  public boolean isPointer() {
    return kind == CTypeKind.TP_POINTER_TO;
  }

  @Override
  public boolean isPointerToFunction() {
    return isPointer() && tpPointer.getPointerTo().isFunction();
  }

  @Override
  public boolean isPointerToObject() {
    return isPointer() && tpPointer.getPointerTo().isObject();
  }

  @Override
  public boolean isPointerToIncomplete() {
    return isPointer() && tpPointer.getPointerTo().isIncomplete();
  }

  @Override
  public boolean isPointerToVoid() {
    return isPointer() && tpPointer.getPointerTo().isVoid();
  }

  @Override
  public boolean isPointerToStructUnion() {
    return isPointer() && tpPointer.getPointerTo().isStrUnion();
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
