package preproc.preprocess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import preproc.Sym;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

enum PP_ifline_kind {
    K_ERROR, K_IF, K_IFDEF, K_IFNDEF, K_ELIF, K_ELSE, K_ENDIF
}

class PP_ifline {

  private List<Token> condition;
  private List<Token> tokens;
  private PP_ifline_kind lkind;

  public PP_ifline() {
    condition = new ArrayList<Token>();
    tokens = new ArrayList<Token>();
    lkind = PP_ifline_kind.K_ERROR;
  }

  public void addAllToCondition(List<Token> list) {
    condition.addAll(list);
  }

  public void addToTokenlist(Token t) {
    tokens.add(t);
  }

  public List<Token> getCondition() {
    return condition;
  }

  public void setCondition(List<Token> condition) {
    this.condition = condition;
  }

  public List<Token> getTokens() {
    return tokens;
  }

  public void setTokens(List<Token> tokens) {
    this.tokens = tokens;
  }

  public PP_ifline_kind getLkind() {
    return lkind;
  }

  public void setLkind(PP_ifline_kind lkind) {
    this.lkind = lkind;
  }

}

public class PP_if2 implements PP_directive {
  private final Scan scanner;

  //@formatter:off
  //  if_line  -> { condition, list<token>, level }
  //  if_lines -> { list<if_line> }
  //
  //  #if 0
  //    tok1 tok2 tok3
  //  #elif (1+1==2)
  //    #if 0
  //    #elif 1
  //      tok4
  //    #endif
  //  #else
  //    tok5
  //  #endif
  //
  //  1) level = 0, pop_noppdirective()
  //    * if see '#' + bol + "if"     -> ++level
  //    * if see '#' + bol + "ifdef"  -> ++level
  //    * if see '#' + bol + "ifndef" -> ++level
  //    * if see '#' + bol + "endif"  -> if (--level ==0) -> end
  //
  //  if_lines[] = {
  //    // if_line 0 (if)
  //    { .cond = 0 || 0,
  //      .list = { tok1, tok2, tok3, },
  //      .level = 0
  //    }, 
  //    // if_line 1 (elif)
  //    { .cond = (1+1==2),
  //      .list = { #, if, 0, #, elif, 1, tok4, #, endif, },
  //      .level = 0
  //    }
  //    // if_line 2 (else)
  //    { .cond = NULL,
  //      .list = { tok5, },
  //      .level = 0
  //    }
  //  };
  //
  //  #if 0
  //    #nothing 1
  //    #nothing 2
  //  #elif 0
  //    #nothing 3
  //    #nothing 4
  //    #nothing 5
  //  #else
  //    #if 1
  //      ok
  //    #elif 1
  //      error
  //    #else
  //      error
  //    #endif
  //  #endif
  //@formatter:on

  public PP_if2(Scan s) {
    scanner = s;
  }

  private List<Token> condition() {
    List<Token> r = new ArrayList<Token>();

    while (!scanner.isEmpty()) {
      Token t = scanner.pop_noppdirective();
      if (t.isNewLine()) {
        r.add(t);
        break;
      }
      r.add(t);
    }

    return r;
  }

  private boolean ifdefsTrue(PP_ifline topline) {

    List<Token> toks = topline.getCondition();
    Token first = toks.remove(0);
    first.checkId();

    if (toks.size() > 1) {
      throw new ScanExc(first.loc() + "error: extra tokens at #ifdef directive");
    }

    Ident mac = first.getIdent();
    Sym sym = mac.getSym();

    if (topline.getLkind().equals(PP_ifline_kind.K_IFDEF)) {
      return sym != null;
    }

    if (topline.getLkind().equals(PP_ifline_kind.K_IFNDEF)) {
      return sym == null;
    }

    return false;
  }

  private boolean expressionTrue(PP_ifline topline, Token pp) throws IOException {

    List<Token> toks = topline.getCondition();

    List<Token> processed = new ArrayList<Token>();
    Stack<Token> iterator = new Stack<Token>();

    for (int jj = toks.size(); --jj >= 0;) {
      iterator.push(toks.get(jj));
    }

    while (!iterator.isEmpty()) {
      Token t = iterator.pop();
      if (t.ofType(T.TOKEN_EOF)) {
        break;
      }
      if (t.ofType(T.TOKEN_IDENT) && t.getValue().equals("defined")) {

        Token nx = iterator.pop();
        if (nx.ofType(T.T_LEFT_PAREN)) {
          Token id = iterator.pop();
          Sym mac = id.getIdent().getSym();
          Token ntok = new Token(id);
          ntok.setType(T.TOKEN_NUMBER);
          ntok.setValue(mac == null ? "0" : "1");
          processed.add(ntok);

          Token rparen = iterator.pop();
          if (!rparen.ofType(T.T_RIGHT_PAREN)) {
            throw new ScanExc(t.loc() + " error: missing rparen in defined operator");
          }
          continue;

        } else {
          Sym mac = nx.getIdent().getSym();
          Token ntok = new Token(nx);
          ntok.setType(T.TOKEN_NUMBER);
          ntok.setValue(mac == null ? "0" : "1");
          processed.add(ntok);
          continue;
        }
      }
      processed.add(t);
    }

    List<Token> clean = new ArrayList<Token>();
    Scan sc = new Scan(processed);
    for (;;) {
      Token etok = sc.get();
      if (etok.ofType(T.TOKEN_EOF)) {
        break;
      }
      if (etok.ofType(T.TOKEN_IDENT)) {
        etok.setType(T.TOKEN_NUMBER);
        etok.setValue("0");
      }
      clean.add(etok);
    }

    //PP_expr ce = new PP_expr(pp, clean);
    //long res = ce.evaluate();

    long res = new PP_expr2(clean).parse();
    return res != 0;
  }

