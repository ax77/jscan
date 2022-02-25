package jscan.pe;

import jscan.buffers.IDataWriter;
import jscan.pe.constants.Alignment;
import jscan.pe.constants.ArraySizes;
import jscan.pe.constants.Magics;
import jscan.pe.constants.MajMin;
import jscan.pe.constants.WindowsSubsystem;

/// typedef struct _IMAGE_OPTIONAL_HEADER64 {
///   uint16_  t Magic;
///   uint8_t  MajorLinkerVersion;
///   uint8_t  MinorLinkerVersion;
///   uint32_t SizeOfCode;
///   uint32_t SizeOfInitializedData;
///   uint32_t SizeOfUninitializedData;
///   uint32_t AddressOfEntryPoint;
///   uint32_t BaseOfCode;
///   uint64_t ImageBase;
///   uint32_t SectionAlignment;
///   uint32_t FileAlignment;
///   uint16_t MajorOperatingSystemVersion;
///   uint16_t MinorOperatingSystemVersion;
///   uint16_t MajorImageVersion;
///   uint16_t MinorImageVersion;
///   uint16_t MajorSubsystemVersion;
///   uint16_t MinorSubsystemVersion;
///   uint32_t Win32VersionValue;
///   uint32_t SizeOfImage;
///   uint32_t SizeOfHeaders;
///   uint32_t CheckSum;
///   uint16_t Subsystem;
///   uint16_t DllCharacteristics;
///   uint64_t SizeOfStackReserve;
///   uint64_t SizeOfStackCommit;
///   uint64_t SizeOfHeapReserve;
///   uint64_t SizeOfHeapCommit;
///   uint32_t LoaderFlags;
///   uint32_t NumberOfRvaAndSizes;
///   IMAGE_DATA_DIRECTORY DataDirectory[IMAGE_NUMBEROF_DIRECTORY_ENTRIES];
/// } IMAGE_OPTIONAL_HEADER64, *PIMAGE_OPTIONAL_HEADER64;

public class ImageOptionalHeader64 {
  /*uint16_  */ int Magic;
  /*uint8_t  */ char MajorLinkerVersion;
  /*uint8_t  */ char MinorLinkerVersion;
  /*uint32_t */ long SizeOfCode;
  /*uint32_t */ long SizeOfInitializedData;
  /*uint32_t */ long SizeOfUninitializedData;
  /*uint32_t */ long AddressOfEntryPoint;
  /*uint32_t */ long BaseOfCode;
  /*uint64_t */ long ImageBase;
  /*uint32_t */ long SectionAlignment;
  /*uint32_t */ long FileAlignment;
  /*uint16_t */ int MajorOperatingSystemVersion;
  /*uint16_t */ int MinorOperatingSystemVersion;
  /*uint16_t */ int MajorImageVersion;
  /*uint16_t */ int MinorImageVersion;
  /*uint16_t */ int MajorSubsystemVersion;
  /*uint16_t */ int MinorSubsystemVersion;
  /*uint32_t */ long Win32VersionValue;
  /*uint32_t */ long SizeOfImage;
  /*uint32_t */ long SizeOfHeaders;
  /*uint32_t */ long CheckSum;
  /*uint16_t */ int Subsystem;
  /*uint16_t */ int DllCharacteristics;
  /*uint64_t */ long SizeOfStackReserve;
  /*uint64_t */ long SizeOfStackCommit;
  /*uint64_t */ long SizeOfHeapReserve;
  /*uint64_t */ long SizeOfHeapCommit;
  /*uint32_t */ long LoaderFlags;
  /*uint32_t */ long NumberOfRvaAndSizes;
  public ImageDataDirectory DataDirectory[];

