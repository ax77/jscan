package ast.attributes.main;

import ast.attributes.AttributeList;
import ast.attributes.asm.AsmList;

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

}
