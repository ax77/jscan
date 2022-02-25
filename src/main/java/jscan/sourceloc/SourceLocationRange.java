package jscan.sourceloc;

import jscan.tokenize.Token;

public class SourceLocationRange {
  private final SourceLocation beginPos;
  private final SourceLocation endPos;

  public SourceLocationRange(SourceLocation beginPos, SourceLocation endPos) {
    this.beginPos = beginPos;
    this.endPos = endPos;
  }

  public SourceLocationRange(Token from, Token to) {
    this.beginPos = new SourceLocation(from);
    this.endPos = new SourceLocation(to);
  }

  public SourceLocation getBeginPos() {
    return beginPos;
  }

  public SourceLocation getEndPos() {
    return endPos;
  }

  @Override
  public String toString() {
    return "SourceLocationRange [beginPos=" + beginPos + ", endPos=" + endPos + "]";
  }

}
