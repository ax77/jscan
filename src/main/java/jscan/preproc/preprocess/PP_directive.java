package jscan.preproc.preprocess;

import java.io.IOException;

import jscan.tokenize.Token;

public interface PP_directive {

  public boolean scan(Token start) throws IOException;

}
