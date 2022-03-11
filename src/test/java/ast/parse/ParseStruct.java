package ast.parse;

import static jscan.tokenize.T.TOKEN_IDENT;
import static jscan.tokenize.T.T_COLON;
import static jscan.tokenize.T.T_SEMI_COLON;

import java.util.ArrayList;
import java.util.List;

import ast.attributes.AttributesAsmsLists;
import ast.builders.ApplyStructInfo;
import ast.builders.ConstexprEval;
import ast.builders.SemanticBitfield;
import ast.builders.TypeMerger;
import ast.symtab.CSymbol;
import ast.symtab.CSymbolBase;
import ast.tree.Declarator;
import ast.tree.Expression;
import ast.types.CStructField;
import ast.types.CStructType;
import ast.types.CType;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;

//TODO:rewrite

public class ParseStruct {
  private final Parse parser;
  private final boolean isUnion;

  public ParseStruct(Parse parser, boolean isUnion) {
    this.parser = parser;
    this.isUnion = isUnion;
  }

  private ApplyStructInfo finalizeStructType(CStructType tpStruct) {
    if (!tpStruct.isComplete) {
      parser.unimplemented("incomplete struct finalization");
    }
    final ApplyStructInfo sinfo = new ApplyStructInfo(tpStruct.isUnion, tpStruct.getFields());

    int i = 0;
    for (CStructField f : tpStruct.getFields()) {
      f.pos = i;
      i += 1;
    }
    return sinfo;
  }

  // TODO: incomplete fields

  public CType parse() {
    //struct ...
    //       ^

    boolean iscorrect = parser.isUserDefinedId() || parser.is(T.T_LEFT_BRACE);
    if (!iscorrect) {
      parser.perror("expect identifier or { for enum type-specifier");
    }

    @SuppressWarnings("unused")
    AttributesAsmsLists attributesAsmsLists = new ParseAttributesAsms(parser).parse();

    Token tag = null;
    if (parser.is(TOKEN_IDENT)) {
      tag = parser.tok();
      parser.move();
    }

    if (tag != null) {
      final CType result = parseStructWithPresentedTag(tag);
      return result;
    }

    final CType result = parseStructWithNoTag();
    return result;

  }

  //////////////////////////////////////////////
  //struct_declaration_list
  //  : struct_declaration
  //  | struct_declaration_list struct_declaration
  //  ;
  //
  //struct_declaration
  //  : specifier_qualifier_list ';'  /* for anonymous struct/union */
  //  | specifier_qualifier_list struct_declarator_list ';'
  //  | static_assert_declaration
  //  ;

  private CType parseStructWithPresentedTag(Token from) {
    Ident name = from.getIdent();

    if (parser.tp() == T.T_SEMI_COLON) {
      return incompleteType(from, name);
    }

    CType type = null;

    CSymbol sym = parser.getTagFromCurrentScope(name);
    if (sym == null && (parser.tp() != T.T_LEFT_BRACE) && parser.getTags().isBlockScope()) {
      sym = parser.getTag(name);
    }
    if (sym != null) {
      type = sym.type;
    }

    if (type == null) {
      type = incompleteType(from, name);
    }

    if (parser.tp() == T.T_LEFT_BRACE) {

      List<CStructField> fields = parseFields(parser);
      type.tpStruct.setFields(fields);

      ApplyStructInfo sizeAlignDto = finalizeStructType(type.tpStruct);

      type.setSize(sizeAlignDto.getSize());
      type.setAlign(sizeAlignDto.getAlign());

    }

    return type;
  }

  private CType incompleteType(Token from, Ident tagId) {

    CStructType incomplete = new CStructType(isUnion, tagId);
    CType type = new CType(incomplete, -1, -1);

    CSymbol structSymbol = new CSymbol(CSymbolBase.SYM_STRUCT_DECLARATION, tagId, type, from);
    parser.defineTag(structSymbol);

    return type;
  }

  private CType parseStructWithNoTag() {
    if (parser.tp() != T.T_LEFT_BRACE) {
      parser.perror("expect '{' for struct with no tag");
    }

    CStructType newstruct = new CStructType(isUnion, null);

    List<CStructField> fields = parseFields(parser);
    newstruct.setFields(fields);

    ApplyStructInfo sizeAlignDto = finalizeStructType(newstruct);
    CType type = new CType(newstruct, sizeAlignDto.getSize(), sizeAlignDto.getAlign());

    return type;
  }

