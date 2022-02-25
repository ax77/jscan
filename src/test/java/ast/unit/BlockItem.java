package ast.unit;

import ast.decls.Declaration;
import ast.stmt.main.CStatement;

public class BlockItem {
  private Declaration declaration;
  private CStatement statement;

  public BlockItem() {
  }

  public BlockItem(Declaration declaration) {
    this.declaration = declaration;
  }

  public BlockItem(CStatement statement) {
    this.statement = statement;
  }

  public Declaration getDeclaration() {
    return declaration;
  }

  public void setDeclaration(Declaration declaration) {
    this.declaration = declaration;
  }

  public CStatement getStatement() {
    return statement;
  }

  public void setStatement(CStatement statement) {
    this.statement = statement;
  }

}
