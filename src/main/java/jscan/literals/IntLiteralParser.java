package jscan.literals;

import static jscan.main.Env.isBin;
import static jscan.main.Env.isDec;
import static jscan.main.Env.isHex;
import static jscan.main.Env.isLetter;
import static jscan.main.Env.isOct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jscan.utils.NullChecker;

public abstract class IntLiteralParser {

  private static void cutForBase(LinkedList<Character> buffer, StringBuilder out, int base) {

    final boolean baseIsCorrect = base == 2 || base == 8 || base == 10 || base == 16;
    if (!baseIsCorrect) {
      throw new RuntimeException("it is not a correct base: " + base);
    }

    for (; !buffer.isEmpty();) {
      final Character peek = buffer.peekFirst();
      final boolean needBreak = (base == 2 && !isBin(peek)) || (base == 8 && !isOct(peek))
          || (base == 10 && !isDec(peek)) || (base == 16 && !isHex(peek));
      if (needBreak) {
        break;
      }

      char c = buffer.removeFirst();
      out.append(c);
    }
  }

  private static char cutMntExp(LinkedList<Character> buffer, StringBuilder mnt, StringBuilder exp, int mntBase) {

    final boolean baseIsCorrect = mntBase == 10 || mntBase == 16;
    if (!baseIsCorrect) {
      throw new RuntimeException("it is not a correct base for a mantissa: " + mntBase);
    }

    char exp_sign = '+';
    if (!buffer.isEmpty() && buffer.peekFirst() == '.') {
      buffer.removeFirst();
      cutForBase(buffer, mnt, mntBase);
    }

    if (!buffer.isEmpty()) {

      final boolean isHexExp = buffer.peekFirst() == 'p' || buffer.peekFirst() == 'P';
      final boolean isDecExp = buffer.peekFirst() == 'e' || buffer.peekFirst() == 'E';

      if (isHexExp && mntBase != 16) {
        throw new RuntimeException("a hex exponent part in a floating constant.");
      }

      if (isHexExp || isDecExp) {
        buffer.removeFirst();
        if (buffer.peekFirst() == '-' || buffer.peekFirst() == '+') {
          exp_sign = buffer.removeFirst();
        }
        cutForBase(buffer, exp, 10);
      }
    }
    return exp_sign;
  }

