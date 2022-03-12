package ast.symtab;

import ast.types.CType;
import jscan.symtab.Ident;

public class CSymTypedef {
  public final Ident name;
  public final CType type;

  public CSymTypedef(Ident name, CType type) {
    this.name = name;
    this.type = type;
  }

}
