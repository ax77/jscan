package jscan.fio;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import jscan.utils.Normalizer;

//from         = "C:\\//_tmp7\\\\astyle.options"
//name         = "astyle.options"
//basename     = "astyle"
//fullname     = "C:/_tmp7/astyle.options"
//path         = "C:/_tmp7/"
//extension    = ".options"

public class FileWrapper {
  private final String from;
  private final String name;
  private final String basename;
  private final String fullname;
  private final String path;
  private final String extension;
  private final boolean isAbsolutePath;
  private final boolean isDirectory;
  private final boolean isExists;
  private final long size;

  public FileWrapper(String from) {
    this.from = from;

    final String normalized = Normalizer.normalize(from).trim();
    final File file = new File(normalized);

    name = getNameWithExtension(normalized).trim();
    basename = getBasenameFromName(name).trim();
    fullname = normalized;
    path = getPathName(normalized);
    extension = getExtension(normalized);

    isAbsolutePath = Normalizer.isAbsolutePath(normalized);
    isDirectory = file.isDirectory();
    isExists = file.exists();
    size = file.length();
  }

  private void assertWithMessage(String m) {
    throw new RuntimeException("error:" + m);
  }

  public void assertIsExists() {
    if (!isExists) {
      assertWithMessage("file doe's not exists: " + fullname);
    }
  }

  public void assertIsFile() {
    if (isDirectory) {
      assertWithMessage("not a file: " + fullname);
    }
  }

  public void assertIsDirectory() {
    if (!isDirectory) {
      assertWithMessage("not a directory: " + fullname);
    }
  }

  public String readToString(FileReadKind kind) throws IOException {
    assertIsExists();
    assertIsFile();

    byte[] encoded = Files.readAllBytes(Paths.get(fullname));

    StringBuilder sb = new StringBuilder();
    sb.append(new String(encoded, Charset.defaultCharset()));

    if (kind == FileReadKind.APPEND_LF) {
      sb.append("\n");
    }

    return sb.toString();
  }

  public FileWrapper constructQuotedInclude(String quotedIncludeFileName) {
    final String unquoted = Normalizer.unquote(quotedIncludeFileName);

    if (Normalizer.isAbsolutePath(unquoted)) {
      return new FileWrapper(unquoted);
    }

    StringBuilder fullpathConstructor = new StringBuilder();
    fullpathConstructor.append(getPath());
    fullpathConstructor.append(unquoted);

    return new FileWrapper(fullpathConstructor.toString());
  }

  private String getBasenameFromName(String from) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < from.length(); i++) {
      char c = from.charAt(i);
      if (c == '.') {
        break;
      }
      sb.append(c);
    }
    return sb.toString();
  }

  private String getExtension(final String from) {

    char s[] = from.toCharArray();
    int len = s.length;

    if (len == 0) {
      return "";
    }

    int ito = len;
    for (; --ito >= 0;) {
      char c = s[ito];
      if (c == '.') {
        break;
      }
      if (c == '\\' || c == '/') {
        return "";
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int i = ito + 1; i < len; i++) {
      sb.append(s[i]);
    }

    return sb.toString();
  }

  private String getNameWithExtension(final String from) {

    char s[] = from.toCharArray();
    int len = s.length;

    if (len == 0) {
      return "";
    }

    int ito = len;
    for (; --ito >= 0;) {
      char c = s[ito];
      if (c == '\\' || c == '/') {
        break;
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int i = ito + 1; i < len; i++) {
      sb.append(s[i]);
    }

    return sb.toString();
  }

  private String getPathName(final String from) {

    String n = Normalizer.normalize(from);
    char s[] = n.toCharArray();

    int ito = s.length;

    for (; --ito >= 0;) {
      char c = s[ito];
      if (c == '/') {
        break;
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < ito; i++) {
      sb.append(s[i]);
    }

    return sb.toString() + "/"; // append last separator
  }

  public List<FileWrapper> allFilesInThisDirectory(FileSearchKind search) {
    List<FileWrapper> files = new ArrayList<FileWrapper>();

    // TODO:error, or silence?
    if (!isDirectory) {
      return files;
    }

    search(new File(fullname), files, /*recursive*/false, search);
    return files;
  }

  public List<FileWrapper> allFilesInThisDirectoryRecirsively(FileSearchKind search) {
    List<FileWrapper> files = new ArrayList<FileWrapper>();

    // TODO:error, or silence?
    if (!isDirectory) {
      return files;
    }

    search(new File(fullname), files, /*recursive*/true, search);
    return files;
  }

  private void search(final File folder, List<FileWrapper> result, boolean recursive, FileSearchKind search) {
    for (final File f : folder.listFiles()) {

      if (f.isDirectory()) {

        if (search == FileSearchKind.FILES_AND_FOLDERS || search == FileSearchKind.FOLDERS_ONLY) {
          final FileWrapper wrapper = new FileWrapper(f.getAbsolutePath());
          result.add(wrapper);
        }

        if (recursive) {
          search(f, result, recursive, search);
        }

      }

      if (f.isFile()) {
        if (search == FileSearchKind.FILES_AND_FOLDERS || search == FileSearchKind.FILES_ONLY) {
          final FileWrapper wrapper = new FileWrapper(f.getAbsolutePath());
          result.add(wrapper);
        }
      }

    }
  }

  public String getFrom() {
    return from;
  }

  public String getName() {
    return name;
  }

  public String getBasename() {
    return basename;
  }

  public String getFullname() {
    return fullname;
  }

  public String getPath() {
    return path;
  }

  public String getExtension() {
    return extension;
  }

  public boolean isAbsolutePath() {
    return isAbsolutePath;
  }

  public boolean isDirectory() {
    return isDirectory;
  }

  public boolean isExists() {
    return isExists;
  }

  public long getSize() {
    return size;
  }

}
