package ast.stmt;

import ast.stmt.main.CStatement;

public class Sdefault {

  private final Sswitch parent;
  private final CStatement stmt;

  public Sdefault(Sswitch parent, CStatement stmt) {
    this.parent = parent;
    this.stmt = stmt;
  }

  public CStatement getStmt() {
    return stmt;
  }

  public Sswitch getParent() {
    return parent;
  }

}
