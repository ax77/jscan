package ast_test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ast.parse.Parse;
import ast.tree.TranslationUnit;
import jscan.preproc.preprocess.Scan;
import jscan.symtab.KeywordsInits;
import jscan.tokenize.Stream;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class Test_Typedefs2 {

  @Test
  public void testTypedefs8() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  #define COMPOUND_DESIGNATORS (0)                             \n");
    sb.append(" /*002*/                                                               \n");
    sb.append(" /*003*/  struct typedef_after {                                       \n");
    sb.append(" /*004*/      int flag;                                                \n");
    sb.append(" /*005*/  } typedef typedef_after, *p_typedef_after;                   \n");
    sb.append(" /*006*/                                                               \n");
    sb.append(" /*007*/  static int test_typedefs_1() {                               \n");
    sb.append(" /*008*/      typedef int i32;                                         \n");
    sb.append(" /*009*/      typedef i32 i32;                                         \n");
    sb.append(" /*010*/      int signed typedef i32;                                  \n");
    sb.append(" /*011*/      i32 typedef i32;                                         \n");
    sb.append(" /*012*/      typedef i32 dword;                                       \n");
    sb.append(" /*013*/                                                               \n");
    sb.append(" /*014*/      i32 x = 1;                                               \n");
    sb.append(" /*015*/      x -= 1;                                                  \n");
    sb.append(" /*016*/                                                               \n");
    sb.append(" /*017*/      dword y = 1;                                             \n");
    sb.append(" /*018*/      y -= 1;                                                  \n");
    sb.append(" /*019*/                                                               \n");
    sb.append(" /*020*/      return x + y;                                            \n");
    sb.append(" /*021*/  }                                                            \n");
    sb.append(" /*022*/                                                               \n");
    sb.append(" /*023*/  static int test_desg_1() {                                   \n");
    sb.append(" /*024*/  #if COMPOUND_DESIGNATORS                                     \n");
    sb.append(" /*025*/      struct s {                                               \n");
    sb.append(" /*026*/          int a[2][2];                                         \n");
    sb.append(" /*027*/      };                                                       \n");
    sb.append(" /*028*/      struct s var = {                                         \n");
    sb.append(" /*029*/          .a[0][0] = 1,                                        \n");
    sb.append(" /*030*/          .a[1][0] = 2,                                        \n");
    sb.append(" /*031*/      };                                                       \n");
    sb.append(" /*032*/      int result = var.a[0][0] + var.a[1][0];                  \n");
    sb.append(" /*033*/      return (result == 3) ? 0 : 1;                            \n");
    sb.append(" /*034*/  #else                                                        \n");
    sb.append(" /*035*/      return 0;                                                \n");
    sb.append(" /*036*/  #endif                                                       \n");
    sb.append(" /*037*/  }                                                            \n");
    sb.append(" /*038*/                                                               \n");
    sb.append(" /*039*/  struct some {                                                \n");
    sb.append(" /*040*/      enum toktype {                                           \n");
    sb.append(" /*041*/          t_ident, t_string,                                   \n");
    sb.append(" /*042*/      };   // warning: declaration does not declare anything   \n");
    sb.append(" /*043*/      int; // warning: declaration does not declare anything   \n");
    sb.append(" /*044*/      int field;                                               \n");
    sb.append(" /*045*/  };                                                           \n");
    sb.append(" /*046*/                                                               \n");
    sb.append(" /*047*/  int main() {                                                 \n");
    sb.append(" /*048*/      enum toktype tp = t_ident;                               \n");
    sb.append(" /*049*/      long long result = 0;                                    \n");
    sb.append(" /*050*/      result += test_desg_1();                                 \n");
    sb.append(" /*051*/      result += test_typedefs_1();                             \n");
    sb.append(" /*052*/                                                               \n");
    sb.append(" /*053*/      return (result == 0) ? 0 : 1;                            \n");
    sb.append(" /*054*/  }                                                            \n");
    sb.append(" /*055*/                                                               \n");
    //@formatter:on

    KeywordsInits.initIdents();
    List<Token> tokens = new Stream("utest", sb.toString()).getTokenlist();
    List<Token> pp = new ArrayList<Token>();
    Scan s = new Scan(tokens);
    for (;;) {
      Token tok = s.get();
      if (tok.ofType(T.TOKEN_EOF)) {
        pp.add(tok);
        break;
      }
      if (tok.typeIsSpecialStreamMarks()) {
        continue;
      }
      pp.add(tok);
    }

    Parse parser = new Parse(pp);
    TranslationUnit unit = parser.parse_unit();

  }

}
