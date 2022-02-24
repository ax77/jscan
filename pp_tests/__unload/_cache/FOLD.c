// Public Domain 2016 Daniel Rasmussen
/* General Utility Macros */
#define EXPAND(a) a
#define FIRST_ARG(a, ...) a
#define REST_ARG(a, ...) __VA_ARGS__
#define NUM_ARGS_(a73,a72,a71,a70,a69,a68,a67,a66,a65,a64,a63,a62,a61,a60,a59,a58,a57,a56,a55,a54,a53,a52,a51,a50,a49,a48,a47,a46,a45,a44,a43,a42,a41,a40,a39,a38,a37,a36,a35,a34,a33,a32,a31,a30,a29,a28,a27,a26,a25,a24,a23,a22,a21,a20,a19,a18,a17,a16,a15,a14,a13,a12,a11,a10,a9,a8,a7,a6,a5,a4,a3,a2,a1,N,...) N
#define NUM_ARGS(...) EXPAND(NUM_ARGS_(__VA_ARGS__,73,72,71,70,69,68,67,66,65,64,63,62,61,60,59,58,57,56,55,54,53,52,51,50,49,48,47,46,45,44,43,42,41,40,39,38,37,36,35,34,33,32,31,30,29,28,27,26,25,24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1))

/* FOLD Macros */
#define FOLD_VERSION 2
#define FOLD(...) FOLD_(NUM_ARGS(__VA_ARGS__), FIRST_ARG(__VA_ARGS__), REST_ARG(__VA_ARGS__))
#define FOLD_(N, M, ...) FOLD__(N, M, __VA_ARGS__)
#define FOLD__(N, M, ...) FOLD_##N(M, FIRST_ARG(__VA_ARGS__), REST_ARG(__VA_ARGS__))
#define FOLD_1(F, ...)
#define FOLD_2(F, ...) FIRST_ARG(__VA_ARGS__)
#define FOLD_3_(F, R) EXPAND(F R)
#define FOLD_3(F, R, L) FOLD_3_(F, (R, L))
#define FOLD_4_(F, R, ...) FOLD_3(F, F R, __VA_ARGS__)
#define FOLD_4(F, R, ...) FOLD_4_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_5_(F, R, ...) FOLD_4(F, F R, __VA_ARGS__)
#define FOLD_5(F, R, ...) FOLD_5_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_6_(F, R, ...) FOLD_5(F, F R, __VA_ARGS__)
#define FOLD_6(F, R, ...) FOLD_6_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_7_(F, R, ...) FOLD_6(F, F R, __VA_ARGS__)
#define FOLD_7(F, R, ...) FOLD_7_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_8_(F, R, ...) FOLD_7(F, F R, __VA_ARGS__)
#define FOLD_8(F, R, ...) FOLD_8_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_9_(F, R, ...) FOLD_8(F, F R, __VA_ARGS__)
#define FOLD_9(F, R, ...) FOLD_9_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_10_(F, R, ...) FOLD_9(F, F R, __VA_ARGS__)
#define FOLD_10(F, R, ...) FOLD_10_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_11_(F, R, ...) FOLD_10(F, F R, __VA_ARGS__)
#define FOLD_11(F, R, ...) FOLD_11_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_12_(F, R, ...) FOLD_11(F, F R, __VA_ARGS__)
#define FOLD_12(F, R, ...) FOLD_12_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_13_(F, R, ...) FOLD_12(F, F R, __VA_ARGS__)
#define FOLD_13(F, R, ...) FOLD_13_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_14_(F, R, ...) FOLD_13(F, F R, __VA_ARGS__)
#define FOLD_14(F, R, ...) FOLD_14_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_15_(F, R, ...) FOLD_14(F, F R, __VA_ARGS__)
#define FOLD_15(F, R, ...) FOLD_15_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_16_(F, R, ...) FOLD_15(F, F R, __VA_ARGS__)
#define FOLD_16(F, R, ...) FOLD_16_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_17_(F, R, ...) FOLD_16(F, F R, __VA_ARGS__)
#define FOLD_17(F, R, ...) FOLD_17_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_18_(F, R, ...) FOLD_17(F, F R, __VA_ARGS__)
#define FOLD_18(F, R, ...) FOLD_18_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_19_(F, R, ...) FOLD_18(F, F R, __VA_ARGS__)
#define FOLD_19(F, R, ...) FOLD_19_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_20_(F, R, ...) FOLD_19(F, F R, __VA_ARGS__)
#define FOLD_20(F, R, ...) FOLD_20_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_21_(F, R, ...) FOLD_20(F, F R, __VA_ARGS__)
#define FOLD_21(F, R, ...) FOLD_21_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_22_(F, R, ...) FOLD_21(F, F R, __VA_ARGS__)
#define FOLD_22(F, R, ...) FOLD_22_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_23_(F, R, ...) FOLD_22(F, F R, __VA_ARGS__)
#define FOLD_23(F, R, ...) FOLD_23_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_24_(F, R, ...) FOLD_23(F, F R, __VA_ARGS__)
#define FOLD_24(F, R, ...) FOLD_24_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_25_(F, R, ...) FOLD_24(F, F R, __VA_ARGS__)
#define FOLD_25(F, R, ...) FOLD_25_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_26_(F, R, ...) FOLD_25(F, F R, __VA_ARGS__)
#define FOLD_26(F, R, ...) FOLD_26_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_27_(F, R, ...) FOLD_26(F, F R, __VA_ARGS__)
#define FOLD_27(F, R, ...) FOLD_27_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_28_(F, R, ...) FOLD_27(F, F R, __VA_ARGS__)
#define FOLD_28(F, R, ...) FOLD_28_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_29_(F, R, ...) FOLD_28(F, F R, __VA_ARGS__)
#define FOLD_29(F, R, ...) FOLD_29_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_30_(F, R, ...) FOLD_29(F, F R, __VA_ARGS__)
#define FOLD_30(F, R, ...) FOLD_30_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_31_(F, R, ...) FOLD_30(F, F R, __VA_ARGS__)
#define FOLD_31(F, R, ...) FOLD_31_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_32_(F, R, ...) FOLD_31(F, F R, __VA_ARGS__)
#define FOLD_32(F, R, ...) FOLD_32_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_33_(F, R, ...) FOLD_32(F, F R, __VA_ARGS__)
#define FOLD_33(F, R, ...) FOLD_33_(F, (R, FIRST_ARG(__VA_ARGS__)), REST_ARG(__VA_ARGS__))
#define FOLD_34(...) -----"Over Max (32) arguments to FOLD vararg passed (33)"-----
#define FOLD_35(...) -----"Over Max (32) arguments to FOLD vararg passed (34)"-----
#define FOLD_36(...) -----"Over Max (32) arguments to FOLD vararg passed (35)"-----
#define FOLD_37(...) -----"Over Max (32) arguments to FOLD vararg passed (36)"-----
#define FOLD_38(...) -----"Over Max (32) arguments to FOLD vararg passed (37)"-----
#define FOLD_39(...) -----"Over Max (32) arguments to FOLD vararg passed (38)"-----
#define FOLD_40(...) -----"Over Max (32) arguments to FOLD vararg passed (39)"-----
#define FOLD_41(...) -----"Over Max (32) arguments to FOLD vararg passed (40)"-----
#define FOLD_42(...) -----"Over Max (32) arguments to FOLD vararg passed (41)"-----
#define FOLD_43(...) -----"Over Max (32) arguments to FOLD vararg passed (42)"-----
#define FOLD_44(...) -----"Over Max (32) arguments to FOLD vararg passed (43)"-----
#define FOLD_45(...) -----"Over Max (32) arguments to FOLD vararg passed (44)"-----
#define FOLD_46(...) -----"Over Max (32) arguments to FOLD vararg passed (45)"-----
#define FOLD_47(...) -----"Over Max (32) arguments to FOLD vararg passed (46)"-----
#define FOLD_48(...) -----"Over Max (32) arguments to FOLD vararg passed (47)"-----
#define FOLD_49(...) -----"Over Max (32) arguments to FOLD vararg passed (48)"-----
#define FOLD_50(...) -----"Over Max (32) arguments to FOLD vararg passed (49)"-----
#define FOLD_51(...) -----"Over Max (32) arguments to FOLD vararg passed (50)"-----
#define FOLD_52(...) -----"Over Max (32) arguments to FOLD vararg passed (51)"-----

