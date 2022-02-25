package ast.types.decl;

import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import ast.errors.ParseException;
import ast.parse.NullChecker;

public class CDecl {
  private Ident name;
  private List<CDeclEntry> typelist;

  public CDecl() {
    this.typelist = new ArrayList<CDeclEntry>(0);
  }

  public Ident getName() {
    if (isAstract()) {
      throw new ParseException("abstract declarator has no name.");
    }
    return name;
  }

  public void setName(Ident name) {
    NullChecker.check(name);
    this.name = name;
  }

  public List<CDeclEntry> getTypelist() {
    return typelist;
  }

  public void add(CDeclEntry type) {
    NullChecker.check(type);
    typelist.add(type);
  }

  public boolean isAstract() {
    return name == null;
  }

}