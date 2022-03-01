package ast;

import java.io.IOException;

import org.junit.Test;

import ast.main.ParserMain;
import ast.parse.Parse;
import ast.tree.TranslationUnit;
import jscan.parse.Tokenlist;

public class Again0 {

  @Test
  public void test0() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int             \n");
    sb.append(" /*002*/  main()          \n");
    sb.append(" /*003*/  {               \n");
    sb.append(" /*004*/      int x;      \n");
    sb.append(" /*005*/      int *p;     \n");
    sb.append(" /*006*/      x = 4;      \n");
    sb.append(" /*007*/      p = &x;     \n");
    sb.append(" /*008*/      *p = 0;     \n");
    sb.append(" /*009*/      return *p;  \n");
    sb.append(" /*010*/  }               \n");

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);

    TranslationUnit unit = p.parse_unit();
    System.out.println(unit);
  }

}
