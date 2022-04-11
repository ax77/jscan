#define clone(...) __VA_ARGS__ __VA_ARGS__
A
clone({
 int x = 42;
})
B

#define EMPTY
EMPTY # include <file.h> // Expands to [#][include][<][file][.][h][>]

#undef EMPTY
EMPTY # include <file.h> // Expands to [EMPTY][#][include][<][file][.][h][>]

