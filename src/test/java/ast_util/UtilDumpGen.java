package ast_util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class UtilDumpGen {

  private static List<String> ENAMES = new ArrayList<String>();
  static {
    ENAMES.add("EASSIGN              ");
    ENAMES.add("EBINARY              ");
    ENAMES.add("ECOMMA               ");
    ENAMES.add("ETERNARY             ");
    ENAMES.add("EUNARY               ");
    ENAMES.add("EPRIMARY_IDENT       ");
    ENAMES.add("EPRIMARY_CONST       ");
    ENAMES.add("EPRIMARY_STRING      ");
    ENAMES.add("EPRIMARY_GENERIC     ");
    ENAMES.add("ECOMPSEL             ");
    ENAMES.add("ECAST                ");
    ENAMES.add("EFCALL               ");
    ENAMES.add("EPREINCDEC           ");
    ENAMES.add("EPOSTINCDEC          ");
    ENAMES.add("ECOMPLITERAL         ");

    ENAMES.add("SCOMPOUND     ");
    ENAMES.add("SIF           ");
    ENAMES.add("SWHILE        ");
    ENAMES.add("SDOWHILE      ");
    ENAMES.add("SEXPR         ");
    ENAMES.add("SBREAK        ");
    ENAMES.add("SCONTINUE     ");
    ENAMES.add("SSEMICOLON    ");
    ENAMES.add("SSWITCH       ");
    ENAMES.add("SCASE         ");
    ENAMES.add("SFOR          ");
    ENAMES.add("SRETURN       ");
    ENAMES.add("SGOTO         ");
    ENAMES.add("SLABEL        ");
    ENAMES.add("SDEFAULT      ");
    ENAMES.add("SASM          ");
  }

  @Ignore
  @Test
  public void test() {
    for (String s : ENAMES) {
      System.out.println("else if(base == " + s.trim() + ") {\n}\n");
    }
  }
}
