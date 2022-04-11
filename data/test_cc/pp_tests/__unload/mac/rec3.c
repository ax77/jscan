#define test(x) x
int test = 0;
int x = test;
int y = test ( test ( test ( test ( x ) ) ) );
int z = test ( test ( test ( test ( y ) ) ) );

#define TESTCONCAT(a, b) AGAIN
#define RECURSION() TESTCONCAT
#define AGAIN RECURSION()(1,2)

AGAIN
