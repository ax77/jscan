package ast.tree;

public class BlockItem {
  public Declaration declaration;
  public Statement statement;

  public BlockItem(Declaration declaration) {
    this.declaration = declaration;
  }

  public BlockItem(Statement statement) {
    this.statement = statement;
  }

  public boolean isDeclaration() {
    return declaration != null;
  }

  public boolean isStatement() {
    return statement != null;
  }

}
