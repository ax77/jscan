package ast.tree;

public class ExternalDeclaration {
  public FunctionDefinition functionDefinition;
  public Declaration declaration;

  public ExternalDeclaration(FunctionDefinition functionDefinition) {
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
