package cc.types;

import java.util.HashMap;
import java.util.Map;

import jscan.literals.IntLiteralType;

public abstract class CTypeImpl {

  public static final CType TYPE_VOID = new CType(CTypeKind.TP_VOID);
  public static final CType TYPE_BOOL = new CType(CTypeKind.TP_BOOL);
  public static final CType TYPE_CHAR = new CType(CTypeKind.TP_CHAR);
  public static final CType TYPE_UCHAR = new CType(CTypeKind.TP_UCHAR);
  public static final CType TYPE_SHORT = new CType(CTypeKind.TP_SHORT);
  public static final CType TYPE_USHORT = new CType(CTypeKind.TP_USHORT);
  public static final CType TYPE_INT = new CType(CTypeKind.TP_INT);
  public static final CType TYPE_UINT = new CType(CTypeKind.TP_UINT);
  public static final CType TYPE_LONG = new CType(CTypeKind.TP_LONG);
  public static final CType TYPE_ULONG = new CType(CTypeKind.TP_ULONG);
  public static final CType TYPE_LONG_LONG = new CType(CTypeKind.TP_LONG_LONG);
  public static final CType TYPE_ULONG_LONG = new CType(CTypeKind.TP_ULONG_LONG);
  public static final CType TYPE_FLOAT = new CType(CTypeKind.TP_FLOAT);
  public static final CType TYPE_DOUBLE = new CType(CTypeKind.TP_DOUBLE);
  public static final CType TYPE_LONG_DOUBLE = new CType(CTypeKind.TP_LONG_DOUBLE);

  //TODO:
  public static Map<IntLiteralType, CType> bindings = new HashMap<IntLiteralType, CType>();
  static {
    bindings.put(IntLiteralType.I32, TYPE_INT);
    bindings.put(IntLiteralType.U32, TYPE_UINT);
    bindings.put(IntLiteralType.I64, TYPE_LONG);
    bindings.put(IntLiteralType.U64, TYPE_ULONG);
    //bindings.put(IntLiteralType.I64, TYPE_LONG_LONG);
    //bindings.put(IntLiteralType.U64, TYPE_ULONG_LONG);
    bindings.put(IntLiteralType.F32, TYPE_FLOAT);
    bindings.put(IntLiteralType.F64, TYPE_DOUBLE);
    //bindings.put(IntLiteralType.F64, TYPE_LONG_DOUBLE);
  }

  public static final int QCONST = 1 << 0;
  public static final int QRESTR = 1 << 1;
  public static final int QVOLAT = 1 << 2;
  public static final int FINLIN = 1 << 3;
  public static final int FNORET = 1 << 4;

}
