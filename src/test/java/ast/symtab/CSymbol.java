package ast.symtab;

import java.util.List;

import ast.tree.Initializer;
import ast.types.CType;
import jscan.sourceloc.Location;
import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.Token;

public class CSymbol implements Location {
  private final SourceLocation location;
  private final Token from;

  // these fields are the base of each symbol
  private final CSymbolBase base;
  private final Ident name;
  private final CType type;

  // enum value, associated with the 'name'
  private int enumValue;

  // variable initializer
  private List<Initializer> initializer;

  // offset of a local variable
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
    return enumValue;
  }

  public void setEnumvalue(int enumValue) {
    this.enumValue = enumValue;
  }

  @Override
  public String toString() {
    String ret = String.format("off=%d, loc=%d, name=%s, type=%s, base=%s", offset, location.getLine(), name.getName(),
        type.toString(), base.toString());
    return ret;
  }

  public List<Initializer> getInitializer() {
    return initializer;
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
