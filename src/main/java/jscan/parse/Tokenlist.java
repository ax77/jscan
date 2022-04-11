package jscan.parse;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.Token;
import jscan.utils.AstParseException;
import jscan.utils.Env;

public class Tokenlist {
  private final List<Token> list;
  private final int size;
  private int offset;

  public Tokenlist(List<Token> list) {
    this.list = list;
    this.size = list.size();
    this.offset = 0;
  }

  public Token next() {
    if (offset >= size) {
      return Env.EOF_TOKEN_ENTRY;
    }
    Token token = list.get(offset);
    offset++;
    return token;
  }

  public Token peek() {
    int s = offset;
    Token t = next();
    offset = s;
    return t;
  }

  public ArrayList<Token> peekCnt(int howMuch) {
    if (howMuch < 0 || howMuch >= 8) {
      throw new AstParseException("you want to look-ahead too much tokens :)");
    }
    final ArrayList<Token> rv = new ArrayList<>();
    final int s = offset;
    for (int i = 0; i < howMuch; i++) {
      rv.add(next());
    }
    offset = s;
    return rv;
  }

  public List<Token> getList() {
    return list;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    if (offset < 0 || offset >= size) {
      throw new AstParseException("set offset overflow.");
    }
    this.offset = offset;
  }

  public boolean hasNext() {
    return offset < size;
  }

  public int getSize() {
    return size;
  }

}
