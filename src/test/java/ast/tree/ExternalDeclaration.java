package ast.tree;

import ast.tree.CSymbol.CSymFunction;

public class ExternalDeclaration {
  public CSymFunction func;
  public Declaration declaration;

  public ExternalDeclaration(CSymFunction functionDefinition) {
    this.func = functionDefinition;
  }

  public ExternalDeclaration(Declaration declaration) {
    this.declaration = declaration;
  }

  public boolean isFunctionDefinition() {
    return func != null;
  }

  public boolean isDeclaration() {
    return declaration != null;
  }

}
