package cc.tree;

import java.util.ArrayList;
import java.util.List;

import cc.types.CFuncParam;
import cc.types.CTypeKind;
import jscan.symtab.Ident;
import jscan.utils.AstParseException;
import jscan.utils.NullChecker;

public class Declarator {
  private Ident name;
  private List<DeclaratorEntry> typelist;

  public Declarator() {
    this.typelist = new ArrayList<>();
  }

  public Ident getName() {
    if (isAbstract()) {
      throw new AstParseException("abstract declarator has no name.");
    }
    return name;
  }

  public void setName(Ident name) {
    NullChecker.check(name);
    this.name = name;
  }

  public List<DeclaratorEntry> getTypelist() {
    return typelist;
  }

  public void add(DeclaratorEntry type) {
    NullChecker.check(type);
    typelist.add(type);
  }

  public boolean isAbstract() {
    return name == null;
  }

  public static class DeclaratorEntry {
    public final CTypeKind base;
    public List<CFuncParam> parameters;
    public int arrlen;
    public boolean isConstPointer;
    public boolean isVariadicFunction;

    public DeclaratorEntry(CTypeKind base) {
      NullChecker.check(base);
      this.base = base;
    }

  }

}