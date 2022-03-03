package ast.flat;

import java.util.List;

import ast.flat.rvalues.Var;

public interface VarCollector {
  public List<Var> getAllVars();
}
