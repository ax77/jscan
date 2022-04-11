#define a 32
#if (a + 1) > 10 && defined a
	a
#else
	b
#endif

#define b 64
#if !defined ( b )
	z
#else
	b
#endif

#define OPSYS  1
#define OS_CMS 2
#define OS_MVS 3
#define OS_DOS 4
#define OS_OS2 5

#if OPSYS == OS_CMS
    fn_syntax = "filename filetype fm";
#elif OPSYS == OS_MVS
    fn_syntax = "'userid.library.type(membername)'";
#elif OPSYS == OS_DOS  ||  OPSYS == OS_OS2
    fn_syntax = "filename.ext";
#else
    fn_syntax = "filename";
#endif

#if 1 != 1
	#error "why?"
#else
	ok
#endif

