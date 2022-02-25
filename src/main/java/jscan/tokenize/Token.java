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
  private CChar charconstant;

  private boolean definedInreplList;

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
    type = T.TOKEN_EOF;
  }

  private void setDefaults() {
    value = "";
    type = T.TOKEN_ERROR;
    argnum = -1;
  }

  // source - location routine
  //

  public String loc() {
    if (location == null) {
      return "<unspec. source-location>"; // TODO:why, and when???
    }
    return location.getFilename() + ":" + location.getLine() + ":" + location.getColumn() + ": ";
  }

  public String getFilename() {
    if (location == null) {
      return "<unspec. source-location>"; // TODO:why, and when???
    }
    return location.getFilename();
  }

  public int getRow() {
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

  private void fillProperyValues(Token other) {

    // XXX: very important fill __ALL__ properties...
    // don't be clever here...

    this.fcategory = other.fcategory;
    this.type = other.type;
    this.ident = other.ident;
    this.value = other.value;
    this.argnum = other.argnum;
    this.strconstant = other.strconstant;
    this.charconstant = other.charconstant;
    this.fposition = other.fposition;
    this.definedInreplList = other.definedInreplList;
    this.location = new SourceLocation(other.location);
  }

  public int getArgnum() {
    return argnum;
  }

  public void setArgnum(int argnum) {
    this.argnum = argnum;
  }

  public boolean ofType(T _type) {
    return type.equals(_type);
  }

  // preprocessor - flags routine
  //

  public boolean isNewLine() {
    return (fposition & fnewline) == fnewline;
  }

  public void setNewLine(boolean isNewLine) {
    if (isNewLine) {
      fposition |= fnewline;
    } else {
      fposition &= ~fnewline;
    }
  }

  public boolean hasLeadingWhitespace() {
    return (fposition & fleadws) == fleadws;
  }

  public void setLeadingWhitespace(boolean b) {
    if (b) {
      fposition |= fleadws;
    } else {
      fposition &= ~fleadws;
    }
  }

  public boolean isAtBol() {
    return (fposition & fatbol) == fatbol;
  }

  public void setAtBol(boolean isAtBol) {
    if (isAtBol) {
      fposition |= fatbol;
    } else {
      fposition &= ~fatbol;
    }
  }

  public boolean isPainted() {
    return (fposition & fpainted) == fpainted;
  }

  public void paint() {
    fposition |= fpainted;
  }

  public int getCategory() {
    return fcategory;
  }

  public void setCategory(int category) {
    this.fcategory = category;
  }

  public boolean typeIsSpecialStreamMarks() {
    return this.ofType(T.TOKEN_EOF) || this.ofType(T.TOKEN_STREAMBEGIN) || this.ofType(T.TOKEN_STREAMEND);
  }

  public void checkId() {
    if (!ofType(T.TOKEN_IDENT)) {
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
    type = T.TOKEN_IDENT;
    value = ident.getName();
    this.ident = ident;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void set(T _type, String _value) {
    type = _type;
    value = _value;
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

  public CChar getCharconstant() {
    return charconstant;
  }

  public void setCharconstant(CChar charconstant) {
    this.charconstant = charconstant;
  }

  public boolean isDefinedInreplList() {
    return definedInreplList;
  }

  public void setDefinedInreplList(boolean definedInreplList) {
    this.definedInreplList = definedInreplList;
  }

  public boolean isIdent(Ident what) {
    if (!ofType(T.TOKEN_IDENT)) {
      return false;
    }
    return getIdent().equals(what);
  }

  public boolean isBuiltinIdent() {
    return ofType(T.TOKEN_IDENT) && (ident.getNs() != 0);
  }

  public String getLocationToString() {
    return loc();
  }

  public int getLine() {
    return location.getLine();
  }

}
