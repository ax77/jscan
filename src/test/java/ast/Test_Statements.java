package ast;

import java.io.IOException;

import org.junit.Test;

import ast.errors.ParseException;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.symtab.elements.CSymbol;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist; 

public class Test_Statements {

  @Test
  public void testStatements1() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                \n");
    sb.append(" /*002*/      int label_ambig = 1;    \n");
    sb.append(" /*003*/      if(label_ambig) {       \n");
    sb.append(" /*004*/          label_ambig = 2;    \n");
    sb.append(" /*005*/          goto label_ambig;   \n");
    sb.append(" /*006*/      }                       \n");
    sb.append(" /*007*/  label_ambig :               \n");
    sb.append(" /*008*/      return label_ambig;     \n");
    sb.append(" /*009*/  }                           \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

    boolean print = false;

    if (print) {
      for (CSymbol sym : unit.getExternalDeclarations().get(0).getFunctionDefinition().getLocals()) {
        System.out.println(sym);
      }
    }

  }

  @Test(expected = ParseException.class)
  public void testStatements2() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                \n");
    sb.append(" /*002*/      int label_ambig = 1;    \n");
    sb.append(" /*003*/      if(label_ambig) {       \n");
    sb.append(" /*004*/          label_ambig = 2;    \n");
    sb.append(" /*005*/          goto label_ambig;   \n");
    sb.append(" /*006*/      }                       \n");
    sb.append(" /*007*/  //label_ambig :               \n");
    sb.append(" /*008*/      return label_ambig;     \n");
    sb.append(" /*009*/  }                           \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

    boolean print = false;

    if (print) {
      for (CSymbol sym : unit.getExternalDeclarations().get(0).getFunctionDefinition().getLocals()) {
        System.out.println(sym);
      }
    }

  }

}
