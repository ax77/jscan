
//#include <stdio.h>

// #include "c_lambda.h"

//
// Write nested, anonymous, and closure functions in ISO C99
//


// Public syntax-extension macros

#define func(rt, n, ...) FN_8CM_GO((8EMIT_NS, (n, (rt n), ( 8blk( __VA_ARGS__ ) ), ((8CM_END,(~))))))
#define namespace(n, ...) FN_8CM_GO((8EMIT_NS, (n, (), ( 8blk( __VA_ARGS__ ) ), ((8CM_END,(~))))))

#define fn(rt, args, ...) ), 8fn(rt, args, (8blk(__VA_ARGS__))), 8blk(
#define cl(rt, args, over, ...) ), 8cl(rt, args, over, (8blk(__VA_ARGS__ }))), 8blk(
#define defn(rt, n, a, ...) static FN_8FTYPE(rt, const, a, n) = fn(rt, a, __VA_ARGS__);

#define closure_type(RT, PT) struct { RT (* _fun) (void * M_FOR_EACH(FN_8CL_ARG_TYPE, M_ID PT)); size_t _size; } *

#define _fe1(_0, ...) FN_8FEXP(_0, (), M_FIRST(__VA_ARGS__), (__VA_ARGS__))
#define _fe2(_0, _1, ...) FN_8FEXP(_0, (_1,), M_FIRST(__VA_ARGS__), (__VA_ARGS__))
#define _fe3(_0, _1, _2, ...) FN_8FEXP(_0, (_1, _2,), M_FIRST(__VA_ARGS__), (__VA_ARGS__))
#define _fe4(_0, _1, _2, _3, ...) FN_8FEXP(_0, (_1, _2, _3,), M_FIRST(__VA_ARGS__), (__VA_ARGS__))

#define _cle1(_0, ...) FN_8CLEXP(_0, (), M_FIRST(__VA_ARGS__), (__VA_ARGS__))
#define _cle2(_0, _1, ...) FN_8CLEXP(_0, (_1,), M_FIRST(__VA_ARGS__), (__VA_ARGS__))
#define _cle3(_0, _1, _2, ...) FN_8CLEXP(_0, (_1, _2,), M_FIRST(__VA_ARGS__), (__VA_ARGS__))
#define _cle4(_0, _1, _2, _3, ...) FN_8CLEXP(_0, (_1, _2, _3,), M_FIRST(__VA_ARGS__), (__VA_ARGS__))


// Continuation machine

#define FN_8CM_GO(x) FN_8CM_RET(FN_8CM_UP_0 x)

#define FN_8CM_RET(x) FN_8CM_RET_(x)
#define FN_8CM_RET_(_, ...) __VA_ARGS__

#define FN_8CM_END(...) ,

#define FN_8CM_D_0(x) FN_8CM_DN_0 x
#define FN_8CM_U_0(x) FN_8CM_UP_1 x
#define FN_8CM_D_1(x) FN_8CM_DN_1 x
#define FN_8CM_U_1(x) FN_8CM_UP_2 x
#define FN_8CM_D_2(x) FN_8CM_DN_2 x
#define FN_8CM_U_2(x) FN_8CM_UP_3 x
#define FN_8CM_D_3(x) FN_8CM_DN_3 x
#define FN_8CM_U_3(x) FN_8CM_UP_4 x
#define FN_8CM_D_4(x) FN_8CM_DN_4 x
#define FN_8CM_U_4(x) FN_8CM_UP_5 x
#define FN_8CM_D_5(x) FN_8CM_DN_5 x
#define FN_8CM_U_5(x) FN_8CM_UP_6 x
#define FN_8CM_D_6(x) FN_8CM_DN_6 x
#define FN_8CM_U_6(x) FN_8CM_UP_7 x
#define FN_8CM_D_7(x) FN_8CM_DN_7 x
#define FN_8CM_U_7(x) FN_8CM_UP_8 x
#define FN_8CM_D_8(x) FN_8CM_DN_8 x
#define FN_8CM_U_8(x) FN_8CM_UP_9 x
#define FN_8CM_D_9(x) FN_8CM_DN_9 x
#define FN_8CM_U_9(x) FN_8CM_UP_10 x
#define FN_8CM_D_10(x) FN_8CM_DN_10 x
#define FN_8CM_U_10(x) FN_8CM_UP_11 x
#define FN_8CM_D_11(x) FN_8CM_DN_11 x
#define FN_8CM_U_11(x) FN_8CM_UP_12 x
#define FN_8CM_D_12(x) FN_8CM_DN_12 x
#define FN_8CM_U_12(x) FN_8CM_UP_13 x
#define FN_8CM_D_13(x) FN_8CM_DN_13 x
#define FN_8CM_U_13(x) FN_8CM_UP_14 x
#define FN_8CM_D_14(x) FN_8CM_DN_14 x
#define FN_8CM_U_14(x) FN_8CM_UP_15 x
#define FN_8CM_D_15(x) FN_8CM_DN_15 x
#define FN_8CM_U_15(x) FN_8CM_UP_16 x
#define FN_8CM_D_16(x) FN_8CM_DN_16 x
#define FN_8CM_U_16(x) FN_8CM_ERROR_OUT_OF_STEPS(0)

