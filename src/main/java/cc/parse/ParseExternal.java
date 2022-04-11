package cc.parse;

import static jscan.tokenize.T.T_LEFT_BRACE;

import java.util.List;

import cc.builders.TypeMerger;
import cc.tree.AttributesAsmsLists;
import cc.tree.CSymbol;
import cc.tree.Declaration;
import cc.tree.Declarator;
import cc.tree.CSymbol.CSymFunction;
import cc.tree.CSymbol.CSymLocalVar;
import cc.tree.TranslationUnit.ExternalDeclaration;
import cc.types.CFuncParam;
import cc.types.CStorageKind;
import cc.types.CType;
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

    @SuppressWarnings("unused")
    AttributesAsmsLists attrs = new ParseAttributesAsms(parser).parse();

    boolean hasOpen = false;
    boolean hasClose = false;

    while (!parser.isEof()) {

      attrs = new ParseAttributesAsms(parser).parse();

      Token t = parser.tok();
      parser.move();

      if (!hasOpen) {
        hasOpen = t.is(T.T_LEFT_PAREN);
      }
      if (!hasClose) {
        hasClose = t.is(T.T_RIGHT_PAREN);
      }

      if (t.is(T.T_ASSIGN)) {
        return false;
      }

      else if (t.is(T.T_SEMI_COLON)) {
        if (parser.is(T.T_LEFT_BRACE)) {
          return hasOpen && hasClose; // KnR - style declaration
        }
        return false; // end of normal declaration
      }

      else if (t.is(T.T_LEFT_BRACE)) {
        return hasOpen && hasClose;
      }
    }

    return false;
  }

  private ExternalDeclaration functionDefinition() {

    final ParseBaseType parseBaseType = new ParseBaseType(parser);
    CType base = parseBaseType.parse();
    CStorageKind storagespec = parseBaseType.getStorageSpec();

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

    CSymFunction func = new CSymFunction(decl.getName(), type);
    parser.defineSym(new CSymbol(storagespec, parser.tok(), func));
    parser.setCurrentFn(func);
    parser.pushscope(ScopeLevels.METHOD_SCOPE);

    defineParameters(func.type);
    define__func__(func.name.getName());

    func.block = new ParseStatement(parser).parseCompoundStatement(true);

    parser.setCurrentFn(null);
    parser.popscope();

    checkLabels(func);
    return new ExternalDeclaration(func);
  }

  private void checkLabels(CSymFunction fd) {
    for (Ident id : fd.gotos) {
      if (!fd.labels.contains(id)) {
        parser.perror("goto " + id.getName() + " has no target label");
      }
    }
  }

  private void define__func__(String funcName) {
  }

  private void defineParameters(CType signature) {

    final List<CFuncParam> parameters = signature.tpFunction.parameters;

    if (parameters.size() == 1) {
      CFuncParam first = parameters.get(0);
      if (first.type.isVoid() && first.name == null) {
        return;
      }
    }

    int paramidx = 0;
    for (CFuncParam fparam : parameters) {
      CSymLocalVar param = new CSymLocalVar(fparam.name, fparam.type, null);
      param.paramidx = paramidx++;
      param.isFparam = true;
      parser.defineSym(new CSymbol(CStorageKind.ST_AUTO, parser.tok(), param));
    }
  }

}
