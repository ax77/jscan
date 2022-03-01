package ast.tree;

import jscan.symtab.Ident;

public class StmtLabel {
  private final FunctionDefinition function;
  private final Ident label;
  private final Statement stmt;

  public StmtLabel(FunctionDefinition function, Ident label, Statement stmt) {
    this.function = function;
    this.label = label;
    this.stmt = stmt;
  }

  public FunctionDefinition getFunction() {
    return function;
  }

  public Ident getLabel() {
    return label;
  }

  public Statement getStmt() {
    return stmt;
  }

}
