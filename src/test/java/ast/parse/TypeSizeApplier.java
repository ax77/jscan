package ast.parse;

import java.util.List;

import ast.types.CStructField;
import ast.types.CType;
import jscan.utils.Aligner;

public abstract class TypeSizeApplier {

  static class Info {
    int max_align;
    int size;

    public Info() {
      max_align = 1;
      size = 0;
    }
  }

  private static void lay_out_union_field(CStructField f, Info info) {

    CType tp = f.type;
    examine_symbol_type(tp);

    if (tp.align > info.max_align)
      info.max_align = tp.align;
    if (tp.size > info.size)
      info.size = tp.size;

    f.offset = 0;
  }

  private static void lay_out_struct_field(CStructField f, Info info) {

    CType tp = f.type;
    examine_symbol_type(tp);

    if (tp.align > info.max_align)
      info.max_align = tp.align;

    int size = Aligner.align(info.size, tp.align);
    f.offset = size;
    info.size = size + tp.size;
  }

  private static void iterate_st(List<CStructField> fields, Info info) {
    for (CStructField f : fields) {
      lay_out_struct_field(f, info);
    }
  }

  private static void iterate_un(List<CStructField> fields, Info info) {
    for (CStructField f : fields) {
      lay_out_union_field(f, info);
    }
  }

  private static void examine_struct_union_type(CType tp, boolean struc) {

    final Info info = new Info();
    final List<CStructField> fields = tp.tpStruct.fields;

    if (struc) {
      iterate_st(fields, info);
    } else {
      iterate_un(fields, info);
    }

    if (tp.align <= 0)
      tp.align = info.max_align;

    tp.size = Aligner.align(info.size, tp.align);

    // set the position for each field, including fields from anonymous
    // struct/union, we need the position for designated-initializers.
    final List<CStructField> allfields = tp.tpStruct.allfields();
    for (int i = 0; i < allfields.size(); i++) {
      allfields.get(i).pos = i;
    }
  }

  public static CType examine_symbol_type(CType tp) {

    // TODO.flexible.array

    if (tp.size > 0) {
      return tp;
    }

    if (tp.isStrUnion()) {
      examine_struct_union_type(tp, tp.isStruct());
      return tp;
    }

    return tp;
  }

}
