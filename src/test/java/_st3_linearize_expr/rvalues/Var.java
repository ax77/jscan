package _st3_linearize_expr.rvalues;

import ast.types.CType;
import jscan.symtab.Ident;

public class Var implements Comparable<Var> {
  private static int oCounter = 0;

  private final CType type;
  private Ident name;
  private final int theOrderOfAppearance;

  /// a bit which indicates that this is
  /// an original varialbe, i.e. no temporary
  /// generated, and we can ignore any other
  /// var when we will generate destructors.
  /// 
  private boolean isOriginalNoTempVar;

  public void replaceName(Ident with) {
    this.name = with;
  }

  public Var(CType type, Ident name) {
    this.type = type;
    this.name = name;

    this.theOrderOfAppearance = oCounter;
    oCounter += 1;
  }

  @Override
  public String toString() {
    return name.getName();
  }

  public CType getType() {
    return type;
  }

  public Ident getName() {
    return name;
  }

  public int getTheOrderOfAppearance() {
    return theOrderOfAppearance;
  }

  public String typeNameToString() {
    if (isOriginalNoTempVar()) {
      return type.toString() + " " + name.getName();
    }
    return /*modsToString() +*/ type.toString() + " " + name.getName();
  }

  public boolean isOriginalNoTempVar() {
    return isOriginalNoTempVar;
  }

  public void setOriginalNoTempVar(boolean isOriginalNoTempVar) {
    this.isOriginalNoTempVar = isOriginalNoTempVar;
  }

  @Override
  public int compareTo(Var o) {
    if (theOrderOfAppearance < o.getTheOrderOfAppearance()) {
      return 1;
    }
    if (theOrderOfAppearance > o.getTheOrderOfAppearance()) {
      return -1;
    }
    return 0;
  }

}
