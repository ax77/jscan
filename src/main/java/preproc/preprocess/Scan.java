package preproc.preprocess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import main.Env;
import preproc.Error;
import preproc.ErrorCode;
import preproc.Sym;
import sourceloc.SourceLocation;
import tokenize.Fcategory;
import tokenize.Stream;
import tokenize.T;
import tokenize.Token;

public final class Scan {
  private final List<Token> tokens;
  private final int size;
  private int offset;
  private boolean inlast;
  private LinkedList<Token> rescan;

  private int incLevelVisual = 0;
  private Stack<String> incstack = new Stack<String>();

  //MARK:{,##__VA_ARGS__}
  //GNU {,##__VA_ARGS__}
  //
  //#define F2(X, ...) f(10, X, ## __VA_ARGS__)
  //#define H2(...) f(10, ## __VA_ARGS__)
  //
  //F2(a, b)   =>   f(10, a, b)  ArgInfo [info=vararg_present_normal] 
  //F2(a, )    =>   f(10, a, )   ArgInfo [info=vararg_present_empty] 
  //F2(a)      =>   f(10, a)     ArgInfo [info=vararg_not_present]                   => delete COMMA here
  //H2(a)      =>   f(10, a)     ArgInfo [info=vararg_single_arg_and_present_normal] 
  //H2()       =>   f(10)        ArgInfo [info=vararg_single_arg_and_present_empty]  => delete COMMA here
  //
  enum ArgInfoVararg {
      noinfo,
        vararg_present_normal,
        vararg_not_present, // actual parameters don't have variadic part at all...
        vararg_present_empty, // this mean, that macros have formal parameters, instead of variadic, and variadic part is empty

        vararg_single_arg_and_present_empty,
        vararg_single_arg_and_present_normal,
  }

  class ArgInfo {
    private ArgInfoVararg info;

    public ArgInfo() {
      this.info = ArgInfoVararg.noinfo;
    }

    public ArgInfoVararg getInfo() {
      return info;
    }

    public void setInfo(ArgInfoVararg info) {
      this.info = info;
    }

    @Override
    public String toString() {
      return "ArgInfo [info=" + info + "]";
    }

  }

  private String levelPad() {
    String pad = "";
    for (int lev = 0; lev < incLevelVisual; lev++) {
      pad += "  ";
    }
    return pad;
  }

  public Scan(List<Token> _tokens) {
    tokens = _tokens;
    size = _tokens.size();
    offset = 0;
    inlast = false;

    rescan = new LinkedList<Token>();
  }

  public Token pop_noppdirective() {
    if (!rescan.isEmpty()) {
      return rescan.removeFirst();
    }
    Token t = tokens.get(offset);
    if (offset == size - 1) {
      inlast = true;
      return tokens.get(size - 1);
    }
    ++offset;
    return t;
  }

  public Token pop() {
    Token t = pop_noppdirective();
    if (t.ofType(T.T_SHARP) && t.isAtBol()) {
      if (t.isNewLine()) {
        t.setType(T.PT_HEOL);
        return t;
      }
      Token pp = pop_noppdirective();
      T pptype = PpEnv.PP_DIRECTIVES.get(pp.getValue());
      if (pptype == null) {
        throw new ScanExc(pp.loc() + " error: unknown pp-directive: " + pp.getValue());
      }
      pp.setType(pptype);
      return pp;
    }
    return t;
  }

  public boolean isEmpty() {
    return rescan.isEmpty() && (tokens.isEmpty() || inlast);
  }

  public void push(Token t) {
    rescan.addFirst(t);
  }

  private void checkLF(Token t) {
    if (t.isNewLine()) {
      throw new ScanExc(t.loc() + " error: incorrect pp-directive");
    }
  }

  public boolean dline(Token pp) throws IOException {

    if (pp.ofType(T.PT_HDEFINE)) {
      checkLF(pp);
      return new PP_define(this).scan(pp);

    } else if (pp.ofType(T.PT_HUNDEF)) {
      checkLF(pp);
      return new PP_undef(this).scan(pp);

    } else if (pp.ofType(T.PT_HINCLUDE)) {
      checkLF(pp);
      return new PP_include(this).scan(pp);

    } else if (isIfs(pp)) {
      checkLF(pp);
      return new PP_if2(this).scan(pp);

    } else if (pp.ofType(T.PT_HERROR)) {
      checkLF(pp);
      return new PP_error(this).scan(pp);

    } else if (pp.ofType(T.PT_HPRAGMA)) {
      checkLF(pp);
      return new PP_pragma(this).scan(pp);

    } else if (pp.ofType(T.PT_HINCLUDE_NEXT)) {
      checkLF(pp);
      return new PP_include_next(this).scan(pp);

    } else if (pp.ofType(T.PT_HWARNING)) {
      checkLF(pp);
      return new PP_warning(this).scan(pp);

    } else if (pp.ofType(T.PT_HEOL)) {
      return true;

    } else {
      throw new ScanExc(pp.loc() + " : error : unknown pp-directive : " + pp.getValue());
    }

  }

