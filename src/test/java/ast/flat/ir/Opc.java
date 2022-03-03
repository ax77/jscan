package ast.flat.ir;

//@formatter:off
public enum Opc {  
    AssignVarBinop            // type a = b + c
  , AssignVarFieldAccess      // type a = b.c
  , AssignVarFlatCallResult   // type a = f(b, c)
  , AssignVarNum              // type a = 1
  , AssignVarUnop             // type a = -b
  , AssignVarVar              // type a = b
  , AssignVarCastExpression   // type a = (type) b
  , FlatCallVoid              // f(a, t1, t2) -> void
  , StoreFieldLiteral         // a.b = 1 | a.b = c
  , StoreVarLiteral           // a = 1   | a = b 
}
