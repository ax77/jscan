package cc.builders;

public enum TypeApplierStage {
  stage_start, assign_lhs, assign_rhs, binary_lhs, binary_rhs, comma_lhs, comma_rhs, tern_cnd, tern_true, tern_false,
  unary_operand, generic_control_expr, compsel_postfix, fcall_function, preincdec_operand, postincdec_operand,
}