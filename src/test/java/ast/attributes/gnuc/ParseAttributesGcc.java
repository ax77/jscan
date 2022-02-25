package ast.attributes.gnuc;

import java.util.List;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import ast.attributes.Attribute;
import ast.attributes.AttributeList;
import ast.attributes.util.BalancedTokenlistParser;
import ast.parse.Parse;

public class ParseAttributesGcc {
  private final Parse parser;

  public ParseAttributesGcc(Parse parser) {
    this.parser = parser;
  }

  public AttributeList parse() {
    AttributeList result = new AttributeList();

    while (parser.isAttributeStartGnuc()) {
      Attribute attr = parseOneAttribute();
      result.push(attr);
    }

    return result;
  }

  private Attribute parseOneAttribute() {

    Attribute result = new Attribute();
    parser.move(); // __attribute__

    BalancedTokenlistParser balanced = new BalancedTokenlistParser(parser);

    List<Token> list = balanced.parse(T.T_LEFT_PAREN, T.T_RIGHT_PAREN);
    balanced.checkRemoveParens(list, T.T_LEFT_PAREN, T.T_RIGHT_PAREN, 2);

    for (Token t : list) {
      result.push(t);
    }

    return result;
  }

}
