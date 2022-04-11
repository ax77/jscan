package ast.tree;

import java.util.ArrayList;
import java.util.List;

import ast.tree.CSymbol.CSymFunction;

public class TranslationUnit {
  private List<ExternalDeclaration> externalDeclarations;

  public TranslationUnit() {
    this.externalDeclarations = new ArrayList<ExternalDeclaration>();
  }

  public List<ExternalDeclaration> getExternalDeclarations() {
    return externalDeclarations;
  }

  public void push(ExternalDeclaration ed) {
    externalDeclarations.add(ed);
  }

  // Nodes

  public static class ExternalDeclaration {
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

}