  private boolean goThroughStreamMarkers(Token t) {

    // stack to avoid too nested include recursion...
    if (t.ofType(T.TOKEN_STREAMBEGIN)) {
      String beginStreamName = t.getFilename();

      // TODO: env-limits...
      if (incstack.size() > 70 + 1) {
        throw new ScanExc(t.getFilename() + ": error: #include nested too deeply");
      }

      incstack.push(beginStreamName);
      if (PpEnv.DUMP_STREAM_INFO) {
        System.out.println(levelPad() + "STREAM_BEGIN : " + beginStreamName);
      }
      incLevelVisual++;
      return true;
    }

    if (t.ofType(T.TOKEN_STREAMEND)) {
      incLevelVisual--;
      String endStreamName = incstack.pop();
      if (PpEnv.DUMP_STREAM_INFO) {
        System.out.println(levelPad() + "STREAM_END   : " + endStreamName);
      }
      return true;
    }

    return false;
  }

  public boolean makeBuiltinToken(Token t) {

    if (!t.ofType(T.TOKEN_IDENT)) {
      return false;
    }

    if (t.getValue().equals("__FILE__")) {
      t.setValue("\"" + t.getFilename() + "\"");
      t.setType(T.TOKEN_STRING);
      return true;
    }

    if (t.getValue().equals("__LINE__")) {
      t.setValue(String.valueOf(t.getRow()));
      t.setType(T.TOKEN_NUMBER);
      return true;
    }

    return false;
  }

  private Token makeDefined(Token t) {
    /*
    6.10.1.4
    Prior to evaluation, macro invocations in the list of preprocessing tokens that will become
    the controlling constant expression are replaced (except for those macro names modified
    by the `defined` unary operator), just as in normal text. If the token `defined` is
    generated as a result of this replacement process or use of the `defined` unary operator
    does not match one of the two specified forms prior to macro replacement, 
    THE BEHAVIOR IS UNDEFINED.
    */

    /*
    The standard rules say: if we see `defined` keyword in replacement list,
    this is undefined behavior.
    But, many compilers solve this situation, and expand `defined` operator.
    
    Also, you can find code like this:
    
    1) '__NORMAL__'  form of `defined`
    #define __INTRINSIC_PROLOG(name) (!defined(__INTRINSIC_DEFINED_ ## name)) ...
    #if __INTRINSIC_PROLOG(_lrotl)
      ...
    #endif
    
    2) '__NOT_NORMAL__' form of `defined`
    #define DEFINED defined
    #define DDD (DEFINED ( AAA ) && AAA > 1 && !DEFINED BBB)
    #if (DDD)
      ...
    #endif
    
    First and second form's is not portability, and THE BEHAVIOR IS UNDEFINED whatever.
    We do the same things like others compilers, but generate warnings...
    
    */

    Token nx = pop();
    for (; nx.ofType(T.T_SPEC_UNHIDE); nx = pop()) {
      nx.getIdent().getSym().unhide();
    }
    if (nx.ofType(T.T_LEFT_PAREN)) {
      Token id = pop();
      id.checkId();
      Sym mac = id.getIdent().getSym();
      Token ntok = new Token(id);
      ntok.setType(T.TOKEN_NUMBER);
      ntok.setValue(mac == null ? "0" : "1");
      Token rparen = pop();
      if (!rparen.ofType(T.T_RIGHT_PAREN)) {
        throw new ScanExc(t.loc() + " error: missing rparen in defined operator");
      }
      return ntok;
    } else {
      nx.checkId();
      Sym mac = nx.getIdent().getSym();
      Token ntok = new Token(nx);
      ntok.setType(T.TOKEN_NUMBER);
      ntok.setValue(mac == null ? "0" : "1");
      return ntok;
    }

  }

