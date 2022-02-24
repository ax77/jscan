package tokenize;

import static main.Env.EOF_TOKEN_ENTRY;
import static main.Env.HC_FEOF;
import static main.Env.isDec;
import static main.Env.isLetter;
import static main.Env.isOpStart;
import static tokenize.T.TOKEN_EOF;
import static tokenize.T.TOKEN_ERROR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import buffers.Cbuf;
import hashed.Hash_ident;
import main.Env;
import preproc.preprocess.ScanExc;
import sourceloc.SourceLocation;
import utils.AstParseException;
import utils.Escaper;

public class Stream {

  private final char Q1 = '\'';
  private final char Q2 = '\"';

  private final Token EOL_TOKEN;
  private final Token WSP_TOKEN;

  private final String filename;
  private final List<Token> tokenlist;
  private final Cbuf buffer;

  public static final Map<String, T> VALID_COMBINATIONS_2 = new HashMap<String, T>();
  private static final Map<String, T> VALID_COMBINATIONS_3 = new HashMap<String, T>();
  private static final Map<String, T> VALID_COMBINATIONS_4 = new HashMap<String, T>();
  public static final Map<String, T> SINGLE_OPERATORS = new HashMap<String, T>();
  public static final Map<String, T> OTHER_ASCII_CHARACTERS = new HashMap<String, T>();

  static {

    // " ... && -= >= ~ + ; ] <: "
    // " <<= &= -> >> % , < ^ :> "
    // " >>= *= /= ^= & - = { <% "
    // " != ++ << |= ( . > | %> "
    // " %= += <= || ) / ? } %: "
    // " ## -- == ! * : [ # %:%: "

    // 4
    VALID_COMBINATIONS_4.put("%:%:", T.T_SHARP_SHARP); // c-digraph

    // 3
    VALID_COMBINATIONS_3.put(">>=", T.T_RSHIFT_EQUAL);
    VALID_COMBINATIONS_3.put("<<=", T.T_LSHIFT_EQUAL);
    VALID_COMBINATIONS_3.put("...", T.T_DOT_DOT_DOT);

    // 2
    VALID_COMBINATIONS_2.put("->", T.T_ARROW);
    VALID_COMBINATIONS_2.put("--", T.T_MINUS_MINUS);
    VALID_COMBINATIONS_2.put("-=", T.T_MINUS_EQUAL);
    VALID_COMBINATIONS_2.put("!=", T.T_NE);
    VALID_COMBINATIONS_2.put("..", T.T_DOT_DOT);
    VALID_COMBINATIONS_2.put("*=", T.T_TIMES_EQUAL);
    VALID_COMBINATIONS_2.put("/=", T.T_DIVIDE_EQUAL);
    VALID_COMBINATIONS_2.put("&=", T.T_AND_EQUAL);
    VALID_COMBINATIONS_2.put("&&", T.T_AND_AND);
    VALID_COMBINATIONS_2.put("##", T.T_SHARP_SHARP);
    VALID_COMBINATIONS_2.put("%=", T.T_PERCENT_EQUAL);
    VALID_COMBINATIONS_2.put("^=", T.T_XOR_EQUAL);
    VALID_COMBINATIONS_2.put("++", T.T_PLUS_PLUS);
    VALID_COMBINATIONS_2.put("+=", T.T_PLUS_EQUAL);
    VALID_COMBINATIONS_2.put("<=", T.T_LE);
    VALID_COMBINATIONS_2.put("<<", T.T_LSHIFT);
    VALID_COMBINATIONS_2.put("==", T.T_EQ);
    VALID_COMBINATIONS_2.put(">=", T.T_GE);
    VALID_COMBINATIONS_2.put(">>", T.T_RSHIFT);
    VALID_COMBINATIONS_2.put("||", T.T_OR_OR);
    VALID_COMBINATIONS_2.put("|=", T.T_OR_EQUAL);
    VALID_COMBINATIONS_2.put("<:", T.T_LEFT_BRACKET); // c-digraph
    VALID_COMBINATIONS_2.put(":>", T.T_RIGHT_BRACKET); // c-digraph
    VALID_COMBINATIONS_2.put("<%", T.T_LEFT_BRACE); // c-digraph
    VALID_COMBINATIONS_2.put("%>", T.T_RIGHT_BRACE); // c-digraph
    VALID_COMBINATIONS_2.put("%:", T.T_SHARP); // c-digraph

    // 1
    SINGLE_OPERATORS.put(",", T.T_COMMA);
    SINGLE_OPERATORS.put("-", T.T_MINUS);
    SINGLE_OPERATORS.put(";", T.T_SEMI_COLON);
    SINGLE_OPERATORS.put(":", T.T_COLON);
    SINGLE_OPERATORS.put("!", T.T_EXCLAMATION);
    SINGLE_OPERATORS.put("?", T.T_QUESTION);
    SINGLE_OPERATORS.put(".", T.T_DOT);
    SINGLE_OPERATORS.put("(", T.T_LEFT_PAREN);
    SINGLE_OPERATORS.put(")", T.T_RIGHT_PAREN);
    SINGLE_OPERATORS.put("[", T.T_LEFT_BRACKET);
    SINGLE_OPERATORS.put("]", T.T_RIGHT_BRACKET);
    SINGLE_OPERATORS.put("{", T.T_LEFT_BRACE);
    SINGLE_OPERATORS.put("}", T.T_RIGHT_BRACE);
    SINGLE_OPERATORS.put("*", T.T_TIMES);
    SINGLE_OPERATORS.put("/", T.T_DIVIDE);
    SINGLE_OPERATORS.put("&", T.T_AND);
    SINGLE_OPERATORS.put("#", T.T_SHARP);
    SINGLE_OPERATORS.put("%", T.T_PERCENT);
    SINGLE_OPERATORS.put("^", T.T_XOR);
    SINGLE_OPERATORS.put("+", T.T_PLUS);
    SINGLE_OPERATORS.put("<", T.T_LT);
    SINGLE_OPERATORS.put("=", T.T_ASSIGN);
    SINGLE_OPERATORS.put(">", T.T_GT);
    SINGLE_OPERATORS.put("|", T.T_OR);
    SINGLE_OPERATORS.put("~", T.T_TILDE);

    // 
    OTHER_ASCII_CHARACTERS.put("$", T.T_DOLLAR_SIGN);
    OTHER_ASCII_CHARACTERS.put("@", T.T_AT_SIGN);
    OTHER_ASCII_CHARACTERS.put("`", T.T_GRAVE_ACCENT);
    OTHER_ASCII_CHARACTERS.put("\\", T.T_BACKSLASH);
  }

