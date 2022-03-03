package ast.flat.rvalues;

import java.util.ArrayList;
import java.util.List;

import ast.flat.VarCollector;

public class Binop implements VarCollector {
  private final Var lhs;
  private final String op;
  private final Var rhs;

  public Binop(Var lhs, String op, Var rhs) {
    this.lhs = lhs;
    this.op = op;
    this.rhs = rhs;
  }

  public Var getLhs() {
    return lhs;
  }

  public String getOp() {
    return op;
  }

  public Var getRhs() {
    return rhs;
  }

  @Override
  public String toString() {
    return "(" + lhs.toString() + " " + op + " " + rhs.toString() + ")";
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(lhs);
    vars.add(rhs);
    return vars;
  }

}
