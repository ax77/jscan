package ast_util;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ast.flat.func.ExecFlowItem;
import ast.flat.func.RewriteStmt;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.tree.FunctionDefinition;
import ast.tree.Statement;
import ast.tree.TranslationUnit;
import jscan.fio.FileReadKind;
import jscan.fio.FileWrapper;

public class Again0 {

  class Func {
    FunctionDefinition func;
    List<ExecFlowItem> items;
  }

  class Code {
    List<Func> functions;
    List<ExecFlowItem> globals;
  }

  @Test
  public void test0() throws IOException {

    String dir = System.getProperty("user.dir");
    String txt = new FileWrapper(dir + "/cc_tests/01.c").readToString(FileReadKind.APPEND_LF);

    Parse p = new Parse(new ParserMain(new StringBuilder(txt)).preprocess());
    TranslationUnit unit = p.parse_unit();

    final Statement block = unit.getExternalDeclarations().get(0).getFunctionDefinition().getBlock();
    RewriteStmt stmt = new RewriteStmt();
    stmt.genStmt(block);
    List<ExecFlowItem> items = stmt.getItems();

    for (ExecFlowItem item : items) {
      System.out.println(item);
    }
  }

}