  public Stream(String fname, String txt) {
    this.filename = fname;
    this.tokenlist = new ArrayList<Token>();
    this.buffer = new Cbuf(txt);

    this.EOL_TOKEN = new Token();
    this.EOL_TOKEN.setNewLine(true);
    this.EOL_TOKEN.setValue("\\n");

    this.WSP_TOKEN = new Token();
    this.WSP_TOKEN.setLeadingWhitespace(true);
    this.WSP_TOKEN.setValue(" ");

    tokenize();
  }

  private Token specialToken(T _type, String _value) {
    Token token = new Token();
    token.set(_type, _value);

    setPos(token);
    return token;
  }

  private Token identToken(Ident _ident) {
    Token token = new Token();
    token.setIdent(_ident);

    setPos(token);
    return token;
  }

  private void setPos(Token token) {
    final int column = buffer.getColumn() - token.getValue().length();
    token.setLocation(new SourceLocation(filename, buffer.getLine(), column));
  }

  public boolean isAsmFileExt() {
    final String tmp = filename.toLowerCase().trim();
    return tmp.endsWith(".s") || tmp.endsWith(".asm");
  }

  public boolean isC89FileExt() {
    final String tmp = filename.toLowerCase().trim();
    return tmp.endsWith(".c") || tmp.endsWith(".h");
  }

