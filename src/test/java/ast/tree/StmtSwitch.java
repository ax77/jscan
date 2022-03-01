package ast.tree;

import java.util.ArrayList;
import java.util.List;

import jscan.utils.AstParseException;

public class StmtSwitch {

  private List<StmtCase> cases;
  private Expression expr;
  private Statement stmt;
  private StmtDefault default_stmt;

  public StmtSwitch(Expression expr) {
    this.cases = new ArrayList<StmtCase>(1);
    this.expr = expr;
  }

  public void pushcase(StmtCase onecase) {
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

  public List<StmtCase> getCases() {
    return cases;
  }

  public void setCases(List<StmtCase> cases) {
    this.cases = cases;
  }

  public StmtDefault getDefault_stmt() {
    return default_stmt;
  }

  public void setDefault_stmt(StmtDefault default_stmt) {
    if (this.default_stmt != null) {
      throw new AstParseException("duplicate default label");
    }
    this.default_stmt = default_stmt;
  }

}
