package ast.stmt.parser;

import static ast.symtab.IdentMap.*;
import static jscan.tokenize.T.TOKEN_EOF;
import static jscan.tokenize.T.TOKEN_IDENT;
import static jscan.tokenize.T.T_COLON;
import static jscan.tokenize.T.T_LEFT_PAREN;
import static jscan.tokenize.T.T_RIGHT_PAREN;
import static jscan.tokenize.T.T_SEMI_COLON;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import ast.decls.Declaration;
import ast.decls.parser.ParseDeclarations;
import ast.expr.CExpression;
import ast.expr.parser.ParseExpression;
import ast.parse.Parse;
import ast.stmt.Scase;
import ast.stmt.Sdefault;
import ast.stmt.Sswitch;
import ast.stmt.main.CStatement;
import ast.stmt.main.CStatementBase;
import ast.stmt.sem.BreakContinue;
import ast.unit.BlockItem;
import ast.unit.FunctionDefinition;

public class ParseStatement {
  private final Parse parser;
  private final Stack<Sswitch> switches;
  private final Stack<String> loops;

  public ParseStatement(Parse parser) {
    this.parser = parser;
    this.switches = new Stack<Sswitch>();
    this.loops = new Stack<String>();
  }

  private CExpression e_expression() {
    return new ParseExpression(parser).e_expression();
  }

  public CStatement parseCompoundStatement(boolean isFunctionStart) {
    if (!isFunctionStart) {
      parser.pushscope();
    }

    List<BlockItem> blocks = new ArrayList<BlockItem>(0);

    Token lbrace = parser.checkedMove(T.T_LEFT_BRACE);

    if (parser.tp() == T.T_RIGHT_BRACE) {
      Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);

      if (!isFunctionStart) {
        parser.popscope(); // XXX:
      }

      return new CStatement(lbrace, rbrace, blocks);
    }

