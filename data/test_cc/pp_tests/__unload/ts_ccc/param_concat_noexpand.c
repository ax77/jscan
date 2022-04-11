//test return 38
/**
 * Make sure concatenated macros are not expanded
 */
#define bool _Bool

int bool_bool = 33;

#define HELLO(type) type ## _ ## type
#define STRING(type) #type

int __test() {
    int *x = &HELLO(bool);
    int y = sizeof(STRING(bool));
    return *x + y;
}
