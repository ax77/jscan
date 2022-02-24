package pe.imports;

import buffers.IDataWriter;

/// typedef struct _IMAGE_IMPORT_DESCRIPTOR {
///   /*__C89_NAMELESS*/ union {
///       DWORD Characteristics;
///       DWORD OriginalFirstThunk;
///   } DUMMYUNIONNAME;
///   DWORD TimeDateStamp;
/// 
///   DWORD ForwarderChain;
///   DWORD Name;
///   DWORD FirstThunk;
/// } IMAGE_IMPORT_DESCRIPTOR;
/// typedef IMAGE_IMPORT_DESCRIPTOR /*UNALIGNED*/ *PIMAGE_IMPORT_DESCRIPTOR;

/// XXX: the 'Characteristics' field from the union is ignored during writing.
///

public class ImageImportDescriptor {
  //long Characteristics;
  public long OriginalFirstThunk;
  public long TimeDateStamp;
  public long ForwarderChain;
  public long Name;
  public long FirstThunk;

  public ImageImportDescriptor() {
  }

  public void write(IDataWriter out) {
    //out.o4(Characteristics);
    out.o4(OriginalFirstThunk);
    out.o4(TimeDateStamp);
    out.o4(ForwarderChain);
    out.o4(Name);
    out.o4(FirstThunk);
  }
}
