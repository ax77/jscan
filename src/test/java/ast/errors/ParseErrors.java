package ast.errors;

public enum ParseErrors {
  E_INCOMPLETE_STRUCT_FIELD {
    @Override
    public String toString() {
      return "struct doe's not contain incomplete field's";
    }
  },

}
