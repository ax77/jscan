package preproc.preprocess;

import static tokenize.T.TOKEN_NUMBER;
import static tokenize.T.T_COLON;
import static tokenize.T.T_DIVIDE;
import static tokenize.T.T_EXCLAMATION;
import static tokenize.T.T_LEFT_PAREN;
import static tokenize.T.T_MINUS;
import static tokenize.T.T_OR_OR;
import static tokenize.T.T_PERCENT;
import static tokenize.T.T_PLUS;
import static tokenize.T.T_QUESTION;
import static tokenize.T.T_RIGHT_PAREN;
import static tokenize.T.T_TILDE;
import static tokenize.T.T_TIMES;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import literals.IntLiteral;
import literals.IntLiteralParser;
import main.Tokenlist;
import tokenize.T;
import tokenize.Token;

enum PP_ExprNodeType {
    e_cnd,
      e_lor,
      e_land,
      e_bor,
      e_bxor,
      e_band,
      e_eq,
      e_neq,
      e_lt,
      e_gt,
      e_le,
      e_ge,
      e_shl,
      e_shr,
      e_add,
      e_sub,
      e_mul,
      e_div,
      e_mod,
      e_unminus,
      e_unplus,
      e_uncompl,
      e_unnot,
      e_prim_num,
      e_prim_char,
}

class PP_ExprNode {

  private final PP_ExprNodeType type;

  private PP_ExprNode cond;
  private PP_ExprNode lhs;
  private PP_ExprNode rhs;

  private Token tok; // for location errors and other things.

  public PP_ExprNode(PP_ExprNodeType type, List<Token> sequence) {
    this.type = type;
  }

  public PP_ExprNode(PP_ExprNodeType type, Token tok, PP_ExprNode left, PP_ExprNode right) {
    this.type = type;
    this.tok = tok;
    this.lhs = left;
    this.rhs = right;
  }

  public PP_ExprNode(PP_ExprNodeType type, Token tok) {
    this.type = type;
    this.tok = tok;
  }

  public PP_ExprNode(PP_ExprNodeType type, PP_ExprNode left, PP_ExprNode right) {
    this.type = type;
    this.lhs = left;
    this.rhs = right;
  }

  public PP_ExprNode(PP_ExprNodeType type, Token tok, PP_ExprNode left) {
    this.type = type;
    this.tok = tok;
    this.lhs = left;
  }

  public PP_ExprNode(PP_ExprNodeType type, PP_ExprNode cond, PP_ExprNode branch_true, PP_ExprNode branch_false) {
    this.type = type;
    this.setCond(cond);
    this.lhs = branch_true;
    this.rhs = branch_false;
  }

  public PP_ExprNodeType getType() {
    return type;
  }

  public PP_ExprNode lhs() {
    return lhs;
  }

  public PP_ExprNode rhs() {
    return rhs;
  }

  public Token getTok() {
    return tok;
  }

  public void setTok(Token tok) {
    this.tok = tok;
  }

  public PP_ExprNode getCond() {
    return cond;
  }

  public void setCond(PP_ExprNode cond) {
    this.cond = cond;
  }

}

public class PP_expr2 {

  private final static Set<T> CPP_CORRECT_OPERATORS = new HashSet<T>();
  private final static Set<T> CPP_CORRECT_SPECIALS = new HashSet<T>();

  private final Tokenlist input;
  private Token tok;

  private T tp() {
    return tok.getType();
  }

  private void move() {
    tok = input.next();
  }

  private void checkedMove(T expected) {
    if (tp() == expected) {
      move();
    } else {
      throw new ScanExc(tok.loc() + " fail: expect " + expected + ", but was " + tp().toString());
    }
  }

  public PP_expr2(List<Token> tokenlist) {
    this.input = new Tokenlist(tokenlist);
    preEvaluationFastCheck();
    move();
  }

  public long parse() {
    PP_ExprNode e = e_expression();
    long ce = ce(e);
    return ce;
  }

