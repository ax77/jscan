#define F1(X, ...) f(10, X, __VA_ARGS__)
#define F2(X, ...) f(10, X, ## __VA_ARGS__)

#define H1(...) f(10, __VA_ARGS__)
#define H2(...) f(10, ## __VA_ARGS__)

F1(a, b)   =>   f(10, a, b)   // standard
F1(a, )    =>   f(10, a, )    // standard (empty variable arguments)
F1(a)      =>   f(10, a, )    // extension (no variable arguments), final comma unaffected

F2(a, b)   =>   f(10, a, b)   // variable arguments not empty
F2(a, )    =>   f(10, a, )    // variable arguments present (though empty), final comma unaffected
F2(a)      =>   f(10, a)      // no variable arguments, final comma deleted

H1(a)      =>   f(10, a)      // standard
H1()       =>   f(10, )       // standard (empty variable arguments)

H2(a)      =>   f(10, a)      // variable arguments not empty
H2()       =>   f(10)         // variable arguments present (though empty), final comma deleted
