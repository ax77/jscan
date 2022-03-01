package ast.tree;

public class StmtSelect {
  private final Expression condition;
  private final Statement ifStmt;
  private final Statement elseStmt;

  public StmtSelect(Expression condition, Statement ifStmt, Statement elseStmt) {
    this.condition = condition;
    this.ifStmt = ifStmt;
    this.elseStmt = elseStmt;
  }

  public Expression getCondition() {
    return condition;
  }

  public Statement getIfStmt() {
    return ifStmt;
  }

  public Statement getElseStmt() {
    return elseStmt;
  }

}
