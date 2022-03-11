package ast.tree;

public class StmtDefault {

  public final StmtSwitch parent;
  public final Statement stmt;

  public StmtDefault(StmtSwitch parent, Statement stmt) {
    this.parent = parent;
    this.stmt = stmt;
  }

}