  public ImageOptionalHeader64(long sizeOfCode, long sizeOfInitializedData, long addressOfEntryPoint, long baseOfCode,
      long sizeOfImage, long sizeOfHeaders) {

    this.Magic = Magics.IMAGE_NT_OPTIONAL_HDR64_MAGIC;
    this.MajorLinkerVersion = MajMin.MajorLinkerVersion;
    this.MinorLinkerVersion = MajMin.MinorLinkerVersion;
    this.SizeOfCode = sizeOfCode;
    this.SizeOfInitializedData = sizeOfInitializedData;
    this.SizeOfUninitializedData = 0;
    this.AddressOfEntryPoint = addressOfEntryPoint;
    this.BaseOfCode = baseOfCode;
    this.ImageBase = 0x140000000L;
    this.SectionAlignment = Alignment.SECTION_ALIGNMENT;
    this.FileAlignment = Alignment.FILE_ALIGNMENT;
    this.MajorOperatingSystemVersion = MajMin.OS_MAJOR;
    this.MinorOperatingSystemVersion = MajMin.OS_MINOR;
    this.MajorImageVersion = MajMin.OS_MAJOR;
    this.MinorImageVersion = MajMin.OS_MINOR;
    this.MajorSubsystemVersion = MajMin.OS_MAJOR;
    this.MinorSubsystemVersion = MajMin.OS_MINOR;
    this.Win32VersionValue = 0;
    this.SizeOfImage = sizeOfImage;
    this.SizeOfHeaders = sizeOfHeaders;
    this.CheckSum = 0;
    this.Subsystem = WindowsSubsystem.IMAGE_SUBSYSTEM_WINDOWS_CUI;
    this.DllCharacteristics = 0;
    this.SizeOfStackReserve = 0x00100000L;
    this.SizeOfStackCommit = 0x00010000L;
    this.SizeOfHeapReserve = 0x00100000L;
    this.SizeOfHeapCommit = 0x00001000L;
    this.LoaderFlags = 0;
    this.NumberOfRvaAndSizes = ArraySizes.IMAGE_NUMBEROF_DIRECTORY_ENTRIES;

    DataDirectory = new ImageDataDirectory[ArraySizes.IMAGE_NUMBEROF_DIRECTORY_ENTRIES];
    for (int i = 0; i < ArraySizes.IMAGE_NUMBEROF_DIRECTORY_ENTRIES; i += 1) {
      DataDirectory[i] = new ImageDataDirectory();
    }

  }

  public void write(IDataWriter dw) {

    /*uint16_  */ dw.o2(Magic);
    /*uint8_t  */ dw.o1(MajorLinkerVersion);
    /*uint8_t  */ dw.o1(MinorLinkerVersion);
    /*uint32_t */ dw.o4(SizeOfCode);
    /*uint32_t */ dw.o4(SizeOfInitializedData);
    /*uint32_t */ dw.o4(SizeOfUninitializedData);
    /*uint32_t */ dw.o4(AddressOfEntryPoint);
    /*uint32_t */ dw.o4(BaseOfCode);
    /*uint64_t */ dw.o8(ImageBase);
    /*uint32_t */ dw.o4(SectionAlignment);
    /*uint32_t */ dw.o4(FileAlignment);
    /*uint16_t */ dw.o2(MajorOperatingSystemVersion);
    /*uint16_t */ dw.o2(MinorOperatingSystemVersion);
    /*uint16_t */ dw.o2(MajorImageVersion);
    /*uint16_t */ dw.o2(MinorImageVersion);
    /*uint16_t */ dw.o2(MajorSubsystemVersion);
    /*uint16_t */ dw.o2(MinorSubsystemVersion);
    /*uint32_t */ dw.o4(Win32VersionValue);
    /*uint32_t */ dw.o4(SizeOfImage);
    /*uint32_t */ dw.o4(SizeOfHeaders);
    /*uint32_t */ dw.o4(CheckSum);
    /*uint16_t */ dw.o2(Subsystem);
    /*uint16_t */ dw.o2(DllCharacteristics);
    /*uint64_t */ dw.o8(SizeOfStackReserve);
    /*uint64_t */ dw.o8(SizeOfStackCommit);
    /*uint64_t */ dw.o8(SizeOfHeapReserve);
    /*uint64_t */ dw.o8(SizeOfHeapCommit);
    /*uint32_t */ dw.o4(LoaderFlags);
    /*uint32_t */ dw.o4(NumberOfRvaAndSizes);

    // Data directories
    int ddc = ArraySizes.IMAGE_NUMBEROF_DIRECTORY_ENTRIES;
    for (int i = 0; i < ddc; i++) {
      DataDirectory[i].write(dw);
    }
  }

}
