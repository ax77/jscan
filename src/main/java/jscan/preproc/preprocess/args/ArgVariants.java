package jscan.preproc.preprocess.args;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.Token;

public final class ArgVariants {

  private final List<ArgVariantsEntry> argvariants;

  public ArgVariants(int argsArity) {
    argvariants = new ArrayList<ArgVariantsEntry>();

    for (int i = 0; i != argsArity; i++) {
      ArgVariantsEntry oneArgVariant = new ArgVariantsEntry();
      argvariants.add(oneArgVariant);
    }
  }

  public List<Token> get(int atIndex, int byMask) {
    return argvariants.get(atIndex).get(byMask);
  }

}
