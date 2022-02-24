// RUN: %clang_cc1 -E %s | grep '^a: x$'
// RUN: %clang_cc1 -E %s | grep '^b: x y, z,h$'
// RUN: %clang_cc1 -E %s | grep '^c: foo(x)$'

#define A(b, c...) b c
a: A(x)
b: A(x, y, z,h)

#define B(b, c...) foo(b, ## c)
c: B(x)

#define eprintf(format, args...)  \
      fprintf (stderr, format , ## args)
      
// fprintf (stderr, "%s:%d: " , input_file_name, line_number)
eprintf ("%s:%d: ", input_file_name, line_number)




#  define dbprintf_gnuc(level, args...) do { \
        if((level) <= DEBUG_LEVEL)      \
            fprintf(stderr, args);      \
    } while(0)

#  define dbprintf_stdc(level, ...) do {         \
        if((level) <= DEBUG_LEVEL)          \
            fprintf(stderr, __VA_ARGS__);   \
    } while(0)



dbprintf_gnuc(2, " s=%d", dcode25->s10);
dbprintf_stdc(2, " s=%d", dcode25->s10);
