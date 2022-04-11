package ast_test;

import java.io.IOException;

import org.junit.Test;

import cc.main.ParseMainNew;
import cc.main.ParseOpts;
import cc.tree.CSymbol;
import cc.tree.Declaration;
import cc.tree.TranslationUnit;
import cc.tree.CSymbol.CSymFunction;
import cc.tree.TranslationUnit.ExternalDeclaration;
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
    String fname = dir + "/data/test_cc/cc_tests/tcc.c";

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