  public Token get() throws IOException {

    restart: while (!isEmpty()) {

      Token t = pop();

      if (goThroughStreamMarkers(t)) {
        continue;
      }

      if (makeBuiltinToken(t)) {
        return t;
      }

      // 0
      //
      if (isPPDirType(t)) {
        if (!dline(t)) {
          throw new ScanExc("Error extract pp-directive...");
        }
        continue;
      }

      // 1
      //
      if (t.ofType(T.T_SPEC_UNHIDE)) {
        t.getIdent().getSym().unhide();
        continue restart;
      }

      // 2
      //
      if (!t.ofType(T.TOKEN_IDENT)) {
        return t;
      }
      if (t.isPainted()) {
        return t;
      }

      //
      if (t.getValue().equals("defined") && t.isDefinedInreplList()) {
        return makeDefined(t);
      }

      // 3
      //
      Sym macros = t.getIdent().getSym();
      if (macros == null) {
        return t;
      }

      // 4
      //
      if (macros.isHidden()) {
        Token painted = new Token(t);
        painted.paint();
        return painted;
      }

      // 5
      //
      if (macros.isObjectLike()) {
        replaceSimple(macros, macros.getRepl(), t, null);
        continue restart;
      }

      // 6
      // otherwise all cases for function-like macros...
      //
      while (!isEmpty()) {
        Token u = pop();

        if (u.ofType(T.T_SPEC_UNHIDE)) {
          u.getIdent().getSym().unhide();
          continue;
        }

        if (!u.ofType(T.T_LEFT_PAREN)) {
          push(u);
          return t; // XXX
        }

        // nullary
        if (macros.getArity() == 0) {
          while (!isEmpty()) {
            u = pop();
            if (u.ofType(T.T_SPEC_UNHIDE)) {
              u.getIdent().getSym().unhide();
              continue;
            } else if (u.ofType(T.T_RIGHT_PAREN)) {
              replaceSimple(macros, macros.getRepl(), t, null);
              continue restart;
            } else {
              throw new RuntimeException("a nullary macro may not be invoked with arguments");
            }
          }
        }

        // non - nullary

        ArgInfo argInfo = new ArgInfo(); //MARK:{,##__VA_ARGS__}
        List<List<Token>> arguments = scanArgs(macros.getParm().size(), macros.isVararg(), t, argInfo);
        ArgVariants argvariants = new ArgVariants(arguments.size());

        for (int argidx = 0; argidx != arguments.size(); ++argidx) {

          if (macros.usage(argidx) == 0) { // argument not used
            for (Token nused : arguments.get(argidx)) {
              if (nused.ofType(T.T_SPEC_UNHIDE)) {
                nused.getIdent().getSym().unhide();
              }
            }
            continue;
          }

          // argument used

          List<Sym> reset = new ArrayList<Sym>();

          // I
          //
          if ((macros.usage(argidx) & Fcategory.stringized) == Fcategory.stringized) {
            Token stringizedTok = stringize(arguments.get(argidx), reset, t);
            List<Token> argvar = argvariants.get(argidx, Fcategory.formal | Fcategory.stringized);
            argvar.add(stringizedTok);
          }

          // II
          //
          if ((macros.usage(argidx) & Fcategory.unscanned) == Fcategory.unscanned) {
            for (Sym mdef : reset) {
              mdef.hide();
            }
            reset.clear();

            List<Token> argvar = argvariants.get(argidx, Fcategory.formal | Fcategory.unscanned);
            for (Token glued : arguments.get(argidx)) {
              if (glued.ofType(T.T_SPEC_UNHIDE)) {
                Sym mac = glued.getIdent().getSym();
                mac.unhide();
                reset.add(mac);
                continue;
              }

              if (!glued.ofType(T.TOKEN_IDENT)) {
                argvar.add(glued);
              } else {
                Sym mac = glued.getIdent().getSym();
                if (mac == null) {
                  argvar.add(glued);
                } else {
                  if (mac.isHidden()) {
                    Token painted = new Token(glued);
                    argvar.add(painted);
                    painted.paint();
                  } else {
                    argvar.add(glued);
                  }
                }
              }
            }

          }

          // III
          //
          if ((macros.usage(argidx) & Fcategory.scanned) == Fcategory.scanned) {
            for (Sym mdef : reset) {
              mdef.hide();
            }

            List<Token> argvar = argvariants.get(argidx, Fcategory.formal | Fcategory.scanned);
            Scan nscan = new Scan(arguments.get(argidx));

            for (;;) {
              Token ntok = nscan.get();
              if (ntok == Env.EOF_TOKEN_ENTRY) {
                break;
              }
              argvar.add(ntok);
            }
          }

        }

        List<Token> subst = new LinkedList<Token>();
        for (Token stok : macros.getRepl()) {
          int c = stok.getCategory();
          final boolean isStringized = c == (Fcategory.formal | Fcategory.stringized);
          final boolean isUnscanned = c == (Fcategory.formal | Fcategory.unscanned);
          final boolean isScanned = c == (Fcategory.formal | Fcategory.scanned);
          final boolean isOneOfCategory = isStringized || isUnscanned || isScanned;

          if (!isOneOfCategory) {
            subst.add(stok);
          }

          else {
            int n = stok.getArgnum();
            if (n < 0 || n > arguments.size()) {
              throw new RuntimeException("Unknown argnum: " + n);
            }

            List<Token> argsToSubst = argvariants.get(n, c);
            boolean first = true;

            for (Token realarg : argsToSubst) {
              if (first) {
                realarg.setLeadingWhitespace(stok.hasLeadingWhitespace());
                first = false;
              }
              subst.add(realarg);
            }
          }

        }

        replaceSimple(macros, subst, t, argInfo);
        continue restart;

      }

      // 7
      //
      return t;
    }

    return Env.EOF_TOKEN_ENTRY;
  }

