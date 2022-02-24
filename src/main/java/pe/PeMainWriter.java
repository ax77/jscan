package pe;

import static pe.constants.ImageDirectoryEntry.IMAGE_DIRECTORY_ENTRY_IAT;
import static pe.constants.ImageDirectoryEntry.IMAGE_DIRECTORY_ENTRY_IMPORT;
import static pe.sections.SectionsIndexesLight.DATA;
import static pe.sections.SectionsIndexesLight.IDATA;
import static pe.sections.SectionsIndexesLight.TEXT;

import java.io.File;
import java.io.IOException;
import java.util.List;

import asm.Asm86;
import pe.constants.Alignment;
import pe.constants.Sizeofs;
import pe.datas.DataSymbols;
import pe.imports.ImportSymbols;
import pe.sections.SectionHeadersBuilder;
import pe.sections.SectionSize;
import pe.sections.SectionsIndexesLight;
import utils.Aligner;
import buffers.IDataWriter;
import buffers.Ubuf;

public class PeMainWriter {

  private final DataSymbols datas;
  private final ImportSymbols imports;
  private final Asm86 code;

  public PeMainWriter(DataSymbols datas, ImportSymbols imports, Asm86 code) {
    this.datas = datas;
    this.imports = imports;
    this.code = code;
  }

  public void write(String fname) throws IOException {

    SectionHeadersBuilder sBuilder = new SectionHeadersBuilder();
    sBuilder.add(".text", new SectionSize(code.bytesCount()), SectionsIndexesLight.flags(TEXT));
    sBuilder.add(".data", new SectionSize(datas.virtual_size(), datas.raw_size()), SectionsIndexesLight.flags(DATA));
    sBuilder.add(".idata", new SectionSize(imports.total_size()), SectionsIndexesLight.flags(IDATA));

    List<ImageSectionHeader> sec_headers = sBuilder.build();

    /// set all RVAs

    datas.set_rva(sec_headers.get(DATA).VirtualAddress);
    imports.set_rva(sec_headers.get(IDATA).VirtualAddress);
    /// XXX:commit exactly here! after all data and imports RVAs are ready.
    code.set_rva(sec_headers.get(TEXT).VirtualAddress, imports, datas);

    //System.out.println(code.printBytesInstr());

    /// Sections, sizes
    final int num_of_sections = sec_headers.size();
    ImageSectionHeader back = sec_headers.get(num_of_sections - 1);
    long allHeadersSize = Sizeofs.sizeofAllHeaders(num_of_sections);
    long sizeOfImage = Aligner.align(back.VirtualAddress + back.VirtualSize, Alignment.SECTION_ALIGNMENT);
    long sizeOfHeaders = Aligner.align(allHeadersSize, Alignment.FILE_ALIGNMENT);

    /// OptHeader
    long sizeOfCode = sec_headers.get(TEXT).SizeOfRawData;
    long sizeOfInitializedData = sec_headers.get(DATA).SizeOfRawData + sec_headers.get(IDATA).SizeOfRawData;
    long addressOfEntryPoint = sec_headers.get(TEXT).VirtualAddress;
    long baseOfCode = sec_headers.get(TEXT).VirtualAddress;

    ImageOptionalHeader64 opt_header = new ImageOptionalHeader64(sizeOfCode, sizeOfInitializedData, addressOfEntryPoint,
        baseOfCode, sizeOfImage, sizeOfHeaders);

    final long idata_addr = sec_headers.get(IDATA).VirtualAddress;
    // Informations of dll-procedure dependencies
    opt_header.DataDirectory[IMAGE_DIRECTORY_ENTRY_IMPORT].Size = imports.image_import_descs_size();
    opt_header.DataDirectory[IMAGE_DIRECTORY_ENTRY_IMPORT].VirtualAddress = idata_addr + imports.iat_size();
    // This is are where OS patches extern (dll) procedure addresses
    opt_header.DataDirectory[IMAGE_DIRECTORY_ENTRY_IAT].VirtualAddress = idata_addr;
    opt_header.DataDirectory[IMAGE_DIRECTORY_ENTRY_IAT].Size = imports.iat_size();

    /// PE32+
    final ImageNtHeader64 imageNtHeader64 = new ImageNtHeader64(new ImageFileHeader(num_of_sections), opt_header);
    PE64 pe = new PE64(new ImageDosHeader(), new DosStub(), imageNtHeader64);

    /// writer
    Ubuf strm = new Ubuf();

    // 1. the file structure
    pe.write(strm);

    // 2. section headers
    for (ImageSectionHeader s : sec_headers) {
      s.write(strm);
    }

    // 3. section binary data
    write_section(strm, code.toBytes());
    write_section(strm, datas.build());
    write_section(strm, imports.build());

    // write the file.
    String dir = System.getProperty("user.dir");
    String filename = dir + "/" + fname;
    strm.fout(filename);
    chmodX(filename);
  }

  private void chmodX(String filename) {
    File file = new File(filename);

    file.setExecutable(true, false);
    file.setReadable(true, false);
    file.setWritable(true, false);
  }

  private void write_section(IDataWriter strm, int bytes[]) {
    for (int b : bytes) {
      strm.o1(b);
    }
    int align = Aligner.align(bytes.length, Alignment.FILE_ALIGNMENT);
    for (int i = bytes.length; i < align; ++i) {
      strm.o1(0);
    }
  }

}
