package ast.types;

import jscan.utils.AstParseException;
import jscan.utils.NullChecker;

public class CArrayType {
  public final CType subtype;
  public int len;
  public boolean isIncomplete;

  public CArrayType(CType subtype, int len) {
    NullChecker.check(subtype); // always must be present. but init-expression - optional, check it later. int x[][2];

    if (len < 0) {
      throw new AstParseException("negative sized array. TODO:");
    }

    if (subtype.isBitfield()) {
      throw new AstParseException("error: array of bitfield.");
    }

    this.subtype = subtype;
    this.len = len;
    this.isIncomplete = (len == 0);
  }

  @Override
  public String toString() {
    return "ar(" + String.format("%d", len) + " " + subtype.toString() + ")";
  }

}