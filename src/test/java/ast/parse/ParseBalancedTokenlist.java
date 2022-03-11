package ast.parse;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseBalancedTokenlist {

  private final Parse parser;

  public ParseBalancedTokenlist(Parse parser) {
    this.parser = parser;
  }

  public List<Token> parse(T open, T close) {

    List<Token> result = new ArrayList<Token>();
    String startLoc = parser.getLastLoc();

    int nesting = 0;

    for (;;) {

      if (parser.tp() == T.TOKEN_EOF) {
        parser.perror("unclosed balanced-tokenlist: " + startLoc);
      }

      if (parser.tp() == open) {
        ++nesting;
      }
      if (parser.tp() == close) {
        if (--nesting == 0) {
          result.add(parser.moveget());
          break;
        }
      }

      result.add(parser.moveget());

    }

    return result;
  }

  // remove leading and trailing balanced braces
  // 
  // __attribute__((...))
  // [[ ... ]]

  public void checkRemoveParens(List<Token> where, T open, T close, int count) {

    if (count == 1) {

      if (where.size() < 2) {
        parser.perror("balanced-tokenlist incomplete");
      } else {
        checkOf(where.get(0), open);
        checkOf(where.get(where.size() - 1), close);

        where.remove(0);
        where.remove(where.size() - 1);
      }

    }

    else if (count == 2) {

      if (where.size() < 4) {
        parser.perror("balanced-tokenlist incomplete");
      } else {
        checkOf(where.get(0), open);
        checkOf(where.get(1), open);
        checkOf(where.get(where.size() - 1), close);
        checkOf(where.get(where.size() - 2), close);

        where.remove(0);
        where.remove(0);
        where.remove(where.size() - 1);
        where.remove(where.size() - 1);
      }

    } else {
      parser.perror("internal error: you want remove too many braces.");
    }

  }

  private void checkOf(Token token, T need) {
    if (!token.is(need)) {
      parser.perror("gcc-attribute list incomplete form");
    }
  }

}
