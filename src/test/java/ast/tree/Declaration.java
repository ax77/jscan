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

  public final Token pos;
  public CType agregate;

  // we're interested only with variables
  public List<CSymbol> variables;

  public Declaration(CType agregate, Token pos) {
    this.pos = pos;
    this.agregate = agregate;
  }

  public Declaration(List<CSymbol> variables, Token pos) {
    this.pos = pos;
    this.variables = variables;
  }

  public Declaration(Token pos) {
    this.pos = pos;
  }

  public boolean isAgregate() {
    return agregate != null;
  }

  public boolean isStaticAssertStub() {
    return variables == null && agregate == null;
  }

  public boolean isVarlist() {
    return variables != null;
  }

}
