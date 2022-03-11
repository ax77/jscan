package ast.builders;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.types.CStructField;
import jscan.symtab.Ident;
import jscan.utils.Aligner;
import jscan.utils.AstParseException;

public class ApplyStructInfo {

  private final List<CStructField> fields;
  private int size;
  private int align;

  public ApplyStructInfo(boolean isUnion, List<CStructField> fields) {

    this.fields = fields;
    this.size = 0;
    this.align = 1;

    checkFieldsUnique();
    applyAlignment();

    if (isUnion) {
      calcUnionFieldsOffsets();
    } else {
      calcStructFieldsOffsets();
    }
  }

  private void applyAlignment() {
    for (CStructField f : fields) {
      align = Aligner.align(align, f.type.align);
    }
  }

  private void checkFieldsUnique() {
    Set<Ident> toCheckUnique = new HashSet<Ident>();
    for (CStructField f : fields) {
      if (f.isHasName()) {
        final Ident name = f.name;
        if (toCheckUnique.contains(name)) {
          throw new AstParseException("duplicate struct/union field: " + name.getName());
        }
        toCheckUnique.add(name);
      }
    }
  }

  private int getMaxFieldSize(List<CStructField> fields) {
    int msize = 0;
    for (CStructField f : fields) {
      final int size = f.type.size;
      if (msize < size) {
        msize = size;
      }
    }
    if (msize == 0) {
      throw new AstParseException("internal error: zero sized field...");
    }
    return msize;
  }

  private void calcStructFieldsOffsets() {
    int offset = 0;
    for (CStructField f : fields) {
      offset = Aligner.align(offset, f.type.align);
      f.offset = offset;
      offset += f.type.size;
    }
    size = offset;
  }

  private void calcUnionFieldsOffsets() {
    size = getMaxFieldSize(fields);
    for (CStructField f : fields) {
      f.offset = 0;
    }
  }

  public int getSize() {
    return size;
  }

  public int getAlign() {
    return align;
  }

}