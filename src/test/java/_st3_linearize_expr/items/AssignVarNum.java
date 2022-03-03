package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;
import _st3_linearize_expr.rvalues.Var;
import jscan.literals.IntLiteral;

public class AssignVarNum implements VarCollector {
  private final Var lvalue;
  private final IntLiteral literal;

  public AssignVarNum(Var lvalue, IntLiteral literal) {
    this.lvalue = lvalue;
    this.literal = literal;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public IntLiteral getLiteral() {
    return literal;
  }

  @Override
  public String toString() {
    return "const " + lvalue.typeNameToString() + " = " + literal.toString();
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(lvalue);
    return vars;
  }

}
