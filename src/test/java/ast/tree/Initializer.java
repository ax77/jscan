package ast.tree;

import ast.types.CType;

public class Initializer implements Comparable<Initializer> {
  public final Expression init;
  public final int offset;
  public final CType type;

  public Initializer(Expression init, CType type, int offset) {
    this.init = init;
    this.type = type;
    this.offset = offset;
  }

  @Override
  public String toString() {
    return String.format("%-8d = %s", offset, init);
  }

  @Override
  public int compareTo(Initializer o) {
    if (offset < o.offset) {
      return -1;
    }
    if (offset > o.offset) {
      return 1;
    }
    return 0;
  }
}