package ast_util;

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
import ast.types.CType;
import jscan.parse.Tokenlist;
import jscan.utils.Aligner;
import jscan.utils.AstParseException;
import static ast.tree.ExpressionBase.*;
import static ast.tree.StatementBase.*;

public class Again0 {

  private void out(String s) {
    if (s.endsWith(":") || s.startsWith(".")) {
      System.out.println(s);
    } else {
      System.out.printf("    %s\n", s);
    }
  }

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
    for (CSymbol var : vars) {
      genVarDecl(var);
    }
  }

  private void genVarDecl(CSymbol var) {
  }

  private int applyOffset(FunctionDefinition functionDefinition) {
    int offset = 0;
    for (CSymbol sym : functionDefinition.getLocals()) {
      sym.setOffset(offset);
      final CType type = sym.getType();
      offset += Aligner.align(type.getSize(), type.getAlign());
    }
    return Aligner.align(offset, 16);
  }

  private void genFunction(FunctionDefinition functionDefinition) {
    CSymbol sym = functionDefinition.getSignature();
    int localsize = applyOffset(functionDefinition);

    out(".global " + sym.getName().getName());
    out(sym.getName().getName() + ":");
    out("push rbp");
    out("mov rbp, rsp");
    out(String.format("sub rsp, %d", localsize));

    Statement block = functionDefinition.getBlock();
    genStmt(block);

    out(String.format("add rsp, %d", localsize));
    out("mov rsp, rbp");
    out("pop rbp");
    out("ret");
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
    // this is ok, return stmt without the expression and so on
    if (e == null) {
      return;
    }

    ExpressionBase base = e.getBase();

    if (base == EASSIGN) {
    }

    else if (base == EBINARY) {
    }

    else if (base == ECOMMA) {
    }

    else if (base == ETERNARY) {
    }

    else if (base == EUNARY) {
    }

    else if (base == EPRIMARY_IDENT) {
    }

    else if (base == EPRIMARY_NUMBER) {
    }

    else if (base == EPRIMARY_STRING) {
    }

    else if (base == EPRIMARY_GENERIC) {
    }

    else if (base == ECOMPSEL) {
    }

    else if (base == ECAST) {
    }

    else if (base == EFCALL) {
    }

    else if (base == EPREINCDEC) {
    }

    else if (base == EPOSTINCDEC) {
    }

    else if (base == ECOMPLITERAL) {
    }

    else {
      throw new AstParseException("todo: " + base);
    }
  }

  @Test
  public void test0() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(" int             \n");
    sb.append(" main()          \n");
    sb.append(" {               \n");
    sb.append("     int x,y,z;  \n");
    sb.append("     x = 4;      \n");
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

    out(".intel_syntax noprefix");
    out(".text");
    cg(unit);
  }

}