  private boolean isNoOp(T t) {
    return t == T.TOKEN_IDENT || t == T.TOKEN_NUMBER || t == T.TOKEN_STRING || t == T.TOKEN_CHAR;
  }

  private List<List<Token>> scanArgs(int arity, boolean isVararg, Token headToCopy, ArgInfo arginfo)
      throws IOException {

    List<List<Token>> args = new ArrayList<List<Token>>();
    for (int i = 0; i != arity; i++) {
      args.add(new ArrayList<Token>());
    }

    int arg = 0;
    int nesting = 0;

    while (!isEmpty()) {
      Token u = pop();

      if (isPPDirType(u)) {
        Error.warning(ErrorCode.W_PP_DIRECTIVE_IN_ARGUMENT_LIST, u.loc());
        if (!dline(u)) {
          throw new ScanExc(u.loc() + "error extract pp-directive...");
        }
        continue;
      }

      List<Token> oneparm = args.get(arg);
      if (isNoOp(u.getType())) {
        oneparm.add(u);
      } else if (u.ofType(T.T_COMMA)) {
        if (nesting == 0) {
          if (arg + 1 == args.size()) {
            if (!isVararg) {
              throw new ScanExc(headToCopy.loc()
                  + " error : a function-like macro must be invoked with the correct number of arguments");
            }
            oneparm.add(u);
          } else {
            ++arg;
          }
        } else {
          oneparm.add(u);
        }
      } else if (u.ofType(T.T_LEFT_PAREN)) {
        ++nesting;
        oneparm.add(u);
      } else if (!u.ofType(T.T_RIGHT_PAREN)) {
        oneparm.add(u);
      } else if (nesting != 0) {
        --nesting;
        oneparm.add(u);
      } else {
        if (arg + 1 != args.size() && !isVararg) {
          throw new ScanExc(
              headToCopy.loc() + " error : a function-like macro must be invoked with the correct number of arguments");
        }
        break;
      }
    }

    //MARK:{,##__VA_ARGS__}
    if (isVararg) {

      int nformal = arity - 1;

      // #define x(a, ...) __VA_ARGS__
      // x(0)   => vararg NOT present
      // x(0,1) => vararg present
      // x(0,)  => vararg present empty

      if (nformal > 0) {
        // #define x(a, ...)

        int zet = arg - nformal;

        if (zet < 0) {
          arginfo.setInfo(ArgInfoVararg.vararg_not_present);
        }
        if (zet >= 0) {
          // the `variadic` argument always last argument...
          //
          List<Token> lastarg = args.get(arity - 1);
          if (lastarg.isEmpty()) {
            arginfo.setInfo(ArgInfoVararg.vararg_present_empty);
          } else {
            arginfo.setInfo(ArgInfoVararg.vararg_present_normal);
          }
        }

      }

      else {
        // #define x(...)
        // `arg` ALWAYS == 0

        List<Token> lastarg = args.get(0);
        if (lastarg.isEmpty()) {
          arginfo.setInfo(ArgInfoVararg.vararg_single_arg_and_present_empty);
        } else {
          arginfo.setInfo(ArgInfoVararg.vararg_single_arg_and_present_normal);
        }
      }

    }

    for (List<Token> aparm : args) {
      if (aparm.isEmpty()) {
        Token placem = new Token(headToCopy);
        placem.setType(T.T_SPEC_PLACEMARKER); // TODO: merge: XXX
        placem.setValue("");
        aparm.add(placem);
      }
    }

    return args;

  }