  static {

    //
    CPP_CORRECT_SPECIALS.add(T.TOKEN_IDENT); // defined(x), defined x, true, false
    CPP_CORRECT_SPECIALS.add(T.TOKEN_CHAR);
    CPP_CORRECT_SPECIALS.add(T.TOKEN_NUMBER);

    //
    CPP_CORRECT_OPERATORS.add(T.T_EQ); /* == */
    CPP_CORRECT_OPERATORS.add(T.T_NE); /* != */

    CPP_CORRECT_OPERATORS.add(T.T_AND_AND); /* && */
    CPP_CORRECT_OPERATORS.add(T.T_OR_OR); /* || */

    CPP_CORRECT_OPERATORS.add(T.T_EXCLAMATION); /* ! */
    CPP_CORRECT_OPERATORS.add(T.T_TILDE); /* ~ */

    CPP_CORRECT_OPERATORS.add(T.T_RSHIFT); /* >> */
    CPP_CORRECT_OPERATORS.add(T.T_LSHIFT); /* << */

    CPP_CORRECT_OPERATORS.add(T.T_LT); /* < */
    CPP_CORRECT_OPERATORS.add(T.T_LE); /* <= */
    CPP_CORRECT_OPERATORS.add(T.T_GT); /* > */
    CPP_CORRECT_OPERATORS.add(T.T_GE); /* >= */

    CPP_CORRECT_OPERATORS.add(T.T_AND); /* & */
    CPP_CORRECT_OPERATORS.add(T.T_OR); /* | */
    CPP_CORRECT_OPERATORS.add(T.T_XOR); /* ^ */

    CPP_CORRECT_OPERATORS.add(T.T_COMMA); /* , */
    CPP_CORRECT_OPERATORS.add(T.T_ASSIGN); /* = */

    CPP_CORRECT_OPERATORS.add(T.T_QUESTION); /* ? */
    CPP_CORRECT_OPERATORS.add(T.T_COLON); /* : */

    CPP_CORRECT_OPERATORS.add(T.T_LEFT_PAREN); /* ( */
    CPP_CORRECT_OPERATORS.add(T.T_RIGHT_PAREN); /* ) */

    CPP_CORRECT_OPERATORS.add(T.T_PLUS); /* + */
    CPP_CORRECT_OPERATORS.add(T.T_MINUS); /* - */
    CPP_CORRECT_OPERATORS.add(T.T_TIMES); /* * */
    CPP_CORRECT_OPERATORS.add(T.T_DIVIDE); /* / */
    CPP_CORRECT_OPERATORS.add(T.T_PERCENT); /* % */
  }

  private static Map<T, PP_ExprNodeType> opmap;
  static {
    opmap = new TreeMap<T, PP_ExprNodeType>();
    opmap.put(T_PLUS, PP_ExprNodeType.e_unplus);
    opmap.put(T_MINUS, PP_ExprNodeType.e_unminus);
    opmap.put(T_TILDE, PP_ExprNodeType.e_uncompl);
    opmap.put(T_EXCLAMATION, PP_ExprNodeType.e_unnot);
  }

  private boolean preEvaluationFastCheck() {

    List<Token> expression = new ArrayList<Token>();
    expression.addAll(input.getList());

    // TODO: location...
    if (expression.isEmpty()) {
      throw new ScanExc("error : constant expression must not be empty.");
    }

    for (Token t : expression) {

      boolean flag = t.ofType(T.TOKEN_EOF) || CPP_CORRECT_OPERATORS.contains(t.getType())
          || CPP_CORRECT_SPECIALS.contains(t.getType());

      if (!flag) {
        throw new ScanExc(t.loc() + " :error : token :" + t.getValue() + " is not valid in preprocessor expressions");
      }
    }

    return true;
  }

