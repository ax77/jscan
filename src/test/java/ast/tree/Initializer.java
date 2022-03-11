package ast.tree;

import ast.types.CType;

public class Initializer implements Comparable<Initializer> {
  private final Expression init;
  private final int offset;
  private CType type;

  public Initializer(Expression init, CType type, int offset) {
    this.init = init;
    this.type = type;
    this.offset = offset;
  }

  public Initializer(Expression init, int offset) {
    this.init = init;
    this.offset = offset;
  }

  public Expression getInit() {
    return init;
  }

  public int getOffset() {
    return offset;
  }

  @Override
  public String toString() {
    return init.toString(); // String.format("%d=%s", offset, init)
  }

  @Override
  public int compareTo(Initializer o) {
    if (offset < o.getOffset()) {
      return -1;
    }
    if (offset > o.getOffset()) {
      return 1;
    }
    return 0;
  }
}