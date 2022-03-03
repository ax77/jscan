package ast.flat.ir;

//@formatter:off
public enum Opc {  
    AssignVarBinop                     // type a = b + c
  , AssignVarFieldAccess               // type a = b.c
  , AssignVarFlatCallResult            // type a = f(b, c)
  , AssignVarNum                       // type a = 1
  , AssignVarUnop                      // type a = -b
  , AssignVarVar                       // type a = b
  , AssignVarCastExpression            // cast(expression : to_this_type) -> cast(32:char)
  , FlatCallVoid                       // f(a, t1, t2) -> void
  , StoreFieldLiteral // a.b = 1 | a.b = true | a.b = default(int) | a.b = c
  , StoreVarLiteral   // a = 1   | a = true   | a = default(int)   | a = b 
}
