package ast.tree;

public class StmtWhileDo {

  private final Expression test;
  private final Statement loop;

  public StmtWhileDo(Expression test, Statement loop) {
    this.test = test;
    this.loop = loop;
  }

  public Expression getTest() {
    return test;
  }

  public Statement getLoop() {
    return loop;
  }

}
