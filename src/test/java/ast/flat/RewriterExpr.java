package ast.flat;

import static ast.tree.ExpressionBase.EASSIGN;
import static ast.tree.ExpressionBase.EBINARY;
import static ast.tree.ExpressionBase.EPRIMARY_IDENT;
import static ast.tree.ExpressionBase.EPRIMARY_NUMBER;
import static ast.tree.ExpressionBase.EUNARY;

import java.util.ArrayList;
import java.util.List;

import ast.flat.ir.FlatCodeItem;
import ast.flat.items.AssignVarBinop;
import ast.flat.items.AssignVarNum;
import ast.flat.items.AssignVarUnop;
import ast.flat.items.AssignVarVar;
import ast.flat.items.StoreFieldLiteral;
import ast.flat.items.StoreVarLiteral;
import ast.flat.rvalues.Binop;
import ast.flat.rvalues.FieldAccess;
import ast.flat.rvalues.Leaf;
import ast.flat.rvalues.Unop;
import ast.flat.rvalues.Var;
import ast.symtab.CSymbol;
import ast.tree.Expression;
import ast.tree.ExpressionBase;
import ast.tree.Initializer;
import ast.types.CType;
import ast.types.CTypeImpl;
import jscan.literals.IntLiteral;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;
import jscan.utils.NullChecker;

public class RewriterExpr {

  private final List<FlatCodeItem> temproraries;
  private final List<FlatCodeItem> rawResult;

  public RewriterExpr(Expression expr) {
    NullChecker.check(expr);

    if (expr.getResultType() == null) {
    }

    this.temproraries = new ArrayList<>();
    this.rawResult = new ArrayList<>();

    gen(expr);
  }

  public RewriterExpr(CSymbol var) {
    NullChecker.check(var);

    this.temproraries = new ArrayList<>();
    this.rawResult = new ArrayList<>();

    List<Initializer> inits = var.getInitializer();

    if (inits == null) {
      throw new AstParseException(var.getLocationToString() + ":expect initializer: " + var.toString());
    }

    if (inits.size() > 1) {
      todo("init-list");
    }

    Initializer init = inits.get(0);
    gen(init.getInit());

    FlatCodeItem srcItem = getLast();
    Var lvaluevar = VarCreator.copyVarDecl(var);
    Var rvaluevar = srcItem.getDest();

    if (isLiteralItem(srcItem)) {
      final Leaf u = makeLeaf(srcItem);
      final StoreVarLiteral s = new StoreVarLiteral(lvaluevar, u, true);
      rawResult.add(new FlatCodeItem(s));
    } else {
      AssignVarVar assignVarVar = new AssignVarVar(lvaluevar, rvaluevar);
      rawResult.add(new FlatCodeItem(assignVarVar));
    }

  }

  private void todo(String string) {
    throw new AstParseException("todo: " + string);
  }

  public List<FlatCodeItem> getRawResult() {
    return rawResult;
  }

  private FlatCodeItem getLast() {
    return rawResult.get(rawResult.size() - 1);
  }

  public String getLastResultNameToString() {
    FlatCodeItem item = getLast();
    return item.getDest().getName().getName();
  }

  private void genRaw(FlatCodeItem item) {
    temproraries.add(0, item);
    rawResult.add(item);
  }

  private FlatCodeItem popCode() {
    return temproraries.remove(0);
  }

  private boolean isLiteralItem(final FlatCodeItem srcItem) {
    return srcItem.isAssignVarNum() || srcItem.isAssignVarVar();
  }

  private List<Var> genArgs(final List<Expression> arguments) {

    for (Expression arg : arguments) {
      gen(arg);
    }

    List<Var> args = new ArrayList<>();
    for (int i = 0; i < arguments.size(); i++) {
      final FlatCodeItem item = popCode();
      final Var var = getVar(item);
      args.add(0, var);
    }

    return args;
  }

  private Leaf makeLeaf(final FlatCodeItem srcItem) {
    if (!isLiteralItem(srcItem)) {
      throw new AstParseException("not a leaf: " + srcItem);
    }

    srcItem.setIgnore(srcItem.getDest());

    if (srcItem.isAssignVarNum()) {
      return new Leaf(srcItem.getAssignVarNum().getLiteral());
    }

    if (srcItem.isAssignVarVar()) {
      return new Leaf(srcItem.getAssignVarVar().getRvalue());
    }

    // unreachable
    return null;
  }

