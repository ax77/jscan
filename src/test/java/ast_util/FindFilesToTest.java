package ast_util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import jscan.fio.FileReadKind;
import jscan.fio.FileSearchKind;
import jscan.fio.FileWrapper;

public class FindFilesToTest {

  //  gcc -o __test .\__test.c

  @Ignore
  @Test
  public void test() throws IOException {
    String dir = "C:\\Users\\win10\\Desktop\\single-exec";
    String out = "C:\\Users\\win10\\Desktop\\single-exec\\out\\";
    StringBuilder cmd = new StringBuilder();

    FileWrapper wrapper = new FileWrapper(dir);
    List<FileWrapper> files = wrapper.allFilesInThisDirectory(FileSearchKind.FILES_ONLY);
    for (FileWrapper f : files) {
      String content = f.readToString(FileReadKind.AS_IS);
      content = content.replaceAll("\t", "    ");
      content = content.replaceAll("\r\n", "\n");

      if (content.contains("#")
        || content.contains("include")
        || content.contains("define")
        || content.contains("printf")) {
        continue;
      }

      FileWriter writer = new FileWriter(out + f.getName(), Charset.forName("UTF-8"));
      writer.write(content);
      writer.write("\n");
      writer.close();

      cmd.append("tcc -o " + f.getBasename() + ".exe " + f.getName() + "\n");
    }

    System.out.println(cmd.toString());
  }
}
