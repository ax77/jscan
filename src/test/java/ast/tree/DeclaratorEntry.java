package ast.tree;

import java.util.List;

import ast.types.CFuncParam;
import ast.types.CTypeKind;
import jscan.utils.NullChecker;

public class DeclaratorEntry {
  private final CTypeKind base;
  private List<CFuncParam> parameters;
  private int arrlen;
  private boolean isConstPointer;
  private boolean isVariadicFunction;

  public DeclaratorEntry(CTypeKind base) {
    NullChecker.check(base);
    this.base = base;
  }

  public List<CFuncParam> getParameters() {
    return parameters;
  }

  public void setParameters(List<CFuncParam> parameters) {
    this.parameters = parameters;
  }

  public CTypeKind getBase() {
    return base;
  }

  public boolean isConstPointer() {
    return isConstPointer;
  }

  public void setConstPointer(boolean isConstPointer) {
    this.isConstPointer = isConstPointer;
  }

  public boolean isVariadicFunction() {
    return isVariadicFunction;
  }

  public void setVariadicFunction(boolean isVariadicFunction) {
    this.isVariadicFunction = isVariadicFunction;
  }

  public void setArrayLen(int arrlen) {
    this.arrlen = arrlen;

  }

  public int getArrlen() {
    return arrlen;
  }

  public void setArrlen(int arrlen) {
    this.arrlen = arrlen;
  }

}