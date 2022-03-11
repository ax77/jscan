package ast.symtab;

import java.util.List;

import ast.tree.Initializer;
import ast.types.CType;
import jscan.sourceloc.Location;
import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.Token;

public class CSymbol implements Location {
  public final Token pos;

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

  public CSymbol(CSymbolBase base, Ident name, CType type, Token pos) {
    this.pos = pos;
    this.base = base;
    this.name = name;
    this.type = type;
  }

  public CSymbol(CSymbolBase base, Ident name, CType type, List<Initializer> initializer, Token pos) {
    this.pos = pos;
    this.base = base;
    this.name = name;
    this.type = type;
    this.initializer = initializer;
  }

  @Override
  public SourceLocation getLocation() {
    return pos.getLocation();
  }

  @Override
  public String getLocationToString() {
    return pos.loc();
  }

  @Override
  public Token getBeginPos() {
    return pos;
  }

  public boolean isFunction() {
    return base == CSymbolBase.SYM_FUNC;
  }

  @Override
  public String toString() {
    String ret = String.format("off=%d, loc=%d, name=%s, type=%s, base=%s", offset, pos.getLine(), name.getName(),
        type.toString(), base.toString());
    return ret;
  }

}
