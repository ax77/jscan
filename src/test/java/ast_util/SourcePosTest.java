package ast_util;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import ast.main.ParserMain;
import ast.parse.Parse;
import ast.parse.ParseExpression;
import ast.tree.Expression;
import jscan.fio.IO;
import jscan.symtab.Ident;
import jscan.symtab.ScopeLevels;
import jscan.tokenize.T;

public class SourcePosTest {

  /// error[E0277]: the trait bound `&[{integer}]: MyIterator<char>` is not satisfied
  ///    --> <anon>:14:5
  ///     |
  ///  14 |     iterate_chars(&[1, 2, 3][..]);
  ///     |     ^^^^^^^^^^^^^ an iterator over elements of type `char` cannot be built from a collection of type `&[{integer}]`
  ///     |
  ///
  ///
  /// error[E0507]: cannot move out of `self.lines` which is behind a mutable reference
  ///    --> src\main.rs:131:21
  ///     |
  /// 131 |         for line in self.lines {
  ///     |                     ^^^^^^^^^^ move occurs because `self.lines` has type `Vec<AsmLine>`, which does not implement the `Copy` trait
  ///     |

  private String pos(List<String> lines, int line, int column) {
    String need = lines.get(line - 1).replace("\t", "    ");
    String pad = "";
    for (int i = 0; i < column - 1; i += 1) {
      pad += " ";
    }
    pad += "^";
    String result = need + "\n" + pad;
    return result;
  }

  @Ignore
  @Test
  public void test() throws IOException {
    String dir = IO.userDir();
    String fname = dir + "/error.c";
    List<String> lines = IO.readf(fname);
    System.out.println(pos(lines, 14, 14));
  }

  /// initializer
  ///     : '{' initializer_list '}'
  ///     | '{' initializer_list ',' '}'
  ///     | assignment_expression
  ///     ;
  /// 
  /// initializer_list
  ///     : designation initializer
  ///     | initializer
  ///     | initializer_list ',' designation initializer
  ///     | initializer_list ',' initializer
  ///     ;
  /// 
  /// designation
  ///     : designator_list '='
  ///     ;
  /// 
  /// designator_list
  ///     : designator
  ///     | designator_list designator
  ///     ;
  /// 
  /// designator
  ///     : '[' constant_expression ']'
  ///     | '.' IDENTIFIER
  ///     ;

  static class Initializer {
    InitializerList braced;
    Expression simple;

    public Initializer(InitializerList braced) {
      this.braced = braced;
    }

    public Initializer(Expression simple) {
      this.simple = simple;
    }

    public boolean isBraced() {
      return braced != null;
    }

    public boolean isSimple() {
      return simple != null;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      if (isBraced()) {
        sb.append("{ ");
        sb.append(braced.toString());
        sb.append(" }");
      } else {
        sb.append(simple.toString());
      }
      return sb.toString();
    }
  }

  static class InitializerList {
    final Designation designation;
    final Initializer initializer;
    final InitializerList next;

    public InitializerList() {
      this.designation = null;
      this.initializer = null;
      this.next = null;
    }

    public InitializerList(Designation designation, Initializer initializer, InitializerList next) {
      this.designation = designation;
      this.initializer = initializer;
      this.next = next;
    }

    public boolean isDesignated() {
      return designation != null;
    }

    public boolean isBraced() {
      return next != null;
    }

    public boolean isEmpty() {
      return designation == null && initializer == null && next == null;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      if (next != null) {
        sb.append(next.toString());
        sb.append(", ");
      }
      if (isDesignated()) {
        sb.append(designation.toString());
        sb.append("=");
      }
      if (initializer != null) {
        sb.append(initializer.toString());
      }
      return sb.toString();
    }
  }

  static class Designation {
    Ident field;
    Expression arrlen;
    Designation next;

    public Designation(Expression arrlen, Designation next) {
      this.arrlen = arrlen;
      this.next = next;
    }

    public Designation(Ident field, Designation next) {
      this.field = field;
      this.next = next;
    }

    public boolean isStructDesg() {
      return field != null;
    }

    public boolean isArrayDesg() {
      return arrlen != null;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      if (next != null) {
        sb.append(next.toString());
      }
      if (isArrayDesg()) {
        sb.append("[" + arrlen + "]");
      }
      if (isStructDesg()) {
        sb.append("." + field.toString());
      }
      return sb.toString();
    }
  }

  /// initializer
  ///     : '{' initializer_list '}'
  ///     | '{' initializer_list ',' '}'
  ///     | assignment_expression
  ///     ;

  private Initializer parseInit(Parse p) {
    if (p.is(T.T_LEFT_BRACE)) {
      return new Initializer(parseBraced(p));
    }
    return parseSimple(p);
  }

  /// initializer_list
  ///     : designation initializer
  ///     | initializer
  ///     | initializer_list ',' designation initializer
  ///     | initializer_list ',' initializer
  ///     ;

  private InitializerList parseBraced(Parse p) {
    p.lbrace();

    // empty one
    if (p.is(T.T_RIGHT_BRACE)) {
      p.rbrace();
      return new InitializerList();
    }

    InitializerList init = new InitializerList(parseDesg(p), parseInit(p), null);
    while (p.is(T.T_COMMA)) {
      p.comma();
      if (p.is(T.T_RIGHT_BRACE)) {
        break;
      }
      init = new InitializerList(parseDesg(p), parseInit(p), init);
    }

    p.rbrace();
    return init;
  }

  /// designation
  ///     : designator_list '='
  ///     ;
  /// 
  /// designator_list
  ///     : designator
  ///     | designator_list designator
  ///     ;
  /// 
  /// designator
  ///     : '[' constant_expression ']'
  ///     | '.' IDENTIFIER
  ///     ;

  private Designation parseDesg(Parse p) {

    Designation d = null;

    while (p.is(T.T_LEFT_BRACKET) || p.is(T.T_DOT)) {
      if (p.is(T.T_LEFT_BRACKET)) {
        p.lbracket();
        d = new Designation(new ParseExpression(p).e_const_expr(), d);
        p.rbracket();
      }

      else if (p.is(T.T_DOT)) {
        p.move();
        Ident field = p.getIdent();
        d = new Designation(field, d);
      }
    }

    if (d != null) {
      p.checkedMove(T.T_ASSIGN);
    }
    return d;
  }

  private Initializer parseSimple(Parse p) {
    final Expression assignment = new ParseExpression(p).e_assign();
    return new Initializer(assignment);
  }

  @Test
  public void inits() throws IOException {
    String dir = System.getProperty("user.dir");
    String txt = "{ .a.b.c = {1,2,3,}, [1].f = 2, .a[1] = 3, { }, [11][22][33] = {11,22, .a={11,22,33}} }";
    Parse p = new Parse(new ParserMain(new StringBuilder(txt)).preprocess());
    p.pushscope(ScopeLevels.FILE_SCOPE);

    Initializer init = parseInit(p);
    System.out.println(init);
  }

}
