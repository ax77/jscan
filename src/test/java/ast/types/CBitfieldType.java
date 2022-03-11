package ast.types;

public class CBitfieldType {

  public final CType basetype;
  public final int width;

  public CBitfieldType(CType basetype, int width) {
    this.basetype = basetype;
    this.width = width;
  }

}