#define FN_8CM_DN_0(f, x, ...) FN_##f x __VA_ARGS__
#define FN_8CM_UP_0(f, x, ...) FN_8CM_U_0(FN_8CM_D_0(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_1(f, x, ...) FN_8CM_D_0(FN_8CM_D_0(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_1(f, x, ...) FN_8CM_U_1(FN_8CM_D_1(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_2(f, x, ...) FN_8CM_D_1(FN_8CM_D_1(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_2(f, x, ...) FN_8CM_U_2(FN_8CM_D_2(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_3(f, x, ...) FN_8CM_D_2(FN_8CM_D_2(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_3(f, x, ...) FN_8CM_U_3(FN_8CM_D_3(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_4(f, x, ...) FN_8CM_D_3(FN_8CM_D_3(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_4(f, x, ...) FN_8CM_U_4(FN_8CM_D_4(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_5(f, x, ...) FN_8CM_D_4(FN_8CM_D_4(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_5(f, x, ...) FN_8CM_U_5(FN_8CM_D_5(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_6(f, x, ...) FN_8CM_D_5(FN_8CM_D_5(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_6(f, x, ...) FN_8CM_U_6(FN_8CM_D_6(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_7(f, x, ...) FN_8CM_D_2(FN_8CM_D_6(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_7(f, x, ...) FN_8CM_U_3(FN_8CM_D_7(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_8(f, x, ...) FN_8CM_D_3(FN_8CM_D_7(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_8(f, x, ...) FN_8CM_U_4(FN_8CM_D_8(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_9(f, x, ...) FN_8CM_D_4(FN_8CM_D_8(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_9(f, x, ...) FN_8CM_U_5(FN_8CM_D_9(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_10(f, x, ...) FN_8CM_D_5(FN_8CM_D_9(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_10(f, x, ...) FN_8CM_U_6(FN_8CM_D_10(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_11(f, x, ...) FN_8CM_D_4(FN_8CM_D_10(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_11(f, x, ...) FN_8CM_U_5(FN_8CM_D_11(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_12(f, x, ...) FN_8CM_D_5(FN_8CM_D_11(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_12(f, x, ...) FN_8CM_U_6(FN_8CM_D_12(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_13(f, x, ...) FN_8CM_D_2(FN_8CM_D_12(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_13(f, x, ...) FN_8CM_U_3(FN_8CM_D_13(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_14(f, x, ...) FN_8CM_D_3(FN_8CM_D_13(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_14(f, x, ...) FN_8CM_U_4(FN_8CM_D_14(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_15(f, x, ...) FN_8CM_D_4(FN_8CM_D_14(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_15(f, x, ...) FN_8CM_U_5(FN_8CM_D_15(FN_##f x)) __VA_ARGS__
#define FN_8CM_DN_16(f, x, ...) FN_8CM_D_5(FN_8CM_D_15(FN_##f x)) __VA_ARGS__
#define FN_8CM_UP_16(f, x, ...) FN_8CM_U_6(FN_8CM_D_16(FN_##f x)) __VA_ARGS__

#define FN_8CM_ERROR_OUT_OF_STEPS(A, B)


// Internal lambda builder functions

#define FN_8CL_ARG_TYPE(T) , T

