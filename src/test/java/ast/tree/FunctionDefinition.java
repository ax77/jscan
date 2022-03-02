package ast.tree;

import java.util.HashSet;
import java.util.Set;

import ast.symtab.CSymbol;
import jscan.symtab.Ident;

public class FunctionDefinition {

  private final CSymbol symbol;
  private Statement block;

  // TODO: location for this two
  private final Set<Ident> gotos;
  private final Set<Ident> labels;

  public FunctionDefinition(CSymbol symbol) {
    this.symbol = symbol;
    this.gotos = new HashSet<Ident>();
    this.labels = new HashSet<Ident>();
  }

  public void addGotos(Ident label) {
    gotos.add(label);
  }

  public void addLabel(Ident label) {
    labels.add(label);
  }

  public void setBlock(Statement compoundStatement) {
    this.block = compoundStatement;
  }

  public CSymbol getSignature() {
    return symbol;
  }

  public Statement getBlock() {
    return block;
  }

  public Set<Ident> getGotos() {
    return gotos;
  }

  public Set<Ident> getLabels() {
    return labels;
  }

}
