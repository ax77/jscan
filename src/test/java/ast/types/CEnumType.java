package ast.types;

import java.util.HashMap;
import java.util.Map;

import jscan.symtab.Ident;

public class CEnumType {
  public Ident tag;
  public boolean isComplete;
  public Map<Ident, Integer> enumerators; // need only for type-compatible routine.

  public CEnumType(Ident tag) {
    this.tag = tag;
    this.enumerators = new HashMap<Ident, Integer>();
  }

  public boolean hasTag() {
    return tag != null;
  }

  @Override
  public String toString() {
    return "(enum " + (hasTag() ? tag.getName() : "<no-tag>") + ": " + enumerators.size() + ")";
  }

}