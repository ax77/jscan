package ast.symtab;

import java.util.List;

import ast.tree.Initializer;
import ast.types.CType;
import jscan.literals.IntLiteral;
import jscan.literals.IntLiteralParser;
import jscan.sourceloc.Location;
import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.Token;

public class CSymbol implements Location {
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

  public CSymbolBase getBase() {
    return base;
  }

  public Token getFrom() {
    return from;
  }

  @Override
  public SourceLocation getLocation() {
    return location;
  }

  @Override
  public String getLocationToString() {
    return location.toString();
  }

  @Override
  public Token getBeginPos() {
    return from;
  }

  public boolean isFunction() {
    return base == CSymbolBase.SYM_FUNC;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

}
