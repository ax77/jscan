package ast_test;

import java.io.IOException;

import org.junit.Test;

import cc.main.ParseMainNew;
import cc.main.ParseOpts;
import cc.tree.TranslationUnit;

public class Test_Current {

  private TranslationUnit parseUnit(StringBuilder sb) throws IOException {
    // ParseOpts.CONCAT_STRINGS, ParseOpts.PREPEND_PREDEFINED_BUFFER
    ParseOpts opts[] = new ParseOpts[] {};
    TranslationUnit unit = new ParseMainNew(opts).parseString(sb.toString());
    return unit;
  }

  @Test
  public void test() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*005*/  int f(int a, int b, int c) {        \n");
    sb.append(" /*006*/   int d = 0;                         \n");
    sb.append(" /*007*/   return a+b+c*d;                    \n");
    sb.append(" /*008*/  }                                   \n");
    //@formatter:on

    TranslationUnit unit = parseUnit(sb);
    System.out.println(unit);
  }

}
