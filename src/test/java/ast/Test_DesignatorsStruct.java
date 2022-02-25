package ast;

import java.io.IOException;

import org.junit.Test;

import ast.decls.Initializer;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.symtab.elements.CSymbol;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist; 

public class Test_DesignatorsStruct {

  @Test
  public void testStructDesignators1() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                   \n");
    sb.append(" /*002*/      struct s {                 \n");
    sb.append(" /*003*/          int a;                 \n");
    sb.append(" /*004*/          int b[3];              \n");
    sb.append(" /*005*/      } varname = {              \n");
    sb.append(" /*006*/          .a = 1,                \n");
    sb.append(" /*007*/          .b = { 2,3,4 }         \n");
    sb.append(" /*008*/      };                         \n");
    sb.append(" /*009*/      return sizeof(struct s);   \n");
    sb.append(" /*010*/  }                              \n");
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
  public void testStructDesignators2() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                   \n");
    sb.append(" /*002*/      struct s {                 \n");
    sb.append(" /*003*/          int a;                 \n");
    sb.append(" /*004*/          int b[3];              \n");
    sb.append(" /*005*/          struct num {           \n");
    sb.append(" /*006*/              int i32;           \n");
    sb.append(" /*007*/              long i64;          \n");
    sb.append(" /*008*/          } num;                 \n");
    sb.append(" /*009*/      } varname = {              \n");
    sb.append(" /*010*/          .a = 1,                \n");
    sb.append(" /*011*/          .b = { 2,3,4 },        \n");
    sb.append(" /*012*/          .num = {               \n");
    sb.append(" /*013*/              .i32 = 5,          \n");
    sb.append(" /*014*/              .i64 = 6,          \n");
    sb.append(" /*015*/          },                     \n");
    sb.append(" /*016*/      };                         \n");
    sb.append(" /*017*/      return sizeof(struct s);   \n");
    sb.append(" /*018*/  }                              \n");
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
