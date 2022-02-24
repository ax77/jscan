package parse;

import java.util.ArrayList;
import java.util.List;

import tokenize.T;
import tokenize.Token;

public class RingBuf {

  private static int level = 0;

  public static String ringBufferToStringLines(List<Token> ringBuffer) {

    level = 0;

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

        if (t.ofType(T.T_RIGHT_BRACE)) {
          --level;
        }

        if (first) {
          tmp.append(String.format("%-7d|", t.getLine()));
          tmp.append(pad());
          first = false;
        }
        if (t.hasLeadingWhitespace()) {
          tmp.append(" ");
        }
        tmp.append(t.getValue());

        if (t.ofType(T.T_LEFT_BRACE)) {
          level++;
        }
      }
      tmp.append("\n");
      sb.append(tmp);
    }

    return sb.toString();
  }

  private static String pad() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < level; ++i) {
      sb.append("  ");
    }
    return sb.toString();
  }
}
