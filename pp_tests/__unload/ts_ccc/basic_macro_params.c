//test return 1340

#define SUM(a, b) ((a) + (b))

int __test() {
    int x = 3;
    short y = 1337;
    int z = SUM(x, y);
    return z;
}
