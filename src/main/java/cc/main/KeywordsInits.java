package cc.main;

import java.lang.reflect.Field;

import cc.tree.Keywords;
import jscan.hashed.Hash_ident;
import jscan.utils.AstParseException;

public abstract class KeywordsInits {

  // we have to initialize it here because of the static-laziness
  // we must be sure that all keywords will be created before parser will be run.
  //
  public static void initIdents() {

    for (Field field : Keywords.class.getDeclaredFields()) {
      final String fname = field.getName();
      if (!fname.endsWith("_ident")) {
        throw new AstParseException("expect ident name");
      }
      final int pos = fname.indexOf("_ident");
      final String sub = fname.substring(0, pos).trim();
      Hash_ident.getHashedIdent(sub, 32);
    }
  }

}