  private Token nex2() {

    final char[] threechars = buffer.peekc4();
    final char c1 = threechars[0];
    final char c2 = threechars[1];
    final char c3 = threechars[2];
    final char c4 = threechars[3];

    if (c1 == HC_FEOF) {
      return EOF_TOKEN_ENTRY;
    }

    final boolean isWhiteSpace = c1 == ' ' || c1 == '\t' || c1 == '\f';
    if (isWhiteSpace) {
      move();
      return WSP_TOKEN;
    }

    if (c1 == '\n') {
      move();
      return EOL_TOKEN;
    }

    // allow asm-comments sometimes
    if (c1 == '#' || c1 == ';') {
      if (isAsmFileExt()) {

        final StringBuilder comments = new StringBuilder();
        comments.append(c1);
        move();

        while (!buffer.isEof()) {
          final char tmpch = buffer.nextc();
          if (tmpch == '\n') {
            return EOL_TOKEN;
          }
          if (tmpch == HC_FEOF) {
            // return EOF_TOKEN_ENTRY;
            throw new AstParseException("no new-line at end of file...");
          }
          comments.append(tmpch);
        }
      }
    }

    // c89/99 style comments
    if (c1 == '/') {

      if (c2 == '/') {
        move();
        move();

        for (;;) {
          int tmpch = buffer.nextc();
          if (tmpch == '\n') {
            return EOL_TOKEN;
          }
          if (tmpch == HC_FEOF) {
            return EOF_TOKEN_ENTRY;
          }
        }
      }

      else if (c2 == '*') {
        move();
        move();

        int prevc = '\0';
        for (;;) {
          int tmpch = buffer.nextc();
          if (tmpch == HC_FEOF) {
            throw new ScanExc(Integer.toString(buffer.getLine()));
          }
          if (tmpch == '/' && prevc == '*') {
            return WSP_TOKEN;
          }
          prevc = tmpch;
        }
      }

    }

    final boolean isStringStart = (c1 == '\'' || c1 == '\"');
    if (isStringStart) {
      return getString(CStrEnc.STR_ENC_NONE);
    }

    final boolean isUcnBegin = (c1 == '\\' && (c2 == 'u' || c2 == 'U'));
    if (isUcnBegin) {
      return getOneIdent();
    }

    // identifier or string
    //
    if (isLetter(c1)) {

      // [u"string"]
      // [U"string"]
      // [L"string"]
      // [u8"string"]
      // or identifier

      boolean isU16 = (c1 == 'u' && (c2 == Q1 || c2 == Q2)); // int c01 = u'\u1990'; char16_t s[] = u"str";
      boolean isU32 = (c1 == 'U' && (c2 == Q1 || c2 == Q2));
      boolean isWide = (c1 == 'L' && (c2 == Q1 || c2 == Q2));
      boolean isU8 = (c1 == 'u' && c2 == '8' && (c3 == Q1 || c3 == Q2));

      if (isU16 || isU32 || isWide || isU8) {
        if (isU16) {
          move();
          return getString(CStrEnc.STR_ENC_CH16_u);
        } else if (isU32) {
          move();
          return getString(CStrEnc.STR_ENC_CH32_U);
        } else if (isWide) {
          move();
          return getString(CStrEnc.STR_ENC_WIDE_L);
        } else if (isU8) {
          move();
          move();
          return getString(CStrEnc.STR_ENC_UTF8_u8);
        }
      }

      return getOneIdent();

    }

    final boolean isPpNum = isDec(c1) || (c1 == '.' && isDec(c2));
    if (isPpNum) {
      return getPpNum();
    }

    if (isOpStart(c1)) {
      return getOperator(c1, c2, c3, c4);
    }

    // specials, reserved
    final String others = combineOp(c1);
    if (OTHER_ASCII_CHARACTERS.containsKey(others)) {
      move();
      return specialToken(OTHER_ASCII_CHARACTERS.get(others), others);
    }

    // unknown
    move(); // XXX
    return specialToken(TOKEN_ERROR, String.format("%c", c1));
  }

  private Token getOneIdent() {

    char c = move();
    char nextchar = buffer.peekc();

    boolean isUcnBegin = (c == '\\' && (nextchar == 'u' || nextchar == 'U'));
    StringBuilder sb = new StringBuilder();

    // TODO: is ucn begin correct unicode value.
    //
    if (isUcnBegin) {
      appendOneUcnCodePoint(sb);
    } else {
      sb.append((char) c);
    }

    // tail

    for (;;) {
      char tmpbuf[] = buffer.peekc2();
      int peek1 = tmpbuf[0];
      int peek2 = tmpbuf[1];

      boolean isUcnIdTail = (peek1 == '\\' && (peek2 == 'u' || peek2 == 'U'));
      boolean isIdentifierTail = isLetter(peek1) || isDec(peek1) || isUcnIdTail;

      if (!isIdentifierTail) {
        break;
      }

      if (isUcnIdTail) {
        buffer.nextc(); // move [\]
        appendOneUcnCodePoint(sb);
      } else {
        sb.append((char) buffer.nextc());
      }

    }

    return identToken(Hash_ident.getHashedIdent(sb.toString()));

  }

  public void appendOneUcnCodePoint(StringBuilder strbuf) {
    int nextchar = buffer.nextc(); // move [uU]

    if (nextchar == 'u' || nextchar == 'U') {
    } else {
      throw new ScanExc("expect uU UCN.");
    }

    int ucnlen = (nextchar == 'u' ? 4 : 8);

    StringBuilder tmpbuffer = new StringBuilder();
    for (int i = 0; i < ucnlen; i++) {
      tmpbuffer.append((char) buffer.nextc());
    }
    int hv = (int) Env.hexValue(tmpbuffer.toString());
    if (!isUCN_c11(hv)) {
      throw new ScanExc("TODO: is not UCN");
    }
    String uncidvalue = newStringCp(hv);
    strbuf.append(uncidvalue);
  }

