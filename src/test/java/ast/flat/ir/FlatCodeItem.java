package ast.flat.ir;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ast.flat.items.AssignVarBinop;
import ast.flat.items.AssignVarFieldAccess;
import ast.flat.items.AssignVarFlatCallResult;
import ast.flat.items.AssignVarNum;
import ast.flat.items.AssignVarUnop;
import ast.flat.items.AssignVarVar;
import ast.flat.items.FlatCallVoid;
import ast.flat.items.StoreFieldLiteral;
import ast.flat.items.StoreVarLiteral;
import ast.flat.rvalues.Var;
import jscan.utils.AstParseException;

public class FlatCodeItem {
  private final Opc opcode;

  //generated code begin
  //@formatter:off
  private AssignVarBinop assignVarBinop;
  private AssignVarFieldAccess assignVarFieldAccess;
  private AssignVarFlatCallResult assignVarFlatCallResult;
  private AssignVarNum assignVarNum;
  private AssignVarUnop assignVarUnop;
  private AssignVarVar assignVarVar;
  private FlatCallVoid flatCallVoid;
  private StoreFieldLiteral storeFieldLiteral;
  private StoreVarLiteral storeVarLiteral;


  private final String uid;
  private boolean ignore;
  private Var rvalueDestWrapper;
  public boolean isIgnore() {
    return ignore;
  }
  public void setIgnore(Var rvalueDestWrapper) { // this dest is only for returns, the result
    this.ignore = true;
    this.rvalueDestWrapper = rvalueDestWrapper;
  }
  private String genUid() {
    return UUID.randomUUID().toString();
  }
  @Override
  public int hashCode() {
    return Objects.hash(uid);
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FlatCodeItem other = (FlatCodeItem) obj;
    return Objects.equals(uid, other.uid);
  }

  public FlatCodeItem(AssignVarBinop assignVarBinop) { this.uid = genUid(); this.opcode = Opc.AssignVarBinop; this.assignVarBinop = assignVarBinop; }
  public FlatCodeItem(AssignVarFieldAccess assignVarFieldAccess) { this.uid = genUid(); this.opcode = Opc.AssignVarFieldAccess; this.assignVarFieldAccess = assignVarFieldAccess; }
  public FlatCodeItem(AssignVarFlatCallResult assignVarFlatCallResult) { this.uid = genUid(); this.opcode = Opc.AssignVarFlatCallResult; this.assignVarFlatCallResult = assignVarFlatCallResult; }
  public FlatCodeItem(AssignVarNum assignVarNum) { this.uid = genUid(); this.opcode = Opc.AssignVarNum; this.assignVarNum = assignVarNum; }
  public FlatCodeItem(AssignVarUnop assignVarUnop) { this.uid = genUid(); this.opcode = Opc.AssignVarUnop; this.assignVarUnop = assignVarUnop; }
  public FlatCodeItem(AssignVarVar assignVarVar) { this.uid = genUid(); this.opcode = Opc.AssignVarVar; this.assignVarVar = assignVarVar; }
  public FlatCodeItem(FlatCallVoid flatCallVoid) { this.uid = genUid(); this.opcode = Opc.FlatCallVoid; this.flatCallVoid = flatCallVoid; }
  public FlatCodeItem(StoreFieldLiteral storeFieldLiteral) { this.uid = genUid(); this.opcode = Opc.StoreFieldLiteral; this.storeFieldLiteral = storeFieldLiteral; }
  public FlatCodeItem(StoreVarLiteral storeVarLiteral) { this.uid = genUid(); this.opcode = Opc.StoreVarLiteral; this.storeVarLiteral = storeVarLiteral; }

  public boolean isAssignVarBinop() { return this.opcode == Opc.AssignVarBinop; }
  public boolean isAssignVarCastExpression() { return this.opcode == Opc.AssignVarCastExpression; }
  public boolean isAssignVarFieldAccess() { return this.opcode == Opc.AssignVarFieldAccess; }
  public boolean isAssignVarFlatCallResult() { return this.opcode == Opc.AssignVarFlatCallResult; }
  public boolean isAssignVarNum() { return this.opcode == Opc.AssignVarNum; }
  public boolean isAssignVarUnop() { return this.opcode == Opc.AssignVarUnop; }
  public boolean isAssignVarVar() { return this.opcode == Opc.AssignVarVar; }
  public boolean isFlatCallVoid() { return this.opcode == Opc.FlatCallVoid; }
  public boolean isStoreFieldLiteral() { return this.opcode == Opc.StoreFieldLiteral; }
  public boolean isStoreVarLiteral() { return this.opcode == Opc.StoreVarLiteral; }

