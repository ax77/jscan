int printf(const char *, ...);

/* Hello!
// */

char *str = "a\"/*b\"\\\\";
char c = '"', d = '\'';

/*\
foo();
*/
/\
* bar(); *\
/

int foo/*	*/(int/**/a, int b) {
	int f = a/*\*//b;
	int g = a/ /**/b;
  return 0;
}

