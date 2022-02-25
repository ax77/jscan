package ast.parse;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.Token;

public class RingBuf {

  public static String ringBufferToStringLines(List<Token> ringBuffer) {

    List<List<Token>> lines = new ArrayList<List<Token>>(0);
    List<Token> line = new ArrayList<Token>(0);

    for (Token t : ringBuffer) {
      if (t.getLocation() != null && t.getLocation().getLine() == 0) {
        continue; // built-in
      }
      line.add(t);
      if (t.isNewLine()) {
        lines.add(line);
        line = new ArrayList<Token>(0);
      }
    }
    if (!line.isEmpty()) {
      lines.add(line);
      line = new ArrayList<Token>(0);
    }

    StringBuilder sb = new StringBuilder();

    for (List<Token> oneline : lines) {
      StringBuilder tmp = new StringBuilder();
      boolean first = true;
      for (Token t : oneline) {
        if (first) {
          tmp.append(String.format("%-5d|", t.getRow()));
          first = false;
        }
        if (t.hasLeadingWhitespace()) {
          tmp.append(" ");
        }
        tmp.append(t.getValue());
      }
      tmp.append("\n");
      sb.append(tmp);
    }

    return sb.toString();
  }
}
