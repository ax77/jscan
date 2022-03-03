package ast.flat;

import ast.flat.rvalues.Var;
import ast.symtab.CSymbol;
import ast.types.CType;
import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;
import jscan.utils.GlobalCounter;

public abstract class VarCreator {

  private static Ident tmpIdent() {
    return Hash_ident.getHashedIdent(String.format("t%d", GlobalCounter.next()));
  }

  public static Var copyVarDecl(CSymbol src) {
    final Var result = new Var(src.getType(), src.getName());
    result.setOriginalNoTempVar(true);
    return result;
  }

  public static Var justNewVar(CType type) {
    final Var result = new Var(type, tmpIdent());
    return result;
  }

  public static Var varWithName(CType type, String name) {
    final Var result = new Var(type, Hash_ident.getHashedIdent(name));
    return result;
  }

}
