package ast.flat.items;

import java.util.ArrayList;
import java.util.List;

import ast.flat.VarCollector;
import ast.flat.rvalues.Var;

public class BuiltinFuncAssertTrue implements VarCollector {

  // void assert_true(int cnd, const char *file, int line, const char *expr)

  private final String cond;
  private final String file;
  private final String line;
  private final String expr;

  public BuiltinFuncAssertTrue(String cond, String file, String line, String expr) {
    this.cond = cond;
    this.file = file;
    this.line = line;
    this.expr = expr;
  }

  public String getCond() {
    return cond;
  }

  public String getFile() {
    return file;
  }

  public String getLine() {
    return line;
  }

  public String getExpr() {
    return expr;
  }

  @Override
  public String toString() {
    return "assert_true(" + cond + ", " + file + ", " + line + ", " + expr + ")";
  }

  @Override
  public List<Var> getAllVars() {
    return new ArrayList<Var>();
  }

}
