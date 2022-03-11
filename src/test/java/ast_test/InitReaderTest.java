package ast_test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import ast.builders.TypeMerger;
import ast.parse.Parse;
import ast.parse.ParseBaseType;
import ast.parse.ParseDeclarator;
import ast.parse.ParseInitializer;
import ast.tree.Declarator;
import ast.tree.Initializer;
import ast.types.CType;
import jscan.preproc.preprocess.Scan;
import jscan.symtab.KeywordsInits;
import jscan.symtab.ScopeLevels;
import jscan.tokenize.Stream;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class InitReaderTest {

  @Test
  public void testStructs() throws IOException {

    List<String> list = new ArrayList<>();
    list.add(" struct x { int a; int b[2][3]; } var = { .a = 32, .b[0][0] = 1, .b = { 11,22,33,44,55,66, }, }; \n");

    KeywordsInits.initIdents();

    for (String str : list) {

      List<Token> tokens = new Stream("utest", str).getTokenlist();
      List<Token> pp = new ArrayList<Token>();
      Scan s = new Scan(tokens);
      for (;;) {
        Token tok = s.get();
        if (tok.is(T.TOKEN_EOF)) {
          pp.add(tok);
          break;
        }
        if (tok.typeIsSpecialStreamMarks()) {
          continue;
        }
        pp.add(tok);
      }

      Parse parser = new Parse(pp);
      parser.pushscope(ScopeLevels.FILE_SCOPE);

      CType base = new ParseBaseType(parser).parse();
      Declarator decl = new ParseDeclarator(parser).parse();
      CType type = TypeMerger.build(base, decl);

      parser.checkedMove(T.T_ASSIGN);

      List<Initializer> inits = new ParseInitializer(parser).parse(type);
      System.out.println();
      System.out.println(decl.getName() + " " + type.size);
      for (Initializer init : inits) {
        System.out.println(init);
      }

      parser.semicolon();

    }
  }

  @Ignore
  @Test
  public void testArrays() throws IOException {

    List<String> list = new ArrayList<>();
    list.add(" int a1[5] = {1,2,3,4,5};                                              \n");
    list.add(" int a2[ ] = {1, 1, 1, 1, 1}; //5                                          \n");
    list.add(" int a3[5] = { };                                                          \n");
    list.add(" int a4[5] = { 0 };                                                        \n");
    list.add(" short q1[4][3][2] = { { { } } };                                          \n");
    list.add(" short q2[4][3][2] = { { 1 }, { 2, 3 }, { 4, 5, 6 } };                     \n");
    list.add(" short q3[4][3][2] = {1, 0, 0, 0, 0, 0, 2, 3, 0, 0, 0, 0, 4, 5, 6};        \n");
    list.add(" short q4[4][3][2] = { { { 1 }, }, { { 2, 3 }, }, { { 4, 5 }, { 6 }, } };  \n");
    list.add(" int c0[3][4] = {0,1,2,3,4,5,6,7,8,9,10,11};                               \n");
    list.add(" int c1[2][3] = {{1, 3, 0}, {-1, 5, 9}};                                   \n");
    list.add(" int c2[][3] = {{1, 3, 0}, {-1, 5, 9}}; //2                                \n");
    list.add(" int c3[2][3] = {1, 3, 0, -1, 5, 9};                                       \n");
    list.add(" int arr1[][2][2] = { {{1,2},3,4},{5},6 }; //3                             \n");
    list.add(" int arr2[][2] = { {1}, {2}, 3,4,5,6,7 }; //5                              \n");
    list.add(" int arr3[][2][3] = { {{1}}, {{2, 3}}, 4,5,6,7, }; //3                     \n");
    list.add(" int arr_00[1][2][3] = { {{1,2,3} ,  {4,5,6}} };                           \n");
    list.add(" int arr_01[1][2][3] = {  {1,2,3  ,   4,5,6}  };                           \n");
    list.add(" int arr_02[1][2][3] = {   1,2,3  ,   4,5,6   };                           \n");
    list.add(" int arr_03[1][2][3] = { {{1    } ,  {4,5,6}} };                           \n");
    list.add(" int arr_04[1][2][3] = {  };                                               \n");
    list.add(" int arr_05[1][2][3] = { 1 };                                              \n");
    list.add(" int arr_06[1][2][3] = { {1,2,3 ,  {4,5,6}} };                             \n");
    list.add(" int arr_10[2][2][2] = { {{1,2},3,4},{5} };                                \n");
    list.add(" int arr_11[2][2][2] = { {{1,2},{3,4}},{{5,6},{7,8}}, };                    \n");
    list.add(" int arr_12[2][2][2] = { {{1},{3}},{{5},{7}} };                            \n");
    list.add(" int arr_14[3] = { 0, [0]=1, 1, [1]=2, [0]=1, [1]=2, 3, };                  \n");

    KeywordsInits.initIdents();

    for (String str : list) {

      List<Token> tokens = new Stream("utest", str).getTokenlist();
      List<Token> pp = new ArrayList<Token>();
      Scan s = new Scan(tokens);
      for (;;) {
        Token tok = s.get();
        if (tok.is(T.TOKEN_EOF)) {
          pp.add(tok);
          break;
        }
        if (tok.typeIsSpecialStreamMarks()) {
          continue;
        }
        pp.add(tok);
      }

      Parse parser = new Parse(pp);
      parser.pushscope(ScopeLevels.FILE_SCOPE);

      CType base = new ParseBaseType(parser).parse();
      Declarator decl = new ParseDeclarator(parser).parse();
      CType type = TypeMerger.build(base, decl);

      parser.checkedMove(T.T_ASSIGN);

      List<Initializer> inits = new ParseInitializer(parser).parse(type);
      System.out.println();
      System.out.println(decl.getName() + " " + type.size);
      for (Initializer init : inits) {
        System.out.println(init);
      }

      parser.semicolon();

    }
  }

}
