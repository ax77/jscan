package ast_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import ast.main.ParseMainNew;
import ast.main.ParseOpts;
import ast.symtab.Finders;
import ast.tree.TranslationUnit;
import ast.types.CStructField;
import ast.types.CType;

public class Test_Fields {

  private TranslationUnit parseUnit(StringBuilder sb) throws IOException {
    ParseOpts opts[] = new ParseOpts[] {};
    TranslationUnit unit = new ParseMainNew(opts).parseString(sb.toString());
    return unit;
  }

  @Test
  public void testOffsetof0() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  struct x {                          \n");
    sb.append(" /*002*/      struct { int a, b; };           \n");
    sb.append(" /*003*/      union {                         \n");
    sb.append(" /*004*/          struct { int c; int d; };   \n");
    sb.append(" /*005*/          int e;                      \n");
    sb.append(" /*006*/          int f;                      \n");
    sb.append(" /*007*/      };                              \n");
    sb.append(" /*008*/      int g,h;                        \n");
    sb.append(" /*008*/      int i;                          \n");
    sb.append(" /*009*/      struct {                        \n");
    sb.append(" /*010*/          struct {                    \n");
    sb.append(" /*011*/              int j;                  \n");
    sb.append(" /*012*/          };                          \n");
    sb.append(" /*013*/          struct {                    \n");
    sb.append(" /*014*/              union {                 \n");
    sb.append(" /*015*/                  int k,l,m;          \n");
    sb.append(" /*016*/              };                      \n");
    sb.append(" /*017*/          };                          \n");
    sb.append(" /*018*/      };                              \n");
    sb.append(" /*019*/  };                                  \n");
    //@formatter:on

    TranslationUnit unit = parseUnit(sb);

    final CType typ = unit.getExternalDeclarations().get(0).declaration.agregate;

    assertEquals(36, typ.size);
    assertEquals(4, typ.align);

    int offsets[] = { 0, 4, 8, 12, 8, 8, 16, 20, 24, 28, 32, 32, 32, };
    String names[] = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", };
    for (int i = 0; i < names.length; i++) {
      int o[] = { 0 };

      CStructField f = Finders.find_identifier(names[i], typ.tpStruct.fields, o);
      assertNotNull(f);

      assertEquals(offsets[i], o[0]);
    }

  }

  /// Aligner
  ///
  /// s   0 a             s   0 int
  /// s   4 b             s   4 int
  /// s   0 <noident>     s   0 (struct <no-tag>)
  /// s   0 c             s   0 int
  /// s   4 d             s   4 int
  /// u   0 <noident>     u   0 (struct <no-tag>)
  /// u   0 e             u   0 int
  /// u   0 f             u   0 int
  /// s   8 <noident>     s   8 (union <no-tag>)
  /// s  16 g             s  16 int
  /// s  20 h             s  20 int
  /// s  24 i             s  24 int
  /// s   0 j             s   0 int
  /// s   0 <noident>     s   0 (struct <no-tag>)
  /// u   0 k             u   0 int
  /// u   0 l             u   0 int
  /// u   0 m             u   0 int
  /// s   0 <noident>     s   0 (union <no-tag>)
  /// s   4 <noident>     s   4 (struct <no-tag>)
  /// s  28 <noident>     s  28 (struct <no-tag>)

  /// Find identifier 1 (*offset)
  ///
  /// 0 a
  /// 4 b
  /// 0 c
  /// 4 d
  /// 0 e
  /// 0 f
  /// 16 g
  /// 20 h
  /// 24 i
  /// 0 j
  /// 0 k
  /// 0 l
  /// 0 m

  /// Find identifier 2 (+=sym->offset)
  ///
  /// 0     0
  /// 0     0
  /// 0     0
  /// 8     8
  /// 0     0
  /// 8     8
  /// 8     8
  /// 8     8
  /// 0     0
  /// 28    28
  /// 0     0
  /// 4     4
  /// 28    28
  /// 0     0
  /// 4     4
  /// 28    28
  /// 0     0
  /// 4     4
  /// 28    28

}
