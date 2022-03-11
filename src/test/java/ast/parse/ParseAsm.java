package ast.parse;

import static jscan.tokenize.T.TOKEN_EOF;
import static jscan.tokenize.T.T_LEFT_PAREN;
import static jscan.tokenize.T.T_RIGHT_PAREN;

import java.util.ArrayList;
import java.util.List;

import ast.attributes.AsmList;
import jscan.tokenize.Token;

public class ParseAsm {
  private final Parse parser;

  public ParseAsm(Parse parser) {
    this.parser = parser;
  }

  public AsmList parse() {

    List<Token> tokens = new ArrayList<Token>(0);

    if (!parser.isAsmStart()) {
      return new AsmList(tokens);
    }

    int nest = 0;
    String startLoc = parser.getLastLoc();

    tokens.add(parser.tok()); // __asm
    parser.move();

    while (Pcheckers.isTypeQual(parser.tok())) {
      Token saved = parser.tok();
      parser.move();
      tokens.add(saved);
    }

    if (!parser.tok().is(T_LEFT_PAREN)) {
      parser.perror("expect `(` after __asm");
    }

    // 
    Token lparen = parser.tok();
    parser.move();
    tokens.add(lparen);

    while (!parser.isEof()) {

      if (parser.tp() == TOKEN_EOF) {
        parser.perror("unclosed asm-statement list started at: " + startLoc);
      } else if (parser.tp() == T_LEFT_PAREN) {
        nest++;
      } else if (parser.tp() == T_RIGHT_PAREN) {
        if (--nest < 0) {
          Token saved = parser.tok();
          parser.move();
          tokens.add(saved);
          break;
        }
      } else {
        Token saved = parser.tok();
        parser.move();
        tokens.add(saved);
      }

    }

    return new AsmList(tokens);
  }
}
