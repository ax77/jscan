package ast.parse;

import static jscan.tokenize.T.TOKEN_IDENT;

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
import jscan.utils.NullChecker;

public class ParseBaseType {
  private final Parse parser;
  private CStorageKind storageSpec;
  private AttributesAsmsLists attributes;

  public ParseBaseType(Parse p) {
    this.parser = p;
    this.storageSpec = CStorageKind.ST_NONE;
  }

  public CType parse() {
    //return findTypeAgain();
    return findTypeAgainAgain();
  }

  //@formatter:off
  public boolean isUserDefinedId(Token what) {
    return what.ofType(TOKEN_IDENT) && !what.isBuiltinIdent();
  }
  
  public boolean isDeclSpecStart(Token what) {
    return Pcheckers.isStorageClassSpec(what)
        || Pcheckers.isTypeSpec(what)
        || Pcheckers.isTypeQual(what)
        || Pcheckers.isFuncSpec(what)
        || Pcheckers.isEnumSpecStart(what)
        || Pcheckers.isStructOrUnionSpecStart(what)
        || Pcheckers.isStaticAssert(what)
        || isUserDefinedId(what);
  }
  //@formatter:on

  private CType findTypeAgainAgain() {
    attributes = new ParseAttributesAsms(parser).parse();

    CType result = null;
    List<Token> storage = new ArrayList<Token>();
    List<Token> specifiers = new ArrayList<Token>();
    Set<Token> qualifiers = new HashSet<Token>();
    Set<Token> funcspecs = new HashSet<Token>();

    while (isDeclSpecStart(parser.tok()) || Pcheckers.isAttributeStartGnuc(parser.tok())) {

      final Token tok = parser.tok();

      if (Pcheckers.isAttributeStartGnuc(parser.tok())) {
        attributes = new ParseAttributesAsms(parser).parse();
      }

      // we found an identifier, it may be a var-name, or a typedef-alias
      if (isUserDefinedId(tok)) {
        if (result == null && specifiers.isEmpty()) {
          CSymbol symbol = parser.getSym(tok.getIdent());

          if (symbol != null) {
            CType typeFromStab = symbol.getType();
            if (symbol.getBase() == CSymbolBase.SYM_TYPEDEF) {
              parser.move();
              result = typeFromStab;
            }
          } else {
            break;
          }
        } else {
          // it's a var-name, we should break the loop
          break;
        }
      } else if (Pcheckers.isStorageClassSpec(tok)) {
        storage.add(parser.moveget());
      } else if (Pcheckers.isTypeSpec(tok)) {
        specifiers.add(parser.moveget());
      } else if (Pcheckers.isTypeQual(tok)) {
        qualifiers.add(parser.moveget());
      } else if (Pcheckers.isFuncSpec(tok)) {
        funcspecs.add(parser.moveget());
      }

      else if (Pcheckers.isStructOrUnionSpecStart(tok)) {
        boolean isUnion = (tok.isIdent(Keywords.union_ident));
        parser.move();

        if (Pcheckers.isAttributeStartGnuc(parser.tok())) {
          attributes = new ParseAttributesAsms(parser).parse();
        }

        result = new ParseStruct(parser, isUnion).parse();
      }

      else if (Pcheckers.isEnumSpecStart(tok)) {
        parser.move();

        if (Pcheckers.isAttributeStartGnuc(parser.tok())) {
          attributes = new ParseAttributesAsms(parser).parse();
        }

        result = new ParseEnum(parser).parse();
      }
    }

    storageSpec = TypeCombiner.combine_storage(storage);

    if (!specifiers.isEmpty()) {
      if (result != null) {
        parser.perror("incorrect combination between primitive/compound types");
      }
      CTypeKind bts = TypeCombiner.combine_typespec(specifiers);
      result = new CType(bts);
    }

    if (result == null) {
      parser.pwarning("type is not specified, the default type is int.");
      result = CTypeImpl.TYPE_INT;
    }

    NullChecker.check(result);
    return result;
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
        final CType enumTypeSpec = new ParseEnum(parser).parse();
        return enumTypeSpec;
      }

      else {
        boolean isUnion = (first.isIdent(Keywords.union_ident));
        final CType strUnionTypeSpec = new ParseStruct(parser, isUnion).parse();
        return strUnionTypeSpec;
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

  public AttributesAsmsLists getAttributes() {
    return attributes;
  }

}
