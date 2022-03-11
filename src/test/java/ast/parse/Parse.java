package ast.parse;

import static jscan.tokenize.T.TOKEN_EOF;
import static jscan.tokenize.T.TOKEN_IDENT;
import static jscan.tokenize.T.T_SEMI_COLON;

import java.util.ArrayList;
import java.util.List;

import ast.builders.TypeMerger;
import ast.symtab.CSymbol;
import ast.symtab.CSymbolBase;
import ast.tree.Declarator;
import ast.tree.ExternalDeclaration;
import ast.tree.Function;
import ast.tree.TranslationUnit;
import ast.types.CType;
import jscan.parse.RingBuf;
import jscan.parse.Tokenlist;
import jscan.symtab.Ident;
import jscan.symtab.KeywordsInits;
import jscan.symtab.ScopeLevels;
import jscan.symtab.Symtab;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;

public class Parse {

  // main thing's
  private final Tokenlist tokenlist;
  private Token tok;

  // need for labels, also for binding local variable's
  private Function currentFn;

  // symbol-tables
  private Symtab<Ident, CSymbol> symbols;
  private Symtab<Ident, CType> typedefs;
  private Symtab<Ident, CSymbol> tags;

  // location, error-handling
  private String lastloc;
  private List<Token> ringBuffer;
  private Token prevtok;

  public Parse(List<Token> tokens) {
    this.tokenlist = new Tokenlist(tokens);
    initParser();
  }

  public Parse(Tokenlist tokenlist) {
    this.tokenlist = tokenlist;
    initParser();
  }

  public Symtab<Ident, CSymbol> getSymbols() {
    return symbols;
  }

  public Symtab<Ident, CSymbol> getTags() {
    return tags;
  }

  public Token tok() {
    return tok;
  }

  public Function getCurrentFn() {
    return currentFn;
  }

  public void defineSym(CSymbol sym) {

    if (sym.base == CSymbolBase.SYM_TYPEDEF) {
      defineTypedef(sym.name, sym.type);
      return;
    }

    CSymbol prevsym = symbols.getsymFromCurrentScope(sym.name);
    if (prevsym != null) {
      if (sym.isFunction() && prevsym.type.isEqualTo(sym.type)) {
        // TODO: normal prototype logic.
      } else {
        perror("redefinition, previous defined here: " + prevsym.getLocationToString());
      }
    }

    if (currentFn != null) {
      currentFn.addLocal(sym);
    }

    symbols.addsym(sym.name, sym);
  }

  public void log(String what) {
    // TODO:
  }

  // TODO: defineLocal()
  // TODO: defineGlobl()
  // TODO: defineProto()
  // TODO: saveStrLabel()

  private void defineTypedef(Ident key, CType type) {
    //System.out.println("typedef " + type.toString() + " " + key.getName());

    CType prevsym = typedefs.getsymFromCurrentScope(key);
    if (prevsym != null) {
      if (!prevsym.isEqualTo(type)) {
        perror("typedefed-name redefinition with different kind of type");
      }
    }
    typedefs.addsym(key, type);
  }

  public void defineTag(CSymbol sym) {
    tags.addsym(sym.name, sym);
  }

  public CSymbol getTagFromCurrentScope(Ident name) {
    return tags.getsymFromCurrentScope(name);
  }

  public CSymbol getSym(Ident name) {
    return symbols.getsym(name);
  }

  public CType getTypedefName(Ident id) {
    return typedefs.getsym(id);
  }

  public CSymbol getTag(Ident name) {
    return tags.getsym(name);
  }

  //TODO:SEMANTIC
  //
  public void pushscope(ScopeLevels level) {
    tags.pushscope(level);
    symbols.pushscope(level);
    typedefs.pushscope(level);
  }

  public void popscope() {
    tags.popscope();
    symbols.popscope();
    typedefs.popscope();
  }

