package ast;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;

import ast.expr.CExpression;
import ast.expr.parser.ParseExpression;
import ast.expr.sem.ConstexprEval;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist; 

public class Test_Iconv {

  @Test
  public void testConv1() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  void f1(int x[4096]) {                                                                                                \n");
    sb.append(" /*002*/      _Static_assert(sizeof(x) == sizeof(unsigned long long)                                                            \n");
    sb.append(" /*003*/          , \"conv.arr to ptr in fparam.\"                                                                              \n");
    sb.append(" /*004*/      );                                                                                                                \n");
    sb.append(" /*005*/  }                                                                                                                     \n");
    sb.append(" /*006*/  void f2(int a, int b) {  }                                                                                            \n");
    sb.append(" /*007*/  void f3(void (*fp)(int, int)) { }                                                                                     \n");
    sb.append(" /*008*/  int main ()                                                                                                           \n");
    sb.append(" /*009*/  {                                                                                                                     \n");
    sb.append(" /*010*/      void (*fp)(int,int);                                                                                              \n");
    sb.append(" /*011*/      char i8 = 0;                                                                                                      \n");
    sb.append(" /*012*/      short i16 = 0;                                                                                                    \n");
    sb.append(" /*013*/      int i32 = 0;                                                                                                      \n");
    sb.append(" /*014*/      long long i64 = 0;                                                                                                \n");
    sb.append(" /*015*/      float f32 = 0;                                                                                                    \n");
    sb.append(" /*016*/      double f64 = 0;                                                                                                   \n");
    sb.append(" /*017*/      long double f128 = 0;                                                                                             \n");
    sb.append(" /*018*/      _Static_assert(    4 == sizeof( ((_Bool)0 + (_Bool)0) ), \"((_Bool)0 + (_Bool)0)\" );                             \n");
    sb.append(" /*019*/      _Static_assert(    4 == sizeof( ((char)0 + (char)0) ), \"((char)0 + (char)0)\" );                                 \n");
    sb.append(" /*020*/      _Static_assert(    4 == sizeof( ((short)0 + (char)0) ), \"((short)0 + (char)0)\" );                               \n");
    sb.append(" /*021*/      _Static_assert(    4 == sizeof( ((int)0 + (char)0) ), \"((int)0 + (char)0)\" );                                   \n");
    sb.append(" /*022*/      _Static_assert(    4 == sizeof( ((float)0 + (char)0) ), \"((float)0 + (char)0)\" );                               \n");
    sb.append(" /*023*/      _Static_assert(    8 == sizeof( ((double)0 + (char)0) ), \"((double)0 + (char)0)\" );                             \n");
    sb.append(" /*024*/      _Static_assert(    sizeof(long long) == sizeof( ((long long)0 - (char)0) ), \"((long long)0 - (char)0)\" );       \n");
    sb.append(" /*025*/      _Static_assert(   16 == sizeof( ((long double)0 + (char)0) ), \"((long double)0 + (char)0)\" );                   \n");
    sb.append(" /*026*/      _Static_assert(    4 == sizeof( ((_Bool)0 || (_Bool)0) ), \"((_Bool)0 || (_Bool)0)\" );                           \n");
    sb.append(" /*027*/      _Static_assert(    4 == sizeof( ((int)0 && (char)0) ), \"((int)0 && (char)0)\" );                                 \n");
    sb.append(" /*028*/      _Static_assert(    4 == sizeof( ((long double)0 && (long double)0) ), \"((long double)0 && (long double)0)\" );   \n");
    sb.append(" /*029*/      _Static_assert(    4 == sizeof( ((_Bool)0 | (_Bool)0) ), \"((_Bool)0 | (_Bool)0)\" );                             \n");
    sb.append(" /*030*/      _Static_assert(    4 == sizeof( ((int)0 & (char)0) ), \"((int)0 & (char)0)\" );                                   \n");
    sb.append(" /*031*/      _Static_assert(    8 == sizeof( ((long long)0 & (char)0) ), \"((long long)0 & (char)0)\" );                       \n");
    sb.append(" /*032*/      _Static_assert(    4 == sizeof( (!(_Bool)0) ), \"(!(_Bool)0)\" );                                                 \n");
    sb.append(" /*033*/      _Static_assert(    4 == sizeof( (-(int)0) ), \"(-(int)0)\" );                                                     \n");
    sb.append(" /*034*/      _Static_assert(    4 == sizeof( (+(char)0) ), \"(+(char)0)\" );                                                   \n");
    sb.append(" /*035*/      _Static_assert(    8 == sizeof( (+(long long)0) ), \"(+(long long)0)\" );                                         \n");
    sb.append(" /*036*/      _Static_assert(    1 == sizeof( (i8 += i16) ), \"(i8 += i16)\" );                                                 \n");
    sb.append(" /*037*/      _Static_assert(    1 == sizeof( (i8 = f64+f128) ), \"(i8 = f64+f128)\" );                                         \n");
    sb.append(" /*038*/      _Static_assert(    2 == sizeof( (i16 = i8+i16+i32+i64) ), \"(i16 = i8+i16+i32+i64)\" );                           \n");
    sb.append(" /*039*/      _Static_assert(    2 == sizeof( (i16 = f64) ), \"(i16 = f64)\" );                                                 \n");
    sb.append(" /*040*/      _Static_assert(    8 == sizeof( (f64 = i8) ), \"(f64 = i8)\" );                                                   \n");
    sb.append(" /*041*/      _Static_assert(    8 == sizeof( (f64 = f128) ), \"(f64 = f128)\" );                                               \n");
    sb.append(" /*042*/      _Static_assert(   16 == sizeof( (f128 = i8 || i32) ), \"(f128 = i8 || i32)\" );                                   \n");
    sb.append(" /*043*/      _Static_assert(    1 == sizeof( (i8++) ), \"(i8++)\" );                                                           \n");
    sb.append(" /*044*/      _Static_assert(    8 == sizeof( (++i64) ), \"(++i64)\" );                                                         \n");
    sb.append(" /*045*/      _Static_assert(    1 == sizeof( (f2(1, 2)) ), \"(f2(1, 2))\" );                                                   \n");
    sb.append(" /*046*/      _Static_assert(    8 == sizeof( (fp = f2) ), \"(fp = f2)\" );                                                     \n");
    sb.append(" /*047*/      _Static_assert(    8 == sizeof( (fp = &f2) ), \"(fp = &f2)\" );                                                   \n");
    sb.append(" /*050*/      _Static_assert(    1 == sizeof( (f3(fp)) ), \"(f3(fp))\" );                                                       \n");
    sb.append(" /*051*/      return 0;                                                                                                         \n");
    sb.append(" /*052*/  }                                                                                                                     \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();

    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Test
  public void testConv2() throws IOException {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  int main() {                                                                         \n");
    sb.append(" /*002*/      int a[7];                                                                        \n");
    sb.append(" /*003*/      int b[1][3];                                                                     \n");
    sb.append(" /*004*/      void c(void);                                                                    \n");
    sb.append(" /*005*/      void (*d)(void);                                                                 \n");
    sb.append(" /*006*/      //                                                                               \n");
    sb.append(" /*007*/      _Static_assert( _Generic(a  , int*            : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*008*/      _Static_assert( _Generic(&a , int(*)[7]       : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*009*/      _Static_assert( _Generic(*a , int             : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*010*/      //                                                                               \n");
    sb.append(" /*011*/      _Static_assert( _Generic(a+1     , int*       : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*012*/      _Static_assert( _Generic(*(a+1)  , int        : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*013*/      _Static_assert( _Generic(&*(a+1) , int*       : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*014*/      //                                                                               \n");
    sb.append(" /*015*/      _Static_assert( _Generic(b  , int(*)[3]       : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*016*/      _Static_assert( _Generic(&b , int(*)[1][3]    : 1, default: 2) == 1, \"NO\" );   \n");
    //sb.append(" /*017*/      _Static_assert( _Generic(*b , int*            : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*018*/      //                                                                               \n");
    sb.append(" /*019*/      _Static_assert( _Generic(c  , void (*)(void)  : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*020*/      _Static_assert( _Generic(&c , void (*)(void)  : 1, default: 2) == 1, \"NO\" );   \n");
    //sb.append(" /*021*/      _Static_assert( _Generic(*c , void (*)(void)  : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*022*/      //                                                                               \n");
    sb.append(" /*023*/      _Static_assert( _Generic(d  , void (*)(void)  : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*024*/      _Static_assert( _Generic(&d , void (**)(void) : 1, default: 2) == 1, \"NO\" );   \n");
    //sb.append(" /*025*/      _Static_assert( _Generic(*d , void (*)(void)  : 1, default: 2) == 1, \"NO\" );   \n");
    sb.append(" /*026*/      //                                                                               \n");
    sb.append(" /*027*/      return 0;                                                                        \n");
    sb.append(" /*028*/  }                                                                                    \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();

    Parse p = new Parse(it);
    TranslationUnit unit = p.parse_unit();

  }

  @Ignore //TODO:NEW_SOURCE
  @Test
  public void testConv3() throws IOException {
    Map<String, Integer> s = new HashMap<String, Integer>();
    //@formatter:off
    s.put( " sizeof(_Bool)                   \n",  1);
    s.put( " sizeof((_Bool)1)                \n",  1);
    s.put( " sizeof((_Bool).2f)              \n",  1);
    s.put( " sizeof(_Bool*)                  \n",  8);
    s.put( " sizeof(char)                    \n",  1);
    s.put( " sizeof(int)                     \n",  4);
    s.put( " sizeof(void)                    \n",  1);
    s.put( " sizeof(1)                       \n",  4);
    s.put( " sizeof(1ULL)                    \n",  8);
    s.put( " sizeof(1 + 2)                   \n",  4);
    s.put( " sizeof(1 + 2ULL)                \n",  8);
    s.put( " sizeof(long double)             \n", 16);
    s.put( " sizeof(long double*)            \n",  8);
    s.put( " sizeof((char)1ULL)              \n",  1);
    s.put( " sizeof((int)1ULL)               \n",  4);
    s.put( " sizeof((long long)1ULL)         \n",  8);
    s.put( " sizeof(1.0f + 2.14)             \n",  8);
    s.put( " sizeof(1.0f + 2.14f)            \n",  4);
    s.put( " sizeof((void*)0)                \n",  8);
    s.put( " sizeof((int*)0)                 \n",  8);
    s.put( " sizeof((int)+1)                 \n",  4);
    s.put( " sizeof(int(*)())                \n",  8);
    s.put( " sizeof 1 + 3                    \n",  7);
    s.put( " sizeof 1 + 3ULL                 \n",  7);
    s.put( " sizeof 1ULL                     \n",  8);
    s.put( " sizeof +1                       \n",  4);
    s.put( " sizeof 1024                     \n",  4);
    s.put( " sizeof 1024 + 1ULL              \n",  5);
    s.put( " sizeof((1+2, 2+3))              \n",  4);
    s.put(" sizeof(((char)'1'))              \n",  1);
    s.put(" sizeof(('1', 2))                 \n",  4);
    s.put(" sizeof(('1', 2, 3ULL))           \n",  8);
    s.put(" sizeof(('1', 2, 3ULL, 4.f))      \n",  4);
    s.put(" sizeof(('1', 2, 3ULL, 4.f, 5.))  \n",  8);
    s.put("sizeof( ((_Bool)0 + (_Bool)0) )   \n",  4);
    //@formatter:on

    int x = 0;
    for (Entry<String, Integer> entry : s.entrySet()) {

      String source = entry.getKey();
      Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();

      Parse p = new Parse(it);
      CExpression expr = new ParseExpression(p).e_expression();

      long ce = new ConstexprEval(p).ce(expr);

      if (ce != entry.getValue()) {
        System.out.println(expr.toString());
      }

      assertEquals(entry.getValue().intValue(), ce);

      //System.out.println("    unsigned long x_" + String.format("%02d", x++) + " = " +  source.trim() + ";");
    }
  }

}
