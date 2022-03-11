package ast.tree;

import java.util.List;

import ast.attributes.AsmList;
import jscan.tokenize.Token;

public class Statement {

  public final StatementBase base;
  public final Token location;
  public List<BlockItem> block;
  public StmtSwitch stmtSwitch;
  public StmtCase stmtCase;
  public StmtDefault stmtDefault;
  public StmtFor stmtFor;
  public StmtWhileDo stmtWhileDo;
  public Expression stmtExpr;
  public StmtSelect stmtSelect;
  public StmtLabel stmtLabel;
  public AsmList asmlist;

  public Statement(StmtFor stmtFor, Token from) {
    this.location = from;
    this.base = StatementBase.SFOR;
    this.stmtFor = stmtFor;
  }

  public Statement(StatementBase base, StmtWhileDo stmtWhileDo, Token from) {
    this.location = from;
    this.base = base;
    this.stmtWhileDo = stmtWhileDo;
  }

  // return expr
  // return ;
  // expr-stmt
  public Statement(Token from, StatementBase base, Expression expr) {
    this.location = from;
    this.base = base;
    this.stmtExpr = expr;
  }

  // label: stmt
  // goto label;
  public Statement(StatementBase base, StmtLabel stmtLabel, Token from) {
    this.location = from;
    this.base = base;
    this.stmtLabel = stmtLabel;
  }

  public Statement(StmtSelect stmtSelect, Token from) {
    this.location = from;
    this.base = StatementBase.SIF;
    this.stmtSelect = stmtSelect;
  }

  public Statement(Token lbrace, Token rbrace, List<BlockItem> block) {
    this.location = lbrace; // TODO: range loc
    this.base = StatementBase.SCOMPOUND;
    this.block = block;
  }

  public Statement(Token from, AsmList asmlist) {
    this.location = from;
    this.base = StatementBase.SASM;
    this.asmlist = asmlist;
  }

  public Statement(Token from, StmtDefault stmtDefault) {
    this.location = from;
    this.base = StatementBase.SDEFAULT;
    this.stmtDefault = stmtDefault;
  }

  // break, continue
  public Statement(Token from, StatementBase base) {
    this.location = from;
    this.base = base;
  }

  public Statement(Token from, StmtSwitch stmtSwitch) {
    this.location = from;
    this.base = StatementBase.SSWITCH;
    this.stmtSwitch = stmtSwitch;
  }

  public Statement(Token from, StmtCase stmtCase) {
    this.location = from;
    this.base = StatementBase.SCASE;
    this.stmtCase = stmtCase;
  }

}