#define FN_8FEXP(call, p, f, R) { FN_8GETTYPE_8fn f = fn f; call(M_ID p M_IF(M_2ORMORE R, (_fun, M_REST_ R), (_fun))); }
#define FN_8CLEXP(call, p, c, R) { void * _fun = cl c; call(M_ID p M_IF(M_2ORMORE R, (_fun, M_REST_ R), (_fun))); }

#define FN_8EMIT_NS(N, H, B, Q) FN_8EMIT_NS_((FN_8GET_NL(N, B)), H, (M_ZIP_W2(FN_8EMIT_ELEM, B, M_ILIST)), Q)
#define FN_8EMIT_NS_(NL, H, BL, Q) (8ZIPNE,(NL, BL, Q), FN_8EMIT_BODY(NL, H, BL))

#define FN_8GET_NL(N, B) M_ZIP_W2(FN_8GET_NL_1, M_ENLIST(_8anon_##N##_, M_NARGS B), M_ILIST)
#define FN_8GET_NL_1(A, B) , M_CONC_(A, B)
#define FN_8EMIT_BODY(NL, H, BL) M_ID H M_ZIP_WITH(FN_8EMIT_BLOCK, BL, NL)

#define FN_8EMIT_ELEM(E, _) ,M_CONC_(FN_8EMIT_, E)
#define FN_8EMIT_8blk(...) (1, 0, __VA_ARGS__)
#define FN_8EMIT_8fn(...) (0, 0, __VA_ARGS__)
#define FN_8EMIT_8cl(...) (0, 1, __VA_ARGS__)
#define FN_8EMIT_BLOCK(P, N) M_IF(M_FIRST P, (M_REST2 P), (FN_8EMIT_NAME((M_REST P, r, a, o), N)))
#define FN_8EMIT_NAME(P, N) M_IF(M_FIRST P, (FN_8EMIT_CL(N, M_ID P)), (N))
#define FN_8EMIT_CL(...) FN_8EMIT_CL_(__VA_ARGS__)
#define FN_8EMIT_CL_(n, _, r, a, o, ...) (void*)&(struct n##_env_t){ \
  n,sizeof(struct n##_env_t) M_FOR_EACH(FN_8CL_SND, M_ID o) }
#define FN_8CL_SND(P) , M_REST_ P

#define FN_8EMIT_ENV(E, Q) (8DO_Q, (Q), FN_8EMIT_ENV_(E))
#define FN_8EMIT_ENV_(n, rt, a, o) struct n##_env_t { \
  FN_8CTYPE(rt, n, a, _fun); size_t _size; M_FOR_EACH(FN_8FLDS, M_ID o) };
#define FN_8FLDS(F) M_FIRST_ F M_REST_ F;
#define FN_8CL_DEC(rt, n, a) static rt n(void * _envV, M_ID a) { struct n##_env_t * _env = _envV; 

#define FN_8FTYPE(rt, C, a, pn) rt(* C pn)a
#define FN_8CTYPE(rt, n, a, pn) rt(* pn)(void *, M_ID a)
#define FN_8FN_TYPE(F) M_CONC(FN_8GETTYPE_, M_FIRST(M_REST((F))))
#define FN_8GETTYPE_8fn(rt, a, ...) rt(* _fun)a
#define FN_8CLSZ(T, N) T M_CONC(_, N);

#define FN_8ZIPNE(NL, BL, Q) (8FLTNE, ((M_ZIP_W2(FN_8ZIPNE_, NL, BL)), Q))
#define FN_8ZIPNE_(N, B) ,(M_FIRST_ B, N, M_REST_ B)

#define FN_8FLTNE(EL, Q) (8POPEM, ((0 M_FOR_EACH(FN_8FLTNE_, M_ID EL)), Q))
#define FN_8FLTNE_(E) M_IF(M_FIRST_ E, (), (,(M_REST_ E)))

#define FN_8POPEM(FL, Q) M_IF(M_2ORMORE(M_ID FL), ((8F2NS, ((M_REST_ FL), Q))), ((8DO_Q, (Q))))

