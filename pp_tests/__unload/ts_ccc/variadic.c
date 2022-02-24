//test return 17
//#include <stdio.h>

#define eprintf(format, ...) fprintf (stdout, format, __VA_ARGS__)

int __test() {
    return eprintf("yolo %s %d\n", "hello", 0x3333);
}
