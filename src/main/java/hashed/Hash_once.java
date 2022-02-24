package hashed;

import java.util.HashSet;
import java.util.Set;

public class Hash_once {
  private static Set<String> once = new HashSet<String>();

  public static void push_once(String filename) {
    once.add(filename);
  }

  public static boolean is_once(String filename) {
    return once.contains(filename);
  }

  public static void clear() {
    once.clear();
  }

}
