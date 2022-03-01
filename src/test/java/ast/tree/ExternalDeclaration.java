package ast.tree;

public class ExternalDeclaration {
  private FunctionDefinition functionDefinition;
  private Declaration declaration;

  public ExternalDeclaration(FunctionDefinition functionDefinition) {
    this.functionDefinition = functionDefinition;
  }

  public ExternalDeclaration(Declaration declaration) {
    this.declaration = declaration;
  }

  public FunctionDefinition getFunctionDefinition() {
    return functionDefinition;
  }

  public void setFunctionDefinition(FunctionDefinition functionDefinition) {
    this.functionDefinition = functionDefinition;
  }

  public Declaration getDeclaration() {
    return declaration;
  }

  public void setDeclaration(Declaration declaration) {
    this.declaration = declaration;
  }

  public boolean isFunctionDefinition() {
    return functionDefinition != null;
  }

  public boolean isDeclaration() {
    return declaration != null;
  }

}
