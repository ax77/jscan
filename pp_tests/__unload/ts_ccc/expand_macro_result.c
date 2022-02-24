//test return 4
#define FOUR 4
#define CONCAT(x, y) x ## y

int __test() {
    return CONCAT(FO, UR);
}