  private List<CStructField> parseFields(Parse parser) {
    parser.checkedMove(T.T_LEFT_BRACE);
    List<CStructField> structDeclarationList = new ArrayList<CStructField>(0);

    // struct S {};
    //           ^
    if (parser.tp() == T.T_RIGHT_BRACE) {
      parser.pwarning("empty struct declaration list");

      parser.checkedMove(T.T_RIGHT_BRACE); // TODO:location
      return structDeclarationList;
    }

    List<CStructField> structDeclaration = parseStructDeclaration();
    structDeclarationList.addAll(structDeclaration);

    while (parser.isDeclSpecStart()) {
      List<CStructField> structDeclarationSeq = parseStructDeclaration();
      structDeclarationList.addAll(structDeclarationSeq);
    }

    parser.checkedMove(T.T_RIGHT_BRACE);
    return structDeclarationList;
  }

  //struct_declaration
  //  : specifier_qualifier_list ';'  /* for anonymous struct/union */
  //  | specifier_qualifier_list struct_declarator_list ';'
  //  | static_assert_declaration
  //  ;

  private List<CStructField> parseStructDeclaration() {

    List<CStructField> r = new ArrayList<CStructField>(0);

    // static_assert
    //
    boolean skip = new ParseStaticAssert(parser).isStaticAssertAndItsOk();
    if (skip) {
      return r;
    }

    // TODO: this is spec-qual
    // no storage here...
    CType basetype = new ParseBaseType(parser).parse();

    if (parser.tp() == T_SEMI_COLON) {
      parser.move();

      /// struct some {                                             
      ///     enum toktype {                                        
      ///         t_ident, t_string,                                
      ///     };   // warning: declaration does not declare anything
      ///     int; // warning: declaration does not declare anything
      ///     int field;                                            
      /// };                                                        

      // TODO:XXX: fields offset, if it's from anonymous UNION...

      boolean isStructUnionEnum = basetype.isStrUnion() || basetype.isEnumeration();
      if (isStructUnionEnum) {

        if (basetype.isEnumeration()) {
          return r;
        }

        boolean isAnonymousDeclaration = !basetype.tpStruct.hasTag();
        if (isAnonymousDeclaration) {
          List<CStructField> fieldsInside = basetype.tpStruct.getFields();
          r.addAll(fieldsInside);
          return r;
        } else {
          parser.pwarning("declaration does not declare anything");
          return r; // TODO: empty now ?
        }

      }

      return r;

    }

    // otherwise declarator-list like: [int a,b,c;]
    parseStructDeclaratorList(r, basetype);
    parser.checkedMove(T_SEMI_COLON);

    return r;
  }

  //struct_declarator_list
  //  : struct_declarator
  //  | struct_declarator_list ',' struct_declarator
  //  ;
  //
  //struct_declarator
  //  : ':' constant_expression
  //  | declarator ':' constant_expression
  //  | declarator
  //  ;

  private void parseStructDeclaratorList(List<CStructField> out, CType specqual) {

    CStructField structDeclarator = parseStructDeclarator(parser, specqual);

    if (structDeclarator.type.isIncomplete()) {
      parser.perror("incomplete struct field");
    }

    out.add(structDeclarator);

    while (parser.tp() == T.T_COMMA) {
      parser.move();
      CStructField structDeclaratorSeq = parseStructDeclarator(parser, specqual);

      if (structDeclaratorSeq.type.isIncomplete()) {
        parser.perror("incomplete struct field");
      }

      out.add(structDeclaratorSeq);
    }

  }

  //struct_declarator
  //  : ':' constant_expression
  //  | declarator ':' constant_expression
  //  | declarator
  //  ;

  private CStructField parseStructDeclarator(Parse parser, CType base) {

    // unnamed-bit-field
    //
    if (parser.tp() == T_COLON) {
      parser.move();

      Expression consterpr = new ParseExpression(parser).e_const_expr();
      int width = (int) new ConstexprEval(parser).ce(consterpr);

      final CType bf = new SemanticBitfield(parser).buildBitfield(base, width);
      final CStructField unnamedBitfield = new CStructField(bf);
      return unnamedBitfield;
    }

    // need normal field or named bit-field

    Declarator decl = new ParseDeclarator(parser).parse();
    CType type = TypeMerger.build(base, decl);

    // named-bit-field
    //
    if (parser.tp() == T_COLON) {
      parser.move();

      Expression consterpr = new ParseExpression(parser).e_const_expr();
      int width = (int) new ConstexprEval(parser).ce(consterpr);

      final CType bf = new SemanticBitfield(parser).buildBitfield(type, width);
      final CStructField namedBitfield = new CStructField(decl.getName(), bf);
      return namedBitfield;
    }

    // plain-field
    //
    final CStructField plainField = new CStructField(decl.getName(), type);
    return plainField;
  }
}
