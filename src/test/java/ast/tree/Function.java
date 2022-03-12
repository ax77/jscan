package ast.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.symtab.CSymLocalVar;
import ast.symtab.CSymbol;
import jscan.symtab.Ident;

public class Function {

  public final CSymbol symbol;
  public Statement block;
  public final Set<Ident> gotos;
  public final Set<Ident> labels;
  public final List<CSymLocalVar> locals;

  public Function(CSymbol symbol) {
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

  public void addLocal(CSymLocalVar sym) {
    locals.add(sym);
  }

}
