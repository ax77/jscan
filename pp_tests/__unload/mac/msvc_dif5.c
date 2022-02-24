// https://stackoverflow.com/questions/35210637/macro-expansion-argument-with-commas

#define ARGS 1,2,3

#define MACROFUNC_OUTER(PARAMS) MACROFUNC_INNER(PARAMS)
#define MACROFUNC_INNER(A,B,C) A + B + C

int a = MACROFUNC_OUTER(ARGS);
