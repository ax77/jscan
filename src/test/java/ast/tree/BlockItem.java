package ast.tree;

public class BlockItem {
  private Declaration declaration;
  private Statement statement;

  public BlockItem() {
  }

  public BlockItem(Declaration declaration) {
    this.declaration = declaration;
  }

  public BlockItem(Statement statement) {
    this.statement = statement;
  }

  public Declaration getDeclaration() {
    return declaration;
  }

  public void setDeclaration(Declaration declaration) {
    this.declaration = declaration;
  }

  public Statement getStatement() {
    return statement;
  }

  public void setStatement(Statement statement) {
    this.statement = statement;
  }

}
