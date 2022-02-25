package jscan.preproc.preprocess;

import java.util.ArrayList;
import java.util.List;

import jscan.preproc.TokenPrint;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public final class PP_warning implements PP_directive {
  private final Scan scanner;

  public PP_warning(Scan s) {
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
    System.out.println(pp.loc() + " :warning: " + TokenPrint.toStrLine(errline));
    return true;
  }
}
