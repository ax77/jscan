package ast.attributes;

import java.util.ArrayList;
import java.util.List;

public class AttributeList {
  private final List<Attribute> attributes;

  public AttributeList() {
    this.attributes = new ArrayList<Attribute>();
  }

  public void push(Attribute e) {
    this.attributes.add(e);
  }

  public List<Attribute> getAttributes() {
    return attributes;
  }

}
