#define argc(a) a
short arg = argc(
#define m32 32
#define m64 64
#if defined (m32)
  m32
#else
  m64
#endif
);
