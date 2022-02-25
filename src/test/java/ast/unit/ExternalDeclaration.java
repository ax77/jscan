package ast.unit;

import ast.decls.Declaration;

public class ExternalDeclaration {
  private final boolean isFunctionDefinition;
  private FunctionDefinition functionDefinition;
  private Declaration declaration;

  public ExternalDeclaration(FunctionDefinition functionDefinition) {
    this.isFunctionDefinition = true;
    this.functionDefinition = functionDefinition;
  }

  public ExternalDeclaration(Declaration declaration) {
    this.isFunctionDefinition = false;
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
    return isFunctionDefinition;
  }

  public boolean isDeclaration() {
    return !isFunctionDefinition;
  }

}
