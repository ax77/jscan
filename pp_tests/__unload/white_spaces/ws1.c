#define str(s) # s
#define xstr(s) str(s)
#define INCFILE(n) vers ## n
\#include xstr(INCFILE(2).h)

#define foo() bar
foo()baz
