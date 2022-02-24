//test return 3
//#include <string.h>

#define FOO 3
#define WRAP(x) x
#define STR(x) #x

int __test() {
    return WRAP(strlen(STR(FOO)));
}
