package ast;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import ast.parse.Parse;
import ast.unit.TranslationUnit;
import jscan.tokenize.Stream;
import jscan.tokenize.Token;

public class Test_Generic {

  private static Stream getHashedStream(String source) throws IOException {
    return new Stream("", source);
  }

  @Ignore //TODO:NEW_SOURCE
  @Test
  public void testGenericSelection() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  void g(void);                                                                 \n");
    sb.append(" /*002*/  int a1[_Generic(0, int: 1, short: 2, float: 3, default: 4) == 1 ? 1 : -1];    \n");
    sb.append(" /*003*/  int a2[_Generic(0, default: 1, short: 2, float: 3, int: 4) == 4 ? 1 : -1];    \n");
    sb.append(" /*004*/  int a3[_Generic(0L, int: 1, short: 2, float: 3, default: 4) == 4 ? 1 : -1];   \n");
    sb.append(" /*005*/  int a4[_Generic(0L, default: 1, short: 2, float: 3, int: 4) == 1 ? 1 : -1];   \n");
    sb.append(" /*006*/  int a5[_Generic(0, int: 1, short: 2, float: 3) == 1 ? 1 : -1];                \n");
    sb.append(" /*007*/  int a6[_Generic(0, short: 1, float: 2, int: 3) == 3 ? 1 : -1];                \n");
    sb.append(" /*008*/  int a7[_Generic(\"test\", char *: 1, default: 2) == 1 ? 1 : -1];              \n");
    sb.append(" /*009*/  int a8[_Generic(g, void (*)(void): 1, default: 2) == 1 ? 1 : -1];             \n");
    sb.append(" /*010*/  const int i = 12;                                                             \n");
    sb.append(" /*011*/  int a9[_Generic(i, int: 1, default: 2) == 1 ? 1 : -1];                        \n");
    //@formatter:on

    List<Token> tokenlist = getHashedStream(sb.toString()).getTokenlist();
    Parse p = new Parse(tokenlist);
    p.pushscope();

    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testGenericTypeItself() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main(int argc, char **argv) {                 \n");
    sb.append(" /*002*/      _Static_assert(                               \n");
    sb.append(" /*003*/          sizeof(                                   \n");
    sb.append(" /*004*/              _Generic((long double)0               \n");
    sb.append(" /*005*/                  , int           :(int)0           \n");
    sb.append(" /*006*/                  , long double   :(long double)0   \n");
    sb.append(" /*007*/                  , default       :(char)0          \n");
    sb.append(" /*008*/              )                                     \n");
    sb.append(" /*009*/          ) == sizeof(long double), \"error 1\"     \n");
    sb.append(" /*010*/      );                                            \n");
    sb.append(" /*011*/      _Static_assert(                               \n");
    sb.append(" /*012*/          sizeof(                                   \n");
    sb.append(" /*013*/              _Generic((int)0                       \n");
    sb.append(" /*014*/                  , int           :(int)0           \n");
    sb.append(" /*015*/                  , long double   :(long double)0   \n");
    sb.append(" /*016*/                  , default       :(char)0          \n");
    sb.append(" /*017*/              )                                     \n");
    sb.append(" /*018*/          ) == sizeof(int), \"error 2\"             \n");
    sb.append(" /*019*/      );                                            \n");
    sb.append(" /*020*/      return 0;                                     \n");
    sb.append(" /*021*/  }                                                 \n");
    //@formatter:on

    List<Token> tokenlist = getHashedStream(sb.toString()).getTokenlist();
    Parse p = new Parse(tokenlist);
    p.pushscope();

    TranslationUnit unit = p.parse_unit();

  }

}
