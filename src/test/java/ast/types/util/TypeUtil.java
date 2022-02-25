package ast.types.util;

import ast.errors.ParseException;

public abstract class TypeUtil {
  public static int align(int value, int alignment) {
    if (alignment <= 0) {
      throw new ParseException("negative or zero alignment.");
    }
    int mod = value % alignment;
    if (mod != 0) {
      return value + alignment - mod;
    }
    return value;
  }
}
