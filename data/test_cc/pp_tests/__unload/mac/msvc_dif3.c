// http://clang-developers.42468.n3.nabble.com/Emulation-of-MSVC-s-macro-argument-substitution-td4039755.html


#define Empty 
#define T()  T1 Empty () 
#define T1() T2 Empty () 
#define T2() T3 Empty () 
#define T3() T4 Empty () 
#define T4() T5 Empty () 
#define T5() T6 Empty () 
#define T6() T7 Empty () 
#define T7() T8 Empty () 
#define T8() T9 Empty () 
#define T9() >>INFINITE RECURSION ERROR<< 


#define A(p) p 

A(T()) // ISO C/C++:  T2 () 
       // MSVC: >>INFINITE RECURSION ERROR<< 
       