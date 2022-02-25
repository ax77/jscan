package ast;

import java.io.IOException;

import org.junit.Test;

import ast.main.ParserMain;
import ast.parse.Parse;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist; 

public class Test_Tags {

  @Test
  public void testTags1() throws IOException {

    //@formatter:off
    StringBuilder sb11 = new StringBuilder();
    sb11.append(" /*001*/  int                              \n");
    sb11.append(" /*002*/  main()                           \n");
    sb11.append(" /*003*/  {                                \n");
    sb11.append(" /*004*/      struct T { int x; } s1;      \n");
    sb11.append(" /*005*/      s1.x = 1;                    \n");
    sb11.append(" /*006*/      {                            \n");
    sb11.append(" /*007*/          struct T { int y; } s2;  \n");
    sb11.append(" /*008*/          s2.y = 1;                \n");
    sb11.append(" /*009*/          if (s1.x - s2.y != 0)    \n");
    sb11.append(" /*010*/              return 1;            \n");
    sb11.append(" /*011*/      }                            \n");
    sb11.append(" /*012*/      return 0;                    \n");
    sb11.append(" /*013*/  }                                \n");
    //@formatter:on

    Tokenlist it = new ParserMain(new StringBuilder(sb11)).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testTags2() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  // tag naming an unknown struct declares it                     \n");
    sb.append(" /*002*/  struct s* p = 0;                                                \n");
    sb.append(" /*003*/  // definition for the struct pointed to by p                    \n");
    sb.append(" /*004*/  struct s { int a; long long b; };                               \n");
    sb.append(" /*005*/  int main()                                                      \n");
    sb.append(" /*006*/  {                                                               \n");
    sb.append(" /*007*/      // forward declaration of a new, local struct s             \n");
    sb.append(" /*008*/      // this hides global struct s until the end of this block   \n");
    sb.append(" /*009*/      struct s;                                                   \n");
    sb.append(" /*010*/      // pointer to local struct s,                               \n");
    sb.append(" /*011*/      // without the forward declaration above,                   \n");
    sb.append(" /*012*/      // this would point at the file-scope s                     \n");
    sb.append(" /*013*/      struct s *p;                                                \n");
    sb.append(" /*014*/      // definitions of the local struct s                        \n");
    sb.append(" /*015*/      struct s { int p; } varname;                                \n");
    sb.append(" /*016*/      varname.p = 32;                                             \n");
    sb.append(" /*017*/      return sizeof(struct s);                                    \n");
    sb.append(" /*018*/  }                                                               \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testTags3() throws IOException {

    //@formatter:off
    StringBuilder sb9 = new StringBuilder();
    sb9.append(" /*001*/  struct empty;         \n");
    sb9.append(" /*002*/  struct empty;         \n");
    sb9.append(" /*003*/  struct empty {        \n");
    sb9.append(" /*004*/      char c;           \n");
    sb9.append(" /*005*/  };                    \n");
    sb9.append(" /*006*/  int main() {          \n");
    sb9.append(" /*007*/      struct empty e;   \n");
    sb9.append(" /*008*/      e.c = 32;         \n");
    sb9.append(" /*009*/      return 0;         \n");
    sb9.append(" /*010*/  }                     \n");
    //@formatter:on

    Tokenlist it = new ParserMain(new StringBuilder(sb9)).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testTags4() throws IOException {

    //@formatter:off
    StringBuilder sb_044 = new StringBuilder();
    sb_044.append(" /*001*/  struct T;                     \n");
    sb_044.append(" /*002*/  struct T {                    \n");
    sb_044.append(" /*003*/      int x;                    \n");
    sb_044.append(" /*004*/  };                            \n");
    sb_044.append(" /*005*/  int                           \n");
    sb_044.append(" /*006*/  main()                        \n");
    sb_044.append(" /*007*/  {                             \n");
    sb_044.append(" /*008*/      struct T v;               \n");
    sb_044.append(" /*009*/      { struct T { int z; }; }  \n");
    sb_044.append(" /*010*/      v.x = 2;                  \n");
    sb_044.append(" /*011*/      if(v.x != 2)              \n");
    sb_044.append(" /*012*/          return 1;             \n");
    sb_044.append(" /*013*/      return 0;                 \n");
    sb_044.append(" /*014*/  }                             \n");
    //@formatter:on

    Tokenlist it = new ParserMain(new StringBuilder(sb_044)).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testTags5() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  // tag naming an unknown struct declares it                     \n");
    sb.append(" /*002*/  struct s* p = 0;                                                \n");
    sb.append(" /*003*/  // definition for the struct pointed to by p                    \n");
    sb.append(" /*004*/  struct s { int a; long long b; };                               \n");
    sb.append(" /*005*/  int main()                                                      \n");
    sb.append(" /*006*/  {                                                               \n");
    sb.append(" /*007*/      // forward declaration of a new, local struct s             \n");
    sb.append(" /*008*/      // this hides global struct s until the end of this block   \n");
    sb.append(" /*009*/      struct s;                                                   \n");
    sb.append(" /*010*/      // error: struct s incomplete here...                       \n");
    sb.append(" /*011*/      // unsigned long x = sizeof(struct s);                      \n");
    sb.append(" /*012*/      // pointer to local struct s,                               \n");
    sb.append(" /*013*/      // without the forward declaration above,                   \n");
    sb.append(" /*014*/      // this would point at the file-scope s                     \n");
    sb.append(" /*015*/      struct s *p;                                                \n");
    sb.append(" /*016*/      // definitions of the local struct s                        \n");
    sb.append(" /*017*/      struct s { int flag; } varname;                             \n");
    sb.append(" /*018*/      varname.flag = 32;                                          \n");
    sb.append(" /*019*/      p = &varname;                                               \n");
    sb.append(" /*020*/      p->flag = 33;                                               \n");
    sb.append(" /*021*/      return sizeof(struct s);                                    \n");
    sb.append(" /*022*/  }                                                               \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

}
