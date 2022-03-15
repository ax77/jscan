package ast_test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ast.main.ParseMainNew;
import ast.main.ParseOpts;
import ast.tree.TranslationUnit;
import ast.types.CStructField;
import ast.types.CType;

public class Test_Fields {

  private TranslationUnit parseUnit(StringBuilder sb) throws IOException {
    ParseOpts opts[] = new ParseOpts[] {};
    TranslationUnit unit = new ParseMainNew(opts).parseString(sb.toString());
    return unit;
  }

  static class Info {
    int max_align;
    int bit_size;

    public Info() {
      max_align = 1;
      bit_size = 0;
    }
  }

  private void lay_out_union(CType sym, Info info) {

    examine_symbol_type(sym);
    if (sym.align > info.max_align)
      info.max_align = sym.align;
    if (sym.size > info.bit_size)
      info.bit_size = sym.size;

    sym.offset = 0;
  }

  private void lay_out_struct(CType sym, Info info) {

    examine_symbol_type(sym);
    if (sym.align > info.max_align)
      info.max_align = sym.align;

    int bit_size = info.bit_size;
    int align_bit_mask = (sym.align << 3) - 1;

    bit_size = (bit_size + align_bit_mask) & ~align_bit_mask;
    sym.offset = bit_size >> 3;
    info.bit_size = bit_size + (sym.size);
  }

  private void iterate_st(CType sym, Info info) {
    for (CStructField f : sym.tpStruct.fields) {
      lay_out_struct(f.type, info);
    }
  }

  private void iterate_un(CType sym, Info info) {
    for (CStructField f : sym.tpStruct.fields) {
      lay_out_union(f.type, info);
    }
  }

  private void examine_struct_union_type(CType sym, boolean struc) {
    Info info = new Info();

    if (struc) {
      iterate_st(sym, info);
    } else {
      iterate_un(sym, info);
    }

    if (sym.align <= 0)
      sym.align = info.max_align;

    int bit_size = info.bit_size;
    int bit_align = (sym.align << 3) - 1;
    bit_size = (bit_size + bit_align) & ~bit_align;
    sym.size = bit_size;
  }

  private CType examine_symbol_type(CType sym) {
    if (sym.size > 0) {
      return sym;
    }

    // TODO.flexible.array

    if (sym.isStruct()) {
      examine_struct_union_type(sym, true);
      return sym;
    }

    if (sym.isUnion()) {
      examine_struct_union_type(sym, false);
      return sym;
    }

    return sym;
  }

  private CStructField find_identifier(String ident, List<CStructField> list, int[] o) {
    for (int i = 0; i < list.size(); i++) {
      CStructField sym = list.get(i);
      if (sym.hasName()) {
        if (!sym.name.getName().equals(ident)) {
          continue;
        }
        o[0] = sym.type.offset;
        return sym;
      } else {
        if (!sym.type.isStrUnion()) {
          continue;
        }
        CStructField sub = find_identifier(ident, sym.type.tpStruct.fields, o);
        if (sub == null) {
          continue;
        }
        o[0] += sym.type.offset;
        return sub;
      }
    }
    return null;
  }

  @Test
  public void testOffsetof0() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  struct x {                          \n");
    sb.append(" /*002*/      struct { int a; int b; };       \n");
    sb.append(" /*003*/      union {                         \n");
    sb.append(" /*004*/          struct { int c; int d; };   \n");
    sb.append(" /*005*/          int e;                      \n");
    sb.append(" /*006*/          int f;                      \n");
    sb.append(" /*007*/      };                              \n");
    sb.append(" /*008*/      int g;                          \n");
    sb.append(" /*008*/      int h;                          \n");
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

    final CType typ = unit.getExternalDeclarations().get(0).declaration.agregate;
    examine_symbol_type(typ);
    //System.out.println(typ.size + " " + typ.align);

    int offsets[] = { 0, 4, 8, 12, 8, 8, 16, 20, 24, 28, 32, 32, 32, };
    String names[] = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", };
    for (int i = 0; i < offsets.length; i++) {
      int o[] = { 0 };
      CStructField f = find_identifier(names[i], typ.tpStruct.fields, o);
      assertEquals(offsets[i], o[0]);
    }

    // assertEquals(24, typ.size);
    // assertEquals(4, typ.align);
    // 
    // final CStructType s = typ.tpStruct;
    // //@formatter:off
    // assertEquals(0 , s.getFieldByName("a").offset);
    // assertEquals(4 , s.getFieldByName("b").offset);
    // assertEquals(8 , s.getFieldByName("c").offset);
    // assertEquals(12, s.getFieldByName("d").offset);
    // assertEquals(8 , s.getFieldByName("e").offset);
    // assertEquals(8 , s.getFieldByName("f").offset);
    // assertEquals(16, s.getFieldByName("g").offset);
    // assertEquals(20, s.getFieldByName("h").offset);
    // assertEquals(24, s.getFieldByName("i").offset);
    // assertEquals(28, s.getFieldByName("j").offset);
    // assertEquals(32, s.getFieldByName("k").offset);
    // assertEquals(32, s.getFieldByName("l").offset);
    // assertEquals(32, s.getFieldByName("m").offset);
    // //@formatter:on

  }

}
