package ast.symtab.elements;

import jscan.literals.IntLiteralType;

public class NumericConstant {
  private final IntLiteralType numtype;

  private final long clong;
  private final double cdouble;

  public NumericConstant(long clong, IntLiteralType numtype) {
    this.numtype = numtype;
    this.clong = clong;
    this.cdouble = (double) clong;
  }

  public NumericConstant(double cdouble, IntLiteralType numtype) {
    this.numtype = numtype;
    this.clong = (long) cdouble;
    this.cdouble = cdouble;
  }

  public long getClong() {
    return clong;
  }

  public double getCdouble() {
    return cdouble;
  }

  public IntLiteralType getNumtype() {
    return numtype;
  }

  public boolean isInteger() {
    if (numtype == IntLiteralType.NO_SUFFIX) {
      return false;
    }
    if (numtype == IntLiteralType.F32 || numtype == IntLiteralType.F64) {
      return false;
    }
    return true;
  }

}
