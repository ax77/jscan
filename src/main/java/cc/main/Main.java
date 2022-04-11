package cc.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.tree.TranslationUnit;
import jscan.fio.IO;
import jscan.preproc.TokenPrint;
import jscan.tokenize.Token;

public class Main {

  private static TranslationUnit parseUnit(StringBuilder sb) throws IOException {
    // ParseOpts.CONCAT_STRINGS, ParseOpts.PREPEND_PREDEFINED_BUFFER
    ParseOpts opts[] = new ParseOpts[] {};
    TranslationUnit unit = new ParseMainNew(opts).parseString(sb.toString());
    return unit;
  }

  public static void main(String[] args) throws IOException {
    List<String> opts = new ArrayList<>();
    for (String s : args) {
      opts.add(s);
    }

    boolean oPreproc = false;
    boolean oCompile = false;
    List<String> files = new ArrayList<>();

    while (!opts.isEmpty()) {
      String o = opts.remove(0);
      if (o.equals("-E")) {
        oPreproc = true;
      } else {
        files.add(o);
      }
    }

    if (oPreproc) {
      String dir = IO.userDir();
      for (String fname : files) {
        ParseOpts parseopts[] = {};
        ParseMainNew p = new ParseMainNew(parseopts);
        List<Token> tokens = p.preprocessFile(dir + "/" + fname);
        System.out.println(TokenPrint.toStrLine(tokens));
      }
    }
  }

}
