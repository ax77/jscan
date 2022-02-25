package jscan.pe;

import jscan.buffers.IDataWriter;
import jscan.pe.constants.Sizeofs;

/// typedef struct _IMAGE_SECTION_HEADER {
///   uint8_t Name[IMAGE_SIZEOF_SHORT_NAME];
///   union {
///       uint32_t PhysicalAddress;
///       uint32_t VirtualSize;
///   } Misc;
///   uint32_t VirtualAddress;
///   uint32_t SizeOfRawData;
///   uint32_t PointerToRawData;
///   uint32_t PointerToRelocations;
///   uint32_t PointerToLinenumbers;
///   uint16_t NumberOfRelocations;
///   uint16_t NumberOfLinenumbers;
///   uint32_t Characteristics;
/// } IMAGE_SECTION_HEADER, *PIMAGE_SECTION_HEADER;

/// XXX: the 'PhysicalAddress' field from the union is ignored during writing.
///

public class ImageSectionHeader {
  public String Name; // 8 bytes
  // public long PhysicalAddress;
  public long VirtualSize;
  public long VirtualAddress;
  public long SizeOfRawData;
  public long PointerToRawData;
  public long PointerToRelocations;
  public long PointerToLinenumbers;
  public int NumberOfRelocations;
  public int NumberOfLinenumbers;
  public long Characteristics;

  public ImageSectionHeader(String name) {
    Name = name;
  }

  public void write(IDataWriter dw) {
    dw.oUtf(Name, Sizeofs.IMAGE_SIZEOF_SHORT_NAME);
    // dw.o4(PhysicalAddress);
    dw.o4(VirtualSize);
    dw.o4(VirtualAddress);
    dw.o4(SizeOfRawData);
    dw.o4(PointerToRawData);
    dw.o4(PointerToRelocations);
    dw.o4(PointerToLinenumbers);
    dw.o2(NumberOfRelocations);
    dw.o2(NumberOfLinenumbers);
    dw.o4(Characteristics);
  }

}
