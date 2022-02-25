package jscan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jscan.fio.FileReadKind;
import jscan.fio.FileSearchKind;
import jscan.fio.FileWrapper;
import jscan.utils.Normalizer;

/*
    //I)
    String path = "src/test/resources";
    File file = new File(path);
    String absolutePath = file.getAbsolutePath();
    System.out.println(absolutePath);
    assertTrue(absolutePath.endsWith("src/test/resources"));
     
    //II)
    String resourceName = "example_resource.txt";
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(resourceName).getFile());
    String absolutePath = file.getAbsolutePath();
    System.out.println(absolutePath);
    assertTrue(absolutePath.endsWith("/example_resource.txt"));
 
 */

//from         = "C:\\//_tmp7\\\\astyle.options"
//name         = "astyle.options"
//basename     = "astyle"
//fullname     = "C:/_tmp7/astyle.options"
//path         = "C:/_tmp7/"
//extension    = ".options"

public class TestFio {

  @Test
  public void testExists() {

    String path = "src/test/resources/example.txt";
    File file = new File(path);
    String absolutePath = file.getAbsolutePath();

    FileWrapper wrapper = new FileWrapper(absolutePath);
    assertTrue(wrapper.isExists());
    assertFalse(wrapper.isDirectory());
    assertEquals(0, wrapper.getSize());

    assertEquals("example.txt", wrapper.getName());
    assertEquals("example", wrapper.getBasename());
    assertEquals("txt", wrapper.getExtension());

    assertTrue(wrapper.getPath().endsWith("/src/test/resources/"));
  }

  @Test
  public void testSize() {

    String path = "src/test/resources/size_3bytes.txt";
    File file = new File(path);
    String absolutePath = file.getAbsolutePath();

    FileWrapper wrapper = new FileWrapper(absolutePath);
    assertTrue(wrapper.isExists());
    assertFalse(wrapper.isDirectory());
    assertEquals(3, wrapper.getSize());

  }

  @Test
  public void testWinPath() {

    String path = "src\\test\\resources\\//example.txt";
    File file = new File(path);
    String absolutePath = file.getAbsolutePath();

    FileWrapper wrapper = new FileWrapper(absolutePath);
    assertTrue(wrapper.isExists());
    assertFalse(wrapper.isDirectory());
    assertEquals(0, wrapper.getSize());

    assertTrue(wrapper.getFullname().endsWith("/src/test/resources/example.txt"));

  }

  @Test
  public void testMock() {
    final String given = "some path//\\and-some-file.txt";
    FileWrapper wrapper = new FileWrapper(given);

    assertEquals("some path/and-some-file.txt", wrapper.getFullname());
    assertEquals("and-some-file.txt", wrapper.getName());
    assertEquals("txt", wrapper.getExtension());
    assertEquals("and-some-file", wrapper.getBasename());
    assertEquals("some path/", wrapper.getPath());
  }

  @Test
  public void testListing() {
    String path = "src/test/resources/f01";
    File file = new File(path);
    String absolutePath = file.getAbsolutePath();

    FileWrapper wrapper = new FileWrapper(absolutePath);

    List<FileWrapper> filesListing = wrapper.allFilesInThisDirectory(FileSearchKind.FILES_ONLY);
    assertEquals(2, filesListing.size());

    List<FileWrapper> foldersListing = wrapper.allFilesInThisDirectory(FileSearchKind.FOLDERS_ONLY);
    assertEquals(1, foldersListing.size());

    List<FileWrapper> fullListing = wrapper.allFilesInThisDirectory(FileSearchKind.FILES_AND_FOLDERS);
    assertEquals(3, fullListing.size());
  }

  @Test
  public void testListingRecursive() {
    String path = "src/test/resources/test_recursive_search";
    File file = new File(path);
    String absolutePath = file.getAbsolutePath();

    FileWrapper wrapper = new FileWrapper(absolutePath);

    List<FileWrapper> filesListing = wrapper.allFilesInThisDirectoryRecirsively(FileSearchKind.FILES_ONLY);
    assertEquals(5, filesListing.size());

    List<FileWrapper> foldersListing = wrapper.allFilesInThisDirectoryRecirsively(FileSearchKind.FOLDERS_ONLY);
    assertEquals(7, foldersListing.size());

    List<FileWrapper> fullListing = wrapper.allFilesInThisDirectoryRecirsively(FileSearchKind.FILES_AND_FOLDERS);
    assertEquals(filesListing.size() + foldersListing.size(), fullListing.size());

  }

  @Test
  public void testListingSimple() {
    String path = "src/test/resources/f01";
    File file = new File(path);
    String absolutePath = file.getAbsolutePath();

    FileWrapper wrapper = new FileWrapper(absolutePath);

    List<FileWrapper> filesListing = wrapper.allFilesInThisDirectoryRecirsively(FileSearchKind.FILES_ONLY);
    for (FileWrapper f : filesListing) {
      //System.out.printf("%-8.2f:%s\n", (float) f.getSize() / 1024, f.getFullname());
      assertTrue(f.isExists());
      assertFalse(f.isDirectory());
    }
  }

  @Test
  public void testReadNoLF() throws IOException {
    String path = "src/test/resources/test_read.txt";
    File file = new File(path);
    String absolutePath = file.getAbsolutePath();

    FileWrapper wrapper = new FileWrapper(absolutePath);
    String source = wrapper.readToString(FileReadKind.AS_IS);

    assertEquals("abcde", source);
  }

