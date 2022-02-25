package ast.main;

import java.io.IOException;

import ast.parse.Parse;
import ast.unit.TranslationUnit;
import jscan.parse.Tokenlist;

public interface ParserMainApi {

  public Tokenlist preprocess() throws IOException;

  public Parse initiateParse() throws IOException;

  public TranslationUnit parseUnit() throws IOException;

}
