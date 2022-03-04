package ast.flat.func;

import ast.flat.LinearExpression;
import ast.flat.rvalues.Var;

public class ExecFlowItem {
  final ExecFlowBase opc;
  String label;
  Cmp cmp;
  LinearExpression expr;
  Var retVar;
  boolean isLeader;
  String basicBlockId = "";

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

  public ExecFlowItem(Var retVar) {
    this.opc = ExecFlowBase.ret;
    this.retVar = retVar;
  }

  public ExecFlowItem(LinearExpression expr) {
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

  public ExecFlowItem(Cmp cmp) {
    this.opc = ExecFlowBase.cmp;
    this.cmp = cmp;
  }

  @Override
  public String toString() {
    if (opc == ExecFlowBase.jmp || opc == ExecFlowBase.je) {
      return opc.toString() + " " + label;
    }
    if (opc == ExecFlowBase.cmp) {
      return cmp.toString();
    }
    if (opc == ExecFlowBase.label) {
      return label + ":";
    }
    if (opc == ExecFlowBase.expr) {
      return expr.toString();
    }
    if (opc == ExecFlowBase.ret) {
      return opc.toString() + " " + retVar.getName();
    }
    return super.toString();
  }

}
