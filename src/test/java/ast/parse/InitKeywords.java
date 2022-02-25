package ast.parse;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;

import static ast.symtab.IdentMap.*;

public class InitKeywords {

  private static Ident g(String name, int ns) {
    return Hash_ident.getHashedIdent(name, ns);
  }

  //@formatter:off
  public static void initIdentMap() {
    g("auto"            , C89|C99|C11|C2X );
    g("break"           , C89|C99|C11|C2X );
    g("case"            , C89|C99|C11|C2X );
    g("char"            , C89|C99|C11|C2X );
    g("const"           , C89|C99|C11|C2X );
    g("continue"        , C89|C99|C11|C2X );
    g("default"         , C89|C99|C11|C2X );
    g("do"              , C89|C99|C11|C2X );
    g("double"          , C89|C99|C11|C2X );
    g("else"            , C89|C99|C11|C2X );
    g("enum"            , C89|C99|C11|C2X );
    g("extern"          , C89|C99|C11|C2X );
    g("float"           , C89|C99|C11|C2X );
    g("for"             , C89|C99|C11|C2X );
    g("goto"            , C89|C99|C11|C2X );
    g("if"              , C89|C99|C11|C2X );
    g("inline"          , ___|C99|C11|C2X );
    g("int"             , C89|C99|C11|C2X );
    g("long"            , C89|C99|C11|C2X );
    g("register"        , C89|C99|C11|C2X );
    g("restrict"        , ___|C99|C11|C2X );
    g("return"          , C89|C99|C11|C2X );
    g("short"           , C89|C99|C11|C2X );
    g("signed"          , C89|C99|C11|C2X );
    g("sizeof"          , C89|C99|C11|C2X );
    g("static"          , C89|C99|C11|C2X );
    g("struct"          , C89|C99|C11|C2X );
    g("switch"          , C89|C99|C11|C2X );
    g("typedef"         , C89|C99|C11|C2X );
    g("union"           , C89|C99|C11|C2X );
    g("unsigned"        , C89|C99|C11|C2X );
    g("void"            , C89|C99|C11|C2X );
    g("volatile"        , C89|C99|C11|C2X );
    g("while"           , C89|C99|C11|C2X );
    g("_Alignas"        , ___|___|C11|C2X );
    g("_Alignof"        , ___|___|C11|C2X );
    g("_Atomic"         , ___|___|C11|C2X );
    g("_Bool"           , ___|C99|C11|C2X );
    g("_Complex"        , ___|C99|C11|C2X );
    g("_Decimal128"     , ___|___|___|C2X );
    g("_Decimal32"      , ___|___|___|C2X );
    g("_Decimal64"      , ___|___|___|C2X );
    g("_Generic"        , ___|___|C11|C2X );
    g("_Imaginary"      , ___|C99|C11|C2X );
    g("_Noreturn"       , ___|___|C11|C2X );
    g("_Static_assert"  , ___|___|C11|C2X );
    g("_Thread_local"   , ___|___|C11|C2X );
    //
    g("asm"             , NS_RID|NS_GNU);
    g("__asm"           , NS_RID|NS_GNU);
    g("__asm__"         , NS_RID|NS_GNU);
    g("__alignof"       , NS_RID|NS_GNU);
    g("__alignof__"     , NS_RID|NS_GNU);
    g("__attribute"     , NS_RID|NS_GNU);
    g("__attribute__"   , NS_RID|NS_GNU);
    g("__complex"       , NS_RID|NS_GNU);
    g("__complex__"     , NS_RID|NS_GNU);
    g("__const"         , NS_RID|NS_GNU);
    g("__const__"       , NS_RID|NS_GNU);
    g("__inline"        , NS_RID|NS_GNU);
    g("__inline__"      , NS_RID|NS_GNU);
    g("__restrict"      , NS_RID|NS_GNU);
    g("__restrict__"    , NS_RID|NS_GNU);
    g("__signed"        , NS_RID|NS_GNU);
    g("__signed__"      , NS_RID|NS_GNU);
    g("__thread"        , NS_RID|NS_GNU);
    g("typeof"          , NS_RID|NS_GNU);
    g("__typeof"        , NS_RID|NS_GNU);
    g("__typeof__"      , NS_RID|NS_GNU);
    g("__volatile"      , NS_RID|NS_GNU);
    g("__volatile__"    , NS_RID|NS_GNU);
    g("__label__"       , NS_RID|NS_GNU);
    g("__extension__"   , NS_RID|NS_GNU);
  }
  
}
