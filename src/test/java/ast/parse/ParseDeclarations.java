package ast.parse;

import java.util.ArrayList;
import java.util.List;

import ast.builders.TypeMerger;
import ast.symtab.CSymbol;
import ast.symtab.CSymbolBase;
import ast.tree.Declaration;
import ast.tree.Declarator;
import ast.tree.Initializer;
import ast.types.CStorageKind;
import ast.types.CType;
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

    List<CSymbol> initDeclaratorList = parseInitDeclaratorList();

    @SuppressWarnings("unused")
    Token endLocation = parser.semicolon();

    final Declaration declaration = new Declaration(initDeclaratorList, startLocation);
    return declaration;
  }

  private List<CSymbol> parseInitDeclaratorList() {
    List<CSymbol> initDeclaratorList = new ArrayList<CSymbol>(0);

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

    Declarator decl = new ParseDeclarator(parser).parse();
    CType type = TypeMerger.build(basetype, decl);

    if (parser.tp() != T.T_ASSIGN) {
      CSymbolBase symBase = parser.isFileScope() ? CSymbolBase.SYM_GVAR : CSymbolBase.SYM_LVAR;
      if (type.isFunction()) {
        symBase = CSymbolBase.SYM_FUNC;
      }

      if (storagespec == CStorageKind.ST_TYPEDEF) {
        symBase = CSymbolBase.SYM_TYPEDEF;
      }

      CSymbol uninit = new CSymbol(symBase, decl.getName(), type, saved);
      parser.defineSym(uninit);

      return uninit;
    }

    parser.checkedMove(T.T_ASSIGN);
    List<Initializer> inits = new ParseInitializer(parser).parse(type);

    if (storagespec == CStorageKind.ST_TYPEDEF) {
      parser.perror("typedef with initializer.");
    }

    CSymbol sym = new CSymbol(CSymbolBase.SYM_LVAR, decl.getName(), type, inits, saved);
    parser.defineSym(sym);

    return sym;
  }

}
