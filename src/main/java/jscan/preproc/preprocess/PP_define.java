package jscan.preproc.preprocess;

import static jscan.preproc.ErrorCode.E_HASH_HASH_AT_BEGIN_REPL;
import static jscan.preproc.ErrorCode.E_HASH_HASH_AT_END_REPL;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jscan.hashed.Hash_ident;
import jscan.preproc.Error;
import jscan.preproc.Sym;
import jscan.tokenize.Fcategory;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import jscan.utils.Tokenlist;

public final class PP_define implements PP_directive {

  private final Scan scanner;

  private final String __VA_ARGS__ = "__VA_ARGS__";

  public PP_define(Scan s) {
    scanner = s;
  }

  private void checkHH(List<Token> repl) {
    int bound = repl.size() - 1;
    int index = 0;
    for (Token t : repl) {
      if (t.is(T.T_SHARP_SHARP)) {
        if (index == 0 || index == bound) {
          Error.error((index == 0 ? E_HASH_HASH_AT_BEGIN_REPL : E_HASH_HASH_AT_END_REPL), t.loc());
        }

        //MARK:{,##__VA_ARGS__}
        if (t.getCategory() != Fcategory.commaopt) {
          t.setCategory(Fcategory.hashhash);
        }
      }
      index++;
    }
  }

  private List<Integer> takeEmptyUsage(int size) {
    List<Integer> usage = new ArrayList<Integer>();

    for (int j = 0; j < size; j++) {
      usage.add(0);
    }

    return usage;
  }

  private List<Integer> usageOfRepl2(List<Token> replacementIN, List<Token> replacementOUT, List<Token> parm) {
    List<Integer> usage = takeEmptyUsage(parm.size());
    int size = replacementIN.size();

    for (int i = 0; i < size; i++) {
      Token curr = replacementIN.get(i);
      Token prev = (i > 0) ? replacementIN.get(i - 1) : null;
      Token next = (i + 1 < size) ? replacementIN.get(i + 1) : null;

      if (curr.is(T.T_SHARP)) {
        if (next == null) {
          throw new RuntimeException("TODO");
        }
        if (!next.is(T.TOKEN_IDENT)) {
          throw new RuntimeException("TODO");
        }
        if (!parm.contains(next)) {
          throw new RuntimeException("TODO");
        }
        next.setCategory(Fcategory.formal | Fcategory.stringized);

      } else if (curr.is(T.T_SHARP_SHARP)) {
        if (i == 0) {
          throw new RuntimeException("TODO"); // at begin of repl-list
        }
        if (next == null) {
          throw new RuntimeException("TODO"); // at end of repl-list
        }

        if (next.is(T.TOKEN_IDENT)) {

          //MARK:{,##__VA_ARGS__}
          if (prev != null && prev.is(T.T_COMMA) && next.getValue().equals(__VA_ARGS__)) {
            curr.setCategory(Fcategory.commaopt);
          }

          else {
            if (parm.contains(next)) {
              next.setCategory(Fcategory.formal | Fcategory.unscanned);
            }
          }
        }

        if (prev != null && prev.is(T.TOKEN_IDENT)) {
          if (parm.contains(prev)) {
            prev.setCategory(Fcategory.formal | Fcategory.unscanned);
          }
        }

      } else if (curr.is(T.TOKEN_IDENT)) {
        if (parm.contains(curr)) {
          int c = curr.getCategory();
          boolean isScanned = true;
          if ((c & Fcategory.stringized) == Fcategory.stringized) {
            isScanned = false;
          }
          if ((c & Fcategory.unscanned) == Fcategory.unscanned) {
            isScanned = false;
          }
          if (isScanned) {
            curr.setCategory(Fcategory.formal | Fcategory.scanned);
          }
        }
      }

    }

    for (Token t : replacementIN) {
      if (!t.is(T.T_SHARP)) {
        replacementOUT.add(t);
      }
    }

    for (Token t : replacementOUT) {
      if ((t.getCategory() & Fcategory.formal) == Fcategory.formal) {
        int argnum = parm.indexOf(t);
        t.setArgnum(argnum);
        if (argnum >= 0) {
          usage.set(argnum, usage.get(argnum) | t.getCategory());
        }
      }
    }

    return usage;
  }

  private Token makeVariadicParameter(Token from) {
    Token varargName = new Token(from); // `from` need only for location
    varargName.setType(T.TOKEN_IDENT);
    varargName.setIdent(Hash_ident.getHashedIdent(__VA_ARGS__));
    varargName.setValue(__VA_ARGS__);
    return varargName;
  }

