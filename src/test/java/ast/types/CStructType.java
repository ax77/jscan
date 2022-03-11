package ast.types;

import java.util.List;

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

  public boolean hasField(String s) {
    checkHasFields();
    for (CStructField f : fields) {
      if (!f.hasName()) {
        continue;
      }
      if (f.name.getName().equals(s)) {
        return true;
      }
    }
    return false;
  }

  public CStructField getFieldByName(Ident fieldName) {
    checkHasFields();
    for (CStructField f : fields) {
      if (!f.hasName()) {
        continue; // unnamed bf. TODO:
      }
      if (f.name.equals(fieldName)) {
        return f;
      }
    }
    return null;
  }

  public void setFields(List<CStructField> fields) {
    this.isComplete = true;
    this.fields = fields;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    final String str = tag == null ? "<no-tag>" : "tag=" + tag.getName() + " ";
    sb.append((isUnion ? "UNION " : "STRUCT ") + str);
    if (isComplete) {
      sb.append(fields.toString());
    }
    return sb.toString();
  }

}