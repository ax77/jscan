package ast.expr.parser;

import static jscan.tokenize.T.TOKEN_CHAR;
import static jscan.tokenize.T.TOKEN_NUMBER;
import static jscan.tokenize.T.TOKEN_STRING;
import static jscan.tokenize.T.T_AND;
import static jscan.tokenize.T.T_AND_AND;
import static jscan.tokenize.T.T_ARROW;
import static jscan.tokenize.T.T_COLON;
import static jscan.tokenize.T.T_DIVIDE;
import static jscan.tokenize.T.T_DOT;
import static jscan.tokenize.T.T_EQ;
import static jscan.tokenize.T.T_GE;
import static jscan.tokenize.T.T_GT;
import static jscan.tokenize.T.T_LE;
import static jscan.tokenize.T.T_LEFT_BRACKET;
import static jscan.tokenize.T.T_LEFT_PAREN;
import static jscan.tokenize.T.T_LSHIFT;
import static jscan.tokenize.T.T_LT;
import static jscan.tokenize.T.T_MINUS;
import static jscan.tokenize.T.T_MINUS_MINUS;
import static jscan.tokenize.T.T_NE;
import static jscan.tokenize.T.T_OR;
import static jscan.tokenize.T.T_OR_OR;
import static jscan.tokenize.T.T_PERCENT;
import static jscan.tokenize.T.T_PLUS;
import static jscan.tokenize.T.T_QUESTION;
import static jscan.tokenize.T.T_RIGHT_PAREN;
import static jscan.tokenize.T.T_RSHIFT;
import static jscan.tokenize.T.T_TIMES;
import static jscan.tokenize.T.T_XOR;

import java.util.ArrayList;
import java.util.List;

import jscan.hashed.Hash_ident;
import jscan.literals.IntLiteral;
import jscan.literals.IntLiteralParser;
import jscan.literals.IntLiteralType;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import ast.decls.Initializer;
import ast.decls.parser.ParseInitializerList;
import ast.expr.CExpression;
import ast.expr.CExpressionBase;
import ast.expr.sem.TypeApplier;
import ast.expr.sem.TypeApplierStage;
import ast.expr.util.ExprUtil;
import ast.parse.Parse;
import ast.parse.ParseState;
import ast.parse.Pcheckers;
import ast.symtab.IdentMap;
import ast.symtab.elements.CSymbol;
import ast.symtab.elements.CSymbolBase;
import ast.symtab.elements.NumericConstant;
import ast.symtab.elements.StringConstant;
import ast.types.CArrayType;
import ast.types.CStructField;
import ast.types.CStructType;
import ast.types.CType;
import ast.types.CTypeImpl;

public class ParseExpression {
  private final Parse parser;

  public ParseExpression(Parse parser) {
    this.parser = parser;
  }

  private CExpression build_unary(Token op, CExpression operand) {
    return new CExpression(CExpressionBase.EUNARY, op, operand);
  }

  private CExpression build_binary(Token operator, CExpression lhs, CExpression rhs) {
    return new CExpression(CExpressionBase.EBINARY, lhs, rhs, operator);
  }

  private CExpression build_ternary(CExpression cnd, CExpression btrue, CExpression bfalse, Token tok) {
    return new CExpression(cnd, btrue, bfalse, tok);
  }

  private CExpression build_assign(Token tok, CExpression lvalue, CExpression rvalue) {
    return new CExpression(CExpressionBase.EASSIGN, lvalue, rvalue, tok);
  }

  private CExpression build_comma(Token tok, T op, CExpression lhs, CExpression rhs) {
    return new CExpression(CExpressionBase.ECOMMA, lhs, rhs, tok);
  }

  // numeric-char-constants
  private CExpression build_number(IntLiteral e, Token token) {
    return new CExpression(e, token);
  }

  // sizeof, alignof
  private CExpression build_usize(long u64, Token token) {
    NumericConstant nc = new NumericConstant(u64, IntLiteralType.U64);
    return new CExpression(nc, token);
  }

  private CExpression build_cast(Parse parser, CType typename, CExpression tocast, Token token) {
    return new CExpression(typename, tocast, token);
  }

  private CExpression build_var(CSymbol e, Token token) {
    return new CExpression(e, token);
  }

  private CExpression build_compsel(CExpression postfis, Token operator, CStructField fieldName) {
    return new CExpression(postfis, operator, fieldName);
  }

  private CExpression build_fcall(CExpression function, List<CExpression> arguments, Token token) {
    return new CExpression(function, arguments, token);
  }

  private CExpression build_incdec(CExpressionBase base, Token op, CExpression lhs) {
    return new CExpression(base, op, lhs);
  }

