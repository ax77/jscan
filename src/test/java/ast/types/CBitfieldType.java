package ast.types;

public class CBitfieldType {

  private final CType basetype;
  private final int width;

  public CBitfieldType(CType basetype, int width) {
    this.basetype = basetype;
    this.width = width;
  }

  public int getWidth() {
    return width;
  }

  public CType getBase() {
    return basetype;
  }

}
