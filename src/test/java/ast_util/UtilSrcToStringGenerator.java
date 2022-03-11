package ast_util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import jscan.fio.IO;

public class UtilSrcToStringGenerator {

  private String pad(String s, int c) {
    String pad = "";
    for (int i = 0; i < c - s.length(); ++i) {
      pad += " ";
    }
    return pad;
  }

  private String lineno(int n) {
    return " /*" + String.format("%03d", n) + "*/  ";
  }

  private String esc(String what) {
    char q2 = '\"';
    char b = '\\';
    char q1 = '\'';
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < what.length(); i++) {
      char c = what.charAt(i);
      if (c == q2 || c == q1 || c == b) {
        sb.append(b);
        sb.append(c);
        continue;
      }
      if (c == '\t') {
        sb.append("  ");
        continue;
      }
      sb.append(c);
    }
    return sb.toString();
  }

  @Ignore
  @Test
  public void tostr() throws IOException {
    final String dir = System.getProperty("user.dir");
    final String fnameinput = dir + "/__test_src.txt";
    final String fnameout = dir + "/__test_src_out.txt";

    String q = "\"";
    String n = "\\n";
    String b = "\\";
    int mlen = 0;
    List<String> lines = IO.readf(fnameinput);
    for (String line : lines) {
      if (mlen < line.length()) {
        mlen = line.length();
      }
    }

    FileWriter fw = new FileWriter(fnameout);
    fw.write("    //@formatter:off\n");
    fw.write("    StringBuilder sb = new StringBuilder();\n");
    int cnt = 1;
    for (String line : lines) {
      fw.write("    sb.append(" + q + lineno(cnt++) + line + pad(line, mlen + 3) + n + q + ");\n");
    }
    fw.write("    //@formatter:on\n");
    fw.close();
  }

}
