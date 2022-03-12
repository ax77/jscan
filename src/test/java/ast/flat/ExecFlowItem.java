package ast.flat;

import ast.builders.ApplyExpressionType;
import ast.builders.TypeApplierStage;
import ast.symtab.CSymbol;
import ast.tree.Expression;

public class ExecFlowItem {
  private final ExecFlowBase opc;

  // union-items
  private String label;
  private Expression expr;
  private CSymbol var;

  // bb-routine
  private boolean isLeader;
  private String basicBlockId = "";

  public ExecFlowItem(CSymbol var) {
    this.opc = ExecFlowBase.sym;
    this.var = var;
  }

  public boolean isAnyJmp() {
    return opc == ExecFlowBase.jmp || opc == ExecFlowBase.jz;
  }

  public boolean isLabel() {
    return opc == ExecFlowBase.label;
  }

  // this is a c-label-statement, and not a generated label
  public boolean isOriginalLabel() {
    if (!isLabel()) {
      return false;
    }
    return !label.contains(".");
  }

  public String getBasicBlockId() {
    return basicBlockId;
  }

  public void setBasicBlockId(String basicBlockId) {
    this.basicBlockId = basicBlockId;
  }

  public boolean isLeader() {
    return isLeader;
  }

  public void setLeader(boolean isLeader) {
    if (isLabel()) {
      return;
    }
    this.isLeader = isLeader;
  }

  public ExecFlowItem(ExecFlowBase opc, Expression expr) {
    ApplyExpressionType.applytype(expr, TypeApplierStage.stage_start);
    this.opc = opc;
    this.expr = expr;
  }

  // jmp label
  public ExecFlowItem(ExecFlowBase opc, String label) {
    this.opc = opc;
    this.label = label;
  }

  public ExecFlowItem(String label) {
    this.opc = ExecFlowBase.label;
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Expression getExpr() {
    return expr;
  }

  public void setExpr(Expression expr) {
    this.expr = expr;
  }

  public ExecFlowBase getOpc() {
    return opc;
  }

  @Override
  public String toString() {
    String id = isLeader ? "\n" + basicBlockId + ":\n" : "";
    if (opc == ExecFlowBase.jmp || opc == ExecFlowBase.jz) {
      return id + opc.toString() + " " + label;
    }
    //    if (opc == ExecFlowBase.sym) {
    //      String base = var.type.toString() + " " + var.name;
    //      String init = var.initializer == null ? "" : " = " + var.initializer.get(0).toString();
    //      return base + init;
    //    }
    if (opc == ExecFlowBase.test) {
      return id + opc.toString() + " " + expr;
    }
    if (opc == ExecFlowBase.label) {
      return id + label + ":";
    }
    if (opc == ExecFlowBase.expr) {
      return id + expr.toString();
    }
    if (opc == ExecFlowBase.ret) {
      if (expr != null) {
        return id + opc.toString() + " " + expr;
      }
      return id + opc.toString();
    }
    return super.toString();
  }

}
