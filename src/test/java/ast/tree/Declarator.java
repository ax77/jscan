package ast.tree;

import java.util.ArrayList;
import java.util.List;

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

}