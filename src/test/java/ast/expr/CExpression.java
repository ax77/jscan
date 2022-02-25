package ast.expr;

import static ast.expr.CExpressionBase.EASSIGN;
import static ast.expr.CExpressionBase.EBINARY;
import static ast.expr.CExpressionBase.ECAST;
import static ast.expr.CExpressionBase.ECOMMA;
import static ast.expr.CExpressionBase.ECOMPSEL;
import static ast.expr.CExpressionBase.EFCALL;
import static ast.expr.CExpressionBase.EPOSTINCDEC;
import static ast.expr.CExpressionBase.EPREINCDEC;
import static ast.expr.CExpressionBase.EPRIMARY_GENERIC;
import static ast.expr.CExpressionBase.ETERNARY;
import static ast.expr.CExpressionBase.EUNARY;

import java.util.List;

import jscan.literals.IntLiteral;
import jscan.literals.IntLiteralType;
import jscan.sourceloc.SourceLocation;
import jscan.tokenize.Token;
import ast.decls.Initializer;
import ast.errors.ParseException;
import ast.parse.ILocation;
import ast.symtab.elements.CSymbol;
import ast.symtab.elements.NumericConstant;
import ast.symtab.elements.StringConstant;
import ast.types.CStructField;
import ast.types.CType;

abstract class NodeTemp {
  private static long iter = 0;

  public static long gettemp() {
    return iter++;
  }
}

public class CExpression implements ILocation {

  private static final int LHS_INDEX = 0;
  private static final int RHS_INDEX = 1;
  private static final int CND_INDEX = 2;

  private final CExpressionBase base; // what union contains
  private final long tname; // just unique id. for codegen.
  private final SourceLocation location;

  private CType resultType; // what expression doe's after evaluation
  private final Token token; // operator, position
  private final CExpression tree[];

  private List<Initializer> initializerList; // (resultType) { initializer-list } compound literal
  private CStructField field;
  private List<CExpression> arglist;

  // primary
  private StringConstant cstring;
  private NumericConstant cnumber;
  private CSymbol symbol;

  private void assertBaseIsOneOf(CExpressionBase... bases) {
    boolean contains = false;
    for (CExpressionBase b : bases) {
      if (base == b) {
        contains = true;
        break;
      }
    }
    if (!contains) {
      throw new ParseException("you want get tree-node that doe's not exists for this base: " + base.toString());
    }
  }

  public CExpression getLhs() {
    assertBaseIsOneOf(ECOMPSEL, EUNARY, EPREINCDEC, EPOSTINCDEC, EASSIGN, EBINARY, ETERNARY, ECOMMA, ECAST, EFCALL,
        EPRIMARY_GENERIC);
    return tree[LHS_INDEX];
  }

  public CExpression getRhs() {
    assertBaseIsOneOf(EASSIGN, EBINARY, ETERNARY, ECOMMA);
    return tree[RHS_INDEX];
  }

  private void setLhs(CExpression e) {
    tree[LHS_INDEX] = e;
  }

  private void setRhs(CExpression e) {
    tree[RHS_INDEX] = e;
  }

  public CExpression getCnd() {
    assertBaseIsOneOf(ETERNARY);
    return tree[CND_INDEX];
  }

  private void setCnd(CExpression condition) {
    tree[CND_INDEX] = condition;
  }

  private CExpression[] emptyTree() {
    return new CExpression[3];
  }

  // pre-post inc-dec
  public CExpression(CExpressionBase base, Token op, CExpression lhs) {
    this.base = base;
    this.tname = NodeTemp.gettemp();
    this.location = new SourceLocation(op);
    this.token = op;
    this.tree = emptyTree();

    setLhs(lhs);
  }

  public Token getToken() {
    return token;
  }

