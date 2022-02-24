package symtab;

import java.util.ArrayList;
import java.util.List;

import utils.AstParseException;

public class Symtab<K, V> {

  // this is simple stack
  // when create new scope, push it on top
  // when add new symbol, get top table, and put symbol on it

  private List<Scope<K, V>> scopes;

  public Symtab() {
    this.scopes = new ArrayList<Scope<K, V>>();
  }

  public void pushscope(ScopeLevels level) {
    this.scopes.add(0, new Scope<K, V>(level));
  }

  public void popscope() {
    checkIsEmpty();
    this.scopes.remove(0);
  }

  private void checkIsEmpty() {
    if (scopes.isEmpty()) {
      throw new AstParseException("empty symbol table. no scopes are available");
    }
  }

  public Scope<K, V> peekScope() {
    checkIsEmpty();
    return scopes.get(0);
  }

  public List<Scope<K, V>> getScopes() {
    return scopes;
  }

  public boolean isFileScope() {
    return scopes.size() == 1;
  }

  public boolean isBlockScope() {
    return scopes.size() > 1;
  }

  public V getsym(K name) {
    for (int i = 0; i < scopes.size(); i++) {
      Scope<K, V> scope = scopes.get(i);
      V sym = scope.get(name);
      if (sym != null) {
        return sym;
      }
    }
    return null;
  }

  // this need __ONLY__ when we define new symbol, and check redefinition
  // others symbols we get from bottom to top scope to scope...
  public V getsymFromCurrentScope(K name) {
    Scope<K, V> scope = scopes.get(0);
    V sym = scope.get(name);
    if (sym != null) {
      return sym;
    }
    return null;
  }

  public void addsym(K key, V sym) {
    checkIsEmpty();
    if (key == null) {
      throw new AstParseException("null key for symbol entry...");
    }
    Scope<K, V> scope = scopes.get(0);
    scope.put(key, sym);
  }

  public boolean isEmpty() {
    return scopes.isEmpty();
  }

  public void dump() {
    for (int i = 0; i < scopes.size(); i++) {
      Scope<K, V> scope = scopes.get(i);
      scope.dump();
    }
  }
}
