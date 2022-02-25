package ast;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import ast.attributes.Attribute;
import ast.attributes.AttributeList;
import ast.attributes.gnuc.ParseAttributesGcc;
import ast.attributes.util.BalancedTokenlistParser;
import ast.main.ParserMain;
import ast.parse.Parse;
import jscan.parse.Tokenlist; 
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class Test_AttrGnuc1 {

  @Ignore
  @Test
  public void testBalancing1() throws IOException {

    List<String> balanced = new ArrayList<String>();
    balanced.add("  ()                 a ");
    balanced.add("  (())               b ");
    balanced.add("  (1)                c ");
    balanced.add("  (1,2,3)            d ");
    balanced.add("  (format(x,y,32))   e ");
    balanced.add("  ((format(x,y,32))) f ");
    balanced.add("  (([]))             g ");

    for (String s : balanced) {
      Tokenlist it = new ParserMain(new StringBuilder(s)).preprocess();
      Parse p = new Parse(it);

      System.out.println(s + ":");
      List<Token> result = new BalancedTokenlistParser(p).parse(T.T_LEFT_PAREN, T.T_RIGHT_PAREN);
      for (Token tok : result) {
        System.out.printf("%s ", tok.getValue());
      }
      System.out.println("tok=" + p.tok().getValue());
      System.out.println();
    }

  }

  @Ignore
  @Test
  public void testBalancing2() throws IOException {

    List<String> balanced = new ArrayList<String>();
    balanced.add("  { int a; int b; }                 a ");
    balanced.add("  { { int a; int b; if(1) {} } }    b ");

    for (String s : balanced) {
      Tokenlist it = new ParserMain(new StringBuilder(s)).preprocess();
      Parse p = new Parse(it);

      System.out.println(s + ":");
      List<Token> result = new BalancedTokenlistParser(p).parse(T.T_LEFT_BRACE, T.T_RIGHT_BRACE);
      for (Token tok : result) {
        System.out.printf("%s ", tok.getValue());
      }
      System.out.println("tok=" + p.tok().getValue());
      System.out.println();
    }

  }

  @Test
  public void testAttr1() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" __attribute__((__format__ (__scanf__, 2, 0)))         \n");
    sb.append(" __attribute__((always_inline))                        \n");
    sb.append(" __attribute__((always_inline))                        \n");
    sb.append(" __attribute__((always_inline))                        \n");
    sb.append(" __attribute__((availability(macosx,introduced=10.7))) \n");
    sb.append(" inline int multiple() { return 2; }                   \n");
    //@formatter:on

    Tokenlist it = new ParserMain(sb).preprocess();
    Parse p = new Parse(it);

    AttributeList list = new ParseAttributesGcc(p).parse();

    assertEquals("inline", p.tok().getValue());

    boolean print = false;
    if (print) {
      for (Attribute attr : list.getAttributes()) {
        for (Token tok : attr.getTokens()) {
          System.out.printf("%s ", tok.getValue());
        }
        System.out.println();
      }
      System.out.println(p.tok().getValue());
    }

  }

}
