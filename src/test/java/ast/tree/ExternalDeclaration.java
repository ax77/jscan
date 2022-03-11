package ast.tree;

public class ExternalDeclaration {
  public Function functionDefinition;
  public Declaration declaration;

  public ExternalDeclaration(Function functionDefinition) {
    this.functionDefinition = functionDefinition;
  }

  public ExternalDeclaration(Declaration declaration) {
    this.declaration = declaration;
  }

  public boolean isFunctionDefinition() {
    return functionDefinition != null;
  }

  public boolean isDeclaration() {
    return declaration != null;
  }

}
