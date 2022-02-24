#define log1(fmt, ...) log0(fmt, ##__VA_ARGS__)
#define log2(fmt, ...) log1(fmt, ##__VA_ARGS__)
#define log3(fmt, ...) log2(fmt, ##__VA_ARGS__)
log3("%p: %u", this, (unsigned)thing);
