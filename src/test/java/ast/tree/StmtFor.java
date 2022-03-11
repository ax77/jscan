package ast.tree;

public class StmtFor {

  public final Declaration decl;
  public final Expression init;
  public final Expression test;
  public final Expression step;
  public final Statement loop;

  public StmtFor(Declaration decl, Expression init, Expression test, Expression step, Statement loop) {
    this.decl = decl;
    this.init = init;
    this.test = test;
    this.step = step;
    this.loop = loop;
  }

}
