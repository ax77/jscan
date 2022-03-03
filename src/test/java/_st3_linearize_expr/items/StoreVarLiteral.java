package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;
import _st3_linearize_expr.rvalues.Leaf;
import _st3_linearize_expr.rvalues.Var;

public class StoreVarLiteral implements VarCollector {
  private final Var dst;
  private final Leaf src;
  private final boolean isInitializer;

  public StoreVarLiteral(Var dst, Leaf src, boolean isInitializer) {
    this.dst = dst;
    this.src = src;
    this.isInitializer = isInitializer;
  }

  public Var getDst() {
    return dst;
  }

  public Leaf getSrc() {
    return src;
  }

  public boolean isInitializer() {
    return isInitializer;
  }

  @Override
  public String toString() {
    if (isInitializer) {
      return dst.typeNameToString() + " = " + src.toString();
    }
    return dst.toString() + " = " + src.toString();
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(dst);
    if (src.isVar()) {
      vars.add(src.getVar());
    }
    return vars;
  }

}
