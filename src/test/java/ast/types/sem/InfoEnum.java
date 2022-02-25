package ast.types.sem;

import java.util.HashMap;
import java.util.Map;

import jscan.symtab.Ident;
import ast.parse.Parse;

public class InfoEnum {
  private final Parse parser;
  private int minvalue;
  private int maxvalue;
  private Map<Ident, Integer> enumerators;
  private int curvalue;

  public InfoEnum(Parse parser) {
    this.parser = parser;
    this.enumerators = new HashMap<Ident, Integer>();
  }

  public void addEnumerator(Ident id, int curvalue) {
    if (enumerators.containsKey(id)) {
      parser.perror("duplicate enum value: " + id.getName()); // TODO: ambiguous with symDef()
    }
    this.enumerators.put(id, curvalue);
    this.curvalue = curvalue;

    this.minvalue = Math.min(this.minvalue, this.curvalue);
    this.maxvalue = Math.max(this.maxvalue, this.curvalue);
    this.curvalue += 1;
  }

  public int getCurvalue() {
    return curvalue;
  }

  public Map<Ident, Integer> getEnumerators() {
    return enumerators;
  }

}