  @Test
  public void testReadWithLF() throws IOException {
    String path = "src/test/resources/test_read.txt";
    File file = new File(path);
    String absolutePath = file.getAbsolutePath();

    FileWrapper wrapper = new FileWrapper(absolutePath);
    String source = wrapper.readToString(FileReadKind.APPEND_LF);

    assertEquals("abcde\n", source);
  }

  @Test
  public void testConstructQuotedIncludeMock() {
    FileWrapper mainFile = new FileWrapper("C:\\project\\test.c");
    String whatIncludeFromMainFile = "header.h";

    // directory:
    // project
    //    project/test.c
    //    project/header.h
    //
    // test.c content: #include "header.h"

    FileWrapper constructs = mainFile.constructQuotedInclude(whatIncludeFromMainFile);
    assertEquals("C:/project/header.h", constructs.getFullname());
  }

  @Test
  public void testConstructQuotedIncludeAbsoluteUnixMock() {
    FileWrapper mainFile = new FileWrapper("/usr/include/test.c");
    String whatIncludeFromMainFile = "../header.h";

    // directory:
    // project
    //    project/test.c
    //    project/header.h
    //
    // test.c content: #include "header.h"

    FileWrapper constructs = mainFile.constructQuotedInclude(whatIncludeFromMainFile);
    assertEquals("/usr/header.h", constructs.getFullname());
  }

  @Test
  public void testConstructQuotedIncludeAbsoluteWinMock() {
    FileWrapper mainFile = new FileWrapper("C:\\project\\test.c");
    String whatIncludeFromMainFile = "../header.h";

    // directory:
    // project
    //    project/test.c
    //    project/header.h
    //
    // test.c content: #include "header.h"

    FileWrapper constructs = mainFile.constructQuotedInclude(whatIncludeFromMainFile);
    assertEquals("C:/header.h", constructs.getFullname());
  }

  private String normalize(String string) {
    return Normalizer.normalize(string);
  }

  @Test
  public void testMockNormalize1() {

    assertEquals("c:/project/test/header.h", normalize("c:/project//test/./././././././header.h"));
    assertEquals("project/header.h", normalize("./project/test/../header.h"));
    assertEquals("C:/header.h", normalize("C:/project/test/../../../../header.h"));
    assertEquals("../header.h", normalize("../project/test/../../header.h"));
    assertEquals("C:/header.h", normalize("C:/project/../header.h"));
    assertEquals("/usr/header.h", normalize("/usr/include/../header.h"));

    assertEquals("../header.h", normalize("././././../header.h"));
    assertEquals("C:/header.h", normalize("C:\\../../../../../../header.h"));

    assertEquals("header.h", normalize("./header.h"));
    assertEquals("header.h", normalize("header.h"));

    assertEquals("header.h", normalize(".\\//\\//\\//\\//header.h"));
    assertEquals("src/header.h", normalize("src//\\./././header.h"));

    assertEquals("D:/p2/core/cache/binary", normalize("D:\\..\\..\\.\\p2\\core\\cache\\binary"));
    assertEquals("C:/file.c", normalize("C:\\file.c"));
    assertEquals("C:/file.c", normalize("C://file.c"));
    assertEquals("C:/file.c", normalize("C:/file.c"));

    assertEquals("C:/testing.txt", normalize("C:\\temp\\test\\..\\..\\testing.txt"));
    assertEquals("C:/more/testing/test.txt", normalize("C:\\temp\\test\\..\\../testing\\..\\more/testing\\test.txt"));

    assertEquals("/usr/local/include/stdio.h", normalize("/usr/local//include//\\stdio.h"));
    assertEquals("", normalize("./"));
    assertEquals("/", normalize("/"));
    assertEquals("../", normalize("../"));

    assertEquals(normalize("/foo//             "), "/foo/    ".trim());
    assertEquals(normalize("/foo/./            "), "/foo/    ".trim());
    assertEquals(normalize("/foo/../bar        "), "/bar     ".trim());
    assertEquals(normalize("/foo/../bar/       "), "/bar/    ".trim());
    assertEquals(normalize("/foo/../bar/../baz "), "/baz     ".trim());
    assertEquals(normalize("//foo//./bar       "), "/foo/bar ".trim());
    assertEquals(normalize("foo/bar/..         "), "foo/     ".trim());
    assertEquals(normalize("foo/../bar         "), "bar      ".trim());

  }

  @Test
  public void testUnquote() {
    assertEquals("file", Normalizer.unquote("file"));
    assertEquals("file", Normalizer.unquote("<file>"));
    assertEquals("file", Normalizer.unquote("\"file\""));

    assertEquals("project/file.c", Normalizer.unquote("<project\\//file.c>"));
    assertEquals("project/file.c", Normalizer.unquote("\"project\\//file.c\""));
  }

  @Test
  public void generateNormalized() {
    List<String> pathes = new ArrayList<String>();
    pathes.add("./");
    pathes.add("/");
    pathes.add("../");

    StringBuilder sb = new StringBuilder();
    String q2 = "\"";

    for (String s : pathes) {
      String normalized = Paths.get(s).normalize().toString();
      String need = normalized.replace('\\', '/');

      sb.append("assertEquals(" + q2 + need + q2 + ", normalize(" + q2 + s + q2 + "));\n");
    }

    //System.out.println(sb.toString());

  }

}
