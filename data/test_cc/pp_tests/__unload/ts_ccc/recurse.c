//test return 11
// Make sure self referential macros aren't expanded twice in argument of
// another macro
// https://gcc.gnu.org/onlinedocs/cpp/Argument-Prescan.html#Argument-Prescan

int plus(int a, int b) {
    return a + b;
}

#define FOO(x) x
#define plus(x, y) x * y + plus(x, y)

int __test() {
    return FOO(plus(2, 3));
}
