package jscan.tokenize;

import static jscan.tokenize.Fposition.fatbol;
import static jscan.tokenize.Fposition.fleadws;
import static jscan.tokenize.Fposition.fnewline;
import static jscan.tokenize.Fposition.fpainted;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;

public class Token {

  private int fcategory;
  private int fposition;
  private T type;
  private Ident ident;
  private String value;
  private int argnum;
  private SourceLocation location;
  private CStr strconstant;
  private boolean definedInreplList;

  private void fillProperyValues(Token other) {

    // XXX: very important fill __ALL__ properties...
    // don't be clever here...

    this.fcategory = other.fcategory;
    this.fposition = other.fposition;
    this.type = other.type;
    this.ident = other.ident;
    this.value = other.value;
    this.argnum = other.argnum;
    this.location = other.location;
    this.strconstant = other.strconstant;
    this.definedInreplList = other.definedInreplList;
  }

  public Token() {
    setDefaults();
  }

  public Token(Token other) {
    fillProperyValues(other);
  }

  public Token(Token other, String value, T type) {
    fillProperyValues(other);
    this.value = value;
    this.type = type;
  }

  public Token(boolean eof) {
    setDefaults();
    this.type = T.TOKEN_EOF;
  }

  private void setDefaults() {
    this.value = "";
    this.type = T.TOKEN_ERROR;
    this.argnum = -1;
  }

  // source - location routine
  //

  public String loc() {
    if (location == null) {
      return "<unspec. source-location>"; // TODO:why, and when???
    }
    return location.toString();
  }

  public String getFilename() {
    if (location == null) {
      return "<unspec. source-location>"; // TODO:why, and when???
    }
    return location.getFilename();
  }

  public int getLine() {
    if (location == null) {
      return -1; // TODO:why, and when???
    }
    return location.getLine();
  }

  public int getColumn() {
    if (location == null) {
      return -1; // TODO:why, and when???
    }
    return location.getColumn();
  }

  public SourceLocation getLocation() {
    return location;
  }

  public void setLocation(SourceLocation location) {
    this.location = location;
  }

  public int getArgnum() {
    return argnum;
  }

  public void setArgnum(int argnum) {
    this.argnum = argnum;
  }

  public boolean is(T t) {
    return type.equals(t);
  }

  // preprocessor - flags routine
  //

  public boolean isNewLine() {
    return (fposition & fnewline) == fnewline;
  }

  public void setNewLine(boolean isNewLine) {
    if (isNewLine) {
      this.fposition |= fnewline;
    } else {
      this.fposition &= ~fnewline;
    }
  }

  public boolean hasLeadingWhitespace() {
    return (fposition & fleadws) == fleadws;
  }

  public void setLeadingWhitespace(boolean b) {
    if (b) {
      this.fposition |= fleadws;
    } else {
      this.fposition &= ~fleadws;
    }
  }

  public boolean isAtBol() {
    return (this.fposition & fatbol) == fatbol;
  }

  public void setAtBol(boolean isAtBol) {
    if (isAtBol) {
      this.fposition |= fatbol;
    } else {
      this.fposition &= ~fatbol;
    }
  }

  public boolean isPainted() {
    return (this.fposition & fpainted) == fpainted;
  }

  public void setNoexpand() {
    this.fposition |= fpainted;
  }

  public int getCategory() {
    return this.fcategory;
  }

  public void setCategory(int category) {
    this.fcategory = category;
  }

  public boolean typeIsSpecialStreamMarks() {
    return this.is(T.TOKEN_EOF) || this.is(T.TOKEN_STREAMBEGIN) || this.is(T.TOKEN_STREAMEND);
  }

  public void checkId() {
    if (!is(T.TOKEN_IDENT)) {
      throw new RuntimeException(loc() + " error : expect id, but was: " + getValue());
    }
  }

  public T getType() {
    return type;
  }

  public void setType(T type) {
    this.type = type;
  }

  public Ident getIdent() {
    return ident;
  }

  public void setIdent(Ident ident) {
    this.type = T.TOKEN_IDENT;
    this.value = ident.getName();
    this.ident = ident;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void set(T type, String value) {
    this.type = type;
    this.value = value;
  }

  @Override
  public String toString() {

    String cat = "";
    String catflags = Fcategory.print(fcategory);
    if (!catflags.isEmpty()) {
      cat = "; fcat=" + catflags;
    }

    String ppf = "";
    String pflags = Fposition.print(fposition);
    if (!pflags.isEmpty()) {
      ppf = "; fpos=" + pflags;
    }

    String arg = "";
    if (argnum != -1) {
      arg = "; argn=" + getArgnum();
    }

    return "\nToken [v=" + value + "; tp=" + type + cat + arg + ppf + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((ident == null) ? 0 : ident.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Token other = (Token) obj;
    if (ident == null) {
      if (other.ident != null)
        return false;
    } else if (!ident.equals(other.ident))
      return false;
    if (type != other.type)
      return false;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

  public CStr getStrconstant() {
    return strconstant;
  }

  public void setStrconstant(CStr strconstant) {
    this.strconstant = strconstant;
  }

  public boolean isDefinedInreplList() {
    return definedInreplList;
  }

  public void setDefinedInreplList(boolean definedInreplList) {
    this.definedInreplList = definedInreplList;
  }

  public boolean isIdent(Ident what) {
    if (!is(T.TOKEN_IDENT)) {
      return false;
    }
    return getIdent().equals(what);
  }

  public boolean isBuiltinIdent() {
    return is(T.TOKEN_IDENT) && (ident.getNs() != 0);
  }

}