  private String newStringCp(int codePoint) {
    if (Character.charCount(codePoint) == 1) {
      return String.valueOf((char) codePoint);
    } else {
      return new String(Character.toChars(codePoint));
    }
  }

  private Token getString(CStrEnc enc) {
    final char endof = move();

    T typeoftok = (endof == '\'') ? T.TOKEN_CHAR : T.TOKEN_STRING;
    StringBuilder strbuf = new StringBuilder();

    for (;;) {
      int next1 = buffer.nextc();
      if (next1 == Env.HC_FEOF) {
        throw new ScanExc(Integer.toString(buffer.getLine()));
      }
      if (next1 == '\n') {
        throw new ScanExc(Integer.toString(buffer.getLine()));
      }
      if (next1 == endof) {
        break;
      }
      if (next1 != '\\') {
        strbuf.append((char) next1);
        continue;
      }
      int next2 = buffer.nextc();
      strbuf.append("\\");
      strbuf.append((char) next2);
    }

    // TODO:
    int escaped[] = Escaper.escape(strbuf.toString());
    Token token = new Token();

    if (endof == '\"') {
      CStr strconstant = new CStr(escaped, enc);
      token.setStrconstant(strconstant);
    } else {
      if (escaped.length == 0) {
        throw new ScanExc("" + " error : empty char constant");
      }
      if (escaped.length > 2) {
        //throw new ScanExc(startLocation + " error : too long char constant"); // TODO: WC
      }
      CChar charconstant = new CChar(escaped[0], enc);
      token.setCharconstant(charconstant);
    }

    setPos(token);
    token.setType(typeoftok);
    token.setValue((char) endof + strbuf.toString() + (char) endof);
    return token;

  }

  public Token getPpNum() {
    /*
     * pp-number:
     *   digit
     *   . digit
     *   pp-number digit
     *   pp-number identifier-nondigit
     *   pp-number e sign
     *   pp-number E sign
     *   pp-number .
     */

    StringBuilder strbuf = new StringBuilder();
    strbuf.append(move());

    for (;;) {
      int peekc = buffer.peekc();
      if (isDec(peekc)) {
        strbuf.append((char) buffer.nextc());
        continue;
      } else if (peekc == 'e' || peekc == 'E' || peekc == 'p' || peekc == 'P') {
        strbuf.append((char) buffer.nextc());

        peekc = buffer.peekc();
        if (peekc == '-' || peekc == '+') {
          strbuf.append((char) buffer.nextc());
        }
        continue;
      } else if (peekc == '.' || isLetter(peekc)) {
        strbuf.append((char) buffer.nextc());
        continue;
      }

      break;
    }
    return specialToken(T.TOKEN_NUMBER, strbuf.toString());
  }

  public List<Token> getTokenlist() {
    return tokenlist;
  }

  private void markbegin() {
    Token t = new Token();
    t.setType(T.TOKEN_STREAMBEGIN);
    t.setLocation(new SourceLocation(filename, 0, 0)); // TODO:real pos
    t.setValue("");
    tokenlist.add(t);
  }

  private void markend() {
    Token t = new Token();
    t.setType(T.TOKEN_STREAMEND);
    t.setLocation(new SourceLocation(filename, 0, 0)); // TODO:real pos
    t.setValue("");
    tokenlist.add(t);
  }

  private void tokenize() {
    markbegin();

    LinkedList<Token> line = new LinkedList<Token>();
    boolean nextws = false;

    for (;;) {
      Token t = nex2();

      if (t.ofType(TOKEN_EOF)) {

        tokenlist.addAll(line);
        markend(); // eostream
        tokenlist.add(t); // eof

        break;
      }

      if (nextws) {
        t.setLeadingWhitespace(true);
        nextws = false;
      }

      if (t == EOL_TOKEN) {
        if (line.isEmpty()) {
          continue;
        }
        line.getLast().setNewLine(true);
        line.getFirst().setAtBol(true);
        line.getFirst().setLeadingWhitespace(true);

        tokenlist.addAll(line);
        line.clear();
        continue;
      }

      if (t == WSP_TOKEN) {
        nextws = true;
        continue;
      }

      line.add(t);
    }
  }

