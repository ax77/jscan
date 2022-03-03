package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;
import _st3_linearize_expr.rvalues.FieldAccess;
import _st3_linearize_expr.rvalues.Leaf;
import _st3_linearize_expr.rvalues.Var;

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
