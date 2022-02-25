package ast;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ast.errors.ParseException;
import ast.parse.Parse;
import ast.unit.TranslationUnit;
import jscan.tokenize.Stream;
import jscan.tokenize.Token;

public class Test_StaticAssert {

  private static Stream getHashedStream(String source) throws IOException {
    return new Stream("", source);
  }

  @Test
  public void testStaticAssert0() throws IOException {
    StringBuilder sb = new StringBuilder();
    //@formatter:off
    sb.append(" /*001*/  struct s                                      \n");
    sb.append(" /*002*/  {                                             \n");
    sb.append(" /*003*/    int a;                                      \n");
    sb.append(" /*004*/    _Static_assert (3, \"s\");                  \n");
    sb.append(" /*005*/    int b;                                      \n");
    sb.append(" /*006*/  };                                            \n");
    sb.append(" /*007*/  union u                                       \n");
    sb.append(" /*008*/  {                                             \n");
    sb.append(" /*009*/    int i;                                      \n");
    sb.append(" /*010*/    _Static_assert (1, L\"\");                  \n");
    sb.append(" /*011*/  };                                            \n");
    sb.append(" /*012*/  void                                          \n");
    sb.append(" /*013*/  f ()                                      \n");
    sb.append(" /*014*/  {                                             \n");
    sb.append(" /*015*/    int i;                                      \n");
    sb.append(" /*016*/    i = 1;                                      \n");
    sb.append(" /*017*/    _Static_assert (0 + 1, \"f\");              \n");
    sb.append(" /*018*/    i = 2;                                      \n");
    sb.append(" /*019*/  }                                             \n");
    sb.append(" /*020*/  void                                          \n");
    sb.append(" /*021*/  g ()                                      \n");
    sb.append(" /*022*/  {                                             \n");
    sb.append(" /*023*/    int i = 0;                                  \n");
    sb.append(" /*024*/    for (_Static_assert (1, \"\"); i < 10; i++) \n");
    sb.append(" /*025*/      ;                                         \n");
    sb.append(" /*026*/  }                                             \n");
    sb.append(" /*027*/  int main() {                                  \n");
    sb.append(" /*028*/      return 0;                                 \n");
    sb.append(" /*029*/  }                                             \n");

    List<Token> tokenlist = getHashedStream(sb.toString()).getTokenlist();
    Parse p = new Parse(tokenlist);
    TranslationUnit unit = p.parse_unit();

  }
  
  @Test
  public void testStaticAssert1() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {             \n");
    sb.append(" /*002*/      _Static_assert(1);   \n");
    sb.append(" /*003*/      return 0;            \n");
    sb.append(" /*004*/  }                        \n");
    //@formatter:on

    List<Token> tokenlist = getHashedStream(sb.toString()).getTokenlist();
    Parse p = new Parse(tokenlist);
    TranslationUnit unit = p.parse_unit();

  }

  @Test(expected = ParseException.class)
  public void testStaticAssert2() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {             \n");
    sb.append(" /*002*/      _Static_assert(0);   \n");
    sb.append(" /*003*/      return 0;            \n");
    sb.append(" /*004*/  }                        \n");
    //@formatter:on

    List<Token> tokenlist = getHashedStream(sb.toString()).getTokenlist();
    Parse p = new Parse(tokenlist);
    TranslationUnit unit = p.parse_unit();

  }
}
