package jscan.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class BitmaskToString {

  // int flag = f1|f2|f5;
  // result ==>> "f1|f2|f5"
  //
  public static String getBitmaskToString(int flag, Map<Integer, String> fnames) {

    final List<Integer> mask = buildMask(fnames);
    StringBuilder res = new StringBuilder();

    for (int f : mask) {
      if ((f & flag) == f) {
        res.append(fnames.get(f));
        res.append("|");
      }
    }

    if (res.length() == 0) {
      return "";
    }

    final String tmp = res.toString();
    if (!tmp.endsWith("|")) {
      return tmp;
    }

    final String result = tmp.substring(0, res.length() - 1);
    return result;

  }

  // given TreeMap, where key is a bitflag, and value is a string representation of variable:
  // public static final int fleadws = 1 << 1;
  //
  // build list with integer part, and sort it.
  //
  private static List<Integer> buildMask(Map<Integer, String> fromNames) {
    List<Integer> r = new ArrayList<Integer>(0);
    for (Entry<Integer, String> e : fromNames.entrySet()) {
      r.add(e.getKey());
    }
    Collections.sort(r);
    return r;
  }

}