  private CExpression build_compliteral(CType typename, List<Initializer> initializerList, Token saved) {
    return new CExpression(typename, initializerList, saved);
  }

  private static int globtempcnt = 0;

  private String newgloblab() {
    return String.format("t%d", globtempcnt++);
  }

  // TODO: define in global scope as label
  //
  private CExpression build_strconst(Token saved) {

    String str = saved.getValue();
    if (str.startsWith("\"") && str.endsWith("\"")) {
      str = str.substring(1, str.length() - 1);
    }

    final Ident varname = Hash_ident.getHashedIdent(newgloblab());
    final CArrayType arrtype = new CArrayType(CTypeImpl.TYPE_CHAR, str.length() + 1);

    final CExpression initexpr = new CExpression(new StringConstant(saved.getStrconstant(), str), saved);
    final Initializer initializer = new Initializer(initexpr, 0);

    List<Initializer> initlist = new ArrayList<Initializer>();
    initlist.add(initializer);

    CSymbol sym = new CSymbol(CSymbolBase.SYM_GVAR, varname, new CType(arrtype), initlist, saved);
    parser.defineSym(varname, sym);

    return new CExpression(sym, saved);
  }

  public CExpression e_expression() {
    CExpression e = e_assign();

    while (parser.tp() == T.T_COMMA) {
      Token saved = parser.checkedMove(T.T_COMMA);
      e = build_comma(saved, saved.getType(), e, e_expression());
    }

    return e;
  }

  public CExpression e_const_expr() {
    return e_cnd();
  }

  public CExpression getExprInParen() {
    parser.checkedMove(T_LEFT_PAREN);
    CExpression e = e_expression();
    parser.checkedMove(T.T_RIGHT_PAREN);
    return e;
  }

  private boolean isCompoundAssign(Token what) {
    return Pcheckers.isAssignOperator(what) && !what.ofType(T.T_ASSIGN);
  }

  public CExpression e_assign() {
    CExpression lhs = e_cnd();

    // if simple, then: this...
    //
    //    if (parser.isAssignOperator()) {
    //      Token saved = parser.tok();
    //      parser.move();
    //      final CExpression rhs = e_assign();
    //      lhs = build_assign(saved, lhs, rhs);
    //    }

    if (Pcheckers.isAssignOperator(parser.tok())) {

      Token saved = parser.tok();

      if (isCompoundAssign(saved)) {
        parser.move();

        // linearize compound assign
        // a+=b :: a=a+b
        //
        // += lhs(a) rhs(b)
        // = lhs(a) rhs( + lhs(a) rhs(b) )

        Token assignOperator = ExprUtil.assignOperator(saved);
        Token binaryOperator = ExprUtil.getOperatorFromCompAssign(saved);

        CExpression rhs = build_binary(binaryOperator, lhs, e_assign());
        lhs = build_assign(assignOperator, lhs, rhs);
      }

      else {

        parser.move();
        lhs = build_assign(saved, lhs, e_assign());
      }

    }

    return lhs;
  }

  private CExpression e_cnd() {
    CExpression res = e_lor();

    if (parser.tp() != T_QUESTION) {
      return res;
    }

    Token saved = parser.tok();
    parser.move();

    CExpression btrue = e_expression();
    parser.checkedMove(T_COLON);

    return build_ternary(res, btrue, e_cnd(), saved);
  }

