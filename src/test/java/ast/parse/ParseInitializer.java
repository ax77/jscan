package ast.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.builders.ConstexprEval;
import ast.tree.Expression;
import ast.tree.Initializer;
import ast.types.CArrayType;
import ast.types.CStructField;
import ast.types.CStructType;
import ast.types.CType;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseInitializer {

  private final Parse parser;

  public ParseInitializer(Parse parser) {
    this.parser = parser;
  }

  public List<Initializer> parse(CType type) {
    List<Initializer> r = new ArrayList<Initializer>();
    if (parser.is(T.T_LEFT_BRACE)) {
      read_list(r, type, 0, false);
    } else {
      Expression simple = new ParseExpression(parser).e_assign();
      r.add(new Initializer(simple, type, 0));
    }
    Collections.sort(r);
    return r;
  }

  private void read_list(List<Initializer> inits, CType type, int offset, boolean designated) {
    boolean brace = parser.moveOptional(T.T_LEFT_BRACE);

    if (type.isArray()) {
      read_array_inits(inits, type, offset, brace, designated);
    } else if (type.isStruct()) {
      read_struct_inits(inits, type, offset, brace, designated);
    } else {
      // int a = {1};
      CType arraytype = make_array_type(type, 1);
      read_array_inits(inits, arraytype, offset, brace, false);
    }

    if (brace) {
      parser.rbrace();
    }
  }

  private void read_initializer_elem(List<Initializer> inits, CType ty, int off, boolean designated) {
    parser.moveOptional(T.T_ASSIGN);
    if (ty.isArray() || ty.isStruct() || parser.is(T.T_LEFT_BRACE)) {
      read_list(inits, ty, off, designated);
    } else {
      Expression val = new ParseExpression(parser).e_assign();
      inits.add(new Initializer(val, ty, off));
    }
    parser.moveOptional(T.T_COMMA);
  }

  private void read_struct_inits(List<Initializer> inits, CType type, int offset, boolean brace, boolean designated) {
    if (!type.isStruct()) {
      parser.perror("expect struct-type");
    }

    CStructType compound = type.tpStruct;
    List<CStructField> fields = compound.getFields();
    int fieldidx = 0;

    while (!parser.isEof()) {
      if (parser.is(T.T_RIGHT_BRACE)) {
        break;
      }

      // we don't actually know what that means.
      // so - it's an unexpected `something`.
      if ((parser.is(T.T_DOT) || parser.is(T.T_LEFT_BRACKET)) && !brace && !designated) {
        //parser.pwarning("unexpected");
        return;
      }

      CStructField cursor = null;

      if (parser.is(T.T_DOT)) {
        parser.move();

        Ident fieldname = parser.getIdent();
        cursor = compound.getFieldByName(fieldname);
        if (cursor == null) {
          parser.perror("no such field: " + fieldname.getName());
        }
        fieldidx = cursor.pos;
        designated = true;
      }

      else {
        if (!designated && fieldidx >= fields.size()) {
          break;
        }
        cursor = fields.get(fieldidx++);
      }
      if (cursor == null) {
        parser.perror("internal error...");
      }

      read_initializer_elem(inits, cursor.type, offset + cursor.offset, designated);
      designated = false;

    }

    if (brace) {
      skip_to_brace(parser);
    }
  }

  private void read_array_inits(List<Initializer> inits, CType type, int offset, boolean brace, boolean designated) {
    if (!type.isArray()) {
      parser.perror("expect array-type");
    }

    final CArrayType compound = type.tpArray;
    final int arrlen = compound.len;
    final int elemsz = compound.subtype.size;
    final boolean flexible = arrlen <= 0;

    int cursor = 0;

    for (cursor = 0; !parser.isEof() /*index < arrlen || flexible*/; cursor++) {
      if (parser.is(T.T_RIGHT_BRACE)) {
        if (compound.len <= 0) {
          compound.len = cursor;
          type.setSize(elemsz * cursor);
        }
        return;
      }

      // we don't actually know what that means.
      // so - it's an unexpected `something`.
      if ((parser.is(T.T_DOT) || parser.is(T.T_LEFT_BRACKET)) && !brace && !designated) {
        //parser.pwarning("unexpected");
        return;
      }

      // designations
      if (parser.is(T.T_LEFT_BRACKET)) {
        // TODO: GNU range [0...2] = 0
        cursor = getDesgIndex(arrlen, flexible);
        designated = true;
      }

      // We've alredy read all of the elements we need to init this array.
      // But: designations may overlap each other many times, and we have to cut them all.
      // That's why we need the `designated` flag here.
      //
      if (!designated && !flexible && cursor >= arrlen) {
        break;
      }

      read_initializer_elem(inits, compound.subtype, offset + elemsz * cursor, designated);
      designated = false;
    }

    if (brace) {
      skip_to_brace(parser);
    }
  }

  private int getDesgIndex(final int arrlen, final boolean flexible) {
    parser.lbracket();
    final Expression ce = new ParseExpression(parser).e_const_expr();
    parser.rbracket();

    final int idx = (int) new ConstexprEval(parser).ce(ce);

    if (idx < 0) {
      parser.perror("array designator index is negative");
    }
    if (!flexible) {
      if (idx >= arrlen) {
        parser.perror("array designator exceeds array bounds");
      }
    }
    return idx;
  }

  private CType make_array_type(CType ty, int len) {
    int size;
    if (len < 0) {
      size = -1;
    } else {
      size = ty.size * len;
    }
    final CType arr = new CType(new CArrayType(ty, len));
    arr.setAlign(ty.align);
    arr.setSize(size);
    return arr;
  }

  private void skip_to_brace(Parse parser) {
    if (parser.is(T.T_RIGHT_BRACE)) {
      return;
    }

    Token pos = parser.tok();

    StringBuilder sb = new StringBuilder();
    while (!parser.isEof()) {
      if (parser.is(T.T_RIGHT_BRACE)) {
        break;
      }
      Token tok = parser.moveget();
      if (tok.hasLeadingWhitespace()) {
        sb.append(" ");
      }
      sb.append(tok.getValue());
      if (tok.isNewLine()) {
        sb.append("\n");
      }
    }
    System.out.printf("excessive initializer: %s\n", sb.toString());
    System.out.println(pos.loc());
  }

}
