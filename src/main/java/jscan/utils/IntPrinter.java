package jscan.utils;

public abstract class IntPrinter {

  // printf string in its binary form
  public static String b32(long n, int s, int octet_underscore) {
    final String fmt = "%" + String.format("%d", s) + "s";
    final String res = String.format(fmt, Long.toBinaryString(n)).replaceAll(" ", "0");

    if (octet_underscore <= 0) {
      return res;
    }

    final StringBuilder sb = new StringBuilder();
    final int length = res.length();
    for (int i = 0; i < length; i += 1) {
      char c = res.charAt(i);
      sb.append(c);

      if ((i + 1) % octet_underscore == 0 && (i + 1) < length) {
        sb.append("_");
      }
    }

    return sb.toString();
  }

}
