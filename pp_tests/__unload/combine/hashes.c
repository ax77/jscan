/* WS after sharp...  */
# //////////////////////                       
# //////////////////////                       
# //////////////////////                       
# //////////////////////   
/* WS after sharp...  */
                    
#define sum(x, y) (x + y)

int f() { return 0; }
int g() { return 1; }
int q() { return 0; }
int main()
{
 int z = f(), y = f(), c = sum
  ( 
# /**/ define p1 32
# /**/ define p2 64
    p1,p2 
  ), 
  d = f(), 
  e = f() + sum
  (
#   undef  p1
#   undef  p2
#   define p1 128
#   define p2 256
    p1,
    p2
  ) + g(/* NOTHING */), h, i, j, k, l, m, n, o,
  #undef  sum
  #define sum(x, y, z) \
             (x+ y+ z)
  #define EMPTY_PARM
  #define A # define X 1
  p = sum
    ( 
      g(EMPTY_PARM),
      f(EMPTY_PARM),
      f(EMPTY_PARM) 
    ), 
  r = (q()+
      sum
      (0,0,0));
  return 0;
}
#
#
#
#
#
