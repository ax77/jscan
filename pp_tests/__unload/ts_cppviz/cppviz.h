/*

Copyright (c) 2018 Georg Ulbrich

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
 
/* guard macro */
#ifndef __CPPVIZ_H_INCLUDED
#define __CPPVIZ_H_INCLUDED

/* determine C standard */
// #ifdef __STDC_VERSION__
// #define __INTERNAL_STDC_VERSION__ __STDC_VERSION__
// #else
// #define __INTERNAL_STDC_VERSION__ 199409L
// #endif

#define __INTERNAL_STDC_VERSION__ 199901L

/* basic helper macros */
#define __CPPVIZ_NUL(...)
#define __CPPVIZ_ID(_1, _2, x)                      x
#define __CPPVIZ_ID_(_1, _2, x)                     x,
#define __CPPVIZ_CAT0(a, b)                         a ## b
#define __CPPVIZ_CAT(a, b)                          __CPPVIZ_CAT0(a, b)
#define __CPPVIZ_GET(I)                             __CPPVIZ_CAT(__CPPVIZ_GET_, I)
#define __CPPVIZ_APPLY(N, f, _1, _2, ...)           __CPPVIZ_CAT(N, _DATA)(f, _1, _2, __VA_ARGS__)

/* macros for picking elements */
#define __CPPVIZ_FIRST(f, _1, _2,  x, ...)          f(_1, _2, x)

/* macros for transforming elements */
#define __CPPVIZ_INC(_1, _2,  x)                    + 1
#define __CPPVIZ_ENUM_PREFIX(N)                     __CPPVIZ_CAT(N, _)
#define __CPPVIZ_ENUM_ELEMENT(_1, _2, x)            __CPPVIZ_CAT(_1, __CPPVIZ_CAT(_, x)),
#define __CPPVIZ_ELEMENT_NAME(_1, _2, x)            #x,
#define __CPPVIZ_SWITCH_CASE(_1, _2, x)             case __CPPVIZ_CAT(_1, __CPPVIZ_CAT(_, x)): __CPPVIZ_CAT(_1, __CPPVIZ_CAT(_, __CPPVIZ_CAT(x, __CPPVIZ_CAT(_, _2)))); break;
#define __CPPVIZ_ATTRIBUTE_TYPE(N, I)               __CPPVIZ_APPLY(N, __CPPVIZ_ID, __CPPVIZ_NUL, __CPPVIZ_NUL, __CPPVIZ_GET(I), __CPPVIZ_NUL, __CPPVIZ_NUL)
#define __CPPVIZ_ATTRIBUTE_NAME(N, I)               __CPPVIZ_APPLY(N, __CPPVIZ_ID, __CPPVIZ_NUL, __CPPVIZ_NUL, __CPPVIZ_NUL, __CPPVIZ_GET(I), __CPPVIZ_NUL)
#define __CPPVIZ_ATTRIBUTE_QNAME(N, I)              __CPPVIZ_CAT(__CPPVIZ_ENUM_PREFIX(N), __CPPVIZ_ATTRIBUTE_NAME(N, I))
#define __CPPVIZ_ATTRIBUTE_ELEMENT(N, I)            __CPPVIZ_APPLY(N, __CPPVIZ_ID_, N, __CPPVIZ_NUL, __CPPVIZ_NUL, __CPPVIZ_NUL, __CPPVIZ_GET(I))

/* create an enum for data table N */
#define ENUM(N)                                     typedef enum __CPPVIZ_CAT(_e_, N) { \
                                                        __CPPVIZ_APPLY(N, __CPPVIZ_ENUM_ELEMENT, N, __CPPVIZ_NUL, __CPPVIZ_NUL, __CPPVIZ_NUL, __CPPVIZ_FIRST) \
                                                    } N

/* create a table with the names of N */
#define NAMES(N)                                    const char * __CPPVIZ_CAT(N, _element_name)[] = { \
                                                        __CPPVIZ_APPLY(N, __CPPVIZ_ELEMENT_NAME, N, __CPPVIZ_NUL, __CPPVIZ_NUL, __CPPVIZ_NUL, __CPPVIZ_FIRST) \
                                                    }

/* count the elements in data table N */
#define COUNT(N)                                    (0 __CPPVIZ_APPLY(N, __CPPVIZ_INC, N, __CPPVIZ_NUL, __CPPVIZ_NUL, __CPPVIZ_NUL, __CPPVIZ_FIRST))

/* iterate over all elements of data table N */
#if __INTERNAL_STDC_VERSION__ < 199901L
#define FOREACH(N, n)                               for (n = (N) 0; n < COUNT(N); n = (N) (((int) n) + 1))
#else
#define FOREACH(N, n)                               for (N n = (N) 0; n < COUNT(N); n = (N) (((int) n) + 1))
#endif

