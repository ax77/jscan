// http://clang-developers.42468.n3.nabble.com/Emulation-of-MSVC-s-macro-argument-substitution-td4039755.html

#define Comma ,
#define L           (
#define E1(a)    E2 L a )
#define E2(a, b) ( a + b )

E1( 1 Comma 2 )
