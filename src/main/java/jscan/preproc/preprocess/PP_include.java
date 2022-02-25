package jscan.preproc.preprocess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jscan.fio.FileWrapper;
import jscan.hashed.Hash_once;
import jscan.hashed.Hash_stream;
import jscan.main.Env;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import jscan.utils.Normalizer;

public final class PP_include implements PP_directive {

  private final Scan scanner;

  public PP_include(Scan s) {
    scanner = s;
  }

  private List<Token> getIncludeLine(Token pp) {

    List<Token> toklist = new ArrayList<Token>();

    for (;;) {
      Token t = scanner.pop();
      if (t.isNewLine() || t.typeIsSpecialStreamMarks()) { // TODO:
        if (!t.typeIsSpecialStreamMarks()) { // if file not have LF at EOF
          toklist.add(t);
        }
        break;
      }
      toklist.add(t);
    }

    if (toklist.isEmpty()) {
      throw new ScanExc(pp.loc() + " error: empty include filename");
    }

    return toklist;
  }

  private String getInclueFilenameAsStringNoCheck(Token pp) throws IOException {

    List<Token> includeLineNoExpanded = getIncludeLine(pp);
    Token firstInIncludeLineTmp = includeLineNoExpanded.get(0);
    boolean needExpansion = firstInIncludeLineTmp.ofType(T.TOKEN_IDENT);

    List<Token> includeLineExpanded = new ArrayList<Token>();
    if (needExpansion) {
      Scan incscan = new Scan(includeLineNoExpanded);
      for (;;) {
        Token t = incscan.get();
        if (t == Env.EOF_TOKEN_ENTRY) {
          break;
        }
        includeLineExpanded.add(t);
      }
    } else {
      includeLineExpanded = includeLineNoExpanded;
    }

    if (includeLineExpanded.isEmpty()) {
      throw new ScanExc(pp.loc() + " error: empty include filename");
    }

    StringBuilder sb = new StringBuilder();

    for (Token t : includeLineExpanded) {
      sb.append(t.getValue());
    }

    return sb.toString();
  }

  @Override
  public boolean scan(Token pp) throws IOException {

    String fname = getInclueFilenameAsStringNoCheck(pp);

    boolean isAngled = fname.charAt(0) == '<';

    checkFname(pp, fname, isAngled);
    fname = Normalizer.unquote(fname);

    String fullpath = "";

    if (Normalizer.isAbsolutePath(fname)) {
      fullpath = fname;
    }

    else {
      if (isAngled) {
        fullpath = getAngledFname(pp, fname);

      } else {

        FileWrapper wrapper = new FileWrapper(pp.getFilename());
        FileWrapper construct = wrapper.constructQuotedInclude(fname);

        fullpath = construct.getFullname();

        File f = new File(fullpath);
        if (!f.exists()) {
          fullpath = getAngledFname(pp, fname);
        }

      }
    }

    File f = new File(fullpath);
    if (!f.exists() || f.isDirectory()) {
      throw new ScanExc(pp.loc() + " file doe's not exists. " + fname);
    }

    pushTokensFromInclude(fullpath);
    return true;

  }

  private void checkFname(Token pp, String fname, boolean angled) {

    if (fname.isEmpty()) {
      throw new RuntimeException(pp.loc() + " empty file-name.");
    }

    int size = fname.length();

    if (angled) {

      if (!fname.startsWith("<")) {
        throw new RuntimeException(pp.loc() + " angled include filename doe's not conforming <file.h> form");
      }
      if (!fname.endsWith(">")) {
        throw new RuntimeException(pp.loc() + " angled include filename doe's not conforming <file.h> form");
      }

      for (int i = 0; i < size; i++) {
        char c = fname.charAt(i);
        if (i > 0 && i < size - 1) {
          if (c == '<' || c == '>') {
            throw new RuntimeException(pp.loc() + " angled include filename doe's not conforming <file.h> form");
          }
        }
      }

    } else {

      for (int i = 0; i < size; i++) {
        char c = fname.charAt(i);
        if (i > 0 && i < size - 1) {
          if (c == '\"') {
            throw new RuntimeException(pp.loc() + " quoted include filename doe's not conforming \"file.h\" form");
          }
        }
      }

    }

  }

  private void pushTokensFromInclude(String whatwefind) throws IOException {

    if (Hash_once.is_once(whatwefind)) {
      return;
    }

    List<Token> tokenlist = Hash_stream.getTokenlist(whatwefind);

    for (int II = tokenlist.size(); --II >= 0;) {
      Token t = tokenlist.get(II);
      if (t != Env.EOF_TOKEN_ENTRY) {
        scanner.push(t);
      }
    }
  }

  private String getAngledFname(Token pp, String fname) {

    // -I
    for (String ifnames : PpEnv.INC_ANGLE_DIRS) {
      String searchname = Normalizer.normalize(ifnames + PpEnv.HC_PATH_SEP + fname);
      File f = new File(searchname);
      if (f.exists()) {
        return searchname;
      }
    }

    // usr/include
    for (String ifnames : PpEnv.INC_DEFAULT_ANGLE_DIRS) {
      String searchname = Normalizer.normalize(ifnames + PpEnv.HC_PATH_SEP + fname);
      File f = new File(searchname);
      if (f.exists()) {
        return searchname;
      }
    }

    throw new ScanExc(pp.loc() + " file doe's not exists. " + fname);
  }
}
