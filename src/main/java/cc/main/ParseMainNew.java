package cc.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.builders.StrConcat;
import cc.parse.Parse;
import cc.tree.TranslationUnit;
import jscan.fio.FileReadKind;
import jscan.fio.FileWrapper;
import jscan.hashed.Hash_all;
import jscan.preproc.preprocess.Scan;
import jscan.sourceloc.SourceLocation;
import jscan.tokenize.Stream;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseMainNew {

  private static final String FILENAME_STUB_FOR_STRING_INPUT = "<string-input>";
  public final ParseOpts[] opts;

  public ParseMainNew(ParseOpts[] opts) {
    this.opts = opts;
    initSymbols();
  }

  private void initSymbols() {
    Hash_all.clearAll();
    KeywordsInits.initIdents();
  }

  public List<Token> tokenizeFile(String filename) throws IOException {
    final String content = new FileWrapper(filename).readToString(FileReadKind.APPEND_LF);
    final List<Token> tokenized = new Stream(filename, content).getTokenlist();
    initSymbols();
    return tokenized;
  }

  public List<Token> tokenizeString(String text) throws IOException {
    final List<Token> tokenized = new Stream(FILENAME_STUB_FOR_STRING_INPUT, text).getTokenlist();
    initSymbols();
    return tokenized;
  }

  public List<Token> preprocessFile(String filename) throws IOException {
    final String content = new FileWrapper(filename).readToString(FileReadKind.APPEND_LF);
    final List<Token> tokenized = new Stream(filename, content).getTokenlist();
    final List<Token> result = makeBuffer(tokenized);
    initSymbols();
    return result;
  }

  public List<Token> preprocessString(String text) throws IOException {
    final List<Token> tokenized = new Stream(FILENAME_STUB_FOR_STRING_INPUT, text).getTokenlist();
    final List<Token> result = makeBuffer(tokenized);
    initSymbols();
    return result;
  }

  public TranslationUnit parseFile(String filename) throws IOException {
    final Parse p = new Parse(preprocessFile(filename));
    final TranslationUnit unit = p.parse_unit();
    initSymbols();
    return unit;
  }

  public TranslationUnit parseString(String text) throws IOException {
    final Parse p = new Parse(preprocessString(text));
    final TranslationUnit unit = p.parse_unit();
    initSymbols();
    return unit;
  }

  private boolean needConcat() {
    return has(ParseOpts.CONCAT_STRINGS);
  }

  private boolean needPredefined() {
    return has(ParseOpts.PREPEND_PREDEFINED_BUFFER);
  }

  private boolean has(ParseOpts opt) {
    for (ParseOpts o : opts) {
      if (o.equals(opt)) {
        return true;
      }
    }
    return false;
  }

  private List<Token> makeBuffer(final List<Token> tokenized) throws IOException {
    final List<Token> buffer = new ArrayList<>();
    if (needPredefined()) {
      buffer.addAll(tokenizePredefined());
    }
    buffer.addAll(tokenized);

    if (needConcat()) {
      return preprocessWithStrConcat(buffer);
    }
    return preprocessNoStrConcat(buffer);
  }

  private List<Token> tokenizePredefined() {

    final String builtinFname = "<built-in>";
    final SourceLocation location = new SourceLocation(builtinFname, 0, 0);

    List<Token> predefined = new Stream(builtinFname, PredefinedBuffer.getPredefinedBuffer()).getTokenlist();
    List<Token> clean = new ArrayList<Token>();

    // TODO: move this logic to Stream.class?
    // special stream-markers, and EOF doesn't need here.
    // but location new and the same for all stream
    for (Token t : predefined) {
      if (t.typeIsSpecialStreamMarks()) {
        continue;
      }
      t.setLocation(location);
      clean.add(t);
    }

    return clean;
  }

  private List<Token> preprocessWithStrConcat(List<Token> input) throws IOException {
    final List<Token> r = preprocessNoStrConcat(input);
    return StrConcat.concatenate(r);
  }

  private List<Token> preprocessNoStrConcat(List<Token> input) throws IOException {
    final List<Token> clean = new ArrayList<>();
    final Scan s = new Scan(input);
    for (;;) {
      Token t = s.get();
      if (t.is(T.TOKEN_EOF)) {
        clean.add(t);
        break;
      }
      clean.add(t);
    }
    return clean;
  }

}
