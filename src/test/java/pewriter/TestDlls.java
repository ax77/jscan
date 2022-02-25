package pewriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import jscan.pe.constants.Sizeofs;
import jscan.pe.imports.ImageImportByName;
import jscan.pe.imports.ImportDll;
import jscan.pe.imports.ImportSymbols;

public class TestDlls {

  private ImportDll getKernel32Dll() {
    ImportDll dll = new ImportDll("kernel32.dll");
    dll.add_procedure(new ImageImportByName("ExitProcess", 0));
    return dll;
  }

  private ImportDll getMsvcrtDll() {
    ImportDll dll = new ImportDll("msvcrt.dll");
    dll.add_procedure(new ImageImportByName("printf", 0));
    dll.add_procedure(new ImageImportByName("scanf", 0));
    dll.add_procedure(new ImageImportByName("strlen", 0));
    return dll;
  }

  private ImportDll getDummyDll() {
    ImportDll dll = new ImportDll("xxx.dll");
    dll.add_procedure(new ImageImportByName("a", 0));
    dll.add_procedure(new ImageImportByName("b", 0));
    dll.add_procedure(new ImageImportByName("c", 0));
    return dll;
  }

  @Test
  public void testDllSlot() {
    ImportDll dll = getKernel32Dll();
    assertEquals(0, dll.import_slot("ExitProcess"));
    assertEquals(-1, dll.import_slot(" "));
  }

  @Test
  public void testImportSyms() {
    ImportSymbols symbols = new ImportSymbols();
    symbols.add_dll(getKernel32Dll());
    symbols.add_dll(getMsvcrtDll());

    assertEquals(0, symbols.symbol("ExitProcess"));
    assertEquals(16, symbols.symbol("printf"));
    assertEquals(24, symbols.symbol("scanf"));
    assertEquals(32, symbols.symbol("strlen"));

    symbols.set_rva(1000);
    assertEquals(0 + 1000, symbols.symbol("ExitProcess"));
    assertEquals(16 + 1000, symbols.symbol("printf"));
    assertEquals(24 + 1000, symbols.symbol("scanf"));
    assertEquals(32 + 1000, symbols.symbol("strlen"));
  }

  @Test
  public void testImportSymsWithThreeDlls() {
    ImportSymbols symbols = new ImportSymbols();
    symbols.add_dll(getKernel32Dll());
    symbols.add_dll(getMsvcrtDll());
    symbols.add_dll(getDummyDll());

    assertEquals(0, symbols.symbol("ExitProcess"));
    // [+] 8 bytes sentinel after

    assertEquals(16, symbols.symbol("printf"));
    assertEquals(24, symbols.symbol("scanf"));
    assertEquals(32, symbols.symbol("strlen"));
    // [+] 8 bytes sentinel here too

    assertEquals(48, symbols.symbol("a"));
    assertEquals(56, symbols.symbol("b"));
    assertEquals(64, symbols.symbol("c"));

  }

  @Test
  public void testOffsetMap() {
    ImportSymbols symbols = new ImportSymbols();
    symbols.add_dll(getKernel32Dll());
    symbols.add_dll(getMsvcrtDll());
    symbols.add_dll(getDummyDll());

    Map<String, Long> hmap = offset_map(symbols.getImport_dlls());

    assertEquals(Long.valueOf(0), hmap.get("ExitProcess"));
    // [+] 8 bytes sentinel after

    assertEquals(Long.valueOf(16), hmap.get("printf"));
    assertEquals(Long.valueOf(24), hmap.get("scanf"));
    assertEquals(Long.valueOf(32), hmap.get("strlen"));
    // [+] 8 bytes sentinel here too

    assertEquals(Long.valueOf(48), hmap.get("a"));
    assertEquals(Long.valueOf(56), hmap.get("b"));
    assertEquals(Long.valueOf(64), hmap.get("c"));

    Long address = hmap.get(" ");
    assertNull(address);
  }

  @Test
  public void testBruteForceResolver() {
    ImportSymbols symbols = new ImportSymbols();
    symbols.add_dll(getKernel32Dll());
    symbols.add_dll(getMsvcrtDll());
    symbols.add_dll(getDummyDll());

    assertEquals(0, symbols.find_import_brute_force("ExitProcess"));
    // [+] 8 bytes sentinel after

    assertEquals(16, symbols.find_import_brute_force("printf"));
    assertEquals(24, symbols.find_import_brute_force("scanf"));
    assertEquals(32, symbols.find_import_brute_force("strlen"));
    // [+] 8 bytes sentinel here too

    assertEquals(48, symbols.find_import_brute_force("a"));
    assertEquals(56, symbols.find_import_brute_force("b"));
    assertEquals(64, symbols.find_import_brute_force("c"));

    assertEquals(-1, symbols.find_import_brute_force(" "));
  }

  private Map<String, Long> offset_map(List<ImportDll> dlls) {
    Map<String, Long> hmap = new LinkedHashMap<>();
    long bytes_offset = 0;
    for (ImportDll dll : dlls) {
      for (ImageImportByName proc : dll.getImports()) {
        hmap.put(proc.getName(), bytes_offset);
        bytes_offset += Sizeofs.SIZEOF_QWORD;
      }
      hmap.put(dll.getName() + "_sentinel_" + UUID.randomUUID().toString(), bytes_offset);
      bytes_offset += Sizeofs.SIZEOF_QWORD;
    }
    return hmap;
  }

  @Test
  public void testHintNameTableSize() {
    final ImportDll dummyDll = getDummyDll();
    final long w = Sizeofs.SIZEOF_WORD;

    ImportSymbols symbols = new ImportSymbols();
    symbols.add_dll(dummyDll);
    symbols.prepare();

    int expect = dummyDll.getName().length() + 2;
    expect += w + "a".length() + 1;
    expect += w + "b".length() + 1;
    expect += w + "c".length() + 1;

    assertEquals(expect, symbols.hint_name_table_size_including_dll_names());
    assertEquals(3 + 1, symbols.getThunks().size()); // 3 functions+sentinel
    assertEquals(1 + 1, symbols.getDescs().size()); // 1 dll + sentinel

  }

}
