package ast.parse;

import java.util.List;

import ast.tree.AttributesAsmsLists.AttributeList;
import ast.tree.AttributesAsmsLists.CAttribute;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseAttributesGcc {
  private final Parse parser;

  public ParseAttributesGcc(Parse parser) {
    this.parser = parser;
  }

  public AttributeList parse() {
    AttributeList result = new AttributeList();

    while (parser.isAttributeStartGnuc()) {
      CAttribute attr = parseOneAttribute();
      result.push(attr);
    }

    return result;
  }

  private CAttribute parseOneAttribute() {

    CAttribute result = new CAttribute();
    parser.move(); // __attribute__

    ParseBalancedTokenlist balanced = new ParseBalancedTokenlist(parser);

    List<Token> list = balanced.parse(T.T_LEFT_PAREN, T.T_RIGHT_PAREN);
    balanced.checkRemoveParens(list, T.T_LEFT_PAREN, T.T_RIGHT_PAREN, 2);

    for (Token t : list) {
      result.push(t);
    }

    return result;
  }

}
