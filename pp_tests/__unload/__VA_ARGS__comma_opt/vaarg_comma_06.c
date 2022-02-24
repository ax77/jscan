// Accept any number of args >= N, but expand to just the Nth one. The macro
// that calls us still only supports 4 args, but the set of values we might
// need to return is 1 larger, so we increase N to 6.
#define _GET_NTH_ARG(_1, _2, _3, _4, _5, N, ...) N
 
// Count how many args are in a variadic macro. We now use GCC/Clang's extension to
// handle the case where ... expands to nothing. We must add a placeholder arg before
// ##__VA_ARGS__ (its value is totally irrelevant, but it's necessary to preserve
// the shifting offset we want). In addition, we must add 0 as a valid value to be in
// the N position.
#define COUNT_VARARGS(...) _GET_NTH_ARG("ignored", ##__VA_ARGS__, 4, 3, 2, 1, 0)

int main() {
    printf("zero args: %d\n", COUNT_VARARGS());
    printf("three args: %d\n", COUNT_VARARGS(1, 2, 3));
}
