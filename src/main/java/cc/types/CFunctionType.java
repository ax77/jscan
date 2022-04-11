package cc.types;

import java.util.List;

import jscan.utils.ArgsListToString;
import jscan.utils.NullChecker;

public class CFunctionType {

  public final CType returnType;
  public final List<CFuncParam> parameters;
  public final boolean isVariadic;

  public CFunctionType(CType type, List<CFuncParam> parameters, boolean isVariadic) {
    NullChecker.check(type, parameters);

    this.returnType = type;
    this.parameters = parameters;
    this.isVariadic = isVariadic;

  }

  public boolean isVariadic() {
    return isVariadic;
  }

  @Override
  public String toString() {
    final String params = parameters == null ? "void" : ArgsListToString.paramsToStringWithBraces(parameters, '(');
    return "fn(" + params + ") -> " + returnType.toString();
  }

}