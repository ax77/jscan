//test return 4
/**
 * Make sure leading and trailing space is trimmed from macro parameters
 */

#define STR(x) #x

int __test() {
    return sizeof(STR(     foo     ));
}
