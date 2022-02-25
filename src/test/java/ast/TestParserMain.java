package ast;

import java.io.IOException;

import org.junit.Test;

import ast.main.ParserMain;
import ast.unit.ExternalDeclaration;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist; 
import jscan.tokenize.Token;

public class TestParserMain {

  private final StringBuilder source = new StringBuilder("int main() { return 0; }");

  @Test
  public void testPreprocess() throws IOException {
    Tokenlist list = new ParserMain(source).preprocess();

    for (Token t : list.getList()) {
      // System.out.println(t.getValue());
    }
  }

  @Test
  public void testParse() throws IOException {
    TranslationUnit unit = new ParserMain(source).parseUnit();
    for (ExternalDeclaration ed : unit.getExternalDeclarations()) {
      // System.out.println(ed.isFunctionDefinition());
    }
  }

}
