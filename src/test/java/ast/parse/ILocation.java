package ast.parse;

import jscan.sourceloc.SourceLocation;

public interface ILocation {
  public SourceLocation getLocation();

  public String getLocationToString();

  public int getLocationLine();

  public int getLocationColumn();

  public String getLocationFile();
}
