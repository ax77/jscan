package jscan.preproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jscan.hashed.Hash_stream;
import jscan.preproc.preprocess.Scan;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import jscan.utils.Env;

public class TokenCmp {
  private final String srcFname;
  private final String expFname;

  public TokenCmp(String srcFname, String expFname) {
    this.srcFname = srcFname;
    this.expFname = expFname;
  }

  public void compare() throws IOException {

    //String srcToPP = FIO.readFile(srcFname, Charset.defaultCharset());
    //String srcToLX = FIO.readFile(expFname, Charset.defaultCharset());

    List<Token> lexed = new ArrayList<Token>();
    for (Token t : Hash_stream.getTokenlist(expFname)) { //  new Stream(expFname, srcToLX).getTokenlist()
      if (t.typeIsSpecialStreamMarks()) {
        continue;
      }
      lexed.add(t);
    }

    Scan s = new Scan(Hash_stream.getTokenlist(srcFname)); // new Stream(srcFname, srcToPP).getTokenlist()
    List<Token> preprocessed = new ArrayList<Token>();
    for (;;) {
      Token t = s.get();
      if (t == Env.EOF_TOKEN_ENTRY) {
        break;
      }
      preprocessed.add(t);
    }

    int bound = preprocessed.size();

    if (bound != lexed.size()) {
      throw new RuntimeException(
          srcFname + "error: size diff...Preprocessed: " + bound + "; but lexed: " + lexed.size());
    }

    for (int j = 0; j < bound; j++) {
      Token ppTok = preprocessed.get(j);
      Token lxTok = lexed.get(j);

      String ppVal = ppTok.getValue();
      String lxVal = lxTok.getValue();
      if (!ppVal.equals(lxVal)) {
        throw new RuntimeException(
            ppTok.loc() + " error : value diff: expected[" + lxVal + "] but was : [" + ppVal + "]");
      }

      T pptype = ppTok.getType();
      T lxtype = lxTok.getType();
      if (!pptype.equals(lxtype)) {
        throw new RuntimeException(
            "Type diff: expected[" + lxtype.toString() + "] but was : [" + pptype.toString() + "]");
      }
    }

  }

}
