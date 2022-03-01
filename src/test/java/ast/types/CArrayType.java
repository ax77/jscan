package ast.types;

import jscan.utils.AstParseException;
import jscan.utils.NullChecker;

public class CArrayType {
  private final CType arrayOf;
  private int arrayLen;
  private boolean isIncomplete;

  public CArrayType(CType arrayof, int arrayLen) {
    NullChecker.check(arrayof); // always must be present. but init-expression - optional, check it later. int x[][2];

    if (arrayLen < 0) {
      throw new AstParseException("negative sized array. TODO:");
    }

    if (arrayof.isBitfield()) {
      throw new AstParseException("error: array of bitfield.");
    }

    this.arrayOf = arrayof;
    this.arrayLen = arrayLen;
    this.isIncomplete = (arrayLen == 0);
  }

  public boolean isIncomplete() {
    return isIncomplete;
  }

  public void setIncomplete(boolean isIncomplete) {
    this.isIncomplete = isIncomplete;
  }

  public CType getArrayOf() {
    return arrayOf;
  }

  @Override
  public String toString() {
    return "ar(" + String.format("%d", arrayLen) + " " + arrayOf.toString() + ")";
  }

  public int getArrayLen() {
    return arrayLen;
  }

  public void setArrayLen(int arrayLen) {
    this.arrayLen = arrayLen;
  }

}