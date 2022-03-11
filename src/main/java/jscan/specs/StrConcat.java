package jscan.specs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;

public class StrConcat {

  public List<Token> preprocessInput(List<Token> input) throws IOException {

    final List<Token> clean = new ArrayList<Token>(0);

    final LinkedList<Token> buffer = new LinkedList<>();
    for (Token tok : input) {
      buffer.addLast(tok);
    }

    List<Token> strings = new ArrayList<Token>(0);
    while (!buffer.isEmpty()) {
      Token t = buffer.removeFirst();

      if (t.is(T.TOKEN_EOF)) {

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
      if (t.is(T.TOKEN_STRING)) {
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
      throw new AstParseException("empty strings list");
    }

    StringBuilder sb = new StringBuilder();
    Token head = null;

    for (Token t : strings) {
      if (head == null) {
        head = t;
      }
      sb.append(t.getValue());
    }

    // paranoia.
    if (head == null) {
      throw new AstParseException("something wrong...");
    }

    return new Token(head, sb.toString(), T.TOKEN_STRING);
  }

}
