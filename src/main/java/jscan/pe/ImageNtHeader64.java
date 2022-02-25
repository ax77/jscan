package jscan.pe;

import jscan.buffers.IDataWriter;
import jscan.pe.constants.Magics;

/// typedef struct _IMAGE_NT_HEADERS64 {
///   DWORD Signature;
///   IMAGE_FILE_HEADER FileHeader;
///   IMAGE_OPTIONAL_HEADER64 OptionalHeader;
/// } IMAGE_NT_HEADERS64,*PIMAGE_NT_HEADERS64;

public class ImageNtHeader64 {

  int Signature;
  ImageFileHeader FileHeader;
  ImageOptionalHeader64 OptionalHeader;

  public ImageNtHeader64(ImageFileHeader fileHeader, ImageOptionalHeader64 optionalHeader) {
    this.Signature = Magics.IMAGE_NT_SIGNATURE;
    this.FileHeader = fileHeader;
    this.OptionalHeader = optionalHeader;
  }

  public void write(IDataWriter out) {
    out.o4(Signature);
    FileHeader.write(out);
    OptionalHeader.write(out);
  }
}
