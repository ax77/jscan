package tokenize;

import java.util.Map;
import java.util.TreeMap;

import utils.BitmaskToString;

public class Fposition {
  public static final int fnewline = 1 << 0;
  public static final int fleadws = 1 << 1;
  public static final int fatbol = 1 << 2;
  public static final int fpainted = 1 << 3;

  private static final Map<Integer, String> fnames = new TreeMap<Integer, String>();
  static {
    fnames.put(fnewline, "fnewline");
    fnames.put(fleadws, "fleadws");
    fnames.put(fatbol, "fatbol");
    fnames.put(fpainted, "fpainted");
  }

  public static String print(int flag) {
    return BitmaskToString.getBitmaskToString(flag, fnames);
  }

}
