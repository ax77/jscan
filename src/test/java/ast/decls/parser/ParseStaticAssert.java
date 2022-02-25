package ast.decls.parser;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import ast.expr.CExpression;
import ast.expr.parser.ParseExpression;
import ast.expr.sem.ConstexprEval;
import ast.parse.Parse;
import ast.symtab.IdentMap;

public class ParseStaticAssert {
  private final Parse parser;

  public ParseStaticAssert(Parse parser) {
    this.parser = parser;
  }

  public boolean isStaticAssertAndItsOk() {

    //  static_assert_declaration
    //    : STATIC_ASSERT '(' constant_expression ',' STRING_LITERAL ')' ';'
    //    ;

    if (!parser.tok().isIdent(IdentMap._Static_assert_ident)) {
      return false;
    }

    parser.checkedMove(IdentMap._Static_assert_ident);
    parser.lparen();

    CExpression ce = new ParseExpression(parser).e_const_expr();
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
