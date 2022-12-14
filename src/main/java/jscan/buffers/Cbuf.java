package jscan.buffers;

import static jscan.utils.Env.HC_FEOF;

public class Cbuf {
  private static final int EOFS_PADDING_BUFLEN = 16;

  private final char buffer[];
  private final int size;
  private int offset;
  private int line;
  private int column;
  private char prevc;
  private int eofs;

  public Cbuf(final String input) {
    final StringBuilder sb = prepareInput(input);

    this.buffer = sb.toString().toCharArray();
    this.size = buffer.length;
    this.offset = 0;
    this.line = 1;
    this.column = 0;
    this.prevc = '\0';
    this.eofs = -1;
  }

  private StringBuilder prepareInput(final String input) {
    final char[] content = input.toCharArray();
    int from = 0;

    // Ignore the BOM, if any.
    if (content.length > 3) {
      if (content[0] == 0xef && content[1] == 0xbb && content[2] == 0xbf) {
        from = 3;
      }
    }

    // Build the main buffer
    final StringBuilder sb = new StringBuilder();
    for (int i = from; i < content.length; i += 1) {
      final char c = content[i];
      sb.append(c);
    }

    // Append an extra padding
    for (int i = 0; i < EOFS_PADDING_BUFLEN; i += 1) {
      sb.append("\0");
    }

    return sb;
  }

  public boolean isEof() {
    return eofs >= EOFS_PADDING_BUFLEN;
  }

  public char peekc() {

    // don't be too smart ;)
    int save_offset = offset;
    int save_line = line;
    int save_column = column;
    char save_prevc = prevc;
    int save_eofs = eofs;

    char c = nextc();

    offset = save_offset;
    line = save_line;
    column = save_column;
    prevc = save_prevc;
    eofs = save_eofs;

    return c;
  }

  public char[] peekc2() {

    char[] res = new char[3];

    // don't be too smart ;)
    int save_offset = offset;
    int save_line = line;
    int save_column = column;
    char save_prevc = prevc;
    int save_eofs = eofs;

    res[0] = nextc();
    res[1] = nextc();

    offset = save_offset;
    line = save_line;
    column = save_column;
    prevc = save_prevc;
    eofs = save_eofs;

    return res;
  }

  public char[] peekc3() {

    char[] res = new char[3];

    // don't be too smart ;)
    int save_offset = offset;
    int save_line = line;
    int save_column = column;
    char save_prevc = prevc;
    int save_eofs = eofs;

    res[0] = nextc();
    res[1] = nextc();
    res[2] = nextc();

    offset = save_offset;
    line = save_line;
    column = save_column;
    prevc = save_prevc;
    eofs = save_eofs;

    return res;
  }

  public char[] peekc4() {

    char[] res = new char[4];

    // don't be too smart ;)
    int save_offset = offset;
    int save_line = line;
    int save_column = column;
    char save_prevc = prevc;
    int save_eofs = eofs;

    res[0] = nextc();
    res[1] = nextc();
    res[2] = nextc();
    res[3] = nextc();

    offset = save_offset;
    line = save_line;
    column = save_column;
    prevc = save_prevc;
    eofs = save_eofs;

    return res;
  }

  public char nextc() {

    // when you build buffer, allocate more space to avoid IOOB check
    // for example: source = { '1', '2', '3', '\0' }, buffer = { '1', '2', '3', '\0', '\0', '\0', '\0', '\0' }

    while (!isEof()) {

      if (eofs > EOFS_PADDING_BUFLEN) {
        throw new RuntimeException("Infinite loop handling...");
      }

      if (offset >= size) {
        eofs += 1;
        return HC_FEOF; // '\0';
      }

      if (prevc == '\n') {
        line += 1;
        column = 0;
      }

      // we do not care about IOOB, because we ALWAYS have a little padding
      char c1 = buffer[offset + 0];
      char c2 = buffer[offset + 1];
      char c3 = buffer[offset + 2];

      if (c1 == '\\') {
        if (c2 == '\r') {
          if (c3 == '\n') {
            // DOS: [\][\r][\n]
            offset += 3;
          } else {
            // OSX: [\][\r]
            offset += 2;
          }

          prevc = '\n';
          continue;
        }

        // UNX: [\][\n]
        if (c2 == '\n') {
          offset += 2;
          prevc = '\n';
          continue;
        }
      }

      if (c1 == '\r') {
        if (c2 == '\n') {
          // DOS: [\r][\n]
          offset += 2;
        } else {
          // OSX: [\r]
          offset += 1;
        }
        prevc = '\n';
        return '\n';
      }

      char next = buffer[offset];
      offset += 1;
      prevc = next;

      if (next == '\t') {
        column += 4;
      } else {
        column += 1;
      }

      if (next == '\0') {
        eofs += 1;
        return HC_FEOF; // '\0';
      }

      return next;
    }

    return HC_FEOF;
  }

  @Override
  public String toString() {
    return String.format("[offset=%d, line=%d, column=%d]", offset, line, column);
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public int getPrevc() {
    return prevc;
  }

}
