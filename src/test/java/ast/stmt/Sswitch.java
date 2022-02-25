package ast.stmt;

import java.util.ArrayList;
import java.util.List;

import ast.errors.ParseException;
import ast.expr.CExpression;
import ast.stmt.main.CStatement;

public class Sswitch {

  private List<Scase> cases;
  private CExpression expr;
  private CStatement stmt;
  private Sdefault default_stmt;

  public Sswitch(CExpression expr) {
    this.cases = new ArrayList<Scase>(1);
    this.expr = expr;
  }

  public void pushcase(Scase onecase) {
    this.cases.add(onecase);
  }

  public CExpression getExpr() {
    return expr;
  }

  public void setExpr(CExpression expr) {
    this.expr = expr;
  }

  public CStatement getStmt() {
    return stmt;
  }

  public void setStmt(CStatement stmt) {
    this.stmt = stmt;
  }

  public List<Scase> getCases() {
    return cases;
  }

  public void setCases(List<Scase> cases) {
    this.cases = cases;
  }

  public Sdefault getDefault_stmt() {
    return default_stmt;
  }

  public void setDefault_stmt(Sdefault default_stmt) {
    if (this.default_stmt != null) {
      throw new ParseException("duplicate default label");
    }
    this.default_stmt = default_stmt;
  }

}
