package ast.parse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.attributes.AttributesAsmsLists;
import ast.builders.TypeCombiner;
import ast.symtab.CSymbol;
import ast.symtab.CSymbolBase;
import ast.types.CStorageKind;
import ast.types.CType;
import ast.types.CTypeImpl;
import ast.types.CTypeKind;
import jscan.symtab.Keywords;
import jscan.tokenize.Token;

public class ParseBaseType {
  private final Parse parser;
  private CStorageKind storageSpec;
  private AttributesAsmsLists attributes;

  public ParseBaseType(Parse p) {
    this.parser = p;
    this.storageSpec = CStorageKind.ST_NONE;
  }

  public CType parse() {
    return findTypeAgain();
  }

  private CType findTypeAgain() {

    attributes = new ParseAttributesAsms(parser).parse();

    List<Token> storage = new ArrayList<Token>();
    List<Token> compoundKeywords = new ArrayList<Token>();
    Set<Token> qualifiers = new HashSet<Token>();
    cut(storage, compoundKeywords, qualifiers);

    // new storage present always...
    storageSpec = TypeCombiner.combine_storage(storage);

    // const typedef struct x tdname;
    // typedef const struct x tdname;
    // struct ...
    // enum  ...
    // union ...

    // if found struct/union/enum with|without typedef: one case
    // if not found: another case
    // if found typedefed-alias from symtab: another-another case
    // if found one more one variant: another-another-another case ... 

    // 1) compound
    if (!compoundKeywords.isEmpty()) {
      Token first = compoundKeywords.remove(0);
      if (first.isIdent(Keywords.enum_ident)) {
        return new ParseEnum(parser).parse();
      }

      else {
        boolean isUnion = (first.isIdent(Keywords.union_ident));
        return new ParseStruct(parser, isUnion).parse();
      }
    }

    if (Pcheckers.isTypeSpec(parser.tok())) {
      // int typedef i32;
      // int x;
      // int const static x;
      // int const typedef i32;
      // ... ... ...
      //

      List<Token> ts = new ArrayList<Token>();
      cut2(storage, ts, qualifiers);
      storageSpec = TypeCombiner.combine_storage(storage);

      CTypeKind bts = TypeCombiner.combine_typespec(ts);
      CType basetype = new CType(bts);

      return basetype;
    }

    // if we here: it guarantee us that the typedef-name must be present.
    // because if we are here: we still not found the type...
    // but: it also may be a typedef-redeclaration (1) or typedef-usage (2):
    // 1) i32 typedef i32;
    // 2) i32 varname;
    // i32 int ... :: error
    //

    if (parser.isUserDefinedId()) {
      CSymbol symbol = parser.getSym(parser.tok().getIdent());
      if (symbol != null) {
        CType typeFromStab = symbol.getType();
        if (symbol.getBase() == CSymbolBase.SYM_TYPEDEF) {
          parser.move();

          List<Token> ts = new ArrayList<Token>();
          cut2(storage, ts, qualifiers);
          if (!ts.isEmpty()) {
            parser.perror("error_1");
          }

          storageSpec = TypeCombiner.combine_storage(storage);
          return typeFromStab;
        }
      }
    }

    // 'int' by default
    parser.pwarning("default type-int... if type not specified.");
    return CTypeImpl.TYPE_INT;
  }

  private void cut2(List<Token> st, List<Token> ts, Set<Token> tq) {
    for (;;) {

      attributes = new ParseAttributesAsms(parser).parse();

      if (Pcheckers.isStorageClassSpec(parser.tok())) {
        Token saved = parser.tok();
        parser.move();
        st.add(saved);
      }

      else if (Pcheckers.isTypeSpec(parser.tok())) {
        Token saved = parser.tok();
        parser.move();
        ts.add(saved);
      }

      else if (Pcheckers.isTypeQual(parser.tok())) {
        Token saved = parser.tok();
        parser.move();
        tq.add(saved);
      }

      else if (Pcheckers.isFuncSpec(parser.tok())) {
        parser.move(); // TODO: inline, noreturn
      }

      else {
        break;
      }
    }
  }

  private void cut(List<Token> storage, List<Token> compoundKeywords, Set<Token> qualifiers) {
    while (!parser.isEof()) {

      attributes = new ParseAttributesAsms(parser).parse();

      if (Pcheckers.isStorageClassSpec(parser.tok())) {
        Token saved = parser.tok();
        parser.move();
        storage.add(saved);
      }

      else if (Pcheckers.isTypeQual(parser.tok())) {
        Token saved = parser.tok();
        parser.move();
        qualifiers.add(saved);
      }

      else if (Pcheckers.isFuncSpec(parser.tok())) {
        parser.move(); // TODO: inline, noreturn
      }

      else if (Pcheckers.isStructOrUnionSpecStart(parser.tok()) || Pcheckers.isEnumSpecStart(parser.tok())) {
        Token saved = parser.tok();
        parser.move();
        compoundKeywords.add(saved);
        break; // XXX: nothing else.
      }

      else {
        break;
      }
    }
  }

  public CStorageKind getStorageSpec() {
    return storageSpec;
  }

  public void setStorageSpec(CStorageKind storageSpec) {
    this.storageSpec = storageSpec;
  }

}
