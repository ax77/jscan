package ast.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jscan.preproc.preprocess.Scan;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import ast.errors.ParseException;

public class StrConcat {

  public List<Token> preprocessInput(List<Token> input) throws IOException {
    List<Token> clean = new ArrayList<Token>(0);
    Scan s = new Scan(input);

    List<Token> strings = new ArrayList<Token>(0);
    for (;;) {
      Token t = s.get();
      if (t.ofType(T.TOKEN_EOF)) {

        // maybe strings are before EOF
        if (!strings.isEmpty()) {
          clean.add(concatStrings(strings));
          strings = new ArrayList<Token>(0);
        }

        // add EOF marker, and go out.
        clean.add(t);
        break;
      }

      // collect all strings, this, and all after this...
      if (t.ofType(T.TOKEN_STRING)) {
        strings.add(t);
        continue;
      } else {

        // merge string-list, add new string token, and add current token
        if (!strings.isEmpty()) {
          clean.add(concatStrings(strings));
          strings = new ArrayList<Token>(0);

          clean.add(t);
          continue;
        }

        // there is no strings, just push current token
        clean.add(t);

      }
    }
    return clean;
  }

  private Token concatStrings(List<Token> strings) {
    if (strings.isEmpty()) {
      throw new ParseException("empty strings list");
    }

    StringBuilder sb = new StringBuilder();
    Token head = null;

    for (Token t : strings) {
      if (head == null) {
        head = t;
      }

      final String strvalue = t.getValue();
      boolean isStrOk = strvalue.startsWith("\"") && strvalue.endsWith("\"");
      if (!isStrOk) {
        throw new ParseException("error str-concat");
      }

      final String res = strvalue.substring(1, strvalue.length() - 1);
      sb.append(res);
    }

    // paranoia.
    if (head == null) {
      throw new ParseException("something wrong...");
    }

    Token r = new Token(head);
    r.setType(T.TOKEN_STRING);
    r.setValue("\"" + sb.toString() + "\"");
    return r;
  }

}
