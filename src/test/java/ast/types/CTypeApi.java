package ast.types;

public interface CTypeApi {
  //@formatter:off
  public int getSize();
  public int getAlign();
  
  // I)
  public boolean isFunction();
  
  // II)
  // object types hierarchy
  public boolean isObject(); // scalar, no-scalar types
  public boolean isScalar(); // pointers, arithmetic types
  public boolean isNoScalar(); // struct, union, array

  // object, no-scalar, may be incomplete
  public boolean isStruct();
  public boolean isUnion();
  public boolean isStrUnion();
  public boolean isArray();
  
  // arithmetic, scalar
  public boolean isArithmetic(); // integer, floating
  public boolean isInteger(); // bitfields, enumeration, [int, char, etc...]
  public boolean isBitfield();
  public boolean isPlainBitfield();
  public boolean isSignedBitfield();
  public boolean isUnsignedBitfield();
  public boolean isEnumeration();
  public boolean isFloatingType();
  
  // pointers, scalar
  public boolean isPointer();
  public boolean isPointerToFunction();
  public boolean isPointerToObject();
  public boolean isPointerToIncomplete();
  
  public boolean isPointerToVoid();
  public boolean isPointerToStructUnion();
  public boolean isPointerToCompat(CType lhs);
  
  // III)
  // incomplete
  public boolean isIncomplete();
  public boolean isVoid();
  public boolean isIncompleteStruct();
  public boolean isIncompleteUnion();
  public boolean isIncompleteArray();
  
  public boolean isEqualTo(CType another);
  
  // qualifiers
  public boolean isConst();
  
  // functions
  public boolean isInline();
  public boolean isNoreturn();
  
  public boolean isHasSignedness();
  public boolean isUnsigned();
  public boolean isSigned();
  
  
  // integers
  public boolean isBool();
  public boolean isChar();
  public boolean isUchar();
  public boolean isShort();
  public boolean isUshort();
  public boolean isInt();
  public boolean isUint();
  public boolean isLong();
  public boolean isUlong();
  public boolean isLongLong();
  public boolean isUlongLong();
  public boolean isFloat();
  public boolean isDouble();
  public boolean isLongDouble();
  
}
