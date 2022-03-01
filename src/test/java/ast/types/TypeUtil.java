package ast.types;

import jscan.utils.AstParseException;

public abstract class TypeUtil {
  public static int align(int value, int alignment) {
    if (alignment <= 0) {
      throw new AstParseException("negative or zero alignment.");
    }
    int mod = value % alignment;
    if (mod != 0) {
      return value + alignment - mod;
    }
    return value;
  }
}
