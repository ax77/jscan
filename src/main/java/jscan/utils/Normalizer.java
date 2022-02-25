package jscan.utils;

import java.util.ArrayList;
import java.util.List;

import jscan.main.Env;

public abstract class Normalizer {

  public static String normalize(final String given) {
    final String tmp = normalizeSlashes(given);
    final List<String> splitten = strSplitChar(tmp, '/', true);

    List<String> worklist = new ArrayList<String>();
    if (isAbsoluteUnix(tmp)) {
      worklist.add("/");
    }

    for (int i = 0; i < splitten.size(); i++) {
      String part = splitten.get(i).trim();

      if (part.isEmpty()) {
        continue;
      }
      if (part.equals(".")) { // ./
        continue;
      }

      if (part.equals("..")) { // ../
        if (!worklist.isEmpty()) {
          final int lastIndex = worklist.size() - 1;
          String last = worklist.get(lastIndex);
          if (!isAbsolutePath(last)) {
            worklist.remove(lastIndex);
            continue;
          }
        }
      }

      if (i < splitten.size() - 1) {
        part += "/";
      }
      worklist.add(part);
    }

    StringBuilder sb = new StringBuilder();
    for (String s : worklist) {
      sb.append(s);
    }
    return sb.toString();
  }

  public static boolean isAbsolutePath(String s) {
    return isAbsolutePathWin(s) || isAbsoluteUnix(s);
  }

  public static boolean isAbsolutePathWin(String s) {
    if (s.length() >= 3) { // C:\, C:\\, C:/, C://
      if (Env.isLetter(s.charAt(0)) && s.charAt(1) == ':' && (s.charAt(2) == '\\' || s.charAt(2) == '/')) {
        return true;
      }
    }
    return false;
  }

  public static boolean isAbsoluteUnix(String s) {
    if (s.length() >= 1) {
      if (s.charAt(0) == '/') {
        return true;
      }
    }
    return false;
  }

  private static String normalizeSlashes(final String what) {

    StringBuilder sb = new StringBuilder();
    char s[] = what.toCharArray();

    int len = s.length;
    char p = '\0';

    for (int i = 0; i < len; i++) {
      char c = s[i];
      if (c == '\\' || c == '/') {
        if (p == '\\' || p == '/') {
          p = c;
          continue;
        }
        if (c == '\\') {
          p = c;
          sb.append('/');
          continue;
        }
      }
      if (c == ' ' || c == '\t') {
        // TODO: warning?
        // #include < stdio.h >
        // #include < folder/ another / file.h >
        // #include "  folder\\ file.h "
        // XXX: [some folder/file.h] is OK...
        // no continue!
      }
      sb.append(c);
      p = c;
    }

    return sb.toString();
  }

  private static List<String> strSplitChar(String where, char sep, boolean includeEmpty) {

    List<String> lines = new ArrayList<String>(0);
    final int len = where.length();

    if (len == 0) {
      return lines;
    }

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < len; i++) {
      char c = where.charAt(i);
      if (c == sep) {
        if (sb.length() > 0 || (sb.length() == 0 && includeEmpty)) {
          lines.add(sb.toString());
        }
        sb = new StringBuilder();
        continue;
      }
      sb.append(c);
    }
    if (sb.length() > 0 || (sb.length() == 0 && includeEmpty)) {
      lines.add(sb.toString());
    }

    return lines;
  }

  // #include "file.h"
  // #include <file.h>
  // remove leading and trailing \\" or <>
  // we know it there are ?????
  public static String unquote(final String what) {

    String n = normalizeSlashes(what);

    char s[] = n.toCharArray();
    int len = s.length;

    if (len == 0) {
      return n;
    }

    char fc = s[0];
    char lc = s[len - 1];

    boolean needtrim = (fc == '<' && lc == '>') || (fc == '\"' && lc == '\"');
    if (!needtrim) {
      return n;
    }

    // else just skip first and last char.
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i < len - 1; i++) {
      char c = s[i];
      sb.append(c);
    }
    return sb.toString().trim();
  }

}
