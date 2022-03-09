package ast_test;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import ast.main.ParserMain;
import ast.parse.Parse;
import ast.symtab.CSymbol;
import ast.tree.Declaration;
import ast.tree.ExternalDeclaration;
import ast.tree.TranslationUnit;
import jscan.parse.Tokenlist;

public class Test_Typedefs2 {

  @Test
  public void testTypedefs8() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*255*/  extern __attribute__((dllimport)) const char _ctype_[]; \n");
    sb.append(" /*190*/  void __assert_func (const char *, int, const char *, const char *)                                                                                                 \n");
    sb.append(" /*191*/       __attribute__ ((__noreturn__));  \n"); 
    
    sb.append(" /*001*/  struct typedef_after {                        \n");
    sb.append(" /*002*/      int flag;                                 \n");
    sb.append(" /*003*/  } typedef typedef_after, *p_typedef_after;    \n");
    
    sb.append(" /*177*/  void __builtin_va_start(void);                                                                                                                                     \n");
    sb.append(" /*178*/  void __builtin_va_arg(void);                                                                                                                                       \n");
    sb.append(" /*179*/  typedef struct {                                                                                                                                                   \n");
    sb.append(" /*180*/          unsigned int gp_offset;                                                                                                                                    \n");
    sb.append(" /*181*/          unsigned int fp_offset;                                                                                                                                    \n");
    sb.append(" /*182*/          void *overflow_arg_area;                                                                                                                                   \n");
    sb.append(" /*183*/          void *reg_save_area;                                                                                                                                       \n");
    sb.append(" /*184*/  } __builtin_va_list[1];                                                                                                                                            \n");
    sb.append(" /*188*/  void __assert (const char *, int, const char *)                                                                                                                    \n");
    sb.append(" /*189*/       __attribute__ ((__noreturn__));                                                                                                                               \n");
    sb.append(" /*190*/  void __assert_func (const char *, int, const char *, const char *)                                                                                                 \n");
    sb.append(" /*191*/       __attribute__ ((__noreturn__));                                                                                                                               \n");
    sb.append(" /*192*/  typedef signed char __int8_t;                                                                                                                                      \n");
    sb.append(" /*193*/  typedef unsigned char __uint8_t;                                                                                                                                   \n");
    sb.append(" /*194*/  typedef short int __int16_t;                                                                                                                                       \n");
    sb.append(" /*195*/  typedef short unsigned int __uint16_t;                                                                                                                             \n");
    sb.append(" /*196*/  typedef int __int32_t;                                                                                                                                             \n");
    sb.append(" /*197*/  typedef unsigned int __uint32_t;                                                                                                                                   \n");
    sb.append(" /*198*/  typedef long int __int64_t;                                                                                                                                        \n");
    sb.append(" /*199*/  typedef long unsigned int __uint64_t;                                                                                                                              \n");
    sb.append(" /*200*/  typedef signed char __int_least8_t;                                                                                                                                \n");
    sb.append(" /*201*/  typedef unsigned char __uint_least8_t;                                                                                                                             \n");
    sb.append(" /*202*/  typedef short int __int_least16_t;                                                                                                                                 \n");
    sb.append(" /*203*/  typedef short unsigned int __uint_least16_t;                                                                                                                       \n");
    sb.append(" /*204*/  typedef int __int_least32_t;                                                                                                                                       \n");
    sb.append(" /*205*/  typedef unsigned int __uint_least32_t;                                                                                                                             \n");
    sb.append(" /*206*/  typedef long int __int_least64_t;                                                                                                                                  \n");
    sb.append(" /*207*/  typedef long unsigned int __uint_least64_t;                                                                                                                        \n");
    sb.append(" /*208*/  typedef long int __intmax_t;                                                                                                                                       \n");
    sb.append(" /*209*/  typedef long unsigned int __uintmax_t;                                                                                                                             \n");
    sb.append(" /*210*/  typedef long int __intptr_t;                                                                                                                                       \n");
    sb.append(" /*211*/  typedef long unsigned int __uintptr_t;                                                                                                                             \n");
    sb.append(" /*212*/  typedef long int ptrdiff_t;                                                                                                                                        \n");
    sb.append(" /*213*/  typedef long unsigned int size_t;                                                                                                                                  \n");
    sb.append(" /*214*/  typedef short unsigned int wchar_t;                                                                                                                                \n");
    sb.append(" /*215*/  typedef struct {                                                                                                                                                   \n");
    sb.append(" /*216*/    long long __max_align_ll __attribute__((__aligned__(__alignof__(long long))));                                                                                   \n");
    sb.append(" /*217*/    long double __max_align_ld __attribute__((__aligned__(__alignof__(long double))));                                                                               \n");
    sb.append(" /*218*/  } max_align_t;  ");
    
    sb.append(" /*004*/  static int test_typedefs_1() {                \n");
    sb.append(" /*005*/      typedef int i32;                          \n");
    sb.append(" /*006*/      typedef i32 i32;                          \n");
    sb.append(" /*007*/      int signed typedef i32;                   \n");
    sb.append(" /*008*/      i32 typedef i32;                          \n");
    sb.append(" /*009*/      typedef i32 dword;                        \n");
    sb.append(" /*010*/      i32 x = 1;                                \n");
    sb.append(" /*011*/      x -= 1;                                   \n");
    sb.append(" /*012*/      dword y = 1;                              \n");
    sb.append(" /*013*/      y -= 1;                                   \n");
    sb.append(" /*014*/      return x + y;                             \n");
    sb.append(" /*015*/  }                                             \n");
    sb.append(" /*027*/  int main() {                                  \n");
    sb.append(" /*028*/      long long result = 0;                     \n");
    sb.append(" /*031*/      return (result == 0) ? 0 : 1;             \n");
    sb.append(" /*032*/  }                                             \n");
    //@formatter:on

    Tokenlist it = new ParserMain(new StringBuilder(sb)).preprocess();
    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

    for (ExternalDeclaration ext : unit.getExternalDeclarations()) {
      if (ext.isDeclaration()) {
        Declaration decl = ext.getDeclaration();
        if (decl.isVarlist()) {
          for (CSymbol sym : decl.getVariables()) {
          }
        }
      }
    }
  }

}
