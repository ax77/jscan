package ast.errors;

public class ParseException extends RuntimeException {

  private static final long serialVersionUID = 1282362108596282528L;

  public ParseException(String msg) {
    super(msg);
  }
}
