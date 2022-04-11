#define NIL(xxx) xxx
#define G_0(arg) NIL(G_1)(arg)
#define G_1(arg) NIL(arg)
G_0(42)
