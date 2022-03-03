package ast.flat.func;

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

import ast.builders.ApplyExpressionType;
import ast.builders.TypeApplierStage;
import ast.flat.LinearExpression;
import ast.flat.RewriterExpr;
import ast.flat.rvalues.Leaf;
import ast.flat.rvalues.Var;
import ast.symtab.CSymbol;
import ast.symtab.CSymbolBase;
import ast.tree.BlockItem;
import ast.tree.Declaration;
import ast.tree.Expression;
import ast.tree.Initializer;
import ast.tree.Statement;
import ast.tree.StatementBase;
import ast.tree.StmtLabel;
import ast.tree.StmtSelect;
import jscan.literals.IntLiteral;
import jscan.literals.IntLiteralType;
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
    return String.format("%s%d", pref, GlobalCounter.next());
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

      LinearExpression flat = genExpr(cond);
      push(new ExecFlowItem(flat));

      Var dest = flat.getDest();

      if (elseStmt != null) {

        Cmp cmp = new Cmp(dest, new Leaf(new IntLiteral(0, IntLiteralType.I32)));
        push(new ExecFlowItem(cmp));
        push(new ExecFlowItem(ExecFlowBase.je, elseLabel));

        genStmt(ifStmt);
        push(new ExecFlowItem(ExecFlowBase.jmp, endLabel));
        push(new ExecFlowItem(elseLabel));

        genStmt(elseStmt);
        push(new ExecFlowItem(endLabel));

      } else {

        Cmp cmp = new Cmp(dest, new Leaf(new IntLiteral(0, IntLiteralType.I32)));
        push(new ExecFlowItem(cmp));
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
      LinearExpression expr = genExpr(s.getStmtExpr());
      push(new ExecFlowItem(expr));
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
      LinearExpression expr = genExpr(s.getStmtExpr());
      push(new ExecFlowItem(expr));

      Var dest = expr.getDest();
      push(new ExecFlowItem(dest));
    }

    else if (base == SGOTO) {
      final StmtLabel stmtLabel = s.getStmtLabel();
      final Ident label = stmtLabel.getLabel();
      push(new ExecFlowItem(ExecFlowBase.jmp, label.getName()));
    }

    else if (base == SLABEL) {
      final StmtLabel stmtLabel = s.getStmtLabel();
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

  private LinearExpression genExpr(Expression e) {
    if (e == null) {
      todo("unexpected here.");
    }

    ApplyExpressionType.applytype(e, TypeApplierStage.stage_start);
    RewriterExpr rew = new RewriterExpr(e);
    LinearExpression lin = new LinearExpression(rew.getRawResult());

    return lin;
  }

  private LinearExpression genLvar(CSymbol sym) {
    List<Initializer> inits = sym.getInitializer();
    if (inits == null) {
      todo("unexpected here.");
    }
    if (inits.size() > 1) {
      todo("initializerr list: " + sym.getName());
    }
    Initializer init = inits.get(0);
    ApplyExpressionType.applytype(init.getInit(), TypeApplierStage.stage_start);

    RewriterExpr rew = new RewriterExpr(sym);
    LinearExpression lin = new LinearExpression(rew.getRawResult());

    return lin;
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

}
