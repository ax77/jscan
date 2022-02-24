package main;

import java.util.List;

import preproc.preprocess.ScanExc;
import tokenize.Token;

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

  public List<Token> getList() {
    return list;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    if (offset < 0 || offset >= size) {
      throw new ScanExc("set offset overflow.");
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
