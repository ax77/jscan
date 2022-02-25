package ast.symtab.elements;

import jscan.tokenize.CStr;

public class StringConstant {
  private final CStr cstring;
  private final String original;

  public StringConstant(CStr cstring, String original) {
    this.cstring = cstring;
    this.original = original;
  }

  public CStr getCstring() {
    return cstring;
  }

  public String getOriginal() {
    return original;
  }

  @Override
  public String toString() {
    return "\"" + original + "\"";
  }

}
