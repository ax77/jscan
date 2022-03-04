package ast.flat;

import java.util.ArrayList;
import java.util.List;

import ast.flat.ir.FlatCodeItem;
import ast.flat.rvalues.Var;

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
      if (item.isIgnore()) {
        continue;
      }
      sb.append(item.toString().trim() + "\n");
    }
    return sb.toString().trim();
  }

}
