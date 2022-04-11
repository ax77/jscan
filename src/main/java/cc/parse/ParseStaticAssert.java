package cc.parse;

import cc.builders.ConstexprEval;
import cc.tree.Expression;
import cc.tree.Keywords;
import cc.tree.Declaration.StaticAssertDeclarationStub;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseStaticAssert {
  private final Parse parser;

  public ParseStaticAssert(Parse parser) {
    this.parser = parser;
  }

  public StaticAssertDeclarationStub isStaticAssertAndItsOk() {

    //  static_assert_declaration
    //    : STATIC_ASSERT '(' constant_expression ',' STRING_LITERAL ')' ';'
    //    ;

    if (!parser.tok().isIdent(Keywords._Static_assert_ident)) {
      return null;
    }

    parser.checkedMove(Keywords._Static_assert_ident);
    parser.lparen();

    Expression ce = new ParseExpression(parser).e_const_expr();
    long result = new ConstexprEval(parser).ce(ce);

    StaticAssertDeclarationStub r = new StaticAssertDeclarationStub(ce, "");
    
    if (parser.is(T.T_RIGHT_PAREN)) {

      // c2x
      if (result == 0) {
        parser.perror("static-assert fail: " + ce);
      }

    } else {

      // c99 
      parser.checkedMove(T.T_COMMA);
      Token message = parser.checkedMove(T.TOKEN_STRING);
      r.msg = message.getValue();

      if (result == 0) {
        parser.perror("static-assert fail: " + message.getValue());
      }

    }

    parser.rparen();
    parser.semicolon();
    return r;
  }

}
