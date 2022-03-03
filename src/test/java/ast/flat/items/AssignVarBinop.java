package ast.flat.items;

import java.util.ArrayList;
import java.util.List;

import ast.flat.VarCollector;
import ast.flat.rvalues.Binop;
import ast.flat.rvalues.Var;

public class AssignVarBinop implements VarCollector {
  private final Var lvalue;
  private final Binop rvalue;

  public AssignVarBinop(Var lvalue, Binop rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Binop getRvalue() {
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
