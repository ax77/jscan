package ast.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.symtab.CSymbol;
import jscan.symtab.Ident;

public class FunctionDefinition {

  private final CSymbol symbol;
  private Statement block;

  // TODO: location for this two
  private final Set<Ident> gotos;
  private final Set<Ident> labels;
  private final List<CSymbol> locals;

  public FunctionDefinition(CSymbol symbol) {
    this.symbol = symbol;
    this.gotos = new HashSet<>();
    this.labels = new HashSet<>();
    this.locals = new ArrayList<>();
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

  public void addLocal(CSymbol sym) {
    locals.add(sym);
  }

  public List<CSymbol> getLocals() {
    return locals;
  }

}
