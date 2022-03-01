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

  private List<CSymbol> locals;
  private int localsize;

  // TODO: location for this two
  private final Set<Ident> gotos;
  private final Set<Ident> labels;

  public FunctionDefinition(CSymbol symbol) {
    this.symbol = symbol;
    this.locals = new ArrayList<CSymbol>();

    this.gotos = new HashSet<Ident>();
    this.labels = new HashSet<Ident>();
  }

  public void addLocal(CSymbol e) {
    locals.add(e);
  }

  public void addGotos(Ident label) {
    gotos.add(label);
  }

  public void addLabel(Ident label) {
    labels.add(label);
  }

  public Statement getCompoundStatement() {
    return block;
  }

  public void setCompoundStatement(Statement compoundStatement) {
    this.block = compoundStatement;
  }

  public CSymbol getSignature() {
    return symbol;
  }

  public CSymbol getSymbol() {
    return symbol;
  }

  public List<CSymbol> getLocals() {
    return locals;
  }

  public int getLocalsize() {
    return localsize;
  }

  public void setLocalsize(int localsize) {
    this.localsize = localsize;
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
