package ast_util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ast.types.CTypeKind;
import jscan.preproc.TokenPrint;
import jscan.tokenize.Stream;
import jscan.tokenize.Token;
import jscan.utils.AstParseException;

public class Ast5 {

  public static Map<List<String>, CTypeKind> typesmap() {
    List<String> types = new ArrayList<>();
    types.add("void                      %     TP_VOID");
    types.add("_Bool                     %     TP_BOOL");
    types.add("char                      %     TP_CHAR");
    types.add("signed@char               %     TP_CHAR");
    types.add("unsigned@char             %     TP_UCHAR");
    types.add("short                     %     TP_SHORT");
    types.add("signed@short              %     TP_SHORT");
    types.add("short@int                 %     TP_SHORT");
    types.add("signed@short@int          %     TP_SHORT");
    types.add("unsigned@short            %     TP_USHORT");
    types.add("unsigned@short@int        %     TP_USHORT");
    types.add("int                       %     TP_INT");
    types.add("signed                    %     TP_INT");
    types.add("signed@int                %     TP_INT");
    types.add("unsigned@int              %     TP_UINT");
    types.add("unsigned                  %     TP_UINT");
    types.add("long                      %     TP_LONG");
    types.add("signed@long               %     TP_LONG");
    types.add("long@int                  %     TP_LONG");
    types.add("signed@long@int           %     TP_LONG");
    types.add("unsigned@long             %     TP_ULONG");
    types.add("unsigned@long@int         %     TP_ULONG");
    types.add("long@long                 %     TP_LONG_LONG");
    types.add("signed@long@long          %     TP_LONG_LONG");
    types.add("long@long@int             %     TP_LONG_LONG");
    types.add("signed@long@long@int      %     TP_LONG_LONG");
    types.add("unsigned@long@long        %     TP_ULONG_LONG");
    types.add("unsigned@long@long@int    %     TP_ULONG_LONG");
    types.add("float                     %     TP_FLOAT");
    types.add("double                    %     TP_DOUBLE");
    types.add("long@double               %     TP_LONG_DOUBLE");
    types.add("float@_Complex            %     TP_FLOAT_COMPLEX");
    types.add("double@_Complex           %     TP_DOUBLE_COMPLEX");
    types.add("long@double@_Complex      %     TP_LONG_DOUBLE_COMPLEX");

    Map<List<String>, CTypeKind> map = new HashMap<List<String>, CTypeKind>();
    for (String s : types) {
      String splitten[] = s.split("%");
      String lhs = splitten[0].trim();
      String rhs = splitten[1].trim();

      final List<String> key = new ArrayList<>();
      for (String onetype : lhs.split("@")) {
        key.add(onetype.trim());
      }
      Collections.sort(key);
      map.put(key, CTypeKind.valueOf(rhs));
    }
    return map;
  }

  private static List<String> buildTheFinderKey(List<Token> list) {
    // let's build the key
    List<String> finder = new ArrayList<>();
    for (Token tok : list) {
      if (tok.typeIsSpecialStreamMarks()) {
        continue;
      }

      tok.checkId();
      final String value = tok.getIdent().getName();

      // 'long long' is fine, but: 'short short' is not :)
      if (finder.contains(value) && !value.equals("long")) {
        continue;
      }
      finder.add(value);
    }
    Collections.sort(finder);
    return finder;
  }

  public static CTypeKind typespec(List<Token> list) {

    final List<String> finder = buildTheFinderKey(list);

    // let's try to find the type
    final Map<List<String>, CTypeKind> types = typesmap();
    final CTypeKind result = types.get(finder);
    if (result != null) {
      return result;
    }

    throw new AstParseException("the type specifier combination is not correct: [" + TokenPrint.toStrLine(list) + "]");
  }

  @Test
  public void test() {
    System.out.println(typespec(new Stream("utest", "int signed signed short").getTokenlist()));
    System.out.println(typespec(new Stream("utest", "long int unsigned long").getTokenlist()));
  }

}