  // binary, asssign, comma, array-subscript
  public CExpression(CExpressionBase base, CExpression lhs, CExpression rhs, Token token) {
    this.base = base;
    this.tname = NodeTemp.gettemp();
    this.location = new SourceLocation(token);
    this.token = token;
    this.tree = emptyTree();

    setLhs(lhs);
    setRhs(rhs);
  }

  // unary
  public CExpression(Token op, CExpression lhs) {
    this.base = CExpressionBase.EUNARY;
    this.tname = NodeTemp.gettemp();
    this.location = new SourceLocation(op);
    this.token = op;
    this.tree = emptyTree();

    setLhs(lhs);
  }

  public CExpression(CType typename, List<Initializer> initializerList, Token token) {
    this.tname = NodeTemp.gettemp();
    this.tree = emptyTree();
    this.location = new SourceLocation(token);
    this.token = token;
    this.base = CExpressionBase.ECOMPLITERAL;
    this.resultType = typename;
    this.initializerList = initializerList;
  }

  public CExpression(CExpression function, List<CExpression> arguments, Token token) {
    this.tname = NodeTemp.gettemp();
    this.tree = emptyTree();
    this.location = new SourceLocation(token);
    this.token = token;
    this.base = CExpressionBase.EFCALL;
    setLhs(function);
    this.arglist = arguments;
  }

  public CExpression(CType typename, CExpression tocast, Token token) {
    this.tname = NodeTemp.gettemp();
    this.tree = emptyTree();
    this.location = new SourceLocation(token);
    this.token = token;
    this.base = CExpressionBase.ECAST;
    this.resultType = typename;
    setLhs(tocast);
  }

  // (*a) -> x
  // a . x
  public CExpression(CExpression postfis, Token operator, CStructField fieldName) {
    this.tname = NodeTemp.gettemp();
    this.location = new SourceLocation(operator);
    this.tree = emptyTree();
    this.token = operator;
    this.base = CExpressionBase.ECOMPSEL;

    setLhs(postfis);
    this.field = fieldName;
  }

  public CExpression(CExpression condition, CExpression branchTrue, CExpression branchFalse, Token token) {
    this.tname = NodeTemp.gettemp();
    this.location = new SourceLocation(token);
    this.tree = emptyTree();
    this.token = token;
    this.base = CExpressionBase.ETERNARY;

    setCnd(condition);
    setLhs(branchTrue);
    setRhs(branchFalse);
  }

  public CExpression(CSymbol e, Token token) {
    this.tname = NodeTemp.gettemp();
    this.location = new SourceLocation(token);
    this.tree = emptyTree();
    this.token = token;
    this.base = CExpressionBase.EPRIMARY_IDENT;
    this.symbol = e;
  }

  public CExpression(IntLiteral e, Token token) {

    this.tname = NodeTemp.gettemp();
    this.location = new SourceLocation(token);
    this.tree = emptyTree();
    this.token = token;
    this.base = CExpressionBase.EPRIMARY_NUMBER;

    //todo:
    //NumericConstant number = null;
    //if (e.isIntegerKind()) {
    //  number = new NumericConstant(e.getClong(), e.getNumtype());
    //} else {
    //  number = new NumericConstant(e.getCdouble(), e.getNumtype());
    //}

    NumericConstant number = new NumericConstant(e.getClong(), IntLiteralType.I32);
    this.cnumber = number;
  }

  public CExpression(StringConstant cstring, Token token) {
    this.tname = NodeTemp.gettemp();
    this.location = new SourceLocation(token);
    this.tree = emptyTree();
    this.token = token;
    this.base = CExpressionBase.EPRIMARY_STRING;
    this.cstring = cstring;
  }

  public CExpression(CExpression genericSelectionResult, Token token) {
    this.tname = NodeTemp.gettemp();
    this.location = new SourceLocation(token);
    this.tree = emptyTree();
    this.token = token;
    this.base = CExpressionBase.EPRIMARY_GENERIC;

    setLhs(genericSelectionResult);
  }

