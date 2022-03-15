package ast.types;

import java.util.ArrayList;
import java.util.List;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;
import jscan.utils.AstParseException;

public class CStructType {
  public boolean isUnion;
  public Ident tag;
  public List<CStructField> fields; // list, because we need original declaration's order without sorting
  public boolean isComplete;

  public CStructType(boolean isUnion, Ident tag) {
    this.isUnion = isUnion;
    this.tag = tag;
    this.fields = null;
  }

  public boolean hasConstFields() {
    checkHasFields();
    for (CStructField f : fields) {
      final CType type = f.type;
      if (type.isConst()) {
        return true;
      }
    }
    return false;
  }

  public List<CStructField> getFields() {
    checkHasFields();
    return fields;
  }

  private void checkHasFields() {
    if (!isComplete) {
      throw new AstParseException("internal error: incomplete struct has no fields.");
    }
  }

  public boolean hasTag() {
    return tag != null;
  }

  public CStructField getFieldByName(Ident fieldName) {
    checkHasFields();
    List<CStructField> allfields = allfields();
    for (CStructField f : allfields) {
      if (f.name == null) {
        continue; // TODO: bitfields
      }
      if (f.name.equals(fieldName)) {
        return f;
      }
    }
    return null;
  }

  public CStructField getFieldByName(String string) {
    return getFieldByName(Hash_ident.getHashedIdent(string));
  }

  private void fill(CStructType from, List<CStructField> allfields) {
    for (CStructField f : from.fields) {
      if (f.type.isStrUnion() && !f.hasName()) {
        fill(f.type.tpStruct, allfields);
      } else {
        allfields.add(f);
      }
    }
  }

  public void setFields(List<CStructField> fields) {
    this.isComplete = true;
    this.fields = fields;
  }

  @Override
  public String toString() {
    final String strun = isUnion ? "union" : "struct";
    return "(" + strun + " " + (hasTag() ? tag.getName() : "<no-tag>") + ")";
  }

  public List<CStructField> allfields() {
    List<CStructField> allfields = new ArrayList<>();
    fill(this, allfields);
    return allfields;
  }

}