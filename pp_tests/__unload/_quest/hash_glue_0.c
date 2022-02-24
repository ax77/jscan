#define str(x) #x
#define xstr(x) str(x)
#define glue(x, y) x ## y
#define xglue(x, y) glue (x, y)
const char * hh = xstr(xglue(glue(%, :), glue(%, :)));
