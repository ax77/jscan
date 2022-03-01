package ast.parse;

import static jscan.tokenize.T.TOKEN_IDENT;
import static jscan.tokenize.T.T_COLON;
import static jscan.tokenize.T.T_SEMI_COLON;

import java.util.ArrayList;
import java.util.List;

import ast.attributes.main.AttributesAsmsLists;
import ast.attributes.main.ParseAttributesAsms;
import ast.symtab.CSymbol;
import ast.symtab.CSymbolBase;
import ast.tree.ConstexprEval;
import ast.tree.Declarator;
import ast.tree.Expression;
import ast.types.CStructField;
import ast.types.CStructType;
import ast.types.CType;
import ast.types.InfoStruct;
import ast.types.SemanticBitfield;
import ast.types.TypeMerger;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseStruct {
  private final Parse parser;
  private final boolean isUnion;

  public ParseStruct(Parse parser, boolean isUnion) {
    this.parser = parser;
    this.isUnion = isUnion;
  }

  private InfoStruct finalizeStructType(CStructType tpStruct) {
    if (tpStruct.isIncomplete()) {
      parser.unimplemented("incomplete struct finalization");
    }
    return new InfoStruct(tpStruct.isUnion(), tpStruct.getFields());
  }

  // TODO: incomplete fields

  public CType parse() {
    //struct ...
    //       ^

    boolean iscorrect = parser.tok().ofType(TOKEN_IDENT) || parser.tok().ofType(T.T_LEFT_BRACE);
    if (!iscorrect) {
      parser.perror("expect identifier or { for enum type-specifier");
    }

    AttributesAsmsLists attributesAsmsLists = new ParseAttributesAsms(parser).parse();

    Token tag = null;
    if (parser.tok().ofType(TOKEN_IDENT)) {
      tag = parser.tok();
      parser.move();
    }

    if (tag != null) {
      return parseStructWithPresentedTag(tag);
    }

    return parseStructWithNoTag();

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
      type = sym.getType();
    }

    if (type == null) {
      type = incompleteType(from, name);
    }

    if (parser.tp() == T.T_LEFT_BRACE) {

      List<CStructField> fields = parseFields(parser);
      type.getTpStruct().setFields(fields);

      InfoStruct sizeAlignDto = finalizeStructType(type.getTpStruct());

      type.setSize(sizeAlignDto.getSize());
      type.setAlign(sizeAlignDto.getAlign());

    }

    return type;
  }

  private CType incompleteType(Token from, Ident tagId) {

    CStructType incomplete = new CStructType(isUnion, tagId);
    CType type = new CType(incomplete, -1, -1);

    CSymbol structSymbol = new CSymbol(CSymbolBase.SYM_STRUCT, tagId, type, from);
    parser.defineTag(tagId, structSymbol);

    return type;
  }

  private CType parseStructWithNoTag() {
    if (parser.tp() != T.T_LEFT_BRACE) {
      parser.perror("expect '{' for struct with no tag");
    }

    CStructType newstruct = new CStructType(isUnion, null);

    List<CStructField> fields = parseFields(parser);
    newstruct.setFields(fields);

    InfoStruct sizeAlignDto = finalizeStructType(newstruct);
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

    if (parser.tp() != T.T_RIGHT_BRACE && parser.tok().ofType(TOKEN_IDENT)) {
      CSymbol sym = parser.getSym(parser.tok().getIdent());
      if (sym != null) {
      }
    }

    if (parser.tp() != T.T_RIGHT_BRACE) {
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
    CType basetype = new ParseBaseType(parser).parseBase();

    if (parser.tp() == T_SEMI_COLON) {
      parser.move();

      // TODO:XXX
      // this mean:
      // 1) we inside struct/union
      // 2) we get something in declspecs
      // 3) we find semicolon
      // 4) this field has no name
      // this may be struct, union, enum
      // this may have tag or not
      // if this has a tag OR name is not anonymous
      // if this has a tag, and no name - is warning 'declaration doe's not declare anything'

      boolean isStructUnionEnum = basetype.isStrUnion() || basetype.isEnumeration();
      if (!isStructUnionEnum) {
        parser.perror("expect struct/union");
      }

      // TODO:XXX: fields offset, if it's from anonymous UNION...

      if (basetype.isEnumeration()) {
        //TODO:
        return r;
      } else {

        boolean isAnonymousDeclaration = !basetype.getTpStruct().isHasTag();
        if (isAnonymousDeclaration) {

          List<CStructField> fieldsInside = basetype.getTpStruct().getFields();
          r.addAll(fieldsInside);
          return r;
        } else {
          parser.pwarning("declaration doe's not declare anything.");
          return r; // TODO: empty now ?
        }

      }

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

    if (structDeclarator.getType().isIncomplete()) {
      parser.perror("incomplete struct field");
    }

    out.add(structDeclarator);

    while (parser.tp() == T.T_COMMA) {
      parser.move();
      CStructField structDeclaratorSeq = parseStructDeclarator(parser, specqual);

      if (structDeclaratorSeq.getType().isIncomplete()) {
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

    Declarator decl = new ParseDecl(parser).parseDecl();
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
