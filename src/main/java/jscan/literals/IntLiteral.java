package jscan.literals;

import java.io.Serializable;

public class IntLiteral implements Serializable {
  private static final long serialVersionUID = 7055604293623516324L;

  // the parsed content of the given input
  private final String originalInput;
  private final char mainSign;
  private final String dec;
  private final String mnt;
  private final String exp;
  private final String suf;
  private final char exponentSign;

  // the result of the evaluation, an immitation of the c-union
  private final IntLiteralType typeBySuffix;
  private long longValue;
  private double doubleValue;

  public IntLiteral(String originalInput, char mainSign, String dec, String mnt, String exp, String suf,
      char exponentSign, IntLiteralType typeBySuffix) {
    this.originalInput = originalInput;
    this.mainSign = mainSign;
    this.dec = dec;
    this.mnt = mnt;
    this.exp = exp;
    this.suf = suf;
    this.exponentSign = exponentSign;
    this.typeBySuffix = typeBySuffix;
  }

  public IntLiteral(long v, IntLiteralType t) {
    this.longValue = v;
    this.typeBySuffix = t;
    this.originalInput = String.format("%d", v);

    this.mainSign = '+';
    this.dec = this.originalInput;
    this.mnt = "";
    this.exp = "";
    this.suf = "";
    this.exponentSign = '\0';
  }

  public void setLong(long n) {
    this.longValue = (long) n;
    this.doubleValue = (double) n;
  }

  public void setDouble(double n) {
    this.longValue = (long) n;
    this.doubleValue = (double) n;
  }

  public String getOriginalInput() {
    return originalInput;
  }

  public char getMainSign() {
    return mainSign;
  }

  public String getDec() {
    return dec;
  }

  public String getMnt() {
    return mnt;
  }

  public String getExp() {
    return exp;
  }

  public String getSuf() {
    return suf;
  }

  public char getExponentSign() {
    return exponentSign;
  }

  public IntLiteralType getTypeBySuffix() {
    return typeBySuffix;
  }

  @Override
  public String toString() {
    return originalInput;
  }

  public long getClong() {
    return longValue;
  }

  public double getCdouble() {
    return doubleValue;
  }

  public float getCfloat() {
    return (float) doubleValue;
  }

  public boolean isInteger() {
    boolean isFloat = typeBySuffix == IntLiteralType.F32 || typeBySuffix == IntLiteralType.F64;
    return !isFloat;
  }

  // A - evaluate as a decimal integer (bin, oct, dec, hex) -> 0b_1111_1111, 0o377, 255, 0xFF
  // f - evaluate as a floating (in its decimal form) -> 3.766280e-04, 3.14, .2003
  // F - evaluate as a floating (in its hex form) -> 0x1.e63674fa06bc9p+18
}