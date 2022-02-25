
package jscan.pe;

import jscan.buffers.IDataWriter;
import jscan.pe.constants.FileCharacteristics;
import jscan.pe.constants.MachineType;
import jscan.pe.constants.Sizeofs;

/// typedef struct _IMAGE_FILE_HEADER {
///   uint16_t Machine;
///   uint16_t NumberOfSections;
///   uint32_t TimeDateStamp;
///   uint32_t PointerToSymbolTable;
///   uint32_t NumberOfSymbols;
///   uint16_t SizeOfOptionalHeader;
///   uint16_t Characteristics;
/// } IMAGE_FILE_HEADER, *PIMAGE_FILE_HEADER;

public class ImageFileHeader {
  /*uint16_t*/ int Machine;
  /*uint16_t*/ int NumberOfSections;
  /*uint32_t*/ long TimeDateStamp;
  /*uint32_t*/ long PointerToSymbolTable;
  /*uint32_t*/ long NumberOfSymbols;
  /*uint16_t*/ int SizeOfOptionalHeader;
  /*uint16_t*/ int Characteristics;

  public ImageFileHeader(int numberOfSections) {

    this.Machine = MachineType.IMAGE_FILE_MACHINE_AMD64;
    this.NumberOfSections = numberOfSections;
    this.TimeDateStamp = 0; // System.currentTimeMillis() / 1000;
    this.PointerToSymbolTable = 0;
    this.NumberOfSymbols = 0;
    this.SizeOfOptionalHeader = Sizeofs.IMAGE_SIZEOF_NT_OPTIONAL64_HEADER;
    this.Characteristics = FileCharacteristics.IMAGE_FILE_RELOCS_STRIPPED
        | FileCharacteristics.IMAGE_FILE_EXECUTABLE_IMAGE | FileCharacteristics.IMAGE_FILE_LARGE_ADDRESS_AWARE;

  }

  public void write(IDataWriter dw) {
    dw.o2(Machine);
    dw.o2(NumberOfSections);
    dw.o4(TimeDateStamp);
    dw.o4(PointerToSymbolTable);
    dw.o4(NumberOfSymbols);
    dw.o2(SizeOfOptionalHeader);
    dw.o2(Characteristics);
  }

}
