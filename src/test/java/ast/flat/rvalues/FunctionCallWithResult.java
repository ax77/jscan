package ast.flat.rvalues;

import java.util.ArrayList;
import java.util.List;

import ast.flat.VarCollector;
import ast.tree.FunctionDefinition;
import ast.types.CType;
import jscan.utils.ArgsListToString;

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
