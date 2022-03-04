package ast.flat;

import static ast.tree.StatementBase.SASM;
import static ast.tree.StatementBase.SBREAK;
import static ast.tree.StatementBase.SCASE;
import static ast.tree.StatementBase.SCONTINUE;
import static ast.tree.StatementBase.SDEFAULT;
import static ast.tree.StatementBase.SDOWHILE;
import static ast.tree.StatementBase.SEXPR;
import static ast.tree.StatementBase.SFOR;
import static ast.tree.StatementBase.SGOTO;
import static ast.tree.StatementBase.SIF;
import static ast.tree.StatementBase.SLABEL;
import static ast.tree.StatementBase.SRETURN;
import static ast.tree.StatementBase.SSEMICOLON;
import static ast.tree.StatementBase.SSWITCH;
import static ast.tree.StatementBase.SWHILE;

import java.util.ArrayList;
import java.util.List;

import ast.symtab.CSymbol;
import ast.symtab.CSymbolBase;
import ast.tree.BlockItem;
import ast.tree.Declaration;
import ast.tree.Expression;
import ast.tree.Statement;
import ast.tree.StatementBase;
import ast.tree.StmtLabel;
import ast.tree.StmtSelect;
import jscan.symtab.Ident;
import jscan.utils.AstParseException;
import jscan.utils.GlobalCounter;

public class RewriteStmt {

  private List<ExecFlowItem> items;

  public RewriteStmt() {
    this.items = new ArrayList<>();
  }

  public List<ExecFlowItem> getItems() {
    return items;
  }

  static private String label(String pref) {
    return GlobalCounter.nextLabel(pref + ".");
  }

  private void push(ExecFlowItem e) {
    items.add(e);
  }

  public void genStmt(Statement s) {
    if (s == null) {
      todo("why?");
    }

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
      String elseLabel = label("else");
      String endLabel = label("out");

      StmtSelect select = s.getStmtSelect();
      Expression cond = select.getCondition();
      Statement ifStmt = select.getIfStmt();
      Statement elseStmt = select.getElseStmt();

      if (elseStmt != null) {

        push(new ExecFlowItem(ExecFlowBase.test, cond));
        push(new ExecFlowItem(ExecFlowBase.je, elseLabel));

        genStmt(ifStmt);
        push(new ExecFlowItem(ExecFlowBase.jmp, endLabel));
        push(new ExecFlowItem(elseLabel));

        genStmt(elseStmt);
        push(new ExecFlowItem(endLabel));

      } else {

        push(new ExecFlowItem(ExecFlowBase.test, cond));
        push(new ExecFlowItem(ExecFlowBase.je, endLabel));

        genStmt(ifStmt);
        push(new ExecFlowItem(endLabel));
      }
    }

    else if (base == SWHILE) {
    }

    else if (base == SDOWHILE) {
    }

    else if (base == SEXPR) {
      push(new ExecFlowItem(ExecFlowBase.expr, s.getStmtExpr()));
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
      push(new ExecFlowItem(ExecFlowBase.ret, s.getStmtExpr()));
    }

    else if (base == SGOTO) {
      final StmtLabel stmtLabel = s.getStmtLabel();
      final Ident label = stmtLabel.getLabel();
      push(new ExecFlowItem(ExecFlowBase.jmp, label.getName()));
    }

    else if (base == SLABEL) {
      final StmtLabel stmtLabel = s.getStmtLabel();
      push(new ExecFlowItem(stmtLabel.getLabel().getName()));
      genStmt(stmtLabel.getStmt());
    }

    else if (base == SDEFAULT) {
    }

    else if (base == SASM) {
    }

    else {
      throw new AstParseException("todo: " + base);
    }
  }

  private void genSym(CSymbol sym) {
    CSymbolBase base = sym.getBase();
    if (base == CSymbolBase.SYM_LVAR) {
      if (sym.getInitializer() != null) {
        push(new ExecFlowItem(sym));
      }
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

}
