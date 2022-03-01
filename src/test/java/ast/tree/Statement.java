package ast.tree;

import java.util.List;

import ast.attributes.AsmList;
import jscan.sourceloc.SourceLocation;
import jscan.tokenize.Token;

public class Statement {

  private final StatementBase base;
  private final SourceLocation location;

  private List<BlockItem> block;
  private StmtSwitch stmtSwitch;
  private StmtCase stmtCase;
  private StmtDefault stmtDefault;
  private StmtFor stmtFor;
  private StmtWhileDo stmtDoWhile;
  private Expression stmtExpr;
  private StmtSelect stmtSelect;
  private StmtLabel stmtLabel;
  private AsmList asmlist;

  public Statement(StmtFor stmtFor, Token from) {
    this.location = new SourceLocation(from);
    this.base = StatementBase.SFOR;
    this.stmtFor = stmtFor;
  }

  public Statement(StatementBase base, StmtWhileDo stmtDoWhile, Token from) {
    this.location = new SourceLocation(from);
    this.base = base;
    this.stmtDoWhile = stmtDoWhile;
  }

  // return expr
  // return ;
  // expr-stmt
  public Statement(Token from, StatementBase base, Expression expr) {
    this.location = new SourceLocation(from);
    this.base = base;
    this.stmtExpr = expr;
  }

  // label: stmt
  // goto label;
  public Statement(StatementBase base, StmtLabel stmtLabel, Token from) {
    this.location = new SourceLocation(from);
    this.base = base;
    this.stmtLabel = stmtLabel;
  }

  public Statement(StmtSelect stmtSelect, Token from) {
    this.location = new SourceLocation(from);
    this.base = StatementBase.SIF;
    this.stmtSelect = stmtSelect;
  }

  public Statement(Token lbrace, Token rbrace, List<BlockItem> block) {
    this.location = new SourceLocation(lbrace); // TODO: range loc
    this.base = StatementBase.SCOMPOUND;
    this.block = block;
  }

  public Statement(Token from, AsmList asmlist) {
    this.location = new SourceLocation(from);
    this.base = StatementBase.SASM;
    this.asmlist = asmlist;
  }

  public Statement(Token from, StmtDefault default_stmt) {
    this.location = new SourceLocation(from);
    this.base = StatementBase.SDEFAULT;
    this.stmtDefault = default_stmt;
  }

  // break, continue
  public Statement(Token from, StatementBase base) {
    this.location = new SourceLocation(from);
    this.base = base;
  }

  public Statement(Token from, StmtSwitch switch_stmt) {
    this.location = new SourceLocation(from);
    this.base = StatementBase.SSWITCH;
    this.stmtSwitch = switch_stmt;
  }

  public Statement(Token from, StmtCase case_stmt) {
    this.location = new SourceLocation(from);
    this.base = StatementBase.SCASE;
    this.stmtCase = case_stmt;
  }

  public StatementBase getBase() {
    return base;
  }

  public SourceLocation getLocation() {
    return location;
  }

  public List<BlockItem> getBlock() {
    return block;
  }

  public StmtSwitch getStmtSwitch() {
    return stmtSwitch;
  }

  public StmtCase getStmtCase() {
    return stmtCase;
  }

  public StmtDefault getStmtDefault() {
    return stmtDefault;
  }

  public StmtFor getStmtFor() {
    return stmtFor;
  }

  public StmtWhileDo getStmtDoWhile() {
    return stmtDoWhile;
  }

  public Expression getStmtExpr() {
    return stmtExpr;
  }

  public StmtSelect getStmtSelect() {
    return stmtSelect;
  }

  public StmtLabel getStmtLabel() {
    return stmtLabel;
  }

  public AsmList getAsmlist() {
    return asmlist;
  }

}
