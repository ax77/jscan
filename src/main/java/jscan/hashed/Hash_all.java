package jscan.hashed;

public abstract class Hash_all {

  public static void clearAll() {
    Hash_ident.clear();
    Hash_once.clear();
    Hash_stream.clear();
    Hash_strings.clear();
  }

}