  private PP_ExprNode e_expression() {
    PP_ExprNode res = e_cnd();
    for (;;) {
      // TODO: warning???
      if (tp() == T.T_COMMA) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_cnd, res, e_cnd());
      } else {
        break;
      }
    }
    return res;
  }

  private PP_ExprNode e_cnd() {
    PP_ExprNode res = e_lor();
    if (tp() == T_QUESTION) {
      move();
      PP_ExprNode btrue = e_expression();
      checkedMove(T_COLON);
      res = new PP_ExprNode(PP_ExprNodeType.e_cnd, res, btrue, e_cnd());
    }
    return res;
  }

  private PP_ExprNode e_lor() {
    PP_ExprNode res = e_land();
    for (;;) {
      if (tp() == T_OR_OR) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_lor, res, e_land());
      } else {
        break;
      }
    }
    return res;
  }

  private PP_ExprNode e_land() {
    PP_ExprNode res = e_bor();
    for (;;) {
      if (tp() == T.T_AND_AND) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_land, res, e_bor());
      } else {
        break;
      }
    }
    return res;
  }

  private PP_ExprNode e_bor() {
    PP_ExprNode res = e_bxor();
    for (;;) {
      if (tp() == T.T_OR) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_bor, res, e_bxor());
      } else {
        break;
      }
    }
    return res;
  }

  private PP_ExprNode e_bxor() {
    PP_ExprNode res = e_band();
    for (;;) {
      if (tp() == T.T_XOR) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_bxor, res, e_band());
      } else {
        break;
      }
    }
    return res;
  }

  private PP_ExprNode e_band() {
    PP_ExprNode res = e_equality();
    for (;;) {
      if (tp() == T.T_AND) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_band, res, e_equality());
      } else {
        break;
      }
    }
    return res;
  }

  private PP_ExprNode e_equality() {
    PP_ExprNode res = e_relational();
    for (;;) {
      if (tp() == T.T_EQ) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_eq, res, e_relational());
      } else if (tp() == T.T_NE) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_neq, res, e_relational());
      } else {
        break;
      }
    }
    return res;
  }

  private PP_ExprNode e_relational() {
    PP_ExprNode res = e_shift();
    for (;;) {
      if (tp() == T.T_LT) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_lt, res, e_shift());
      } else if (tp() == T.T_GT) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_gt, res, e_shift());
      } else if (tp() == T.T_LE) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_le, res, e_shift());
      } else if (tp() == T.T_GE) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_ge, res, e_shift());
      } else {
        break;
      }
    }
    return res;
  }

  private PP_ExprNode e_shift() {
    PP_ExprNode res = e_add();
    for (;;) {
      if (tp() == T.T_LSHIFT) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_shl, res, e_add());
      } else if (tp() == T.T_RSHIFT) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_shr, res, e_add());
      } else {
        break;
      }
    }
    return res;
  }

  private PP_ExprNode e_add() {
    PP_ExprNode res = e_mul();
    for (;;) {
      if (tp() == T_PLUS) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_add, res, e_mul());
      } else if (tp() == T_MINUS) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_sub, res, e_mul());
      } else {
        break;
      }
    }
    return res;
  }

  private PP_ExprNode e_mul() {
    PP_ExprNode res = parseCastExpression();
    for (;;) {
      if (tp() == T_TIMES) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_mul, res, parseCastExpression());
      } else if (tp() == T_DIVIDE) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_div, res, parseCastExpression());
      } else if (tp() == T_PERCENT) {
        move();
        res = new PP_ExprNode(PP_ExprNodeType.e_mod, res, parseCastExpression());
      } else {
        break;
      }
    }
    return res;
  }

  private PP_ExprNode parseCastExpression() {
    PP_ExprNode res = e_unary();
    return res;
  }

  //@formatter:off
  private boolean isUnaryOperator() {
    return tok.ofType(T.T_PLUS)
        || tok.ofType(T_MINUS) 
        || tok.ofType(T_TILDE)
        || tok.ofType(T.T_EXCLAMATION);
  }
  //@formatter:on

  private PP_ExprNode e_unary() {
    if (isUnaryOperator()) {
      Token operator = tok;
      move();
      return new PP_ExprNode(opmap.get(operator.getType()), operator, parseCastExpression());
    }
    return e_postfix();
  }

  private PP_ExprNode e_postfix() {
    PP_ExprNode res = e_primary();
    return res;
  }

  private PP_ExprNode e_primary() {
    if (tp() == TOKEN_NUMBER) {
      Token saved = tok;
      move();
      PP_ExprNode e = new PP_ExprNode(PP_ExprNodeType.e_prim_num, saved);
      return e;
    }
    if (tp() == T.TOKEN_CHAR) {
      Token saved = tok;
      move();
      PP_ExprNode e = new PP_ExprNode(PP_ExprNodeType.e_prim_char, saved);
      return e;
    }
    if (tp() == T_LEFT_PAREN) {
      move();
      PP_ExprNode e = e_expression();
      checkedMove(T_RIGHT_PAREN);
      return e;
    }
    throw new ScanExc("Prim. error.");
  }

  private long ce(PP_ExprNode e) {

    if (e == null) {
      return 0;
    }

    PP_ExprNodeType t = e.getType();

    if (t == PP_ExprNodeType.e_prim_num) {
      Token numtok = e.getTok();

      IntLiteral strtox = IntLiteralParser.parse(numtok.getValue());
      return strtox.getClong();
    }

    if (t == PP_ExprNodeType.e_prim_char) {
      Token numtok = e.getTok();
      long res = numtok.getCharconstant().getV();
      return res;
    }

    //1)
    if (t == PP_ExprNodeType.e_cnd) {
      long cond = ce(e.getCond());
      if (cond != 0) {
        return ce(e.lhs());
      } else {
        return ce(e.rhs());
      }
    }
    //2)
    if (t == PP_ExprNodeType.e_lor) {
      return ((ce(e.lhs()) != 0 || ce(e.rhs()) != 0) ? 1 : 0);
    }
    //3)
    if (t == PP_ExprNodeType.e_land) {
      return ((ce(e.lhs()) != 0 && ce(e.rhs()) != 0) ? 1 : 0);
    }
    //4)
    if (t == PP_ExprNodeType.e_bor) {
      return ce(e.lhs()) | ce(e.rhs());
    }
    //5)
    if (t == PP_ExprNodeType.e_bxor) {
      return ce(e.lhs()) ^ ce(e.rhs());
    }
    //6)
    if (t == PP_ExprNodeType.e_band) {
      return ce(e.lhs()) & ce(e.rhs());
    }
    //7)
    if (t == PP_ExprNodeType.e_eq) {
      return ((ce(e.lhs()) == ce(e.rhs())) ? 1 : 0);
    }
    //8)
    if (t == PP_ExprNodeType.e_neq) {
      return ((ce(e.lhs()) != ce(e.rhs())) ? 1 : 0);
    }

    //9)
    if (t == PP_ExprNodeType.e_lt) {
      return ((ce(e.lhs()) < ce(e.rhs())) ? 1 : 0);
    }
    //10)
    if (t == PP_ExprNodeType.e_gt) {
      return ((ce(e.lhs()) > ce(e.rhs())) ? 1 : 0);
    }
    //11)
    if (t == PP_ExprNodeType.e_le) {
      return ((ce(e.lhs()) <= ce(e.rhs())) ? 1 : 0);
    }
    //12)
    if (t == PP_ExprNodeType.e_ge) {
      return ((ce(e.lhs()) >= ce(e.rhs())) ? 1 : 0);
    }

    //14)
    if (t == PP_ExprNodeType.e_shl) {
      return ce(e.lhs()) << ce(e.rhs());
    }
    //15
    if (t == PP_ExprNodeType.e_shr) {
      return ce(e.lhs()) >> ce(e.rhs());
    }

    //16)
    if (t == PP_ExprNodeType.e_add) {
      return ce(e.lhs()) + ce(e.rhs());
    }
    //17)
    if (t == PP_ExprNodeType.e_sub) {
      return ce(e.lhs()) - ce(e.rhs());
    }

    //18)
    if (t == PP_ExprNodeType.e_mul) {
      return ce(e.lhs()) * ce(e.rhs());
    }
    //19)
    if (t == PP_ExprNodeType.e_div) {
      return ce(e.lhs()) / ce(e.rhs());
    }
    //20)
    if (t == PP_ExprNodeType.e_mod) {
      return ce(e.lhs()) % ce(e.rhs());
    }

    //21)
    if (t == PP_ExprNodeType.e_unminus) {
      return -ce(e.lhs());
    }
    //22)
    if (t == PP_ExprNodeType.e_unplus) {
      return ce(e.lhs());
    }

    //23)
    if (t == PP_ExprNodeType.e_uncompl) {
      return ~ce(e.lhs());
    }
    //24)
    if (t == PP_ExprNodeType.e_unnot) {
      long r = ce(e.lhs());
      return (r == 0 ? 1 : 0);
    }

    throw new ScanExc("error.");

  }

}
