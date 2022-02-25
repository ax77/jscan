package jscan.preproc.preprocess;

import java.io.IOException;

import jscan.tokenize.T;
import jscan.tokenize.Token;

public class PP_include_next implements PP_directive {

  private final Scan scanner;

  public PP_include_next(Scan s) {
    this.scanner = s;
  }

  @Override
  public boolean scan(Token start) throws IOException {
    for (;;) {
      Token t = scanner.pop();
      if (t.isNewLine() || t.ofType(T.TOKEN_EOF)) {
        break;
      }
    }
    return true;
  }

}