    BlockItem block = parseOneBlock();
    for (;;) {
      blocks.add(block);
      if (parser.tp() == T.T_RIGHT_BRACE) {
        break;
      }
      if (block == null) {
        break;
      }
      block = parseOneBlock();
    }

    Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);

    if (!isFunctionStart) {
      parser.popscope();
    }

    return new CStatement(lbrace, rbrace, blocks);
  }

  private CStatement parseStatement() {

    // null-statement

    if (parser.tok().ofType(T_SEMI_COLON)) {
      Token from = parser.semicolon();
      return new CStatement(from, CStatementBase.SSEMICOLON);
    }

    // default:

    if (parser.tok().isIdent(default_ident)) {
      Token from = parser.checkedMove(default_ident);
      parser.checkedMove(T_COLON);

      if (parser.tp() == T.T_RIGHT_BRACE) {
        parser.perror("default label without statement");
      }

      if (getSwitches().isEmpty()) {
        parser.perror("default label outside switch");
      }

      CStatement stmt = parseStatement();
      Sswitch parent = peekSwitch();

      parent.setDefault_stmt(new Sdefault(parent, stmt));
      return new CStatement(from, new Sdefault(parent, stmt));
    }

    // label:

    if (isLabel()) {
      return parseLabel();
    }

    // _asm ...

    if (parser.isAsmStart()) {
      return parseAsm();
    }

    // goto label;

    if (parser.tok().isIdent(goto_ident)) {
      FunctionDefinition function = parser.getCurrentFn();
      CStatement labelstmt = null; // XXX: ok

      if (function == null) {
        parser.perror("goto statement outside function");
      }

      Token from = parser.checkedMove(goto_ident);
      Ident label = parser.getIdent();

      function.addGotos(label);

      parser.semicolon();
      return new CStatement(from, CStatementBase.SGOTO, function, label, labelstmt);
    }

    // return ... ;
    // return ;

    if (parser.tok().isIdent(return_ident)) {
      Token from = parser.checkedMove(return_ident);

      if (parser.tp() == T_SEMI_COLON) {
        parser.move();
        return new CStatement(from, CStatementBase.SRETURN, null);
      }

      CExpression expr = e_expression();

      parser.checkedMove(T_SEMI_COLON);
      return new CStatement(from, CStatementBase.SRETURN, expr);
    }

    // for( ;; )

    if (parser.tok().isIdent(for_ident)) {
      Declaration decl = null;
      CExpression init = null;
      CExpression test = null;
      CExpression step = null;
      CStatement loop = null;

      pushLoop("for");
      parser.pushscope(); // TODO:

      Token from = parser.checkedMove(for_ident);
      parser.lparen();

      if (parser.tp() != T_SEMI_COLON) {

        if (parser.isDeclSpecStart()) {
          decl = new ParseDeclarations(parser).parse(); // XXX: semicolon moved in parse_declaration()
        }

        else {
          init = e_expression();
          parser.semicolon();
        }
      }

      else {
        parser.semicolon();
      }

      if (parser.tp() != T_SEMI_COLON) {
        test = e_expression();
      }
      parser.semicolon();

      if (parser.tp() != T_RIGHT_PAREN) {
        step = e_expression();
      }
      parser.rparen();

      loop = parseStatement();

      popLoop();
      parser.popscope(); // TODO:
      return new CStatement(from, decl, init, test, step, loop);
    }

    // switch

    if (parser.tok().isIdent(switch_ident)) {
      Token from = parser.checkedMove(switch_ident);
      CExpression expr = new ParseExpression(parser).getExprInParen();

      Sswitch nodeSwitch = new Sswitch(expr);
      pushSwitch(nodeSwitch);

      CStatement stmt = parseStatement();
      nodeSwitch.setStmt(stmt);

      popSwitch();
      return new CStatement(from, nodeSwitch);
    }

    // case :

    if (parser.tok().isIdent(case_ident)) {
      if (getSwitches().isEmpty()) {
        parser.perror("case outside switch");
      }

      Token from = parser.checkedMove(case_ident);
      CExpression expr = new ParseExpression(parser).e_const_expr();
      parser.checkedMove(T_COLON);

      Sswitch parent = peekSwitch();
      Scase caselab = new Scase(parent, expr);
      parent.pushcase(caselab);

      CStatement stmt = parseStatement();
      caselab.setCasestmt(stmt);

      return new CStatement(from, caselab);
    }

    // if ( expr ) stmt else stmt

    if (parser.tok().isIdent(if_ident)) {
      Token from = parser.checkedMove(if_ident);

      CExpression ifexpr = new ParseExpression(parser).getExprInParen();
      CStatement ifstmt = parseStatement();
      CStatement ifelse = null;

      if (parser.tok().isIdent(else_ident)) {
        Token elsekw = parser.checkedMove(else_ident);
        ifelse = parseStatement();
        return new CStatement(elsekw, ifexpr, ifstmt, ifelse);
      }

      return new CStatement(from, ifexpr, ifstmt, ifelse);
    }

    // while ( expr ) stmt

    if (parser.tok().isIdent(while_ident)) {
      pushLoop("while");
      Token from = parser.checkedMove(while_ident);

      CExpression test = new ParseExpression(parser).getExprInParen();
      CStatement loop = parseStatement();

      popLoop();
      return new CStatement(from, CStatementBase.SWHILE, test, loop);
    }

    // do stmt while expr

    if (parser.tok().isIdent(do_ident)) {
      pushLoop("do_while");
      Token from = parser.checkedMove(do_ident);

      CStatement loop = parseStatement();
      parser.checkedMove(while_ident);

      CExpression test = new ParseExpression(parser).getExprInParen();
      parser.semicolon();

      popLoop();
      return new CStatement(from, CStatementBase.SDOWHILE, test, loop);
    }

    // break ;

    if (parser.tok().isIdent(break_ident)) {
      return new BreakContinue(parser, this).breakStatement();
    }

    // continue ;

    if (parser.tok().isIdent(continue_ident)) {
      return new BreakContinue(parser, this).continueStatement();
    }

    // {  }

    if (parser.tok().ofType(T.T_LEFT_BRACE)) {
      return parseCompoundStatement(false);
    }

    // expression-statement by default
    //
    CStatement ret = new CStatement(parser.tok(), CStatementBase.SEXPR, e_expression());
    parser.semicolon();
    return ret;
  }

  private CStatement parseAsm() {

    List<Token> asmlist = new ArrayList<Token>();
    int nest = 0;
    Token startLoc = parser.tok();

    while (!parser.isEof()) {
      asmlist.add(parser.tok());
      parser.move();
      if (parser.tp() == TOKEN_EOF) {
        parser.perror("unclosed asm statement at: " + startLoc.loc());
      }
      if (parser.tp() == T_LEFT_PAREN) {
        nest++;
      }
      if (parser.tp() == T_RIGHT_PAREN) {
        if (--nest == 0) {
          asmlist.add(parser.tok());
          break;
        }
      }
    }

    parser.checkedMove(T.T_RIGHT_PAREN);
    parser.checkedMove(T_SEMI_COLON);

    return new CStatement(startLoc, asmlist);
  }

  private BlockItem parseOneBlock() {

    // TODO: doe's it correct, or maybe clean way exists?
    // different scope between names of labels and all other names.
    // why ??? 
    if (isLabel()) {
      BlockItem block = new BlockItem();
      block.setStatement(parseLabel());
      return block;
    }

    if (parser.isDeclSpecStart()) {
      Declaration dec = new ParseDeclarations(parser).parse();

      BlockItem block = new BlockItem();
      block.setDeclaration(dec);
      return block;
    }

    CStatement stmt = parseStatement();
    if (stmt != null) {
      BlockItem block = new BlockItem();
      block.setStatement(stmt);
      return block;
    }

    return null;
  }

  private boolean isLabel() {
    if (parser.isUserDefinedId()) {
      Token peek = parser.peek();
      if (peek.ofType(T_COLON)) {
        return true;
      }
    }
    return false;
  }

  private CStatement parseLabel() {

    FunctionDefinition function = parser.getCurrentFn();

    if (function == null) {
      parser.perror("label statement outside function");
    }

    Token from = parser.tok();
    Ident label = parser.getIdent();

    function.addLabel(label);

    parser.checkedMove(T_COLON);
    CStatement labelstmt = parseStatement();

    return new CStatement(from, CStatementBase.SLABEL, function, label, labelstmt);
  }

  private void pushSwitch(Sswitch s) {
    switches.push(s);
  }

  private Sswitch peekSwitch() {
    return switches.peek();
  }

  private void popSwitch() {
    switches.pop();
  }

  private void pushLoop(String s) {
    loops.push(s);
  }

  private void popLoop() {
    loops.pop();
  }

  public Stack<Sswitch> getSwitches() {
    return switches;
  }

  public Stack<String> getLoops() {
    return loops;
  }

}