/* create a switch over all elements of N */
#define SWITCH(N, n, name)                          switch (n) { \
                                                        __CPPVIZ_APPLY(N, __CPPVIZ_SWITCH_CASE, N, name, __CPPVIZ_NUL, __CPPVIZ_NUL, __CPPVIZ_FIRST) \
                                                        default: break; \
                                                    }

/* create an array for the I-th attribute */
#define ATTRIBUTE(N, I)                             __CPPVIZ_ATTRIBUTE_TYPE(N, I) __CPPVIZ_ATTRIBUTE_QNAME(N, I)[] = { \
                                                        __CPPVIZ_ATTRIBUTE_ELEMENT(N, I) \
                                                    }

/* script-start
n=100
echo "#define __CPPVIZ_GET_1(f, _1, _2, x, ...) f(_1, _2, x)"
for i in $(seq 2 $n)
do
    echo "#define __CPPVIZ_GET_$i(f, _1, _2, x, ...) __CPPVIZ_GET_$((i - 1))(f, _1, _2, __VA_ARGS__)"
done
script-end */

/* get-start */
#define __CPPVIZ_GET_1(f, _1, _2, x, ...) f(_1, _2, x)
#define __CPPVIZ_GET_2(f, _1, _2, x, ...) __CPPVIZ_GET_1(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_3(f, _1, _2, x, ...) __CPPVIZ_GET_2(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_4(f, _1, _2, x, ...) __CPPVIZ_GET_3(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_5(f, _1, _2, x, ...) __CPPVIZ_GET_4(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_6(f, _1, _2, x, ...) __CPPVIZ_GET_5(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_7(f, _1, _2, x, ...) __CPPVIZ_GET_6(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_8(f, _1, _2, x, ...) __CPPVIZ_GET_7(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_9(f, _1, _2, x, ...) __CPPVIZ_GET_8(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_10(f, _1, _2, x, ...) __CPPVIZ_GET_9(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_11(f, _1, _2, x, ...) __CPPVIZ_GET_10(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_12(f, _1, _2, x, ...) __CPPVIZ_GET_11(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_13(f, _1, _2, x, ...) __CPPVIZ_GET_12(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_14(f, _1, _2, x, ...) __CPPVIZ_GET_13(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_15(f, _1, _2, x, ...) __CPPVIZ_GET_14(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_16(f, _1, _2, x, ...) __CPPVIZ_GET_15(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_17(f, _1, _2, x, ...) __CPPVIZ_GET_16(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_18(f, _1, _2, x, ...) __CPPVIZ_GET_17(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_19(f, _1, _2, x, ...) __CPPVIZ_GET_18(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_20(f, _1, _2, x, ...) __CPPVIZ_GET_19(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_21(f, _1, _2, x, ...) __CPPVIZ_GET_20(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_22(f, _1, _2, x, ...) __CPPVIZ_GET_21(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_23(f, _1, _2, x, ...) __CPPVIZ_GET_22(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_24(f, _1, _2, x, ...) __CPPVIZ_GET_23(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_25(f, _1, _2, x, ...) __CPPVIZ_GET_24(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_26(f, _1, _2, x, ...) __CPPVIZ_GET_25(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_27(f, _1, _2, x, ...) __CPPVIZ_GET_26(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_28(f, _1, _2, x, ...) __CPPVIZ_GET_27(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_29(f, _1, _2, x, ...) __CPPVIZ_GET_28(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_30(f, _1, _2, x, ...) __CPPVIZ_GET_29(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_31(f, _1, _2, x, ...) __CPPVIZ_GET_30(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_32(f, _1, _2, x, ...) __CPPVIZ_GET_31(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_33(f, _1, _2, x, ...) __CPPVIZ_GET_32(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_34(f, _1, _2, x, ...) __CPPVIZ_GET_33(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_35(f, _1, _2, x, ...) __CPPVIZ_GET_34(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_36(f, _1, _2, x, ...) __CPPVIZ_GET_35(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_37(f, _1, _2, x, ...) __CPPVIZ_GET_36(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_38(f, _1, _2, x, ...) __CPPVIZ_GET_37(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_39(f, _1, _2, x, ...) __CPPVIZ_GET_38(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_40(f, _1, _2, x, ...) __CPPVIZ_GET_39(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_41(f, _1, _2, x, ...) __CPPVIZ_GET_40(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_42(f, _1, _2, x, ...) __CPPVIZ_GET_41(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_43(f, _1, _2, x, ...) __CPPVIZ_GET_42(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_44(f, _1, _2, x, ...) __CPPVIZ_GET_43(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_45(f, _1, _2, x, ...) __CPPVIZ_GET_44(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_46(f, _1, _2, x, ...) __CPPVIZ_GET_45(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_47(f, _1, _2, x, ...) __CPPVIZ_GET_46(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_48(f, _1, _2, x, ...) __CPPVIZ_GET_47(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_49(f, _1, _2, x, ...) __CPPVIZ_GET_48(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_50(f, _1, _2, x, ...) __CPPVIZ_GET_49(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_51(f, _1, _2, x, ...) __CPPVIZ_GET_50(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_52(f, _1, _2, x, ...) __CPPVIZ_GET_51(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_53(f, _1, _2, x, ...) __CPPVIZ_GET_52(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_54(f, _1, _2, x, ...) __CPPVIZ_GET_53(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_55(f, _1, _2, x, ...) __CPPVIZ_GET_54(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_56(f, _1, _2, x, ...) __CPPVIZ_GET_55(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_57(f, _1, _2, x, ...) __CPPVIZ_GET_56(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_58(f, _1, _2, x, ...) __CPPVIZ_GET_57(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_59(f, _1, _2, x, ...) __CPPVIZ_GET_58(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_60(f, _1, _2, x, ...) __CPPVIZ_GET_59(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_61(f, _1, _2, x, ...) __CPPVIZ_GET_60(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_62(f, _1, _2, x, ...) __CPPVIZ_GET_61(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_63(f, _1, _2, x, ...) __CPPVIZ_GET_62(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_64(f, _1, _2, x, ...) __CPPVIZ_GET_63(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_65(f, _1, _2, x, ...) __CPPVIZ_GET_64(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_66(f, _1, _2, x, ...) __CPPVIZ_GET_65(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_67(f, _1, _2, x, ...) __CPPVIZ_GET_66(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_68(f, _1, _2, x, ...) __CPPVIZ_GET_67(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_69(f, _1, _2, x, ...) __CPPVIZ_GET_68(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_70(f, _1, _2, x, ...) __CPPVIZ_GET_69(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_71(f, _1, _2, x, ...) __CPPVIZ_GET_70(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_72(f, _1, _2, x, ...) __CPPVIZ_GET_71(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_73(f, _1, _2, x, ...) __CPPVIZ_GET_72(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_74(f, _1, _2, x, ...) __CPPVIZ_GET_73(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_75(f, _1, _2, x, ...) __CPPVIZ_GET_74(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_76(f, _1, _2, x, ...) __CPPVIZ_GET_75(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_77(f, _1, _2, x, ...) __CPPVIZ_GET_76(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_78(f, _1, _2, x, ...) __CPPVIZ_GET_77(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_79(f, _1, _2, x, ...) __CPPVIZ_GET_78(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_80(f, _1, _2, x, ...) __CPPVIZ_GET_79(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_81(f, _1, _2, x, ...) __CPPVIZ_GET_80(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_82(f, _1, _2, x, ...) __CPPVIZ_GET_81(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_83(f, _1, _2, x, ...) __CPPVIZ_GET_82(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_84(f, _1, _2, x, ...) __CPPVIZ_GET_83(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_85(f, _1, _2, x, ...) __CPPVIZ_GET_84(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_86(f, _1, _2, x, ...) __CPPVIZ_GET_85(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_87(f, _1, _2, x, ...) __CPPVIZ_GET_86(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_88(f, _1, _2, x, ...) __CPPVIZ_GET_87(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_89(f, _1, _2, x, ...) __CPPVIZ_GET_88(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_90(f, _1, _2, x, ...) __CPPVIZ_GET_89(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_91(f, _1, _2, x, ...) __CPPVIZ_GET_90(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_92(f, _1, _2, x, ...) __CPPVIZ_GET_91(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_93(f, _1, _2, x, ...) __CPPVIZ_GET_92(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_94(f, _1, _2, x, ...) __CPPVIZ_GET_93(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_95(f, _1, _2, x, ...) __CPPVIZ_GET_94(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_96(f, _1, _2, x, ...) __CPPVIZ_GET_95(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_97(f, _1, _2, x, ...) __CPPVIZ_GET_96(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_98(f, _1, _2, x, ...) __CPPVIZ_GET_97(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_99(f, _1, _2, x, ...) __CPPVIZ_GET_98(f, _1, _2, __VA_ARGS__)
#define __CPPVIZ_GET_100(f, _1, _2, x, ...) __CPPVIZ_GET_99(f, _1, _2, __VA_ARGS__)
/* get-end */

/* end of guard macro */
#endif