  private List<Token> pasteAll(Token headToCopy, List<Token> replacement, ArgInfo argInfo) {
    List<Token> rv = new LinkedList<Token>();
    for (int iter = 0; iter < replacement.size(); iter++) {

      Token tok = replacement.get(iter);

      if (tok.getCategory() == Fcategory.commaopt) {

        boolean deletecomma = (argInfo.getInfo() == ArgInfoVararg.vararg_not_present)
            || (argInfo.getInfo() == ArgInfoVararg.vararg_single_arg_and_present_empty);
        if (deletecomma) {
          rv.remove(rv.size() - 1); // we know the comma there.
          iter++; // over `##`
          continue;
        }
      }

      else if (tok.getCategory() == Fcategory.hashhash) {

        if (!rv.isEmpty()) {
          Token LHS = rv.remove(rv.size() - 1);
          Token RHS = replacement.get(++iter); // we know that: after '##' must be something. we check it when parse define...

          boolean needlx = true;
          if (LHS.ofType(T.T_SPEC_PLACEMARKER)) {
            needlx = false;
            rv.add(RHS);
          }
          if (RHS.ofType(T.T_SPEC_PLACEMARKER)) {
            needlx = false;
            rv.add(LHS);
          }

          boolean needwsp = LHS.hasLeadingWhitespace();

          if (needlx) {
            String ntokv = LHS.getValue() + RHS.getValue();
            List<Token> ntoks = new Stream("<glue>", ntokv).getTokenlist();
            List<Token> cleanListToGlue = new ArrayList<Token>();
            for (Token lxd : ntoks) {
              if (lxd.typeIsSpecialStreamMarks()) {
                continue;
              }
              cleanListToGlue.add(lxd);
            }
            if (cleanListToGlue.size() != 1) {
              throw new RuntimeException("Glued error with multiple tokens... [" + ntokv + "]\n");
            }

            Token restok = cleanListToGlue.get(0);
            // whitespace/newline we handle below...
            restok.setLeadingWhitespace(needwsp);
            restok.setNewLine(false);
            restok.setAtBol(false);
            rv.add(restok);
          }
        }

      } else {
        Token ntok = new Token(tok);
        ntok.setLocation(new SourceLocation(headToCopy.getLocation()));
        rv.add(ntok);
      }

    }

    if (!rv.isEmpty()) {
      Token first = rv.get(0);
      first.setLeadingWhitespace(headToCopy.hasLeadingWhitespace());
    }

    return rv;
  }

  public Token stringize(List<Token> ts, List<Sym> reset, Token headToCopy) {

    Token stringized = new Token(headToCopy);
    stringized.setType(T.TOKEN_STRING);

    if (ts.isEmpty()) {
      stringized.setValue("\"" + "\"");
      return stringized;
    }

    String tmp = "";
    int bound = ts.size() - 1;

    for (int j = 0; j <= bound; j++) {
      Token t = ts.get(j);

      if (t.ofType(T.TOKEN_STRING) || t.ofType(T.TOKEN_CHAR)) {
        if (t.hasLeadingWhitespace() && tmp.length() > 0) {
          tmp += " ";
        }
        String escval = t.getValue();
        int slen = escval.length();
        for (int i = 0; i < slen; i++) {
          char c = escval.charAt(i);
          if (c == '\"' || c == '\\') {
            tmp += "\\";
          }
          tmp += c;
        }
        continue;
      }

      if (t.ofType(T.T_SPEC_UNHIDE)) {
        Sym mac = t.getIdent().getSym();
        mac.unhide();
        reset.add(mac);
        continue;
      }

      if (t.hasLeadingWhitespace() && tmp.length() > 0) {
        tmp += " ";
      }
      tmp += t.getValue();
    }

    tmp = "\"" + tmp + "\"";
    stringized.setValue(tmp);

    return stringized;

  }

  private void replaceSimple(Sym macros, List<Token> repl, Token t, ArgInfo argInfo) {
    macros.hide();

    List<Token> replacement = repl;
    List<Token> res = pasteAll(t, replacement, argInfo);
    for (int j = res.size(); --j >= 0;) {
      Token tokp = res.get(j);
      if (!tokp.ofType(T.T_SPEC_PLACEMARKER)) {
        push(tokp);
      }
    }
  }

  private boolean isPPDirType(Token tok) {
    return PpEnv.PP_DIRECTIVES_SET.contains(tok.getType());
  }

  private boolean isIfs(Token tok) {
    return tok.ofType(T.PT_HIF) || tok.ofType(T.PT_HIFDEF) || tok.ofType(T.PT_HIFNDEF);
  }

}