  public static IntLiteral parse(String input) {

    if (input == null || input.trim().length() == 0) {
      throw new RuntimeException("An empty input.");
    }

    // slight underscores support
    // we do not check whether the underscore is between two digits or it isn't.
    // we may just ignore them all.
    //
    final String originalInput = new String(input);
    input = input.replaceAll("_", "");

    final LinkedList<Character> buffer = new LinkedList<Character>();
    for (char c : input.toCharArray()) {
      // allow all letters, digits, dot, plus, minus,
      final boolean charIsOk = isLetter(c) || isDec(c) || c == '-' || c == '+' || c == '.';
      if (!charIsOk) {
        throw new RuntimeException("not a number: " + input);
      }

      buffer.add(c);
    }

    char main_sign = '+';
    if (buffer.peekFirst() == '-' || buffer.peekFirst() == '+') {
      main_sign = buffer.removeFirst();
    }

    StringBuilder dec = new StringBuilder();
    StringBuilder mnt = new StringBuilder();
    StringBuilder exp = new StringBuilder();
    StringBuilder suf = new StringBuilder();
    char exp_sign = '+';

    boolean isBin = false;
    boolean isOct = false;
    boolean isHex = false;

    if (buffer.size() > 2) {
      char c1 = buffer.get(0);
      char c2 = buffer.get(1);
      if (c1 == '0' && (c2 == 'b' || c2 == 'B')) {
        isBin = true;
      }
      if ((c1 == '0' && (c2 == 'o' || c2 == 'O')) || (c1 == '0' && isOct(c2))) {
        isOct = true;
      }
      if (c1 == '0' && (c2 == 'x' || c2 == 'X')) {
        isHex = true;
      }
    }

    if (isBin || isOct || isHex) {
      if (isOct) {
        buffer.removeFirst();
        if (buffer.peekFirst() == 'o' || buffer.peekFirst() == 'O') {
          buffer.removeFirst();
        }
      } else {
        buffer.removeFirst();
        buffer.removeFirst();
      }

      // we'he checked that the buffer.size() > 2,
      // and now we know, that there's something here.

      if (isBin) {
        cutForBase(buffer, dec, 2);
      }

      if (isOct) {
        cutForBase(buffer, dec, 8);
      }

      if (isHex) {
        cutForBase(buffer, dec, 16);
        exp_sign = cutMntExp(buffer, mnt, exp, 16);
      }

    }

    else {

      if (buffer.peekFirst() == '.') {
        exp_sign = cutMntExp(buffer, mnt, exp, 10);
      }

      else {

        // parse decimal|floating|floating_exponent

        if (!isDec(buffer.get(0))) {
          throw new RuntimeException("not a number: " + input);
        }

        cutForBase(buffer, dec, 10);
        exp_sign = cutMntExp(buffer, mnt, exp, 10);
      }

    }

    while (!buffer.isEmpty()) {
      suf.append(buffer.removeFirst());
    }

    ///////
    // TODO:TODO:TODO: evaluation
    ///////

    final boolean isFloating = mnt.length() > 0 || exp.length() > 0;
    IntLiteralType typeBySuf = suffix(suf.toString(), isFloating);

    IntLiteral ret = new IntLiteral(originalInput, main_sign, dec.toString(), mnt.toString(), exp.toString(),
        suf.toString(), exp_sign, typeBySuf);

    if (isBin || isOct) {
      final int base = isBin ? 2 : 8;
      long value = evalLong(base, dec.toString());
      ret.setLong(value);
    }

    else if (isHex) {
      if (isFloating) {
        double value = evalhexfloat(dec.toString(), mnt.toString(), exp.toString(), exp_sign);
        ret.setDouble(value);
      } else {
        long value = evalLong(16, dec.toString());
        ret.setLong(value);
      }
    }

    else {
      // plain decimal, or floating
      if (isFloating) {
        double value = evaldecfloat(dec.toString(), mnt.toString(), exp.toString(), exp_sign);
        ret.setDouble(value);
      } else {
        long value = evalLong(10, dec.toString());
        ret.setLong(value);
      }
    }

    return ret;
  }

  private static IntLiteralType suffix(String suf, boolean isFloating) {

    if (isFloating) {
      IntLiteralType floatingSuff = null;

      if (suf.isEmpty() || suf.toUpperCase().equals("L")) {
        floatingSuff = IntLiteralType.F64;
      }
      if (suf.toUpperCase().equals("F")) {
        floatingSuff = IntLiteralType.F32;
      }

      NullChecker.check(floatingSuff);
      return floatingSuff;
    }

    // not a floating constant, i32 by default
    if (suf.isEmpty()) {
      return IntLiteralType.I32;
    }

    Map<String, IntLiteralType> map = new HashMap<>();

    List<String> suffixes = new ArrayList<>();
    suffixes.add("U    @  U32 ");
    suffixes.add("L    @  I64 ");
    suffixes.add("LL   @  I64 ");
    suffixes.add("UL   @  U64 ");
    suffixes.add("LU   @  U64 ");
    suffixes.add("ULL  @  U64 ");
    suffixes.add("LLU  @  U64 ");
    suffixes.add("i8   @  I8  ");
    suffixes.add("u8   @  U8  ");
    suffixes.add("i16  @  I16 ");
    suffixes.add("u16  @  U16 ");
    suffixes.add("i32  @  I32 ");
    suffixes.add("u32  @  U32 ");
    suffixes.add("i64  @  I64 ");
    suffixes.add("u64  @  U64 ");
    //suffixes.add("f32  @  F32 ");
    //suffixes.add("f64  @  F64 ");

    for (String s : suffixes) {
      String splitten[] = s.split("@");
      String lhs = splitten[0].trim();
      String rhs = splitten[1].trim();
      map.put(lhs.toLowerCase(), IntLiteralType.valueOf(rhs));
      map.put(lhs.toUpperCase(), IntLiteralType.valueOf(rhs));
    }

    IntLiteralType res = map.get(suf);
    NullChecker.check(res);
    return res;
  }

  private static long evalLong(int base, String dec) {

    long retval = 0;

    final char buffer[] = dec.toCharArray();
    for (int c : buffer) {
      if (!charCorrectForBase(c, base)) {
        throw new RuntimeException("the character is not suit for base: " + c);
      }
      retval = retval * base + cv(base, c);
    }

    return retval;
  }

