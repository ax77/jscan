package cc.builders;

import java.util.HashMap;
import java.util.Map;

import cc.types.CTypeKind;

public abstract class TypePrinter {

  private static Map<CTypeKind, String> types = new HashMap<CTypeKind, String>();
  static {
    //@formatter:off
    types.put(CTypeKind.TP_VOID                     , "void                   ");
    types.put(CTypeKind.TP_BOOL                     , "_Bool                  ");
    types.put(CTypeKind.TP_CHAR                     , "char                   ");
    types.put(CTypeKind.TP_UCHAR                    , "unsigned char          ");
    types.put(CTypeKind.TP_SHORT                    , "short                  ");
    types.put(CTypeKind.TP_USHORT                   , "unsigned short         ");
    types.put(CTypeKind.TP_INT                      , "int                    ");
    types.put(CTypeKind.TP_UINT                     , "unsigned int           ");
    types.put(CTypeKind.TP_LONG                     , "long                   ");
    types.put(CTypeKind.TP_ULONG                    , "unsigned long          ");
    types.put(CTypeKind.TP_LONG_LONG                , "long long              ");
    types.put(CTypeKind.TP_ULONG_LONG               , "unsigned long long     ");
    types.put(CTypeKind.TP_FLOAT                    , "float                  ");
    types.put(CTypeKind.TP_DOUBLE                   , "double                 ");
    types.put(CTypeKind.TP_LONG_DOUBLE              , "long double            ");
    types.put(CTypeKind.TP_FLOAT_IMAGINARY          , "float _Imaginary       ");
    types.put(CTypeKind.TP_DOUBLE_IMAGINARY         , "double _Imaginary      ");
    types.put(CTypeKind.TP_LONG_DOUBLE_IMAGINARY    , "long double _Imaginary ");
    types.put(CTypeKind.TP_FLOAT_COMPLEX            , "float _Complex         ");
    types.put(CTypeKind.TP_DOUBLE_COMPLEX           , "double _Complex        ");
    types.put(CTypeKind.TP_LONG_DOUBLE_COMPLEX      , "long double _Complex   ");
    //@formatter:on
  }

  public static String primitiveToString(CTypeKind kind) {
    return types.get(kind).trim();
  }

}
