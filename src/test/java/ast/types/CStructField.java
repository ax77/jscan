package ast.types;

import jscan.symtab.Ident;
import ast.parse.NullChecker;

public class CStructField {
  private final Ident name;
  private final CType type;
  private int offset;

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

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public Ident getName() {
    return name;
  }

  public CType getType() {
    return type;
  }

  public boolean isHasName() {
    return name != null;
  }

  public boolean isBitfield() {
    return type.isBitfield();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (name != null) {
      sb.append("name=");
      sb.append(name.getName());
    }
    sb.append("; type=");
    sb.append(type.toString());
    return sb.toString();
  }

}