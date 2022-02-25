package ast.decls;

import java.util.List;

import jscan.sourceloc.SourceLocationRange;
import jscan.tokenize.Token;
import ast.parse.ILocationRange;
import ast.symtab.elements.CSymbol;
import ast.types.CType;

public class Declaration implements ILocationRange {

  //  declaration
  //    : declaration_specifiers ';'
  //    | declaration_specifiers init_declarator_list ';'
  //    ;

  //  declaration
  //      : declaration_specifiers ';'
  //      | declaration_specifiers init_declarator_list ';'
  //      | static_assert_declaration
  //      ;

  private final SourceLocationRange location;
  private final CType agregate; // TODO:symbol
  private final List<CSymbol> variables;

  // internal
  private final boolean isStaticAssertStub;
  private final boolean isAgregate;

  public Declaration(Token from, Token to, CType agregate) {
    this.location = new SourceLocationRange(from, to);
    this.agregate = agregate;
    this.variables = null;
    this.isStaticAssertStub = false;
    this.isAgregate = true;
  }

  public Declaration(Token from, Token to, List<CSymbol> variables) {
    this.location = new SourceLocationRange(from, to);
    this.agregate = null;
    this.variables = variables;
    this.isStaticAssertStub = false;
    this.isAgregate = false;
  }

  public Declaration() {
    this.location = null;
    this.agregate = null;
    this.variables = null;
    isStaticAssertStub = true;
    this.isAgregate = false;
  }

  public CType getAgregate() {
    return agregate;
  }

  public List<CSymbol> getVariables() {
    return variables;
  }

  public boolean isAgregate() {
    return isAgregate;
  }

  // TODO: more clean...
  public boolean isStaticAssert() {
    return isStaticAssertStub;
  }

  public boolean isVarlist() {
    if (isAgregate) {
      return false;
    }
    if (isStaticAssertStub) {
      return false;
    }
    return true;
  }

  @Override
  public String getLocationToStringBegin() {
    return location.getBeginPos().toString();
  }

  @Override
  public String getLocationToStringEnd() {
    return location.getEndPos().toString();
  }

  @Override
  public int getLocationLineBegin() {
    return location.getBeginPos().getLine();
  }

  @Override
  public int getLocationLineEnd() {
    return location.getEndPos().getLine();
  }

  @Override
  public String getLocationFile() {
    return location.getBeginPos().getFilename();
  }

  @Override
  public SourceLocationRange getLocation() {
    return location;
  }

}
