package ast.symtab;

import java.util.List;

import ast.tree.Initializer;
import ast.types.CType;
import jscan.sourceloc.Location;
import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.Token;

public class CSymbol implements Location {
  public final SourceLocation location;
  public final Token from;

  // these fields are the base of each symbol
  public final CSymbolBase base;
  public final Ident name;
  public CType type;

  // enum value, associated with the 'name'
  public int enumValue;

  // variable initializer
  public List<Initializer> initializer;

  // offset of a local variable
  public int offset;

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

  @Override
  public String toString() {
    String ret = String.format("off=%d, loc=%d, name=%s, type=%s, base=%s", offset, location.getLine(), name.getName(),
        type.toString(), base.toString());
    return ret;
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

}
