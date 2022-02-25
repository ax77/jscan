package jscan.hashed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jscan.symtab.Ident;

public final class Hash_ident {
  private static Map<String, Ident> identHash = new HashMap<String, Ident>();

  public static void clear() {

    // remove only user-defined identifiers
    // for builtin ident clear Sym, with this ident associated
    //

    List<String> toremove = new ArrayList<String>();
    for (Entry<String, Ident> entry : identHash.entrySet()) {
      Ident id = entry.getValue();
      if (!id.isBuiltin()) {
        toremove.add(id.getName());
      } else {
        id.setSymNull();
      }
    }
    for (String s : toremove) {
      identHash.remove(s);
    }
  }

  public static Ident getHashedIdent(String name, int ns) {
    if (!identHash.containsKey(name)) {
      Ident newid = new Ident(name, ns);
      identHash.put(name, newid);
      return newid;
    }
    return identHash.get(name);
  }

  public static Ident getHashedIdent(String name) {
    if (!identHash.containsKey(name)) {
      Ident newid = new Ident(name);
      identHash.put(name, newid);
      return newid;
    }
    return identHash.get(name);
  }

  public static void dumpIdentTable() {
    System.out.println("\nIDENT TABLE:");
    for (Map.Entry<String, Ident> entry : identHash.entrySet()) {
      System.out.println(entry.getValue().toString());
    }
  }

}
