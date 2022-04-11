package cc.parse;

import static jscan.tokenize.T.T_AND;
import static jscan.tokenize.T.T_EXCLAMATION;
import static jscan.tokenize.T.T_MINUS;
import static jscan.tokenize.T.T_PLUS;
import static jscan.tokenize.T.T_TILDE;
import static jscan.tokenize.T.T_TIMES;

import cc.tree.Keywords;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public abstract class Pcheckers {

  //@formatter:off
  
  public static boolean isAssignOperator(Token what) {
    return what.is(T.T_ASSIGN)
        || what.is(T.T_TIMES_EQUAL)
        || what.is(T.T_PERCENT_EQUAL)
        || what.is(T.T_DIVIDE_EQUAL)
        || what.is(T.T_PLUS_EQUAL)
        || what.is(T.T_MINUS_EQUAL)
        || what.is(T.T_LSHIFT_EQUAL)
        || what.is(T.T_RSHIFT_EQUAL)
        || what.is(T.T_AND_EQUAL)
        || what.is(T.T_XOR_EQUAL)
        || what.is(T.T_OR_EQUAL);
  }

  // & * + - ~ !
  public static boolean isUnaryOperator(Token what) {
    return what.is(T_AND)
        || what.is(T_TIMES)
        || what.is(T_PLUS)
        || what.is(T_MINUS)
        || what.is(T_TILDE)
        || what.is(T_EXCLAMATION);
  }

  public static boolean isStorageClassSpec(Token what) {
    return what.isIdent(Keywords.static_ident)
        || what.isIdent(Keywords.extern_ident)
        || what.isIdent(Keywords.auto_ident)
        || what.isIdent(Keywords.register_ident)
        || what.isIdent(Keywords.typedef_ident);
  }

  public static boolean isTypeSpec(Token what) {
    return what.isIdent(Keywords.void_ident)
        || what.isIdent(Keywords.char_ident)
        || what.isIdent(Keywords.short_ident)
        || what.isIdent(Keywords.int_ident)
        || what.isIdent(Keywords.long_ident)
        || what.isIdent(Keywords.float_ident)
        || what.isIdent(Keywords.double_ident)
        || what.isIdent(Keywords.signed_ident)
        || what.isIdent(Keywords.unsigned_ident)
        || what.isIdent(Keywords._Bool_ident)
        || what.isIdent(Keywords._Complex_ident);
  }

  public static boolean isConstIdent(Token what) {
    return what.isIdent(Keywords.const_ident)
        || what.isIdent(Keywords.__const___ident)
        || what.isIdent(Keywords.__const_ident);
  }

  public static boolean isVolatileIdent(Token what) {
    return what.isIdent(Keywords.volatile_ident)
        || what.isIdent(Keywords.__volatile___ident)
        || what.isIdent(Keywords.__volatile_ident);
  }

  private static boolean isRestrictIdent(Token what) {
    return what.isIdent(Keywords.restrict_ident)
        || what.isIdent(Keywords.__restrict___ident)
        || what.isIdent(Keywords.__restrict_ident);
  }

  public static boolean isTypeQual(Token what) {
    return isConstIdent(what) || isVolatileIdent(what) || isRestrictIdent(what);
  }

  public static boolean isInlineIdent(Token what) {
    return what.isIdent(Keywords.inline_ident)
        || what.isIdent(Keywords.__inline_ident)
        || what.isIdent(Keywords.__inline___ident);
  }

  public static boolean isFuncSpec(Token what) {
    return isInlineIdent(what) || isNoreturnIdent(what);
  }

  public static boolean isNoreturnIdent(Token what) {
    return what.isIdent(Keywords._Noreturn_ident);
  }

  public static boolean isEnumSpecStart(Token what) {
    return what.isIdent(Keywords.enum_ident);
  }

  public static boolean isStructOrUnionSpecStart(Token what) {
    return what.isIdent(Keywords.struct_ident) || what.isIdent(Keywords.union_ident);
  }

  public static boolean isAttributeStartGnuc(Token what) {
    return what.isIdent(Keywords.__attribute___ident) || what.isIdent(Keywords.__attribute_ident);
  }

  public static boolean isAsmStart(Token what) {
    return what.isIdent(Keywords.asm_ident)
        || what.isIdent(Keywords.__asm___ident)
        || what.isIdent(Keywords.__asm_ident);
  }

  public static boolean isStaticAssert(Token what) {
    return what.isIdent(Keywords._Static_assert_ident);
  }
}
