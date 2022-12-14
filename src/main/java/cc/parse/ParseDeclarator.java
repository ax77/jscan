package cc.parse;

import static jscan.tokenize.T.TOKEN_IDENT;
import static jscan.tokenize.T.T_RIGHT_PAREN;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.builders.ConstexprEval;
import cc.builders.TypeMerger;
import cc.tree.AttributesAsmsLists;
import cc.tree.Declarator;
import cc.tree.Expression;
import cc.tree.Keywords;
import cc.tree.Declarator.DeclaratorEntry;
import cc.types.CFuncParam;
import cc.types.CType;
import cc.types.CTypeKind;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseDeclarator {
  private final Parse parser;

  public ParseDeclarator(Parse p) {
    this.parser = p;
  }

  public Declarator parse() {
    Declarator decl = new Declarator();
    parseDeclInternal(decl);
    return decl;
  }

  private void parseDeclInternal(Declarator out) {
    List<Integer> pointers = new ArrayList<Integer>(0);

    while (parser.is(T.T_TIMES)) {
      parser.move();

      Set<Ident> ptrTypeQuals = new HashSet<Ident>();
      while (Pcheckers.isTypeQual(parser.tok())) {
        Token saved = parser.tok();
        parser.move();
        ptrTypeQuals.add(saved.getIdent());
      }

      if (!ptrTypeQuals.isEmpty() && ptrTypeQuals.contains(Keywords.const_ident)) {
        pointers.add(2);
      } else {
        pointers.add(0);
      }

    }

    parseDirectDeclarator(out);

    while (!pointers.isEmpty()) {
      int p = pointers.remove(0);
      DeclaratorEntry e = new DeclaratorEntry(CTypeKind.TP_POINTER_TO);
      if (p == 2) {
        e.isConstPointer = true;
      }
      out.add(e);
    }

    // TODO:attributes
    @SuppressWarnings("unused")
    AttributesAsmsLists attributesAsmsLists = new ParseAttributesAsms(parser).parse();

  }

  private void parseDirectDeclarator(Declarator out) {
    if (parser.is(T.T_LEFT_PAREN)) {
      parser.lparen();
      parseDeclInternal(out);
      parser.rparen();
    } else if (parser.is(T.TOKEN_IDENT)) {
      Token saved = parser.tok();
      parser.move();
      out.setName(saved.getIdent());
    }
    while (parser.is(T.T_LEFT_PAREN) || parser.is(T.T_LEFT_BRACKET)) {
      Token saved = parser.tok();
      parser.move();
      if (saved.is(T.T_LEFT_PAREN)) {

        final DeclaratorEntry ent = new DeclaratorEntry(CTypeKind.TP_FUNCTION);
        ent.parameters = parseParams(ent);

        out.add(ent);
      } else {
        final DeclaratorEntry ent = new DeclaratorEntry(CTypeKind.TP_ARRAY_OF);
        final Expression arrinit = parseArrayInit();

        if (arrinit != null) {
          int arrlen = (int) new ConstexprEval(parser).ce(arrinit);
          ent.arrlen = arrlen;
        }

        out.add(ent);
      }
      if (saved.is(T.T_LEFT_PAREN)) {
        parser.rparen();
      } else {
        parser.rbracket();
      }
    }
  }

  private Expression parseArrayInit() {

    // int x[]
    //       ^
    if (parser.is(T.T_RIGHT_BRACKET)) {
      return null;
    }

    return new ParseExpression(parser).e_expression();
  }

  private List<CFuncParam> parseParams(final DeclaratorEntry ent) {
    List<CFuncParam> params = new ArrayList<CFuncParam>();

    // int x()
    //       ^
    if (parser.is(T.T_RIGHT_PAREN)) {
      return params;
    }

    // check declarations in semantic stage.
    // int x(a,b,c) int a,b,c; {}
    //       ^
    if (!parser.isDeclSpecStart() && parser.tp() == TOKEN_IDENT) {
      parseIdentifierList(params);
      return params;
    }

    CFuncParam param = parseOneParam(ent);
    params.add(param);

    while (parser.tp() == T.T_COMMA) {
      parser.move();

      // int f(char*, ...)
      //
      if (parser.tp() == T.T_DOT_DOT_DOT) {

        parser.move(); // [...]
        if (!parser.is(T_RIGHT_PAREN)) {
          parser.perror("expect `)` after `...`");
        }

        ent.isVariadicFunction = true;
        break;
      }

      CFuncParam paramSeq = parseOneParam(ent);
      params.add(paramSeq);
    }

    return params;
  }

  private void parseIdentifierList(List<CFuncParam> params) {
    Ident id = parser.getIdent();
    params.add(new CFuncParam(id));

    while (parser.tp() == T.T_COMMA) {
      parser.move();
      Ident idSeq = parser.getIdent();
      params.add(new CFuncParam(idSeq));
    }
  }

  private CFuncParam parseOneParam(DeclaratorEntry e) {
    CType base = new ParseBaseType(parser).parse();
    Declarator decl = parse();

    CType type = TypeMerger.build(base, decl);
    if (decl.isAbstract()) {
      return new CFuncParam(type);
    }
    return new CFuncParam(decl.getName(), type);
  }

}
