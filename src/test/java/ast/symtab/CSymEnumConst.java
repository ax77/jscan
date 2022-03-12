package ast.symtab;

import ast.types.CType;
import jscan.symtab.Ident;

public class CSymEnumConst {

  // a single enumerator with its value, type-is the type of the enum itself.

  public final Ident name;
  public final CType type;
  public final int value;

  public CSymEnumConst(Ident name, CType type, int value) {
    this.name = name;
    this.type = type;
    this.value = value;
  }

}
