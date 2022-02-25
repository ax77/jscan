package ast.stmt.main;

import java.util.List;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.Token;
import ast.decls.Declaration;
import ast.expr.CExpression;
import ast.parse.ILocation;
import ast.stmt.Scase;
import ast.stmt.Sdefault;
import ast.stmt.Sswitch;
import ast.unit.BlockItem;
import ast.unit.FunctionDefinition;

public class CStatement implements ILocation {

  //TODO: range loc

  private final CStatementBase base;
  private final SourceLocation location;

  private List<BlockItem> compound;

  // switch-cases
  private Sswitch sswitch;
  private Scase scase;
  private Sdefault sdefault;
  private List<Token> asmlist;

  // for
  private Declaration decl;
  private CExpression init;
  private CExpression test;
  private CExpression step;
  private CStatement loop;

  // return expr
  // expr-stmt
  private CExpression expr;

  // if's
  private CExpression ifexpr;
  private CStatement ifstmt;
  private CStatement ifelse;

  // label:
  private FunctionDefinition function;
  private Ident label;
  private CStatement labelstmt;

  public CStatement(Token from, Declaration decl, CExpression init, CExpression test, CExpression step, CStatement loop) {
    this.location = new SourceLocation(from);
    this.base = CStatementBase.SFOR;

    this.decl = decl;
    this.init = init;
    this.test = test;
    this.step = step;
    this.loop = loop;
  }

  // do loop while test
  // while (test) loop

  public CStatement(Token from, CStatementBase base, CExpression test, CStatement loop) {
    this.location = new SourceLocation(from);
    this.base = base;

    this.test = test;
    this.loop = loop;
  }

  // return expr
  // return ;
  // expr-stmt
  public CStatement(Token from, CStatementBase base, CExpression expr) {
    this.location = new SourceLocation(from);
    this.base = base;

    this.expr = expr;
  }

  // label: stmt
  // goto label;
  public CStatement(Token from, CStatementBase base, FunctionDefinition function, Ident label, CStatement labelstmt) {
    this.location = new SourceLocation(from);
    this.base = base;

    this.function = function;
    this.label = label;
    this.labelstmt = labelstmt;
  }

  public CStatement(Token from, CExpression ifexpr, CStatement ifstmt, CStatement ifelse) {
    this.location = new SourceLocation(from);
    this.base = CStatementBase.SIF;

    this.ifexpr = ifexpr;
    this.ifstmt = ifstmt;
    this.ifelse = ifelse;
  }

  public CStatement(Token lbrace, Token rbrace, List<BlockItem> blockItemList) {
    this.location = new SourceLocation(lbrace); // TODO: range loc
    this.base = CStatementBase.SCOMPOUND;

    this.compound = blockItemList;
  }

  public CStatement(Token from, List<Token> asmlist) {
    this.location = new SourceLocation(from);
    this.base = CStatementBase.SASM;
    this.asmlist = asmlist;
  }

  public CStatement(Token from, Sdefault default_stmt) {
    this.location = new SourceLocation(from);
    this.base = CStatementBase.SDEFAULT;
    this.sdefault = default_stmt;
  }

  // break, continue
  public CStatement(Token from, CStatementBase base) {
    this.location = new SourceLocation(from);
    this.base = base;
  }

  public CStatement(Token from, Sswitch switch_stmt) {
    this.location = new SourceLocation(from);
    this.base = CStatementBase.SSWITCH;
    this.sswitch = switch_stmt;
  }

  public CStatement(Token from, Scase case_stmt) {
    this.location = new SourceLocation(from);
    this.base = CStatementBase.SCASE;
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

  public CStatementBase getBase() {
    return base;
  }

  @Override
  public SourceLocation getLocation() {
    return location;
  }

  @Override
  public String getLocationToString() {
    return location.toString();
  }

  @Override
  public int getLocationLine() {
    return location.getLine();
  }

  @Override
  public int getLocationColumn() {
    return location.getColumn();
  }

  @Override
  public String getLocationFile() {
    return location.getFilename();
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

  public CExpression getInit() {
    return init;
  }

  public void setInit(CExpression init) {
    this.init = init;
  }

  public CExpression getTest() {
    return test;
  }

  public void setTest(CExpression test) {
    this.test = test;
  }

  public CExpression getStep() {
    return step;
  }

  public void setStep(CExpression step) {
    this.step = step;
  }

  public CStatement getLoop() {
    return loop;
  }

  public void setLoop(CStatement loop) {
    this.loop = loop;
  }

  public CExpression getExpr() {
    return expr;
  }

  public void setExpr(CExpression expr) {
    this.expr = expr;
  }

  public CExpression getIfexpr() {
    return ifexpr;
  }

  public void setIfexpr(CExpression ifexpr) {
    this.ifexpr = ifexpr;
  }

  public CStatement getIfstmt() {
    return ifstmt;
  }

  public void setIfstmt(CStatement ifstmt) {
    this.ifstmt = ifstmt;
  }

  public CStatement getIfelse() {
    return ifelse;
  }

  public void setIfelse(CStatement ifelse) {
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

  public CStatement getLabelstmt() {
    return labelstmt;
  }

  public void setLabelstmt(CStatement labelstmt) {
    this.labelstmt = labelstmt;
  }

}
