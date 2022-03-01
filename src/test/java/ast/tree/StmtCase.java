package ast.tree;

public class StmtCase {

  private final StmtSwitch parent;
  private Expression constexpr;
  private Statement casestmt;

  public StmtCase(StmtSwitch parent, Expression constexpr) {
    this.parent = parent;
    this.constexpr = constexpr;
  }

  public Expression getConstexpr() {
    return constexpr;
  }

  public void setConstexpr(Expression constexpr) {
    this.constexpr = constexpr;
  }

  public Statement getCasestmt() {
    return casestmt;
  }

  public void setCasestmt(Statement casestmt) {
    this.casestmt = casestmt;
  }

  public StmtSwitch getParent() {
    return parent;
  }

}
