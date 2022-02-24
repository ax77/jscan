#define MACRO(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z) (A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z)

#define M00(A)A
#define M01(A,B)B
#define M02(A,B,C)C
#define M03(A,B,C,D)D
#define M04(A,B,C,D,E)E
#define M05(A,B,C,D,E,F)F
#define M06(A,B,C,D,E,F,G)G
#define M07(A,B,C,D,E,F,G,H)H
#define M08(A,B,C,D,E,F,G,H,I)I
#define M09(A,B,C,D,E,F,G,H,I,J)J
#define M10(A,B,C,D,E,F,G,H,I,J,K)K
#define M11(A,B,C,D,E,F,G,H,I,J,K,L)L
#define M12(A,B,C,D,E,F,G,H,I,J,K,L,M)M
#define M13(A,B,C,D,E,F,G,H,I,J,K,L,M,N)N
#define M14(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O)O
#define M15(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P)P
#define M16(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q)Q
#define M17(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R)R
#define M18(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S)S
#define M19(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T)T
#define M20(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U)U
#define M21(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V)V
#define M22(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W)W
#define M23(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X)X
#define M24(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y)Y
#define M25(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z)Z


#define SCAN(ARG)ARG
#define A_00(_) SCAN(_)
#define B_01(_) SCAN(_)
#define C_02(_) SCAN(_)
#define D_03(_) SCAN(_)
#define E_04(_) SCAN(_)
#define F_05(_) SCAN(_)
#define G_06(_) SCAN(_)
#define H_07(_) SCAN(_)
#define I_08(_) SCAN(_)
#define J_09(_) SCAN(_)
#define K_10(_) SCAN(_)
#define L_11(_) SCAN(_)
#define M_12(_) SCAN(_)
#define N_13(_) SCAN(_)
#define O_14(_) SCAN(_)
#define P_15(_) SCAN(_)
#define Q_16(_) SCAN(_)
#define R_17(_) SCAN(_)
#define S_18(_) SCAN(_)
#define T_19(_) SCAN(_)
#define U_20(_) SCAN(_)
#define V_21(_) SCAN(_)
#define W_22(_) SCAN(_)
#define X_23(_) SCAN(_)
#define Y_24(_) SCAN(_)
#define Z_25(_) SCAN(_)


MACRO(A,,,,,,,,,,,,,,,,,,,,,,,,,)
MACRO(,B,,,,,,,,,,,,,,,,,,,,,,,,)
MACRO(,,C,,,,,,,,,,,,,,,,,,,,,,,)
MACRO(,,,D,,,,,,,,,,,,,,,,,,,,,,)
MACRO(,,,,E,,,,,,,,,,,,,,,,,,,,,)
MACRO(,,,,,F,,,,,,,,,,,,,,,,,,,,)
MACRO(,,,,,,G,,,,,,,,,,,,,,,,,,,)
MACRO(,,,,,,,H,,,,,,,,,,,,,,,,,,)
MACRO(,,,,,,,,I,,,,,,,,,,,,,,,,,)
MACRO(,,,,,,,,,J,,,,,,,,,,,,,,,,)
MACRO(,,,,,,,,,,K,,,,,,,,,,,,,,,)
MACRO(,,,,,,,,,,,L,,,,,,,,,,,,,,)
MACRO(,,,,,,,,,,,,M,,,,,,,,,,,,,)
MACRO(,,,,,,,,,,,,,N,,,,,,,,,,,,)
MACRO(,,,,,,,,,,,,,,O,,,,,,,,,,,)
MACRO(,,,,,,,,,,,,,,,P,,,,,,,,,,)
MACRO(,,,,,,,,,,,,,,,,Q,,,,,,,,,)
MACRO(,,,,,,,,,,,,,,,,,R,,,,,,,,)
MACRO(,,,,,,,,,,,,,,,,,,S,,,,,,,)
MACRO(,,,,,,,,,,,,,,,,,,,T,,,,,,)
MACRO(,,,,,,,,,,,,,,,,,,,,U,,,,,)
MACRO(,,,,,,,,,,,,,,,,,,,,,V,,,,)
MACRO(,,,,,,,,,,,,,,,,,,,,,,W,,,)
MACRO(,,,,,,,,,,,,,,,,,,,,,,,X,,)
MACRO(,,,,,,,,,,,,,,,,,,,,,,,,Y,)
MACRO(,,,,,,,,,,,,,,,,,,,,,,,,,Z)


