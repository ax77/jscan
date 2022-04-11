package cc.parse;

import java.util.ArrayList;
import java.util.List;

import cc.builders.TypeMerger;
import cc.tree.CSymbol;
import cc.tree.Declaration;
import cc.tree.Declarator;
import cc.tree.Initializer;
import cc.tree.CSymbol.CSymGlobalVar;
import cc.tree.CSymbol.CSymLocalVar;
import cc.tree.Declaration.StaticAssertDeclarationStub;
import cc.tree.Declaration.TypedefDeclarationStub;
import cc.types.CStorageKind;
import cc.types.CType;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
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

    StaticAssertDeclarationStub skip = new ParseStaticAssert(parser).isStaticAssertAndItsOk();
    if (skip != null) {
      return new Declaration(skip, startLocation);
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
      ArrayList<TypedefDeclarationStub> typedefs = new ArrayList<>();
      parseAndDefineTypedefs(typedefs);
      parser.semicolon();
      return new Declaration(typedefs, startLocation);
    }

    List<CSymbol> initDeclaratorList = parseInitDeclaratorList();

    @SuppressWarnings("unused")
    Token endLocation = parser.semicolon();

    final Declaration declaration = new Declaration(initDeclaratorList, startLocation);
    return declaration;
  }

  private void parseAndDefineTypedefs(ArrayList<TypedefDeclarationStub> typedefs) {

    parseAndDefineOneTypedef(typedefs);
    while (parser.tp() == T.T_COMMA) {
      parser.move();
      parseAndDefineOneTypedef(typedefs);
    }

    if (parser.is(T.T_ASSIGN)) {
      parser.perror("typedef-name must not have an initializer");
    }
  }

  private void parseAndDefineOneTypedef(ArrayList<TypedefDeclarationStub> typedefs) {
    final Declarator decl = new ParseDeclarator(parser).parse();
    final CType type = TypeMerger.build(basetype, decl);
    final Ident name = decl.getName();
    parser.defineTypedef(name, type);
    typedefs.add(new TypedefDeclarationStub(name, type));
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

    boolean isGlobal = parser.isFileScope();

    if (parser.tp() != T.T_ASSIGN) {
      return defineSym(isGlobal, storagespec, saved, name, type, null);
    }

    parser.checkedMove(T.T_ASSIGN);
    List<Initializer> inits = new ParseInitializer(parser).parse(type);

    if (storagespec == CStorageKind.ST_TYPEDEF) {
      parser.perror("typedef with initializer.");
    }

    return defineSym(isGlobal, storagespec, saved, name, type, inits);
  }

  private CSymbol defineSym(boolean isGlobal, CStorageKind storagespec, Token saved, Ident name, CType type,
      List<Initializer> inits) {

    if (isGlobal) {
      final CSymGlobalVar gvar = new CSymGlobalVar(name, type, inits);
      final CSymbol sym = new CSymbol(storagespec, saved, gvar);
      parser.defineSym(sym);
      return sym;
    }

    final CSymLocalVar lvar = new CSymLocalVar(name, type, inits);
    final CSymbol sym = new CSymbol(storagespec, saved, lvar);
    parser.defineSym(sym);
    return sym;
  }

}
