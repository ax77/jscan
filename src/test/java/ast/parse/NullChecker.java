package ast.parse;

import ast.errors.ParseException;

public abstract class NullChecker {

  public static void check(Object... what) {
    for (Object o : what) {
      if (o == null) {
        throw new ParseException("non-nullable property was null...");
      }
    }
  }

}
