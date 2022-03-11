package ast.tree;

import java.util.ArrayList;
import java.util.List;

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

}
