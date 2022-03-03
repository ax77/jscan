package _st3_linearize_expr;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.ir.FlatCodeItem;
import _st3_linearize_expr.rvalues.Var;

public class LinearExpression {
  private final List<FlatCodeItem> items;

  public LinearExpression() {
    this.items = new ArrayList<>();
  }

  public LinearExpression(List<FlatCodeItem> items) {
    this.items = items;
  }

  public FlatCodeItem getLast() {
    return items.get(items.size() - 1);
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public Var getDest() {
    return getLast().getDest();
  }

  public String getDestToString() {
    Var res = getLast().getDest();
    return res.getName().getName();
  }

  public List<FlatCodeItem> getItems() {
    return items;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (FlatCodeItem item : items) {
      sb.append(item.toString());

      boolean needSemicolon = true;
      boolean needNewline = true;

      if (item.isIgnore()) {
        needSemicolon = false;
        needNewline = false;
      }

      if (needSemicolon) {
        sb.append(";");
      }

      if (needNewline) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }

}
