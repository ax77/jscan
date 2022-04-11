//#include <stdio.h>

int main()
{
#define A 1
#define B A+1
#define C A+B+1
#define D A+B+C+1
#define E A+B+C+D+1
#define F A+B+C+D+E+1
#define G A+B+C+D+E+F+1
#define H A+B+C+D+E+F+G+1
#define I A+B+C+D+E+F+G+H+1
#define J A+B+C+D+E+F+G+H+I+1
#define K A+B+C+D+E+F+G+H+I+J+1
#define L A+B+C+D+E+F+G+H+I+J+K+1
#define M A+B+C+D+E+F+G+H+I+J+K+L+1
#define N A+B+C+D+E+F+G+H+I+J+K+L+M+1
#define O A+B+C+D+E+F+G+H+I+J+K+L+M+N+1
#define P A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+1
#define Q A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+1
#define R A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+1
#define S A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+1
/*
#define T A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+S+1
#define U A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+S+T+1
#define V A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+S+T+U+1
#define W A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+S+T+U+V+1
#define X A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+S+T+U+V+W+1
#define Y A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+S+T+U+V+W+X+1
#define Z A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+S+T+U+V+W+X+Y+1
*/


  printf("A: %lu\n", A);
  printf("B: %lu\n", B);
  printf("C: %lu\n", C);
  printf("D: %lu\n", D);
  printf("E: %lu\n", E);
  printf("F: %lu\n", F);
  printf("G: %lu\n", G);
  printf("H: %lu\n", H);
  printf("I: %lu\n", I);
  printf("J: %lu\n", J);
  printf("K: %lu\n", K);
  printf("L: %lu\n", L);
  printf("M: %lu\n", M);
  printf("N: %lu\n", N);
  printf("O: %lu\n", O);
  printf("P: %lu\n", P);
  printf("Q: %lu\n", Q);
  printf("R: %lu\n", R);
  printf("S: %lu\n", S);
  /*
  printf("T: %lu\n", T);
  printf("U: %lu\n", U);
  printf("V: %lu\n", V);
  printf("W: %lu\n", W);
  printf("X: %lu\n", X);
  printf("Y: %lu\n", Y);
  printf("Z: %lu\n", Z);
  */

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






