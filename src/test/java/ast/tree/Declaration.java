package ast.tree;

import java.util.ArrayList;
import java.util.List;

import ast.symtab.CSymbol;
import ast.types.CType;
import jscan.symtab.Ident;
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

  // these fields we keep only for consistence.
  // and we may use them when we want to write ast to json, for example.
  // they are being parsed, and defined, but they do not produce the variables.
  // and we ignore them during the code generation.
  // if it was a static-assert - it means that it was parsed and evaluated, and the result is fine.
  // if it was a struct/union/enum - the tagged thing was parsed, defined, etc.
  // if it was a typedef name - the same thing, it was defined in a symtab, for future use.
  // but we're not interested with these fields at all :)
  // they don't affect the stack or produce any code.
  //
  public StaticAssertDeclarationStub staticAssertStub;
  public ArrayList<TypedefDeclarationStub> typedefsStub;
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

  public Declaration(StaticAssertDeclarationStub staticAssertStub, Token pos) {
    this.staticAssertStub = staticAssertStub;
    this.pos = pos;
  }

  public Declaration(ArrayList<TypedefDeclarationStub> typedefsStub, Token pos) {
    this.typedefsStub = typedefsStub;
    this.pos = pos;
  }

  public boolean isAgregate() {
    return agregate != null;
  }

  public boolean isStaticAssertStub() {
    return staticAssertStub != null;
  }

  public boolean isVarlist() {
    return variables != null;
  }

  public boolean isTypedefsStub() {
    return typedefsStub != null;
  }

  // stub nodes

  public static class StaticAssertDeclarationStub {
    public final Expression condition;
    public String msg;

    public StaticAssertDeclarationStub(Expression condition, String msg) {
      this.condition = condition;
      this.msg = msg;
    }
  }

  public static class TypedefDeclarationStub {
    public final Ident name;
    public final CType type;

    public TypedefDeclarationStub(Ident name, CType type) {
      this.name = name;
      this.type = type;
    }
  }

}