/* FOLDR Macros */
#define BEGIN_ARG_1(...)
#define LAST_ARG_1(...)
#define BEGIN_ARG_2(...) 
#define LAST_ARG_2(...) __VA_ARGS__
#define BEGIN_ARG_3(a1,...) a1
#define LAST_ARG_3(a1,...) __VA_ARGS__
#define BEGIN_ARG_4(a1,a2,...) a1,a2
#define LAST_ARG_4(a1,a2,...) __VA_ARGS__
#define BEGIN_ARG_5(a1,a2,a3,...) a1,a2,a3
#define LAST_ARG_5(a1,a2,a3,...) __VA_ARGS__
#define BEGIN_ARG_6(a1,a2,a3,a4,...) a1,a2,a3,a4
#define LAST_ARG_6(a1,a2,a3,a4,...) __VA_ARGS__
#define BEGIN_ARG_7(a1,a2,a3,a4,a5,...) a1,a2,a3,a4,a5
#define LAST_ARG_7(a1,a2,a3,a4,a5,...) __VA_ARGS__
#define BEGIN_ARG_8(a1,a2,a3,a4,a5,a6,...) a1,a2,a3,a4,a5,a6
#define LAST_ARG_8(a1,a2,a3,a4,a5,a6,...) __VA_ARGS__
#define BEGIN_ARG_9(a1,a2,a3,a4,a5,a6,a7,...) a1,a2,a3,a4,a5,a6,a7
#define LAST_ARG_9(a1,a2,a3,a4,a5,a6,a7,...) __VA_ARGS__
#define BEGIN_ARG_10(a1,a2,a3,a4,a5,a6,a7,a8,...) a1,a2,a3,a4,a5,a6,a7,a8
#define LAST_ARG_10(a1,a2,a3,a4,a5,a6,a7,a8,...) __VA_ARGS__
#define BEGIN_ARG_11(a1,a2,a3,a4,a5,a6,a7,a8,a9,...) a1,a2,a3,a4,a5,a6,a7,a8,a9
#define LAST_ARG_11(a1,a2,a3,a4,a5,a6,a7,a8,a9,...) __VA_ARGS__
#define BEGIN_ARG_12(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10
#define LAST_ARG_12(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,...) __VA_ARGS__
#define BEGIN_ARG_13(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11
#define LAST_ARG_13(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,...) __VA_ARGS__
#define BEGIN_ARG_14(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12
#define LAST_ARG_14(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,...) __VA_ARGS__
#define BEGIN_ARG_15(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13
#define LAST_ARG_15(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,...) __VA_ARGS__
#define BEGIN_ARG_16(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14
#define LAST_ARG_16(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,...) __VA_ARGS__
#define BEGIN_ARG_17(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15
#define LAST_ARG_17(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,...) __VA_ARGS__
#define BEGIN_ARG_18(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16
#define LAST_ARG_18(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,...) __VA_ARGS__
#define BEGIN_ARG_19(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17
#define LAST_ARG_19(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,...) __VA_ARGS__
#define BEGIN_ARG_20(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18
#define LAST_ARG_20(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,...) __VA_ARGS__
#define BEGIN_ARG_21(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19
#define LAST_ARG_21(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,...) __VA_ARGS__
#define BEGIN_ARG_22(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20
#define LAST_ARG_22(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,...) __VA_ARGS__
#define BEGIN_ARG_23(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21
#define LAST_ARG_23(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,...) __VA_ARGS__
#define BEGIN_ARG_24(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22
#define LAST_ARG_24(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,...) __VA_ARGS__
#define BEGIN_ARG_25(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23
#define LAST_ARG_25(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,...) __VA_ARGS__
#define BEGIN_ARG_26(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24
#define LAST_ARG_26(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,...) __VA_ARGS__
#define BEGIN_ARG_27(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25
#define LAST_ARG_27(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,...) __VA_ARGS__
#define BEGIN_ARG_28(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26
#define LAST_ARG_28(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,...) __VA_ARGS__
#define BEGIN_ARG_29(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27
#define LAST_ARG_29(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,...) __VA_ARGS__
#define BEGIN_ARG_30(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28
#define LAST_ARG_30(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,...) __VA_ARGS__
#define BEGIN_ARG_31(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29
#define LAST_ARG_31(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,...) __VA_ARGS__
#define BEGIN_ARG_32(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30
#define LAST_ARG_32(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,...) __VA_ARGS__
#define BEGIN_ARG_33(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,a31,...) a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,a31
#define LAST_ARG_33(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,a31,...) __VA_ARGS__
#define FOLDR(...) FOLDR_(NUM_ARGS(__VA_ARGS__), FIRST_ARG(__VA_ARGS__), REST_ARG(__VA_ARGS__))
#define FOLDR_(N, F, ...) FOLDR__(N, F, __VA_ARGS__)
#define FOLDR__(N, F, ...) FOLDR_##N(F, LAST_ARG_##N(__VA_ARGS__), BEGIN_ARG_##N(__VA_ARGS__))
#define FOLDR_1(F, ...)
#define FOLDR_2(F, ...) EXPAND(FIRST_ARG(__VA_ARGS__))
#define FOLDR_3_(F, R) EXPAND(F R)
#define FOLDR_3(F, R, L) FOLDR_3_(F, (L, R))
#define FOLDR_4_(F, R, ...) FOLDR_3(F, F R, __VA_ARGS__)
#define FOLDR_4(F, R, ...) FOLDR_4_(F, (LAST_ARG_3(__VA_ARGS__), R), BEGIN_ARG_3(__VA_ARGS__))
#define FOLDR_5_(F, R, ...) FOLDR_4(F, F R, __VA_ARGS__)
#define FOLDR_5(F, R, ...) FOLDR_5_(F, (LAST_ARG_4(__VA_ARGS__), R), BEGIN_ARG_4(__VA_ARGS__))
#define FOLDR_6_(F, R, ...) FOLDR_5(F, F R, __VA_ARGS__)
#define FOLDR_6(F, R, ...) FOLDR_6_(F, (LAST_ARG_5(__VA_ARGS__), R), BEGIN_ARG_5(__VA_ARGS__))
#define FOLDR_7_(F, R, ...) FOLDR_6(F, F R, __VA_ARGS__)
#define FOLDR_7(F, R, ...) FOLDR_7_(F, (LAST_ARG_6(__VA_ARGS__), R), BEGIN_ARG_6(__VA_ARGS__))
#define FOLDR_8_(F, R, ...) FOLDR_7(F, F R, __VA_ARGS__)
#define FOLDR_8(F, R, ...) FOLDR_8_(F, (LAST_ARG_7(__VA_ARGS__), R), BEGIN_ARG_7(__VA_ARGS__))
#define FOLDR_9_(F, R, ...) FOLDR_8(F, F R, __VA_ARGS__)
#define FOLDR_9(F, R, ...) FOLDR_9_(F, (LAST_ARG_8(__VA_ARGS__), R), BEGIN_ARG_8(__VA_ARGS__))
#define FOLDR_10_(F, R, ...) FOLDR_9(F, F R, __VA_ARGS__)
#define FOLDR_10(F, R, ...) FOLDR_10_(F, (LAST_ARG_9(__VA_ARGS__), R), BEGIN_ARG_9(__VA_ARGS__))
#define FOLDR_11_(F, R, ...) FOLDR_10(F, F R, __VA_ARGS__)
#define FOLDR_11(F, R, ...) FOLDR_11_(F, (LAST_ARG_10(__VA_ARGS__), R), BEGIN_ARG_10(__VA_ARGS__))
#define FOLDR_12_(F, R, ...) FOLDR_11(F, F R, __VA_ARGS__)
#define FOLDR_12(F, R, ...) FOLDR_12_(F, (LAST_ARG_11(__VA_ARGS__), R), BEGIN_ARG_11(__VA_ARGS__))
#define FOLDR_13_(F, R, ...) FOLDR_12(F, F R, __VA_ARGS__)
#define FOLDR_13(F, R, ...) FOLDR_13_(F, (LAST_ARG_12(__VA_ARGS__), R), BEGIN_ARG_12(__VA_ARGS__))
#define FOLDR_14_(F, R, ...) FOLDR_13(F, F R, __VA_ARGS__)
#define FOLDR_14(F, R, ...) FOLDR_14_(F, (LAST_ARG_13(__VA_ARGS__), R), BEGIN_ARG_13(__VA_ARGS__))
#define FOLDR_15_(F, R, ...) FOLDR_14(F, F R, __VA_ARGS__)
#define FOLDR_15(F, R, ...) FOLDR_15_(F, (LAST_ARG_14(__VA_ARGS__), R), BEGIN_ARG_14(__VA_ARGS__))
#define FOLDR_16_(F, R, ...) FOLDR_15(F, F R, __VA_ARGS__)
#define FOLDR_16(F, R, ...) FOLDR_16_(F, (LAST_ARG_15(__VA_ARGS__), R), BEGIN_ARG_15(__VA_ARGS__))
#define FOLDR_17_(F, R, ...) FOLDR_16(F, F R, __VA_ARGS__)
#define FOLDR_17(F, R, ...) FOLDR_17_(F, (LAST_ARG_16(__VA_ARGS__), R), BEGIN_ARG_16(__VA_ARGS__))
#define FOLDR_18_(F, R, ...) FOLDR_17(F, F R, __VA_ARGS__)
#define FOLDR_18(F, R, ...) FOLDR_18_(F, (LAST_ARG_17(__VA_ARGS__), R), BEGIN_ARG_17(__VA_ARGS__))
#define FOLDR_19_(F, R, ...) FOLDR_18(F, F R, __VA_ARGS__)
#define FOLDR_19(F, R, ...) FOLDR_19_(F, (LAST_ARG_18(__VA_ARGS__), R), BEGIN_ARG_18(__VA_ARGS__))
#define FOLDR_20_(F, R, ...) FOLDR_19(F, F R, __VA_ARGS__)
#define FOLDR_20(F, R, ...) FOLDR_20_(F, (LAST_ARG_19(__VA_ARGS__), R), BEGIN_ARG_19(__VA_ARGS__))
#define FOLDR_21_(F, R, ...) FOLDR_20(F, F R, __VA_ARGS__)
#define FOLDR_21(F, R, ...) FOLDR_21_(F, (LAST_ARG_20(__VA_ARGS__), R), BEGIN_ARG_20(__VA_ARGS__))
#define FOLDR_22_(F, R, ...) FOLDR_21(F, F R, __VA_ARGS__)
#define FOLDR_22(F, R, ...) FOLDR_22_(F, (LAST_ARG_21(__VA_ARGS__), R), BEGIN_ARG_21(__VA_ARGS__))
#define FOLDR_23_(F, R, ...) FOLDR_22(F, F R, __VA_ARGS__)
#define FOLDR_23(F, R, ...) FOLDR_23_(F, (LAST_ARG_22(__VA_ARGS__), R), BEGIN_ARG_22(__VA_ARGS__))
#define FOLDR_24_(F, R, ...) FOLDR_23(F, F R, __VA_ARGS__)
#define FOLDR_24(F, R, ...) FOLDR_24_(F, (LAST_ARG_23(__VA_ARGS__), R), BEGIN_ARG_23(__VA_ARGS__))
#define FOLDR_25_(F, R, ...) FOLDR_24(F, F R, __VA_ARGS__)
#define FOLDR_25(F, R, ...) FOLDR_25_(F, (LAST_ARG_24(__VA_ARGS__), R), BEGIN_ARG_24(__VA_ARGS__))
#define FOLDR_26_(F, R, ...) FOLDR_25(F, F R, __VA_ARGS__)
#define FOLDR_26(F, R, ...) FOLDR_26_(F, (LAST_ARG_25(__VA_ARGS__), R), BEGIN_ARG_25(__VA_ARGS__))
#define FOLDR_27_(F, R, ...) FOLDR_26(F, F R, __VA_ARGS__)
#define FOLDR_27(F, R, ...) FOLDR_27_(F, (LAST_ARG_26(__VA_ARGS__), R), BEGIN_ARG_26(__VA_ARGS__))
#define FOLDR_28_(F, R, ...) FOLDR_27(F, F R, __VA_ARGS__)
#define FOLDR_28(F, R, ...) FOLDR_28_(F, (LAST_ARG_27(__VA_ARGS__), R), BEGIN_ARG_27(__VA_ARGS__))
#define FOLDR_29_(F, R, ...) FOLDR_28(F, F R, __VA_ARGS__)
#define FOLDR_29(F, R, ...) FOLDR_29_(F, (LAST_ARG_28(__VA_ARGS__), R), BEGIN_ARG_28(__VA_ARGS__))
#define FOLDR_30_(F, R, ...) FOLDR_29(F, F R, __VA_ARGS__)
#define FOLDR_30(F, R, ...) FOLDR_30_(F, (LAST_ARG_29(__VA_ARGS__), R), BEGIN_ARG_29(__VA_ARGS__))
#define FOLDR_31_(F, R, ...) FOLDR_30(F, F R, __VA_ARGS__)
#define FOLDR_31(F, R, ...) FOLDR_31_(F, (LAST_ARG_30(__VA_ARGS__), R), BEGIN_ARG_30(__VA_ARGS__))
#define FOLDR_32_(F, R, ...) FOLDR_31(F, F R, __VA_ARGS__)
#define FOLDR_32(F, R, ...) FOLDR_32_(F, (LAST_ARG_31(__VA_ARGS__), R), BEGIN_ARG_31(__VA_ARGS__))
#define FOLDR_33_(F, R, ...) FOLDR_32(F, F R, __VA_ARGS__)
#define FOLDR_33(F, R, ...) FOLDR_33_(F, (LAST_ARG_32(__VA_ARGS__), R), BEGIN_ARG_32(__VA_ARGS__))
#define FOLDR_34(...) -----"Over Max (32) arguments to FOLDR vararg passed (33)"-----
#define FOLDR_35(...) -----"Over Max (32) arguments to FOLDR vararg passed (34)"-----
#define FOLDR_36(...) -----"Over Max (32) arguments to FOLDR vararg passed (35)"-----
#define FOLDR_37(...) -----"Over Max (32) arguments to FOLDR vararg passed (36)"-----
#define FOLDR_38(...) -----"Over Max (32) arguments to FOLDR vararg passed (37)"-----
#define FOLDR_39(...) -----"Over Max (32) arguments to FOLDR vararg passed (38)"-----
#define FOLDR_40(...) -----"Over Max (32) arguments to FOLDR vararg passed (39)"-----
#define FOLDR_41(...) -----"Over Max (32) arguments to FOLDR vararg passed (40)"-----
#define FOLDR_42(...) -----"Over Max (32) arguments to FOLDR vararg passed (41)"-----
#define FOLDR_43(...) -----"Over Max (32) arguments to FOLDR vararg passed (42)"-----
#define FOLDR_44(...) -----"Over Max (32) arguments to FOLDR vararg passed (43)"-----
#define FOLDR_45(...) -----"Over Max (32) arguments to FOLDR vararg passed (44)"-----
#define FOLDR_46(...) -----"Over Max (32) arguments to FOLDR vararg passed (45)"-----
#define FOLDR_47(...) -----"Over Max (32) arguments to FOLDR vararg passed (46)"-----
#define FOLDR_48(...) -----"Over Max (32) arguments to FOLDR vararg passed (47)"-----
#define FOLDR_49(...) -----"Over Max (32) arguments to FOLDR vararg passed (48)"-----
#define FOLDR_50(...) -----"Over Max (32) arguments to FOLDR vararg passed (49)"-----
#define FOLDR_51(...) -----"Over Max (32) arguments to FOLDR vararg passed (50)"-----
#define FOLDR_52(...) -----"Over Max (32) arguments to FOLDR vararg passed (51)"-----