  public List<Var> getAllVars() {
    if ( isAssignVarBinop() ) { return assignVarBinop.getAllVars(); }
    if ( isAssignVarFieldAccess() ) { return assignVarFieldAccess.getAllVars(); }
    if ( isAssignVarFlatCallResult() ) { return assignVarFlatCallResult.getAllVars(); }
    if ( isAssignVarNum() ) { return assignVarNum.getAllVars(); }
    if ( isAssignVarUnop() ) { return assignVarUnop.getAllVars(); }
    if ( isAssignVarVar() ) { return assignVarVar.getAllVars(); }
    if ( isFlatCallVoid() ) { return flatCallVoid.getAllVars(); }
    if ( isStoreFieldLiteral() ) { return storeFieldLiteral.getAllVars(); }
    if ( isStoreVarLiteral() ) { return storeVarLiteral.getAllVars(); }
    err();
    return null;
  }

  public Var getLvalue() {
    if ( isAssignVarBinop() ) { return assignVarBinop.getLvalue(); }
    if ( isAssignVarFieldAccess() ) { return assignVarFieldAccess.getLvalue(); }
    if ( isAssignVarFlatCallResult() ) { return assignVarFlatCallResult.getLvalue(); }
    if ( isAssignVarNum() ) { return assignVarNum.getLvalue(); }
    if ( isAssignVarUnop() ) { return assignVarUnop.getLvalue(); }
    if ( isAssignVarVar() ) { return assignVarVar.getLvalue(); }
    err();
    return null;
  }

  @Override
  public String toString() {
    if(ignore) { return ""; }
    if(isAssignVarBinop()) { return assignVarBinop.toString(); }
    if(isAssignVarFieldAccess()) { return assignVarFieldAccess.toString(); }
    if(isAssignVarFlatCallResult()) { return assignVarFlatCallResult.toString(); }
    if(isAssignVarNum()) { return assignVarNum.toString(); }
    if(isAssignVarUnop()) { return assignVarUnop.toString(); }
    if(isAssignVarVar()) { return assignVarVar.toString(); }
    if(isFlatCallVoid()) { return flatCallVoid.toString(); }
    if(isStoreFieldLiteral()) { return storeFieldLiteral.toString(); }
    if(isStoreVarLiteral()) { return storeVarLiteral.toString(); }
    return "?UnknownItem"; 
  }

  public Opc getOpcode() { return this.opcode; }
  public AssignVarBinop getAssignVarBinop() { return this.assignVarBinop; }
  public AssignVarFieldAccess getAssignVarFieldAccess() { return this.assignVarFieldAccess; }
  public AssignVarFlatCallResult getAssignVarFlatCallResult() { return this.assignVarFlatCallResult; }
  public AssignVarNum getAssignVarNum() { return this.assignVarNum; }
  public AssignVarUnop getAssignVarUnop() { return this.assignVarUnop; }
  public AssignVarVar getAssignVarVar() { return this.assignVarVar; }
  public FlatCallVoid getFlatCallVoid() { return this.flatCallVoid; }
  public StoreFieldLiteral getStoreFieldLiteral() { return this.storeFieldLiteral; }
  public StoreVarLiteral getStoreVarLiteral() { return this.storeVarLiteral; }


  public boolean isOneOfAssigns() {
      return 
         isAssignVarBinop()  
      || isAssignVarFieldAccess() 
      || isAssignVarFlatCallResult()
      || isAssignVarNum() 
      || isAssignVarUnop()   
      || isAssignVarVar()
      || isAssignVarCastExpression() 
      ;
  }

  public Var getDest() {
    if(ignore) { return rvalueDestWrapper; }
    if(isAssignVarBinop()) { return assignVarBinop.getLvalue(); }
    if(isAssignVarFieldAccess()) { return assignVarFieldAccess.getLvalue(); }
    if(isAssignVarFlatCallResult()) { return assignVarFlatCallResult.getLvalue(); }
    if(isAssignVarNum()) { return assignVarNum.getLvalue(); }
    if(isAssignVarUnop()) { return assignVarUnop.getLvalue(); }
    if(isAssignVarVar()) { return assignVarVar.getLvalue(); }
    if(isFlatCallVoid()) { err(); }
    if(isStoreFieldLiteral()) { err(); }
    if(isStoreVarLiteral()) { err(); }
    throw new AstParseException("unknown item for result: " + toString());
  }
  private void err() { throw new AstParseException("unexpected item for result: " + toString()); }
  //@formatter:on
  //generated code end

}
