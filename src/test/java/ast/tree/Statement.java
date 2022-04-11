package ast.tree;

import java.util.ArrayList;
import java.util.List;

import ast.tree.AttributesAsmsLists.AsmList;
import ast.tree.CSymbol.CSymFunction;
import jscan.symtab.Ident;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;

public class Statement {

  public final StatementBase base;
  public final Token location;
  public List<BlockItem> block;
  public StmtSwitch stmtSwitch;
  public StmtCase stmtCase;
  public StmtDefault stmtDefault;
  public StmtFor stmtFor;
  public StmtWhileDo stmtWhileDo;
  public Expression stmtExpr;
  public StmtSelect stmtSelect;
  public StmtLabel stmtLabel;
  public AsmList asmlist;

  public Statement(StmtFor stmtFor, Token from) {
    this.location = from;
    this.base = StatementBase.SFOR;
    this.stmtFor = stmtFor;
  }

  public Statement(StatementBase base, StmtWhileDo stmtWhileDo, Token from) {
    this.location = from;
    this.base = base;
    this.stmtWhileDo = stmtWhileDo;
  }

  // return expr
  // return ;
  // expr-stmt
  public Statement(Token from, StatementBase base, Expression expr) {
    this.location = from;
    this.base = base;
    this.stmtExpr = expr;
  }

  // label: stmt
  // goto label;
  public Statement(StatementBase base, StmtLabel stmtLabel, Token from) {
    this.location = from;
    this.base = base;
    this.stmtLabel = stmtLabel;
  }

  public Statement(StmtSelect stmtSelect, Token from) {
    this.location = from;
    this.base = StatementBase.SIF;
    this.stmtSelect = stmtSelect;
  }

  public Statement(Token lbrace, Token rbrace, List<BlockItem> block) {
    this.location = lbrace; // TODO: range loc
    this.base = StatementBase.SCOMPOUND;
    this.block = block;
  }

  public Statement(Token from, AsmList asmlist) {
    this.location = from;
    this.base = StatementBase.SASM;
    this.asmlist = asmlist;
  }

  public Statement(Token from, StmtDefault stmtDefault) {
    this.location = from;
    this.base = StatementBase.SDEFAULT;
    this.stmtDefault = stmtDefault;
  }

  // break, continue
  public Statement(Token from, StatementBase base) {
    this.location = from;
    this.base = base;
  }

  public Statement(Token from, StmtSwitch stmtSwitch) {
    this.location = from;
    this.base = StatementBase.SSWITCH;
    this.stmtSwitch = stmtSwitch;
  }

  public Statement(Token from, StmtCase stmtCase) {
    this.location = from;
    this.base = StatementBase.SCASE;
    this.stmtCase = stmtCase;
  }

  // Nodes

  public static class StmtCase {
    public final StmtSwitch parent;
    public Expression constexpr;
    public Statement casestmt;

    public StmtCase(StmtSwitch parent, Expression constexpr) {
      this.parent = parent;
      this.constexpr = constexpr;
    }
  }

  public static class StmtDefault {
    public final StmtSwitch parent;
    public final Statement stmt;

    public StmtDefault(StmtSwitch parent, Statement stmt) {
      this.parent = parent;
      this.stmt = stmt;
    }
  }

  public static class StmtFor {
    public final Declaration decl;
    public final Expression init;
    public final Expression test;
    public final Expression step;
    public final Statement loop;

    public StmtFor(Declaration decl, Expression init, Expression test, Expression step, Statement loop) {
      this.decl = decl;
      this.init = init;
      this.test = test;
      this.step = step;
      this.loop = loop;
    }
  }

  public static class StmtSwitch {
    public List<StmtCase> cases;
    public Expression expr;
    public Statement stmt;
    public StmtDefault defaultStmt;

    public StmtSwitch(Expression expr) {
      this.cases = new ArrayList<StmtCase>(1);
      this.expr = expr;
    }

    public void pushcase(StmtCase onecase) {
      this.cases.add(onecase);
    }

    public void setDefault(StmtDefault defaultStmt) {
      if (this.defaultStmt != null) {
        throw new AstParseException("duplicate default label");
      }
      this.defaultStmt = defaultStmt;
    }
  }

  public static class StmtWhileDo {
    public final Expression test;
    public final Statement loop;

    public StmtWhileDo(Expression test, Statement loop) {
      this.test = test;
      this.loop = loop;
    }
  }

  public static class StmtSelect {
    public final Expression condition;
    public final Statement ifStmt;
    public final Statement elseStmt;

    public StmtSelect(Expression condition, Statement ifStmt, Statement elseStmt) {
      this.condition = condition;
      this.ifStmt = ifStmt;
      this.elseStmt = elseStmt;
    }
  }

  public static class StmtLabel {
    public final CSymFunction function;
    public final Ident label;
    public final Statement stmt;

    public StmtLabel(CSymFunction function, Ident label, Statement stmt) {
      this.function = function;
      this.label = label;
      this.stmt = stmt;
    }
  }

}
