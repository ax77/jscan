package ast.types;

import jscan.symtab.Ident;
import jscan.utils.NullChecker;

public class CFuncParam {
  private final Ident name;
  private CType type; // we apply the type, when build old-style function identifier-list+declarations

  public CFuncParam(Ident name, CType type) {
    NullChecker.check(name, type);
    this.name = name;
    this.type = genpointer(type);
  }

  // KnR identifier-list func-definition [int x(a,b,c) int a,b,c; {}]
  public CFuncParam(Ident name) {
    NullChecker.check(name);
    this.name = name;
    this.type = null;
  }

  // abstract func-declaration [int x(char*, int*);]
  public CFuncParam(CType type) {
    NullChecker.check(type);
    this.name = null;
    this.type = genpointer(type);
  }

  private CType genpointer(CType from) {
    if (from.isArray()) {
      CPointerType ptr = new CPointerType(from.getTpArray().getArrayOf(), false);
      return new CType(ptr);
    }
    return from;
  }

  public Ident getName() {
    return name;
  }

  public CType getType() {
    return type;
  }

  public boolean isHasName() {
    return name != null;
  }

  public boolean isHasType() {
    return type != null;
  }

  public void setType(CType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    String stype = "";
    String sname = "";
    if (type != null) {
      stype = type.toString();
    }
    if (name != null) {
      sname = name.getName();
    }
    final String result = stype + " " + sname;
    return result.trim();
  }

}