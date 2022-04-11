package ast_test;

import java.io.IOException;

import org.junit.Test;

import ast.main.ParseMainNew;
import ast.main.ParseOpts;
import ast.tree.CSymbol;
import ast.tree.CSymbol.CSymFunction;
import ast.tree.Declaration;
import ast.tree.TranslationUnit;
import ast.tree.TranslationUnit.ExternalDeclaration;
import jscan.fio.FileReadKind;
import jscan.fio.FileWrapper;
import jscan.fio.IO;

public class Test_ParseTcc {

  private TranslationUnit parseUnit(StringBuilder sb) throws IOException {
    ParseOpts opts[] = new ParseOpts[] { ParseOpts.CONCAT_STRINGS, ParseOpts.PREPEND_PREDEFINED_BUFFER };
    TranslationUnit unit = new ParseMainNew(opts).parseString(sb.toString());
    return unit;
  }

  @Test
  public void parseTcc() throws IOException {
    String dir = IO.userDir();
    String fname = dir + "/cc_tests/tcc.c";

    TranslationUnit unit = parseUnit(new StringBuilder(new FileWrapper(fname).readToString(FileReadKind.AS_IS)));
    for (ExternalDeclaration d : unit.getExternalDeclarations()) {
      if (d.isDeclaration()) {
        Declaration decl = d.declaration;
        if (decl.isVarlist()) {
          for (CSymbol s : decl.variables) {
            //System.out.println("var::: " + s.getNameStr());
          }
        }
      } else {
        CSymFunction fn = d.func;
        if (!fn.gotos.isEmpty()) {
          //System.out.println();
        }
      }
    }
  }

}
