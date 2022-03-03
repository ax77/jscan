package ast_util;

import java.io.IOException;

import org.junit.Test;

import _st3_linearize_expr.LinearExpression;
import _st3_linearize_expr.RewriterExpr;
import _st3_linearize_expr.ir.FlatCodeItem;
import ast.builders.ApplyExpressionType;
import ast.builders.TypeApplierStage;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.tree.Expression;
import ast.tree.TranslationUnit;
import jscan.parse.Tokenlist;

public class Again0 {

  @Test
  public void test0() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(" int             \n");
    sb.append(" main()          \n");
    sb.append(" {               \n");
    sb.append("     int x,y,z;  \n");
    sb.append("     x = 1+2*3;      \n");
    sb.append("     return x;   \n");
    sb.append(" }               \n");

    // .intel_syntax noprefix
    // .bss
    // .data
    // .text
    // .global main

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);

    TranslationUnit unit = p.parse_unit();
    Expression expr = unit.getExternalDeclarations().get(0).getFunctionDefinition().getBlock().getBlock().get(1)
        .getStatement().getStmtExpr();
    ApplyExpressionType.applytype(expr, TypeApplierStage.stage_start);

    RewriterExpr rew = new RewriterExpr(expr);
    LinearExpression lin = new LinearExpression(rew.getRawResult());
    System.out.println(lin.toString());

    for (FlatCodeItem item : rew.getRawResult()) {
      System.out.println(item);
    }
  }

}
