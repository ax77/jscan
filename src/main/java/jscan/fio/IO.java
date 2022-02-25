
package jscan.fio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IO {

  public static byte[] toBytes(File f) throws IOException {
    byte[] b = new byte[(int) f.length()];
    FileInputStream fis = new FileInputStream(f);
    fis.read(b);
    fis.close();
    return b;
  }

  public static List<String> readf(String fname) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(fname));

    List<String> lines = new ArrayList<String>();
    String maybe = br.readLine();
    while (maybe != null) {
      String line = maybe.trim();
      if (!line.isEmpty()) {
        lines.add(line);
      }
      maybe = br.readLine();
    }

    br.close();
    return lines;
  }

  public static String userDir() {
    return System.getProperty("user.dir");
  }

}
