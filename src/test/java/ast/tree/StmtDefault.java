package ast.tree;

public class StmtDefault {

  private final StmtSwitch parent;
  private final Statement stmt;

  public StmtDefault(StmtSwitch parent, Statement stmt) {
    this.parent = parent;
    this.stmt = stmt;
  }

  public Statement getStmt() {
    return stmt;
  }

  public StmtSwitch getParent() {
    return parent;
  }

}
