package ast.symtab;

import java.util.List;

import ast.tree.Initializer;
import ast.types.CType;
import jscan.symtab.Ident;

public class CSymGlobalVar {

  public final Ident name;
  public final CType type;
  public List<Initializer> initializer;

  public CSymGlobalVar(Ident name, CType type, List<Initializer> initializer) {
    this.name = name;
    this.type = type;
    this.initializer = initializer;
  }

  public CSymGlobalVar(Ident name, CType type) {
    this.name = name;
    this.type = type;
  }

}
