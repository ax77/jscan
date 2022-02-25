package jscan.hashed;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jscan.fio.FileReadKind;
import jscan.fio.FileWrapper;
import jscan.tokenize.Stream;
import jscan.tokenize.Token;

public final class Hash_stream {
  private static Map<String, Stream> streamHash = new HashMap<String, Stream>();

  public static void clear() {
    streamHash.clear();
  }

  public static List<Token> getTokenlist(String filename) throws IOException {
    List<Token> source = getHashedStream(filename).getTokenlist();
    return source;
  }

  public static Stream getHashedStream(String filename) throws IOException {
    if (!streamHash.containsKey(filename)) {

      FileWrapper wrapper = new FileWrapper(filename);
      wrapper.assertIsExists();
      wrapper.assertIsFile();

      String source = wrapper.readToString(FileReadKind.APPEND_LF); //XXX:read this file, only if not read before
      Stream newEntry = new Stream(filename, source);

      streamHash.put(filename, newEntry);
      return newEntry;
    }
    //System.out.println("HASHED: " + filename);
    return streamHash.get(filename);
  }

  public static void dump() {
    System.out.println("\nHASHED_STREAMS:");
    for (Map.Entry<String, Stream> entry : streamHash.entrySet()) {
      System.out.println(entry.getValue().toString());
    }
  }

}
