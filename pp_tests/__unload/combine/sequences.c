#define O 0 +X ## Y A
#define A() A() B
#define B() B() A
#define C(x, y) x ## y
#define D(...) D D ## __VA_ARGS__ __VA_ARGS__ #__VA_ARGS__
#define ID(...) __VA_ARGS__
#define P(p, x) p ## x(P
O()
A()()()
C(C,)
D(D(0, 1))
ID (
1
)
P(,ID)(1,2),P(1,2)))
