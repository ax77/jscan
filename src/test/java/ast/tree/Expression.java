package ast.tree;

import static ast.tree.ExpressionBase.EASSIGN;
import static ast.tree.ExpressionBase.EBINARY;
import static ast.tree.ExpressionBase.ECAST;
import static ast.tree.ExpressionBase.ECOMMA;
import static ast.tree.ExpressionBase.ECOMPSEL;
import static ast.tree.ExpressionBase.EFCALL;
import static ast.tree.ExpressionBase.EPOSTINCDEC;
import static ast.tree.ExpressionBase.EPREINCDEC;
import static ast.tree.ExpressionBase.EPRIMARY_GENERIC;
import static ast.tree.ExpressionBase.ETERNARY;
import static ast.tree.ExpressionBase.EUNARY;

import java.util.List;

import ast.symtab.CSymbol;
import ast.types.CStructField;
import ast.types.CType;
import jscan.literals.IntLiteral;
import jscan.tokenize.CStr;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;

public class Expression {

  private static final int LHS_INDEX = 0;
  private static final int RHS_INDEX = 1;
  private static final int CND_INDEX = 2;

  private final ExpressionBase base; // what union contains
  private CType resultType; // what expression doe's after evaluation
  private final Token token; // operator, position
  private final Expression tree[];

  private List<Initializer> initializerList; // (resultType) { initializer-list } compound literal
  private CStructField field;
  private List<Expression> arglist;

  // primary
  private CStr cstring;
  private IntLiteral cnumber;
  private CSymbol symbol;

  private void assertBaseIsOneOf(ExpressionBase... bases) {
    boolean contains = false;
    for (ExpressionBase b : bases) {
      if (base == b) {
        contains = true;
        break;
      }
    }
    if (!contains) {
      throw new AstParseException("you want get tree-node that doe's not exists for this base: " + base.toString());
    }
  }

  public Expression getLhs() {
    assertBaseIsOneOf(ECOMPSEL, EUNARY, EPREINCDEC, EPOSTINCDEC, EASSIGN, EBINARY, ETERNARY, ECOMMA, ECAST, EFCALL,
        EPRIMARY_GENERIC);
    return tree[LHS_INDEX];
  }

  public Expression getRhs() {
    assertBaseIsOneOf(EASSIGN, EBINARY, ETERNARY, ECOMMA);
    return tree[RHS_INDEX];
  }

  private void setLhs(Expression e) {
    tree[LHS_INDEX] = e;
  }

  private void setRhs(Expression e) {
    tree[RHS_INDEX] = e;
  }

  public Expression getCnd() {
    assertBaseIsOneOf(ETERNARY);
    return tree[CND_INDEX];
  }

  private void setCnd(Expression condition) {
    tree[CND_INDEX] = condition;
  }

  private Expression[] emptyTree() {
    return new Expression[3];
  }

  // pre-post inc-dec
  public Expression(ExpressionBase base, Token op, Expression lhs) {
    this.base = base;
    this.token = op;
    this.tree = emptyTree();

    setLhs(lhs);
  }

  public Token getToken() {
    return token;
  }

  // binary, asssign, comma, array-subscript
  public Expression(ExpressionBase base, Expression lhs, Expression rhs, Token token) {
    this.base = base;
    this.token = token;
    this.tree = emptyTree();

    setLhs(lhs);
    setRhs(rhs);
  }

  // unary
  public Expression(Token op, Expression lhs) {
    this.base = ExpressionBase.EUNARY;
    this.token = op;
    this.tree = emptyTree();

    setLhs(lhs);
  }

  public Expression(CType typename, List<Initializer> initializerList, Token token) {
    this.tree = emptyTree();
    this.token = token;
    this.base = ExpressionBase.ECOMPLITERAL;
    this.resultType = typename;
    this.initializerList = initializerList;
  }

  public Expression(Expression function, List<Expression> arguments, Token token) {
    this.tree = emptyTree();
    this.token = token;
    this.base = ExpressionBase.EFCALL;
    setLhs(function);
    this.arglist = arguments;
  }

  public Expression(CType typename, Expression tocast, Token token) {
    this.tree = emptyTree();
    this.token = token;
    this.base = ExpressionBase.ECAST;
    this.resultType = typename;
    setLhs(tocast);
  }

  // (*a) -> x
  // a . x
  public Expression(Expression postfis, Token operator, CStructField fieldName) {
    this.tree = emptyTree();
    this.token = operator;
    this.base = ExpressionBase.ECOMPSEL;

    setLhs(postfis);
    this.field = fieldName;
  }

  public Expression(Expression condition, Expression branchTrue, Expression branchFalse, Token token) {
    this.tree = emptyTree();
    this.token = token;
    this.base = ExpressionBase.ETERNARY;

    setCnd(condition);
    setLhs(branchTrue);
    setRhs(branchFalse);
  }

  public Expression(CSymbol e, Token token) {
    this.tree = emptyTree();
    this.token = token;
    this.base = ExpressionBase.EPRIMARY_IDENT;
    this.symbol = e;
  }

  public Expression(CStr cstring, Token token) {
    this.tree = emptyTree();
    this.token = token;
    this.base = ExpressionBase.EPRIMARY_STRING;
    this.cstring = cstring;
  }

  public Expression(Expression genericSelectionResult, Token token) {
    this.tree = emptyTree();
    this.token = token;
    this.base = ExpressionBase.EPRIMARY_GENERIC;

    setLhs(genericSelectionResult);
  }

  public Expression(IntLiteral number, Token from) {
    this.tree = emptyTree();
    this.token = from;
    this.base = ExpressionBase.EPRIMARY_NUMBER;
    this.cnumber = number;
  }

  public CType getResultType() {
    return resultType;
  }

  public void setResultType(CType resultType) {
    this.resultType = resultType;
  }

  public ExpressionBase getBase() {
    return base;
  }

  public List<Initializer> getInitializerList() {
    return initializerList;
  }

  public CStructField getField() {
    return field;
  }

  public CSymbol getSymbol() {
    return symbol;
  }

  public List<Expression> getArglist() {
    return arglist;
  }

  public Expression[] getTree() {
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
      return "(" + getLhs().toString() + getToken().getValue() + field.name.getName() + ")";
    }

    case ECAST: {
      return "(" + resultType.toString() + ") " + "(" + getLhs().toString() + ")";
    }

    case EFCALL: {
      StringBuilder sb = new StringBuilder();
      sb.append(getLhs().toString() + "(");

      int argc = 0;
      for (Expression e : arglist) {
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
      return symbol.name.getName();
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
      throw new AstParseException("unknown: " + base.toString());
    }
    }

  }

  public IntLiteral getCnumber() {
    return cnumber;
  }

  public void setCnumber(IntLiteral cnumber) {
    this.cnumber = cnumber;
  }

  public boolean isIntegerZero() {
    return base == ExpressionBase.EPRIMARY_NUMBER && cnumber.isInteger() && cnumber.getClong() == 0;
  }

  public boolean isModifiableLvalue() {
    // TODO : XXX
    return true;
  }

}
