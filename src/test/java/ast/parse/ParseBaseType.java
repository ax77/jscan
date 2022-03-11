package ast.parse;

import static jscan.tokenize.T.TOKEN_IDENT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.attributes.AttributesAsmsLists;
import ast.builders.TypeCombiner;
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
    return what.is(TOKEN_IDENT) && !what.isBuiltinIdent();
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

  /// I)
  /// struct typedef_after {                     
  ///   int flag;                              
  /// } typedef typedef_after, *p_typedef_after; 
  ///
  /// II)
  /// typedef int i32;          
  /// typedef i32 i32;          
  /// int signed typedef i32;   
  /// i32 typedef i32;          
  /// typedef i32 dword;     
  /// typedef i32 i32, i32arr5[5], (*i32func)();
  ///
  /// III)
  /// typedef signed int t;
  /// typedef int plain;   
  /// struct tag {         
  ///     unsigned t:4; // `t` is a name   
  ///     const t:5;    // `t` is a type, unnamed bitfied (const int:5;)    
  ///     plain r:5;    // `r` is a name   
  /// };                   

  private CType findTypeAgainAgain() {
    attributes = new ParseAttributesAsms(parser).parse();

    CType result = null;
    List<Token> storage = new ArrayList<Token>();
    List<Token> specifiers = new ArrayList<Token>();
    Set<Token> qualifiers = new HashSet<Token>();
    Set<Token> funcspecs = new HashSet<Token>();

    while (isDeclSpecStart(parser.tok()) || Pcheckers.isAttributeStartGnuc(parser.tok())) {

      if (Pcheckers.isAttributeStartGnuc(parser.tok())) {
        attributes = new ParseAttributesAsms(parser).parse();
      }

      final Token tok = parser.tok();

      // typedef-routine
      // we found an identifier, it may be a var-name, or a typedef-alias
      if (isUserDefinedId(tok)) {

        // if we've already found a type, or we have some type-specs in a list - this 
        // identifier must be a variable name, not a typedefed-name.
        final boolean isVarName = hasResultOrSpecs(result, specifiers);
        if (isVarName) {
          break;
        }

        // we'he got an identifier, which may be a typedefed-alias
        final CType typedefed = parser.getTypedefName(tok.getIdent());
        if (typedefed != null) {
          result = typedefed;
          parser.move(); // move the identifier, and assign the type we've found by this id.
        } else {
          // specifiers-list is empty, and we have an identifier.
          // it means - that the type-specifier is omitted, and the default type is 'int'
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
        checkResultAndSpecsAreEmpty(result, specifiers);

        boolean isUnion = (tok.isIdent(Keywords.union_ident));
        parser.move();

        if (Pcheckers.isAttributeStartGnuc(parser.tok())) {
          attributes = new ParseAttributesAsms(parser).parse();
        }

        result = new ParseStruct(parser, isUnion).parse();
      }

      else if (Pcheckers.isEnumSpecStart(tok)) {
        checkResultAndSpecsAreEmpty(result, specifiers);
        parser.move();

        if (Pcheckers.isAttributeStartGnuc(parser.tok())) {
          attributes = new ParseAttributesAsms(parser).parse();
        }

        result = new ParseEnum3(parser).parse();
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

  private boolean hasResultOrSpecs(CType result, List<Token> specifiers) {
    if (result != null) {
      return true;
    }
    if (specifiers.size() > 0) {
      return true;
    }
    return false;
  }

  private void checkResultAndSpecsAreEmpty(CType result, List<Token> specifiers) {
    if (hasResultOrSpecs(result, specifiers)) {
      parser.perror("incorrect combination between primitive and compound type-specifiers");
    }
  }

  public CStorageKind getStorageSpec() {
    return storageSpec;
  }

  public AttributesAsmsLists getAttributes() {
    return attributes;
  }

}
