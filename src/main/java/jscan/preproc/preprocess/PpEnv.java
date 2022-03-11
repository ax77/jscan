package jscan.preproc.preprocess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jscan.tokenize.T;
import jscan.tokenize.Token;

public abstract class PpEnv {

  public static final Map<String, T> PP_DIRECTIVES = new HashMap<String, T>();
  public static final Set<T> PP_DIRECTIVES_SET = new HashSet<T>();

  public static final Set<String> INC_ANGLE_DIRS = new HashSet<String>(); // -I
  public static final Set<String> INC_DEFAULT_ANGLE_DIRS = new HashSet<String>(); // usr/include/

  public static final boolean DUMP_STREAM_INFO = false;
  public static final boolean WARN_UNRECOGNIZED_CHARS = false;

  public static final char HC_PATH_SEP = '/';

  static {

    // to check, that Token one of this...
    //
    PP_DIRECTIVES_SET.add(T.PT_HEOL);
    PP_DIRECTIVES_SET.add(T.PT_HINCLUDE);
    PP_DIRECTIVES_SET.add(T.PT_HDEFINE);
    PP_DIRECTIVES_SET.add(T.PT_HUNDEF);
    PP_DIRECTIVES_SET.add(T.PT_HIF);
    PP_DIRECTIVES_SET.add(T.PT_HIFDEF);
    PP_DIRECTIVES_SET.add(T.PT_HIFNDEF);
    PP_DIRECTIVES_SET.add(T.PT_HENDIF);
    PP_DIRECTIVES_SET.add(T.PT_HELSE);
    PP_DIRECTIVES_SET.add(T.PT_HELIF);
    PP_DIRECTIVES_SET.add(T.PT_HLINE);
    PP_DIRECTIVES_SET.add(T.PT_HERROR);
    PP_DIRECTIVES_SET.add(T.PT_HPRAGMA);
    PP_DIRECTIVES_SET.add(T.PT_HWARNING);
    PP_DIRECTIVES_SET.add(T.PT_HINCLUDE_NEXT);

    PP_DIRECTIVES.put("define", T.PT_HDEFINE);
    PP_DIRECTIVES.put("undef", T.PT_HUNDEF);
    PP_DIRECTIVES.put("include", T.PT_HINCLUDE);
    PP_DIRECTIVES.put("if", T.PT_HIF);
    PP_DIRECTIVES.put("ifdef", T.PT_HIFDEF);
    PP_DIRECTIVES.put("ifndef", T.PT_HIFNDEF);
    PP_DIRECTIVES.put("endif", T.PT_HENDIF);
    PP_DIRECTIVES.put("else", T.PT_HELSE);
    PP_DIRECTIVES.put("elif", T.PT_HELIF);
    PP_DIRECTIVES.put("line", T.PT_HLINE);
    PP_DIRECTIVES.put("error", T.PT_HERROR);
    PP_DIRECTIVES.put("pragma", T.PT_HPRAGMA);
    PP_DIRECTIVES.put("warning", T.PT_HWARNING);
    PP_DIRECTIVES.put("include_next", T.PT_HINCLUDE_NEXT);
  }

  public static boolean isPPDirType(Token tok) {
    return PpEnv.PP_DIRECTIVES_SET.contains(tok.getType());
  }

  public static boolean isIfs(Token tok) {
    return tok.is(T.PT_HIF) || tok.is(T.PT_HIFDEF) || tok.is(T.PT_HIFNDEF);
  }

}