#define FN_8F2NS(FL, Q) (8DO_Q, ((M_FOR_E2(FN_8F2NS_1, M_ID FL), M_ID Q)))
#define FN_8F2NS_1(F) ,FN_8F2NS_2 F
#define FN_8F2NS_2(n, isC, rt, a, ...) M_IF(isC, \
  ((8EMIT_NS_NX, (n, (FN_8CL_DEC(rt, n, a)), M_REST(__VA_ARGS__))), (8EMIT_ENV,(n, rt, a, M_FIRST(__VA_ARGS__)))), \
  ((8EMIT_NS_NX, (n, (static rt n a), __VA_ARGS__))))

#define FN_8DO_Q(Q) (M_ID(M_FIRST_ M_FIRST_ Q),(M_ID M_REST_ M_FIRST_ Q, (M_REST_ Q)))

#define FN_8EMIT_NS_NX(...) (8EMIT_NS, (__VA_ARGS__))


// Some generic simple metaprogramming macros used by the above

//#include "cmacros.h"

//
// Simple (i.e. very limited) metaprogramming macros
//      

#ifndef C_MACROS_H
#define C_MACROS_H 1


#define M_MAX_DEPTH 32

#define M_NARGS(...) M_NARGS_(__VA_ARGS__, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, \
20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
#define M_NARGS_(_32, _31, _30, _29, _28, _27, _26, _25, _24, _23, _22, _21, _20, _19, _18, _17, \
_16, _15, _14, _13, _12, _11, _10, _9, _8, _7, _6, _5, _4, _3, _2, _1, N, ...) N

#define M_2ORMORE(...) M_NARGS_(__VA_ARGS__, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, \
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0)

#define M_ILIST (0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, \
17, 18, 19, 20, 21, 23, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32)

#define M_ID(...) __VA_ARGS__
#define M_FIRST(...) M_FIRST_(__VA_ARGS__,)
#define M_REST(...) M_REST_(__VA_ARGS__)
#define M_FIRST_(A, ...) A
#define M_REST_(A, ...) __VA_ARGS__
#define M_FIRST2(A, B, ...) A, B
#define M_REST2(A, B, ...) __VA_ARGS__
#define M_ROT(A, ...) __VA_ARGS__, A
#define M_REVERSE(...) M_RECUR(M_REVERSE_, M_NARGS(__VA_ARGS__), (__VA_ARGS__))
#define M_REVERSE_(...) (M_REST(__VA_ARGS__)), M_FIRST(__VA_ARGS__)
#define M_CONC(A, B) M_CONC_(A, B)
#define M_CONC_(A, B) A##B
#define M_DIV2(N) M_CONC(M_DIV2_, N)
#define M_DECR(N) M_CONC(M_DECR_, N)
#define M_IF(P, T, E) M_CONC(M_IF_, P)(T, E)
#define M_IF_0(T, E) M_ID E
#define M_IF_1(T, E) M_ID T
#define M_ENLIST(X, N) M_DO_1(M_REST M_ENLIST_1(X, N))
#define M_ENLIST_1(X, N) (M_CONC(M_NEST_, N)(M_ENLIST_2, X))
#define M_ENLIST_2(...) M_FIRST(__VA_ARGS__), __VA_ARGS__

#define M_BEFORE_AFTER(BEF, AFT) for(int _b = (BEF, 1); _b; (AFT, _b = 0))

#define M_STATIC_ASSERT(EXP, NAME) {if(sizeof(struct{unsigned int NAME:((EXP)?1:-1);}));}

#define M_RECUR(ACT, N, ARG) M_CONC(M, _REST M_CONC(M_DO_, N)(M_CONC(M_REPT_, N)(ACT)ARG))

#define M_FOR_EACH(ACTN, ...) M_CONC(M_FOR_EACH_, M_NARGS(__VA_ARGS__)) (ACTN, __VA_ARGS__)
#define M_FOR_E2(ACTN, ...) M_REST(M_FOR_EACH(ACTN, __VA_ARGS__))

