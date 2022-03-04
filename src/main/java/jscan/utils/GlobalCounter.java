package jscan.utils;

public class GlobalCounter {
  private static int count = 0;

  public static int next() {
    final int res = count;
    count += 1;
    return res;
  }

  public static String nextLabel(String pref) {
    int cnt = next();
    return String.format("%s%d", pref, cnt);
  }
}
