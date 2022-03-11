package ast.types;

import jscan.utils.NullChecker;

public class CPointerType {
  public final CType subtype;
  public final boolean isConst;

  public CPointerType(CType pointerTo, boolean isConst) {
    NullChecker.check(pointerTo);

    this.subtype = pointerTo;
    this.isConst = isConst;
  }

  @Override
  public String toString() {
    return "p(" + subtype.toString() + ")";
  }

}
