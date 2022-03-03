package ast.flat.func;

import ast.flat.rvalues.Leaf;
import ast.flat.rvalues.Var;

public class Cmp {
  Var var;
  Leaf leaf;

  public Cmp(Var var, Leaf leaf) {
    this.var = var;
    this.leaf = leaf;
  }

  @Override
  public String toString() {
    return "cmp " + var.toString() + ", " + leaf.toString();
  }
}