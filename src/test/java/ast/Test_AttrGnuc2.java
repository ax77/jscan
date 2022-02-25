package ast;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import ast.main.ParserMain;
import ast.parse.Parse;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist; 

public class Test_AttrGnuc2 {

  @Test
  public void testAttr1() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  __attribute__((always_inline))                                \n");
    sb.append(" /*002*/  inline int prefix()  { return 1; }                            \n");
    sb.append(" /*003*/  //                                                            \n");
    sb.append(" /*004*/  inline int suffix() __attribute__((always_inline));           \n");
    sb.append(" /*005*/  inline int suffix() { return 1; }                             \n");
    sb.append(" /*006*/  //                                                            \n");
    sb.append(" /*007*/  __attribute__((always_inline))                                \n");
    sb.append(" /*008*/  __attribute__((always_inline))                                \n");
    sb.append(" /*009*/  inline int multiple() { return 2; }                           \n");
    sb.append(" /*010*/  //                                                            \n");
    sb.append(" /*011*/  unsigned int                                                  \n");
    sb.append(" /*012*/  __attribute__ ((bitwidth(1)))                                 \n");
    sb.append(" /*013*/  pref_post()                                                   \n");
    sb.append(" /*014*/  __attribute__ ((nothrow))                                     \n");
    sb.append(" /*015*/  __attribute__ ((noinline));                                   \n");
    sb.append(" /*016*/  //                                                            \n");
    sb.append(" /*017*/  typedef unsigned int __attribute__((bitwidth(1)))        a;   \n");
    sb.append(" /*018*/  typedef unsigned int __attribute__((bitwidth(2)))        b;   \n");
    sb.append(" /*019*/  typedef unsigned int __attribute__ ((__mode__ (__HI__))) c;   \n");
    sb.append(" /*020*/  //                                                            \n");
    sb.append(" /*021*/  int main() {                                                  \n");
    sb.append(" /*022*/      return 0;                                                 \n");
    sb.append(" /*023*/  }                                                             \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse parser = new Parse(it);
    TranslationUnit unit = parser.parse_unit();

    assertEquals(4, unit.countOfFunctionDefinitions());

  }

}
