package ast_test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import ast.main.ParseMainNew;
import ast.main.ParseOpts;
import ast.tree.TranslationUnit;
import ast.types.CStructType;
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
    sb.append(" /*001*/  /**/                                                        \n");
    sb.append(" /*002*/                                                              \n");
    sb.append(" /*003*/  struct x {                                                  \n");
    sb.append(" /*004*/      struct {                                                \n");
    sb.append(" /*005*/          int a;                                              \n");
    sb.append(" /*006*/          int b;                                              \n");
    sb.append(" /*007*/      };                                                      \n");
    sb.append(" /*008*/      union {                                                 \n");
    sb.append(" /*009*/          int c;                                              \n");
    sb.append(" /*010*/          int d;                                              \n");
    sb.append(" /*011*/      };                                                      \n");
    sb.append(" /*012*/      int e;                                                  \n");
    sb.append(" /*013*/      int f;                                                  \n");
    sb.append(" /*014*/      int g;                                                  \n");
    sb.append(" /*015*/  };                                                          \n");
    //@formatter:on

    TranslationUnit unit = parseUnit(sb);

    final CType typ = unit.getExternalDeclarations().get(0).declaration.agregate;
    assertEquals(24, typ.size);
    assertEquals(4, typ.align);

    final CStructType s = typ.tpStruct;
    assertEquals(0, s.getFieldByName("a").offset);
    assertEquals(4, s.getFieldByName("b").offset);
    assertEquals(8, s.getFieldByName("c").offset);
    assertEquals(8, s.getFieldByName("d").offset);
    assertEquals(12, s.getFieldByName("e").offset);
    assertEquals(16, s.getFieldByName("f").offset);
    assertEquals(20, s.getFieldByName("g").offset);
  }

  @Test
  public void testOffsetof1() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  struct x {                                                  \n");
    sb.append(" /*002*/      struct {                                                \n");
    sb.append(" /*003*/          int a;                                              \n");
    sb.append(" /*004*/          int b;                                              \n");
    sb.append(" /*005*/      };                                                      \n");
    sb.append(" /*006*/      int c;                                                  \n");
    sb.append(" /*007*/      int d;                                                  \n");
    sb.append(" /*008*/      int e;                                                  \n");
    sb.append(" /*009*/  };                                                          \n");
    //@formatter:on

    TranslationUnit unit = parseUnit(sb);
    CStructType tp = unit.getExternalDeclarations().get(0).declaration.agregate.tpStruct;
    assertEquals(tp.getFieldByName("a").offset, 0);
    assertEquals(tp.getFieldByName("b").offset, 4);
    assertEquals(tp.getFieldByName("c").offset, 8);
    assertEquals(tp.getFieldByName("d").offset, 12);
    assertEquals(tp.getFieldByName("e").offset, 16);
  }

}
