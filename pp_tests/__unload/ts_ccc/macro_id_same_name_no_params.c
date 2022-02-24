//test return 3
#define FOO(x) ((x) + (x))

enum foo {
    FOO = 3,
};

int __test() {
    return FOO;
}
