#define sum5(x1,x2,x3,x4,x5) (x1+x2+x3+x4+x5)
#define xxx(a,b,c) (a+b+c)
#define parm(x,y,z) xxx(x,y,z)
sum5(parm(1,2,3),parm(4,5,6),parm(7,8,9),parm(10,11,12),parm(13,14,15))
