package jscan.pe;

import jscan.buffers.IDataWriter;

/// typedef struct _IMAGE_DATA_DIRECTORY {
///   uint32_t VirtualAddress;
///   uint32_t Size;
/// } IMAGE_DATA_DIRECTORY, *PIMAGE_DATA_DIRECTORY;

public class ImageDataDirectory {
  public long VirtualAddress;
  public long Size;

  public ImageDataDirectory() {

  }

  public ImageDataDirectory(long virtualAddress, long size) {
    VirtualAddress = virtualAddress;
    Size = size;
  }

  public void write(IDataWriter dw) {
    dw.o4(VirtualAddress);
    dw.o4(Size);
  }

}
