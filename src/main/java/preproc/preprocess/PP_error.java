package preproc.preprocess;

import java.util.ArrayList;
import java.util.List;

import preproc.TokenPrint;
import tokenize.T;
import tokenize.Token;

public final class PP_error implements PP_directive {
  private final Scan scanner;

  public PP_error(Scan s) {
    scanner = s;
  }

  @Override
  public boolean scan(Token pp) {
    List<Token> errline = new ArrayList<Token>();
    for (;;) {
      Token t1 = scanner.pop();
      if (t1.ofType(T.TOKEN_EOF) || t1.isNewLine()) {
        errline.add(t1);
        break;
      }
      errline.add(t1);
    }
    throw new ScanExc(pp.loc() + " :error: " + TokenPrint.toStrLine(errline));
  }
}