//Macro                   Expansion
FOLD(Func, a)          // a
FOLD(Func, a, b)       // Func(a, b)
FOLD(Func, a, b, c)    // Func(Func(a, b), c)
FOLD(Func, a, b, c, d) // Func(Func(Func(a, b), c), d)


// Directly make Func a vararg function
int Func_(int a, float b);
#define Func(...) FOLD(Func_, __VA_ARGS__)
//Macro             Expansion
Func(a)          // a
Func(a, b)       // Func_(a, b)
Func(a, b, c)    // Func_(Func_(a, b), c)
Func(a, b, c, d) // Func_(Func_(Func_(a, b), c), d)
#undef Func


// With preprocessor macro
#define CONCAT(a, b) a##b
//Macro                     Expansion
FOLD(CONCAT, a)          // a
FOLD(CONCAT, a, b)       // ab
FOLD(CONCAT, a, b, c)    // abc
FOLD(CONCAT, a, b, c, d) // abcd
#undef CONCAT


// Or more simply
#define CONCAT_(a, b) a##b
#define CONCAT(...) FOLD(CONCAT_, __VA_ARGS__)
//Macro               Expansion
CONCAT(a)          // a
CONCAT(a, b)       // ab
CONCAT(a, b, c)    // abc
CONCAT(a, b, c, d) // abcd





















