package ast.builders;

import ast.parse.Parse;
import ast.types.CBitfieldType;
import ast.types.CType;
import ast.types.CTypeKind;
import ast.types.TypeSizes;

public class SemanticBitfield {
  private final Parse parser;

  public SemanticBitfield(Parse parser) {
    this.parser = parser;
  }

  public CType buildBitfield(CType base, int width) {

    if (width < 0) {
      parser.perror("negative bitfield-width");
    }

    CTypeKind kind = base.getKind();

    // TODO: warning about enum
    // 
    if (!isPrimitiveInteger(kind)) {
      parser.perror("bitfield type error.");
    }

    int maxbits = TypeSizes.get(kind) * 8; // TODO: settings __CHAR_BIT__
    if (width > maxbits) {
      parser.perror("width exceeds its type");
    }

    CBitfieldType bf = new CBitfieldType(base, width);
    return new CType(bf);
  }

  private boolean isPrimitiveInteger(CTypeKind kind) {
    return kind == CTypeKind.TP_CHAR || kind == CTypeKind.TP_UCHAR || kind == CTypeKind.TP_SHORT
        || kind == CTypeKind.TP_USHORT || kind == CTypeKind.TP_INT || kind == CTypeKind.TP_UINT
        || kind == CTypeKind.TP_LONG || kind == CTypeKind.TP_ULONG || kind == CTypeKind.TP_LONG_LONG
        || kind == CTypeKind.TP_ULONG_LONG;
  }

}
