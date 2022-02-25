package ast.symtab;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;

public final class IdentMap {

  public static final int ___ = 0;
  public static final int C89 = 1 << 0;
  public static final int C99 = 1 << 1;
  public static final int C11 = 1 << 2;
  public static final int C2X = 1 << 3;
  public static final int NS_GNU = 1 << 4;
  public static final int NS_MSV = 1 << 5;
  public static final int NS_RID = 1 << 6;
  public static final int NS_ALL = C89 | C99 | C11;

  //@formatter:off
  public static final Ident auto_ident             = g("auto"            , C89|C99|C11|C2X );
  public static final Ident break_ident            = g("break"           , C89|C99|C11|C2X );
  public static final Ident case_ident             = g("case"            , C89|C99|C11|C2X );
  public static final Ident char_ident             = g("char"            , C89|C99|C11|C2X );
  public static final Ident const_ident            = g("const"           , C89|C99|C11|C2X );
  public static final Ident continue_ident         = g("continue"        , C89|C99|C11|C2X );
  public static final Ident default_ident          = g("default"         , C89|C99|C11|C2X );
  public static final Ident do_ident               = g("do"              , C89|C99|C11|C2X );
  public static final Ident double_ident           = g("double"          , C89|C99|C11|C2X );
  public static final Ident else_ident             = g("else"            , C89|C99|C11|C2X );
  public static final Ident enum_ident             = g("enum"            , C89|C99|C11|C2X );
  public static final Ident extern_ident           = g("extern"          , C89|C99|C11|C2X );
  public static final Ident float_ident            = g("float"           , C89|C99|C11|C2X );
  public static final Ident for_ident              = g("for"             , C89|C99|C11|C2X );
  public static final Ident goto_ident             = g("goto"            , C89|C99|C11|C2X );
  public static final Ident if_ident               = g("if"              , C89|C99|C11|C2X );
  public static final Ident inline_ident           = g("inline"          , ___|C99|C11|C2X );
  public static final Ident int_ident              = g("int"             , C89|C99|C11|C2X );
  public static final Ident long_ident             = g("long"            , C89|C99|C11|C2X );
  public static final Ident register_ident         = g("register"        , C89|C99|C11|C2X );
  public static final Ident restrict_ident         = g("restrict"        , ___|C99|C11|C2X );
  public static final Ident return_ident           = g("return"          , C89|C99|C11|C2X );
  public static final Ident short_ident            = g("short"           , C89|C99|C11|C2X );
  public static final Ident signed_ident           = g("signed"          , C89|C99|C11|C2X );
  public static final Ident sizeof_ident           = g("sizeof"          , C89|C99|C11|C2X );
  public static final Ident static_ident           = g("static"          , C89|C99|C11|C2X );
  public static final Ident struct_ident           = g("struct"          , C89|C99|C11|C2X );
  public static final Ident switch_ident           = g("switch"          , C89|C99|C11|C2X );
  public static final Ident typedef_ident          = g("typedef"         , C89|C99|C11|C2X );
  public static final Ident union_ident            = g("union"           , C89|C99|C11|C2X );
  public static final Ident unsigned_ident         = g("unsigned"        , C89|C99|C11|C2X );
  public static final Ident void_ident             = g("void"            , C89|C99|C11|C2X );
  public static final Ident volatile_ident         = g("volatile"        , C89|C99|C11|C2X );
  public static final Ident while_ident            = g("while"           , C89|C99|C11|C2X );
  public static final Ident _Alignas_ident         = g("_Alignas"        , ___|___|C11|C2X );
  public static final Ident _Alignof_ident         = g("_Alignof"        , ___|___|C11|C2X );
  public static final Ident _Atomic_ident          = g("_Atomic"         , ___|___|C11|C2X );
  public static final Ident _Bool_ident            = g("_Bool"           , ___|C99|C11|C2X );
  public static final Ident _Complex_ident         = g("_Complex"        , ___|C99|C11|C2X );
  public static final Ident _Decimal128_ident      = g("_Decimal128"     , ___|___|___|C2X );
  public static final Ident _Decimal32_ident       = g("_Decimal32"      , ___|___|___|C2X );
  public static final Ident _Decimal64_ident       = g("_Decimal64"      , ___|___|___|C2X );
  public static final Ident _Generic_ident         = g("_Generic"        , ___|___|C11|C2X );
  public static final Ident _Imaginary_ident       = g("_Imaginary"      , ___|C99|C11|C2X );
  public static final Ident _Noreturn_ident        = g("_Noreturn"       , ___|___|C11|C2X );
  public static final Ident _Static_assert_ident   = g("_Static_assert"  , ___|___|C11|C2X );
  public static final Ident _Thread_local_ident    = g("_Thread_local"   , ___|___|C11|C2X );



