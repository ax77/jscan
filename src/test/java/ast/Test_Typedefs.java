package ast;

import java.io.IOException;

import org.junit.Test;

import ast.errors.ParseException;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist; 

public class Test_Typedefs {

  private Tokenlist getTokenlist(StringBuilder sb) throws IOException {
    return new ParserMain(sb).preprocess();
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

  @Test(expected = ParseException.class)
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

}
