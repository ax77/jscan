package ast.stmt.sem;

import jscan.tokenize.Token;
import ast.parse.Parse;
import ast.stmt.main.CStatement;
import ast.stmt.main.CStatementBase;
import ast.stmt.parser.ParseStatement;
import ast.symtab.IdentMap;

public class BreakContinue {
  private final Parse parser;
  private final ParseStatement parseStatement;

  public BreakContinue(Parse parser, ParseStatement parseStatement) {
    this.parser = parser;
    this.parseStatement = parseStatement;
  }

  private boolean canBreak() {
    boolean hasSWitch = !parseStatement.getSwitches().isEmpty();
    boolean hasLoop = !parseStatement.getLoops().isEmpty();
    return hasSWitch || hasLoop;
  }

  private boolean canContinue() {
    return !parseStatement.getLoops().isEmpty();
  }

  private void checkBreak() {
    if (!canBreak()) {
      parser.perror("stray `break`");
    }
  }

  private void checkContinue() {
    if (!canContinue()) {
      parser.perror("stray `continue`");
    }
  }

  public CStatement breakStatement() {
    checkBreak();

    Token from = parser.checkedMove(IdentMap.break_ident);
    parser.semicolon();
    return new CStatement(from, CStatementBase.SBREAK);
  }

  public CStatement continueStatement() {
    checkContinue();

    Token from = parser.checkedMove(IdentMap.continue_ident);
    parser.semicolon();
    return new CStatement(from, CStatementBase.SCONTINUE);
  }

}
