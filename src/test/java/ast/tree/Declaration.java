package ast.tree;

import java.util.List;

import ast.symtab.CSymbol;
import ast.types.CType;
import jscan.tokenize.Token;

public class Declaration {

  //  declaration
  //    : declaration_specifiers ';'
  //    | declaration_specifiers init_declarator_list ';'
  //    ;

  //  declaration
  //      : declaration_specifiers ';'
  //      | declaration_specifiers init_declarator_list ';'
  //      | static_assert_declaration
  //      ;

  private final CType agregate; // TODO:symbol
  private final List<CSymbol> variables;

  public Declaration(Token from, Token to, CType agregate) {
    this.agregate = agregate;
    this.variables = null;
  }

  public Declaration(Token from, Token to, List<CSymbol> variables) {
    this.agregate = null;
    this.variables = variables;
  }

  public Declaration() {
    this.agregate = null;
    this.variables = null;
  }

  public CType getAgregate() {
    return agregate;
  }

  public List<CSymbol> getVariables() {
    return variables;
  }

  public boolean isAgregate() {
    return agregate != null;
  }

  public boolean isStaticAssert() {
    return !isAgregate() && !isVarlist();
  }

  public boolean isVarlist() {
    return variables != null;
  }

}
