package preproc.preprocess;

import tokenize.Token;

public final class PP_undef implements PP_directive {
  private final Scan scanner;

  public PP_undef(Scan s) {
    scanner = s;
  }

  @Override
  public boolean scan(Token pp) {
    Token macid = scanner.pop();
    macid.checkId();

    macid.getIdent().undefSym();
    return true;
  }
}
