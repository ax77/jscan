package ast.tree;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.Token;

public class AttributesAsmsLists {
  private AttributeList attributeList;
  private AsmList asmList;
  private AttributeList attributeListC2x; // TODO: merge with one. just for tests now.

  public AttributeList getAttributeList() {
    return attributeList;
  }

  public void setAttributeList(AttributeList attributeList) {
    this.attributeList = attributeList;
  }

  public AsmList getAsmList() {
    return asmList;
  }

  public void setAsmList(AsmList asmList) {
    this.asmList = asmList;
  }

  public AttributeList getAttributeListC2x() {
    return attributeListC2x;
  }

  public void setAttributeListC2x(AttributeList attributeListC2x) {
    this.attributeListC2x = attributeListC2x;
  }

  // Nodes

  public static class CAttribute {
    private final List<Token> tokens;

    public CAttribute() {
      this.tokens = new ArrayList<Token>();
    }

    public void push(Token e) {
      this.tokens.add(e);
    }

    public List<Token> getTokens() {
      return tokens;
    }

  }

  public static class AsmList {
    private final List<Token> tokens;

    public AsmList(List<Token> tokens) {
      this.tokens = tokens;
    }

    public List<Token> getTokens() {
      return tokens;
    }

  }

  public static class AttributeList {
    private final List<CAttribute> attributes;

    public AttributeList() {
      this.attributes = new ArrayList<CAttribute>();
    }

    public void push(CAttribute e) {
      this.attributes.add(e);
    }

    public List<CAttribute> getAttributes() {
      return attributes;
    }

  }

}