#define M_FOR_EACH_0(ACTN, E) E
#define M_FOR_EACH_1(ACTN, E) ACTN(E)
#define M_FOR_EACH_2(ACTN, E, ...) ACTN(E) M_FOR_EACH_1(ACTN, __VA_ARGS__)
#define M_FOR_EACH_3(ACTN, E, ...) ACTN(E) M_FOR_EACH_2(ACTN, __VA_ARGS__)
#define M_FOR_EACH_4(ACTN, E, ...) ACTN(E) M_FOR_EACH_3(ACTN, __VA_ARGS__)
#define M_FOR_EACH_5(ACTN, E, ...) ACTN(E) M_FOR_EACH_4(ACTN, __VA_ARGS__)
#define M_FOR_EACH_6(ACTN, E, ...) ACTN(E) M_FOR_EACH_5(ACTN, __VA_ARGS__)
#define M_FOR_EACH_7(ACTN, E, ...) ACTN(E) M_FOR_EACH_6(ACTN, __VA_ARGS__)
#define M_FOR_EACH_8(ACTN, E, ...) ACTN(E) M_FOR_EACH_7(ACTN, __VA_ARGS__)
#define M_FOR_EACH_9(ACTN, E, ...) ACTN(E) M_FOR_EACH_8(ACTN, __VA_ARGS__)
#define M_FOR_EACH_10(ACTN, E, ...) ACTN(E) M_FOR_EACH_9(ACTN, __VA_ARGS__)
#define M_FOR_EACH_11(ACTN, E, ...) ACTN(E) M_FOR_EACH_10(ACTN, __VA_ARGS__)
#define M_FOR_EACH_12(ACTN, E, ...) ACTN(E) M_FOR_EACH_11(ACTN, __VA_ARGS__)
#define M_FOR_EACH_13(ACTN, E, ...) ACTN(E) M_FOR_EACH_12(ACTN, __VA_ARGS__)
#define M_FOR_EACH_14(ACTN, E, ...) ACTN(E) M_FOR_EACH_13(ACTN, __VA_ARGS__)
#define M_FOR_EACH_15(ACTN, E, ...) ACTN(E) M_FOR_EACH_14(ACTN, __VA_ARGS__)
#define M_FOR_EACH_16(ACTN, E, ...) ACTN(E) M_FOR_EACH_15(ACTN, __VA_ARGS__)
#define M_FOR_EACH_17(ACTN, E, ...) ACTN(E) M_FOR_EACH_16(ACTN, __VA_ARGS__)
#define M_FOR_EACH_18(ACTN, E, ...) ACTN(E) M_FOR_EACH_17(ACTN, __VA_ARGS__)
#define M_FOR_EACH_19(ACTN, E, ...) ACTN(E) M_FOR_EACH_18(ACTN, __VA_ARGS__)
#define M_FOR_EACH_20(ACTN, E, ...) ACTN(E) M_FOR_EACH_19(ACTN, __VA_ARGS__)
#define M_FOR_EACH_21(ACTN, E, ...) ACTN(E) M_FOR_EACH_20(ACTN, __VA_ARGS__)
#define M_FOR_EACH_22(ACTN, E, ...) ACTN(E) M_FOR_EACH_21(ACTN, __VA_ARGS__)
#define M_FOR_EACH_23(ACTN, E, ...) ACTN(E) M_FOR_EACH_22(ACTN, __VA_ARGS__)
#define M_FOR_EACH_24(ACTN, E, ...) ACTN(E) M_FOR_EACH_23(ACTN, __VA_ARGS__)
#define M_FOR_EACH_25(ACTN, E, ...) ACTN(E) M_FOR_EACH_24(ACTN, __VA_ARGS__)
#define M_FOR_EACH_26(ACTN, E, ...) ACTN(E) M_FOR_EACH_25(ACTN, __VA_ARGS__)
#define M_FOR_EACH_27(ACTN, E, ...) ACTN(E) M_FOR_EACH_26(ACTN, __VA_ARGS__)
#define M_FOR_EACH_28(ACTN, E, ...) ACTN(E) M_FOR_EACH_27(ACTN, __VA_ARGS__)
#define M_FOR_EACH_29(ACTN, E, ...) ACTN(E) M_FOR_EACH_28(ACTN, __VA_ARGS__)
#define M_FOR_EACH_30(ACTN, E, ...) ACTN(E) M_FOR_EACH_29(ACTN, __VA_ARGS__)
#define M_FOR_EACH_31(ACTN, E, ...) ACTN(E) M_FOR_EACH_30(ACTN, __VA_ARGS__)
#define M_FOR_EACH_32(ACTN, E, ...) ACTN(E) M_FOR_EACH_31(ACTN, __VA_ARGS__)

