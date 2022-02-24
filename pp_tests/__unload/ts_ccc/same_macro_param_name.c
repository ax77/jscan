//test return 33
#define FOO(a) a
#define BAR(a) FOO(a)

int __test() {
    int a = 33;
    return BAR(a);
}
