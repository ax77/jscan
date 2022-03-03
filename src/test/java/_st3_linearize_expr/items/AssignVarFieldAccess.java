package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;
import _st3_linearize_expr.rvalues.FieldAccess;
import _st3_linearize_expr.rvalues.Var;

public class AssignVarFieldAccess implements VarCollector {
  private final Var lvalue;
  private final FieldAccess rvalue;

  public AssignVarFieldAccess(Var lvalue, FieldAccess rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public FieldAccess getRvalue() {
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
    vars.addAll(rvalue.getAllVars());
    return vars;
  }
}