#define M_ZIP_WITH(ACTN, L1, L2) M_CONC(M_ZIP_WITH_, M_NARGS L1)(ACTN, L1, L2)
#define M_ZIP_W2(ACTN, L1, L2) M_REST(M_ZIP_WITH(ACTN, L1, L2))

#define M_ZIP_WITH_0(ACTN, L1, L2)
#define M_ZIP_WITH_1(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2)
#define M_ZIP_WITH_2(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_1(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_3(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_2(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_4(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_3(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_5(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_4(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_6(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_5(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_7(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_6(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_8(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_7(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_9(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_8(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_10(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_9(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_11(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_10(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_12(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_11(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_13(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_12(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_14(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_13(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_15(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_14(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_16(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_15(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_17(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_16(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_18(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_17(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_19(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_18(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_20(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_19(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_21(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_20(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_22(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_21(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_23(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_22(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_24(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_23(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_25(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_24(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_26(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_25(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_27(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_26(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_28(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_27(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_29(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_28(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_30(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_29(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_31(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_30(ACTN, (M_REST L1), (M_REST L2))
#define M_ZIP_WITH_32(ACTN, L1, L2) ACTN(M_FIRST L1, M_FIRST L2) M_ZIP_WITH_31(ACTN, (M_REST L1), (M_REST L2))

#define M_REPT_0(A, ...)
#define M_REPT_1(A, ...) A __VA_ARGS__
#define M_REPT_2(A, ...) M_REPT_1(A, A __VA_ARGS__)
#define M_REPT_3(A, ...) M_REPT_2(A, A __VA_ARGS__)
#define M_REPT_4(A, ...) M_REPT_3(A, A __VA_ARGS__)
#define M_REPT_5(A, ...) M_REPT_4(A, A __VA_ARGS__)
#define M_REPT_6(A, ...) M_REPT_5(A, A __VA_ARGS__)
#define M_REPT_7(A, ...) M_REPT_6(A, A __VA_ARGS__)
#define M_REPT_8(A, ...) M_REPT_7(A, A __VA_ARGS__)
#define M_REPT_9(A, ...) M_REPT_8(A, A __VA_ARGS__)
#define M_REPT_10(A, ...) M_REPT_9(A, A __VA_ARGS__)
#define M_REPT_11(A, ...) M_REPT_10(A, A __VA_ARGS__)
#define M_REPT_12(A, ...) M_REPT_11(A, A __VA_ARGS__)
#define M_REPT_13(A, ...) M_REPT_12(A, A __VA_ARGS__)
#define M_REPT_14(A, ...) M_REPT_13(A, A __VA_ARGS__)
#define M_REPT_15(A, ...) M_REPT_14(A, A __VA_ARGS__)
#define M_REPT_16(A, ...) M_REPT_15(A, A __VA_ARGS__)
#define M_REPT_17(A, ...) M_REPT_16(A, A __VA_ARGS__)
#define M_REPT_18(A, ...) M_REPT_17(A, A __VA_ARGS__)
#define M_REPT_19(A, ...) M_REPT_18(A, A __VA_ARGS__)
#define M_REPT_20(A, ...) M_REPT_19(A, A __VA_ARGS__)
#define M_REPT_21(A, ...) M_REPT_20(A, A __VA_ARGS__)
#define M_REPT_22(A, ...) M_REPT_21(A, A __VA_ARGS__)
#define M_REPT_23(A, ...) M_REPT_22(A, A __VA_ARGS__)
#define M_REPT_24(A, ...) M_REPT_23(A, A __VA_ARGS__)
#define M_REPT_25(A, ...) M_REPT_24(A, A __VA_ARGS__)
#define M_REPT_26(A, ...) M_REPT_25(A, A __VA_ARGS__)
#define M_REPT_27(A, ...) M_REPT_26(A, A __VA_ARGS__)
#define M_REPT_28(A, ...) M_REPT_27(A, A __VA_ARGS__)
#define M_REPT_29(A, ...) M_REPT_28(A, A __VA_ARGS__)
#define M_REPT_30(A, ...) M_REPT_29(A, A __VA_ARGS__)
#define M_REPT_31(A, ...) M_REPT_30(A, A __VA_ARGS__)
#define M_REPT_32(A, ...) M_REPT_31(A, A __VA_ARGS__)

