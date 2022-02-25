package jscan.tokenize;

public enum T {

    // special
    TOKEN_EOF,
      TOKEN_ERROR,
      TOKEN_STREAMBEGIN,
      TOKEN_STREAMEND,

      //
      TOKEN_IDENT,
      TOKEN_NUMBER,
      TOKEN_CHAR,
      TOKEN_STRING,

      T_SPEC_PLACEMARKER,
      T_SPEC_UNHIDE,

      // 'super' - lexem for preprocessor.
      // handle it if see '#' at begin of line, and second is one of this...
      PT_HEOL,
      PT_HINCLUDE,
      PT_HDEFINE,
      PT_HUNDEF,
      PT_HIF,
      PT_HIFDEF,
      PT_HIFNDEF,
      PT_HENDIF,
      PT_HELSE,
      PT_HELIF,
      PT_HLINE,
      PT_HERROR,
      PT_HPRAGMA,
      PT_HWARNING,
      PT_HINCLUDE_NEXT,

      // combinations
      /* "->" */T_ARROW,
      /* "--" */T_MINUS_MINUS,
      /* "-=" */T_MINUS_EQUAL,
      /* "!=" */T_NE,
      /* ".." */T_DOT_DOT,
      /* "*=" */T_TIMES_EQUAL,
      /* "/=" */T_DIVIDE_EQUAL,
      /* "&=" */T_AND_EQUAL,
      /* "&&" */T_AND_AND,
      /* "##" */T_SHARP_SHARP,
      /* "%=" */T_PERCENT_EQUAL,
      /* "^=" */T_XOR_EQUAL,
      /* "++" */T_PLUS_PLUS,
      /* "+=" */T_PLUS_EQUAL,
      /* "<=" */T_LE,
      /* "<<" */T_LSHIFT,
      /* "==" */T_EQ,
      /* ">=" */T_GE,
      /* ">>" */T_RSHIFT,
      /* "||" */T_OR_OR,
      /* "|=" */T_OR_EQUAL,

      // single
      /* "," */T_COMMA,
      /* "-" */T_MINUS,
      /* ";" */T_SEMI_COLON,
      /* ":" */T_COLON,
      /* "!" */T_EXCLAMATION,
      /* "?" */T_QUESTION,
      /* "." */T_DOT,
      /* "(" */T_LEFT_PAREN,
      /* ")" */T_RIGHT_PAREN,
      /* "[" */T_LEFT_BRACKET,
      /* "]" */T_RIGHT_BRACKET,
      /* "{" */T_LEFT_BRACE,
      /* "}" */T_RIGHT_BRACE,
      /* "*" */T_TIMES,
      /* "/" */T_DIVIDE,
      /* "&" */T_AND,
      /* "#" */T_SHARP,
      /* "%" */T_PERCENT,
      /* "^" */T_XOR,
      /* "+" */T_PLUS,
      /* "<" */T_LT,
      /* "=" */T_ASSIGN,
      /* ">" */T_GT,
      /* "|" */T_OR,
      /* "~" */T_TILDE,

      // others
      /* "$" */T_DOLLAR_SIGN,
      /* "@" */T_AT_SIGN,
      /* "`" */T_GRAVE_ACCENT,
      /* "\" */T_BACKSLASH, // naked, not [\][\n]

      // large
      /* "..." */T_DOT_DOT_DOT,
      /* ">>=" */T_LSHIFT_EQUAL,
      /* "<<=" */T_RSHIFT_EQUAL

      , TOKEN_COMMENT,

}
