package hashed;

import java.util.HashMap;
import java.util.Map;

public class Hash_strings {
  private static Map<String, String> strtab = new HashMap<String, String>();

  public static String getHashedString(String what) {
    if (!strtab.containsKey(what)) {
      strtab.put(what, what);
      return what;
    }
    return strtab.get(what);
  }

  public static void clear() {
    strtab.clear();
  }

  public static void dump() {
    System.out.println("\nHASHED_STRINGS:");
    for (Map.Entry<String, String> entry : strtab.entrySet()) {
      System.out.println(entry.getValue().toString());
    }
  }

}
