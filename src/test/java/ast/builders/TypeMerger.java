package ast.builders;

import java.util.ArrayList;
import java.util.List;

import ast.tree.Declarator;
import ast.tree.DeclaratorEntry;
import ast.types.CArrayType;
import ast.types.CFunctionType;
import ast.types.CPointerType;
import ast.types.CType;
import ast.types.CTypeKind;
import jscan.symtab.Ident;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;

public abstract class TypeMerger {

  public static CType build(CType basetype, Declarator declarator) {

    CType tp = basetype;
    if (declarator == null) {
      return tp; // TODO: for unit-tests this ok, but how about reality?
    }

    final List<DeclaratorEntry> typelist = declarator.getTypelist();
    if (typelist.isEmpty()) {
      return tp;
    }

    List<CType> links = new ArrayList<CType>(0);

    for (int i = typelist.size(); --i >= 0;) {
      DeclaratorEntry entry = typelist.get(i);
      links.add(0, tp);
      tp = buildFromDeclaratorEntry(entry, links.remove(0));
    }

    return tp;
  }

  private static CType buildFromDeclaratorEntry(DeclaratorEntry e, CType type) {
    CTypeKind base = e.base;
    if (base == CTypeKind.TP_ARRAY_OF) {
      CArrayType arr = new CArrayType(type, e.arrlen);
      return new CType(arr);
    }
    if (base == CTypeKind.TP_POINTER_TO) {
      final CPointerType ptrTo = new CPointerType(type, e.isConstPointer);
      return new CType(ptrTo);
    }
    if (base == CTypeKind.TP_FUNCTION) {
      CFunctionType fn = new CFunctionType(type, e.parameters, e.isVariadicFunction);
      return new CType(fn);
    }
    throw new AstParseException("build from declarator fail: entry=" + e.base.toString() + "; type=" + type.toString());
  }

  public static void checkTagNotNullForReference(Token tag) {
    if (tag == null) {
      throw new AstParseException("for struct/union/enum reference tag must be present always");
    }
  }

  public static Ident getIdentOrNull(Token tag) {
    return tag == null ? null : tag.getIdent();
  }

}