  private void pushToplevels(List<Token> topline) {

    for (int JJ = topline.size(); --JJ >= 0;) {
      Token iflev = topline.get(JJ);
      scanner.push(iflev);
    }

  }

  @Override
  public boolean scan(Token pp) throws IOException {

    int ifstate_if = 1;
    int ifstate_elif = 2;
    int ifstate_else = 4;
    int state = ifstate_if;

    List<PP_ifline> iflines = new ArrayList<PP_ifline>();

    PP_ifline firstCondition = new PP_ifline();

    if (pp.ofType(T.PT_HIF)) {
      firstCondition.setLkind(PP_ifline_kind.K_IF);

    } else if (pp.ofType(T.PT_HIFDEF)) {
      firstCondition.setLkind(PP_ifline_kind.K_IFDEF);

    } else if (pp.ofType(T.PT_HIFNDEF)) {
      firstCondition.setLkind(PP_ifline_kind.K_IFNDEF);
    }

    List<Token> conditionFirst = condition();

    firstCondition.addAllToCondition(conditionFirst);
    iflines.add(firstCondition);

    List<Token> ifstack = new LinkedList<Token>();
    ifstack.add(0, pp);

    int level = 0;
    while (!scanner.isEmpty()) {

      Token first = scanner.pop_noppdirective();
      PP_ifline ifline = iflines.get(iflines.size() - 1);

      //  1) level = 0, pop_noppdirective()
      //    * if see '#' + bol + "if"     -> ++level
      //    * if see '#' + bol + "ifdef"  -> ++level
      //    * if see '#' + bol + "ifndef" -> ++level
      //    * if see '#' + bol + "endif"  -> if (--level ==0) -> end

      if (first.ofType(T.T_SHARP) && first.isAtBol()) {

        if (first.isNewLine()) {
          continue; // just sharp is ok
        }

        Token second = scanner.pop_noppdirective();
        String whatname = second.getValue();

        if (whatname.equals("if") || whatname.equals("ifdef") || whatname.equals("ifndef")) {
          ifline.addToTokenlist(first);
          ifline.addToTokenlist(second);
          ++level;

          ifstack.add(0, second);
          continue;
        }

        if (whatname.equals("elif")) {
          if (level == 0) {

            if ((state & ifstate_else) == ifstate_else) {
              throw new ScanExc(second.loc() + "error: #elif after #else");
            }

            PP_ifline elif_node = new PP_ifline();
            elif_node.setLkind(PP_ifline_kind.K_ELIF);

            List<Token> condition = condition();

            elif_node.addAllToCondition(condition);
            iflines.add(elif_node);

            state |= ifstate_elif;

          } else {
            ifline.addToTokenlist(first);
            ifline.addToTokenlist(second);
          }

          continue;
        }

        if (whatname.equals("else")) {
          if (level == 0) {

            if ((state & ifstate_else) == ifstate_else) {
              throw new ScanExc(second.loc() + "error: #else after #else");
            }

            PP_ifline else_node = new PP_ifline();
            else_node.setLkind(PP_ifline_kind.K_ELSE);

            iflines.add(else_node);
            state |= ifstate_else;

          } else {
            ifline.addToTokenlist(first);
            ifline.addToTokenlist(second);
          }
          continue;
        }

        if (whatname.equals("endif")) {
          if (level == 0) {
            break;
          } else {
            ifline.addToTokenlist(first);
            ifline.addToTokenlist(second);
            --level;
          }

          ifstack.remove(0);
          continue;
        }

        // 
        ifline.addToTokenlist(first);
        ifline.addToTokenlist(second);
        continue;

      }

      if (first.ofType(T.TOKEN_EOF)) {
        if (!ifstack.isEmpty()) {
          String location = "\n";
          for (Token tmptok : ifstack) {
            location += tmptok.loc() + "\n";
          }
          throw new ScanExc(location + " unterminated #if...");
        }
      }

      ifline.addToTokenlist(first);

    }

    for (PP_ifline line : iflines) {

      PP_ifline_kind kind = line.getLkind();

      // always true
      //
      if (kind.equals(PP_ifline_kind.K_ELSE)) {
        pushToplevels(line.getTokens());
        return true;
      }

      //
      if (kind.equals(PP_ifline_kind.K_IF) || kind.equals(PP_ifline_kind.K_ELIF)) {
        if (expressionTrue(line, pp)) {
          pushToplevels(line.getTokens());
          return true;
        }
      }

      if (kind.equals(PP_ifline_kind.K_IFDEF) || kind.equals(PP_ifline_kind.K_IFNDEF)) {
        if (ifdefsTrue(line)) {
          pushToplevels(line.getTokens());
          return true;
        }
      }

    }

    return true;
  }

}
