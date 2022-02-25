package jscan.preproc;

import static jscan.preproc.ErrorCode.E_FILE_NOT_FOUND;
import static jscan.preproc.ErrorCode.E_HASH_HASH_AT_BEGIN_REPL;
import static jscan.preproc.ErrorCode.E_HASH_HASH_AT_END_REPL;
import static jscan.preproc.ErrorCode.E_NO_FORMAT_PARAMETER_AFTER_SHARP;
import static jscan.preproc.ErrorCode.E_UNTERMINATED_CHAR_CONSTANT;
import static jscan.preproc.ErrorCode.E_UNTERMINATED_COMMENT;
import static jscan.preproc.ErrorCode.E_UNTERMINATED_STRING_CONSTANT;

import java.util.Map;
import java.util.TreeMap;

import jscan.preproc.preprocess.ScanExc;

public class Error {

  private static Map<ErrorCode, String> error_table = new TreeMap<ErrorCode, String>();
  private static Map<ErrorCode, String> warning_table = new TreeMap<ErrorCode, String>();

  static {

    // fatal - errors... 

    error_table.put(E_FILE_NOT_FOUND, "file not found");
    error_table.put(E_UNTERMINATED_COMMENT, "unterminated '/*' comment");

    error_table.put(E_UNTERMINATED_STRING_CONSTANT, "unterminated string constant");
    error_table.put(E_UNTERMINATED_CHAR_CONSTANT, "unterminated character constant");

    error_table.put(E_HASH_HASH_AT_BEGIN_REPL, "\"##\" may not be first in a macro replacement list");
    error_table.put(E_HASH_HASH_AT_END_REPL, "\"##\" may not be last in a macro replacement list");

    error_table.put(E_NO_FORMAT_PARAMETER_AFTER_SHARP, "\"#\" is not followed by a macro parameter");

    warning_table.put(ErrorCode.W_DEFINED_IN_REPLACEMENT_LIST,
        "`defined` unary operator in replacement list is undefined behavior." + "This code is not portable.");

    warning_table.put(ErrorCode.W_PP_DIRECTIVE_IN_ARGUMENT_LIST,
        "preprocessor directive in agrument list is not portable.");
  }

  public static void error(ErrorCode code, String prefixMsg) {
    throw new ScanExc(prefixMsg.trim() + " error: " + error_table.get(code));
  }

  public static void warning(ErrorCode code, String prefixMsg) {
    System.out.println(prefixMsg + " warning: " + warning_table.get(code));
  }
}
