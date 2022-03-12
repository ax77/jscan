package ast.symtab;

import ast.types.CType;
import jscan.symtab.Ident;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;

public class CSymbol {

  public final CSymbolBase base;
  public final Token pos;

  public CSymEnumConst enumConst;
  public CSymGlobalVar globalVar;
  public CSymLocalVar localVar;

  public CSymbol(CSymEnumConst enumConst, Token pos) {
    this.base = CSymbolBase.SYM_ENUM_CONST;
    this.enumConst = enumConst;
    this.pos = pos;
  }

  public CSymbol(CSymGlobalVar globalVar, Token pos) {
    this.base = CSymbolBase.SYM_GVAR;
    this.globalVar = globalVar;
    this.pos = pos;
  }

  public CSymbol(CSymLocalVar localVar, Token pos) {
    this.base = CSymbolBase.SYM_LVAR;
    this.localVar = localVar;
    this.pos = pos;
  }

  public CType getType() {
    if (base == CSymbolBase.SYM_ENUM_CONST) {
      return enumConst.type;
    }
    if (base == CSymbolBase.SYM_GVAR) {
      return globalVar.type;
    }
    if (base == CSymbolBase.SYM_LVAR) {
      return localVar.type;
    }
    throw new AstParseException("there's no type");
  }

  public String getNameStr() {
    Ident id = getNameId();
    if (id != null) {
      return id.getName();
    }
    return "???";
  }

  public Ident getNameId() {
    if (base == CSymbolBase.SYM_ENUM_CONST) {
      return enumConst.name;
    }
    if (base == CSymbolBase.SYM_GVAR) {
      return globalVar.name;
    }
    if (base == CSymbolBase.SYM_LVAR) {
      return localVar.name;
    }
    throw new AstParseException("there's no name");
  }

  public boolean isFunction() {
    return (base == CSymbolBase.SYM_GVAR || base == CSymbolBase.SYM_LVAR) && getType().isFunction();
  }

}
