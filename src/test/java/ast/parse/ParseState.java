package ast.parse;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.Token;
import ast.unit.FunctionDefinition;

public class ParseState {
  private final int tokenlistOffset;
  private final Token tok;
  private final FunctionDefinition currentFn;
  private final List<Token> ringBuffer;
  private final String lastloc;
  private final Token prevtok;

  public ParseState(Parse parser) {
    this.tokenlistOffset = parser.getTokenlist().getOffset();
    this.tok = parser.tok();
    this.currentFn = parser.getCurrentFn();
    this.ringBuffer = new ArrayList<Token>(parser.getRingBuffer());
    this.lastloc = parser.getLastLoc();
    this.prevtok = parser.getPrevtok();
  }

  public int getTokenlistOffset() {
    return tokenlistOffset;
  }

  public Token getTok() {
    return tok;
  }

  public FunctionDefinition getCurrentFn() {
    return currentFn;
  }

  public List<Token> getRingBuffer() {
    return ringBuffer;
  }

  public String getLastloc() {
    return lastloc;
  }

  public Token getPrevtok() {
    return prevtok;
  }

}
