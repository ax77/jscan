package ast.parse;

import static jscan.tokenize.T.T_LEFT_BRACE;

import java.util.List;
import java.util.Set;

import ast.attributes.main.AttributesAsmsLists;
import ast.attributes.main.ParseAttributesAsms;
import ast.symtab.CSymbol;
import ast.symtab.CSymbolBase;
import ast.tree.Declaration;
import ast.tree.Declarator;
import ast.tree.ExternalDeclaration;
import ast.tree.FunctionDefinition;
import ast.tree.Statement;
import ast.types.CFuncParam;
import ast.types.CType;
import ast.types.TypeMerger;
import jscan.symtab.Ident;
import jscan.symtab.ScopeLevels;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseExternal {

  private final Parse parser;

  public ParseExternal(Parse parser) {
    this.parser = parser;
  }

  public ExternalDeclaration parse() {

    if (!isFunc()) {
      final Declaration declaration = new ParseDeclarations(parser).parse();
      return new ExternalDeclaration(declaration);
    }

    return functionDefinition();
  }

  public boolean isFunc() {

    ParseState state = new ParseState(parser);
    boolean result = isFuncInternal();

    parser.restoreState(state);
    return result;
  }

  private boolean isFuncInternal() {

    //
    //  1) declaration's
    //
    //  int a               ;
    //  int a, b, c         ;
    //  int a = 1           ;
    //  int a()             ;
    //  struct s { int x; } ;
    //  int a, b(), c[1]    ;
    //
    //  2) function's
    //
    //  int f(a,b,c) int a,b,c; {  }
    //  int f(void) {  }
    //  int f() {  }
    //

    // try to do it lexically, instead of mess guess.

    while (parser.tp() == T.T_SEMI_COLON) {
      parser.move();
    }

    AttributesAsmsLists attrs = new ParseAttributesAsms(parser).parse();

    boolean hasOpen = false;
    boolean hasClose = false;

    while (!parser.isEof()) {

      attrs = new ParseAttributesAsms(parser).parse();

      Token t = parser.tok();
      parser.move();

      if (!hasOpen) {
        hasOpen = t.ofType(T.T_LEFT_PAREN);
      }
      if (!hasClose) {
        hasClose = t.ofType(T.T_RIGHT_PAREN);
      }

      if (t.ofType(T.T_ASSIGN)) {
        return false;
      }

      else if (t.ofType(T.T_SEMI_COLON)) {
        if (parser.tok().ofType(T.T_LEFT_BRACE)) {
          return hasOpen && hasClose; // KnR - style declaration
        }
        return false; // end of normal declaration
      }

      else if (t.ofType(T.T_LEFT_BRACE)) {
        return hasOpen && hasClose;
      }
    }

    return false;
  }

  private ExternalDeclaration functionDefinition() {

    CType base = new ParseBaseType(parser).parse();

    Declarator decl = new ParseDeclarator(parser).parse();
    CType type = TypeMerger.build(base, decl);

    // K&R function style declaration-list
    //
    if (parser.isDeclSpecStart()) {
      parser.perror("unimpl. KnR function declaration.");
    }

    // and corner case: ANSI function-definition
    //
    if (parser.tp() != T_LEFT_BRACE || !type.isFunction()) {
      parser.perror("expect function definition");
    }

    CSymbol funcSymbol = new CSymbol(CSymbolBase.SYM_FUNC, decl.getName(), type, parser.tok());
    parser.defineSym(decl.getName(), funcSymbol);

    FunctionDefinition fd = new FunctionDefinition(funcSymbol);
    parser.setCurrentFn(fd);
    parser.pushscope(ScopeLevels.METHOD_SCOPE);

    defineParameters(fd.getSignature().getType());
    define__func__(fd.getSymbol().getName());

    Statement cst = new ParseStatement(parser).parseCompoundStatement(true);
    fd.setCompoundStatement(cst);

    parser.setCurrentFn(null);
    parser.popscope();

    checkLabels(fd);
    return new ExternalDeclaration(fd);
  }

  private void checkLabels(FunctionDefinition fd) {
    Set<Ident> gotos = fd.getGotos();
    Set<Ident> labels = fd.getLabels();

    for (Ident id : gotos) {
      if (!labels.contains(id)) {
        parser.perror("goto " + id.getName() + " has no target label");
      }
    }
  }

  private void define__func__(Ident funcName) {
  }

  private void defineParameters(CType signature) {

    final List<CFuncParam> parameters = signature.getTpFunction().getParameters();

    if (parameters.size() == 1) {
      CFuncParam first = parameters.get(0);
      if (first.getType().isVoid() && first.getName() == null) {
        return;
      }
    }

    for (CFuncParam fparam : parameters) {
      CSymbol paramsym = new CSymbol(CSymbolBase.SYM_LVAR, fparam.getName(), fparam.getType(), parser.tok());
      parser.defineSym(fparam.getName(), paramsym);
    }
  }

}
