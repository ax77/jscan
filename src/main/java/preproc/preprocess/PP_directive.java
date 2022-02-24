package preproc.preprocess;

import java.io.IOException;

import tokenize.Token;

public interface PP_directive {

  public boolean scan(Token start) throws IOException;

}
