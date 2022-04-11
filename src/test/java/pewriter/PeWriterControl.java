package pewriter;

import static jscan.pe.constants.ImageDirectoryEntry.IMAGE_DIRECTORY_ENTRY_IAT;
import static jscan.pe.constants.ImageDirectoryEntry.IMAGE_DIRECTORY_ENTRY_IMPORT;
import static jscan.pe.sections.SectionsIndexesLight.DATA;
import static jscan.pe.sections.SectionsIndexesLight.IDATA;
import static jscan.pe.sections.SectionsIndexesLight.TEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import jscan.buffers.IDataWriter;
import jscan.buffers.Ubuf;
import jscan.fio.ByteBuffDiff;
import jscan.fio.IO;
import jscan.pe.DosStub;
import jscan.pe.ImageDosHeader;
import jscan.pe.ImageFileHeader;
import jscan.pe.ImageNtHeader64;
import jscan.pe.ImageOptionalHeader64;
import jscan.pe.ImageSectionHeader;
import jscan.pe.PE64;
import jscan.pe.constants.Alignment;
import jscan.pe.constants.Sizeofs;
import jscan.pe.datas.DataSymbols;
import jscan.pe.imports.ImageImportByName;
import jscan.pe.imports.ImportDll;
import jscan.pe.imports.ImportSymbols;
import jscan.pe.sections.SectionHeadersBuilder;
import jscan.pe.sections.SectionSize;
import jscan.pe.sections.SectionsIndexesLight;
import jscan.utils.Aligner;

public class PeWriterControl {

  /// import_symbols construct_iat()
  /// {
  ///     import_symbols imports;
  ///     imports.add_dll("KERNEL32.dll")
  ///         .import_procedure("ExitProcess", 0x120);
  ///     imports.add_dll("msvcrt.dll")
  ///         .import_procedure("printf", 0x48b)
  ///         .import_procedure("scanf", 0x49b);
  ///     imports.prepare();
  /// 
  ///     return imports;
  /// }

  private ImportSymbols construct_iat() {
    ImportSymbols imports = new ImportSymbols();

    final ImportDll kernelDLL = new ImportDll("KERNEL32.dll");
    kernelDLL.add_procedure(new ImageImportByName("ExitProcess", 0x120));

    final ImportDll msvcrtDLL = new ImportDll("msvcrt.dll");
    msvcrtDLL.add_procedure(new ImageImportByName("printf", 0x48b));
    msvcrtDLL.add_procedure(new ImageImportByName("scanf", 0x49b));

    imports.add_dll(kernelDLL);
    imports.add_dll(msvcrtDLL);

    imports.prepare();
    return imports;
  }

  @Test
  public void write() throws IOException {

    // stub data
    DataSymbols datas = new DataSymbols();
    datas.add("zzzzz");

    // stub import
    ImportSymbols imports = construct_iat();

    // stub code
    /// push   rbp
    /// mov    rbp,rsp
    /// mov    rax,0x20
    /// mov    rsp,rbp
    /// pop    rbp
    /// ret
    int bytes[] = { 0x55, 0x48, 0x89, 0xE5, 0x48, 0xC7, 0xC0, 0x20, 0x00, 0x00, 0x00, 0x48, 0x89, 0xEC, 0x5D, 0xC3 };
    Ubuf code = new Ubuf();
    code.oArr(bytes);

    SectionHeadersBuilder sBuilder = new SectionHeadersBuilder();
    sBuilder.add(".text", new SectionSize(512/*TODO, it's a stub :)*/), SectionsIndexesLight.flags(TEXT));
    sBuilder.add(".data", new SectionSize(datas.virtual_size(), datas.raw_size()), SectionsIndexesLight.flags(DATA));
    sBuilder.add(".idata", new SectionSize(imports.total_size()), SectionsIndexesLight.flags(IDATA));

    List<ImageSectionHeader> sec_headers = sBuilder.build();

    /// set all RVAs
    datas.set_rva(sec_headers.get(DATA).VirtualAddress);
    imports.set_rva(sec_headers.get(IDATA).VirtualAddress);

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
    String filename = dir + "/data/test_pe/pefile.exe";
    strm.fout(filename);
    chmodX(filename);

    File f1 = new File(filename);
    File f2 = new File(dir + "/data/test_pe/evmc.exe");
    boolean diff = ByteBuffDiff.findDiff(IO.toBytes(f1), IO.toBytes(f2), false);
    assertFalse(diff);

    /// exit   12288
    /// printf 12304
    /// scanf  12312
    /// zzzzz  8192
    /// System.out.println("exit   " + imports.import_symbol("ExitProcess"));
    /// System.out.println("printf " + imports.import_symbol("printf"));
    /// System.out.println("scanf  " + imports.import_symbol("scanf"));
    /// System.out.println("zzzzz  " + datas.symbol("zzzzz"));

    assertEquals(12288, imports.symbol("ExitProcess"));
    assertEquals(12304, imports.symbol("printf"));
    assertEquals(12312, imports.symbol("scanf"));
    assertEquals(8192, datas.symbol("zzzzz"));

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