  @Override
  public boolean scan(Token pp) {
    List<Token> repl = new LinkedList<Token>();
    List<Token> parm = new ArrayList<Token>();

    boolean isVararg = false;
    boolean isFlike = false;

    boolean isNamedVararg = false;
    String namedVarargParmName = "";

    Token macid = scanner.pop();
    macid.checkId();

    // [#define x]
    if (macid.isNewLine()) {
      Sym sym = new Sym(macid, repl);
      macid.getIdent().defineSym(sym);
      return true;
    }

    List<Token> line = new ArrayList<Token>();
    for (;;) {
      Token linetok = scanner.pop();
      if (linetok.isNewLine() || linetok.is(T.TOKEN_EOF)) {
        line.add(linetok);
        break;
      }
      line.add(linetok);
    }

    // #define x 1
    //           ^

    // #define x() 1
    //          ^

    Tokenlist iterator = new Tokenlist(line);
    Token firstAfterMacroName = iterator.peek();
    isFlike = firstAfterMacroName.is(T.T_LEFT_PAREN) && !firstAfterMacroName.hasLeadingWhitespace();

    if (isFlike) {
      Token next = iterator.next(); // (

      for (;;) {
        next = iterator.next();
        Token nextpeek = iterator.peek();

        if (next.is(T.T_RIGHT_PAREN)) {

          // [#define x()]
          // NOTE: this have no replacement, but also may have parameters [#define xxx(...)]
          // remember it.
          if (next.isNewLine()) {

            Sym sym = new Sym(macid, repl, parm, isVararg, takeEmptyUsage(parm.size()));
            macid.getIdent().defineSym(sym);
            return true;

          }

          break;
        }

        if (next.is(T.TOKEN_IDENT)) {

          // GNU variadic macros
          if (nextpeek.is(T.T_DOT_DOT_DOT)) {

            // save name and flag
            //
            isNamedVararg = true;
            isVararg = true;
            namedVarargParmName = next.getValue();

            // check the GNU variadic macro parameter at last of parameter list.
            //
            next = iterator.next(); // eat nextpeek -> [...]
            next = iterator.next(); // must be rparen

            if (!next.is(T.T_RIGHT_PAREN)) {
              throw new ScanExc(
                  next.loc() + "error: named vararg parameter must be last parameter: " + next.getValue());
            }

            // we move name, and `...`
            //
            parm.add(makeVariadicParameter(next));
            break;

          }

          else {
            if (parm.contains(next)) {
              throw new ScanExc(next.loc() + "error: duplicate parameter name: " + next.getValue());
            }
            parm.add(next);
          }
        }

        // ISO variadic macros...
        //
        if (next.is(T.T_DOT_DOT_DOT)) {
          isVararg = true;
          parm.add(makeVariadicParameter(next));
        }

      }
    }

    // take replacement

    Token prevreptok = null;
    for (;;) {
      Token reptok = iterator.next();
      if (reptok.isNewLine() || reptok.is(T.TOKEN_EOF)) {
        if (!reptok.is(T.TOKEN_EOF)) {
          repl.add(reptok);
        }
        break;
      }

      // ignore duplicate... [#define glue(a,b) a ## ## ## b]
      // 
      if (prevreptok != null && prevreptok.is(T.T_SHARP_SHARP) && reptok.is(T.T_SHARP_SHARP)) {
        continue;
      }

      repl.add(reptok);
      prevreptok = reptok;
    }

    // rename GNU variadic argument name in replacement list
    //
    if (isNamedVararg) {
      if (namedVarargParmName.isEmpty()) {
        throw new ScanExc("TODO");
      }

      int s = repl.size();
      for (int i = 0; i < s; i++) {
        Token repltok = repl.get(i);
        if (repltok.getValue().equals(namedVarargParmName)) {
          repltok.setIdent(Hash_ident.getHashedIdent(__VA_ARGS__));
        }
      }
    }

    checkHH(repl);

    if (isFlike) {

      List<Token> replOut = new LinkedList<Token>();
      List<Integer> usage = usageOfRepl2(repl, replOut, parm);

      Sym sym = new Sym(macid, replOut, parm, isVararg, usage);
      macid.getIdent().defineSym(sym);
      return true;

    } else {

      Sym sym = new Sym(macid, repl);

      macid.getIdent().defineSym(sym);
      return true;
    }

  }

}
