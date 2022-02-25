package ast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ast.errors.ParseException;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist; 

public class Test_StructFieldAccess {

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

  @Test(expected = ParseException.class)
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

  @Test(expected = ParseException.class)
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

}
