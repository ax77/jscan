package ast.types.util;

import java.util.HashMap;
import java.util.Map;

import ast.types.main.CTypeKind;

public abstract class TypeSizes {

  //@formatter:off
  private static Map<CTypeKind, Integer> BSIZES = new HashMap<CTypeKind, Integer>();
  static {
    BSIZES.put(CTypeKind.TP_VOID           , 1);
    BSIZES.put(CTypeKind.TP_BOOL           , 1);
    BSIZES.put(CTypeKind.TP_CHAR           , 1);
    BSIZES.put(CTypeKind.TP_UCHAR          , 1);
    BSIZES.put(CTypeKind.TP_SHORT          , 2);
    BSIZES.put(CTypeKind.TP_USHORT         , 2);
    BSIZES.put(CTypeKind.TP_INT            , 4);
    BSIZES.put(CTypeKind.TP_UINT           , 4);
    BSIZES.put(CTypeKind.TP_LONG           , 8);
    BSIZES.put(CTypeKind.TP_ULONG          , 8);
    BSIZES.put(CTypeKind.TP_LONG_LONG      , 8);
    BSIZES.put(CTypeKind.TP_ULONG_LONG     , 8);
    BSIZES.put(CTypeKind.TP_FLOAT          , 4);
    BSIZES.put(CTypeKind.TP_DOUBLE         , 8);
    BSIZES.put(CTypeKind.TP_LONG_DOUBLE    , 16);
    
    BSIZES.put(CTypeKind.TP_FUNCTION       , 1);
    BSIZES.put(CTypeKind.TP_ENUM           , 4);
    BSIZES.put(CTypeKind.TP_POINTER_TO     , 8);
  }
  //@formatter:on

  public static int get(CTypeKind b) {
    return BSIZES.get(b);
  }
}