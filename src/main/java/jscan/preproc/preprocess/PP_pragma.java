package jscan.preproc.preprocess;

import java.io.IOException;

import jscan.hashed.Hash_once;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class PP_pragma implements PP_directive {

  private final Scan scanner;

  public PP_pragma(Scan s) {
    this.scanner = s;
  }

  @Override
  public boolean scan(Token start) throws IOException {
    for (;;) {
      Token t = scanner.pop();

      if (t.is(T.TOKEN_IDENT) && t.isNewLine() && t.getValue().equals("once")) {
        Hash_once.push_once(t.getFilename());
      }

      if (t.isNewLine() || t.is(T.TOKEN_EOF)) {
        break;
      }

    }
    return true;
  }

}
