package ast.tree;

public class StmtWhileDo {

  public final Expression test;
  public final Statement loop;

  public StmtWhileDo(Expression test, Statement loop) {
    this.test = test;
    this.loop = loop;
  }

}
