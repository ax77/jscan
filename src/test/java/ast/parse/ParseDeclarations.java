package ast.parse;

import java.util.ArrayList;
import java.util.List;

import ast.builders.TypeMerger;
import ast.symtab.CSymGlobalVar;
import ast.symtab.CSymLocalVar;
import ast.symtab.CSymbol;
import ast.tree.Declaration;
import ast.tree.Declarator;
import ast.tree.Initializer;
import ast.types.CStorageKind;
import ast.types.CType;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;
import jscan.utils.NullChecker;

public class ParseDeclarations {
  private final Parse parser;
  private CType basetype;
  private CStorageKind storagespec;

  public ParseDeclarations(Parse parser) {
    this.parser = parser;
  }

  public Declaration parse() {

    Token startLocation = parser.tok();

    boolean skip = new ParseStaticAssert(parser).isStaticAssertAndItsOk();
    if (skip) {
      return new Declaration(startLocation);
    }

    ParseBaseType pb = new ParseBaseType(parser);
    this.basetype = pb.parse();
    this.storagespec = pb.getStorageSpec();
    NullChecker.check(this.basetype, this.storagespec); // paranoia

    /// this may be struct/union/enum declaration
    ///
    if (parser.tp() == T.T_SEMI_COLON) {
      @SuppressWarnings("unused")
      Token endLocation = parser.semicolon();

      // semicolon after mean: this declaration has no name, no declarator after...
      // if this aggregate declared without name in function-scope, it NOT change stack-size.

      return new Declaration(basetype, startLocation);
    }

    if (storagespec == CStorageKind.ST_TYPEDEF) {
      parseAndDefineTypedefs();
      parser.semicolon();
      return new Declaration(startLocation);
    }

    List<CSymbol> initDeclaratorList = parseInitDeclaratorList();

    @SuppressWarnings("unused")
    Token endLocation = parser.semicolon();

    final Declaration declaration = new Declaration(initDeclaratorList, startLocation);
    return declaration;
  }

  private void parseAndDefineTypedefs() {

    parseAndDefineOneTypedef();
    while (parser.tp() == T.T_COMMA) {
      parser.move();
      parseAndDefineOneTypedef();
    }

    if (parser.is(T.T_ASSIGN)) {
      parser.perror("typedef-name must not have an initializer");
    }
  }

  private void parseAndDefineOneTypedef() {
    final Declarator decl = new ParseDeclarator(parser).parse();
    final CType type = TypeMerger.build(basetype, decl);
    final Ident name = decl.getName();
    parser.defineTypedef(name, type);
  }

  // TODO: ignore typedefs here: `initDeclaratorList.add(initDeclarator);` :)

  private List<CSymbol> parseInitDeclaratorList() {
    List<CSymbol> initDeclaratorList = new ArrayList<>();

    CSymbol initDeclarator = parseInitDeclarator();
    initDeclaratorList.add(initDeclarator);

    while (parser.tp() == T.T_COMMA) {
      parser.move();

      CSymbol initDeclaratorSeq = parseInitDeclarator();
      initDeclaratorList.add(initDeclaratorSeq);
    }

    return initDeclaratorList;
  }

  private CSymbol parseInitDeclarator() {
    //  init_declarator
    //    : declarator '=' initializer
    //    | declarator
    //    ;

    Token saved = parser.tok();

    final Declarator decl = new ParseDeclarator(parser).parse();
    final CType type = TypeMerger.build(basetype, decl);
    final Ident name = decl.getName();

    if (parser.tp() != T.T_ASSIGN) {
      boolean isGlobal = parser.isFileScope();

      if (!isGlobal) {
        final CSymLocalVar lvar = new CSymLocalVar(name, type);
        final CSymbol sym = new CSymbol(lvar, saved);
        parser.defineSym(sym);
        return sym;
      }

      if (isGlobal) {
        final CSymGlobalVar gvar = new CSymGlobalVar(name, type);
        final CSymbol sym = new CSymbol(gvar, saved);
        parser.defineSym(sym);
        return sym;
      }

      throw new AstParseException("unreachable");
    }

    parser.checkedMove(T.T_ASSIGN);
    List<Initializer> inits = new ParseInitializer(parser).parse(type);

    if (storagespec == CStorageKind.ST_TYPEDEF) {
      parser.perror("typedef with initializer.");
    }

    CSymLocalVar lvar = new CSymLocalVar(name, type, inits);
    CSymbol sym = new CSymbol(lvar, saved);
    parser.defineSym(sym);

    return sym;
  }

}
