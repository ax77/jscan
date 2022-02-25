package jscan.pe.constants;

import jscan.utils.Aligner;

public abstract class Sizeofs {
  public static final int IMAGE_SIZEOF_NT_OPTIONAL64_HEADER = 240;
  public static final int IMAGE_SIZEOF_FILE_HEADER = 20;
  public static final int IMAGE_SIZEOF_SECTION_HEADER = 40;
  public static final int IMAGE_SIZEOF_NT_HEADERS64 = 264; // signature(u32)+sizeof(FH)+sizeof(OH64)=4+20+240
  public static final int IMAGE_SIZEOF_IMPORT_DESCRIPTOR = 20;

  // DOS
  public static final int IMAGE_SIZEOF_DOS_HEADER = 64;
  public static final int SIZEOF_DOS_STUB = 64;

  // Image section header name
  public static final int IMAGE_SIZEOF_SHORT_NAME = 8;

  // types
  public static final long SIZEOF_WORD = 2;
  public static final long SIZEOF_DWORD = 4;
  public static final long SIZEOF_QWORD = 8;

  // Size of all headers, aligned.
  public static final int sizeofAllHeaders(int numSections) {
    int SECTION_HEADERS_FILE_OFFSET = IMAGE_SIZEOF_DOS_HEADER + SIZEOF_DOS_STUB + IMAGE_SIZEOF_NT_HEADERS64;
    int x = SECTION_HEADERS_FILE_OFFSET + (numSections * IMAGE_SIZEOF_SECTION_HEADER);
    return Aligner.align(x, Alignment.FILE_ALIGNMENT);
  }

}
