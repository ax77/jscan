package jscan.literals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
      char exponentSign) {
    this.originalInput = originalInput;
    this.mainSign = mainSign;
    this.dec = dec;
    this.mnt = mnt;
    this.exp = exp;
    this.suf = suf;
    this.exponentSign = exponentSign;
    this.typeBySuffix = suffix(suf);
  }

  public void setLong(long n) {
    this.longValue = (long) n;
    this.doubleValue = (double) n;
  }

  public void setDouble(double n) {
    this.longValue = (long) n;
    this.doubleValue = (double) n;
  }

  private IntLiteralType suffix(String suf) {
    Map<String, IntLiteralType> map = new HashMap<>();

    List<String> suffixes = new ArrayList<>();
    suffixes.add("U    @  U32 ");
    suffixes.add("L    @  I64 ");
    suffixes.add("LL   @  I64 ");
    suffixes.add("UL   @  U64 ");
    suffixes.add("LU   @  U64 ");
    suffixes.add("ULL  @  U64 ");
    suffixes.add("LLU  @  U64 ");
    suffixes.add("i8   @  I8  ");
    suffixes.add("u8   @  U8  ");
    suffixes.add("i16  @  I16 ");
    suffixes.add("u16  @  U16 ");
    suffixes.add("i32  @  I32 ");
    suffixes.add("u32  @  U32 ");
    suffixes.add("i64  @  I64 ");
    suffixes.add("u64  @  U64 ");
    suffixes.add("f32  @  F32 ");
    suffixes.add("f64  @  F64 ");

    for (String s : suffixes) {
      String splitten[] = s.split("@");
      String lhs = splitten[0].trim();
      String rhs = splitten[1].trim();
      map.put(lhs.toLowerCase(), IntLiteralType.valueOf(rhs));
      map.put(lhs.toUpperCase(), IntLiteralType.valueOf(rhs));
    }

    IntLiteralType res = map.get(suf);
    if (res == null) {
      return IntLiteralType.NO_SUFFIX;
    }
    return res;
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

  // A - evaluate as a decimal integer (bin, oct, dec, hex) -> 0b_1111_1111, 0o377, 255, 0xFF
  // f - evaluate as a floating (in its decimal form) -> 3.766280e-04, 3.14, .2003
  // F - evaluate as a floating (in its hex form) -> 0x1.e63674fa06bc9p+18
}