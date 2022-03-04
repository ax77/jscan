package ast.flat;

import ast.symtab.CSymbol;
import ast.tree.Expression;

public class ExecFlowItem {
  final ExecFlowBase opc;
  String label;
  Expression expr;
  CSymbol var;
  boolean isLeader;
  String basicBlockId = "";

  public ExecFlowItem(CSymbol var) {
    this.opc = ExecFlowBase.sym;
    this.var = var;
  }

  public boolean isAnyJmp() {
    return opc == ExecFlowBase.jmp || opc == ExecFlowBase.je;
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

  public ExecFlowItem(Expression expr) {
    this.opc = ExecFlowBase.expr;
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
    if (opc == ExecFlowBase.jmp || opc == ExecFlowBase.je) {
      return id + opc.toString() + " " + label;
    }
    if (opc == ExecFlowBase.sym) {
      String base = var.getType().toString() + " " + var.getName();
      String init = var.getInitializer() == null ? "" : " = " + var.getInitializer().get(0).toString();
      return base + init;
    }
    if (opc == ExecFlowBase.cmp) {
    }
    if (opc == ExecFlowBase.label) {
      return id + label + ":";
    }
    if (opc == ExecFlowBase.expr) {
      return id + expr.toString();
    }
    if (opc == ExecFlowBase.ret) {
    }
    return super.toString();
  }

}
