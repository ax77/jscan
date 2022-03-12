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
import ast.symtab.CSymTag;
import ast.tree.Declarator;
import ast.tree.Expression;
import ast.types.CStructField;
import ast.types.CStructType;
import ast.types.CType;
import jscan.symtab.Ident;
import jscan.symtab.Keywords;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import jscan.utils.NullChecker;

public class ParseStruct3 {

  private final Parse parser;
  private final boolean isUnion;

  public ParseStruct3(Parse parser, boolean isUnion) {
    this.parser = parser;
    this.isUnion = isUnion;
  }

  public CType parse() {

    //struct ...
    //       ^

    boolean iscorrect = parser.isUserDefinedId() || parser.is(T.T_LEFT_BRACE);
    if (!iscorrect) {
      parser.perror("expect identifier or { for enum type-specifier");
    }

    @SuppressWarnings("unused")
    AttributesAsmsLists attributesAsmsLists = new ParseAttributesAsms(parser).parse();

    Token pos = parser.tok();
    Ident tag = null;
    if (parser.is(TOKEN_IDENT)) {
      tag = parser.getIdent();
    }

    CStructType s = new CStructType(isUnion, tag);
    CType tp = new CType(s, -1, -1);

    if (tag != null) {

      CSymTag cur = parser.getTagFromCurrentScope(tag);
      CSymTag all = parser.getTag(tag);
      if (all != null) {
        // A declaration of the identifier as a tag
        // is visible in this or an enclosing scope.
        if (cur == null && (parser.is(T.T_SEMI_COLON) || parser.is(T.T_LEFT_BRACE))) {
          tp = incompl(tag, pos);
        } else {
          tp = all.type;
        }
      } else {
        tp = incompl(tag, pos);
      }
      NullChecker.check(tp);
      if (parser.is(T.T_LEFT_BRACE)) {
        if (cur != null && !cur.type.isIncomplete()) {
          parser.perror("redefinition of struct: " + tag);
        }
        fields(tp);
        if (cur != null) {
          cur.type = tp;
        }
      }

    } else if (parser.is(T.T_LEFT_BRACE)) {
      // anonymous
      fields(tp);
    } else {
      parser.unreachable("expecting tag or {");
    }

    return tp;
  }

  private void fields(CType tp) {
    List<CStructField> fields = parseFields();
    tp.tpStruct.setFields(fields);
    finalizeStruct(tp);
  }

  private List<CStructField> parseFields() {
    parser.lbrace();

    // struct S {};
    //           ^
    if (parser.tp() == T.T_RIGHT_BRACE) {
      parser.pwarning("empty struct declaration list");
      parser.rbrace();
      return new ArrayList<>();
    }

    List<CStructField> flds = new ArrayList<>();
    flds.addAll(parseStructDeclaration());
    while (parser.isDeclSpecStart()) {
      flds.addAll(parseStructDeclaration());
    }

    parser.rbrace();
    return flds;
  }

  //struct_declaration
  //  : specifier_qualifier_list ';'  /* for anonymous struct/union */
  //  | specifier_qualifier_list struct_declarator_list ';'
  //  | static_assert_declaration
  //  ;

  private List<CStructField> parseStructDeclaration() {

    List<CStructField> r = new ArrayList<>();

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

      if (basetype.isEnumeration()) {
        return r;
      }

      /// struct some {                                             
      ///     enum toktype {                                        
      ///         t_ident, t_string,                                
      ///     };   // warning: declaration does not declare anything
      ///     int; // warning: declaration does not declare anything
      ///     int field;                                            
      /// };                                                        

      // TODO:XXX: fields offset, if it's from anonymous UNION...

      if (basetype.isStrUnion()) {
        boolean isAnonymousDeclaration = !basetype.tpStruct.hasTag();
        if (isAnonymousDeclaration) {
          List<CStructField> fieldsInside = basetype.tpStruct.getFields();
          r.addAll(fieldsInside);
          return r;
        }
      }

      parser.pwarning("declaration does not declare anything");
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

  private void parseStructDeclaratorList(List<CStructField> out, CType tp) {

    CStructField structDeclarator = parseStructDeclarator(tp);

    if (structDeclarator.type.isIncomplete()) {
      parser.perror("incomplete struct field");
    }

    out.add(structDeclarator);

    while (parser.tp() == T.T_COMMA) {
      parser.move();
      CStructField structDeclaratorSeq = parseStructDeclarator(tp);

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

  private CStructField parseStructDeclarator(CType tp) {

    // unnamed-bit-field
    //
    if (parser.tp() == T_COLON) {
      parser.move();

      Expression consterpr = new ParseExpression(parser).e_const_expr();
      int width = (int) new ConstexprEval(parser).ce(consterpr);

      final CType bf = new SemanticBitfield(parser).buildBitfield(tp, width);
      return new CStructField(bf);
    }

    // need normal field or named bit-field

    Declarator decl = new ParseDeclarator(parser).parse();
    CType type = TypeMerger.build(tp, decl);

    // named-bit-field
    //
    if (parser.tp() == T_COLON) {
      parser.move();

      Expression consterpr = new ParseExpression(parser).e_const_expr();
      int width = (int) new ConstexprEval(parser).ce(consterpr);

      final CType bf = new SemanticBitfield(parser).buildBitfield(type, width);
      return new CStructField(decl.getName(), bf);
    }

    // plain-field
    //
    return new CStructField(decl.getName(), type);
  }

  private void finalizeStruct(CType tp) {
    CStructType su = tp.tpStruct;
    if (!su.isComplete) {
      parser.unimplemented("incomplete struct finalization");
    }
    final ApplyStructInfo info = new ApplyStructInfo(su.isUnion, su.getFields());
    tp.setSize(info.getSize());
    tp.setAlign(info.getAlign());
  }

  private CType incompl(Ident tag, Token pos) {
    final CStructType su = new CStructType(isUnion, tag);
    final CType typ = new CType(su, -1, -1);
    final Ident keyword = isUnion ? Keywords.union_ident : Keywords.struct_ident;
    parser.defineTag(new CSymTag(keyword, tag, typ));
    return typ;
  }

}
