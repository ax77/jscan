package ast.parse;

import jscan.sourceloc.SourceLocationRange;

public interface ILocationRange {
  public SourceLocationRange getLocation();

  public String getLocationToStringBegin();

  public String getLocationToStringEnd();

  public int getLocationLineBegin();

  public int getLocationLineEnd();

  public String getLocationFile();
}
