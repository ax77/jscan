package ast.symtab;

import java.util.List;

import ast.tree.Initializer;
import ast.types.CType;
import ast.types.CTypeApi;
import jscan.literals.IntLiteral;
import jscan.literals.IntLiteralParser;
import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.Token;

public class CSymbol implements CTypeApi {
  private final SourceLocation location;
  private final Token from;

  private final CSymbolBase base;
  private final Ident name;
  private final CType type;

  private IntLiteral numericConstant;
  private List<Initializer> initializer;

  private int offset;

  public CSymbol(CSymbolBase base, Ident name, CType type, Token from) {
    this.location = new SourceLocation(from);
    this.from = from;
    this.base = base;
    this.name = name;
    this.type = type;
  }

  public CSymbol(CSymbolBase base, Ident name, CType type, List<Initializer> initializer, Token from) {
    this.location = new SourceLocation(from);
    this.from = from;
    this.base = base;
    this.name = name;
    this.type = type;
    this.initializer = initializer;
  }

  public Ident getName() {
    return name;
  }

  public CType getType() {
    return type;
  }

  public int getEnumvalue() {
    return (int) numericConstant.getClong();
  }

  public void setEnumvalue(int enumvalue) {
    IntLiteral result = IntLiteralParser.parse(String.format("%d", enumvalue));
    this.numericConstant = result; // TODO:
  }

  @Override
  public String toString() {
    return "line=" + String.format("%-3d", location.getLine()) + " (name=" + name.getName() + ", type="
        + type.toString() + ", base=" + base.toString() + ") ";
  }

  public List<Initializer> getInitializer() {
    return initializer;
  }

  public IntLiteral getNumericConstant() {
    return numericConstant;
  }

  public void setNumericConstant(IntLiteral numericConstant) {
    this.numericConstant = numericConstant;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  //@formatter:off
  @Override public int     getSize()                       {  return type.getSize()                 ; }
  @Override public int     getAlign()                      {  return type.getAlign()                ; }
  @Override public boolean isFunction()                    {  return type.isFunction()              ; }
  @Override public boolean isObject()                      {  return type.isObject()                ; }
  @Override public boolean isScalar()                      {  return type.isScalar()                ; }
  @Override public boolean isNoScalar()                    {  return type.isNoScalar()              ; }
  @Override public boolean isStruct()                      {  return type.isStruct()                ; }
  @Override public boolean isUnion()                       {  return type.isUnion()                 ; }
  @Override public boolean isArray()                       {  return type.isArray()                 ; }
  @Override public boolean isArithmetic()                  {  return type.isArithmetic()            ; }
  @Override public boolean isInteger()                 {  return type.isInteger()           ; }
  @Override public boolean isBitfield()                    {  return type.isBitfield()              ; }
  @Override public boolean isPlainBitfield()               {  return type.isPlainBitfield()         ; }
  @Override public boolean isSignedBitfield()              {  return type.isSignedBitfield()        ; }
  @Override public boolean isUnsignedBitfield()            {  return type.isUnsignedBitfield()      ; }
  @Override public boolean isEnumeration()                 {  return type.isEnumeration()           ; }
  @Override public boolean isFloatingType()                {  return type.isFloatingType()          ; }
  @Override public boolean isPointer()                     {  return type.isPointer()               ; }
  @Override public boolean isPointerToFunction()           {  return type.isPointerToFunction()     ; }
  @Override public boolean isPointerToObject()             {  return type.isPointerToObject()       ; }
  @Override public boolean isPointerToIncomplete()         {  return type.isPointerToIncomplete()   ; }
  @Override public boolean isIncomplete()                  {  return type.isIncomplete()            ; }
  @Override public boolean isVoid()                        {  return type.isVoid()                  ; }
  @Override public boolean isIncompleteStruct()            {  return type.isIncompleteStruct()      ; }
  @Override public boolean isIncompleteUnion()             {  return type.isIncompleteUnion()       ; }
  @Override public boolean isIncompleteArray()             {  return type.isIncompleteArray()       ; }
  @Override public boolean isEqualTo(CType another)        {  return type.isEqualTo(      another)  ; }
  @Override public boolean isConst()                       {  return type.isConst()                 ; }
  @Override public boolean isInline()                      {  return type.isInline()                ; }
  @Override public boolean isNoreturn()                    {  return type.isNoreturn()              ; }
  @Override public boolean isHasSignedness()               {  return type.isHasSignedness()         ; }
  @Override public boolean isUnsigned()                    {  return type.isUnsigned()              ; }
  @Override public boolean isSigned()                      {  return type.isSigned()                ; }
  //
  @Override public boolean isBool()            { return type.isBool()         ; }
  @Override public boolean isChar()            { return type.isChar()         ; }
  @Override public boolean isUchar()           { return type.isUchar()        ; }
  @Override public boolean isShort()           { return type.isShort()        ; }
  @Override public boolean isUshort()          { return type.isUshort()       ; }
  @Override public boolean isInt()             { return type.isInt()          ; }
  @Override public boolean isUint()            { return type.isUint()         ; }
  @Override public boolean isLong()            { return type.isLong()         ; }
  @Override public boolean isUlong()           { return type.isUlong()        ; }
  @Override public boolean isLongLong()        { return type.isLongLong()     ; }
  @Override public boolean isUlongLong()       { return type.isUlongLong()    ; }
  @Override public boolean isFloat()           { return type.isFloat()        ; }
  @Override public boolean isDouble()          { return type.isDouble()       ; }
  @Override public boolean isLongDouble()      { return type.isLongDouble()   ; }
  @Override public boolean isStrUnion()        { return type.isStrUnion()   ; }
  
  @Override public boolean isPointerToVoid()            { return type.isPointerToVoid()   ; }
  @Override public boolean isPointerToStructUnion()     { return type.isPointerToStructUnion()   ; }
  @Override public boolean isPointerToCompat(CType lhs) { return type.isPointerToCompat(lhs)   ; }
  //@formatter:on

  public CSymbolBase getBase() {
    return base;
  }

  public Token getFrom() {
    return from;
  }

}
