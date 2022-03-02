package ast.tree;

public class BlockItem {
  private Declaration declaration;
  private Statement statement;

  public BlockItem(Declaration declaration) {
    this.declaration = declaration;
  }

  public BlockItem(Statement statement) {
    this.statement = statement;
  }

  public Declaration getDeclaration() {
    return declaration;
  }

  public Statement getStatement() {
    return statement;
  }

  public boolean isDeclaration() {
    return declaration != null;
  }

  public boolean isStatement() {
    return statement != null;
  }

}
