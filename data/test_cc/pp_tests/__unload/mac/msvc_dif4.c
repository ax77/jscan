// http://clang-developers.42468.n3.nabble.com/Emulation-of-MSVC-s-macro-argument-substitution-td4039755.html

#define T0() () 
#define T1() () 
#define T2() () 
#define T3() () 
#define T4() () 

#define A(p)  p 
A(T4 T3 T2 T1 T0() )             
    // ISO: T4 T3 T2 () 
    // MSVC: T4 T3 T2 () 

A(A(T4 T3 T2 T1 T0() ))         
    // ISO: T4 T3 () 
    // MSVC: T4 T3 T2 () 

A(A(A(T4 T3 T2 T1 T0() ))) 
    // ISO: T4 () 
    // MSVC: T4 T3 T2 () 
    