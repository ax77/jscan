package ast.flat.items;

import java.util.ArrayList;
import java.util.List;

import ast.flat.VarCollector;
import ast.flat.rvalues.FunctionCallWithResult;
import ast.flat.rvalues.Var;

public class AssignVarFlatCallResult implements VarCollector {
  private final Var lvalue;
  private final FunctionCallWithResult rvalue;

  public AssignVarFlatCallResult(Var lvalue, FunctionCallWithResult rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public FunctionCallWithResult getRvalue() {
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
