package ast.parse;

import static jscan.symtab.Keywords.break_ident;
import static jscan.symtab.Keywords.case_ident;
import static jscan.symtab.Keywords.continue_ident;
import static jscan.symtab.Keywords.default_ident;
import static jscan.symtab.Keywords.do_ident;
import static jscan.symtab.Keywords.else_ident;
import static jscan.symtab.Keywords.for_ident;
import static jscan.symtab.Keywords.goto_ident;
import static jscan.symtab.Keywords.if_ident;
import static jscan.symtab.Keywords.return_ident;
import static jscan.symtab.Keywords.switch_ident;
import static jscan.symtab.Keywords.while_ident;
import static jscan.tokenize.T.TOKEN_EOF;
import static jscan.tokenize.T.T_COLON;
import static jscan.tokenize.T.T_LEFT_PAREN;
import static jscan.tokenize.T.T_RIGHT_PAREN;
import static jscan.tokenize.T.T_SEMI_COLON;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ast.tree.BlockItem;
import ast.tree.BreakContinue;
import ast.tree.Declaration;
import ast.tree.Expression;
import ast.tree.FunctionDefinition;
import ast.tree.Scase;
import ast.tree.Sdefault;
import ast.tree.Sswitch;
import ast.tree.Statement;
import ast.tree.StatementBase;
import jscan.symtab.Ident;
import jscan.symtab.ScopeLevels;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseStatement {
  private final Parse parser;
  private final Stack<Sswitch> switches;
  private final Stack<String> loops;

  public ParseStatement(Parse parser) {
    this.parser = parser;
    this.switches = new Stack<Sswitch>();
    this.loops = new Stack<String>();
  }

  private Expression e_expression() {
    return new ParseExpression(parser).e_expression();
  }

  public Statement parseCompoundStatement(boolean isFunctionStart) {
    if (!isFunctionStart) {
      parser.pushscope(ScopeLevels.BLOCK_SCOPE);
    }

    List<BlockItem> blocks = new ArrayList<BlockItem>(0);

    Token lbrace = parser.checkedMove(T.T_LEFT_BRACE);

    if (parser.tp() == T.T_RIGHT_BRACE) {
      Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);

      if (!isFunctionStart) {
        parser.popscope(); // XXX:
      }

      return new Statement(lbrace, rbrace, blocks);
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

    return new Statement(lbrace, rbrace, blocks);
  }

  private Statement parseStatement() {

    // null-statement

    if (parser.tok().ofType(T_SEMI_COLON)) {
      Token from = parser.semicolon();
      return new Statement(from, StatementBase.SSEMICOLON);
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

      Statement stmt = parseStatement();
      Sswitch parent = peekSwitch();

      parent.setDefault_stmt(new Sdefault(parent, stmt));
      return new Statement(from, new Sdefault(parent, stmt));
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
      Statement labelstmt = null; // XXX: ok

      if (function == null) {
        parser.perror("goto statement outside function");
      }

      Token from = parser.checkedMove(goto_ident);
      Ident label = parser.getIdent();

      function.addGotos(label);

      parser.semicolon();
      return new Statement(from, StatementBase.SGOTO, function, label, labelstmt);
    }

    // return ... ;
    // return ;

    if (parser.tok().isIdent(return_ident)) {
      Token from = parser.checkedMove(return_ident);

      if (parser.tp() == T_SEMI_COLON) {
        parser.move();
        return new Statement(from, StatementBase.SRETURN, null);
      }

      Expression expr = e_expression();

      parser.checkedMove(T_SEMI_COLON);
      return new Statement(from, StatementBase.SRETURN, expr);
    }

    // for( ;; )

    if (parser.tok().isIdent(for_ident)) {
      Declaration decl = null;
      Expression init = null;
      Expression test = null;
      Expression step = null;
      Statement loop = null;

      pushLoop("for");
      parser.pushscope(ScopeLevels.BLOCK_SCOPE); // TODO:

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
      return new Statement(from, decl, init, test, step, loop);
    }

    // switch

    if (parser.tok().isIdent(switch_ident)) {
      Token from = parser.checkedMove(switch_ident);
      Expression expr = new ParseExpression(parser).getExprInParen();

      Sswitch nodeSwitch = new Sswitch(expr);
      pushSwitch(nodeSwitch);

      Statement stmt = parseStatement();
      nodeSwitch.setStmt(stmt);

      popSwitch();
      return new Statement(from, nodeSwitch);
    }

    // case :

    if (parser.tok().isIdent(case_ident)) {
      if (getSwitches().isEmpty()) {
        parser.perror("case outside switch");
      }

      Token from = parser.checkedMove(case_ident);
      Expression expr = new ParseExpression(parser).e_const_expr();
      parser.checkedMove(T_COLON);

      Sswitch parent = peekSwitch();
      Scase caselab = new Scase(parent, expr);
      parent.pushcase(caselab);

      Statement stmt = parseStatement();
      caselab.setCasestmt(stmt);

      return new Statement(from, caselab);
    }

    // if ( expr ) stmt else stmt

    if (parser.tok().isIdent(if_ident)) {
      Token from = parser.checkedMove(if_ident);

      Expression ifexpr = new ParseExpression(parser).getExprInParen();
      Statement ifstmt = parseStatement();
      Statement ifelse = null;

      if (parser.tok().isIdent(else_ident)) {
        Token elsekw = parser.checkedMove(else_ident);
        ifelse = parseStatement();
        return new Statement(elsekw, ifexpr, ifstmt, ifelse);
      }

      return new Statement(from, ifexpr, ifstmt, ifelse);
    }

    // while ( expr ) stmt

    if (parser.tok().isIdent(while_ident)) {
      pushLoop("while");
      Token from = parser.checkedMove(while_ident);

      Expression test = new ParseExpression(parser).getExprInParen();
      Statement loop = parseStatement();

      popLoop();
      return new Statement(from, StatementBase.SWHILE, test, loop);
    }

    // do stmt while expr

    if (parser.tok().isIdent(do_ident)) {
      pushLoop("do_while");
      Token from = parser.checkedMove(do_ident);

      Statement loop = parseStatement();
      parser.checkedMove(while_ident);

      Expression test = new ParseExpression(parser).getExprInParen();
      parser.semicolon();

      popLoop();
      return new Statement(from, StatementBase.SDOWHILE, test, loop);
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
    Statement ret = new Statement(parser.tok(), StatementBase.SEXPR, e_expression());
    parser.semicolon();
    return ret;
  }

  private Statement parseAsm() {

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

    return new Statement(startLoc, asmlist);
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

    Statement stmt = parseStatement();
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

  private Statement parseLabel() {

    FunctionDefinition function = parser.getCurrentFn();

    if (function == null) {
      parser.perror("label statement outside function");
    }

    Token from = parser.tok();
    Ident label = parser.getIdent();

    function.addLabel(label);

    parser.checkedMove(T_COLON);
    Statement labelstmt = parseStatement();

    return new Statement(from, StatementBase.SLABEL, function, label, labelstmt);
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