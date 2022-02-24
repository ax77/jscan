package tokenize;

import java.util.Map;
import java.util.TreeMap;

import utils.BitmaskToString;

public class Fcategory {

  public static final int formal = 1 << 0;
  public static final int scanned = 1 << 1;
  public static final int stringized = 1 << 2;
  public static final int unscanned = 1 << 3;
  public static final int hashhash = 1 << 4;
  public static final int commaopt = 1 << 5;

  private static final Map<Integer, String> fnames = new TreeMap<Integer, String>();
  static {
    fnames.put(formal, "formal");
    fnames.put(scanned, "scanned");
    fnames.put(stringized, "stringized");
    fnames.put(unscanned, "unscanned");
    fnames.put(hashhash, "hashhash");
    fnames.put(commaopt, "commaopt");
  }

  public static String print(int f) {
    return BitmaskToString.getBitmaskToString(f, fnames);
  }

}
