package jscan.preproc;

import java.util.List;

import jscan.sourceloc.SourceLocation;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public final class Sym {

  private final String name; // for debug only...
  private final List<Token> repl;
  private final List<Token> parm;
  private final boolean isVararg;
  private final int arity;
  private final List<Integer> usage;
  private boolean isHidden;

  private final SourceLocation location;

  private Token createVtok(Token macid) {
    Token unhide_ = new Token(macid);
    unhide_.setType(T.T_SPEC_UNHIDE);
    unhide_.setValue("@" + macid.getValue());
    unhide_.setLeadingWhitespace(false);
    unhide_.setNewLine(false);
    unhide_.setArgnum(-1);
    return unhide_;
  }

  private void checkDefinedInReplList() {
    for (Token t : repl) {
      if (!t.is(T.TOKEN_IDENT)) {
        continue;
      }
      if (t.getValue().equals("defined")) {
        t.setDefinedInreplList(true);
        Error.warning(ErrorCode.W_DEFINED_IN_REPLACEMENT_LIST, t.loc());
      }
    }
  }

  public Sym(Token macid, List<Token> _repl) {
    name = macid.getValue();

    // XXX
    _repl.add(createVtok(macid)); // TODO:check that not vtok here at now...
    repl = _repl;

    parm = null;
    isVararg = false;
    arity = -1;
    isHidden = false;
    usage = null;

    this.location = new SourceLocation(macid);
    checkDefinedInReplList();
  }

  public Sym(Token macid, List<Token> _repl, List<Token> _parm, boolean _var, List<Integer> _usage) {
    name = macid.getValue();

    // XXX
    _repl.add(createVtok(macid)); // TODO:check that not vtok here at now...
    repl = _repl;

    parm = _parm;
    isVararg = _var;
    arity = parm.size();
    isHidden = false;
    usage = _usage;

    this.location = new SourceLocation(macid);
    checkDefinedInReplList();
  }

  public int usage(int n) {
    if (n < 0 || n >= arity) {
      throw new IndexOutOfBoundsException();
    }
    return usage.get(n);
  }

  public boolean isHidden() {
    return isHidden;
  }

  public void hide() {
    if (isHidden) {
      throw new RuntimeException("Macros already hidden: " + name);
    }
    this.isHidden = true;
  }

  public void unhide() {
    if (!isHidden) {
      throw new RuntimeException("Macros already unhidden: " + name);
    }
    this.isHidden = false;
  }

  public List<Token> getRepl() {
    return repl;
  }

  public List<Token> getParm() {
    return parm;
  }

  public boolean isVararg() {
    return isVararg;
  }

  public int getArity() {
    return arity;
  }

  public boolean isObjectLike() {
    return arity == -1;
  }

  public boolean isFlike() {
    return arity >= 0;
  }

  @Override
  public String toString() {
    return "Sym [name=" + name + "\nrepl=" + repl + "\nparm=" + parm + ", arity=" + arity + "]";
  }

  public String buildRedefInfo() {
    StringBuilder sb = new StringBuilder();
    sb.append("#define " + getName());

    if (getArity() > 0) {
      if (getParm() != null) {
        sb.append("(");
        List<Token> parameters = getParm();
        for (int j = 0; j < parameters.size(); j++) {
          Token t = parameters.get(j);
          sb.append(t.getValue());
          if (j < parameters.size()) {
            sb.append(",");
          }
        }
        sb.append(")");
      } else {
        sb.append("(");
        sb.append(")");
      }
    }
    for (Token t : getRepl()) {
      if (t.hasLeadingWhitespace()) {
        sb.append(" ");
      }
      if (t.is(T.T_SPEC_UNHIDE)) {
        continue;
      }
      sb.append(t.getValue());
    }

    return sb.toString();
  }

  public boolean incorrectRedefined(Sym other) {

    // These definitions are effectively the same:

    //#define FOUR (2 + 2)
    //#define FOUR         (2    +    2)
    //#define FOUR (2 /* two */ + 2)

    // but these are not:

    //#define FOUR (2 + 2)
    //#define FOUR ( 2+2 )
    //#define FOUR (2 * 2)
    //#define FOUR(score,and,seven,years,ago) (2 + 2)

    if (other == null) {
      return false;
    }

    if (getArity() != other.getArity()) {
      return true;
    }

    if (getArity() > 0) {

      if (getParm().size() != other.getParm().size()) {
        return true;
      }

      List<Token> oldparm = getParm();
      List<Token> newparm = other.getParm();

      if (oldparm.size() > 0) {
        for (int j = 0; j < oldparm.size(); j++) {
          Token oldpt = oldparm.get(j);
          Token newpt = newparm.get(j);

          if (!oldpt.getType().equals(newpt.getType())) {
            return true;
          }

          if (!oldpt.getValue().equals(newpt.getValue())) {
            return true;
          }
        }
      }
    }

    List<Token> oldrepl = getRepl();
    List<Token> newrepl = other.getRepl();

    if (oldrepl.size() != newrepl.size()) {
      return true;
    }

    for (int j = 0; j < oldrepl.size(); j++) {
      Token oldrt = oldrepl.get(j);
      Token newrt = newrepl.get(j);

      if (!oldrt.getType().equals(newrt.getType())) {
        return true;
      }

      if (oldrt.getCategory() != newrt.getCategory()) {
        return true;
      }

      if (!oldrt.getValue().equals(newrt.getValue())) {
        return true;
      }

      if (oldrt.hasLeadingWhitespace() != newrt.hasLeadingWhitespace()) {
        return true;
      }

      if (oldrt.isNewLine() != newrt.isNewLine()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + arity;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    Sym other = (Sym) obj;
    if (arity != other.arity)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  public String getName() {
    return name;
  }

  public String getLocation() {
    return location.getFilename() + ":" + location.getLine() + ":";
  }

}
