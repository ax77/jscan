package ast.tree;

public class StmtSelect {
  public final Expression condition;
  public final Statement ifStmt;
  public final Statement elseStmt;

  public StmtSelect(Expression condition, Statement ifStmt, Statement elseStmt) {
    this.condition = condition;
    this.ifStmt = ifStmt;
    this.elseStmt = elseStmt;
  }



}
