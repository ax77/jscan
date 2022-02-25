package ast.expr.sem;

import jscan.tokenize.T;
import ast.errors.ParseException;
import ast.expr.CExpression;
import ast.expr.CExpressionBase;
import ast.parse.Parse;

public class ConstexprEval {

  @SuppressWarnings("unused")
  private final Parse parser; // need for symtab. like enums.

  public ConstexprEval(Parse parser) {
    this.parser = parser;
  }

  public long ce(CExpression expression) {
    CExpressionBase base = expression.getBase();

    if (base == CExpressionBase.ETERNARY) {

      //1)
      long cond = ce(expression.getCnd());
      if (cond != 0) {
        return ce(expression.getLhs());
      } else {
        return ce(expression.getRhs());
      }
    }

    if (base == CExpressionBase.EBINARY) {
      T op = expression.getToken().getType();

      //2)
      if (op == T.T_OR_OR) {
        return ((ce(expression.getLhs()) != 0 || ce(expression.getRhs()) != 0) ? 1 : 0);
      }
      //3)
      if (op == T.T_AND_AND) {
        return ((ce(expression.getLhs()) != 0 && ce(expression.getRhs()) != 0) ? 1 : 0);
      }
      //4)
      if (op == T.T_OR) {
        return ce(expression.getLhs()) | ce(expression.getRhs());
      }
      //5)
      if (op == T.T_XOR) {
        return ce(expression.getLhs()) ^ ce(expression.getRhs());
      }
      //6)
      if (op == T.T_AND) {
        return ce(expression.getLhs()) & ce(expression.getRhs());
      }
      //7)
      if (op == T.T_EQ) {
        return ((ce(expression.getLhs()) == ce(expression.getRhs())) ? 1 : 0);
      }
      //8)
      if (op == T.T_NE) {
        return ((ce(expression.getLhs()) != ce(expression.getRhs())) ? 1 : 0);
      }

      //9)
      if (op == T.T_LT) {
        return ((ce(expression.getLhs()) < ce(expression.getRhs())) ? 1 : 0);
      }
      //10)
      if (op == T.T_GT) {
        return ((ce(expression.getLhs()) > ce(expression.getRhs())) ? 1 : 0);
      }
      //11)
      if (op == T.T_LE) {
        return ((ce(expression.getLhs()) <= ce(expression.getRhs())) ? 1 : 0);
      }
      //12)
      if (op == T.T_GE) {
        return ((ce(expression.getLhs()) >= ce(expression.getRhs())) ? 1 : 0);
      }

      //14)
      if (op == T.T_LSHIFT) {
        return ce(expression.getLhs()) << ce(expression.getRhs());
      }
      //15
      if (op == T.T_RSHIFT) {
        return ce(expression.getLhs()) >> ce(expression.getRhs());
      }

      //16)
      if (op == T.T_PLUS) {
        return ce(expression.getLhs()) + ce(expression.getRhs());
      }
      //17)
      if (op == T.T_MINUS) {
        return ce(expression.getLhs()) - ce(expression.getRhs());
      }

      //18)
      if (op == T.T_TIMES) {
        return ce(expression.getLhs()) * ce(expression.getRhs());
      }
      //19)
      if (op == T.T_DIVIDE) {
        return ce(expression.getLhs()) / ce(expression.getRhs());
      }
      //20)
      if (op == T.T_PERCENT) {
        return ce(expression.getLhs()) % ce(expression.getRhs());
      }

      throw new ParseException("incorrect binary operator " + op.toString());
    }

    if (base == CExpressionBase.EUNARY) {
      T t = expression.getToken().getType();

      //21)
      if (t == T.T_MINUS) {
        return -ce(expression.getLhs());
      }
      //22)
      if (t == T.T_PLUS) {
        return ce(expression.getLhs());
      }

      //23)
      if (t == T.T_TILDE) {
        return ~ce(expression.getLhs());
      }
      //24)
      if (t == T.T_EXCLAMATION) {
        long r = ce(expression.getLhs());
        return (r == 0 ? 1 : 0);
      }

      throw new ParseException("incorrect unary operator " + t.toString());
    }

    ///////////////////////////////////////////
    // all others...

    // 25) TODO:
    if (base == CExpressionBase.ECOMMA) {
      return ce(expression.getRhs());
    }

    if (base == CExpressionBase.EPRIMARY_NUMBER) {
      //TODO:
      return expression.getCnumber().getClong();
    }

    if (base == CExpressionBase.EPRIMARY_IDENT) {
      //TODO:
      return expression.getSymbol().getEnumvalue();
    }

    // try expand _Generic
    // int a1[_Generic(0, int: 1, short: 2, float: 3, default: 4) == 1 ? 1 : -1];
    //
    if (base == CExpressionBase.EPRIMARY_GENERIC) {
      final CExpression genericSelectionResult = expression.getLhs();
      return ce(genericSelectionResult);
    }

    if (base == CExpressionBase.ECAST) {
      return ce(expression.getLhs());
    }

    throw new ParseException("not a constant expression " + base.toString());
  }
}
