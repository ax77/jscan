package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;
import _st3_linearize_expr.rvalues.Var;

public class AssignVarVar implements VarCollector {
  private final Var lvalue;
  private final Var rvalue;

  public AssignVarVar(Var lvalue, Var rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Var getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(lvalue);
    vars.add(rvalue);
    return vars;
  }

}
