package ast.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ast.tree.ConstexprEval;
import ast.tree.Expression;
import ast.tree.Initializer;
import ast.types.CArrayType;
import ast.types.CStructField;
import ast.types.CType;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseInitializerList {

  private final Parse parser;
  private final CType inputType;

  private static final int DEFAULT_UNKNOWN_ARLEN = 65536; // int x[?][2][2]
  private static final int NESTED_INITS_LIMIT = 256; // max recursion deep

  public ParseInitializerList(Parse parser, CType inputType) {
    this.parser = parser;
    this.inputType = inputType;
  }

  public List<Initializer> parse() {
    List<Initializer> inits = new ArrayList<Initializer>();
    readInitializerListInternal(inits, inputType, 0, NESTED_INITS_LIMIT);

    Collections.sort(inits);
    return inits;
  }

  private void readInitializerListInternal(List<Initializer> inits, CType ty, int off, int deep) {

    // check recursion deep, to prevent stack overflow.

    if (deep <= 0) {
      parser.perror("nested initializers too deep.");
    }

    // this condition used between array / struct
    // if only array could be nested, condition would not be necessary

    // 1)
    if (ty.isArray()) {

      boolean isHasBrace = parser.moveOptional(T.T_LEFT_BRACE);
      int arlen = (ty.getTpArray().getArrayLen() <= 0) ? DEFAULT_UNKNOWN_ARLEN : (ty.getTpArray().getArrayLen());

      CType sub = ty.getTpArray().getArrayOf();
      int elsize = sub.getSize();

      // recursion implement nested loop
      // for array: int x[3][2][2] this loop look like this:
      //
      // for (int i = 0; i < 3; i++) {
      //   for (int j = 0; j < 2; j++) {
      //     for (int k = 0; k < 2; k++) {
      //         ...
      //     }
      //   }
      // }

      int count = 0;
      for (count = 0; count < arlen; count++) {

        checkOverflow(DEFAULT_UNKNOWN_ARLEN, count);

        Token tok = parser.tok();
        if (tok.ofType(T.T_RIGHT_BRACE)) {
          break;
        }

        if (tok.ofType(T.T_LEFT_BRACKET)) {
          parser.lbracket();

          Expression expr = new ParseExpression(parser).e_const_expr();
          int indexDes = (int) new ConstexprEval(parser).ce(expr);

          parser.rbracket();

          checkNestedDesignationsUnimpl();
          parser.checkedMove(T.T_ASSIGN);

          if (indexDes >= arlen) {
            parser.perror("array designation index out of range");
          }

          count = indexDes;
        }

        int offsetOf = off + elsize * count;
        boolean nestedExpansion = sub.isArray() || sub.isStrUnion();

        if (!nestedExpansion) {
          addInit(inits, offsetOf);
          parser.moveOptional(T.T_COMMA);
          continue;
        }

        // I) recursive expansion of sub-initializer
        readInitializerListInternal(inits, sub, offsetOf, deep - 1);
        parser.moveOptional(T.T_COMMA);

      }

      if (isHasBrace) {
        warningExcessElements("array");
        expectClose();
      }

      if (ty.getTpArray().getArrayLen() <= 0) {
        ty.getTpArray().setArrayLen(count);
        ty.setSize(elsize * count);
      }

    }

    // 2)
    else if (ty.isStrUnion()) {

      boolean isHasBrace = parser.moveOptional(T.T_LEFT_BRACE);
      int fieldIdx = 0;

      List<CStructField> fields = ty.getTpStruct().getFields();

      // when designator change the index

      Map<Ident, Integer> fieldsIndexMap = new HashMap<Ident, Integer>();
      Map<Ident, CStructField> fieldsMap = new HashMap<Ident, CStructField>();
      for (int i = 0; i < fields.size(); i++) {
        CStructField f = fields.get(i);
        fieldsIndexMap.put(f.getName(), i);
        fieldsMap.put(f.getName(), f);
      }

      for (;;) {

        checkOverflow(DEFAULT_UNKNOWN_ARLEN, fieldIdx);

        Token tok = parser.tok();
        if (tok.ofType(T.T_RIGHT_BRACE)) {
          break;
        }

        if (fieldIdx == fields.size()) {
          break;
        }

        // .a = 3
        CStructField field = null;
        if (tok.ofType(T.T_DOT)) {
          parser.move();

          Ident fieldname = parser.getIdent();

          checkNestedDesignationsUnimpl();
          parser.checkedMove(T.T_ASSIGN);

          fieldIdx = fieldsIndexMap.get(fieldname);
          field = fieldsMap.get(fieldname);

          if (field == null) {
            parser.perror("struct has no field: " + fieldname.getName());
          }
        }

        else {
          field = fields.get(fieldIdx++);
        }

        if (field == null) {
          parser.perror("struct field initialization internal error");
        }

        int offsetOf = off + field.getOffset();

        CType sub = field.getType();
        boolean nestedExpansion = sub.isArray() || sub.isStrUnion();

        if (!nestedExpansion) {
          addInit(inits, offsetOf);
          parser.moveOptional(T.T_COMMA);
          continue;
        }

        // II) recursive expansion of sub-initializer
        readInitializerListInternal(inits, sub, offsetOf, deep - 1);
        parser.moveOptional(T.T_COMMA);

        if (!ty.isStrUnion()) {
          System.out.println("parse initializer: not a struct: " + ty.toString());
          break;
        }

      }

      if (isHasBrace) {
        warningExcessElements("struct");
        expectClose();
      }
    }

    // 3)
    else {
      System.out.println("III");
      CType arraytype = new CType(new CArrayType(ty, 1));
      readInitializerListInternal(inits, arraytype, off, deep - 1);
    }
  }

  private void warningExcessElements(String where) {
    while (!parser.isEof()) {
      Token tok = parser.tok();
      if (tok.ofType(T.T_RIGHT_BRACE)) {
        return;
      }
      if (tok.ofType(T.TOKEN_EOF)) {
        parser.perror("unexpected EOF in initializer-list");
      }
      if (tok.ofType(T.T_DOT)) {
        parser.perror("unimpl. skip designations.");
      }
      Expression expr = new ParseExpression(parser).e_assign();
      parser.moveOptional(T.T_COMMA);

      System.out.println("excess elements in " + where + " initializer: " + expr);
    }
  }

  private void expectClose() {
    @SuppressWarnings("unused")
    Token jo = parser.checkedMove(T.T_RIGHT_BRACE);
  }

  private void addInit(List<Initializer> where, int withOffset) {
    if (parser.tok().ofType(T.T_LEFT_BRACE)) {
      parser.perror("braces around scalar initializer");
    }
    Expression expr = new ParseExpression(parser).e_assign();
    where.add(new Initializer(expr, withOffset));
  }

  private void checkNestedDesignationsUnimpl() {

    // TODO: 
    // .a[0] = ...
    // [1][2].a = ...
    // etc.

    if (parser.tok().ofType(T.T_DOT)) {
      parser.unimplemented("nested struct designators .a.b.c.d.e = ...");
    }
    if (parser.tok().ofType(T.T_LEFT_BRACKET)) {
      parser.unimplemented("nested array designators [1][2] = ...");
    }
  }

  private void checkOverflow(int guard, int initsCnt) {
    parser.unexpectedEof();
    if (initsCnt >= guard) {
      parser.perror("struct/array initializer list too big...");
    }
  }

}
