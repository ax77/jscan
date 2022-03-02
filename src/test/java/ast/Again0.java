package ast;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ast.main.ParserMain;
import ast.parse.Parse;
import ast.symtab.CSymbol;
import ast.tree.BlockItem;
import ast.tree.Declaration;
import ast.tree.Expression;
import ast.tree.ExpressionBase;
import ast.tree.ExternalDeclaration;
import ast.tree.FunctionDefinition;
import ast.tree.Statement;
import ast.tree.StatementBase;
import ast.tree.TranslationUnit;
import jscan.parse.Tokenlist;
import jscan.utils.AstParseException;

public class Again0 {

  private void cg(TranslationUnit unit) {
    for (ExternalDeclaration ext : unit.getExternalDeclarations()) {
      if (ext.isDeclaration()) {
        genDeclaration(ext.getDeclaration());
      }
      if (ext.isFunctionDefinition()) {
        genFunction(ext.getFunctionDefinition());
      }
    }
  }

  private void genDeclaration(Declaration declaration) {
    if (declaration.isStaticAssertStub()) {
      return;
    }

    if (declaration.isAgregate()) {
      throw new AstParseException("todo: agregate declarations");
    }

    List<CSymbol> vars = declaration.getVariables();
    for(CSymbol var : vars) {
      genVarDecl(var);
    }
  }

  private void genVarDecl(CSymbol var) {
    System.out.println(var);
  }

  private void genFunction(FunctionDefinition functionDefinition) {
    CSymbol sym = functionDefinition.getSignature();
    Statement block = functionDefinition.getBlock();
    genStmt(block);
  }

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

    else if (base == StatementBase.SEXPR) {
      Expression e = s.getStmtExpr();
      genExpr(e);
    }

    else if (base == StatementBase.SRETURN) {
      Expression e = s.getStmtExpr();
      genExpr(e);
    }

    else {
      throw new AstParseException("todo: " + base);
    }
  }

  private void genExpr(Expression e) {
    // this is ok, return stmt without the expression and so on
    if (e == null) {
      return;
    }

    ExpressionBase base = e.getBase();
    System.out.println(e);

    if (base == ExpressionBase.EASSIGN) {

    }

    else if (base == ExpressionBase.EUNARY) {

    }

    else if (base == ExpressionBase.EBINARY) {

    }

    else {
      throw new AstParseException("todo: " + base);
    }
  }

  @Test
  public void test0() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int             \n");
    sb.append(" /*002*/  main()          \n");
    sb.append(" /*003*/  {               \n");
    sb.append(" /*004*/      int x;      \n");
    sb.append(" /*005*/      int *p;     \n");
    sb.append(" /*006*/      x = 4;      \n");
    sb.append(" /*007*/      p = &x;     \n");
    sb.append(" /*008*/      *p = 0;     \n");
    sb.append(" /*009*/      return *p;  \n");
    sb.append(" /*010*/  }               \n");

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);

    TranslationUnit unit = p.parse_unit();
    cg(unit);
  }

}
