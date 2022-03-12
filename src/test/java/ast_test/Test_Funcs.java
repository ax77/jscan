package ast_test;

import java.io.IOException;

import org.junit.Test;

import ast.main.ParseMainNew;
import ast.main.ParseOpts;
import ast.tree.TranslationUnit;

public class Test_Funcs {

  private TranslationUnit parseUnit(StringBuilder sb) throws IOException {
    ParseOpts opts[] = new ParseOpts[] {};
    TranslationUnit unit = new ParseMainNew(opts).parseString(sb.toString());
    return unit;
  }

  @Test
  public void testfuncs1() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  struct tok {                        \n");
    sb.append(" /*002*/   int f, pos;                        \n");
    sb.append(" /*003*/  };                                  \n");
    sb.append(" /*004*/                                      \n");
    sb.append(" /*005*/  int f(int a, int b, int c) {        \n");
    sb.append(" /*006*/   int d = 0;                         \n");
    sb.append(" /*007*/   return a+b+c*d;                    \n");
    sb.append(" /*008*/  }                                   \n");
    sb.append(" /*009*/                                      \n");
    sb.append(" /*010*/  int main(int argc, char **argv) {   \n");
    sb.append(" /*011*/   typedef int i32; struct tok varname;                \n");
    sb.append(" /*012*/   return f(1,2,argc);                \n");
    sb.append(" /*013*/  }                                   \n");
    //@formatter:on

    TranslationUnit unit = parseUnit(sb);
  }

}
