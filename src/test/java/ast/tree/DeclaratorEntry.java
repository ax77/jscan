package ast.tree;

import java.util.List;

import ast.types.CFuncParam;
import ast.types.CTypeKind;
import jscan.utils.NullChecker;

public class DeclaratorEntry {
  public final CTypeKind base;
  public List<CFuncParam> parameters;
  public int arrlen;
  public boolean isConstPointer;
  public boolean isVariadicFunction;

  public DeclaratorEntry(CTypeKind base) {
    NullChecker.check(base);
    this.base = base;
  }

}