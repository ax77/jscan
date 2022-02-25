package ast.types;

import java.util.List;

import jscan.symtab.Ident;
import ast.errors.ParseException;

public class CStructType {
  private boolean isUnion;
  private Ident tag;
  private List<CStructField> fields; // list, because we need original declaration's order without sorting
  private boolean isIncomplete;

  public CStructType(boolean isUnion, Ident tag) {
    this.isUnion = isUnion;
    this.tag = tag;
    this.fields = null;
    this.isIncomplete = true;
  }

  public CStructType(boolean isUnion, Ident tag, List<CStructField> fields) {
    this.isUnion = isUnion;
    this.tag = tag;
    this.fields = fields;
    this.isIncomplete = true;
  }

  public boolean isHasConstFields() {
    checkHasFields();
    for (CStructField f : fields) {
      final CType type = f.getType();
      if (type.isConst()) {
        return true;
      }
    }
    return false;
  }

  public boolean isIncomplete() {
    return isIncomplete;
  }

  public void setIncomplete(boolean isIncomplete) {
    this.isIncomplete = isIncomplete;
  }

  public boolean isUnion() {
    return isUnion;
  }

  public Ident getTag() {
    return tag;
  }

  public List<CStructField> getFields() {
    checkHasFields();
    return fields;
  }

  private void checkHasFields() {
    if (isIncomplete) {
      throw new ParseException("internal error: incomplete struct has no fields.");
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    final String str = tag == null ? "<no-tag>" : "tag=" + tag.getName() + " ";
    sb.append((isUnion ? "UNION " : "STRUCT ") + str);
    if (!isIncomplete) {
      sb.append(fields.toString());
    }
    return sb.toString();
  }

  public boolean isHasTag() {
    return tag != null;
  }

  public boolean isHasField(String s) {
    checkHasFields();
    for (CStructField f : fields) {
      if (!f.isHasName()) {
        continue;
      }
      if (f.getName().getName().equals(s)) {
        return true;
      }
    }
    return false;
  }

  public CStructField findField(Ident fieldName) {
    checkHasFields();
    for (CStructField f : fields) {
      if (!f.isHasName()) {
        continue; // unnamed bf. TODO:
      }
      if (f.getName().equals(fieldName)) {
        return f;
      }
    }
    return null;
  }

  public void setUnion(boolean isUnion) {
    this.isUnion = isUnion;
  }

  public void setTag(Ident tag) {
    this.tag = tag;
  }

  public void setFields(List<CStructField> fields) {
    this.isIncomplete = false;
    this.fields = fields;
  }

}