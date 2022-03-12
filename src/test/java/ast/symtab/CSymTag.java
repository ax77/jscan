package ast.symtab;

import ast.types.CType;
import jscan.symtab.Ident;

public class CSymTag {

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
