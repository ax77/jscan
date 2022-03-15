package ast.types;

import jscan.symtab.Ident;
import jscan.utils.NullChecker;

public class CStructField {
  public final Ident name;
  public final CType type;
  public int offset;
  public int pos;

  //TODO:later
  //plain field: but anonymous struct/union, or unnamed bitfield.
  public CStructField(CType type) {
    NullChecker.check(type);

    this.name = null;
    this.type = type;
  }

  //plain field: normal
  public CStructField(Ident name, CType type) {
    NullChecker.check(name, type);

    this.name = name;
    this.type = type;
  }

  public boolean hasName() {
    return name != null;
  }

  public boolean isBitfield() {
    return type.isBitfield();
  }

  @Override
  public String toString() {
    String t = (type == null) ? "" : type.toString();
    String n = (name == null) ? "" : name.getName();
    return String.format("%s %s", t, n);
  }

}