package ast_test;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ast.main.ParseMainNew;
import ast.main.ParseOpts;
import ast.tree.TranslationUnit;
import jscan.fio.FileSearchKind;
import jscan.fio.FileWrapper;
import jscan.fio.IO;

public class RunCcTests {

  @Test
  public void runTestSuite() throws IOException {
    String dir = IO.userDir() + "/cc_tests/suite";
    FileWrapper wrapper = new FileWrapper(dir);
    List<FileWrapper> files = wrapper.allFilesInThisDirectory(FileSearchKind.FILES_ONLY);
    for (FileWrapper f : files) {
      final String fullname = f.getFullname();

      if (fullname.contains("00095")
        || fullname.contains("00096")
        || fullname.contains("00110")
        || fullname.contains("00114")
        || fullname.contains("00209")
        || fullname.contains("00050")) {
        continue;
      }

      System.out.println("C:TEST:" + fullname);

      ParseOpts opts[] = { ParseOpts.CONCAT_STRINGS };
      TranslationUnit unit = new ParseMainNew(opts).parseFile(fullname);

    }
    System.out.println("TEST:OK");
  }

}
