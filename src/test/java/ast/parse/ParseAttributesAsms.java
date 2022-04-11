package ast.parse;

import ast.tree.AttributesAsmsLists;
import ast.tree.AttributesAsmsLists.AsmList;
import ast.tree.AttributesAsmsLists.AttributeList;

public class ParseAttributesAsms {
  private final Parse parser;

  public ParseAttributesAsms(Parse parser) {
    this.parser = parser;
  }

  public AttributesAsmsLists parse() {
    AttributesAsmsLists retval = new AttributesAsmsLists();

    if (parser.isAttributeStartGnuc()) {
      AttributeList attributeList = new ParseAttributesGcc(parser).parse();
      retval.setAttributeList(attributeList);
    }

    if (parser.isAsmStart()) {
      AsmList asmList = new ParseAsm(parser).parse();
      retval.setAsmList(asmList);
    }

    if (parser.isAttributeStartC2X()) {
      AttributeList attributeList = new ParseAttributesC2x(parser).parse();
      retval.setAttributeListC2x(attributeList);
    }

    return retval;
  }

}
