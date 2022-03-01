package ast.tree;

import java.util.ArrayList;
import java.util.List;

import jscan.utils.AstParseException;

public class Sswitch {

  private List<Scase> cases;
  private Expression expr;
  private Statement stmt;
  private Sdefault default_stmt;

  public Sswitch(Expression expr) {
    this.cases = new ArrayList<Scase>(1);
    this.expr = expr;
  }

  public void pushcase(Scase onecase) {
    this.cases.add(onecase);
  }

  public Expression getExpr() {
    return expr;
  }

  public void setExpr(Expression expr) {
    this.expr = expr;
  }

  public Statement getStmt() {
    return stmt;
  }

  public void setStmt(Statement stmt) {
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
      throw new AstParseException("duplicate default label");
    }
    this.default_stmt = default_stmt;
  }

}
