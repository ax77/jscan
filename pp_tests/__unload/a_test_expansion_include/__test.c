#define xstr(x)#x
#define str(x)xstr(x)
#define _(x)x

#ifdef FNAME
#  error "FNAME redefined"
#endif
#define FNAME str(_(h_00.h))
#include FNAME

#ifdef FNAME
#  error "FNAME redefined"
#endif
#define FNAME str(_(h_01.h))
#include FNAME

#ifdef FNAME
#  error "FNAME redefined"
#endif
#define FNAME str(_(h_02.h))
#include FNAME

#ifdef FNAME
#  error "FNAME redefined"
#endif
#define FNAME str(_(h_03.h))
#include FNAME

#ifdef FNAME
#  error "FNAME redefined"
#endif
#define FNAME str(_(h_04.h))
#include FNAME

#ifdef FNAME
#  error "FNAME redefined"
#endif
#define FNAME str(_(h_05.h))
#include FNAME

#ifdef FNAME
#  error "FNAME redefined"
#endif
#define FNAME str(_(h_06.h))
#include FNAME

#ifdef FNAME
#  error "FNAME redefined"
#endif
#define FNAME str(_(h_07.h))
#include FNAME

int main() { return 0; }