  // public static final Ident define_ident                     = getHashedIdent("define"                    , NS_ALL|NS_PRP);
  // public static final Ident defined_ident                    = getHashedIdent("defined"                   , NS_ALL|NS_PRP);
  // public static final Ident elif_ident                       = getHashedIdent("elif"                      , NS_ALL|NS_PRP);
  // public static final Ident endif_ident                      = getHashedIdent("endif"                     , NS_ALL|NS_PRP);
  // public static final Ident error_ident                      = getHashedIdent("error"                     , NS_ALL|NS_PRP);
  // public static final Ident ifdef_ident                      = getHashedIdent("ifdef"                     , NS_ALL|NS_PRP);
  // public static final Ident ifndef_ident                     = getHashedIdent("ifndef"                    , NS_ALL|NS_PRP);
  // public static final Ident include_ident                    = getHashedIdent("include"                   , NS_ALL|NS_PRP);
  // public static final Ident line_ident                       = getHashedIdent("line"                      , NS_ALL|NS_PRP);
  // public static final Ident pragma_ident                     = getHashedIdent("pragma"                    , NS_ALL|NS_PRP);
  // public static final Ident undef_ident                      = getHashedIdent("undef"                     , NS_ALL|NS_PRP);
  // public static final Ident warning_ident                    = getHashedIdent("warning"                   , NS_ALL|NS_PRP);
  // public static final Ident __LINE___ident                   = getHashedIdent("__LINE__"                  , NS_ALL|NS_PRP);
  // public static final Ident __FILE___ident                   = getHashedIdent("__FILE__"                  , NS_ALL|NS_PRP);
  // public static final Ident __DATE___ident                   = getHashedIdent("__DATE__"                  , NS_ALL|NS_PRP);
  // public static final Ident __TIME___ident                   = getHashedIdent("__TIME__"                  , NS_ALL|NS_PRP);
  // public static final Ident include_next_ident               = getHashedIdent("include_next"              , NS_ALL|NS_PRP);
  // public static final Ident __PRETTY_FUNCTION___ident        = getHashedIdent("__PRETTY_FUNCTION__"       , NS_ALL|NS_PRP);
  // public static final Ident __COUNTER___ident                = getHashedIdent("__COUNTER__"               , NS_ALL|NS_PRP);
  // public static final Ident __FUNCTION___ident               = getHashedIdent("__FUNCTION__"              , NS_ALL|NS_PRP);
  // public static final Ident __FUNCSIG___ident                = getHashedIdent("__FUNCSIG__"               , NS_ALL|NS_PRP);
  // public static final Ident __FUNCDNAME___ident              = getHashedIdent("__FUNCDNAME__"             , NS_ALL|NS_PRP);


  public static final Ident asm_ident                        = g("asm"                       , NS_RID|NS_GNU);
  public static final Ident __asm_ident                      = g("__asm"                     , NS_RID|NS_GNU);
  public static final Ident __asm___ident                    = g("__asm__"                   , NS_RID|NS_GNU);
  public static final Ident __alignof_ident                  = g("__alignof"                 , NS_RID|NS_GNU);
  public static final Ident __alignof___ident                = g("__alignof__"               , NS_RID|NS_GNU);
  public static final Ident __attribute_ident                = g("__attribute"               , NS_RID|NS_GNU);
  public static final Ident __attribute___ident              = g("__attribute__"             , NS_RID|NS_GNU);
  public static final Ident __complex_ident                  = g("__complex"                 , NS_RID|NS_GNU);
  public static final Ident __complex___ident                = g("__complex__"               , NS_RID|NS_GNU);
  public static final Ident __const_ident                    = g("__const"                   , NS_RID|NS_GNU);
  public static final Ident __const___ident                  = g("__const__"                 , NS_RID|NS_GNU);
  public static final Ident __inline_ident                   = g("__inline"                  , NS_RID|NS_GNU);
  public static final Ident __inline___ident                 = g("__inline__"                , NS_RID|NS_GNU);
  public static final Ident __restrict_ident                 = g("__restrict"                , NS_RID|NS_GNU);
  public static final Ident __restrict___ident               = g("__restrict__"              , NS_RID|NS_GNU);
  public static final Ident __signed_ident                   = g("__signed"                  , NS_RID|NS_GNU);
  public static final Ident __signed___ident                 = g("__signed__"                , NS_RID|NS_GNU);
  public static final Ident __thread_ident                   = g("__thread"                  , NS_RID|NS_GNU);
  public static final Ident typeof_ident                     = g("typeof"                    , NS_RID|NS_GNU);
  public static final Ident __typeof_ident                   = g("__typeof"                  , NS_RID|NS_GNU);
  public static final Ident __typeof___ident                 = g("__typeof__"                , NS_RID|NS_GNU);
  public static final Ident __volatile_ident                 = g("__volatile"                , NS_RID|NS_GNU);
  public static final Ident __volatile___ident               = g("__volatile__"              , NS_RID|NS_GNU);
  public static final Ident __label___ident                  = g("__label__"                 , NS_RID|NS_GNU);
  public static final Ident __extension___ident              = g("__extension__"             , NS_RID|NS_GNU);


