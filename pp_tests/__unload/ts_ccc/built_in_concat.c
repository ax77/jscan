//test return 65
//#include <stdio.h>

#define STRIFY(foo) #foo
#define STR "foo"

int __test() {
    //int len1 = printf("%d %s\n", __LINE__, __FILE__ __DATE__ __TIME__);
    int len2 = printf("%s\n", "hello"STR STRIFY(bob));
    return len1 + len2 + EOF;
}
