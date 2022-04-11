package cc.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.types.CStorageKind;
import cc.types.CType;
import jscan.symtab.Ident;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;

public class CSymbol {

  public final CStorageKind storagespec;
  public final Token pos;

  public CSymEnumConst enumConst;
  public CSymGlobalVar globalVar;
  public CSymLocalVar localVar;
  public CSymFunction fn;

  public CSymbol(CStorageKind storagespec, Token pos, CSymFunction fn) {
    this.storagespec = storagespec;
    this.pos = pos;
    this.fn = fn;
  }

  public CSymbol(CStorageKind storagespec, Token pos, CSymEnumConst enumConst) {
    this.storagespec = storagespec;
    this.pos = pos;
    this.enumConst = enumConst;
  }

  public CSymbol(CStorageKind storagespec, Token pos, CSymGlobalVar globalVar) {
    this.storagespec = storagespec;
    this.pos = pos;
    this.globalVar = globalVar;
  }

  public CSymbol(CStorageKind storagespec, Token pos, CSymLocalVar localVar) {
    this.storagespec = storagespec;
    this.pos = pos;
    this.localVar = localVar;
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
    if (isFunc()) {
      return fn.type;
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

  public boolean isFunc() {
    return fn != null;
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
    if (isFunc()) {
      return fn.name;
    }
    throw new AstParseException("there's no name");
  }

  public boolean isFunction() {
    return getType().isFunction();
  }
  
  public String loc() {
    return pos.loc();
  }

  // Nodes

  public static class CSymTag {

    // a tagged struct/union

    public final Ident keyword; // struct/union/enum
    public final Ident tag;
    public CType type;

    public CSymTag(Ident keyword, Ident tag, CType type) {
      this.keyword = keyword;
      this.tag = tag;
      this.type = type;
    }

  }

  public static class CSymLocalVar {

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

  }

  public static class CSymGlobalVar {

    public final Ident name;
    public final CType type;
    public List<Initializer> initializer;

    public CSymGlobalVar(Ident name, CType type, List<Initializer> initializer) {
      this.name = name;
      this.type = type;
      this.initializer = initializer;
    }

  }

  public static class CSymFunction {

    public final Ident name;
    public final CType type;
    public Statement block;
    public final Set<Ident> gotos;
    public final Set<Ident> labels;
    public final List<CSymLocalVar> locals;

    public CSymFunction(Ident name, CType type) {
      this.name = name;
      this.type = type;
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

  public static class CSymEnumConst {

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

}