  private static boolean charCorrectForBase(int C, int base) {
    if (base == 16 && isHex(C)) {
      return true;
    }
    if (base == 10 && isDec(C)) {
      return true;
    }
    if (base == 8 && isOct(C)) {
      return true;
    }
    if (base == 2 && isBin(C)) {
      return true;
    }
    return false;
  }

  private static double evaldecfloat(String dec, String mnt, String exp, char sig) {

    double realval = 0.0;

    for (int c : dec.toCharArray()) {
      realval = realval * 10.0 + (double) cv(10, c);
    }

    if (!mnt.isEmpty()) {
      double m = 0.1;
      for (int c : mnt.toCharArray()) {
        realval = realval + (double) cv(10, c) * m;
        m *= 0.1;
      }
    }

    boolean div = false;
    if (sig == '-') {
      div = true;
    }

    if (!exp.isEmpty()) {
      int pow = 0;
      for (int c : exp.toCharArray()) {
        pow = pow * 10 + (int) cv(10, c);
      }
      double m = 1.0;
      for (int i = 0; i < pow; i++) {
        m *= 10.0;
      }
      if (div) {
        realval /= m;
      } else {
        realval *= m;
      }
    }

    return realval;

  }

  private static double evalhexfloat(String dec, String mnt, String exp, char sig) {

    double realval = 0.0;

    for (int c : dec.toCharArray()) {
      double cv = (double) cv(16, c);
      realval = realval * 16.0 + cv;
    }

    if (!mnt.isEmpty()) {
      double m = 0.0625;
      for (int c : mnt.toCharArray()) {
        double cv = (double) cv(16, c);
        realval = realval + cv * m;
        m *= 0.0625;
      }
    }

    boolean div = false;
    if (sig == '-') {
      div = true;
    }

    if (!exp.isEmpty()) {
      //
      int pow = 0;
      for (int c : exp.toCharArray()) {
        pow = pow * 10 + (int) cv(10, c);
      }
      //
      double m = 1.0;
      for (int i = 0; i < pow; i++) {
        m *= 2.0;
      }
      //
      if (div) {
        realval /= m;
      } else {
        realval *= m;
      }

    }

    return realval;

  }

//@formatter:off
  private static  int cv(int base, int c) {
    
    boolean baseInRange = (base == 2) || (base == 8) || (base == 10) || (base == 16);
    if (!baseInRange) {
      throw new RuntimeException("error eval base = " + base + " for char=" + (char) c);
    }
    
    if (base == 2) {
      switch (c) {
      case '0': return 0;
      case '1': return 1;
      default:
        throw new RuntimeException("error eval bin base = " + base + " for char=" + (char) c);
      }
    }
    
    if (base == 8) {
      switch (c) {
      case '0': return 0;
      case '1': return 1;
      case '2': return 2;
      case '3': return 3;
      case '4': return 4;
      case '5': return 5;
      case '6': return 6;
      case '7': return 7;
      default:
        throw new RuntimeException("error eval oct base = " + base + " for char=" + (char) c);
      }
    }
    
    if (base == 10) {
      switch (c) {
      case '0': return 0;
      case '1': return 1;
      case '2': return 2;
      case '3': return 3;
      case '4': return 4;
      case '5': return 5;
      case '6': return 6;
      case '7': return 7;
      case '8': return 8;
      case '9': return 9;
      default:
        throw new RuntimeException("error eval int base = " + base + " for char=" + (char) c);
      }
    }
    
    if (base == 16) {
      switch (c) {
      case '0': return 0;
      case '1': return 1;
      case '2': return 2;
      case '3': return 3;
      case '4': return 4;
      case '5': return 5;
      case '6': return 6;
      case '7': return 7;
      case '8': return 8;
      case '9': return 9;
      case 'a': case 'A': return 10;
      case 'b': case 'B': return 11;
      case 'c': case 'C': return 12;
      case 'd': case 'D': return 13;
      case 'e': case 'E': return 14;
      case 'f': case 'F': return 15;
      default:
        throw new RuntimeException("error eval hex base = " + base + " for char=" + (char) c);
      }
    }
    
    throw new RuntimeException("error eval base = " + base + " for char=" + (char) c);
  }
  //@formatter:on

}