  private CExpression e_lor() {
    CExpression e = e_land();
    while (parser.tp() == T_OR_OR) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_land());
    }
    return e;
  }

  private CExpression e_land() {
    CExpression e = e_bor();
    while (parser.tp() == T_AND_AND) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_bor());
    }
    return e;
  }

  private CExpression e_bor() {
    CExpression e = e_bxor();
    while (parser.tp() == T_OR) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_bxor());
    }
    return e;
  }

  private CExpression e_bxor() {
    CExpression e = e_band();
    while (parser.tp() == T_XOR) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_band());
    }
    return e;
  }

  private CExpression e_band() {
    CExpression e = e_equality();
    while (parser.tp() == T_AND) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_equality());
    }
    return e;
  }

  private CExpression e_equality() {
    CExpression e = e_relational();
    while (parser.tp() == T_EQ || parser.tp() == T_NE) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_relational());
    }
    return e;
  }

  private CExpression e_relational() {
    CExpression e = e_shift();
    while (parser.tp() == T_LT || parser.tp() == T_GT || parser.tp() == T_LE || parser.tp() == T_GE) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_shift());
    }
    return e;
  }

  private CExpression e_shift() {
    CExpression e = e_add();
    while (parser.tp() == T_LSHIFT || parser.tp() == T_RSHIFT) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_add());
    }
    return e;
  }

  private CExpression e_add() {
    CExpression e = e_mul();
    while (parser.tp() == T_PLUS || parser.tp() == T_MINUS) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_mul());
    }
    return e;
  }

  private CExpression e_mul() {
    CExpression e = e_cast();
    while (parser.tp() == T_TIMES || parser.tp() == T_DIVIDE || parser.tp() == T_PERCENT) {
      Token saved = parser.tok();
      parser.move();
      e = build_binary(saved, e, e_cast());
    }
    return e;
  }

  private CExpression e_cast() {

    if (parser.tp() == T_LEFT_PAREN) {
      ParseState state = new ParseState(parser);

      Token peek = parser.peek();
      if (parser.isDeclSpecStart(peek)) {

        Token lparen = parser.lparen();
        CType typeName = parser.parseTypename();
        parser.rparen();

        // ambiguous
        // "(" type-name ")" "{" initializer-list "}"
        // "(" type-name ")" "{" initializer-list "," "}"

        if (parser.tp() != T.T_LEFT_BRACE) {
          final CExpression tocast = e_cast();
          return build_cast(parser, typeName, tocast, lparen);
        }
      }

      parser.restoreState(state);

    }

    return e_unary();
  }

  private CExpression e_unary() {

    // [& * + - ~ !]
    if (Pcheckers.isUnaryOperator(parser.tok())) {
      Token operator = parser.tok();
      parser.move();
      return build_unary(operator, e_cast());
    }

    if (parser.tp() == T.T_PLUS_PLUS || parser.tp() == T_MINUS_MINUS) {
      Token operator = parser.tok();
      parser.move();
      return build_incdec(CExpressionBase.EPREINCDEC, operator, e_unary());
    }

    // TODO: merge with _Alignof()
    if (parser.tok().isIdent(IdentMap.sizeof_ident)) {
      return e_sizeof();
    }

    return e_postfix();
  }

  private CExpression e_sizeof() {
    Token id = parser.checkedMove(IdentMap.sizeof_ident);

    if (parser.tp() == T_LEFT_PAREN) {
      parser.lparen();

      // sizeof(int)

      if (parser.isDeclSpecStart()) {

        CType typename = parser.parseTypename();
        parser.rparen();

        return build_usize(typename.getSize(), id);

      } else {

        // sizeof(any-varname)

        CExpression sizeofexpr = e_expression();
        TypeApplier.applytype(sizeofexpr, TypeApplierStage.stage_start);
        parser.rparen();

        if (sizeofexpr.getResultType() == null) {
          parser.perror("unimplemented sizeof for: " + sizeofexpr.toString());
        }

        return build_usize(sizeofexpr.getResultType().getSize(), id);

      }

    }

    // sizeof 1

    CExpression sizeofexpr = e_unary();
    TypeApplier.applytype(sizeofexpr, TypeApplierStage.stage_start);

    if (sizeofexpr.getResultType() == null) {
      parser.perror("unimplemented sizeof for: " + sizeofexpr.toString());
    }

    return build_usize(sizeofexpr.getResultType().getSize(), id);

  }

  private CExpression e_postfix() {

    // "(" type-name ")" "{" initializer-list "}"
    // "(" type-name ")" "{" initializer-list "," "}"

    if (parser.tp() == T_LEFT_PAREN && parser.isDeclSpecStart(parser.peek())) {
      ParseState state = new ParseState(parser);

      parser.lparen();
      CType typename = parser.parseTypename();
      parser.rparen();

      // if next is `{` return compound, restore state otherwise
      //
      if (parser.tp() == T.T_LEFT_BRACE) {
        Token saved = parser.tok();

        List<Initializer> initializerList = new ParseInitializerList(parser, typename).parse();
        return build_compliteral(typename, initializerList, saved);
      }

      parser.restoreState(state);
    }

    CExpression lhs = e_prim();

    for (;;) {

      // function - call
      //
      if (parser.tp() == T_LEFT_PAREN) {
        Token lparen = parser.lparen();

        List<CExpression> arglist = new ArrayList<CExpression>();

        if (parser.tp() != T_RIGHT_PAREN) {
          CExpression onearg = e_assign();
          arglist.add(onearg);

          while (parser.tp() == T.T_COMMA) {
            parser.move();

            CExpression oneargSeq = e_assign();
            arglist.add(oneargSeq);
          }
        }

        lhs = build_fcall(lhs, arglist, lparen);
        parser.rparen();
      }

      // direct|indirect selection
      //
      else if (parser.tp() == T_DOT || parser.tp() == T_ARROW) {
        Token operator = parser.tok();
        parser.move(); // move . or ->

        Ident fieldName = parser.getIdent();
        TypeApplier.applytype(lhs, TypeApplierStage.stage_start);

        // a->b :: (*a).b
        if (operator.ofType(T_ARROW)) {

          final Token operatorDeref = ExprUtil.derefOperator(operator);
          final Token operatorDot = ExprUtil.dotOperator(operator);
          final CStructField field = getFieldArrow(lhs, fieldName);

          CExpression inBrace = build_unary(operatorDeref, lhs);
          lhs = build_compsel(inBrace, operatorDot, field);
        }

        else {

          final CStructField field = getFieldDot(lhs, fieldName);
          lhs = build_compsel(lhs, operator, field);
        }

      }

      // ++ --
      //
      else if (parser.tp() == T.T_PLUS_PLUS || parser.tp() == T_MINUS_MINUS) {
        Token operator = parser.tok();
        parser.move();
        lhs = build_incdec(CExpressionBase.EPOSTINCDEC, operator, lhs);
      }

      // array-subscript
      //
      else if (parser.tp() == T.T_LEFT_BRACKET) {
        while (parser.tp() == T_LEFT_BRACKET) {
          Token lbrack = parser.lbracket();

          // a[5] :: *(a+5)
          Token operatorPlus = ExprUtil.plusOperator(lbrack);
          Token operatorDeref = ExprUtil.derefOperator(lbrack);

          CExpression inBrace = build_binary(operatorPlus, lhs, e_expression());
          lhs = build_unary(operatorDeref, inBrace);

          parser.rbracket();
        }
      }

      else {
        break;
      }
    }

    return lhs;
  }

  private CStructField getFieldArrow(CExpression postfix, Ident fieldName) {

    final CType tp = postfix.getResultType();
    if (!tp.isPointerToStructUnion()) {
      parser.perror("expect pointer to struct or union for '->' operator");
    }

    final CStructType tpStruct = tp.getTpPointer().getPointerTo().getTpStruct();

    if (tpStruct.isIncomplete()) {
      parser.perror("field selection [a->b] from incomplete struct/union");
    }

    CStructField field = tpStruct.findField(fieldName);
    if (field == null) {
      parser.perror("error: struct has no field: " + fieldName.getName());
    }

    return field;
  }

  private CStructField getFieldDot(CExpression postfix, Ident fieldName) {

    final CType tp = postfix.getResultType();
    if (!tp.isStrUnion()) {
      parser.perror("expect struct or union for '.' operator");
    }

    if (tp.getTpStruct() == null || tp.getTpStruct().isIncomplete()) {
      parser.perror("field selection [a.b] from incomplete struct/union");
    }

    CStructField field = tp.getTpStruct().findField(fieldName);
    if (field == null) {
      parser.perror("error: struct has no field: " + fieldName.getName());
    }

    return field;
  }

  private CExpression e_prim() {

    //  primary_expression
    //      : IDENTIFIER
    //      | constant
    //      | string
    //      | '(' expression ')'
    //      | generic_selection
    //      ;

    if (parser.tok().isIdent(IdentMap._Generic_ident)) {
      Token saved = parser.tok();
      return new ExpandGenericResult(parser).getGenericResult(saved);
    }

    if (parser.tp() == TOKEN_NUMBER || parser.tp() == TOKEN_CHAR || parser.tp() == TOKEN_STRING) {
      Token saved = parser.tok();
      parser.move();

      if (saved.ofType(TOKEN_STRING)) {
        return build_strconst(saved);
      }

      else {

        //TODO:NUMBERS
        String toeval = "";
        if (saved.ofType(TOKEN_CHAR)) {
          toeval = String.format("%d", saved.getCharconstant().getV());
        } else {
          toeval = saved.getValue();
        }

        // TODO:NUMBERS
        IntLiteral strtox = IntLiteralParser.parse(toeval);
        return build_number(strtox, saved);
      }
    }

    if (parser.tp() == T.TOKEN_IDENT) {
      Token saved = parser.tok();
      parser.move();

      CSymbol sym = parser.getSym(saved.getIdent());
      if (sym == null) {
        parser.perror("symbol '" + saved.getValue() + "' was not declared in the scope.");
      }

      return build_var(sym, saved);
    }

    if (parser.tp() == T_LEFT_PAREN) {
      parser.move();
      CExpression e = e_expression();
      parser.checkedMove(T_RIGHT_PAREN);
      return e;
    }

    parser.perror("something wrong in expression...");
    return null; // you never return this ;)

  }

}
