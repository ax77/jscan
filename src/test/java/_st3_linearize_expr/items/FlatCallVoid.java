package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;
import _st3_linearize_expr.rvalues.Var;
import ast.tree.FunctionDefinition;

public class FlatCallVoid implements VarCollector {

  private final FunctionDefinition method;
  private final String fullname;
  private final List<Var> args;

  public FlatCallVoid(FunctionDefinition method, String fullname, List<Var> args) {
    this.method = method;
    this.fullname = fullname;
    this.args = args;
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
