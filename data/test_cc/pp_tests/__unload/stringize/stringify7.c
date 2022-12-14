int printf(const char *, ...);
int puts(const char *);

#define STR(x) #x

static char *c[] = {
	STR(
		)
	STR( )
	STR(\n)
	STR($),
	STR(@ #),
	STR( 2.34  ),
	STR(while),
	STR(==),
	STR(#),
	STR("\\ \" \t"),
	STR(""),
	STR(' ')
};

static int foo(void) {
	int n = 0;
	n += puts(STR(   this   is some   string  ));
	n += puts(STR(this -= '8' 2u '\a' "i\ns\t"
		'\n' a "te\0st"));
	return n;
}

int main(void) {
	int i;
	long n = 0;
	for (i = 0; i < sizeof(c) / sizeof(*c); ++i) {
		n += puts(c[i]);
	}

	n += foo();
	n += '\302';
	n += '\x98';
	n += '\xA';
	return printf("%ld\n", n);
}
