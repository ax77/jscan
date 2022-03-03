package _st3_linearize_expr.rvalues;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;

public class FieldAccess implements VarCollector {
  private final Var object;
  private final Var field;

  public FieldAccess(Var object, Var field) {
    this.object = object;
    this.field = field;
  }

  public Var getObject() {
    return object;
  }

  public Var getField() {
    return field;
  }

  @Override
  public String toString() {
    return object.toString() + "->" + field.toString();
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(object);
    vars.add(field);
    return vars;
  }

}
