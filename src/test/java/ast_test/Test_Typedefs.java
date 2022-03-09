package ast_test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;

import ast.builders.ConstexprEval;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.parse.ParseExpression;
import ast.tree.Expression;
import ast.tree.TranslationUnit;
import jscan.parse.Tokenlist;
import jscan.tokenize.Stream;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;

public class Test_Typedefs {

  private Tokenlist getTokenlist(StringBuilder sb) throws IOException {
    return new ParserMain(sb).preprocess();
  }

  private static Stream getHashedStream(String source) throws IOException {
    return new Stream("", source);
  }

  @Test
  public void testTypedefs0() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append("typedef int i32, *pi32;\n");
    sb.append("i32 x = 0;\n");
    sb.append("pi32 y = &x;\n");
    sb.append("static int z = -1;\n");

    String methodName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    Tokenlist tokenlist = getTokenlist(sb);

    Parse p = new Parse(tokenlist);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testTypedefs1() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  typedef int i32;                                 \n");
    sb.append(" /*002*/  int typedef i32;                                 \n");
    sb.append(" /*003*/  typedef i32 i32, i32arr5[5], (*i32func)();       \n");
    sb.append(" /*004*/  typedef i32 WORD;                                \n");
    sb.append(" /*005*/  unsigned long long int typedef u64, *pu64;       \n");
    sb.append(" /*006*/  WORD x;                                          \n");
    sb.append(" /*007*/  i32 y;                                           \n");
    sb.append(" /*008*/  const i32 ci;                                    \n");
    sb.append(" /*009*/  static WORD si;                                  \n");
    sb.append(" /*010*/  static const u64 scu;                            \n");
    sb.append(" /*011*/  static pu64 p;                                   \n");
    sb.append(" /*014*/  int typedef (*fn)(int, char*), i32, WORD;        \n");
    sb.append(" /*018*/  signed short int typedef i16, *pi16, HWORD;      \n");
    sb.append(" /*019*/  unsigned typedef char i8, *pi8, BYTE, *PBYTE;    \n");

    Tokenlist tokenlist = getTokenlist(sb);

