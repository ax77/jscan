package ast.tree;

public class StmtCase {

  public final StmtSwitch parent;
  public Expression constexpr;
  public Statement casestmt;

  public StmtCase(StmtSwitch parent, Expression constexpr) {
    this.parent = parent;
    this.constexpr = constexpr;
  }

}
