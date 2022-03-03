package _st3_linearize_expr.rvalues;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;

public class Unop implements VarCollector {
  private final String op;
  private final Var operand;

  public Unop(String op, Var operand) {
    this.op = op;
    this.operand = operand;
  }

  public String getOp() {
    return op;
  }

  public Var getOperand() {
    return operand;
  }

  @Override
  public String toString() {
    return op + operand.toString();
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(operand);
    return vars;
  }

}
