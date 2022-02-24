package tokenize;

public class CStr {

  private final CStrEnc enc;
  private final int v[];
  private final int len;

  public CStr(int buffer[], CStrEnc escapePrefix) {
    v = buffer;
    enc = escapePrefix;
    len = v.length;
  }

  public CStrEnc getEnc() {
    return enc;
  }

  public int[] getV() {
    return v;
  }

  public int getLen() {
    return len;
  }

}
