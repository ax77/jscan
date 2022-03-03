package ast.flat.items;

import java.util.ArrayList;
import java.util.List;

import ast.flat.VarCollector;
import ast.flat.rvalues.FieldAccess;
import ast.flat.rvalues.Leaf;
import ast.flat.rvalues.Var;

public class StoreFieldLiteral implements VarCollector {
  private final FieldAccess dst;
  private final Leaf src;

  public StoreFieldLiteral(FieldAccess dst, Leaf src) {
    this.dst = dst;
    this.src = src;
  }

  public FieldAccess getDst() {
    return dst;
  }

  public Leaf getSrc() {
    return src;
  }

  @Override
  public String toString() {
    return dst.toString() + " = " + src.toString();
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.addAll(dst.getAllVars());
    if (src.isVar()) {
      vars.add(src.getVar());
    }
    return vars;
  }

}
