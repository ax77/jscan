package preproc;

import java.util.List;

import tokenize.Token;

public class TokenPrint {

  public static String toStrLine(List<Token> list) {
    StringBuilder sb = new StringBuilder();
    for (Token t : list) {
      if (t.hasLeadingWhitespace()) {
        sb.append(" ");
      }
      sb.append(t.getValue());
      if (t.isNewLine()) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }

}
