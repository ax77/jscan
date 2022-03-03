package ast_util;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ast.builders.ApplyExpressionType;
import ast.builders.TypeApplierStage;
import ast.flat.LinearExpression;
import ast.flat.RewriterExpr;
import ast.flat.ir.FlatCodeItem;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.symtab.CSymbol;
import ast.symtab.CSymbolBase;
import ast.tree.BlockItem;
import ast.tree.Declaration;
import ast.tree.Expression;
import ast.tree.Initializer;
import ast.tree.Statement;
import ast.tree.StatementBase;
import ast.tree.TranslationUnit;
import jscan.fio.FileReadKind;
import jscan.fio.FileWrapper;
import jscan.parse.Tokenlist;
import jscan.utils.AstParseException;

import static ast.tree.StatementBase.*;

public class Again0 {

  private void genStmt(Statement s) {
    StatementBase base = s.getBase();

    if (base == StatementBase.SCOMPOUND) {
      for (BlockItem item : s.getBlock()) {
        if (item.isDeclaration()) {
          genDeclaration(item.getDeclaration());
        } else {
          genStmt(item.getStatement());
        }
      }
    }

    else if (base == SIF) {
    }

    else if (base == SWHILE) {
    }

    else if (base == SDOWHILE) {
    }

    else if (base == SEXPR) {
      genExpr(s.getStmtExpr());
    }

    else if (base == SBREAK) {
    }

    else if (base == SCONTINUE) {
    }

    else if (base == SSEMICOLON) {
    }

    else if (base == SSWITCH) {
    }

    else if (base == SCASE) {
    }

    else if (base == SFOR) {
    }

    else if (base == SRETURN) {
      genExpr(s.getStmtExpr());
    }

    else if (base == SGOTO) {
    }

    else if (base == SLABEL) {
    }

    else if (base == SDEFAULT) {
    }

    else if (base == SASM) {
    }

    else {
      throw new AstParseException("todo: " + base);
    }
  }

  private void genExpr(Expression e) {
    if (e == null) {
      return;
    }

    ApplyExpressionType.applytype(e, TypeApplierStage.stage_start);
    RewriterExpr rew = new RewriterExpr(e);
    LinearExpression lin = new LinearExpression(rew.getRawResult());

    System.out.println(lin);
  }

  private void genLvar(CSymbol sym) {
    List<Initializer> inits = sym.getInitializer();
    if (inits == null) {
      return;
    }
    if (inits.size() > 1) {
      todo("initializerr list: " + sym.getName());
    }
    Initializer init = inits.get(0);
    ApplyExpressionType.applytype(init.getInit(), TypeApplierStage.stage_start);

    RewriterExpr rew = new RewriterExpr(sym);
    LinearExpression lin = new LinearExpression(rew.getRawResult());
    System.out.println(lin);
  }

  private void genSym(CSymbol sym) {
    CSymbolBase base = sym.getBase();
    if (base == CSymbolBase.SYM_LVAR) {
      genLvar(sym);
    } else {
      todo("symbol: " + base.toString());
    }
  }

  private void genDeclaration(Declaration declaration) {
    if (declaration.isVarlist()) {
      for (CSymbol sym : declaration.getVariables()) {
        genSym(sym);
      }
    } else {
      todo("agregate decl.");
    }

  }

  private void todo(String string) {
    throw new AstParseException("todo: " + string);
  }

  @Test
  public void test0() throws IOException {

    String dir = System.getProperty("user.dir");
    String txt = new FileWrapper(dir + "/cc_tests/01.c").readToString(FileReadKind.APPEND_LF);

    Parse p = new Parse(new ParserMain(new StringBuilder(txt)).preprocess());
    TranslationUnit unit = p.parse_unit();

    genStmt(unit.getExternalDeclarations().get(0).getFunctionDefinition().getBlock());

  }

}
