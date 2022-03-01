package ast.types;

import jscan.utils.NullChecker;

public class CPointerType {
  private final CType pointerTo;
  private final boolean isConst;

  public CPointerType(CType pointerTo, boolean isConst) {
    NullChecker.check(pointerTo);

    this.pointerTo = pointerTo;
    this.isConst = isConst;
  }

  public CType getPointerTo() {
    return pointerTo;
  }

  public boolean isConst() {
    return isConst;
  }

  @Override
  public String toString() {
    return "p(" + pointerTo.toString() + ")";
  }

}
