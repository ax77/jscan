package ast.stmt;

import ast.expr.CExpression;
import ast.stmt.main.CStatement;

public class Scase {

  private final Sswitch parent;
  private CExpression constexpr;
  private CStatement casestmt;

  public Scase(Sswitch parent, CExpression constexpr) {
    this.parent = parent;
    this.constexpr = constexpr;
  }

  public CExpression getConstexpr() {
    return constexpr;
  }

  public void setConstexpr(CExpression constexpr) {
    this.constexpr = constexpr;
  }

  public CStatement getCasestmt() {
    return casestmt;
  }

  public void setCasestmt(CStatement casestmt) {
    this.casestmt = casestmt;
  }

  public Sswitch getParent() {
    return parent;
  }

}
