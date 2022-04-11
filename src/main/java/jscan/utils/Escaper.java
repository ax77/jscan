package jscan.utils;

import static jscan.utils.Env.hexValue;
import static jscan.utils.Env.isHex;
import static jscan.utils.Env.isOct;
import static jscan.utils.Env.octValue;

import java.util.ArrayList;
import java.util.List;

import jscan.buffers.Cbuf;

public abstract class Escaper {

  public static String unesc(char c) {
    if (c == '\n') {
      return "\\n";
    }
    if (c == '\r') {
      return "\\r";
    }
    if (c == '\t') {
      return "\\t";
    }
    if (c == '\0') {
      return "\\0";
    }
    if (c == '\\') {
      return "\\\\";
    }
    if (c == '\'') {
      return "\\'";
    }
    if (c == '\"') {
      return "\\\"";
    }
    return String.format("%c", c);
  }

  public static String toCString(String raw) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < raw.length(); i += 1) {
      String c = unesc(raw.charAt(i));
      sb.append(c);
    }
    return sb.toString();
  }

  public static int[] escape(String s) {
    return escapeInternal(new Cbuf(s));
  }

  private static int[] escapeInternal(Cbuf buffer) {

    List<Integer> ints = new ArrayList<Integer>();

    for (;;) {
      int c = buffer.nextc();
      if (c == Env.HC_FEOF) {
        break;
      }
      if (c != '\\') {
        ints.add(c);
        continue;
      }
      int c2 = readesc(buffer);
      ints.add(c2);
    }

    int len = ints.size();
    int result[] = new int[len + 1];
    for (int i = 0; i < ints.size(); i++) {
      result[i] = ints.get(i);
    }

    result[len] = '\0';
    return result;
  }

  private static int readesc(Cbuf buffer) {
    int c = buffer.nextc();

    if (c == '\'' || c == '\\' || c == '\"' || c == '?') {
      return c;
    }

    if (c == 'x') {
      String hxval = "";
      for (;;) {
        if (!isHex(buffer.peekc())) {
          break;
        }
        hxval += (char) buffer.nextc();
      }
      int hx = (int) hexValue(hxval);
      return hx;
    }

    if (isOct(c)) {
      String octval = "";
      octval += (char) c;
      if (isOct(buffer.peekc())) {
        octval += (char) buffer.nextc();
        if (isOct(buffer.peekc())) {
          octval += (char) buffer.nextc();
        }
      }
      int ov = (int) octValue(octval);
      return ov;
    }

    if (c == 'n') {
      return '\n';
    }
    if (c == 'r') {
      return '\r';
    }
    if (c == 't') {
      return '\t';
    }

    return c;
    // throw new ScanExc(location + " error : unrecognized escape sequence: " + (char) c);
  }
}

//	#include <stdio.h>
//	#include <string.h>
//
//	int main() {
//		const char s[] = "\"\'\\0\'\x1\x2\1\2\001\002\r\n\"";
//		int i, len;
//		for(i = 0, len = strlen(s); i < len; i++) {
//			printf("%3d : [%3d]\n", i, s[i]);
//		}
//		return 0;
//	}
//
//	/*
//
//	  0 : [ 34]
//	  1 : [ 39]
//	  2 : [ 92]
//	  3 : [ 48]
//	  4 : [ 39]
//	  5 : [  1]
//	  6 : [  2]
//	  7 : [  1]
//	  8 : [  2]
//	  9 : [  1]
//	 10 : [  2]
//	 11 : [ 13]
//	 12 : [ 10]
//	 13 : [ 34]
//
//	*/
