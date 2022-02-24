//test return 18
/**
 * Tests macros in macro parameters
 */
#define FOO(a, b) ((a) * (b))
#define BAR(a, b) ((a) + (b))
#define BAZ 2
int __test() {
    return FOO(3, BAR(BAZ, 4));
}