  private void store(CType resultType) {

    final FlatCodeItem srcItem = popCode();
    final FlatCodeItem dstItem = popCode();

    if (dstItem.isAssignVarVar()) {

      // it was: a = b
      // we need: b = srv
      final Var dst = dstItem.getAssignVarVar().getRvalue();
      final Var src = srcItem.getDest();

      if (isLiteralItem(srcItem)) {

        final Leaf u = makeLeaf(srcItem);
        final StoreVarLiteral s = new StoreVarLiteral(dst, u, false);
        genRaw(new FlatCodeItem(s));

      }

      else {
        // the var here may be a result of a function-call, etc.
        final StoreVarLiteral u = new StoreVarLiteral(dst, new Leaf(src), false);
        genRaw(new FlatCodeItem(u));
      }

    }

    else if (dstItem.isAssignVarFieldAccess()) {

      // it was: a = b.c
      // we need: b.c = src

      final FieldAccess dst = dstItem.getAssignVarFieldAccess().getRvalue();
      final Var src = srcItem.getDest();

      if (isLiteralItem(srcItem)) {

        final Leaf u = makeLeaf(srcItem);
        final StoreFieldLiteral s = new StoreFieldLiteral(dst, u);
        genRaw(new FlatCodeItem(s));

      }

      else {
        // the var here may be a result of a function-call, etc.
        final StoreFieldLiteral u = new StoreFieldLiteral(dst, new Leaf(src));
        genRaw(new FlatCodeItem(u));
      }

    }

    else {
      throw new AstParseException("unimplimented store for dst: " + dstItem.toString());
    }

    dstItem.setIgnore(dstItem.getDest()); // we can easily ignore this one

  }

  private Var getVar(final FlatCodeItem item) {
    if (item.isAssignVarVar()) {
      item.setIgnore(item.getAssignVarVar().getRvalue());
      return item.getAssignVarVar().getRvalue();
    }
    return item.getDest();
  }

  private Binop makeBinop(String op, final FlatCodeItem Ritem, final FlatCodeItem Litem) {

    final Var lvarRes = getVar(Litem);
    final Var rvarRes = getVar(Ritem);
    final Binop binop = new Binop(lvarRes, op, rvarRes);

    return binop;
  }

  private Unop makeUnop(String op, final FlatCodeItem Litem) {
    return new Unop(op, getVar(Litem));
  }

  private void gen(Expression e) {
    NullChecker.check(e);
    ExpressionBase base = e.getBase();

    if (base == EASSIGN) {

      final Expression lvalue = e.getLhs();
      gen(lvalue);

      final Expression rvalue = e.getRhs();
      gen(rvalue);

      store(e.getResultType());
    }

    else if (base == EBINARY) {

      final Token op = e.getToken();

      gen(e.getLhs());
      gen(e.getRhs());

      final FlatCodeItem Ritem = popCode();
      final FlatCodeItem Litem = popCode();

      final Binop binop = makeBinop(op.getValue(), Ritem, Litem);

      final Var justNewVar = VarCreator.justNewVar(e.getResultType());
      FlatCodeItem item = new FlatCodeItem(new AssignVarBinop(justNewVar, binop));
      genRaw(item);

    }

    else if (base == EUNARY) {
      final Token op = e.getToken();
      gen(e.getLhs());

      final FlatCodeItem Litem = popCode();
      final Unop unop = makeUnop(op.getValue(), Litem);

      FlatCodeItem item = new FlatCodeItem(new AssignVarUnop(VarCreator.justNewVar(e.getResultType()), unop));
      genRaw(item);
    }

    else if (base == EPRIMARY_IDENT) {
      CSymbol var = e.getSymbol();
      final Var lvalueTmp = VarCreator.justNewVar(var.getType());
      final Var rvalueTmp = VarCreator.copyVarDecl(var);
      final FlatCodeItem item = new FlatCodeItem(new AssignVarVar(lvalueTmp, rvalueTmp));
      genRaw(item);

    }

    else if (base == ExpressionBase.EPRIMARY_STRING) {

    }

    else if (base == EPRIMARY_NUMBER) {
      final IntLiteral number = e.getCnumber();
      final Var lhsVar = VarCreator.justNewVar(CTypeImpl.TYPE_INT); //TODO:tac
      AssignVarNum assignVarNum = new AssignVarNum(lhsVar, number);
      genRaw(new FlatCodeItem(assignVarNum));
    }

    else {
      throw new AstParseException(base.toString() + ": unimplemented");
    }

  }

}
