package ast.tree;

import java.util.List;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.Token;

public class Statement {

  //TODO: range loc

  private final StatementBase base;
  private final SourceLocation location;

  private List<BlockItem> compound;

  // switch-cases
  private Sswitch sswitch;
  private Scase scase;
  private Sdefault sdefault;
  private List<Token> asmlist;

  // for
  private Declaration decl;
  private Expression init;
  private Expression test;
  private Expression step;
  private Statement loop;

  // return expr
  // expr-stmt
  private Expression expr;

  // if's
  private Expression ifexpr;
  private Statement ifstmt;
  private Statement ifelse;

  // label:
  private FunctionDefinition function;
  private Ident label;
  private Statement labelstmt;

  public Statement(Token from, Declaration decl, Expression init, Expression test, Expression step, Statement loop) {
    this.location = new SourceLocation(from);
    this.base = StatementBase.SFOR;

    this.decl = decl;
    this.init = init;
    this.test = test;
    this.step = step;
    this.loop = loop;
  }

  // do loop while test
  // while (test) loop

  public Statement(Token from, StatementBase base, Expression test, Statement loop) {
    this.location = new SourceLocation(from);
    this.base = base;

    this.test = test;
    this.loop = loop;
  }

  // return expr
  // return ;
  // expr-stmt
  public Statement(Token from, StatementBase base, Expression expr) {
    this.location = new SourceLocation(from);
    this.base = base;

    this.expr = expr;
  }

  // label: stmt
  // goto label;
  public Statement(Token from, StatementBase base, FunctionDefinition function, Ident label, Statement labelstmt) {
    this.location = new SourceLocation(from);
    this.base = base;

    this.function = function;
    this.label = label;
    this.labelstmt = labelstmt;
  }

  public Statement(Token from, Expression ifexpr, Statement ifstmt, Statement ifelse) {
    this.location = new SourceLocation(from);
    this.base = StatementBase.SIF;

    this.ifexpr = ifexpr;
    this.ifstmt = ifstmt;
    this.ifelse = ifelse;
  }

  public Statement(Token lbrace, Token rbrace, List<BlockItem> blockItemList) {
    this.location = new SourceLocation(lbrace); // TODO: range loc
    this.base = StatementBase.SCOMPOUND;

    this.compound = blockItemList;
  }

  public Statement(Token from, List<Token> asmlist) {
    this.location = new SourceLocation(from);
    this.base = StatementBase.SASM;
    this.asmlist = asmlist;
  }

  public Statement(Token from, Sdefault default_stmt) {
    this.location = new SourceLocation(from);
    this.base = StatementBase.SDEFAULT;
    this.sdefault = default_stmt;
  }

  // break, continue
  public Statement(Token from, StatementBase base) {
    this.location = new SourceLocation(from);
    this.base = base;
  }

  public Statement(Token from, Sswitch switch_stmt) {
    this.location = new SourceLocation(from);
    this.base = StatementBase.SSWITCH;
    this.sswitch = switch_stmt;
  }

  public Statement(Token from, Scase case_stmt) {
    this.location = new SourceLocation(from);
    this.base = StatementBase.SCASE;
    this.scase = case_stmt;
  }

  public Sswitch getSswitch() {
    return sswitch;
  }

  public void setSswitch(Sswitch sswitch) {
    this.sswitch = sswitch;
  }

  public Scase getScase() {
    return scase;
  }

  public void setScase(Scase scase) {
    this.scase = scase;
  }

  public Sdefault getSdefault() {
    return sdefault;
  }

  public void setSdefault(Sdefault sdefault) {
    this.sdefault = sdefault;
  }

  public StatementBase getBase() {
    return base;
  }

  public List<BlockItem> getCompound() {
    return compound;
  }

  public void setCompound(List<BlockItem> compound) {
    this.compound = compound;
  }

  public List<Token> getAsmlist() {
    return asmlist;
  }

  public void setAsmlist(List<Token> asmlist) {
    this.asmlist = asmlist;
  }

  public Declaration getDecl() {
    return decl;
  }

  public void setDecl(Declaration decl) {
    this.decl = decl;
  }

  public Expression getInit() {
    return init;
  }

  public void setInit(Expression init) {
    this.init = init;
  }

  public Expression getTest() {
    return test;
  }

  public void setTest(Expression test) {
    this.test = test;
  }

  public Expression getStep() {
    return step;
  }

  public void setStep(Expression step) {
    this.step = step;
  }

  public Statement getLoop() {
    return loop;
  }

  public void setLoop(Statement loop) {
    this.loop = loop;
  }

  public Expression getExpr() {
    return expr;
  }

  public void setExpr(Expression expr) {
    this.expr = expr;
  }

  public Expression getIfexpr() {
    return ifexpr;
  }

  public void setIfexpr(Expression ifexpr) {
    this.ifexpr = ifexpr;
  }

  public Statement getIfstmt() {
    return ifstmt;
  }

  public void setIfstmt(Statement ifstmt) {
    this.ifstmt = ifstmt;
  }

  public Statement getIfelse() {
    return ifelse;
  }

  public void setIfelse(Statement ifelse) {
    this.ifelse = ifelse;
  }

  public FunctionDefinition getFunction() {
    return function;
  }

  public void setFunction(FunctionDefinition function) {
    this.function = function;
  }

  public Ident getLabel() {
    return label;
  }

  public void setLabel(Ident label) {
    this.label = label;
  }

  public Statement getLabelstmt() {
    return labelstmt;
  }

  public void setLabelstmt(Statement labelstmt) {
    this.labelstmt = labelstmt;
  }

}
