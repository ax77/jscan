#define TESTCONCAT(a, b) CC
#define RECURSION() TESTCONCAT
#define CC RECURSION()(1,0)
#define DD RECURSION()(C,C)

DD