  public boolean isFileScope() {
    return symbols.getScopes().get(0).getLevel() == ScopeLevels.FILE_SCOPE;
  }

  //
  // TODO:SEMANTIC

  private void initParser() {
    KeywordsInits.initIdents();
    initDefaults();
    initScopes();
    move();
  }

  private void initDefaults() {
    this.currentFn = null;
    this.ringBuffer = new ArrayList<Token>(0);
    this.lastloc = "";
  }

  private void initScopes() {
    this.symbols = new Symtab<>();
    this.tags = new Symtab<>();
    this.typedefs = new Symtab<>();
  }

  public String getLastLoc() {
    return lastloc;
  }

  public Token getPrevtok() {
    return prevtok;
  }

  public void setPrevtok(Token prevtok) {
    this.prevtok = prevtok;
  }

  public List<Token> getRingBuffer() {
    return ringBuffer;
  }

  public T tp() {
    return tok.getType();
  }

  public void move() {

    tok = tokenlist.next();
    if (tok.is(T.TOKEN_STREAMBEGIN) || tok.is(T.TOKEN_STREAMEND)) {
      tok = tokenlist.next();
    }

    addLoc();
  }

  public Token moveget() {
    Token tok = tok();
    move();
    return tok;
  }

  private void addLoc() {

    if (ringBuffer.size() >= 230) {
      ringBuffer.remove(0);
    }
    ringBuffer.add(tok);

    lastloc = (prevtok == null ? tok.loc() : prevtok.loc());
    prevtok = tok;
  }

  //////////////////////////////////////////////////////////////////////

  public void perror(String m) {
    StringBuilder sb = new StringBuilder();
    sb.append("error: " + m + "\n");
    sb.append("  --> " + lastloc + "\n\n");
    sb.append(RingBuf.ringBufferToStringLines(ringBuffer) + "\n");

    throw new AstParseException(sb.toString());
  }

  public void pwarning(String m) {
    StringBuilder sb = new StringBuilder();
    sb.append("warning: " + m + "\n");
    sb.append("  --> " + lastloc + "\n\n");
    sb.append(RingBuf.ringBufferToStringLines(ringBuffer) + "\n");
    //System.out.println(sb.toString());
  }

  public Token checkedMove(Ident expect) {
    if (!tok.isIdent(expect)) {
      perror("expect id: " + expect.getName() + ", but was: " + tok.getValue());
    }
    Token saved = tok();
    move();
    return saved;
  }

  public Ident getIdent() {
    if (!tok.is(TOKEN_IDENT)) {
      perror("expect ident, but was: " + tok.getValue());
    }
    Token saved = tok;
    move();
    final Ident ident = saved.getIdent();
    if (ident.isBuiltin()) {
      perror("unexpected builtin ident: " + ident.getName());
    }
    return ident;
  }

  public Token checkedMove(T expect) {
    if (tp() != expect) {
      perror("expect: " + expect.toString() + ", but was: " + tok.getValue());
    }
    Token saved = tok;
    move();
    return saved;
  }

  public boolean moveOptional(T t) {
    if ((tp() == t)) {
      move();
      return true;
    }
    return false;
  }

  public void unexpectedEof() {
    if (tok.is(TOKEN_EOF)) {
      perror("EOF unexpected at this context");
    }
  }

  public void unimplemented(String what) {
    perror("unimplemented: " + what);
  }

  public void unreachable(String what) {
    perror("unreachable: " + what);
  }

  public Token peek() {
    return tokenlist.peek();
  }

  public boolean is(T t) {
    return tok.is(t);
  }

  public Token lparen() {
    return checkedMove(T.T_LEFT_PAREN);
  }

  public Token rparen() {
    return checkedMove(T.T_RIGHT_PAREN);
  }

  public Token lbracket() {
    return checkedMove(T.T_LEFT_BRACKET);
  }

  public Token rbracket() {
    return checkedMove(T.T_RIGHT_BRACKET);
  }

