package ast.parse;

import static jscan.tokenize.T.TOKEN_IDENT;

import ast.builders.ConstexprEval;
import ast.symtab.CSymTag;
import ast.symtab.CSymEnumConst;
import ast.symtab.CSymbol;
import ast.symtab.CSymbolBase;
import ast.tree.Expression;
import ast.types.CEnumType;
import ast.types.CType;
import ast.types.CTypeImpl;
import jscan.symtab.Ident;
import jscan.symtab.Keywords;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import jscan.utils.NullChecker;

public class ParseEnum3 {

  private final Parse parser;

  public ParseEnum3(Parse parser) {
    this.parser = parser;
  }

  public CType parse() {

    // enum name ;
    // enum name {
    // enum      {
    // .... ^....^  

    boolean iscorrect = parser.isUserDefinedId() || parser.is(T.T_LEFT_BRACE);
    if (!iscorrect) {
      parser.perror("expect identifier or { for enum type-specifier");
    }

    Token pos = parser.tok();
    Ident tag = null;
    if (parser.is(TOKEN_IDENT)) {
      tag = parser.getIdent();
    }

    if (tag != null) {
      CType tp = null;
      CSymTag cur = parser.getTagFromCurrentScope(tag);
      CSymTag all = parser.getTag(tag);
      if (all != null) {
        // A declaration of the identifier as a tag
        // is visible in this or an enclosing scope.
        if (cur == null && (parser.is(T.T_SEMI_COLON) || parser.is(T.T_LEFT_BRACE))) {
          tp = incompl(tag, pos);
        } else {
          tp = all.type;
        }
      } else {
        tp = incompl(tag, pos);
      }
      NullChecker.check(tp);
      if (parser.is(T.T_LEFT_BRACE)) {
        if (cur != null && !cur.type.isIncomplete()) {
          parser.perror("redefinition of enum: " + tag);
        }
        enums(tp);
        if (cur != null) {
          cur.type = tp;
        }
      }

    } else if (parser.is(T.T_LEFT_BRACE)) {
      CType tp = new CType(new CEnumType(tag)); // anonymous
      enums(tp);
    } else {
      parser.unreachable("expecting tag or {");
    }

    return CTypeImpl.TYPE_INT;
  }

  private void enums(CType type) {
    parser.lbrace();

    int val = 0;
    while (parser.is(TOKEN_IDENT)) {
      Token pos = parser.tok();

      Ident enm = parser.getIdent();
      if (parser.is(T.T_ASSIGN)) {
        parser.move();
        Expression exp = new ParseExpression(parser).e_const_expr();
        val = (int) new ConstexprEval(parser).ce(exp);
      }

      CSymEnumConst enumConst = new CSymEnumConst(enm, type, val);
      CSymbol sym = new CSymbol(enumConst, pos);
      parser.defineSym(sym);
      val += 1;

      if (parser.is(T.T_COMMA)) {
        parser.move();
      }
    }

    parser.rbrace();
    type.tpEnum.isComplete = true;
  }

  private CType incompl(Ident tag, Token pos) {
    final CType typ = new CType(new CEnumType(tag));
    parser.defineTag(new CSymTag(Keywords.enum_ident, tag, typ));
    return typ;
  }

}
