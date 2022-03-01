package ast.parse;

import ast.tree.ConstexprEval;
import ast.tree.Expression;
import jscan.symtab.Keywords;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseStaticAssert {
  private final Parse parser;

  public ParseStaticAssert(Parse parser) {
    this.parser = parser;
  }

  public boolean isStaticAssertAndItsOk() {

    //  static_assert_declaration
    //    : STATIC_ASSERT '(' constant_expression ',' STRING_LITERAL ')' ';'
    //    ;

    if (!parser.tok().isIdent(Keywords._Static_assert_ident)) {
      return false;
    }

    parser.checkedMove(Keywords._Static_assert_ident);
    parser.lparen();

    Expression ce = new ParseExpression(parser).e_const_expr();
    long result = new ConstexprEval(parser).ce(ce);

    if (parser.tok().ofType(T.T_RIGHT_PAREN)) {

      // c2x
      if (result == 0) {
        parser.perror("static-assert fail: " + ce);
      }

    } else {

      // c99 
      parser.checkedMove(T.T_COMMA);
      Token message = parser.checkedMove(T.TOKEN_STRING);

      if (result == 0) {
        parser.perror("static-assert fail: " + message.getValue());
      }

    }

    parser.rparen();
    parser.semicolon();
    return true;
  }

}
