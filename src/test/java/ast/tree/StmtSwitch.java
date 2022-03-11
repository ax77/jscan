package ast.tree;

import java.util.ArrayList;
import java.util.List;

import jscan.utils.AstParseException;

public class StmtSwitch {

  public List<StmtCase> cases;
  public Expression expr;
  public Statement stmt;
  public StmtDefault defaultStmt;

  public StmtSwitch(Expression expr) {
    this.cases = new ArrayList<StmtCase>(1);
    this.expr = expr;
  }

  public void pushcase(StmtCase onecase) {
    this.cases.add(onecase);
  }

  public void setDefault_stmt(StmtDefault defaultStmt) {
    if (this.defaultStmt != null) {
      throw new AstParseException("duplicate default label");
    }
    this.defaultStmt = defaultStmt;
  }

}
