package ast;

import java.io.IOException;

import org.junit.Test;

import ast.decls.Initializer;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.symtab.elements.CSymbol;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist; 

public class Test_DesignatorsArrays {

  @Test
  public void testArrayDesignators0() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                                                       \n");
    sb.append(" /*002*/      int a[][2] = { [0] = { 1,2 }, [1] = { 3,4 }, };                \n");
    sb.append(" /*003*/      return a[0][0]==1 && a[0][1]==2 && a[1][0]==3 && a[1][1]==4;   \n");
    sb.append(" /*004*/  }                                                                  \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

    boolean print = false;
    if (!print) {
      return;
    }

    for (CSymbol sym : unit.getExternalDeclarations().get(0).getFunctionDefinition().getLocals()) {
      if (sym.getInitializer() != null) {
        System.out.printf("name=%s, type=%s\n", sym.getName().getName(), sym.getType());
        for (Initializer init : sym.getInitializer()) {
          System.out.println(init);
        }
        System.out.println();
      }
    }
  }

  @Test
  public void testArrayDesignators1() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                                                       \n");
    sb.append(" /*002*/      int a[][2] = { [1] = { 1,2 }, [0] = { 3,4 }, };                \n");
    sb.append(" /*003*/      return a[0][0]==3 && a[0][1]==4 && a[1][0]==1 && a[1][1]==2;   \n");
    sb.append(" /*004*/  }                                                                  \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

    boolean print = false;
    if (!print) {
      return;
    }

    for (CSymbol sym : unit.getExternalDeclarations().get(0).getFunctionDefinition().getLocals()) {
      if (sym.getInitializer() != null) {
        System.out.printf("name=%s, type=%s\n", sym.getName().getName(), sym.getType());
        for (Initializer init : sym.getInitializer()) {
          System.out.println(init);
        }
        System.out.println();
      }
    }
  }

}
