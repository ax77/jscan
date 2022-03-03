package _st3_linearize_expr.items;

import java.util.List;

public class ArgsListToString {

  public static <E> String paramsToStringWithBraces(List<E> args, char open) {
    StringBuilder sb = new StringBuilder();
    sb.append(open);

    for (int i = 0; i < args.size(); i++) {
      E param = args.get(i);
      sb.append(param.toString());

      if (i + 1 < args.size()) {
        sb.append(", ");
      }
    }

    // method parameters, generic parameters
    sb.append(open == '(' ? ")" : ">");
    return sb.toString();
  }

}
