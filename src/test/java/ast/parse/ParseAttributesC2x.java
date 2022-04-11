package ast.parse;

import static jscan.tokenize.T.T_LEFT_BRACKET;
import static jscan.tokenize.T.T_RIGHT_BRACKET;

import java.util.List;

import ast.tree.AttributesAsmsLists.AttributeList;
import ast.tree.AttributesAsmsLists.CAttribute;
import jscan.tokenize.Token;

public class ParseAttributesC2x {
  private final Parse parser;

  public ParseAttributesC2x(Parse parser) {
    this.parser = parser;
  }

  public AttributeList parse() {
    AttributeList result = new AttributeList();

    while (parser.isAttributeStartC2X()) {
      CAttribute attr = parseOneAttribute();
      result.push(attr);
    }

    return result;
  }

  private CAttribute parseOneAttribute() {

    CAttribute result = new CAttribute();
    ParseBalancedTokenlist balanced = new ParseBalancedTokenlist(parser);

    List<Token> list = balanced.parse(T_LEFT_BRACKET, T_RIGHT_BRACKET);
    balanced.checkRemoveParens(list, T_LEFT_BRACKET, T_RIGHT_BRACKET, 2);

    for (Token t : list) {
      result.push(t);
    }

    return result;
  }

}
