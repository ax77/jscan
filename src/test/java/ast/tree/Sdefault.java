package ast.tree;

public class Sdefault {

  private final Sswitch parent;
  private final Statement stmt;

  public Sdefault(Sswitch parent, Statement stmt) {
    this.parent = parent;
    this.stmt = stmt;
  }

  public Statement getStmt() {
    return stmt;
  }

  public Sswitch getParent() {
    return parent;
  }

}
