# /* ********************************************************************
#  *                                                                    *
#  *    (C) Copyright Paul Mensonides 2003-2005.                        *
#  *                                                                    *
#  *    Distributed under the Boost Software License, Version 1.0.      *
#  *    (See accompanying file LICENSE).                                *
#  *                                                                    *
#  *    See http://chaos-pp.sourceforge.net for most recent version.    *
#  *                                                                    *
#  ******************************************************************** */
#
# ifndef CHAOS_PREPROCESSOR_RECURSION_MACHINE_H
# define CHAOS_PREPROCESSOR_RECURSION_MACHINE_H
#
# include <chaos/preprocessor/config.h>
# include <chaos/preprocessor/debug/failure.h>
# include <chaos/preprocessor/facilities/split.h>
# include <chaos/preprocessor/lambda/ops.h>
#
# /* CHAOS_PP_MACHINE */
#
# if CHAOS_PP_VARIADICS
#    define CHAOS_PP_MACHINE(k) \
        CHAOS_PP_SPLIT(1, CHAOS_IP_MACHINE_OUTER_A_0 k) \
        /**/
#    define CHAOS_PP_MACHINE_ID() CHAOS_PP_MACHINE
#    define CHAOS_PP_MACHINE_ CHAOS_PP_LAMBDA(CHAOS_PP_MACHINE_ID)()
# endif
#
# if CHAOS_PP_VARIADICS
#    define CHAOS_PP_INSTRUCTION(id) CHAOS_PP_INSTRUCTION_ ## id
#    define CHAOS_PP_INSTRUCTION_ID() CHAOS_PP_INSTRUCTION
#    define CHAOS_PP_INSTRUCTION_ CHAOS_PP_LAMBDA(CHAOS_PP_INSTRUCTION_ID)()
# endif
#
# if CHAOS_PP_VARIADICS
#    define CHAOS_PP_INSTRUCTION_0xchaos(id) CHAOS_PP_INSTRUCTION_0xchaos_ ## id
#    define CHAOS_PP_INSTRUCTION_0xchaos_0xstop(p, x, ...) , p ## x
# endif
#
# if CHAOS_PP_VARIADICS
#    define CHAOS_PP_INSTRUCTION_0xchaos_0xdetail(id) CHAOS_PP_INSTRUCTION_0xchaos_0xdetail_ ## id
# endif
#
# if CHAOS_PP_VARIADICS
#    define CHAOS_IP_MACHINE_INNER_B_0(...) CHAOS_IP_MACHINE_INNER_A_0 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_0(...) CHAOS_IP_MACHINE_OUTER_A_1 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_1(...) CHAOS_IP_MACHINE_INNER_A_1 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_1(...) CHAOS_IP_MACHINE_OUTER_A_2 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_2(...) CHAOS_IP_MACHINE_INNER_A_2 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_2(...) CHAOS_IP_MACHINE_OUTER_A_3 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_3(...) CHAOS_IP_MACHINE_INNER_A_3 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_3(...) CHAOS_IP_MACHINE_OUTER_A_4 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_4(...) CHAOS_IP_MACHINE_INNER_A_4 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_4(...) CHAOS_IP_MACHINE_OUTER_A_5 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_5(...) CHAOS_IP_MACHINE_INNER_A_5 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_5(...) CHAOS_IP_MACHINE_OUTER_A_6 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_6(...) CHAOS_IP_MACHINE_INNER_A_6 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_6(...) CHAOS_IP_MACHINE_OUTER_A_7 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_7(...) CHAOS_IP_MACHINE_INNER_A_7 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_7(...) CHAOS_IP_MACHINE_OUTER_A_8 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_8(...) CHAOS_IP_MACHINE_INNER_A_8 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_8(...) CHAOS_IP_MACHINE_OUTER_A_9 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_9(...) CHAOS_IP_MACHINE_INNER_A_9 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_9(...) CHAOS_IP_MACHINE_OUTER_A_10 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_10(...) CHAOS_IP_MACHINE_INNER_A_10 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_10(...) CHAOS_IP_MACHINE_OUTER_A_11 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_11(...) CHAOS_IP_MACHINE_INNER_A_11 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_11(...) CHAOS_IP_MACHINE_OUTER_A_12 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_12(...) CHAOS_IP_MACHINE_INNER_A_12 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_12(...) CHAOS_IP_MACHINE_OUTER_A_13 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_13(...) CHAOS_IP_MACHINE_INNER_A_13 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_13(...) CHAOS_IP_MACHINE_OUTER_A_14 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_14(...) CHAOS_IP_MACHINE_INNER_A_14 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_14(...) CHAOS_IP_MACHINE_OUTER_A_15 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_15(...) CHAOS_IP_MACHINE_INNER_A_15 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_15(...) CHAOS_IP_MACHINE_OUTER_A_16 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_16(...) CHAOS_IP_MACHINE_INNER_A_16 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_16(...) CHAOS_IP_MACHINE_OUTER_A_17 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_17(...) CHAOS_IP_MACHINE_INNER_A_17 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_17(...) CHAOS_IP_MACHINE_OUTER_A_18 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_18(...) CHAOS_IP_MACHINE_INNER_A_18 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_18(...) CHAOS_IP_MACHINE_OUTER_A_19 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_19(...) CHAOS_IP_MACHINE_INNER_A_19 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_19(...) CHAOS_IP_MACHINE_OUTER_A_20 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_20(...) CHAOS_IP_MACHINE_INNER_A_20 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_20(...) CHAOS_IP_MACHINE_OUTER_A_21 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_21(...) CHAOS_IP_MACHINE_INNER_A_21 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_21(...) CHAOS_IP_MACHINE_OUTER_A_22 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_22(...) CHAOS_IP_MACHINE_INNER_A_22 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_22(...) CHAOS_IP_MACHINE_OUTER_A_23 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_23(...) CHAOS_IP_MACHINE_INNER_A_23 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_23(...) CHAOS_IP_MACHINE_OUTER_A_24 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_24(...) CHAOS_IP_MACHINE_INNER_A_24 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_24(...) CHAOS_IP_MACHINE_OUTER_A_25 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_25(...) CHAOS_IP_MACHINE_INNER_A_25 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_25(...) CHAOS_IP_MACHINE_OUTER_A_26 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_26(...) CHAOS_IP_MACHINE_INNER_A_26 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_26(...) CHAOS_IP_MACHINE_OUTER_A_27 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_27(...) CHAOS_IP_MACHINE_INNER_A_27 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_27(...) CHAOS_IP_MACHINE_OUTER_A_28 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_28(...) CHAOS_IP_MACHINE_INNER_A_28 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_28(...) CHAOS_IP_MACHINE_OUTER_A_29 __VA_ARGS__
#    define CHAOS_IP_MACHINE_INNER_B_29(...) CHAOS_IP_MACHINE_INNER_A_29 __VA_ARGS__
#    define CHAOS_IP_MACHINE_OUTER_B_29(...) CHAOS_IP_MACHINE_OUTER_A_30 __VA_ARGS__
# endif
#
# if CHAOS_PP_VARIADICS
#    define CHAOS_IP_MACHINE_INNER_A_0(p, x, f, ...) CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)
#    define CHAOS_IP_MACHINE_OUTER_A_0(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_0(CHAOS_IP_MACHINE_INNER_B_0(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_1(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_0(CHAOS_IP_MACHINE_INNER_B_0(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_1(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_1(CHAOS_IP_MACHINE_INNER_B_1(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_2(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_1(CHAOS_IP_MACHINE_INNER_B_1(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_2(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_2(CHAOS_IP_MACHINE_INNER_B_2(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_3(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_2(CHAOS_IP_MACHINE_INNER_B_2(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_3(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_3(CHAOS_IP_MACHINE_INNER_B_3(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_4(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_3(CHAOS_IP_MACHINE_INNER_B_3(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_4(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_4(CHAOS_IP_MACHINE_INNER_B_4(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_5(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_4(CHAOS_IP_MACHINE_INNER_B_4(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_5(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_5(CHAOS_IP_MACHINE_INNER_B_5(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_6(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_5(CHAOS_IP_MACHINE_INNER_B_5(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_6(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_6(CHAOS_IP_MACHINE_INNER_B_6(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_7(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_6(CHAOS_IP_MACHINE_INNER_B_6(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_7(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_7(CHAOS_IP_MACHINE_INNER_B_7(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_8(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_7(CHAOS_IP_MACHINE_INNER_B_7(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_8(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_8(CHAOS_IP_MACHINE_INNER_B_8(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_9(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_8(CHAOS_IP_MACHINE_INNER_B_8(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_9(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_9(CHAOS_IP_MACHINE_INNER_B_9(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_10(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_9(CHAOS_IP_MACHINE_INNER_B_9(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_10(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_10(CHAOS_IP_MACHINE_INNER_B_10(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_11(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_10(CHAOS_IP_MACHINE_INNER_B_10(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_11(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_11(CHAOS_IP_MACHINE_INNER_B_11(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_12(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_11(CHAOS_IP_MACHINE_INNER_B_11(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_12(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_12(CHAOS_IP_MACHINE_INNER_B_12(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_13(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_12(CHAOS_IP_MACHINE_INNER_B_12(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_13(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_13(CHAOS_IP_MACHINE_INNER_B_13(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_14(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_13(CHAOS_IP_MACHINE_INNER_B_13(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_14(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_14(CHAOS_IP_MACHINE_INNER_B_14(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_15(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_14(CHAOS_IP_MACHINE_INNER_B_14(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_15(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_15(CHAOS_IP_MACHINE_INNER_B_15(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_16(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_15(CHAOS_IP_MACHINE_INNER_B_15(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_16(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_16(CHAOS_IP_MACHINE_INNER_B_16(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_17(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_16(CHAOS_IP_MACHINE_INNER_B_16(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_17(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_17(CHAOS_IP_MACHINE_INNER_B_17(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_18(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_17(CHAOS_IP_MACHINE_INNER_B_17(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_18(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_18(CHAOS_IP_MACHINE_INNER_B_18(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_19(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_18(CHAOS_IP_MACHINE_INNER_B_18(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_19(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_19(CHAOS_IP_MACHINE_INNER_B_19(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_20(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_19(CHAOS_IP_MACHINE_INNER_B_19(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_20(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_20(CHAOS_IP_MACHINE_INNER_B_20(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_21(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_20(CHAOS_IP_MACHINE_INNER_B_20(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_21(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_21(CHAOS_IP_MACHINE_INNER_B_21(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_22(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_21(CHAOS_IP_MACHINE_INNER_B_21(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_22(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_22(CHAOS_IP_MACHINE_INNER_B_22(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_23(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_22(CHAOS_IP_MACHINE_INNER_B_22(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_23(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_23(CHAOS_IP_MACHINE_INNER_B_23(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_24(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_23(CHAOS_IP_MACHINE_INNER_B_23(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_24(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_24(CHAOS_IP_MACHINE_INNER_B_24(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_25(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_24(CHAOS_IP_MACHINE_INNER_B_24(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_25(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_25(CHAOS_IP_MACHINE_INNER_B_25(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_26(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_25(CHAOS_IP_MACHINE_INNER_B_25(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_26(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_26(CHAOS_IP_MACHINE_INNER_B_26(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_27(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_26(CHAOS_IP_MACHINE_INNER_B_26(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_27(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_27(CHAOS_IP_MACHINE_INNER_B_27(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_28(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_27(CHAOS_IP_MACHINE_INNER_B_27(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_28(p, x, f, ...) CHAOS_IP_MACHINE_OUTER_B_28(CHAOS_IP_MACHINE_INNER_B_28(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_INNER_A_29(p, x, f, ...) CHAOS_IP_MACHINE_INNER_B_28(CHAOS_IP_MACHINE_INNER_B_28(CHAOS_PP_INSTRUCTION p ## f(, p ## x, p ## __VA_ARGS__)))
#    define CHAOS_IP_MACHINE_OUTER_A_29(p, x, f, ...) CHAOS_PP_FAILURE()
# endif
#
# endif