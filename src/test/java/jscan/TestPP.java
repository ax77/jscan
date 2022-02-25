package jscan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jscan.hashed.Hash_all;
import jscan.preproc.TokenCmp;
import jscan.preproc.preprocess.PpEnv;

public class TestPP {

  private static final String PP_TEST_DIR_MAIN = "pp_tests/__unload";

  private void addAngleDir(String p) throws IOException {
    PpEnv.INC_ANGLE_DIRS.add(p);
  }

  @SuppressWarnings("unused")
  private void addDefaultAngleDir(String p) throws IOException {
    PpEnv.INC_DEFAULT_ANGLE_DIRS.add(p);
  }

  private List<String> listFilesForFolder(final File folder) {
    List<String> ret = new ArrayList<String>();
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isDirectory()) {
        //listFilesForFolder(fileEntry);
        ret.add(fileEntry.getAbsolutePath());
      }
    }
    return ret;
  }

  private List<String> listFiles(final File folder) {
    List<String> ret = new ArrayList<String>();
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isDirectory()) {
        continue;
      }
      if (fileEntry.getAbsolutePath().endsWith(".txt")) {
        continue;
      }
      ret.add(fileEntry.getAbsolutePath());
    }
    return ret;
  }

  // if we want ignore this cases at now. 
  // for fast...
  private List<String> ignoreFolders() {
    List<String> ret = new ArrayList<String>();
    ret.add("diff");
    ret.add("b_c99_meta");

    //TODO:
    //this in settings...
    ret.add("b_boost_pp_test");
    ret.add("b_chaos_pp_test");
    ret.add("forc");
    ret.add("perf_middle");
    return ret;
  }

  private void runTests() throws IOException {

    String maindir = PP_TEST_DIR_MAIN;
    List<String> ret = listFilesForFolder(new File(maindir));

    int index = 1;
    List<String> ignoreCases = ignoreFolders();

    // if we want only one folder to test at now...
    String thisOne = "";

    for (String f : ret) {

      if (thisOne.isEmpty()) {
        boolean ignoreFlag = false;
        for (String ignoreFold : ignoreCases) {
          if (f.endsWith(ignoreFold)) {
            ignoreFlag = true;
            break;
          }
        }
        if (ignoreFlag) {
          continue;
        }
      } else {
        if (!f.endsWith(thisOne)) {
          continue;
        }
      }

      if (f.endsWith("_include")) {
        Hash_all.clearAll();

        String fnametmp = f + "\\" + "__test.c";
        System.out.printf("TEST %03d : %s\n", index++, fnametmp);

        TokenCmp cmp = new TokenCmp(fnametmp, f + "\\__test_exp.txt");
        cmp.compare();

      } else {

        List<String> files = listFiles(new File(f));
        for (String fname : files) {
          Hash_all.clearAll();

          boolean extIsOk = fname.endsWith(".c") || fname.endsWith(".txt");
          if (!extIsOk) {
            continue;
          }

          String noext = fname.substring(0, fname.length() - 2);
          System.out.printf("TEST %03d : %s\n", index++, noext);

          TokenCmp cmp = new TokenCmp(fname, noext + "_exp.txt");
          cmp.compare();
        }
      }

    }
    System.out.println("TEST OK");

  }

  @Test
  public void testPP() throws IOException {

    final String dir = System.getProperty("user.dir");

    addAngleDir(dir + "\\pp_tests\\__include");
    addAngleDir(dir + "\\pp_tests\\__include\\chaos_pp\\");
    addAngleDir(dir + "\\pp_tests\\__include\\boost_pp\\");
    addAngleDir(dir + "\\pp_tests\\__include\\vrm_pp\\");
    addAngleDir(dir + "\\pp_tests\\__unload\\a_test_include\\");
    addAngleDir(dir + "\\pp_tests\\__unload\\a_test_expansion_include\\");

    long start = System.currentTimeMillis();
    runTests();

    long end = System.currentTimeMillis();
    float sec = (end - start) / 1000F;

    System.out.println(sec + " seconds");

  }

}
