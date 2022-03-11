package ast_test;

import java.io.IOException;

import org.junit.Test;

import ast.main.ParseMainNew;
import ast.main.ParseOpts;
import ast.tree.TranslationUnit;
import jscan.fio.IO;

public class Test_ParserMainNew {

  @Test
  public void testFile() throws IOException {
    String dir = IO.userDir();
    String filename = dir + "/cc_tests/01.c";
    ParseOpts opts[] = new ParseOpts[] {};
    TranslationUnit unit = new ParseMainNew(opts).parseFile(filename);
  }

  @Test
  public void testString() throws IOException {
    String text = "#define a 32\n int main() { return a; }";
    ParseOpts opts[] = new ParseOpts[] {};
    TranslationUnit unit = new ParseMainNew(opts).parseString(text);
  }

}