    Parse p = new Parse(tokenlist);
    TranslationUnit unit = p.parse_unit();
  }

  @Test
  public void testTypedefs2() throws IOException {
    StringBuilder sb = new StringBuilder();
    //@formatter:off
    sb.append(" /*001*/  int main()                                            \n");
    sb.append(" /*002*/  {                                                     \n");
    sb.append(" /*003*/      typedef float REAL;                               \n");
    sb.append(" /*004*/      REAL x1;                                          \n");
    sb.append(" /*005*/      float x2;                                         \n");
    sb.append(" /*006*/      const REAL y = 0.0;                               \n");
    sb.append(" /*007*/      static REAL z;                                    \n");
    sb.append(" /*008*/      typedef unsigned long int ULONG;                  \n");
    sb.append(" /*009*/      typedef float TEMP;                               \n");
    sb.append(" /*010*/      typedef float WIND_SPEED;                         \n");
    sb.append(" /*011*/      typedef struct {int a; int b;} S, *PS;            \n");
    sb.append(" /*012*/      PS ps1;                                           \n");
    sb.append(" /*013*/      S* ps2;                                           \n");
    sb.append(" /*014*/      typedef union {int i; float f;} U, *PU;           \n");
    sb.append(" /*015*/      PU pu1;                                           \n");
    sb.append(" /*016*/      U* pu2;                                           \n");
    sb.append(" /*017*/      typedef enum {club,diamond,heart,spade} E, *PE;   \n");
    sb.append(" /*018*/      PE pe1;                                           \n");
    sb.append(" /*019*/      E* pe2;                                           \n");
    sb.append(" /*020*/      typedef int ARR_T[10];                            \n");
    sb.append(" /*021*/      ARR_T a1;                                         \n");
    sb.append(" /*022*/      int a2[10];                                       \n");
    sb.append(" /*023*/      int i=0;                                          \n");
    sb.append(" /*024*/      typedef int * PTR_TO_INT;                         \n");
    sb.append(" /*025*/      PTR_TO_INT pi = &i;                               \n");
    sb.append(" /*026*/      typedef void (*FP)(void);                         \n");
    sb.append(" /*027*/      typedef signed int t;                             \n");
    sb.append(" /*028*/      typedef int plain;                                \n");
    sb.append(" /*029*/      struct tag {                                      \n");
    sb.append(" /*030*/          unsigned t:4;                                 \n");
    sb.append(" /*031*/          const t:5;                                    \n");
    sb.append(" /*032*/          plain r:5;                                    \n");
    sb.append(" /*033*/      };                                                \n");
    sb.append(" /*034*/      return 0;                                         \n");
    sb.append(" /*035*/  }                                                     \n");
    //@formatter:on

    Tokenlist tokenlist = getTokenlist(sb);

    Parse p = new Parse(tokenlist);
    TranslationUnit unit = p.parse_unit();
  }

  @Test
  public void testTypedefs3() throws IOException {
    StringBuilder sb = new StringBuilder();
    //@formatter:off
    sb.append(" /*001*/  typedef int i32;                                             \n");
    sb.append(" /*002*/  int typedef i32;                                             \n");
    sb.append(" /*003*/  typedef i32 i32, i32arr5[5], (*i32func)();                   \n");
    sb.append(" /*004*/  typedef i32 WORD;                                            \n");
    sb.append(" /*005*/  unsigned long long int typedef u64, *pu64;                   \n");
    sb.append(" /*006*/  WORD x;                                                      \n");
    sb.append(" /*007*/  i32 y;                                                       \n");
    sb.append(" /*008*/  const i32 ci;                                                \n");
    sb.append(" /*009*/  static WORD si;                                              \n");
    sb.append(" /*010*/  static const u64 scu;                                        \n");
    sb.append(" /*011*/  static pu64 p;                                               \n");
    sb.append(" /*012*/  typedef struct stag { char c; } sname, *psname, **ppsname;   \n");
    sb.append(" /*013*/  typedef enum etag { ok = 32 } ename;                         \n");
    sb.append(" /*014*/  int typedef (*fn)(int, char*), i32, WORD;                    \n");
    sb.append(" /*015*/  int msg(int k, char *m) {}                                   \n");
    sb.append(" /*016*/  typedef struct token TOKEN, *PTOKEN;                         \n");
    sb.append(" /*017*/  struct token { char *sval; };                                \n");
    sb.append(" /*018*/  signed short int typedef i16, *pi16, HWORD;                  \n");
    sb.append(" /*019*/  unsigned typedef char i8, *pi8, BYTE, *PBYTE;                \n");
    sb.append(" /*020*/  BYTE const typedef (*ipf)(i8, i16, i32);                     \n");
    sb.append(" /*021*/  int main(int argc, char **argv) {                            \n");
    sb.append(" /*022*/      fn printer1 = &msg;                                      \n");
    sb.append(" /*023*/      fn printer2 = msg;                                       \n");
    sb.append(" /*024*/      TOKEN *token;                                            \n");
    sb.append(" /*025*/      PTOKEN const ptoken;                                     \n");
    sb.append(" /*026*/      static BYTE b1 = ok;                                     \n");
    sb.append(" /*027*/      PBYTE const pb1 = &b1;                                   \n");
    sb.append(" /*028*/      typedef signed int t;                                    \n");
    sb.append(" /*029*/      typedef int plain;                                       \n");
    sb.append(" /*030*/          struct tag {                                         \n");
    sb.append(" /*031*/          unsigned t:4;                                        \n");
    sb.append(" /*032*/          const t:5;                                           \n");
    sb.append(" /*033*/          plain r:5;                                           \n");
    sb.append(" /*034*/      };                                                       \n");
    sb.append(" /*035*/      long long int typedef i32;                               \n");
    sb.append(" /*036*/      i32 xxx;                                                 \n");
    sb.append(" /*037*/      typedef int alias;                                       \n");
    sb.append(" /*038*/      alias a1 = 0;                                            \n");
    sb.append(" /*039*/      {                                                        \n");
    sb.append(" /*040*/          typedef float alias;                                 \n");
    sb.append(" /*041*/          float typedef alias;                                 \n");
    sb.append(" /*042*/          alias a1;                                            \n");
    sb.append(" /*043*/          {                                                    \n");
    sb.append(" /*044*/              long unsigned typedef alias;                     \n");
    sb.append(" /*045*/              alias a1;                                        \n");
    sb.append(" /*046*/          }                                                    \n");
    sb.append(" /*047*/      }                                                        \n");
    sb.append(" /*048*/      return a1;                                               \n");
    sb.append(" /*049*/  }                                                            \n");
    //@formatter:on

    Tokenlist tokenlist = getTokenlist(sb);

    Parse p = new Parse(tokenlist);
    TranslationUnit unit = p.parse_unit();
  }

  @Test
  public void testTypedefs4() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  typedef int i32;                                      \n");
    sb.append(" /*002*/  typedef i32 i32;                                      \n");
    sb.append(" /*003*/  int typedef i32;                                      \n");
    sb.append(" /*004*/  i32 typedef const word;                               \n");
    sb.append(" /*005*/  i32 x;                                                \n");
    sb.append(" /*006*/  static i32 y;                                         \n");
    sb.append(" /*007*/  const i32 z;                                          \n");
    sb.append(" /*008*/  typedef struct token { int f; } token_t, *ptoken_t;   \n");
    sb.append(" /*009*/  typedef token_t token_t;                              \n");
    sb.append(" /*010*/  token_t tok;                                          \n");
    sb.append(" /*011*/  ptoken_t ptok;                                        \n");
    sb.append(" /*012*/  i32 typedef i32, int32_ht, *pi32;                     \n");

    Tokenlist tokenlist = getTokenlist(sb);

    Parse p = new Parse(tokenlist);
    TranslationUnit unit = p.parse_unit();
  }

  @Test(expected = AstParseException.class)
  public void testTypedefs5() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  typedef struct test {         \n");
    sb.append(" /*002*/    int a;                      \n");
    sb.append(" /*003*/    int b;                      \n");
    sb.append(" /*004*/  } test_t;                     \n");
    sb.append(" /*005*/  typedef struct test test_t;   \n");
    sb.append(" /*006*/  typedef int test_t;           \n");
    sb.append(" /*007*/  test_t x;                     \n");

    Tokenlist tokenlist = getTokenlist(sb);

    Parse p = new Parse(tokenlist);
    TranslationUnit unit = p.parse_unit();
  }

  @Test
  public void testTypedefs6() throws IOException {

    StringBuilder sb = new StringBuilder();

    //@formatter:off
    sb.append(" /*001*/  typedef char alias; alias x;                                                         \n");
    sb.append(" /*002*/  void f() {                                                                           \n");
    sb.append(" /*003*/    typedef short int alias; alias x;                                                  \n");
    sb.append(" /*004*/  }                                                                                    \n");
    sb.append(" /*005*/  int main()                                                                           \n");
    sb.append(" /*006*/  {                                                                                    \n");
    sb.append(" /*007*/    typedef int alias; alias x;                                                        \n");
    sb.append(" /*008*/    {                                                                                  \n");
    sb.append(" /*009*/      typedef long alias; alias x;                                                     \n");
    sb.append(" /*010*/      {                                                                                \n");
    sb.append(" /*011*/        typedef long double alias; alias x;                                            \n");
    sb.append(" /*012*/      }                                                                                \n");
    sb.append(" /*013*/    }                                                                                  \n");
    sb.append(" /*014*/    typedef alias alias; //define(alias, typedef, int(comes from alias td-name))       \n");
    sb.append(" /*015*/    int typedef alias;   //define(alias, typedef, int)                                 \n");
    sb.append(" /*016*/    alias typedef alias; //define(alias, typedef, int(comes from alias td-name))       \n");
    sb.append(" /*017*/    typedef alias alias; //define(alias, typedef, int(comes from alias td-name))       \n");
    sb.append(" /*018*/    signed typedef alias, (*ptrto_funcret_alias)();                                    \n");
    sb.append(" /*019*/    int signed typedef alias, *ptrto_alias, arrof_alias[3], (*ptrto_arrof_alias)[3];   \n");
    sb.append(" /*020*/    static const alias a;                                                              \n");
    sb.append(" /*021*/    struct test {                                                                      \n");
    sb.append(" /*022*/      alias a;                                                                         \n");
    sb.append(" /*023*/      unsigned alias : 4;                                                              \n");
    sb.append(" /*024*/      alias : 1; // unnamed bitfield                                                   \n");
    sb.append(" /*025*/      struct {                                                                         \n");
    sb.append(" /*026*/        alias a;                                                                       \n");
    sb.append(" /*027*/        alias alias;                                                                   \n");
    sb.append(" /*028*/        const alias b;                                                                 \n");
    sb.append(" /*029*/      } s;                                                                             \n");
    sb.append(" /*030*/    };                                                                                 \n");
    sb.append(" /*031*/    struct test strtest;                                                               \n");
    sb.append(" /*032*/    strtest.alias = 1;                                                                 \n");
    sb.append(" /*033*/    // $6.7.8.6                                                                        \n");
    sb.append(" /*034*/    typedef signed int t;                                                              \n");
    sb.append(" /*035*/      typedef int plain;                                                               \n");
    sb.append(" /*036*/    struct tag {                                                                       \n");
    sb.append(" /*037*/        unsigned t;                                                                    \n");
    sb.append(" /*038*/        const t:5; // unnamed bitfield                                                 \n");
    sb.append(" /*039*/        plain r;                                                                       \n");
    sb.append(" /*040*/    };                                                                                 \n");
    sb.append(" /*041*/    struct tag str;                                                                    \n");
    sb.append(" /*042*/    str.t = 1; // f1                                                                   \n");
    sb.append(" /*043*/    str.r = 2; // f3                                                                   \n");
    sb.append(" /*044*/    typedef int i32;                                                                   \n");
    sb.append(" /*045*/    //static const i32; // warning: declaration does not declare anything              \n");
    sb.append(" /*046*/    static const i32 xx;                                                               \n");
    sb.append(" /*047*/    return sizeof(a);                                                                  \n");
    sb.append(" /*048*/  }                                                                                    \n");
    //@formatter:on

    Tokenlist tokenlist = getTokenlist(sb);

    Parse p = new Parse(tokenlist);
    TranslationUnit unit = p.parse_unit();
  }

  @Test
  public void testTypedefs7() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                                                            \n");
    sb.append(" /*002*/      // 1) typedef declaration                                           \n");
    sb.append(" /*003*/      typedef int i32;                                                    \n");
    sb.append(" /*004*/      int typedef i32;                                                    \n");
    sb.append(" /*005*/      typedef struct cnumber { int i32; } cnumber_t;                      \n");
    sb.append(" /*006*/      typedef enum regs { eax, ecx, edx } regs;                           \n");
    sb.append(" /*007*/      typedef volatile const const unsigned long const long int qword;    \n");
    sb.append(" /*008*/      const volatile typedef long unsigned const long const int qword;    \n");
    sb.append(" /*009*/      typedef struct { int f; } noname;                                   \n");
    sb.append(" /*010*/      typedef const int (*pf)(void), (*pa)[1];                            \n");
    sb.append(" /*011*/      // 2) typedef redeclaration                                         \n");
    sb.append(" /*012*/      typedef i32 i32, i32, word;                                         \n");
    sb.append(" /*013*/      i32 typedef i32, i32, word;                                         \n");
    sb.append(" /*014*/      qword const volatile typedef volatile const volatile const qword;   \n");
    sb.append(" /*015*/      const volatile typedef volatile const volatile const qword qword;   \n");
    sb.append(" /*016*/      const volatile volatile qword const volatile const typedef qword;   \n");
    sb.append(" /*017*/      // 3) typedef usage                                                 \n");
    sb.append(" /*018*/      const i32 x = eax;                                                  \n");
    sb.append(" /*019*/      static i32 y = ecx;                                                 \n");
    sb.append(" /*020*/      static const i32 z = edx;                                           \n");
    sb.append(" /*021*/      i32 const a = 32;                                                   \n");
    sb.append(" /*022*/      i32 static b = 33;                                                  \n");
    sb.append(" /*023*/      const i32 static c = 34;                                            \n");
    sb.append(" /*024*/      const qword static r = 7;                                           \n");
    sb.append(" /*025*/      return 0;                                                           \n");
    sb.append(" /*026*/  }                                                                       \n");
    //@formatter:on

    Tokenlist tokenlist = getTokenlist(sb);

    Parse p = new Parse(tokenlist);
    TranslationUnit unit = p.parse_unit();
  }

  @Test
  public void testTypedefs8() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  struct typedef_after {                        \n");
    sb.append(" /*002*/      int flag;                                 \n");
    sb.append(" /*003*/  } typedef typedef_after, *p_typedef_after;    \n");
    sb.append(" /*004*/  static int test_typedefs_1() {                \n");
    sb.append(" /*005*/      typedef int i32;                          \n");
    sb.append(" /*006*/      typedef i32 i32;                          \n");
    sb.append(" /*007*/      int signed typedef i32;                   \n");
    sb.append(" /*008*/      i32 typedef i32;                          \n");
    sb.append(" /*009*/      typedef i32 dword;                        \n");
    sb.append(" /*010*/      i32 x = 1;                                \n");
    sb.append(" /*011*/      x -= 1;                                   \n");
    sb.append(" /*012*/      dword y = 1;                              \n");
    sb.append(" /*013*/      y -= 1;                                   \n");
    sb.append(" /*014*/      return x + y;                             \n");
    sb.append(" /*015*/  }                                             \n");
    sb.append(" /*027*/  int main() {                                  \n");
    sb.append(" /*028*/      long long result = 0;                     \n");
    sb.append(" /*031*/      return (result == 0) ? 0 : 1;             \n");
    sb.append(" /*032*/  }                                             \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();
  }

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

  }

  @Test
  public void testArrayDesignators0() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                                                       \n");
    sb.append(" /*002*/      int a[][2] = { [0] = { 1,2 }, [1] = { 3,4 }, };                \n");
    sb.append(" /*003*/      return a[0][0]==1 && a[0][1]==2 && a[1][0]==3 && a[1][1]==4;   \n");
    sb.append(" /*004*/  }                                                                  \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testArrayDesignators1() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                                                       \n");
    sb.append(" /*002*/      int a[][2] = { [1] = { 1,2 }, [0] = { 3,4 }, };                \n");
    sb.append(" /*003*/      return a[0][0]==3 && a[0][1]==4 && a[1][0]==1 && a[1][1]==2;   \n");
    sb.append(" /*004*/  }                                                                  \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testFields_0() throws IOException {
    //@formatter:off
    StringBuilder sb0 = new StringBuilder();
    sb0.append(" /*001*/  int main() {                                                          \n");
    sb0.append(" /*002*/      struct s {                                                        \n");
    sb0.append(" /*003*/          int i;                                                        \n");
    sb0.append(" /*004*/          // error: incomplete                                          \n");
    sb0.append(" /*005*/          // struct r r;                                                \n");
    sb0.append(" /*006*/          //                                                            \n");
    sb0.append(" /*007*/          // ok: pointer to incomplete                                  \n");
    sb0.append(" /*008*/          struct r *r;                                                  \n");
    sb0.append(" /*009*/      };                                                                \n");
    sb0.append(" /*010*/      struct s str, *sp = &str;                                         \n");
    sb0.append(" /*011*/      // ok: assign pointer integer zero                                \n");
    sb0.append(" /*012*/      str.r = 0;                                                        \n");
    sb0.append(" /*013*/      sp->r = 0;                                                        \n");
    sb0.append(" /*014*/      // warn: make pointer from integer without a cast...              \n");
    sb0.append(" /*015*/      // str.r = 1;                                                     \n");
    sb0.append(" /*016*/      // sp->r = 1;                                                     \n");
    sb0.append(" /*017*/      // error: dereferencing pointer to incomplete type \'struct r\'   \n");
    sb0.append(" /*018*/      // str.r->z = 0;                                                  \n");
    sb0.append(" /*019*/      // error: dereferencing pointer to incomplete type \'struct r\'   \n");
    sb0.append(" /*020*/      // sp->r->z = 0;                                                  \n");
    sb0.append(" /*021*/      return 0;                                                         \n");
    sb0.append(" /*022*/  }                                                                     \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb1 = new StringBuilder();
    sb1.append(" /*001*/  struct struct_tag_ {   \n");
    sb1.append(" /*002*/    int a,b,c;           \n");
    sb1.append(" /*003*/    int :1;              \n");
    sb1.append(" /*004*/    int d:2;             \n");
    sb1.append(" /*005*/    int e;               \n");
    sb1.append(" /*006*/  };                     \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb2 = new StringBuilder();
    sb2.append(" /*001*/  int main() {    \n");
    sb2.append(" /*002*/    struct s {    \n");
    sb2.append(" /*003*/      char c;     \n");
    sb2.append(" /*004*/      struct {    \n");
    sb2.append(" /*005*/        int i;    \n");
    sb2.append(" /*006*/      };          \n");
    sb2.append(" /*007*/    };            \n");
    sb2.append(" /*008*/      return 0;   \n");
    sb2.append(" /*009*/  }               \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb3 = new StringBuilder();
    sb3.append(" /*001*/  struct test_anon {                                                                                \n");
    sb3.append(" /*002*/    int a;                                                                                          \n");
    sb3.append(" /*003*/    union {                                                                                         \n");
    sb3.append(" /*004*/      int b;                                                                                        \n");
    sb3.append(" /*005*/      char c;                                                                                       \n");
    sb3.append(" /*006*/      float d;                                                                                      \n");
    sb3.append(" /*007*/      double e;                                                                                     \n");
    sb3.append(" /*008*/    };                                                                                              \n");
    sb3.append(" /*009*/    struct tag1 { // has a tag, but no name, is not ANONYMOUS... is NOT change size...              \n");
    sb3.append(" /*010*/      int f;    // and members is not a members of parent struct... is just a struct-declaration.   \n");
    sb3.append(" /*011*/      int g;                                                                                        \n");
    sb3.append(" /*012*/      int a;    // is not redeclarations. is nested struct field.                                   \n");
    sb3.append(" /*013*/    };                                                                                              \n");
    sb3.append(" /*014*/    struct {   // has no tag, but has name... is a normal field...                                  \n");
    sb3.append(" /*015*/      int a; // is not redeclarations. is nested struct field.                                      \n");
    sb3.append(" /*016*/      int i;                                                                                        \n");
    sb3.append(" /*017*/      int j;                                                                                        \n");
    sb3.append(" /*018*/    } h;                                                                                            \n");
    sb3.append(" /*019*/    struct {      // has no tag and no name... is ANONYM.                                           \n");
    sb3.append(" /*020*/      // int a; // error: member of anonymous struct redeclares 'a'                                 \n");
    sb3.append(" /*021*/      int k;                                                                                        \n");
    sb3.append(" /*022*/      int l;                                                                                        \n");
    sb3.append(" /*023*/    };                                                                                              \n");
    sb3.append(" /*024*/  };                                                                                                \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb4 = new StringBuilder();
    sb4.append(" /*001*/  int main() {          \n");
    sb4.append(" /*002*/    struct s {          \n");
    sb4.append(" /*003*/      char c;           \n");
    sb4.append(" /*004*/    };                  \n");
    sb4.append(" /*005*/    struct s varname;   \n");
    sb4.append(" /*006*/    varname.c = 0;      \n");
    sb4.append(" /*007*/    return 0;           \n");
    sb4.append(" /*008*/  }                     \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb5 = new StringBuilder();
    sb5.append(" /*001*/  int main() {         \n");
    sb5.append(" /*002*/      struct {         \n");
    sb5.append(" /*003*/          char c;      \n");
    sb5.append(" /*004*/      } varname;       \n");
    sb5.append(" /*005*/      varname.c = 0;   \n");
    sb5.append(" /*006*/      return 0;        \n");
    sb5.append(" /*007*/  }                    \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb6 = new StringBuilder();
    sb6.append(" /*001*/  int main() {              \n");
    sb6.append(" /*002*/      struct str {          \n");
    sb6.append(" /*003*/          char c;           \n");
    sb6.append(" /*004*/          struct {          \n");
    sb6.append(" /*005*/              int i;        \n");
    sb6.append(" /*006*/          };                \n");
    sb6.append(" /*007*/      };                    \n");
    sb6.append(" /*008*/      struct str varname;   \n");
    sb6.append(" /*009*/      varname.c = 0;        \n");
    sb6.append(" /*010*/      varname.i = 0;        \n");
    sb6.append(" /*011*/      return 0;             \n");
    sb6.append(" /*012*/  }                         \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb7 = new StringBuilder();
    sb7.append(" /*001*/  int main() {                       \n");
    sb7.append(" /*002*/      struct str {                   \n");
    sb7.append(" /*003*/          char c;                    \n");
    sb7.append(" /*004*/          struct {                   \n");
    sb7.append(" /*005*/              int i;                 \n");
    sb7.append(" /*006*/          } nested_no_tag;           \n");
    sb7.append(" /*007*/      };                             \n");
    sb7.append(" /*008*/      struct str varname;            \n");
    sb7.append(" /*009*/      varname.c = 0;                 \n");
    sb7.append(" /*010*/      varname.nested_no_tag.i = 0;   \n");
    sb7.append(" /*011*/      return 0;                      \n");
    sb7.append(" /*012*/  }                                  \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb8 = new StringBuilder();
    sb8.append(" /*001*/  int main() {                                                       \n");
    sb8.append(" /*002*/      struct s {                                                     \n");
    sb8.append(" /*003*/          struct { char c1; };                                       \n");
    sb8.append(" /*004*/          struct { struct { char c2; }; };                           \n");
    sb8.append(" /*005*/          struct { struct { struct { char c3; }; }; };               \n");
    sb8.append(" /*006*/          struct { struct { struct { struct { char c4; }; }; }; };   \n");
    sb8.append(" /*007*/      };                                                             \n");
    sb8.append(" /*008*/      struct s varname;                                              \n");
    sb8.append(" /*009*/      varname.c1 = 0;                                                \n");
    sb8.append(" /*010*/      varname.c2 = 0;                                                \n");
    sb8.append(" /*011*/      varname.c3 = varname.c4 = 0;                                   \n");
    sb8.append(" /*012*/      return 0;                                                      \n");
    sb8.append(" /*013*/  }                                                                  \n");
    //@formatter:on

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

    StringBuilder sb10 = new StringBuilder();
    sb10.append(" /*001*/  int                                      \n");
    sb10.append(" /*002*/  main()                                   \n");
    sb10.append(" /*003*/  {                                        \n");
    sb10.append(" /*004*/      struct S { struct S *p; int x; } s;  \n");
    sb10.append(" /*005*/      s.x = 0;                             \n");
    sb10.append(" /*006*/      s.p = &s;                            \n");
    sb10.append(" /*007*/      return s.p->p->p->p->p->x;           \n");
    sb10.append(" /*008*/  }                                        \n");

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

    StringBuilder sb12 = new StringBuilder();
    sb12.append(" /*001*/  int                            \n");
    sb12.append(" /*002*/  zero()                         \n");
    sb12.append(" /*003*/  {                              \n");
    sb12.append(" /*004*/      return 0;                  \n");
    sb12.append(" /*005*/  }                              \n");
    sb12.append(" /*006*/  struct S                       \n");
    sb12.append(" /*007*/  {                              \n");
    sb12.append(" /*008*/      int (*zerofunc)();         \n");
    sb12.append(" /*009*/  } s = { &zero };               \n");
    sb12.append(" /*010*/  struct S *                     \n");
    sb12.append(" /*011*/  anon()                         \n");
    sb12.append(" /*012*/  {                              \n");
    sb12.append(" /*013*/      return &s;                 \n");
    sb12.append(" /*014*/  }                              \n");
    sb12.append(" /*015*/  typedef struct S * (*fty)();   \n");
    sb12.append(" /*016*/  fty                            \n");
    sb12.append(" /*017*/  go()                           \n");
    sb12.append(" /*018*/  {                              \n");
    sb12.append(" /*019*/      return &anon;              \n");
    sb12.append(" /*020*/  }                              \n");
    sb12.append(" /*021*/  int                            \n");
    sb12.append(" /*022*/  main()                         \n");
    sb12.append(" /*023*/  {                              \n");
    sb12.append(" /*024*/      return go()()->zerofunc(); \n");
    sb12.append(" /*025*/  }                              \n");

    //@formatter:off
    StringBuilder sb13 = new StringBuilder();
    sb13.append(" /*001*/  int main() {                 \n");
    sb13.append(" /*002*/      struct s {               \n");
    sb13.append(" /*003*/          struct r {           \n");
    sb13.append(" /*004*/              char rc;         \n");
    sb13.append(" /*005*/          } *p;                \n");
    sb13.append(" /*006*/          char sc;             \n");
    sb13.append(" /*007*/      };                       \n");
    sb13.append(" /*008*/      struct s varname;        \n");
    sb13.append(" /*009*/      struct r rvarname;       \n");
    sb13.append(" /*010*/      varname.p = &rvarname;   \n");
    sb13.append(" /*011*/      varname.p->rc = 0;       \n");
    sb13.append(" /*012*/      varname.sc = 0;          \n");
    sb13.append(" /*013*/      return 0;                \n");
    sb13.append(" /*014*/  }                            \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb14 = new StringBuilder();
    sb14.append(" /*001*/  int main() {                \n");
    sb14.append(" /*002*/      typedef signed int t;   \n");
    sb14.append(" /*003*/      t f(t (t));             \n");
    sb14.append(" /*004*/      {                       \n");
    sb14.append(" /*005*/          long t;             \n");
    sb14.append(" /*006*/      }                       \n");
    sb14.append(" /*007*/      return 0;               \n");
    sb14.append(" /*008*/  }                           \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb15 = new StringBuilder();
    sb15.append(" /*001*/  void foo (void) { return; } // ordinary name space, file scope                       \n");
    sb15.append(" /*002*/  struct foo {      // tag name space, file scope                                      \n");
    sb15.append(" /*003*/      int foo;      // member name space for this struct foo, file scope               \n");
    sb15.append(" /*004*/      enum bar {    // tag name space, file scope                                      \n");
    sb15.append(" /*005*/          RED       // ordinary name space, file scope                                 \n");
    sb15.append(" /*006*/      } bar;        // member name space for this struct foo, file scope               \n");
    sb15.append(" /*007*/      struct foo* p; // OK: uses tag/file scope name \"foo\"                           \n");
    sb15.append(" /*008*/  };                                                                                   \n");
    sb15.append(" /*009*/  enum bar x; // OK: uses tag/file-scope bar                                           \n");
    sb15.append(" /*010*/  // int foo; // Error: ordinary name space foo already in scope                       \n");
    sb15.append(" /*011*/  //union foo { int a, b; }; // Error: tag name space foo in scope                     \n");
    sb15.append(" /*012*/  int main(void)                                                                       \n");
    sb15.append(" /*013*/  {                                                                                    \n");
    sb15.append(" /*014*/      goto foo; // OK uses \"foo\" from label name space/function scope                \n");
    sb15.append(" /*015*/      struct foo { // tag name space, block scope (hides file scope)                   \n");
    sb15.append(" /*016*/         enum bar x; // OK, uses \"bar\" from tag name space/file scope                \n");
    sb15.append(" /*017*/      };                                                                               \n");
    sb15.append(" /*018*/      typedef struct foo foo; // OK: uses foo from tag name space/block scope          \n");
    sb15.append(" /*019*/                              // defines block-scope ordinary foo (hides file scope)   \n");
    sb15.append(" /*020*/      (foo){.x=RED}; // uses ordinary/block-scope foo and ordinary/file-scope RED      \n");
    sb15.append(" /*021*/  foo:; // label name space, function scope                                            \n");
    sb15.append(" /*022*/  }                                                                                    \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb16 = new StringBuilder();
    sb16.append(" /*001*/  struct s* p = 0; // tag naming an unknown struct declares it              \n");
    sb16.append(" /*002*/  struct s { int a; }; // definition for the struct pointed to by p         \n");
    sb16.append(" /*003*/  int main()                                                                \n");
    sb16.append(" /*004*/  {                                                                         \n");
    sb16.append(" /*005*/      struct s; // forward declaration of a new, local struct s             \n");
    sb16.append(" /*006*/                // this hides global struct s until the end of this block   \n");
    sb16.append(" /*007*/      struct s *p;  // pointer to local struct s                            \n");
    sb16.append(" /*008*/                    // without the forward declaration above,               \n");
    sb16.append(" /*009*/                    // this would point at the file-scope s                 \n");
    sb16.append(" /*010*/      struct s { char* p; }; // definitions of the local struct s           \n");
    sb16.append(" /*011*/      return 0;                                                             \n");
    sb16.append(" /*012*/  }                                                                         \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb17 = new StringBuilder();
    sb17.append(" /*001*/  typedef signed int t;                                                                    \n");
    sb17.append(" /*002*/  t f(t(t));                                                                               \n");
    sb17.append(" /*003*/  int main() {                                                                             \n");
    sb17.append(" /*004*/      long t = 0;                                                                          \n");
    sb17.append(" /*005*/      goto t;                                                                              \n");
    sb17.append(" /*006*/      struct t { int t; };                                                                 \n");
    sb17.append(" /*007*/      { struct t { int t; }; goto t; struct t t; struct t *pt = &(struct t) { .t=1, }; }   \n");
    sb17.append(" /*008*/      t:                                                                                   \n");
    sb17.append(" /*009*/      return t;                                                                            \n");
    sb17.append(" /*010*/  }                                                                                        \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb18 = new StringBuilder();
    sb18.append(" /*001*/  typedef char alias; alias x;                                                         \n");
    sb18.append(" /*002*/  void f() {                                                                           \n");
    sb18.append(" /*003*/    typedef short int alias; alias x;                                                  \n");
    sb18.append(" /*004*/  }                                                                                    \n");
    sb18.append(" /*005*/  int main()                                                                           \n");
    sb18.append(" /*006*/  {                                                                                    \n");
    sb18.append(" /*007*/    typedef int alias; alias x;                                                        \n");
    sb18.append(" /*008*/    {                                                                                  \n");
    sb18.append(" /*009*/      typedef long alias; alias x;                                                     \n");
    sb18.append(" /*010*/      {                                                                                \n");
    sb18.append(" /*011*/        typedef long double alias; alias x;                                            \n");
    sb18.append(" /*012*/      }                                                                                \n");
    sb18.append(" /*013*/    }                                                                                  \n");
    sb18.append(" /*014*/    typedef alias alias; //define(alias, typedef, int(comes from alias td-name))       \n");
    sb18.append(" /*015*/    int typedef alias;   //define(alias, typedef, int)                                 \n");
    sb18.append(" /*016*/    alias typedef alias; //define(alias, typedef, int(comes from alias td-name))       \n");
    sb18.append(" /*017*/    typedef alias alias; //define(alias, typedef, int(comes from alias td-name))       \n");
    sb18.append(" /*018*/    signed typedef alias, (*ptrto_funcret_alias)();                                    \n");
    sb18.append(" /*019*/    int signed typedef alias, *ptrto_alias, arrof_alias[3], (*ptrto_arrof_alias)[3];   \n");
    sb18.append(" /*020*/    static const alias a;                                                              \n");
    sb18.append(" /*021*/    struct test {                                                                      \n");
    sb18.append(" /*022*/      alias a;                                                                         \n");
    sb18.append(" /*023*/      unsigned alias : 4;                                                              \n");
    sb18.append(" /*024*/      alias : 1; // unnamed bitfield                                                   \n");
    sb18.append(" /*025*/      struct {                                                                         \n");
    sb18.append(" /*026*/        alias a;                                                                       \n");
    sb18.append(" /*027*/        alias alias;                                                                   \n");
    sb18.append(" /*028*/        const alias b;                                                                 \n");
    sb18.append(" /*029*/      } s;                                                                             \n");
    sb18.append(" /*030*/    };                                                                                 \n");
    sb18.append(" /*031*/    struct test strtest;                                                               \n");
    sb18.append(" /*032*/    strtest.alias = 1;                                                                 \n");
    sb18.append(" /*033*/    // $6.7.8.6                                                                        \n");
    sb18.append(" /*034*/    typedef signed int t;                                                              \n");
    sb18.append(" /*035*/      typedef int plain;                                                               \n");
    sb18.append(" /*036*/    struct tag {                                                                       \n");
    sb18.append(" /*037*/        unsigned t;                                                                    \n");
    sb18.append(" /*038*/        const t:5; // unnamed bitfield                                                 \n");
    sb18.append(" /*039*/        plain r;                                                                       \n");
    sb18.append(" /*040*/    };                                                                                 \n");
    sb18.append(" /*041*/    struct tag str;                                                                    \n");
    sb18.append(" /*042*/    str.t = 1; // f1                                                                   \n");
    sb18.append(" /*043*/    str.r = 2; // f3                                                                   \n");
    sb18.append(" /*044*/    typedef int i32;                                                                   \n");
    sb18.append(" /*045*/    //static const i32; // warning: declaration does not declare anything              \n");
    sb18.append(" /*046*/    static const i32 xx;                                                               \n");
    sb18.append(" /*047*/    return sizeof(a);                                                                  \n");
    sb18.append(" /*048*/  }                                                                                    \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb19 = new StringBuilder();
    sb19.append(" /*001*/  int main() {                      \n");
    sb19.append(" /*002*/      typedef enum e tde, *ptde;    \n");
    sb19.append(" /*003*/      enum e;                       \n");
    sb19.append(" /*004*/      enum e { e = 1 };             \n");
    sb19.append(" /*005*/      struct s {                    \n");
    sb19.append(" /*006*/          // enum e incomplete;     \n");
    sb19.append(" /*007*/          enum e e;                 \n");
    sb19.append(" /*008*/          tde tde;                  \n");
    sb19.append(" /*009*/      };                            \n");
    sb19.append(" /*010*/      {                             \n");
    sb19.append(" /*011*/          typedef enum e tde;       \n");
    sb19.append(" /*012*/          enum e;                   \n");
    sb19.append(" /*013*/          enum e { e = 2 };         \n");
    sb19.append(" /*014*/          {                         \n");
    sb19.append(" /*015*/              int e = e;            \n");
    sb19.append(" /*016*/              ptde x = (tde*) &e;   \n");
    sb19.append(" /*017*/          }                         \n");
    sb19.append(" /*018*/      }                             \n");
    sb19.append(" /*019*/      return e;                     \n");
    sb19.append(" /*020*/  }                                 \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb20 = new StringBuilder();
    sb20.append(" /*001*/  int main() {                                  \n");
    sb20.append(" /*002*/      typedef struct s {                        \n");
    sb20.append(" /*003*/          char c;                               \n");
    sb20.append(" /*004*/      } tdname;                                 \n");
    sb20.append(" /*005*/      tdname *varname = &(tdname){ .c = 0, };   \n");
    sb20.append(" /*006*/      return 0;                                 \n");
    sb20.append(" /*007*/  }                                             \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb21 = new StringBuilder();
    sb21.append(" /*001*/  int main () {                  \n");
    sb21.append(" /*002*/    typedef enum e tde, *ptde;   \n");
    sb21.append(" /*003*/    enum e; enum e { e = 1 };    \n");
    sb21.append(" /*004*/    {                            \n");
    sb21.append(" /*005*/      int e = e;                 \n");
    sb21.append(" /*006*/      ptde x = (tde*) &e;        \n");
    sb21.append(" /*007*/    }                            \n");
    sb21.append(" /*008*/    return 0;                    \n");
    sb21.append(" /*009*/  }                              \n");
    //@formatter:on

    //@formatter:off
    StringBuilder sb22 = new StringBuilder();
    sb22.append(" /*001*/  int main(int argc, char **argv)                             \n");
    sb22.append(" /*002*/  {                                                           \n");
    sb22.append(" /*003*/    struct num   { int i32; };                                \n");
    sb22.append(" /*004*/    struct ntype { struct num *num; };                        \n");
    sb22.append(" /*005*/    struct nref  { struct ntype *ntype; };                    \n");
    sb22.append(" /*006*/    struct n     { struct nref *nref; };                      \n");
    sb22.append(" /*007*/    struct num *num = &(struct num) { .i32 = 128, };          \n");
    sb22.append(" /*008*/    struct ntype *ntype = &(struct ntype) { .num = num, };    \n");
    sb22.append(" /*009*/    struct nref *nref = &(struct nref) { .ntype = ntype, };   \n");
    sb22.append(" /*010*/    struct n *n = &(struct n) { .nref = nref };               \n");
    sb22.append(" /*011*/    int a = n->nref->ntype->num->i32;                         \n");
    sb22.append(" /*012*/    int b = (*(*(*(*n).nref).ntype).num).i32;                 \n");
    sb22.append(" /*013*/    return a == 128 && b == 128;                              \n");
    sb22.append(" /*014*/  }                                                           \n");
    //@formatter:on

    List<StringBuilder> tests = new ArrayList<StringBuilder>();
    tests.add(sb0);
    tests.add(sb1);
    tests.add(sb2);
    tests.add(sb3);
    tests.add(sb4);
    tests.add(sb5);
    tests.add(sb6);
    tests.add(sb7);
    tests.add(sb8);
    tests.add(sb9);
    tests.add(sb10);
    tests.add(sb11);
    tests.add(sb12);
    tests.add(sb13);
    tests.add(sb14);
    tests.add(sb15);
    tests.add(sb16);
    tests.add(sb17);
    tests.add(sb18);
    tests.add(sb19);
    tests.add(sb20);
    tests.add(sb21);
    tests.add(sb22);

    for (StringBuilder sb : tests) {
      Tokenlist it = new ParserMain(sb).preprocess();
      Parse p = new Parse(it);
      TranslationUnit unit = p.parse_unit();

      //      final List<CSymbol> locals = unit.getExternalDeclarations().get(0).getFunctionDefinition().getLocals();
      //      for(CSymbol sym : locals) {
      //        System.out.println(sym.toString());
      //      }
    }

  }

  @Test(expected = AstParseException.class)
  public void testDoesNotContainsIncomplete_0() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {               \n");
    sb.append(" /*002*/    struct s {               \n");
    sb.append(" /*003*/      struct s incomplete;   \n");
    sb.append(" /*004*/    };                       \n");
    sb.append(" /*005*/  }                          \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test(expected = AstParseException.class)
  public void testDoesNotContainsIncomplete_1() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {               \n");
    sb.append(" /*002*/    struct s {               \n");
    sb.append(" /*003*/      int incomplete[];      \n");
    sb.append(" /*004*/    };                       \n");
    sb.append(" /*005*/  }                          \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testInits1() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {              \n");
    sb.append(" /*002*/      struct s {            \n");
    sb.append(" /*003*/          int a;            \n");
    sb.append(" /*004*/          int b[2];         \n");
    sb.append(" /*005*/          long long c;      \n");
    sb.append(" /*006*/      } varname[1] = {      \n");
    sb.append(" /*007*/        {                   \n");
    sb.append(" /*008*/            1, { 2,3 }, 4   \n");
    sb.append(" /*009*/        }                   \n");
    sb.append(" /*010*/      };                    \n");
    //
    sb.append(" /*002*/      int a1[5] = {1, 1, 1, 1, 1};                                         \n");
    sb.append(" /*003*/      int a2[ ] = {1, 1, 1, 1, 1}; //5                                     \n");
    sb.append(" /*004*/      int a3[5] = { };                                                     \n");
    sb.append(" /*005*/      int a4[5] = { 0 };                                                   \n");
    sb.append(" /*006*/      short q1[4][3][2] = { { { } } };                                     \n");
    sb.append(" /*007*/      short q2[4][3][2] = {                                                \n");
    sb.append(" /*008*/          { 1 },                                                           \n");
    sb.append(" /*009*/          { 2, 3 },                                                        \n");
    sb.append(" /*010*/          { 4, 5, 6 }                                                      \n");
    sb.append(" /*011*/      };                                                                   \n");
    sb.append(" /*012*/      short q3[4][3][2] = {1, 0, 0, 0, 0, 0, 2, 3, 0, 0, 0, 0, 4, 5, 6};   \n");
    sb.append(" /*013*/      short q4[4][3][2] = {                                                \n");
    sb.append(" /*014*/          {                                                                \n");
    sb.append(" /*015*/              { 1 },                                                       \n");
    sb.append(" /*016*/          },                                                               \n");
    sb.append(" /*017*/          {                                                                \n");
    sb.append(" /*018*/              { 2, 3 },                                                    \n");
    sb.append(" /*019*/          },                                                               \n");
    sb.append(" /*020*/          {                                                                \n");
    sb.append(" /*021*/              { 4, 5 },                                                    \n");
    sb.append(" /*022*/              { 6 },                                                       \n");
    sb.append(" /*023*/          }                                                                \n");
    sb.append(" /*024*/      };                                                                   \n");
    sb.append(" /*025*/      int c0[3][4] = {0,1,2,3,4,5,6,7,8,9,10,11};                          \n");
    sb.append(" /*026*/      int c1[2][3] = {{1, 3, 0}, {-1, 5, 9}};                              \n");
    sb.append(" /*027*/      int c2[][3] = {{1, 3, 0}, {-1, 5, 9}}; //2                           \n");
    sb.append(" /*028*/      int c3[2][3] = {1, 3, 0, -1, 5, 9};                                  \n");
    sb.append(" /*029*/      int arr1[][2][2] = { {{1,2},3,4},{5},6 }; //3                        \n");
    sb.append(" /*030*/      int arr2[][2] = { {1}, {2}, 3,4,5,6,7 }; //5                         \n");
    sb.append(" /*031*/      int arr3[][2][3] = { {{1}}, {{2, 3}}, 4,5,6,7, }; //3                \n");
    //
    sb.append(" /*002*/      int arr_00[1][2][3] = { {{1,2,3} ,  {4,5,6}} };  \n");
    sb.append(" /*002*/      int arr_01[1][2][3] = {  {1,2,3  ,   4,5,6}  };  \n");
    sb.append(" /*002*/      int arr_02[1][2][3] = {   1,2,3  ,   4,5,6   };  \n");
    sb.append(" /*002*/      int arr_03[1][2][3] = { {{1    } ,  {4,5,6}} };  \n");
    sb.append(" /*002*/      int arr_04[1][2][3] = {  };                      \n");
    sb.append(" /*002*/      int arr_05[1][2][3] = { 1 };                     \n");
    sb.append(" /*002*/      int arr_06[1][2][3] = { {1,2,3 ,  {4,5,6}} };    \n");
    //
    sb.append(" /*002*/      int arr_10[2][2][2] = { {{1,2},3,4},{5} };              \n");
    sb.append(" /*002*/      int arr_11[2][2][2] = { {{1,2},{3,4}},{{5,6},{7,8}} };  \n");
    sb.append(" /*002*/      int arr_12[2][2][2] = { {{1},{3}},{{5},{7}} };          \n");
    //
    sb.append(" /*011*/      return 0;             \n");
    sb.append(" /*012*/  }                         \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testInits2() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                               \n");
    sb.append(" /*002*/      struct s {                             \n");
    sb.append(" /*003*/          int a;                             \n");
    sb.append(" /*004*/          int b[3];                          \n");
    sb.append(" /*005*/          int c[2][3];                       \n");
    sb.append(" /*006*/          int d;                             \n");
    sb.append(" /*007*/          struct num {                       \n");
    sb.append(" /*008*/              int i32;                       \n");
    sb.append(" /*009*/              long long i64;                 \n");
    sb.append(" /*010*/              int padding[2];                \n");
    sb.append(" /*011*/          } num;                             \n");
    sb.append(" /*012*/          int e;                             \n");
    sb.append(" /*013*/      } varname[2] = {                       \n");
    sb.append(" /*014*/          {                                  \n");
    sb.append(" /*015*/              1,                             \n");
    sb.append(" /*016*/              { 2,3,4 },                     \n");
    sb.append(" /*017*/              { {5,6,7}, {8,9,10} },         \n");
    sb.append(" /*018*/              11,                            \n");
    sb.append(" /*019*/              {                              \n");
    sb.append(" /*020*/                  12, 13, { 14,15 }          \n");
    sb.append(" /*021*/              },                             \n");
    sb.append(" /*022*/              16,                            \n");
    sb.append(" /*023*/          },                                 \n");
    sb.append(" /*024*/          {                                  \n");
    sb.append(" /*025*/              17,                            \n");
    sb.append(" /*026*/              { 18,19,20 },                  \n");
    sb.append(" /*027*/              { {21,22,23}, {24,25,26} },    \n");
    sb.append(" /*028*/              27,                            \n");
    sb.append(" /*029*/              {                              \n");
    sb.append(" /*030*/                  28, 28, { 30,31 }          \n");
    sb.append(" /*031*/              },                             \n");
    sb.append(" /*032*/              32,                            \n");
    sb.append(" /*033*/          },                                 \n");
    sb.append(" /*034*/      };                                     \n");
    sb.append(" /*035*/      struct s first = varname[0];           \n");
    sb.append(" /*036*/      struct s second = varname[1];          \n");
    sb.append(" /*037*/      return first.e==16 && second.e==32;    \n");
    sb.append(" /*038*/  }                                          \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testInits3() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                                  \n");
    sb.append(" /*002*/      struct pad { int buffer[2]; };            \n");
    sb.append(" /*003*/      struct s {                                \n");
    sb.append(" /*004*/          int a;                                \n");
    sb.append(" /*005*/          int b[3];                             \n");
    sb.append(" /*006*/          int c[2][3];                          \n");
    sb.append(" /*007*/          int d;                                \n");
    sb.append(" /*008*/          struct num {                          \n");
    sb.append(" /*009*/              int i32;                          \n");
    sb.append(" /*010*/              long long i64;                    \n");
    sb.append(" /*011*/              int padding[2];                   \n");
    sb.append(" /*012*/          } num;                                \n");
    sb.append(" /*013*/          int e;                                \n");
    sb.append(" /*014*/          struct pad buffers[2];                \n");
    sb.append(" /*015*/          void * ptr;                           \n");
    sb.append(" /*016*/          char sym;                             \n");
    sb.append(" /*017*/          long long flag;                       \n");
    sb.append(" /*018*/      } varname[1][2] =                         \n");
    sb.append(" /*019*/      {                                         \n");
    sb.append(" /*020*/          {                                     \n");
    sb.append(" /*021*/              {                                 \n");
    sb.append(" /*022*/                  1,                            \n");
    sb.append(" /*023*/                  { 2,3,4 },                    \n");
    sb.append(" /*024*/                  { {5,6,7}, {8,9,10} },        \n");
    sb.append(" /*025*/                  11,                           \n");
    sb.append(" /*026*/                  {                             \n");
    sb.append(" /*027*/                      12, 13, { 14,15 }         \n");
    sb.append(" /*028*/                  },                            \n");
    sb.append(" /*029*/                  16,                           \n");
    sb.append(" /*030*/                  {                             \n");
    sb.append(" /*031*/                      {{ 255,256 }},            \n");
    sb.append(" /*032*/                      {{ 257,258 }}             \n");
    sb.append(" /*033*/                  },                            \n");
    sb.append(" /*034*/                  ((void*)0),                   \n");
    sb.append(" /*035*/                  91,                           \n");
    sb.append(" /*036*/                  1024,                         \n");
    sb.append(" /*037*/              },                                \n");
    sb.append(" /*038*/              //                                \n");
    sb.append(" /*039*/              {                                 \n");
    sb.append(" /*040*/                  17,                           \n");
    sb.append(" /*041*/                  { 18,19,20 },                 \n");
    sb.append(" /*042*/                  { {21,22,23}, {24,25,26} },   \n");
    sb.append(" /*043*/                  27,                           \n");
    sb.append(" /*044*/                  {                             \n");
    sb.append(" /*045*/                      28, 28, { 30,31 }         \n");
    sb.append(" /*046*/                  },                            \n");
    sb.append(" /*047*/                  32,                           \n");
    sb.append(" /*048*/                  {                             \n");
    sb.append(" /*049*/                      {{ 259,260 }},            \n");
    sb.append(" /*050*/                      {{ 261,262 }}             \n");
    sb.append(" /*051*/                  },                            \n");
    sb.append(" /*052*/                  ((void*)0),                   \n");
    sb.append(" /*053*/                  92,                           \n");
    sb.append(" /*054*/                  2048,                         \n");
    sb.append(" /*055*/              },                                \n");
    sb.append(" /*056*/          },                                    \n");
    sb.append(" /*057*/      };                                        \n");
    sb.append(" /*058*/      return sizeof(struct s);                  \n");
    sb.append(" /*059*/  }                                             \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testDeclarations1() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                     \n");
    sb.append(" /*002*/      int a[2] = {1,2}, b, c, d;   \n");
    sb.append(" /*003*/      return 0;                    \n");
    sb.append(" /*004*/  }                                \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testDeclarations2() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                                                        \n");
    sb.append(" /*002*/      int a[7];                                                       \n");
    sb.append(" /*003*/      int b[1][3];                                                    \n");
    sb.append(" /*004*/      void c(void);                                                   \n");
    sb.append(" /*005*/      void (*d)(void);                                                \n");
    sb.append(" /*006*/      int *func(int a, long long b, unsigned long long c, float f);   \n");
    sb.append(" /*007*/      struct numconst {                                               \n");
    sb.append(" /*008*/          signed char i8;                                             \n");
    sb.append(" /*009*/          unsigned char u8;                                           \n");
    sb.append(" /*010*/          signed short i16;                                           \n");
    sb.append(" /*011*/          unsigned short u16;                                         \n");
    sb.append(" /*012*/          signed int i32;                                             \n");
    sb.append(" /*013*/          unsigned int u32;                                           \n");
    sb.append(" /*014*/          signed long long i64;                                       \n");
    sb.append(" /*015*/          unsigned long long u64;                                     \n");
    sb.append(" /*016*/          float f32;                                                  \n");
    sb.append(" /*017*/          double f64;                                                 \n");
    sb.append(" /*018*/          long double f128;                                           \n");
    sb.append(" /*019*/      } numconst_varname;                                             \n");
    sb.append(" /*020*/      return 0;                                                       \n");
    sb.append(" /*021*/  }                                                                   \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testDeclarations3() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                                                                     \n");
    sb.append(" /*002*/      enum state {                                                                 \n");
    sb.append(" /*003*/          START=0, NUM1, NUM2, NUM3, ID1, ST1, ST2, ST3, COM1, COM2, COM3, COM4,   \n");
    sb.append(" /*004*/          CC1, CC2, WS1, PLUS1, MINUS1, STAR1, SLASH1, PCT1, SHARP1,               \n");
    sb.append(" /*005*/          CIRC1, GT1, GT2, LT1, LT2, OR1, AND1, ASG1, NOT1, DOTS1,                 \n");
    sb.append(" /*006*/          S_SELF=1024, S_SELFB, S_EOF, S_NL, S_EOFSTR,                             \n");
    sb.append(" /*007*/          S_STNL, S_COMNL, S_EOFCOM, S_COMMENT, S_EOB, S_WS, S_NAME                \n");
    sb.append(" /*008*/      };                                                                           \n");
    sb.append(" /*009*/      int tottok;                                                                  \n");
    sb.append(" /*010*/      int tokkind[256];                                                            \n");
    sb.append(" /*011*/      struct  fsm {                                                                \n");
    sb.append(" /*012*/          int state;                                                               \n");
    sb.append(" /*013*/          unsigned char ch[4];                                                     \n");
    sb.append(" /*014*/          int nextstate;                                                           \n");
    sb.append(" /*015*/      };                                                                           \n");
    sb.append(" /*016*/      struct fsm fsm[] = {                                                         \n");
    sb.append(" /*017*/          START,  { 1 },  1,                                                       \n");
    sb.append(" /*018*/          START,  { 11,12, }, WS1,                                                 \n");
    sb.append(" /*019*/          START,  { 3 },  2,                                                       \n");
    sb.append(" /*020*/          -1                                                                       \n");
    sb.append(" /*021*/      };                                                                           \n");
    sb.append(" /*022*/      return 0;                                                                    \n");
    sb.append(" /*023*/  }                                                                                \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

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

    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testConv1() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  void f1(int x[4096]) {                                                                                                \n");
    sb.append(" /*002*/      _Static_assert(sizeof(x) == sizeof(unsigned long long)                                                            \n");
    sb.append(" /*003*/          , \"conv.arr to ptr in fparam.\"                                                                              \n");
    sb.append(" /*004*/      );                                                                                                                \n");
    sb.append(" /*005*/  }                                                                                                                     \n");
    sb.append(" /*006*/  void f2(int a, int b) {  }                                                                                            \n");
    sb.append(" /*007*/  void f3(void (*fp)(int, int)) { }                                                                                     \n");
    sb.append(" /*008*/  int main ()                                                                                                           \n");
    sb.append(" /*009*/  {                                                                                                                     \n");
    sb.append(" /*010*/      void (*fp)(int,int);                                                                                              \n");
    sb.append(" /*011*/      char i8 = 0;                                                                                                      \n");
    sb.append(" /*012*/      short i16 = 0;                                                                                                    \n");
    sb.append(" /*013*/      int i32 = 0;                                                                                                      \n");
    sb.append(" /*014*/      long long i64 = 0;                                                                                                \n");
    sb.append(" /*015*/      float f32 = 0;                                                                                                    \n");
    sb.append(" /*016*/      double f64 = 0;                                                                                                   \n");
    sb.append(" /*017*/      long double f128 = 0;                                                                                             \n");
    sb.append(" /*018*/      _Static_assert(    4 == sizeof( ((_Bool)0 + (_Bool)0) ), \"((_Bool)0 + (_Bool)0)\" );                             \n");
    sb.append(" /*019*/      _Static_assert(    4 == sizeof( ((char)0 + (char)0) ), \"((char)0 + (char)0)\" );                                 \n");
    sb.append(" /*020*/      _Static_assert(    4 == sizeof( ((short)0 + (char)0) ), \"((short)0 + (char)0)\" );                               \n");
    sb.append(" /*021*/      _Static_assert(    4 == sizeof( ((int)0 + (char)0) ), \"((int)0 + (char)0)\" );                                   \n");
    sb.append(" /*022*/      _Static_assert(    4 == sizeof( ((float)0 + (char)0) ), \"((float)0 + (char)0)\" );                               \n");
    sb.append(" /*023*/      _Static_assert(    8 == sizeof( ((double)0 + (char)0) ), \"((double)0 + (char)0)\" );                             \n");
    sb.append(" /*024*/      _Static_assert(    sizeof(long long) == sizeof( ((long long)0 - (char)0) ), \"((long long)0 - (char)0)\" );       \n");
    sb.append(" /*025*/      _Static_assert(   16 == sizeof( ((long double)0 + (char)0) ), \"((long double)0 + (char)0)\" );                   \n");
    sb.append(" /*026*/      _Static_assert(    4 == sizeof( ((_Bool)0 || (_Bool)0) ), \"((_Bool)0 || (_Bool)0)\" );                           \n");
    sb.append(" /*027*/      _Static_assert(    4 == sizeof( ((int)0 && (char)0) ), \"((int)0 && (char)0)\" );                                 \n");
    sb.append(" /*028*/      _Static_assert(    4 == sizeof( ((long double)0 && (long double)0) ), \"((long double)0 && (long double)0)\" );   \n");
    sb.append(" /*029*/      _Static_assert(    4 == sizeof( ((_Bool)0 | (_Bool)0) ), \"((_Bool)0 | (_Bool)0)\" );                             \n");
    sb.append(" /*030*/      _Static_assert(    4 == sizeof( ((int)0 & (char)0) ), \"((int)0 & (char)0)\" );                                   \n");
    sb.append(" /*031*/      _Static_assert(    8 == sizeof( ((long long)0 & (char)0) ), \"((long long)0 & (char)0)\" );                       \n");
    sb.append(" /*032*/      _Static_assert(    4 == sizeof( (!(_Bool)0) ), \"(!(_Bool)0)\" );                                                 \n");
    sb.append(" /*033*/      _Static_assert(    4 == sizeof( (-(int)0) ), \"(-(int)0)\" );                                                     \n");
    sb.append(" /*034*/      _Static_assert(    4 == sizeof( (+(char)0) ), \"(+(char)0)\" );                                                   \n");
    sb.append(" /*035*/      _Static_assert(    8 == sizeof( (+(long long)0) ), \"(+(long long)0)\" );                                         \n");
    sb.append(" /*036*/      _Static_assert(    1 == sizeof( (i8 += i16) ), \"(i8 += i16)\" );                                                 \n");
    sb.append(" /*037*/      _Static_assert(    1 == sizeof( (i8 = f64+f128) ), \"(i8 = f64+f128)\" );                                         \n");
    sb.append(" /*038*/      _Static_assert(    2 == sizeof( (i16 = i8+i16+i32+i64) ), \"(i16 = i8+i16+i32+i64)\" );                           \n");
    sb.append(" /*039*/      _Static_assert(    2 == sizeof( (i16 = f64) ), \"(i16 = f64)\" );                                                 \n");
    sb.append(" /*040*/      _Static_assert(    8 == sizeof( (f64 = i8) ), \"(f64 = i8)\" );                                                   \n");
    sb.append(" /*041*/      _Static_assert(    8 == sizeof( (f64 = f128) ), \"(f64 = f128)\" );                                               \n");
    sb.append(" /*042*/      _Static_assert(   16 == sizeof( (f128 = i8 || i32) ), \"(f128 = i8 || i32)\" );                                   \n");
    sb.append(" /*043*/      _Static_assert(    1 == sizeof( (i8++) ), \"(i8++)\" );                                                           \n");
    sb.append(" /*044*/      _Static_assert(    8 == sizeof( (++i64) ), \"(++i64)\" );                                                         \n");
    sb.append(" /*045*/      _Static_assert(    1 == sizeof( (f2(1, 2)) ), \"(f2(1, 2))\" );                                                   \n");
    sb.append(" /*046*/      _Static_assert(    8 == sizeof( (fp = f2) ), \"(fp = f2)\" );                                                     \n");
    sb.append(" /*047*/      _Static_assert(    8 == sizeof( (fp = &f2) ), \"(fp = &f2)\" );                                                   \n");
    sb.append(" /*050*/      _Static_assert(    1 == sizeof( (f3(fp)) ), \"(f3(fp))\" );                                                       \n");
    sb.append(" /*051*/      return 0;                                                                                                         \n");
    sb.append(" /*052*/  }                                                                                                                     \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();

    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testConv2() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                                                                         \n");
    sb.append(" /*002*/      int a[7];                                                                        \n");
    sb.append(" /*003*/      int b[1][3];                                                                     \n");
    sb.append(" /*004*/      void c(void);                                                                    \n");
    sb.append(" /*005*/      void (*d)(void);                                                                 \n");
    sb.append(" /*006*/      //                                                                               \n");
    sb.append(" /*007*/      _Static_assert( _Generic(a  , int*            : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*008*/      _Static_assert( _Generic(&a , int(*)[7]       : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*009*/      _Static_assert( _Generic(*a , int             : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*010*/      //                                                                               \n");
    sb.append(" /*011*/      _Static_assert( _Generic(a+1     , int*       : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*012*/      _Static_assert( _Generic(*(a+1)  , int        : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*013*/      _Static_assert( _Generic(&*(a+1) , int*       : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*014*/      //                                                                               \n");
    sb.append(" /*015*/      _Static_assert( _Generic(b  , int(*)[3]       : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*016*/      _Static_assert( _Generic(&b , int(*)[1][3]    : 1, default: 2) == 1, \"NO\" );   \n");
    //sb.append(" /*017*/      _Static_assert( _Generic(*b , int*            : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*018*/      //                                                                               \n");
    sb.append(" /*019*/      _Static_assert( _Generic(c  , void (*)(void)  : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*020*/      _Static_assert( _Generic(&c , void (*)(void)  : 1, default: 2) == 1, \"NO\" );   \n");
    //sb.append(" /*021*/      _Static_assert( _Generic(*c , void (*)(void)  : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*022*/      //                                                                               \n");
    sb.append(" /*023*/      _Static_assert( _Generic(d  , void (*)(void)  : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*024*/      _Static_assert( _Generic(&d , void (**)(void) : 1, default: 2) == 1, \"NO\" );   \n");
    //sb.append(" /*025*/      _Static_assert( _Generic(*d , void (*)(void)  : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*026*/      //                                                                               \n");
    sb.append(" /*027*/      return 0;                                                                        \n");
    sb.append(" /*028*/  }                                                                                    \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();

    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testConv3() throws IOException {
    Map<String, Integer> s = new HashMap<String, Integer>();
    //@formatter:off
    s.put( " sizeof(_Bool)                   \n",  1);
    s.put( " sizeof((_Bool)1)                \n",  1);
    s.put( " sizeof((_Bool).2f)              \n",  1);
    s.put( " sizeof(_Bool*)                  \n",  8);
    s.put( " sizeof(char)                    \n",  1);
    s.put( " sizeof(int)                     \n",  4);
    s.put( " sizeof(void)                    \n",  1);
    s.put( " sizeof(1)                       \n",  4);
    s.put( " sizeof(1ULL)                    \n",  8);
    s.put( " sizeof(1 + 2)                   \n",  4);
    s.put( " sizeof(1 + 2ULL)                \n",  8);
    s.put( " sizeof(long double)             \n", 16);
    s.put( " sizeof(long double*)            \n",  8);
    s.put( " sizeof((char)1ULL)              \n",  1);
    s.put( " sizeof((int)1ULL)               \n",  4);
    s.put( " sizeof((long long)1ULL)         \n",  8);
    s.put( " sizeof(1.0f + 2.14)             \n",  8); // TODO:
    s.put( " sizeof(1.0f + 2.14f)            \n",  4);
    s.put( " sizeof((void*)0)                \n",  8);
    s.put( " sizeof((int*)0)                 \n",  8);
    s.put( " sizeof((int)+1)                 \n",  4);
    s.put( " sizeof(int(*)())                \n",  8);
    s.put( " sizeof 1 + 3                    \n",  7);
    s.put( " sizeof 1 + 3ULL                 \n",  7);
    s.put( " sizeof 1ULL                     \n",  8);
    s.put( " sizeof +1                       \n",  4);
    s.put( " sizeof 1024                     \n",  4);
    s.put( " sizeof 1024 + 1ULL              \n",  5);
    s.put( " sizeof((1+2, 2+3))              \n",  4);
    s.put(" sizeof(((char)'1'))              \n",  1);
    s.put(" sizeof(('1', 2))                 \n",  4);
    s.put(" sizeof(('1', 2, 3ULL))           \n",  8);
    s.put(" sizeof(('1', 2, 3ULL, 4.f))      \n",  4);
    s.put(" sizeof(('1', 2, 3ULL, 4.f, 5.))  \n",  8); // TODO:
    s.put("sizeof( ((_Bool)0 + (_Bool)0) )   \n",  4);
    //@formatter:on

    int x = 0;
    for (Entry<String, Integer> entry : s.entrySet()) {

      String source = entry.getKey();
      Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();

      Parse p = new Parse(it);
      Expression expr = new ParseExpression(p).e_expression();

      long ce = new ConstexprEval(p).ce(expr);

      if (ce != entry.getValue()) {
        System.out.println(entry.getKey());
      }

      assertEquals(entry.getValue().intValue(), ce);

    }
  }

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

  }

  @Test(expected = AstParseException.class)
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

  }

}