  public CExpression(NumericConstant number, Token from) {
    this.tname = NodeTemp.gettemp();
    this.location = new SourceLocation(from);
    this.tree = emptyTree();
    this.token = from;
    this.base = CExpressionBase.EPRIMARY_NUMBER;
    this.cnumber = number;
  }

  public CType getResultType() {
    return resultType;
  }

  public void setResultType(CType resultType) {
    this.resultType = resultType;
  }

  public CExpressionBase getBase() {
    return base;
  }

  public List<Initializer> getInitializerList() {
    return initializerList;
  }

  public void setInitializerList(List<Initializer> initializerList) {
    this.initializerList = initializerList;
  }

  public CStructField getField() {
    return field;
  }

  public CSymbol getSymbol() {
    return symbol;
  }

  public void setSymbol(CSymbol symbol) {
    this.symbol = symbol;
  }

  public List<CExpression> getArglist() {
    return arglist;
  }

  public void setArglist(List<CExpression> arglist) {
    this.arglist = arglist;
  }

  public long getTname() {
    return tname;
  }

  public CExpression[] getTree() {
    return tree;
  }

  private String tokenTos(Token t) {
    return " " + t.getValue() + " ";
  }

  @Override
  public String toString() {

    switch (base) {

    case EASSIGN: {
      return "(" + getLhs().toString().trim() + tokenTos(getToken()) + getRhs().toString().trim() + ")";
    }

    case EBINARY: {
      return "(" + getLhs().toString() + tokenTos(getToken()) + getRhs().toString() + ")";
    }

    case ECOMMA: {
      return getLhs().toString() + tokenTos(getToken()) + getRhs().toString();
    }

    case ETERNARY: {
      return "(" + getCnd().toString().trim() + " ? " + getLhs().toString().trim() + " : " + getRhs().toString().trim()
          + ")";
    }

    case EUNARY: {
      return "(" + getToken().getValue() + getLhs().toString() + ")";
    }

    case ECOMPSEL: {
      return "(" + getLhs().toString() + getToken().getValue() + field.getName().getName() + ")";
    }

    case ECAST: {
      return "(" + resultType.toString() + ") " + "(" + getLhs().toString() + ")";
    }

    case EFCALL: {
      StringBuilder sb = new StringBuilder();
      sb.append(getLhs().toString() + "(");

      int argc = 0;
      for (CExpression e : arglist) {
        sb.append(e.toString());
        if (argc < arglist.size() - 1) {
          sb.append(",");
        }
        ++argc;
      }

      sb.append(")");
      return sb.toString();
    }

    case EPREINCDEC: {
      return "(" + getToken().getValue() + getLhs().toString() + ")";
    }

    case EPOSTINCDEC: {
      return "(" + getLhs().toString() + getToken().getValue() + ")";
    }

    case ECOMPLITERAL: {
      return "(" + resultType.toString() + ") {" + initializerList.toString() + " }";
    }

    case EPRIMARY_IDENT: {
      return symbol.getName().getName();
    }

    case EPRIMARY_NUMBER: {
      if (cnumber.isInteger()) {
        return String.format("%d", cnumber.getClong());
      } else {
        return String.format("%f", cnumber.getCdouble());
      }
    }

    case EPRIMARY_STRING: {
      return cstring.toString();
    }

    case EPRIMARY_GENERIC: {
      return getLhs().toString();
    }

    default: {
      throw new ParseException("unknown: " + base.toString());
    }
    }

  }

  public NumericConstant getCnumber() {
    return cnumber;
  }

  public void setCnumber(NumericConstant cnumber) {
    this.cnumber = cnumber;
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

  public boolean isIntegerZero() {
    return base == CExpressionBase.EPRIMARY_NUMBER && cnumber.isInteger() && cnumber.getClong() == 0;
  }

  public boolean isModifiableLvalue() {
    // TODO : XXX
    return true;
  }

}
