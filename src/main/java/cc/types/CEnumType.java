package cc.types;

import jscan.symtab.Ident;

public class CEnumType {
  public Ident tag;
  public boolean isComplete;

  public CEnumType(Ident tag) {
    this.tag = tag;
  }

  public boolean hasTag() {
    return tag != null;
  }

  @Override
  public String toString() {
    return "(enum " + (hasTag() ? tag.getName() : "<no-tag>") + ")";
  }

}