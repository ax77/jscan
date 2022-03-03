package ast.flat.items;

import java.util.ArrayList;
import java.util.List;

import ast.flat.VarCollector;
import ast.flat.rvalues.Unop;
import ast.flat.rvalues.Var;

public class AssignVarUnop implements VarCollector {
  private final Var lvalue;
  private final Unop rvalue;

  public AssignVarUnop(Var lvalue, Unop rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Unop getRvalue() {
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
