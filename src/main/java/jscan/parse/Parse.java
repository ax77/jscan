package jscan.parse;

import static jscan.tokenize.T.TOKEN_EOF;
import static jscan.tokenize.T.TOKEN_IDENT;
import static jscan.tokenize.T.T_COLON;
import static jscan.tokenize.T.T_SEMI_COLON;

import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;

public class Parse {

  // main thing's
  private final Tokenlist tokenlist;
  private Token tok;

  // location, error-handling
  private String lastloc;
  private List<Token> ringBuffer;
  private Token prevtok;
  private int flags = 0;

  public Parse(Tokenlist tokenlist) {
    this.tokenlist = tokenlist;
    checkItHasEOF();
    initParser();
  }

  private void checkItHasEOF() {
    final List<Token> tokens = tokenlist.getList();
    // for sure!
    if (!tokens.isEmpty()) {
      Token last = tokens.get(tokens.size() - 1);
      if (!last.ofType(T.TOKEN_EOF)) {
        throw new AstParseException("token-list without EOF");
      }
    }
  }

  private void initParser() {
    initDefaults();
    initScopes();
    move();
  }

  private void initDefaults() {
    this.ringBuffer = new ArrayList<>();
    this.lastloc = "";
  }

  private void initScopes() {
  }

  public void move() {
    tok = tokenlist.next();
    while (tok.ofType(T.TOKEN_COMMENT)) {
      tok = tokenlist.next();
    }
    addLoc();
  }

  public Token tok() {
    return tok;
  }

  public boolean hasBit(int m) {
    return (flags & m) != 0;
  }

  public void setBit(int f) {
    flags |= f;
  }

  public void clearBit(int f) {
    flags &= ~f;
  }

  public int getFlags() {
    return flags;
  }

  //////////////////////////////////////////////////////////////////////
  // PRE-SYMTAB

  public boolean isUserDefinedIdentNoKeyword(Token what) {
    return what.ofType(T.TOKEN_IDENT) && !what.isBuiltinIdent();
  }

  //////////////////////////////////////////////////////////////////////
  // ROUTINE

  public String getLastLoc() {
    return lastloc;
  }

  public Token getPrevtok() {
    return prevtok;
  }

  public void setPrevtok(Token prevtok) {
    this.prevtok = prevtok;
  }

  public List<Token> getRingBuffer() {
    return ringBuffer;
  }

  public Tokenlist getTokenlist() {
    return tokenlist;
  }

  private void addLoc() {

    if (ringBuffer.size() >= 230) {
      ringBuffer.remove(0);
    }
    ringBuffer.add(tok);

    lastloc = (prevtok == null ? tok.getLocationToString() : prevtok.getLocationToString());
    prevtok = tok;
  }

  //////////////////////////////////////////////////////////////////////
  // STATE

  public void perror(String m) {

    StringBuilder sb = new StringBuilder();
    sb.append("error: " + m + "\n");
    sb.append("  --> " + lastloc + "\n\n");
    sb.append(RingBuf.ringBufferToStringLines(ringBuffer) + "\n");

    throw new AstParseException(sb.toString());
  }

  public void pwarning(String m) {

    StringBuilder sb = new StringBuilder();
    sb.append("warning: " + m + "\n");
    sb.append("  --> " + lastloc + "\n\n");
    sb.append(RingBuf.ringBufferToStringLines(ringBuffer) + "\n");

  }

  public boolean is(T toktype) {
    return tok.getType().equals(toktype);
  }

  public boolean is(Ident ident) {
    return tok.ofType(T.TOKEN_IDENT) && ident.equals(tok.getIdent());
  }

  public T tp() {
    return tok.getType();
  }

  public Token moveget() {
    Token tok = tok();
    move();
    return tok;
  }

  public Token checkedMove(Ident expect) {
    if (!tok.isIdent(expect)) {
      perror("expect id: " + expect.getName() + ", but was: " + tok.getValue());
    }
    Token saved = tok();
    move();
    return saved;
  }

  public Ident getIdent() {
    if (!tok.ofType(TOKEN_IDENT)) {
      perror("expect ident, but was: " + tok.getValue());
    }
    Token saved = tok;
    move();
    final Ident ident = saved.getIdent();
    if (ident.isBuiltin()) {
      perror("unexpected builtin ident: " + ident.getName());
    }
    return ident;
  }

  public Token checkedMove(T expect) {
    if (tp() != expect) {
      perror("expect: " + expect.toString() + ", but was: " + tok.getValue());
    }
    Token saved = tok;
    move();
    return saved;
  }

  public boolean moveOptional(T t) {
    if ((tp() == t)) {
      move();
      return true;
    }
    return false;
  }

  public void unexpectedEof() {
    if (tok.ofType(TOKEN_EOF)) {
      perror("EOF unexpected at this context");
    }
  }

  public void unimplemented(String what) {
    perror("unimplemented: " + what);
  }

  public void unreachable(String what) {
    perror("unreachable: " + what);
  }

  public Token peek() {
    return tokenlist.peek();
  }

  public ArrayList<Token> peekCnt(int howMuch) {
    return tokenlist.peekCnt(howMuch);
  }

  public Token lparen() {
    return checkedMove(T.T_LEFT_PAREN);
  }

  public Token rparen() {
    return checkedMove(T.T_RIGHT_PAREN);
  }

  public Token lbracket() {
    return checkedMove(T.T_LEFT_BRACKET);
  }

  public Token lbrace() {
    return checkedMove(T.T_LEFT_BRACE);
  }

  public Token rbrace() {
    return checkedMove(T.T_RIGHT_BRACE);
  }

  public Token rbracket() {
    return checkedMove(T.T_RIGHT_BRACKET);
  }

  public Token semicolon() {
    return checkedMove(T_SEMI_COLON);
  }

  public Token colon() {
    return checkedMove(T_COLON);
  }

  public Token lt() {
    return checkedMove(T.T_LT);
  }

  public Token gt() {
    return checkedMove(T.T_GT);
  }

  //////////////////////////////////////////////////////////////////////

  public boolean isEof() {
    return tok.ofType(T.TOKEN_EOF);
  }

  public void restoreState(ParseState state) {
    this.tokenlist.setOffset(state.getTokenlistOffset());
    this.tok = state.getTok();
    this.ringBuffer = new ArrayList<Token>(state.getRingBuffer());
    this.lastloc = state.getLastloc();
    this.prevtok = state.getPrevtok();
    this.flags = state.getFlags();
  }

  //////////////////////////////////////////////////////////////////////
  // ENTRY

  public void errorCommaExpression() {
    StringBuilder sb = new StringBuilder();
    sb.append("Comma-expression list is deprecated, cause it makes such a mess sometimes -> [a=1, b=a, c=b].");
    sb.append("You may use comma-list only in for-loop -> for(int i=0, j=0; i<10; i+=1, j+=2)");

    if (is(T.T_COMMA)) {
      perror(sb.toString());
    }
  }

  public void errorArray() {
    perror("raw-arrays are unimplemented, you may use array<T> instead");
  }

  public void errorStraySemicolon() {
    if (is(T.T_SEMI_COLON)) {
      perror("stray semicolons [;] are deprecated by design.");
    }
  }

}
