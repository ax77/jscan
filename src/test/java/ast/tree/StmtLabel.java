package ast.tree;

import jscan.symtab.Ident;

public class StmtLabel {
  public final FunctionDefinition function;
  public final Ident label;
  public final Statement stmt;

  public StmtLabel(FunctionDefinition function, Ident label, Statement stmt) {
    this.function = function;
    this.label = label;
    this.stmt = stmt;
  }

}
