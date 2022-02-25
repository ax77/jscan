package ast.attributes;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.Token;

public class Attribute {
  private final List<Token> tokens;

  public Attribute() {
    this.tokens = new ArrayList<Token>();
  }

  public void push(Token e) {
    this.tokens.add(e);
  }

  public List<Token> getTokens() {
    return tokens;
  }

}