  public Token semicolon() {
    return checkedMove(T_SEMI_COLON);
  }

  public void lbrace() {
    checkedMove(T.T_LEFT_BRACE);
  }

  public void rbrace() {
    checkedMove(T.T_RIGHT_BRACE);
  }

  public void comma() {
    checkedMove(T.T_COMMA);
  }

  //@formatter:off
  public boolean isDeclSpecStart() {
    return Pcheckers.isStorageClassSpec(tok)
        || Pcheckers.isTypeSpec(tok)
        || Pcheckers.isTypeQual(tok)
        || Pcheckers.isFuncSpec(tok)
        || Pcheckers.isEnumSpecStart(tok)
        || Pcheckers.isStructOrUnionSpecStart(tok)
        || Pcheckers.isStaticAssert(tok)
        || isTypedefName(tok());
  }
  
  // this one need for cast expression and compound literal
  // XXX : fix
  public boolean isDeclSpecStart(Token what) {
    return Pcheckers.isStorageClassSpec(what)
        || Pcheckers.isTypeSpec(what)
        || Pcheckers.isTypeQual(what)
        || Pcheckers.isFuncSpec(what)
        || Pcheckers.isEnumSpecStart(what)
        || Pcheckers.isStructOrUnionSpecStart(what)
        || Pcheckers.isStaticAssert(what)
        || isTypedefName(what);
  }
  //@formatter:on

  public boolean isAttributeStartGnuc() {
    return Pcheckers.isAttributeStartGnuc(tok);
  }

  public boolean isAttributeStartC2X() {
    Token currtok = tok();
    Token nexttok = peek();
    // [[  ...  ]]
    return currtok.is(T.T_LEFT_BRACKET) && nexttok.is(T.T_LEFT_BRACKET);
  }

  public boolean isAsmStart() {
    return Pcheckers.isAsmStart(tok);
  }

  public boolean isUserDefinedId() {
    return tok.is(TOKEN_IDENT) && !tok.isBuiltinIdent();
  }

  public boolean isUserDefinedId(Token what) {
    return what.is(TOKEN_IDENT) && !what.isBuiltinIdent();
  }

  private boolean isTypedefName(Token tok) {
    if (!isUserDefinedId(tok)) {
      return false;
    }
    CType tp = typedefs.getsym(tok.getIdent());
    return tp != null;
  }

  public boolean isEof() {
    return tok.is(T.TOKEN_EOF);
  }

  public CType parseTypename() {

    CType base = new ParseBaseType(this).parse();
    Declarator decl = new ParseDeclarator(this).parse();
    CType type = TypeMerger.build(base, decl);

    if (!decl.isAbstract()) {
      perror("expect abstract declarator.");
    }
    return type;

  }

  public Tokenlist getTokenlist() {
    return tokenlist;
  }

  public void setCurrentFn(Function currentFn) {
    this.currentFn = currentFn;
  }

  public void restoreState(ParseState parseState) {
    this.tokenlist.setOffset(parseState.getTokenlistOffset());
    this.tok = parseState.getTok();
    this.currentFn = parseState.getCurrentFn();
    this.ringBuffer = new ArrayList<Token>(parseState.getRingBuffer());
    this.lastloc = parseState.getLastloc();
    this.prevtok = parseState.getPrevtok();
  }

  ///////////////////////////////////////////////////////////////////
  // ENTRY

  private void moveStraySemicolon() {
    while (tp() == T.T_SEMI_COLON) {
      move();
    }
  }

  public TranslationUnit parse_unit() {
    TranslationUnit tu = new TranslationUnit();
    pushscope(ScopeLevels.FILE_SCOPE);

    // top-level
    moveStraySemicolon();

    while (!tok.is(TOKEN_EOF)) {

      // before each function or global declaration
      moveStraySemicolon();

      ExternalDeclaration ed = new ParseExternal(this).parse();
      tu.push(ed);
    }

    popscope();
    return tu;
  }

}
