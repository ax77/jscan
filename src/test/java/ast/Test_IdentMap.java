package ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import ast.main.ParserMain;
import ast.parse.Pcheckers;
import ast.symtab.IdentMap;
import jscan.parse.Tokenlist; 
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class Test_IdentMap {

  @Test
  public void testIdents() throws IOException {
    String source = "int int int int int int int";
    Tokenlist it = new ParserMain(new StringBuilder(source)).preprocess();
    for (;;) {
      Token t = it.next();
      if (t.ofType(T.TOKEN_STREAMBEGIN) || t.ofType(T.TOKEN_STREAMEND)) {
        continue;
      }
      if (t.ofType(T.TOKEN_EOF)) {
        break;
      }
      assertEquals(IdentMap.int_ident, t.getIdent());
      assertTrue(t.isIdent(IdentMap.int_ident));
      assertTrue(Pcheckers.isTypeSpec(t));
    }
  }

}
