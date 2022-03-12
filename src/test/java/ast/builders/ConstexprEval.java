package ast.builders;

import ast.parse.Parse;
import ast.symtab.CSymbol;
import ast.symtab.CSymbolBase;
import ast.tree.Expression;
import ast.tree.ExpressionBase;
import jscan.tokenize.T;
import jscan.utils.AstParseException;

public class ConstexprEval {

  @SuppressWarnings("unused")
  private final Parse parser; // need for symtab. like enums.

  public ConstexprEval(Parse parser) {
    this.parser = parser;
  }

  public long ce(Expression expression) {
    ExpressionBase base = expression.getBase();

    if (base == ExpressionBase.ETERNARY) {

      //1)
      long cond = ce(expression.getCnd());
      if (cond != 0) {
        return ce(expression.getLhs());
      } else {
        return ce(expression.getRhs());
      }
    }

    if (base == ExpressionBase.EBINARY) {
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

      throw new AstParseException("incorrect binary operator " + op.toString());
    }

    if (base == ExpressionBase.EUNARY) {
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

      throw new AstParseException("incorrect unary operator " + t.toString());
    }

    ///////////////////////////////////////////
    // all others...

    // 25) TODO:
    if (base == ExpressionBase.ECOMMA) {
      return ce(expression.getRhs());
    }

    if (base == ExpressionBase.EPRIMARY_NUMBER) {
      //TODO:
      return expression.getCnumber().getClong();
    }

    if (base == ExpressionBase.EPRIMARY_IDENT) {
      CSymbol sym = expression.getSymbol();
      if (sym.base != CSymbolBase.SYM_ENUM_CONST) {
        parser.perror("not a enum constant");
      }
      return sym.enumConst.value;
    }

    // try expand _Generic
    // int a1[_Generic(0, int: 1, short: 2, float: 3, default: 4) == 1 ? 1 : -1];
    //
    if (base == ExpressionBase.EPRIMARY_GENERIC) {
      final Expression genericSelectionResult = expression.getLhs();
      return ce(genericSelectionResult);
    }

    if (base == ExpressionBase.ECAST) {
      return ce(expression.getLhs());
    }

    throw new AstParseException("not a constant expression " + base.toString());
  }
}
