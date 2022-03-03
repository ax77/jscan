package _st3_linearize_expr;

import java.util.List;

import _st3_linearize_expr.rvalues.Var;

public interface VarCollector {
  public List<Var> getAllVars();
}
