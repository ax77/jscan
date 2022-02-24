package tokenize;

public class CChar {
  private final CStrEnc enc;
  private final int v;

  public CChar(int cval, CStrEnc escapePrefix) {
    v = cval;
    enc = escapePrefix;
  }

  public CStrEnc getEnc() {
    return enc;
  }

  public int getV() {
    return v;
  }

}
