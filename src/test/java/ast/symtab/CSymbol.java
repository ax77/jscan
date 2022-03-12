package ast.symtab;

import ast.types.CType;
import jscan.symtab.Ident;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;

public class CSymbol {

  public final Token pos;

  public CSymEnumConst enumConst;
  public CSymGlobalVar globalVar;
  public CSymLocalVar localVar;

  public CSymbol(CSymEnumConst enumConst, Token pos) {
    this.enumConst = enumConst;
    this.pos = pos;
  }

  public CSymbol(CSymGlobalVar globalVar, Token pos) {
    this.globalVar = globalVar;
    this.pos = pos;
  }

  public CSymbol(CSymLocalVar localVar, Token pos) {
    this.localVar = localVar;
    this.pos = pos;
  }

  public CType getType() {
    if (isEnumConst()) {
      return enumConst.type;
    }
    if (isGlobalVar()) {
      return globalVar.type;
    }
    if (isLocalVar()) {
      return localVar.type;
    }
    throw new AstParseException("there's no type");
  }

  public boolean isLocalVar() {
    return localVar != null;
  }

  public boolean isGlobalVar() {
    return globalVar != null;
  }

  public boolean isEnumConst() {
    return enumConst != null;
  }

  public String getNameStr() {
    Ident id = getNameId();
    if (id != null) {
      return id.getName();
    }
    throw new AstParseException("there's no name");
  }

  public Ident getNameId() {
    if (isEnumConst()) {
      return enumConst.name;
    }
    if (isGlobalVar()) {
      return globalVar.name;
    }
    if (isLocalVar()) {
      return localVar.name;
    }
    throw new AstParseException("there's no name");
  }

  public boolean isFunction() {
    return getType().isFunction();
  }

}
