package ast.parse;

import static jscan.tokenize.T.TOKEN_IDENT;

import ast.builders.ApplyEnumInfo;
import ast.builders.ConstexprEval;
import ast.symtab.CSymbol;
import ast.symtab.CSymbolBase;
import ast.tree.Expression;
import ast.types.CEnumType;
import ast.types.CType;
import ast.types.CTypeImpl;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseEnum {
  private final Parse parser;

  public ParseEnum(Parse parser) {
    this.parser = parser;
  }

  public CType parse() {

    //enum ...
    //     ^

    boolean iscorrect = parser.tok().ofType(TOKEN_IDENT) || parser.tok().ofType(T.T_LEFT_BRACE);
    if (!iscorrect) {
      parser.perror("expect identifier or { for enum type-specifier");
    }

    Token tag = null;
    if (parser.tok().ofType(TOKEN_IDENT)) {
      tag = parser.tok();
      parser.move();
    }

    // 1: struct S
    // 2: struct {
    // 3: struct S {
    // 4: struct S *sP;

    // linearize it as well

    //    if (tag != null) {
    //      if (parser.isHasTag(tag.getIdent())) {
    //        if (parser.tp() == T.T_LEFT_BRACE) {
    //        } else {
    //        }
    //      } else {
    //        if (parser.tp() == T.T_LEFT_BRACE) {
    //        } else {
    //        }
    //      }
    //    } else {
    //      if (parser.tp() == T.T_LEFT_BRACE) {
    //      } else {
    //      }
    //    }

    boolean paranoia = false;

    if (tag != null) {
      if (parser.tok().ofType(T.T_SEMI_COLON)) {
        if (parser.isHasTag(tag.getIdent())) {
          CType type = parser.getTag(tag.getIdent()).getType();
          return type;
        } else {
          final CType incomplete = new CType(new CEnumType(tag.getIdent()));
          final CSymbol nsymbol = new CSymbol(CSymbolBase.SYM_ENUM, tag.getIdent(), incomplete, tag);
          parser.defineTag(tag.getIdent(), nsymbol);
          return incomplete;
        }
      } else if (parser.tp() == T.T_LEFT_BRACE) {
      }
    }

    if (tag != null) {

      if (parser.isHasTag(tag.getIdent())) {

        // tag was in symtab
        //
        if (parser.tp() == T.T_LEFT_BRACE) {
          CType type = parser.getTag(tag.getIdent()).getType();

          // TODO:
          // 1) this is warning: declaration was not declare anything???
          // 2) complete previous incompleted
          // 3) ... ??? 

          ApplyEnumInfo dto = parseEnumeratorList();

          if (paranoia) {
            System.out.println("1");
          }

          if (type.isIncomplete()) {
            // TODO: max size, align.
            type.getTpEnum().setEnumerators(dto.getEnumerators());
          }

          // is complete.
          // TODO:XXX:?
          else {
            if (paranoia) {
              System.out.println("1.1");
            }

            CEnumType newenum = new CEnumType(tag.getIdent());
            newenum.setEnumerators(dto.getEnumerators());

            CType newtype = new CType(newenum);

            parser.defineTag(tag.getIdent(), new CSymbol(CSymbolBase.SYM_ENUM, tag.getIdent(), newtype, tag));
            return newtype;
          }

          return type;
        }

        // not a brace '{'
        //
        else {
          CType type = parser.getTag(tag.getIdent()).getType();

          if (paranoia) {
            System.out.println("2");
          }

          return type;
        }
      }

      else {
        // tag present, but not found.
        // register incomplete forward.
        //

        CEnumType incomplete = new CEnumType(tag.getIdent());
        final CType structIncompleteType = new CType(incomplete);
        final CSymbol structSymbol = new CSymbol(CSymbolBase.SYM_ENUM, tag.getIdent(), structIncompleteType, tag);
        parser.defineTag(tag.getIdent(), structSymbol);

        if (parser.tp() == T.T_LEFT_BRACE) {

          // TODO:XXX:?
          // rewrite...???

          ApplyEnumInfo dto = parseEnumeratorList();
          CType type = parser.getTag(tag.getIdent()).getType();
          type.getTpEnum().setEnumerators(dto.getEnumerators());

          if (paranoia) {
            System.out.println("3");
          }

          return type;

        }

        else {
          if (paranoia) {
            System.out.println("4");
          }

          return structIncompleteType;

        }
      }

    }

    else {

      // all cases if (tag == null)

      if (parser.tp() == T.T_LEFT_BRACE) {

        ApplyEnumInfo dto = parseEnumeratorList();
        CEnumType newenum = new CEnumType(null);

        newenum.setEnumerators(dto.getEnumerators());
        CType type = new CType(newenum);

        if (paranoia) {
          System.out.println("5");
        }

        return type;

      }

      else {

        if (paranoia) {
          System.out.println("6");
        }
      }
    }

    parser.perror("paranoia");
    return null;

  }

  private ApplyEnumInfo parseEnumeratorList() {

    parser.checkedMove(T.T_LEFT_BRACE);

    ApplyEnumInfo enumdto = new ApplyEnumInfo(parser);
    parseEnumerator(enumdto);

    while (parser.tp() == T.T_COMMA) {
      parser.move();

      //
      if (parser.tp() == T.T_RIGHT_BRACE) {
        // taint comma: 
        // enum { a,b,c, }
        //             ^
        break;
      }
      //

      parseEnumerator(enumdto);
    }

    parser.checkedMove(T.T_RIGHT_BRACE);
    return enumdto;
  }

  private void parseEnumerator(ApplyEnumInfo enumdto) {
    Token saved = parser.tok();
    Ident identifier = parser.getIdent();

    int enumvalue = enumdto.getCurvalue();

    if (parser.tp() != T.T_ASSIGN) {
      final CSymbol symbol = new CSymbol(CSymbolBase.SYM_ENUM_CONST, identifier, CTypeImpl.TYPE_INT, saved); // TODO:Storage
      symbol.setEnumvalue(enumvalue);

      parser.defineSym(identifier, symbol);
      enumdto.addEnumerator(identifier, enumvalue);

      return; // XXX:
    }

    parser.checkedMove(T.T_ASSIGN);

    Expression constexpr = new ParseExpression(parser).e_const_expr();
    enumvalue = (int) new ConstexprEval(parser).ce(constexpr);

    final CSymbol symbol = new CSymbol(CSymbolBase.SYM_ENUM_CONST, identifier, CTypeImpl.TYPE_INT, saved); // TODO:Storage
    symbol.setEnumvalue(enumvalue);

    parser.defineSym(identifier, symbol);
    enumdto.addEnumerator(identifier, enumvalue);

  }
}
