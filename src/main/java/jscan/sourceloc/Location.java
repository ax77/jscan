package jscan.sourceloc;

import jscan.tokenize.Token;

public interface Location {
  SourceLocation getLocation();

  String getLocationToString();

  /// we store the whole token, because the token
  /// itself has a location inside, and we also can see
  /// its value, it is helpful for debug.
  ///
  Token getBeginPos();
}
