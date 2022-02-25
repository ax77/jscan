package ast.unit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jscan.symtab.Ident;
import ast.stmt.main.CStatement;
import ast.symtab.elements.CSymbol;

public class FunctionDefinition {

  private final CSymbol symbol;
  private CStatement block;

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

  public CStatement getCompoundStatement() {
    return block;
  }

  public void setCompoundStatement(CStatement compoundStatement) {
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

  public CStatement getBlock() {
    return block;
  }

  public Set<Ident> getGotos() {
    return gotos;
  }

  public Set<Ident> getLabels() {
    return labels;
  }

}
