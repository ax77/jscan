package ast;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ast.decls.Initializer;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.symtab.elements.CSymbol;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist; 

public class Test_Declarations {

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

    boolean print = false;
    if (!print) {
      return;
    }

    final List<CSymbol> locals = unit.getExternalDeclarations().get(0).getFunctionDefinition().getLocals();
    for (CSymbol sym : locals) {
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

    boolean print = false;
    if (!print) {
      return;
    }

    final List<CSymbol> locals = unit.getExternalDeclarations().get(0).getFunctionDefinition().getLocals();
    for (CSymbol sym : locals) {
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

    boolean print = false;
    if (!print) {
      return;
    }

    final List<CSymbol> locals = unit.getExternalDeclarations().get(0).getFunctionDefinition().getLocals();
    for (CSymbol sym : locals) {
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
