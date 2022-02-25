package ast;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;

import ast.parse.Parse;
import ast.unit.TranslationUnit;
import jscan.tokenize.Stream;
import jscan.tokenize.Token;

public class TestSkipper {

  private static Stream getHashedStream(String source) throws IOException {
    return new Stream(TestSkipper.class.getSimpleName(), source);
  }

  @Test
  public void testSkipAttrAndAsmInDeclarator() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("   typedef unsigned long size_t; \n");
    sb.append("   typedef char* va_list; \n");
    sb.append("   extern int __vsnprintf_chk (char * restrict, size_t, int, size_t,                                                  \n");
    sb.append("          const char * restrict, va_list);                                                                            \n");
    sb.append("   void *memchr(const void *__s, int __c, size_t __n);                                                                \n");
    sb.append("   int memcmp(const void *__s1, const void *__s2, size_t __n);                                                        \n");
    sb.append("   void *memcpy(void *__dst, const void *__src, size_t __n);                                                          \n");
    sb.append("   void *memmove(void *__dst, const void *__src, size_t __len);                                                       \n");
    sb.append("   void *memset(void *__b, int __c, size_t __len);                                                                    \n");
    sb.append("   char *strcat(char *__s1, const char *__s2);                                                                        \n");
    sb.append("   char *strchr(const char *__s, int __c);                                                                            \n");
    sb.append("   int strcmp(const char *__s1, const char *__s2);                                                                    \n");
    sb.append("   int strcoll(const char *__s1, const char *__s2);                                                                   \n");
    sb.append("   char *strcpy(char *__dst, const char *__src);                                                                      \n");
    sb.append("   size_t strcspn(const char *__s, const char *__charset);                                                            \n");
    sb.append("   char *strerror(int __errnum) __asm(\"_\" \"strerror\" );                                                               \n");
    sb.append("   size_t strlen(const char *__s);                                                                                    \n");
    sb.append("   char *strncat(char *__s1, const char *__s2, size_t __n);                                                           \n");
    sb.append("   int strncmp(const char *__s1, const char *__s2, size_t __n);                                                       \n");
    sb.append("   char *strncpy(char *__dst, const char *__src, size_t __n);                                                         \n");
    sb.append("   char *strpbrk(const char *__s, const char *__charset);                                                             \n");
    sb.append("   char *strrchr(const char *__s, int __c);                                                                           \n");
    sb.append("   size_t strspn(const char *__s, const char *__charset);                                                             \n");
    sb.append("   char *strstr(const char *__big, const char *__little);                                                             \n");
    sb.append("   char *strtok(char *__str, const char *__sep);                                                                      \n");
    sb.append("   size_t strxfrm(char *__s1, const char *__s2, size_t __n);                                                          \n");
    sb.append("   char *strtok_r(char *__str, const char *__sep, char **__lasts);                                                    \n");
    sb.append("   int strerror_r(int __errnum, char *__strerrbuf, size_t __buflen);                                                  \n");
    sb.append("   char *strdup(const char *__s1);                                                                                    \n");
    sb.append("   void *memccpy(void *__dst, const void *__src, int __c, size_t __n);                                                \n");
    sb.append("   char *stpcpy(char *__dst, const char *__src);                                                                      \n");
    sb.append("   char *stpncpy(char *__dst, const char *__src, size_t __n) __attribute__((availability(macosx,introduced=10.7)));   \n");
    sb.append("   char *strndup(const char *__s1, size_t __n) __attribute__((availability(macosx,introduced=10.7)));                 \n");
    sb.append("   size_t strnlen(const char *__s1, size_t __n) __attribute__((availability(macosx,introduced=10.7)));                \n");
    sb.append("   char *strsignal(int __sig);                                                                                        \n");


    List<Token> tokenlist = getHashedStream(sb.toString()).getTokenlist();
    Parse p = new Parse(tokenlist);
    TranslationUnit unit = p.parse_unit();
    
    //34
    assertEquals(34, unit.getExternalDeclarations().size());

  }
  
  @Test
  public void testStraySemicolon_1() throws IOException {
    Map<String, String> tests = new TreeMap<String, String>();

    tests.put(";;;;; int main(int argc, char **argv) { return 0;;;;; }", "");

    for (Entry<String, String> e : tests.entrySet()) {

      List<Token> tokenlist = getHashedStream(e.getKey()).getTokenlist();
      Parse p = new Parse(tokenlist);
      TranslationUnit unit = p.parse_unit();

      assertEquals(1, unit.getExternalDeclarations().size());

    }
  }

  @Test
  public void testStraySemicolon_2() throws IOException {
    Map<String, String> tests = new TreeMap<String, String>();

    tests.put(";;;;;", "");

    for (Entry<String, String> e : tests.entrySet()) {

      List<Token> tokenlist = getHashedStream(e.getKey()).getTokenlist();
      Parse p = new Parse(tokenlist);
      TranslationUnit unit = p.parse_unit();

      assertEquals(0, unit.getExternalDeclarations().size());

    }
  }


}