MACRO(M00(A),,,,,,,,,,,,,,,,,,,,,,,,,)
MACRO(,M01(,B),,,,,,,,,,,,,,,,,,,,,,,,)
MACRO(,,M02(,,C),,,,,,,,,,,,,,,,,,,,,,,)
MACRO(,,,M03(,,,D),,,,,,,,,,,,,,,,,,,,,,)
MACRO(,,,,M04(,,,,E),,,,,,,,,,,,,,,,,,,,,)
MACRO(,,,,,M05(,,,,,F),,,,,,,,,,,,,,,,,,,,)
MACRO(,,,,,,M06(,,,,,,G),,,,,,,,,,,,,,,,,,,)
MACRO(,,,,,,,M07(,,,,,,,H),,,,,,,,,,,,,,,,,,)
MACRO(,,,,,,,,M08(,,,,,,,,I),,,,,,,,,,,,,,,,,)
MACRO(,,,,,,,,,M09(,,,,,,,,,J),,,,,,,,,,,,,,,,)
MACRO(,,,,,,,,,,M10(,,,,,,,,,,K),,,,,,,,,,,,,,,)
MACRO(,,,,,,,,,,,M11(,,,,,,,,,,,L),,,,,,,,,,,,,,)
MACRO(,,,,,,,,,,,,M12(,,,,,,,,,,,,M),,,,,,,,,,,,,)
MACRO(,,,,,,,,,,,,,M13(,,,,,,,,,,,,,N),,,,,,,,,,,,)
MACRO(,,,,,,,,,,,,,,M14(,,,,,,,,,,,,,,O),,,,,,,,,,,)
MACRO(,,,,,,,,,,,,,,,M15(,,,,,,,,,,,,,,,P),,,,,,,,,,)
MACRO(,,,,,,,,,,,,,,,,M16(,,,,,,,,,,,,,,,,Q),,,,,,,,,)
MACRO(,,,,,,,,,,,,,,,,,M17(,,,,,,,,,,,,,,,,,R),,,,,,,,)
MACRO(,,,,,,,,,,,,,,,,,,M18(,,,,,,,,,,,,,,,,,,S),,,,,,,)
MACRO(,,,,,,,,,,,,,,,,,,,M19(,,,,,,,,,,,,,,,,,,,T),,,,,,)
MACRO(,,,,,,,,,,,,,,,,,,,,M20(,,,,,,,,,,,,,,,,,,,,U),,,,,)
MACRO(,,,,,,,,,,,,,,,,,,,,,M21(,,,,,,,,,,,,,,,,,,,,,V),,,,)
MACRO(,,,,,,,,,,,,,,,,,,,,,,M22(,,,,,,,,,,,,,,,,,,,,,,W),,,)
MACRO(,,,,,,,,,,,,,,,,,,,,,,,M23(,,,,,,,,,,,,,,,,,,,,,,,X),,)
MACRO(,,,,,,,,,,,,,,,,,,,,,,,,M24(,,,,,,,,,,,,,,,,,,,,,,,,Y),)
MACRO(,,,,,,,,,,,,,,,,,,,,,,,,,M25(,,,,,,,,,,,,,,,,,,,,,,,,,Z))


MACRO(SCAN(A_00(A)),,,,,,,,,,,,,,,,,,,,,,,,,)
MACRO(,SCAN(B_01(B)),,,,,,,,,,,,,,,,,,,,,,,,)
MACRO(,,SCAN(C_02(C)),,,,,,,,,,,,,,,,,,,,,,,)
MACRO(,,,SCAN(D_03(D)),,,,,,,,,,,,,,,,,,,,,,)
MACRO(,,,,SCAN(E_04(E)),,,,,,,,,,,,,,,,,,,,,)
MACRO(,,,,,SCAN(F_05(F)),,,,,,,,,,,,,,,,,,,,)
MACRO(,,,,,,SCAN(G_06(G)),,,,,,,,,,,,,,,,,,,)
MACRO(,,,,,,,SCAN(H_07(H)),,,,,,,,,,,,,,,,,,)
MACRO(,,,,,,,,SCAN(I_08(I)),,,,,,,,,,,,,,,,,)
MACRO(,,,,,,,,,SCAN(J_09(J)),,,,,,,,,,,,,,,,)
MACRO(,,,,,,,,,,SCAN(K_10(K)),,,,,,,,,,,,,,,)
MACRO(,,,,,,,,,,,SCAN(L_11(L)),,,,,,,,,,,,,,)
MACRO(,,,,,,,,,,,,SCAN(M_12(M)),,,,,,,,,,,,,)
MACRO(,,,,,,,,,,,,,SCAN(N_13(N)),,,,,,,,,,,,)
MACRO(,,,,,,,,,,,,,,SCAN(O_14(O)),,,,,,,,,,,)
MACRO(,,,,,,,,,,,,,,,SCAN(P_15(P)),,,,,,,,,,)
MACRO(,,,,,,,,,,,,,,,,SCAN(Q_16(Q)),,,,,,,,,)
MACRO(,,,,,,,,,,,,,,,,,SCAN(R_17(R)),,,,,,,,)
MACRO(,,,,,,,,,,,,,,,,,,SCAN(S_18(S)),,,,,,,)
MACRO(,,,,,,,,,,,,,,,,,,,SCAN(T_19(T)),,,,,,)
MACRO(,,,,,,,,,,,,,,,,,,,,SCAN(U_20(U)),,,,,)
MACRO(,,,,,,,,,,,,,,,,,,,,,SCAN(V_21(V)),,,,)
MACRO(,,,,,,,,,,,,,,,,,,,,,,SCAN(W_22(W)),,,)
MACRO(,,,,,,,,,,,,,,,,,,,,,,,SCAN(X_23(X)),,)
MACRO(,,,,,,,,,,,,,,,,,,,,,,,,SCAN(Y_24(Y)),)
MACRO(,,,,,,,,,,,,,,,,,,,,,,,,,SCAN(Z_25(Z)))


