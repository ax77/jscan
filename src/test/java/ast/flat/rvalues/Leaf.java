package ast.flat.rvalues;

import jscan.literals.IntLiteral;

public class Leaf {
  private Var var;
  private IntLiteral intLiteral;
  private Boolean boolLiteral;

  public Leaf(Var var) {
    this.var = var;
  }

  public Leaf(IntLiteral intLiteral) {
    this.intLiteral = intLiteral;
  }

  public Leaf(Boolean boolLiteral) {
    this.boolLiteral = boolLiteral;
  }

  public boolean isVar() {
    return var != null;
  }

  public boolean isBool() {
    return boolLiteral != null;
  }

  public boolean isIntLiteral() {
    return intLiteral != null;
  }

  public Var getVar() {
    return var;
  }

  public IntLiteral getIntLiteral() {
    return intLiteral;
  }

  public Boolean getBoolLiteral() {
    return boolLiteral;
  }

  @Override
  public String toString() {
    if (isVar()) {
      return var.toString();
    }
    if (isBool()) {
      return boolLiteral.toString();
    }
    if (isIntLiteral()) {
      return intLiteral.toString();
    }
    return "Leaf ?[ 0";
  }

}
