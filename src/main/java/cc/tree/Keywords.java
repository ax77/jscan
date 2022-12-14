package cc.tree;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;

public final class Keywords {

  /// you may put here ONLY keywords... nothings else...
  /// each field will be processed by reflection at ParserMain
  /// initialization, and it will be formed as an
  /// keyword in a symbol-table.

  private static Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 32);
  }

  //@formatter:off

  public static final Ident auto_ident             = g("auto"            );
  public static final Ident break_ident            = g("break"           );
  public static final Ident case_ident             = g("case"            );
  public static final Ident char_ident             = g("char"            );
  public static final Ident const_ident            = g("const"           );
  public static final Ident continue_ident         = g("continue"        );
  public static final Ident default_ident          = g("default"         );
  public static final Ident do_ident               = g("do"              );
  public static final Ident double_ident           = g("double"          );
  public static final Ident else_ident             = g("else"            );
  public static final Ident enum_ident             = g("enum"            );
  public static final Ident extern_ident           = g("extern"          );
  public static final Ident float_ident            = g("float"           );
  public static final Ident for_ident              = g("for"             );
  public static final Ident goto_ident             = g("goto"            );
  public static final Ident if_ident               = g("if"              );
  public static final Ident inline_ident           = g("inline"          );
  public static final Ident int_ident              = g("int"             );
  public static final Ident long_ident             = g("long"            );
  public static final Ident register_ident         = g("register"        );
  public static final Ident restrict_ident         = g("restrict"        );
  public static final Ident return_ident           = g("return"          );
  public static final Ident short_ident            = g("short"           );
  public static final Ident signed_ident           = g("signed"          );
  public static final Ident sizeof_ident           = g("sizeof"          );
  public static final Ident static_ident           = g("static"          );
  public static final Ident struct_ident           = g("struct"          );
  public static final Ident switch_ident           = g("switch"          );
  public static final Ident typedef_ident          = g("typedef"         );
  public static final Ident union_ident            = g("union"           );
  public static final Ident unsigned_ident         = g("unsigned"        );
  public static final Ident void_ident             = g("void"            );
  public static final Ident volatile_ident         = g("volatile"        );
  public static final Ident while_ident            = g("while"           );
  public static final Ident _Alignas_ident         = g("_Alignas"        );
  public static final Ident _Alignof_ident         = g("_Alignof"        );
  public static final Ident _Atomic_ident          = g("_Atomic"         );
  public static final Ident _Bool_ident            = g("_Bool"           );
  public static final Ident _Complex_ident         = g("_Complex"        );
  public static final Ident _Decimal128_ident      = g("_Decimal128"     );
  public static final Ident _Decimal32_ident       = g("_Decimal32"      );
  public static final Ident _Decimal64_ident       = g("_Decimal64"      );
  public static final Ident _Generic_ident         = g("_Generic"        );
  public static final Ident _Imaginary_ident       = g("_Imaginary"      );
  public static final Ident _Noreturn_ident        = g("_Noreturn"       );
  public static final Ident _Static_assert_ident   = g("_Static_assert"  );
  public static final Ident _Thread_local_ident    = g("_Thread_local"   );
  
  public static final Ident asm_ident              = g("asm"             );
  public static final Ident __asm_ident            = g("__asm"           );
  public static final Ident __asm___ident          = g("__asm__"         );
  public static final Ident __alignof_ident        = g("__alignof"       );
  public static final Ident __alignof___ident      = g("__alignof__"     );
  public static final Ident __attribute_ident      = g("__attribute"     );
  public static final Ident __attribute___ident    = g("__attribute__"   );
  public static final Ident __complex_ident        = g("__complex"       );
  public static final Ident __complex___ident      = g("__complex__"     );
  public static final Ident __const_ident          = g("__const"         );
  public static final Ident __const___ident        = g("__const__"       );
  public static final Ident __inline_ident         = g("__inline"        );
  public static final Ident __inline___ident       = g("__inline__"      );
  public static final Ident __restrict_ident       = g("__restrict"      );
  public static final Ident __restrict___ident     = g("__restrict__"    );
  public static final Ident __signed_ident         = g("__signed"        );
  public static final Ident __signed___ident       = g("__signed__"      );
  public static final Ident __thread_ident         = g("__thread"        );
  public static final Ident typeof_ident           = g("typeof"          );
  public static final Ident __typeof_ident         = g("__typeof"        );
  public static final Ident __typeof___ident       = g("__typeof__"      );
  public static final Ident __volatile_ident       = g("__volatile"      );
  public static final Ident __volatile___ident     = g("__volatile__"    );
  public static final Ident __label___ident        = g("__label__"       );
  public static final Ident __extension___ident    = g("__extension__"   );
  
  //@formatter:on

}
