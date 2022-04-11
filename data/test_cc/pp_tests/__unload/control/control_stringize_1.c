#define str(x)      #x
#define xstr(x)     str(x)
#define x(x, y)     str(x + y)
#define z(x)        str(++ x)
#define t(x)        str(-x)
#define hh          # ## #
#define mh(x,y,z)   xstr(x hh y hh z)
#define EMPTY

#define f_FFF(x) str(-x)
f_FFF(
    1)

// "a ## b ## c"
mh(  a,  b,  c  )
mh(a,b,c)
mh(a,
b,
c)
mh
(
 a,
  b,
 c
)
mh(    a,b,c    )
mh(a,    \
       b,\
       c \
)
mh(    a  EMPTY,  b  EMPTY,  c   EMPTY )



// char v[] = "ax.by.cz"
#define F(A, B, C) A ## x.B ## y.C ## z
#define STRINGIFY(x) #x
#define EXPAND_AND_STRINGIFY(x) STRINGIFY(x)
char v0[] = EXPAND_AND_STRINGIFY(F(a, b, c))
char v1[] = EXPAND_AND_STRINGIFY( F ( a, b, c ) )
char v2[] = EXPAND_AND_STRINGIFY( F ( a, 
                                      b, 
                                      c ) )
char v3[] = EXPAND_AND_STRINGIFY( 
                                  F ( a, 
                                      b, 
                                      c ) )
char v4[] = EXPAND_AND_STRINGIFY( F ( a  ,  b  ,  c ) )
char v5[] = EXPAND_AND_STRINGIFY( F ( a  ,  b  ,  c ) )




// assert( strcmp( "'\"' + \"' \\\"\"", "'\"' + \"' \\\"\"") == 0);
assert( strcmp( str( '"' + "' \""), "'\"' + \"' \\\"\"") == 0);



// "a c"
#define t1(x) #x
t1(a
c)



// fputs("strncmp(\"abc\\0d?\", \"abc\", '\\4', \"\\u1234\") == 0" ": @\n", s);
fputs(str(strncmp("abc\0d?", "abc", '\4', "\u1234") /* this goes away */
== 0) str(: @\n), s);



// fputs("strncmp(\"abc\\0d\", \"abc\", '\\4') == 0" ": @\n", s);
fputs(str(strncmp("abc\0d", "abc", '\4') // this goes away

== 0) str(: @\n), s);



// "3 + 2"
x(3,
  2)



// "a b c"
str
(
    a
        b
    c
)



// "++ a"
z(
    a
        )



// "-1"
t(
  1
)



// "abc"
str
   (
a\
b\
c\
   )



// "a b c"
str(
   a\
   b\
   c\
)



// "a b c"
str(
a  \
b  \
c  \
   )



// "a b c"
str(
a // comment
b // comment
c // comment
)



// "a b c"
str(
a /**/
b /**/
c /**/
)



// "a COMMA b" 
// "(a, b)"
#define M(x, y) #x #y
#define COMMA , 
M(a COMMA b, (a, b)) 



// X163 "one##tok"
#define STR1(u) # u
#define pass(a) a
X163 pass(STR1(one##tok))



// "a b c d e f g h i j k l m n o p q r s t u v w x y z"
str(/**/a/**//**/b/**//**/c/**//**/d/**//**/e/**//**/f/**//**/g/**//**/h/**//**/i/**//**/j/**//**/k/**//**/l/**//**/m/**//**/n/**//**/o/**//**/p/**//**/q/**//**/r/**//**/s/**//**/t/**//**/u/**//**/v/**//**/w/**//**/x/**//**/y/**//**/z/**/)
	


#define ZX_str_(x) #              x
#define ZX_str1(x) ZX_str_( -     x     - )
#define ZX_str2(x) ZX_str1( -     x     - )
#define ZX_str3(x) ZX_str2( -     x     - )
#define ZX_str4(x) ZX_str3( -     x     - )
#define ZX_str5(x) ZX_str4( -     x     - )
#define ZX_str6(x) ZX_str5( -     x     - )
#define ZX_str7(x) ZX_str6( -     x     - )
#define ZX_STR_EMPTY
ZX_str7
(
  X
)
ZX_str7(    X    )
ZX_str7(X
)
ZX_str7(
X)
ZX_str7
(X)
ZX_str7\
(\
X\
)
ZX_str7(/**/X/**/)
ZX_str7(/**/   X   /**/)
ZX_str7(/**/   X/**/)
ZX_str7(/**/X   /**/)

ZX_str7(/////////////
X////////////////////
)////////////////////

ZX_str7//////////////
(////////////////////
/*///////////*/X/////
)////////////////////

ZX_str7( ZX_STR_EMPTY X ZX_STR_EMPTY )
ZX_str7( 
        ZX_STR_EMPTY 
		X 
		ZX_STR_EMPTY 
)

ZX_str7(  ZX_str_  (  X  )  )
ZX_str7(  ZX_str1  (  X  )  )
ZX_str7(  ZX_str2  (  X  )  )
ZX_str7(  ZX_str3  (  X  )  )
ZX_str7(  ZX_str4  (  X  )  )
ZX_str7(  ZX_str5  (  X  )  )
ZX_str7(  ZX_str6  (  X  )  )
ZX_str7(  ZX_str7  (  X  )  )






























	