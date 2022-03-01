package ast.types;

import java.util.List;

import jscan.utils.NullChecker;

public class CFunctionType {

  private final CType returnType;
  private final List<CFuncParam> parameters;
  private final boolean isVariadic;

  public CFunctionType(CType type, List<CFuncParam> parameters, boolean isVariadic) {
    NullChecker.check(type, parameters);

    this.returnType = type;
    this.parameters = parameters;
    this.isVariadic = isVariadic;
  }

  public CType getReturnType() {
    return returnType;
  }

  public List<CFuncParam> getParameters() {
    return parameters;
  }

  public boolean isVariadic() {
    return isVariadic;
  }

  public int getArgc() {
    return parameters.size();
  }

  @Override
  public String toString() {
    return "fn(" + returnType.toString() + ")";
  }

}