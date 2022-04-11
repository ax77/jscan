/* n_37.t:  Translation limits. */

/* 37.1:    Number of parameters in macro: at least 31. */
#define glue31(a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E)   \
    a##b##c##d##e##f##g##h##i##j##k##l##m##n##o##p##q##r##s##t##u##v##w##x##y##z##A##B##C##D##E

/* 37.2:    Number of arguments of macro call: at least 31. */
/*  ABCDEFGHIJKLMNOPQRSTUVWXYZabcde;    */
    glue31( A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R
            , S, T, U, V, W, X, Y, Z, a, b, c, d, e);

/* 37.3:    Significant initial characters in an internal identifier or a
        macro name: at least 31.  */
/*  ABCDEFGHIJKLMNOPQRSTUVWXYZabcd_;    */
    ABCDEFGHIJKLMNOPQRSTUVWXYZabcd_;