#define M_NEST_0(A, ...)
#define M_NEST_1(A, ...) A(__VA_ARGS__)
#define M_NEST_2(A, ...) M_NEST_1(A, A(__VA_ARGS__))
#define M_NEST_3(A, ...) M_NEST_2(A, A(__VA_ARGS__))
#define M_NEST_4(A, ...) M_NEST_3(A, A(__VA_ARGS__))
#define M_NEST_5(A, ...) M_NEST_4(A, A(__VA_ARGS__))
#define M_NEST_6(A, ...) M_NEST_5(A, A(__VA_ARGS__))
#define M_NEST_7(A, ...) M_NEST_6(A, A(__VA_ARGS__))
#define M_NEST_8(A, ...) M_NEST_7(A, A(__VA_ARGS__))
#define M_NEST_9(A, ...) M_NEST_8(A, A(__VA_ARGS__))
#define M_NEST_10(A, ...) M_NEST_9(A, A(__VA_ARGS__))
#define M_NEST_11(A, ...) M_NEST_10(A, A(__VA_ARGS__))
#define M_NEST_12(A, ...) M_NEST_11(A, A(__VA_ARGS__))
#define M_NEST_13(A, ...) M_NEST_12(A, A(__VA_ARGS__))
#define M_NEST_14(A, ...) M_NEST_13(A, A(__VA_ARGS__))
#define M_NEST_15(A, ...) M_NEST_14(A, A(__VA_ARGS__))
#define M_NEST_16(A, ...) M_NEST_15(A, A(__VA_ARGS__))
#define M_NEST_17(A, ...) M_NEST_16(A, A(__VA_ARGS__))
#define M_NEST_18(A, ...) M_NEST_17(A, A(__VA_ARGS__))
#define M_NEST_19(A, ...) M_NEST_18(A, A(__VA_ARGS__))
#define M_NEST_20(A, ...) M_NEST_19(A, A(__VA_ARGS__))
#define M_NEST_21(A, ...) M_NEST_20(A, A(__VA_ARGS__))
#define M_NEST_22(A, ...) M_NEST_21(A, A(__VA_ARGS__))
#define M_NEST_23(A, ...) M_NEST_22(A, A(__VA_ARGS__))
#define M_NEST_24(A, ...) M_NEST_23(A, A(__VA_ARGS__))
#define M_NEST_25(A, ...) M_NEST_24(A, A(__VA_ARGS__))
#define M_NEST_26(A, ...) M_NEST_25(A, A(__VA_ARGS__))
#define M_NEST_27(A, ...) M_NEST_26(A, A(__VA_ARGS__))
#define M_NEST_28(A, ...) M_NEST_27(A, A(__VA_ARGS__))
#define M_NEST_29(A, ...) M_NEST_28(A, A(__VA_ARGS__))
#define M_NEST_30(A, ...) M_NEST_29(A, A(__VA_ARGS__))
#define M_NEST_31(A, ...) M_NEST_30(A, A(__VA_ARGS__))
#define M_NEST_32(A, ...) M_NEST_31(A, A(__VA_ARGS__))

