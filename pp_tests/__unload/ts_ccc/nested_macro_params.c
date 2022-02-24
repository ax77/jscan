//test return 14
/**
 * Check paramaters in nested macros
 */
#define FOO(a, b) ((a) + (b))
#define BAR(c, d) FOO(c * d, c + d)
int __test() {
    return BAR(4, 2);
}
