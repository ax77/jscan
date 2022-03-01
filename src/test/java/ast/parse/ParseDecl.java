package ast.parse;

import static jscan.tokenize.T.TOKEN_IDENT;
import static jscan.tokenize.T.T_RIGHT_PAREN;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.attributes.main.AttributesAsmsLists;
import ast.attributes.main.ParseAttributesAsms;
import ast.tree.ConstexprEval;
import ast.tree.Declarator;
import ast.tree.DeclaratorEntry;
import ast.tree.Expression;
import ast.types.CFuncParam;
import ast.types.CType;
import ast.types.CTypeKind;
import ast.types.TypeMerger;
import jscan.symtab.Ident;
import jscan.symtab.Keywords;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseDecl {
  private final Parse parser;

  public ParseDecl(Parse p) {
    this.parser = p;
  }

  public Declarator parseDecl() {
    Declarator decl = new Declarator();
    parseDeclInternal(decl);
    return decl;
  }

  private void parseDeclInternal(Declarator out) {
    List<Integer> pointers = new ArrayList<Integer>(0);

    while (parser.tok().ofType(T.T_TIMES)) {
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
        e.setConstPointer(true);
      }
      out.add(e);
    }

    // TODO:attributes
    @SuppressWarnings("unused")
    AttributesAsmsLists attributesAsmsLists = new ParseAttributesAsms(parser).parse();

  }

  private void parseDirectDeclarator(Declarator out) {
    if (parser.tok().ofType(T.T_LEFT_PAREN)) {
      parser.lparen();
      parseDeclInternal(out);
      parser.rparen();
    } else if (parser.tok().ofType(T.TOKEN_IDENT)) {
      Token saved = parser.tok();
      parser.move();
      out.setName(saved.getIdent());
    } else {
      //p.perror("no-name");
    }
    while (parser.tok().ofType(T.T_LEFT_PAREN) || parser.tok().ofType(T.T_LEFT_BRACKET)) {
      Token saved = parser.tok();
      parser.move();
      if (saved.ofType(T.T_LEFT_PAREN)) {

        DeclaratorEntry e = new DeclaratorEntry(CTypeKind.TP_FUNCTION);
        List<CFuncParam> params = parseParams(e);
        e.setParameters(params);

        out.add(e);
      } else {
        DeclaratorEntry e = new DeclaratorEntry(CTypeKind.TP_ARRAY_OF);

        Expression arrinit = parseArrayInit();

        if (arrinit != null) {
          int arrlen = (int) new ConstexprEval(parser).ce(arrinit);
          e.setArrayLen(arrlen);
        }

        out.add(e);
      }
      if (saved.ofType(T.T_LEFT_PAREN)) {
        parser.rparen();
      } else {
        parser.rbracket();
      }
    }
  }

  private Expression parseArrayInit() {

    // int x[]
    //       ^
    if (parser.tok().ofType(T.T_RIGHT_BRACKET)) {
      return null;
    }

    return new ParseExpression(parser).e_expression();
  }

  private List<CFuncParam> parseParams(DeclaratorEntry e) {
    List<CFuncParam> params = new ArrayList<CFuncParam>();

    // int x()
    //       ^
    if (parser.tok().ofType(T.T_RIGHT_PAREN)) {
      return params;
    }

    // check declarations in semantic stage.
    // int x(a,b,c) int a,b,c; {}
    //       ^
    if (!parser.isDeclSpecStart() && parser.tp() == TOKEN_IDENT) {
      parseIdentifierList(params);
      return params;
    }

    CFuncParam param = parseOneParam(e);
    params.add(param);

    while (parser.tp() == T.T_COMMA) {
      parser.move();

      // int f(char*, ...)
      //
      if (parser.tp() == T.T_DOT_DOT_DOT) {

        parser.move(); // [...]
        if (!parser.tok().ofType(T_RIGHT_PAREN)) {
          parser.perror("expect `)` after `...`");
        }

        e.setVariadicFunction(true);
        break;
      }

      CFuncParam paramSeq = parseOneParam(e);
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
    CType base = new ParseBaseType(parser).parseBase();
    Declarator decl = parseDecl();

    CType type = TypeMerger.build(base, decl);
    if (decl.isAstract()) {
      return new CFuncParam(type);
    }
    return new CFuncParam(decl.getName(), type);
  }

}
