package ast.tree;

public class StmtFor {

  private final Declaration decl;
  private final Expression init;
  private final Expression test;
  private final Expression step;
  private final Statement loop;

  public StmtFor(Declaration decl, Expression init, Expression test, Expression step, Statement loop) {
    this.decl = decl;
    this.init = init;
    this.test = test;
    this.step = step;
    this.loop = loop;
  }

  public Declaration getDecl() {
    return decl;
  }

  public Expression getInit() {
    return init;
  }

  public Expression getTest() {
    return test;
  }

  public Expression getStep() {
    return step;
  }

  public Statement getLoop() {
    return loop;
  }

}
