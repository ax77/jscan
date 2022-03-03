package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;
import _st3_linearize_expr.rvalues.Var;

public class AssignVarBool implements VarCollector {
  private final Var lvalue;
  private final String literal;

  public AssignVarBool(Var lvalue, String literal) {
    this.lvalue = lvalue;
    this.literal = literal;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public String getLiteral() {
    return literal;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + literal;
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(lvalue);
    return vars;
  }

}
