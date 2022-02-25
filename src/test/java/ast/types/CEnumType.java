package ast.types;

import java.util.HashMap;
import java.util.Map;

import jscan.symtab.Ident;

public class CEnumType {
  private Ident tag;
  private boolean isIncomplete;
  private Map<Ident, Integer> enumerators; // need only for type-compatible routine.

  public CEnumType(Ident tag) {
    this.tag = tag;
    this.isIncomplete = true;
    this.enumerators = new HashMap<Ident, Integer>();
  }

  public boolean isIncomplete() {
    return isIncomplete;
  }

  public void setIncomplete(boolean isIncomplete) {
    this.isIncomplete = isIncomplete;
  }

  public void setTag(Ident tag) {
    this.tag = tag;
  }

  public void setEnumerators(Map<Ident, Integer> enumerators) {
    this.enumerators = enumerators;
    this.isIncomplete = false;
  }

  public boolean isHasTag() {
    return tag != null;
  }

  public Ident getTag() {
    return tag;
  }

  public Map<Ident, Integer> getEnumerators() {
    return enumerators;
  }

  @Override
  public String toString() {
    return "(enum " + (isHasTag() ? tag.getName() : "<no-tag>") + ": " + enumerators.size() + ")";
  }

}