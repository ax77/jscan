package ast.builders;

import java.util.List;

import ast.types.CStructField;

public abstract class Finders {

  // We use 'String' instead of 'Ident' because of the unit-testing, doesn't matter :)
  public static CStructField find_identifier(String ident, List<CStructField> list, int[] o) {
    for (int i = 0; i < list.size(); i++) {
      final CStructField field = list.get(i);
      if (field.hasName()) {
        if (!field.name.getName().equals(ident)) {
          continue;
        }
        o[0] = field.offset;
        return field;
      } else {
        if (!field.type.isStrUnion()) {
          continue;
        }
        final List<CStructField> fields = field.type.tpStruct.fields;
        final CStructField sub = find_identifier(ident, fields, o);
        if (sub == null) {
          continue;
        }
        o[0] += field.offset;
        return sub;
      }
    }
    return null;
  }

}
