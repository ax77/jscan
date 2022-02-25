package jscan.main;

import jscan.tokenize.Token;

public class Env {

  public static final char HC_FEOF = '\0';
  public static final Token EOF_TOKEN_ENTRY = new Token(true);

  public static boolean isLetter(int c) {
    return c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f' || c == 'g' || c == 'h' || c == 'i'
        || c == 'j' || c == 'k' || c == 'l' || c == 'm' || c == 'n' || c == 'o' || c == 'p' || c == 'q' || c == 'r'
        || c == 's' || c == 't' || c == 'u' || c == 'v' || c == 'w' || c == 'x' || c == 'y' || c == 'z' || c == 'A'
        || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F' || c == 'G' || c == 'H' || c == 'I' || c == 'J'
        || c == 'K' || c == 'L' || c == 'M' || c == 'N' || c == 'O' || c == 'P' || c == 'Q' || c == 'R' || c == 'S'
        || c == 'T' || c == 'U' || c == 'V' || c == 'W' || c == 'X' || c == 'Y' || c == 'Z' || c == '_';
  }

  public static boolean isDec(int c) {
    return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8'
        || c == '9';
  }

  public static boolean isHex(int c) {
    return isDec(c) || (c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F' || c == 'a' || c == 'b'
        || c == 'c' || c == 'd' || c == 'e' || c == 'f');
  }

  public static boolean isOct(int c) {
    return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7';
  }

  public static boolean isBin(int c) {
    return c == '0' || c == '1';
  }

  public static boolean isOpStart(int c) {
    return c == '>' || c == '<' || c == '-' || c == '|' || c == '+' || c == '&' || c == '#' || c == '^' || c == '='
        || c == '%' || c == '/' || c == '!' || c == '*' || c == '.' || c == '~' || c == '}' || c == '{' || c == ')'
        || c == '(' || c == ']' || c == '?' || c == ':' || c == ';' || c == ',' || c == '[';
  }

  public static long hexValue(String s) {
    char buffer[] = s.toCharArray();
    long value = 0;
    for (int c : buffer) {
      if (c >= '0' && c <= '9') {
        c -= '0';
      } else if (c >= 'a' && c <= 'f') {
        c = c - 'a' + 10;
      } else if (c >= 'A' && c <= 'F') {
        c = c - 'A' + 10;
      } else {
        break;
      }
      value = value * 16 + c;
    }
    return value;
  }

  public static long octValue(String s) {
    char buffer[] = s.toCharArray();
    long value = 0;
    for (int c : buffer) {
      if (c >= '0' && c <= '7') {
        c -= '0';
      } else {
        break;
      }
      value = value * 8 + c;
    }
    return value;
  }

}
