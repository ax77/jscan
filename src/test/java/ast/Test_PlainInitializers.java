package ast;

import java.io.IOException;

import org.junit.Test;

import ast.decls.Initializer;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.symtab.elements.CSymbol;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist; 

public class Test_PlainInitializers {

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

    boolean print = false;
    if (!print) {
      return;
    }

    for (CSymbol sym : unit.getExternalDeclarations().get(0).getFunctionDefinition().getLocals()) {
      if (sym.getInitializer() != null) {
        System.out.printf("name=%s, type=%s\n", sym.getName().getName(), sym.getType());
        for (Initializer init : sym.getInitializer()) {
          System.out.println(init);
        }
        System.out.println();
      }
    }
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

    boolean print = false;
    if (!print) {
      return;
    }

    for (CSymbol sym : unit.getExternalDeclarations().get(0).getFunctionDefinition().getLocals()) {
      if (sym.getInitializer() != null) {
        System.out.printf("name=%s, type=%s\n", sym.getName().getName(), sym.getType());
        for (Initializer init : sym.getInitializer()) {
          System.out.println(init);
        }
        System.out.println();
      }
    }
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

    boolean print = false;
    if (!print) {
      return;
    }

    for (CSymbol sym : unit.getExternalDeclarations().get(0).getFunctionDefinition().getLocals()) {
      if (sym.getInitializer() != null) {
        System.out.printf("name=%s, type=%s\n", sym.getName().getName(), sym.getType());
        for (Initializer init : sym.getInitializer()) {
          System.out.println(init);
        }
        System.out.println();
      }
    }
  }

}
