//test return 123
/**
 * Tests concatenation in macros
 */

#define CATTER(a, b, c) a ## b ##\
    c ## L

int __test() {
    return CATTER(1, 2, 3);
}
