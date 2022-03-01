package ast.tree;

public class Scase {

  private final Sswitch parent;
  private Expression constexpr;
  private Statement casestmt;

  public Scase(Sswitch parent, Expression constexpr) {
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

  public Sswitch getParent() {
    return parent;
  }

}
