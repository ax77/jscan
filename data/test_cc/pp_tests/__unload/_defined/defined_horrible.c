//1
#define stub (defined(a))
#define a
#if stub
  __LINE__
#else
  __LINE__
#endif
#undef stub
#undef a

//2
#define a
#define stub (defined(a))
#if stub
  __LINE__
#else
  __LINE__
#endif
#undef stub
#undef a

//3
#define stub (defined(a))
#if stub
  __LINE__
#else
  __LINE__
#endif
#define a


