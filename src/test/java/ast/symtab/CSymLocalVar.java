package ast.symtab;

import java.util.List;

import ast.tree.Initializer;
import ast.types.CType;
import jscan.symtab.Ident;

public class CSymLocalVar {

  // a plain local-variable, or a function-parameter

  public final Ident name;
  public final CType type;
  public List<Initializer> initializer;
  public int offset;
  public boolean isFparam;
  public int paramidx;

  public CSymLocalVar(Ident name, CType type, List<Initializer> initializer) {
    this.name = name;
    this.type = type;
    this.initializer = initializer;
  }

  public CSymLocalVar(Ident name, CType type) {
    this.name = name;
    this.type = type;
  }

}
