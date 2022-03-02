package ast.builders;

import static jscan.tokenize.T.TOKEN_NUMBER;
import static jscan.tokenize.T.T_AND;
import static jscan.tokenize.T.T_AND_EQUAL;
import static jscan.tokenize.T.T_DIVIDE;
import static jscan.tokenize.T.T_DIVIDE_EQUAL;
import static jscan.tokenize.T.T_LSHIFT;
import static jscan.tokenize.T.T_LSHIFT_EQUAL;
import static jscan.tokenize.T.T_MINUS;
import static jscan.tokenize.T.T_MINUS_EQUAL;
import static jscan.tokenize.T.T_OR;
import static jscan.tokenize.T.T_OR_EQUAL;
import static jscan.tokenize.T.T_PERCENT;
import static jscan.tokenize.T.T_PERCENT_EQUAL;
import static jscan.tokenize.T.T_PLUS;
import static jscan.tokenize.T.T_PLUS_EQUAL;
import static jscan.tokenize.T.T_RSHIFT;
import static jscan.tokenize.T.T_RSHIFT_EQUAL;
import static jscan.tokenize.T.T_TIMES;
import static jscan.tokenize.T.T_TIMES_EQUAL;
import static jscan.tokenize.T.T_XOR;
import static jscan.tokenize.T.T_XOR_EQUAL;

import java.util.HashMap;
import java.util.Map;

import ast.tree.Expression;
import ast.types.CTypeImpl;
import jscan.literals.IntLiteral;
import jscan.literals.IntLiteralType;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import jscan.utils.NullChecker;

public abstract class ExprUtil {

  public static Token copyTokenAddNewType(Token from, T newtype, String newvalue) {
    NullChecker.check(from, newtype, newvalue);

    Token ntoken = new Token(from);
    ntoken.setType(newtype);
    ntoken.setValue(newvalue);
    return ntoken;
  }

  public static Token plusOperator(Token from) {
    return copyTokenAddNewType(from, T.T_PLUS, "+");
  }

  public static Token derefOperator(Token from) {
    return copyTokenAddNewType(from, T.T_TIMES, "*");
  }

  public static Token assignOperator(Token from) {
    return copyTokenAddNewType(from, T.T_ASSIGN, "=");
  }

  public static Token dotOperator(Token from) {
    return copyTokenAddNewType(from, T.T_DOT, ".");
  }

  public static Expression createNumericConst(Token from, Integer intValue) {
    IntLiteral e = new IntLiteral(intValue.toString(), '+', "", "", "", "", '\0', IntLiteralType.I32);
    e.setLong(intValue);
    return new Expression(e, from);
  }

  public static Expression digitZero(Token from) {
    NullChecker.check(from);

    IntLiteral number = new IntLiteral(0, IntLiteralType.I32);
    Expression ret = new Expression(number, copyTokenAddNewType(from, TOKEN_NUMBER, "0"));

    ret.setResultType(CTypeImpl.TYPE_INT);
    return ret;
  }

  public static Expression digitOne(Token from) {
    NullChecker.check(from);

    IntLiteral number = new IntLiteral(1, IntLiteralType.I32);
    Expression ret = new Expression(number, copyTokenAddNewType(from, TOKEN_NUMBER, "1"));

    ret.setResultType(CTypeImpl.TYPE_INT);
    return ret;
  }

  private static Map<T, T> asopmap = new HashMap<T, T>();
  static {
    asopmap.put(T_TIMES_EQUAL, T_TIMES);
    asopmap.put(T_PERCENT_EQUAL, T_PERCENT);
    asopmap.put(T_DIVIDE_EQUAL, T_DIVIDE);
    asopmap.put(T_PLUS_EQUAL, T_PLUS);
    asopmap.put(T_MINUS_EQUAL, T_MINUS);
    asopmap.put(T_LSHIFT_EQUAL, T_LSHIFT);
    asopmap.put(T_RSHIFT_EQUAL, T_RSHIFT);
    asopmap.put(T_AND_EQUAL, T_AND);
    asopmap.put(T_XOR_EQUAL, T_XOR);
    asopmap.put(T_OR_EQUAL, T_OR);
  }

  public static Token getOperatorFromCompAssign(Token from) {
    Token ntoken = new Token(from);
    ntoken.setType(asopmap.get(from.getType()));
    switch (ntoken.getType()) {
    case T_TIMES:
      ntoken.setValue("*");
      break;
    case T_PERCENT:
      ntoken.setValue("%");
      break;
    case T_DIVIDE:
      ntoken.setValue("/");
      break;
    case T_PLUS:
      ntoken.setValue("+");
      break;
    case T_MINUS:
      ntoken.setValue("-");
      break;
    case T_LSHIFT:
      ntoken.setValue("<<");
      break;
    case T_RSHIFT:
      ntoken.setValue(">>");
      break;
    case T_AND:
      ntoken.setValue("&");
      break;
    case T_XOR:
      ntoken.setValue("^");
      break;
    case T_OR:
      ntoken.setValue("|");
      break;
    default:
      break;
    }
    return ntoken;
  }

}