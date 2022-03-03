package _st3_linearize_expr.rvalues;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;
import _st3_linearize_expr.items.ArgsListToString;
import ast.tree.FunctionDefinition;
import ast.types.CType;

public class FunctionCallWithResult implements VarCollector {
  private final FunctionDefinition method;
  private final String fullname;
  private final CType type;
  private final List<Var> args;

  public FunctionCallWithResult(FunctionDefinition method, String fullname, CType type, List<Var> args) {
    this.method = method;
    this.fullname = fullname;
    this.type = type;
    this.args = args;
  }

  public CType getType() {
    return type;
  }

  public List<Var> getArgs() {
    return args;
  }

  public String getFullname() {
    return fullname;
  }

  public FunctionDefinition getMethod() {
    return method;
  }

  @Override
  public String toString() {
    return fullname + ArgsListToString.paramsToStringWithBraces(args, '(');
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.addAll(args);
    return vars;
  }

}
