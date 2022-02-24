package preproc.preprocess;

import java.util.ArrayList;
import java.util.List;

import tokenize.Fcategory;
import tokenize.Token;

public class ArgVariantsEntry {
  private List<Token> arg_formal_scanned;
  private List<Token> arg_formal_stringized;
  private List<Token> arg_formal_unscanned;

  public ArgVariantsEntry() {
    this.arg_formal_scanned = new ArrayList<Token>(0);
    this.arg_formal_stringized = new ArrayList<Token>(0);
    this.arg_formal_unscanned = new ArrayList<Token>(0);
  }

  public List<Token> get(int mask) {
    int fscanned = Fcategory.formal | Fcategory.scanned;
    int fstringized = Fcategory.formal | Fcategory.stringized;
    int funscanned = Fcategory.formal | Fcategory.unscanned;

    if ((mask & fscanned) == fscanned) {
      return arg_formal_scanned;
    }

    if ((mask & fstringized) == fstringized) {
      return arg_formal_stringized;
    }

    if ((mask & funscanned) == funscanned) {
      return arg_formal_unscanned;
    }

    throw new ScanExc("unknown mask: " + mask);
  }
}
