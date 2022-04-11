#define BR ()
#define LP (
#define RP )
#define re() verse LP RP
#define verse() re LP RP
#define c() fn { re BR; verse LP RP; verse BR; re LP RP; }
#define x(x) x

re()
verse()

x(re())
x(verse())

x(c())
x(x(c()))
x(x(x(c())))
x(x(x(x(c()))))
x(x(x(x(x(c())))))
x(x(x(x(x(x(c()))))))
x(x(x(x(x(x(x(c())))))))
x(x(x(x(x(x(x(x(c()))))))))
