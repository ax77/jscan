
int main()
{
#define A(X) 1
#define B(X) A(1)+1
#define C(X) A(1)+B(1)+1
#define D(X) A(1)+B(1)+C(1)+1
#define E(X) A(1)+B(1)+C(1)+D(1)+1
#define F(X) A(1)+B(1)+C(1)+D(1)+E(1)+1
#define G(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+1
#define H(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+1
#define I(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+1
#define J(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+1
#define K(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+1
#define L(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+1
#define M(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+1
#define N(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+1
#define O(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+N(1)+1
#define P(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+N(1)+O(1)+1
#define Q(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+N(1)+O(1)+P(1)+1
#define R(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+N(1)+O(1)+P(1)+Q(1)+1
#define S(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+N(1)+O(1)+P(1)+Q(1)+R(1)+1
//#define T(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+N(1)+O(1)+P(1)+Q(1)+R(1)+S(1)+1
//#define U(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+N(1)+O(1)+P(1)+Q(1)+R(1)+S(1)+T(1)+1
//#define V(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+N(1)+O(1)+P(1)+Q(1)+R(1)+S(1)+T(1)+U(1)+1
//#define W(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+N(1)+O(1)+P(1)+Q(1)+R(1)+S(1)+T(1)+U(1)+V(1)+1
//#define X(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+N(1)+O(1)+P(1)+Q(1)+R(1)+S(1)+T(1)+U(1)+V(1)+W(1)+1
//#define Y(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+N(1)+O(1)+P(1)+Q(1)+R(1)+S(1)+T(1)+U(1)+V(1)+W(1)+X(1)+1
//#define Z(X) A(1)+B(1)+C(1)+D(1)+E(1)+F(1)+G(1)+H(1)+I(1)+J(1)+K(1)+L(1)+M(1)+N(1)+O(1)+P(1)+Q(1)+R(1)+S(1)+T(1)+U(1)+V(1)+W(1)+X(1)+Y(1)+1


  printf("A: %lu\n", A());
  printf("B: %lu\n", B());
  printf("C: %lu\n", C());
  printf("D: %lu\n", D());
  printf("E: %lu\n", E());
  printf("F: %lu\n", F());
  printf("G: %lu\n", G());
  printf("H: %lu\n", H());
  printf("I: %lu\n", I());
  printf("J: %lu\n", J());
  printf("K: %lu\n", K());
  printf("L: %lu\n", L());
  printf("M: %lu\n", M());
  printf("N: %lu\n", N());
  printf("O: %lu\n", O());
  printf("P: %lu\n", P());
  printf("Q: %lu\n", Q());
  printf("R: %lu\n", R());
  printf("S: %lu\n", S());
  //printf("T: %lu\n", T());
  //printf("U: %lu\n", U());
  //printf("V: %lu\n", V());
  //printf("W: %lu\n", W());
  //printf("X: %lu\n", X());
  //printf("Y: %lu\n", Y());
  //printf("Z: %lu\n", Z());




  return 0;
}

// A: 1
// B: 2
// C: 4
// D: 8
// E: 16
// F: 32
// G: 64
// H: 128
// I: 256
// J: 512
// K: 1024
// L: 2048
// M: 4096
// N: 8192
// O: 16384
// P: 32768
// Q: 65536
// R: 131072
// S: 262144