  private boolean isUCN_c11(int c) {
    //@formatter:off
    if(c == 0x00A8) { return true; }
    if(c == 0x00AA) { return true; }
    if(c == 0x00AD) { return true; }
    if(c == 0x00AF) { return true; }
    if(c >= 0x00B2 && c <= 0x00B5) { return true; }
    if(c >= 0x00B7 && c <= 0x00BA) { return true; }
    if(c >= 0x00BC && c <= 0x00BE) { return true; }
    if(c >= 0x00C0 && c <= 0x00D6) { return true; }
    if(c >= 0x00D8 && c <= 0x00F6) { return true; }
    if(c >= 0x00F8 && c <= 0x00FF) { return true; }
    if(c >= 0x0100 && c <= 0x167F) { return true; }
    if(c >= 0x1681 && c <= 0x180D) { return true; }
    if(c >= 0x180F && c <= 0x1FFF) { return true; }
    if(c >= 0x200B && c <= 0x200D) { return true; }
    if(c >= 0x202A && c <= 0x202E) { return true; }
    if(c >= 0x203F && c <= 0x2040) { return true; }
    if(c == 0x2054) { return true; }
    if(c >= 0x2060 && c <= 0x206F) { return true; }
    if(c >= 0x2070 && c <= 0x218F) { return true; }
    if(c >= 0x2460 && c <= 0x24FF) { return true; }
    if(c >= 0x2776 && c <= 0x2793) { return true; }
    if(c >= 0x2C00 && c <= 0x2DFF) { return true; }
    if(c >= 0x2E80 && c <= 0x2FFF) { return true; }
    if(c >= 0x3004 && c <= 0x3007) { return true; }
    if(c >= 0x3021 && c <= 0x302F) { return true; }
    if(c >= 0x3031 && c <= 0x303F) { return true; }
    if(c >= 0x3040 && c <= 0xD7FF) { return true; }
    if(c >= 0xF900 && c <= 0xFD3D) { return true; }
    if(c >= 0xFD40 && c <= 0xFDCF) { return true; }
    if(c >= 0xFDF0 && c <= 0xFE44) { return true; }
    if(c >= 0xFE47 && c <= 0xFFFD) { return true; }
    if(c >= 0x10000 && c <= 0x1FFFD) { return true; }
    if(c >= 0x20000 && c <= 0x2FFFD) { return true; }
    if(c >= 0x30000 && c <= 0x3FFFD) { return true; }
    if(c >= 0x40000 && c <= 0x4FFFD) { return true; }
    if(c >= 0x50000 && c <= 0x5FFFD) { return true; }
    if(c >= 0x60000 && c <= 0x6FFFD) { return true; }
    if(c >= 0x70000 && c <= 0x7FFFD) { return true; }
    if(c >= 0x80000 && c <= 0x8FFFD) { return true; }
    if(c >= 0x90000 && c <= 0x9FFFD) { return true; }
    if(c >= 0xA0000 && c <= 0xAFFFD) { return true; }
    if(c >= 0xB0000 && c <= 0xBFFFD) { return true; }
    if(c >= 0xC0000 && c <= 0xCFFFD) { return true; }
    if(c >= 0xD0000 && c <= 0xDFFFD) { return true; }
    if(c >= 0xE0000 && c <= 0xEFFFD) { return true; }
    return false;
    //@formatter:on
  }

  /// this function is not-optimized, of course
  /// but it works as primitive and simple as possible
  /// 
  private Token getOperator(char c1, char c2, char c3, char c4) {

    final String four = combineOp(c1, c2, c3, c4);
    if (VALID_COMBINATIONS_4.containsKey(four)) {
      move();
      move();
      move();
      move();
      return specialToken(VALID_COMBINATIONS_4.get(four), four);
    }

    // from top to bottom

    final String three = combineOp(c1, c2, c3);
    if (VALID_COMBINATIONS_3.containsKey(three)) {
      move();
      move();
      move();
      return specialToken(VALID_COMBINATIONS_3.get(three), three);
    }

    final String two = combineOp(c1, c2);
    if (VALID_COMBINATIONS_2.containsKey(two)) {
      move();
      move();
      return specialToken(VALID_COMBINATIONS_2.get(two), two);
    }

    final String one = combineOp(c1);
    if (SINGLE_OPERATORS.containsKey(one)) {
      move();
      return specialToken(SINGLE_OPERATORS.get(one), one);
    }

    throw new AstParseException("unknown operator: " + three);
  }

  private String combineOp(char... ops) {
    StringBuilder sb = new StringBuilder();
    for (char op : ops) {
      if (op == '\0') {
        continue;
      }
      sb.append(op);
    }
    return sb.toString();
  }

  private char move() {
    return buffer.nextc();
  }

}