#define M_DO_0(...)
#define M_DO_1(...) (__VA_ARGS__)
#define M_DO_2(...) M_DO_1(__VA_ARGS__)
#define M_DO_3(...) M_DO_2(__VA_ARGS__)
#define M_DO_4(...) M_DO_3(__VA_ARGS__)
#define M_DO_5(...) M_DO_4(__VA_ARGS__)
#define M_DO_6(...) M_DO_5(__VA_ARGS__)
#define M_DO_7(...) M_DO_6(__VA_ARGS__)
#define M_DO_8(...) M_DO_7(__VA_ARGS__)
#define M_DO_9(...) M_DO_8(__VA_ARGS__)
#define M_DO_10(...) M_DO_9(__VA_ARGS__)
#define M_DO_11(...) M_DO_10(__VA_ARGS__)
#define M_DO_12(...) M_DO_11(__VA_ARGS__)
#define M_DO_13(...) M_DO_12(__VA_ARGS__)
#define M_DO_14(...) M_DO_13(__VA_ARGS__)
#define M_DO_15(...) M_DO_14(__VA_ARGS__)
#define M_DO_16(...) M_DO_15(__VA_ARGS__)
#define M_DO_17(...) M_DO_16(__VA_ARGS__)
#define M_DO_18(...) M_DO_17(__VA_ARGS__)
#define M_DO_19(...) M_DO_18(__VA_ARGS__)
#define M_DO_20(...) M_DO_19(__VA_ARGS__)
#define M_DO_21(...) M_DO_20(__VA_ARGS__)
#define M_DO_22(...) M_DO_21(__VA_ARGS__)
#define M_DO_23(...) M_DO_22(__VA_ARGS__)
#define M_DO_24(...) M_DO_23(__VA_ARGS__)
#define M_DO_25(...) M_DO_24(__VA_ARGS__)
#define M_DO_26(...) M_DO_25(__VA_ARGS__)
#define M_DO_27(...) M_DO_26(__VA_ARGS__)
#define M_DO_28(...) M_DO_27(__VA_ARGS__)
#define M_DO_29(...) M_DO_28(__VA_ARGS__)
#define M_DO_30(...) M_DO_29(__VA_ARGS__)
#define M_DO_31(...) M_DO_30(__VA_ARGS__)
#define M_DO_32(...) M_DO_31(__VA_ARGS__)

#define M_DIV2_2 1
#define M_DIV2_4 2
#define M_DIV2_6 3
#define M_DIV2_8 4
#define M_DIV2_10 5
#define M_DIV2_12 6
#define M_DIV2_14 7
#define M_DIV2_16 8
#define M_DIV2_18 9
#define M_DIV2_20 10
#define M_DIV2_22 11
#define M_DIV2_24 12
#define M_DIV2_26 13
#define M_DIV2_28 14
#define M_DIV2_30 15
#define M_DIV2_32 16


#endif









func(int, G, (void) {
	_fe1(foo, (int, (int x, int y), {return x*y;}));
	_fe2(foo, 1, (int, (int x, int y), {return x*y;}));
	_fe3(foo, 1, 2, (int, (int x, int y), {return x*y;}));
	_fe4(foo, 1, 2, 3, (int, (int x, int y), {return x*y;}));
	return 0;
})

namespace(myFunctionList,
  typedef int(* fptr)(void);
  fptr functions[] = {
    fn(int, (void), { return 1; }),  // Simple function literals
	fn(int, (void), { return 2; }),
	fn(int, (void), { return 3; })
  };
)

func(int, myGlobalFunc, (int blah) {
	//...code here, presumably involving inner functions
	return blah;
})

typedef int(* ifptr)(int);
func(ifptr, someFunc, (void) {
    return fn(int, (int a), {
        ifptr f = fn(int, (int b), { return b * 6; });
        return a * f(a + 1);
    });
})

func(int, someFunc1, (void) {
	int foo = fn(int, (int a, int b), { return (a + b) * (b - a); })(4, 5);
	fn(void, (int c), { printf("Received c: %d\n", c); })(47);
	return foo;
})

func(int, someFunc2, (void) {
	defn(int, helper, (int a, int b), {
		return (a + b) * (b - a);
	})
	int foo = helper(4, 5);
	return helper(foo, 2);
})

// User supplies a big enough buffer here... dirty trickery
func(void, makeAdder, (void * out_buf, int add) {
	closure_type(int, (int)) c = cl(int, (int x), ((int, add)), {
                                    return x + _env->add;
					             });
	memcpy(out_buf, c, c->_size);
	
	int x = 42, y = 47;
	cl(void, (...), ((int, x), (int, y)), {
		printf("%d, %d\n", _env->x, _env->y);
	});
})

/*
func(void, eTest, (void) {
	 // Assume gforeach and gmap are higher-order functions...
	_fe2(gforeach, glist((char*[]){"foo", "bar", "baz"}), (void, (char * c), {
		printf("%s: %d\n", c, strlen(c));
	}));

	GList * out; int k = 2;
	_cle2(gmap, glist((int[]){ 1, 2, 3, 4 }), (int, (int n), ((int, k)), {
		return n * _env->k;
	}), &out);
	// "out" now contains { 2, 4, 6, 8 }
})
*/

int main(void) {
	return 0;
}

