package ast.attributes;

import java.util.List;

import jscan.tokenize.Token;

public class AsmList {
  private final List<Token> tokens;

  public AsmList(List<Token> tokens) {
    this.tokens = tokens;
  }

  public List<Token> getTokens() {
    return tokens;
  }

}