  // public static final Ident __declspec_ident                 = getHashedIdent("__declspec"                , NS_RID|NS_MSV);
  // public static final Ident __cdecl_ident                    = getHashedIdent("__cdecl"                   , NS_RID|NS_MSV);
  // public static final Ident __stdcall_ident                  = getHashedIdent("__stdcall"                 , NS_RID|NS_MSV);
  // public static final Ident __fastcall_ident                 = getHashedIdent("__fastcall"                , NS_RID|NS_MSV);
  // public static final Ident __thiscall_ident                 = getHashedIdent("__thiscall"                , NS_RID|NS_MSV);
  // public static final Ident __regcall_ident                  = getHashedIdent("__regcall"                 , NS_RID|NS_MSV);
  // public static final Ident __vectorcall_ident               = getHashedIdent("__vectorcall"              , NS_RID|NS_MSV);
  // public static final Ident __forceinline_ident              = getHashedIdent("__forceinline"             , NS_RID|NS_MSV);
  // public static final Ident __unaligned_ident                = getHashedIdent("__unaligned"               , NS_RID|NS_MSV);
  // public static final Ident __super_ident                    = getHashedIdent("__super"                   , NS_RID|NS_MSV);
  // public static final Ident _asm_ident                       = getHashedIdent("_asm"                      , NS_RID|NS_MSV);
  // public static final Ident _int8_ident                      = getHashedIdent("_int8"                     , NS_RID|NS_MSV);
  // public static final Ident __int8_ident                     = getHashedIdent("__int8"                    , NS_RID|NS_MSV);
  // public static final Ident _int16_ident                     = getHashedIdent("_int16"                    , NS_RID|NS_MSV);
  // public static final Ident __int16_ident                    = getHashedIdent("__int16"                   , NS_RID|NS_MSV);
  // public static final Ident _int32_ident                     = getHashedIdent("_int32"                    , NS_RID|NS_MSV);
  // public static final Ident __int32_ident                    = getHashedIdent("__int32"                   , NS_RID|NS_MSV);
  // public static final Ident _cdecl_ident                     = getHashedIdent("_cdecl"                    , NS_RID|NS_MSV);
  // public static final Ident _fastcall_ident                  = getHashedIdent("_fastcall"                 , NS_RID|NS_MSV);
  // public static final Ident _stdcall_ident                   = getHashedIdent("_stdcall"                  , NS_RID|NS_MSV);
  // public static final Ident _thiscall_ident                  = getHashedIdent("_thiscall"                 , NS_RID|NS_MSV);
  // public static final Ident _vectorcall_ident                = getHashedIdent("_vectorcall"               , NS_RID|NS_MSV);
  // public static final Ident _inline_ident                    = getHashedIdent("_inline"                   , NS_RID|NS_MSV);
  // public static final Ident _declspec_ident                  = getHashedIdent("_declspec"                 , NS_RID|NS_MSV);
  // public static final Ident once_ident                       = getHashedIdent("once"                      , NS_RID|NS_MSV);
  // public static final Ident pack_ident                       = getHashedIdent("pack"                      , NS_RID|NS_MSV);
  // public static final Ident push_ident                       = getHashedIdent("push"                      , NS_RID|NS_MSV);
  // public static final Ident pop_ident                        = getHashedIdent("pop"                       , NS_RID|NS_MSV);
  //@formatter:on

  private static Ident g(String name, int ns) {
    return Hash_ident.getHashedIdent(name, ns);
  }

}
