package specs;

public abstract class PredefinedBuffer {

  public static final boolean CC_SETTINGS_USE_GCC = true;
  public static final boolean CC_SETTINGS_USE_CLANG = false;
  public static final boolean CC_SETTINGS_USE_APPLE = false;
  public static final boolean CC_SETTINGS_USE_WIN32 = false;
  public static final boolean CC_SETTINGS_USE_I386 = false;
  public static final StringBuilder sb = new StringBuilder();

  public static final String getPredefinedBuffer() {
    return sb.toString() + "\n\n";
  }

  private static void addline(String string) {
    sb.append(string);
    sb.append("\n");
  }

  static {
    addline("#ifndef CC_SETTINGS_DEFINED");
    addline("#define CC_SETTINGS_DEFINED");

    // STDC
    // 
    addline("#define __STDC__ 1");
    addline("#define __STDC_VERSION__ 201112L");
    addline("#define __STDC_NO_THREADS__ 1");
    addline("#define __STDC_NO_ATOMICS__ 1");
    addline("#define __STDC_NO_COMPLEX__ 1");
    addline("#define __STDC_HOSTED__ 1");
    addline("#define __STDC_UTF_16__ 1");
    addline("#define __STDC_UTF_32__ 1");

    // ARCH
    // 
    addline("#define _LP64 1");
    addline("#define __LP64__ 1");
    addline("#define __x86_64 1");
    addline("#define __x86_64__ 1");
    addline("#define __amd64 1");
    addline("#define __amd64__ 1");

    // BYTE_ORDER
    // 
    addline("#define __BYTE_ORDER__ __ORDER_LITTLE_ENDIAN__");
    addline("#define __CHAR16_TYPE__ unsigned short");
    addline("#define __CHAR32_TYPE__ unsigned int");
    addline("#define __CHAR_BIT__ 8");
    addline("#define __LITTLE_ENDIAN__ 1");
    addline("#define __ORDER_BIG_ENDIAN__ 4321");
    addline("#define __ORDER_LITTLE_ENDIAN__ 1234");

    // SIZEOF
    // 
    addline("#define __SIZEOF_DOUBLE__ 8");
    addline("#define __SIZEOF_FLOAT__ 4");
    addline("#define __SIZEOF_INT128__ 16");
    addline("#define __SIZEOF_INT__ 4");
    addline("#define __SIZEOF_LONG_DOUBLE__ 16");
    addline("#define __SIZEOF_LONG_LONG__ 8");
    addline("#define __SIZEOF_LONG__ 8");
    addline("#define __SIZEOF_POINTER__ 8");
    addline("#define __SIZEOF_PTRDIFF_T__ 8");
    addline("#define __SIZEOF_SHORT__ 2");
    addline("#define __SIZEOF_SIZE_T__ 8");
    addline("#define __SIZEOF_WCHAR_T__ 4");
    addline("#define __SIZEOF_WINT_T__ 4");

    // SIZE
    // 
    addline("#define __SIZE_MAX__ 18446744073709551615UL");
    addline("#define __SIZE_TYPE__ long unsigned int");
    addline("#define __SIZE_WIDTH__ 64");

    // POINTERS
    // 
    addline("#define __POINTER_WIDTH__ 64");
    addline("#define __INTPTR_MAX__ 9223372036854775807L");
    addline("#define __INTPTR_TYPE__ long int");
    addline("#define __INTPTR_WIDTH__ 64");
    addline("#define __UINTPTR_MAX__ 18446744073709551615UL");
    addline("#define __UINTPTR_TYPE__ long unsigned int");
    addline("#define __UINTPTR_WIDTH__ 64");
    addline("#define __PTRDIFF_MAX__ 9223372036854775807L");
    addline("#define __PTRDIFF_TYPE__ long int");
    addline("#define __PTRDIFF_WIDTH__ 64");

    // INT
    // 
    addline("#define __SCHAR_MAX__ 127");
    addline("#define __SHRT_MAX__ 32767");
    addline("#define __INT_MAX__ 2147483647");
    addline("#define __INTMAX_TYPE__ long int");
    addline("#define __INTMAX_WIDTH__ 64");
    addline("#define __LONG_MAX__ 9223372036854775807L");
    addline("#define __LONG_LONG_MAX__ 9223372036854775807LL");
    addline("#define __INTMAX_MAX__ 9223372036854775807L");
    addline("#define __INT8_MAX__ 127");
    addline("#define __INT8_TYPE__ signed char");
    addline("#define __INT16_MAX__ 32767");
    addline("#define __INT16_TYPE__ short");
    addline("#define __INT32_MAX__ 2147483647");
    addline("#define __INT32_TYPE__ int");
    addline("#define __INT64_MAX__ 9223372036854775807LL");
    addline("#define __INT64_TYPE__ long long int");
    addline("#define __INT_FAST8_MAX__ 127");
    addline("#define __INT_FAST8_TYPE__ signed char");
    addline("#define __INT_FAST16_MAX__ 32767");
    addline("#define __INT_FAST16_TYPE__ short");
    addline("#define __INT_FAST32_MAX__ 2147483647");
    addline("#define __INT_FAST32_TYPE__ int");
    addline("#define __INT_FAST64_MAX__ 9223372036854775807LL");
    addline("#define __INT_FAST64_TYPE__ long long int");
    addline("#define __INT_LEAST8_MAX__ 127");
    addline("#define __INT_LEAST8_TYPE__ signed char");
    addline("#define __INT_LEAST16_MAX__ 32767");
    addline("#define __INT_LEAST16_TYPE__ short");
    addline("#define __INT_LEAST32_MAX__ 2147483647");
    addline("#define __INT_LEAST32_TYPE__ int");
    addline("#define __INT_LEAST64_MAX__ 9223372036854775807LL");
    addline("#define __INT_LEAST64_TYPE__ long long int");

    // UINT
    // 
    addline("#define __UINTMAX_MAX__ 18446744073709551615UL");
    addline("#define __UINTMAX_TYPE__ long unsigned int");
    addline("#define __UINTMAX_WIDTH__ 64");
    addline("#define __UINT8_MAX__ 255");
    addline("#define __UINT8_TYPE__ unsigned char");
    addline("#define __UINT16_MAX__ 65535");
    addline("#define __UINT16_TYPE__ unsigned short");
    addline("#define __UINT32_MAX__ 4294967295U");
    addline("#define __UINT32_TYPE__ unsigned int");
    addline("#define __UINT64_MAX__ 18446744073709551615ULL");
    addline("#define __UINT64_TYPE__ long long unsigned int");
    addline("#define __UINT_FAST8_MAX__ 255");
    addline("#define __UINT_FAST8_TYPE__ unsigned char");
    addline("#define __UINT_FAST16_MAX__ 65535");
    addline("#define __UINT_FAST16_TYPE__ unsigned short");
    addline("#define __UINT_FAST32_MAX__ 4294967295U");
    addline("#define __UINT_FAST32_TYPE__ unsigned int");
    addline("#define __UINT_FAST64_MAX__ 18446744073709551615ULL");
    addline("#define __UINT_FAST64_TYPE__ long long unsigned int");
    addline("#define __UINT_LEAST8_MAX__ 255");
    addline("#define __UINT_LEAST8_TYPE__ unsigned char");
    addline("#define __UINT_LEAST16_MAX__ 65535");
    addline("#define __UINT_LEAST16_TYPE__ unsigned short");
    addline("#define __UINT_LEAST32_MAX__ 4294967295U");
    addline("#define __UINT_LEAST32_TYPE__ unsigned int");
    addline("#define __UINT_LEAST64_MAX__ 18446744073709551615ULL");
    addline("#define __UINT_LEAST64_TYPE__ long long unsigned int");

    // WIDE
    // 
    addline("#define __WCHAR_MAX__ 2147483647");
    addline("#define __WCHAR_TYPE__ int");
    addline("#define __WCHAR_WIDTH__ 32");
    addline("#define __WINT_MAX__ 2147483647");
    addline("#define __WINT_TYPE__ int");
    addline("#define __WINT_WIDTH__ 32");

    // FLOAT
    // 
    addline("#define __FLT16_DECIMAL_DIG__ 5");
    addline("#define __FLT16_DENORM_MIN__ 5.9604644775390625e-8F16");
    addline("#define __FLT16_DIG__ 3");
    addline("#define __FLT16_EPSILON__ 9.765625e-4F16");
    addline("#define __FLT16_HAS_DENORM__ 1");
    addline("#define __FLT16_HAS_INFINITY__ 1");
    addline("#define __FLT16_HAS_QUIET_NAN__ 1");
    addline("#define __FLT16_MANT_DIG__ 11");
    addline("#define __FLT16_MAX_10_EXP__ 4");
    addline("#define __FLT16_MAX_EXP__ 15");
    addline("#define __FLT16_MAX__ 6.5504e+4F16");
    addline("#define __FLT16_MIN_10_EXP__ (-13)");
    addline("#define __FLT16_MIN_EXP__ (-14)");
    addline("#define __FLT16_MIN__ 6.103515625e-5F16");
    addline("#define __FLT_DECIMAL_DIG__ 9");
    addline("#define __FLT_DENORM_MIN__ 1.40129846e-45F");
    addline("#define __FLT_DIG__ 6");
    addline("#define __FLT_EPSILON__ 1.19209290e-7F");
    addline("#define __FLT_EVAL_METHOD__ 0");
    addline("#define __FLT_HAS_DENORM__ 1");
    addline("#define __FLT_HAS_INFINITY__ 1");
    addline("#define __FLT_HAS_QUIET_NAN__ 1");
    addline("#define __FLT_MANT_DIG__ 24");
    addline("#define __FLT_MAX_10_EXP__ 38");
    addline("#define __FLT_MAX_EXP__ 128");
    addline("#define __FLT_MAX__ 3.40282347e+38F");
    addline("#define __FLT_MIN_10_EXP__ (-37)");
    addline("#define __FLT_MIN_EXP__ (-125)");
    addline("#define __FLT_MIN__ 1.17549435e-38F");
    addline("#define __FLT_RADIX__ 2");

    // DOUBLE
    // 
    addline("#define __DBL_DECIMAL_DIG__ 17");
    addline("#define __DBL_DENORM_MIN__ 4.9406564584124654e-324");
    addline("#define __DBL_DIG__ 15");
    addline("#define __DBL_EPSILON__ 2.2204460492503131e-16");
    addline("#define __DBL_HAS_DENORM__ 1");
    addline("#define __DBL_HAS_INFINITY__ 1");
    addline("#define __DBL_HAS_QUIET_NAN__ 1");
    addline("#define __DBL_MANT_DIG__ 53");
    addline("#define __DBL_MAX_10_EXP__ 308");
    addline("#define __DBL_MAX_EXP__ 1024");
    addline("#define __DBL_MAX__ 1.7976931348623157e+308");
    addline("#define __DBL_MIN_10_EXP__ (-307)");
    addline("#define __DBL_MIN_EXP__ (-1021)");
    addline("#define __DBL_MIN__ 2.2250738585072014e-308");

    // LONG_DOUBLE
    // 
    addline("#define __LDBL_DECIMAL_DIG__ 21");
    addline("#define __LDBL_DENORM_MIN__ 3.64519953188247460253e-4951L");
    addline("#define __LDBL_DIG__ 18");
    addline("#define __LDBL_EPSILON__ 1.08420217248550443401e-19L");
    addline("#define __LDBL_HAS_DENORM__ 1");
    addline("#define __LDBL_HAS_INFINITY__ 1");
    addline("#define __LDBL_HAS_QUIET_NAN__ 1");
    addline("#define __LDBL_MANT_DIG__ 64");
    addline("#define __LDBL_MAX_10_EXP__ 4932");
    addline("#define __LDBL_MAX_EXP__ 16384");
    addline("#define __LDBL_MAX__ 1.18973149535723176502e+4932L");
    addline("#define __LDBL_MIN_10_EXP__ (-4931)");
    addline("#define __LDBL_MIN_EXP__ (-16381)");
    addline("#define __LDBL_MIN__ 3.36210314311209350626e-4932L");

    // GCC
    // 
    if (PredefinedBuffer.CC_SETTINGS_USE_GCC) {
      addline("#define __GNUC_MINOR__ 2");
      addline("#define __GNUC_PATCHLEVEL__ 1");
      addline("#define __GNUC_STDC_INLINE__ 1");
      addline("#define __GNUC__ 4");
    }

    // CLANG
    // 
    if (PredefinedBuffer.CC_SETTINGS_USE_CLANG) {
      addline("#define __clang__ 1");
      addline("#define __llvm__ 1");
      addline("#define __clang_major__ 11");
      addline("#define __clang_minor__ 0");
      addline("#define __clang_patchlevel__ 0");
    }

    // APPLE
    // 
    if (PredefinedBuffer.CC_SETTINGS_USE_APPLE) {
      addline("#define __APPLE_CC__ 6000");
      addline("#define __APPLE__ 1");
    }

    // WIN32
    // 
    if (PredefinedBuffer.CC_SETTINGS_USE_WIN32) {
      addline("#define _WIN32 1");
      addline("#define __WIN32 1");
      addline("#define __WIN32__ 1");
      addline("#define WIN32 1");
      addline("#define WINNT 1");
      addline("#define __WINNT 1");
      addline("#define __WINNT__ 1");
      addline("#define __WINT_MAX__ 65535");
      addline("#define __WINT_MIN__ 0");
      addline("#define __MSVCRT__ 1");
      addline("#define _MSC_VER 1400");
      addline("#define __cdecl __attribute__((__cdecl__))");
      addline("#define __code_model_32__ 1");
      addline("#define __declspec(x) __attribute__((x))");
      addline("#define __fastcall __attribute__((__fastcall__))");
      addline("#define __stdcall __attribute__((__stdcall__))");
      addline("#define __thiscall __attribute__((__thiscall__))");
      addline("#define _cdecl __attribute__((__cdecl__))");
      addline("#define _fastcall __attribute__((__fastcall__))");
      addline("#define _stdcall __attribute__((__stdcall__))");
      addline("#define _thiscall __attribute__((__thiscall__))");
    }

    // I386
    // 
    if (PredefinedBuffer.CC_SETTINGS_USE_I386) {
      addline("#define i386 1");
      addline("#define __i386 1");
      addline("#define __i386__ 1");
    }

    // TYPEDEFS
    // 
    addline("void __builtin_va_start(void);");
    addline("void __builtin_va_arg(void);");
    addline("typedef struct {");
    addline("        unsigned int gp_offset;");
    addline("        unsigned int fp_offset;");
    addline("        void *overflow_arg_area;");
    addline("        void *reg_save_area;");
    addline("} __builtin_va_list[1];");

    // STUBS
    // 
    addline("#define noreturn _Noreturn");
    addline("#define __extension__");

    addline("#endif"); // CC_SETTINGS_DEFINED
  }
}
