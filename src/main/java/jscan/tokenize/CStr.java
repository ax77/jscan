package jscan.tokenize;

public class CStr {

  private final String original;
  private final CStrEnc enc;
  private final int v[];

  public CStr(String original, int buffer[], CStrEnc escapePrefix) {
    this.original = original;
    this.v = buffer;
    this.enc = escapePrefix;
  }

  public CStrEnc getEnc() {
    return enc;
  }

  public int[] getV() {
    return v;
  }

  public String getOriginal() {
    return original;
  }

  @Override
  public String toString() {
    return original;
  }

}
