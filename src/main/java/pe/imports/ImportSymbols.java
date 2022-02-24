package pe.imports;

import java.util.ArrayList;
import java.util.List;

import buffers.Ubuf;
import pe.constants.Sizeofs;
import utils.Aligner;

public class ImportSymbols implements ISymbol {
  private List<ImportDll> import_dlls;
  private List<ImageThunkData64> thunks;
  private List<ImageImportDescriptor> descs;
  private long virtual_addr;

  public ImportSymbols() {
    this.import_dlls = new ArrayList<>();
    this.thunks = new ArrayList<>();
    this.descs = new ArrayList<>();
  }

  public void add_dll(ImportDll dll) {
    for (ImportDll e : import_dlls) {
      if (e.getName().equals(dll.getName())) {
        throw new RuntimeException("DLL name already present: " + dll.getName());
      }
    }

    import_dlls.add(dll);
  }

  public List<ImportDll> getImport_dlls() {
    return import_dlls;
  }

  public List<ImageThunkData64> getThunks() {
    return thunks;
  }

  public List<ImageImportDescriptor> getDescs() {
    return descs;
  }

  /// The main structure and layout:
  ///
  /// IMAGE_THUNK_DATA (array, import address table, one chunk for each function, and the sentinel after all)
  /// IMAGE_IMPORT_DESCRIPTOR (array, one descriptor for each DLL, and the sentinel after all)
  /// IMAGE_THUNK_DATA (array, import names table, one chunk for each function, and the sentinel after all)
  /// IMAGE_IMPORT_BY_NAME (array, which also contains the address of the name of the DLL)
  ///     [dllname(str) + \0\0] + [hint(u16) + name(str) + \0] + [hint(u16) + name(str) + \0] ...  

  //TODO:test
  public void prepare() {

    /// OriginalFirstThunk - INT (import names table)
    /// FirstThunk         - IAT (import address table)

    int num_thunks = 0;
    for (ImportDll dll : import_dlls) {
      num_thunks += dll.getImports().size() + 1; // including sentinel thunk
    }

    final long thunks_size_in_bytes = num_thunks * Sizeofs.SIZEOF_QWORD;

    final int num_descs = import_dlls.size() + 1;
    final long descs_size_in_bytes = num_descs * Sizeofs.IMAGE_SIZEOF_IMPORT_DESCRIPTOR;

    final long original_thunk_dist = thunks_size_in_bytes + descs_size_in_bytes;
    final long start_directory = original_thunk_dist + thunks_size_in_bytes; // [IAT + descs + INT]...

    long entry_addr = start_directory;
    long thunk_addr = 0;

    for (ImportDll dll : import_dlls) {
      final ImageImportDescriptor d = new ImageImportDescriptor();

      d.Name = entry_addr; /// RVA of dll name
      d.FirstThunk = thunk_addr; /// RVA of IAT
      d.OriginalFirstThunk = d.FirstThunk + original_thunk_dist; /// RVA of INT
      descs.add(d);

      entry_addr += dll.getName().length() + 2; // two \0\0
      for (ImageImportByName proc : dll.getImports()) {

        thunks.add(new ImageThunkData64(entry_addr));

        // WORD Hint
        // BYTE[] Name
        // [hint(u16) + name(str) + \0]
        entry_addr += Sizeofs.SIZEOF_WORD + proc.getName().length() + 1;
      }

      // sentinel
      thunks.add(new ImageThunkData64());

      // step to the next DLL, increment the thunk address as:
      // num_thunks_in_dll + sentinel_thunk(u64) * sizeof(u64)
      thunk_addr += (dll.getImports().size() + 1) * Sizeofs.SIZEOF_QWORD;
    }

    descs.add(new ImageImportDescriptor()); // sentinel

  }

  //TODO:test
  public long iat_size() {
    return thunks.size() * Sizeofs.SIZEOF_QWORD;
  }

  //TODO:test
  public long image_import_descs_size() {
    return descs.size() * Sizeofs.IMAGE_SIZEOF_IMPORT_DESCRIPTOR;
  }

  public long hint_name_table_size_including_dll_names() {
    long directory_size = 0;

    for (ImportDll dll : import_dlls) {
      directory_size += dll.getName().length() + 2; // name + \0\0
      for (ImageImportByName proc : dll.getImports()) {
        directory_size += Sizeofs.SIZEOF_WORD; // hint(u16)
        directory_size += proc.getName().length() + 1; // name + \0
      }
    }

    return directory_size;
  }

  //TODO:test
  public long total_size() {

    /// iat*2, these vars for understanding
    final long importAddressTableSize = iat_size();
    final long importNamesTableSize = iat_size();

    return importAddressTableSize + importNamesTableSize + image_import_descs_size()
        + hint_name_table_size_including_dll_names();
  }

  public void set_rva(long virtualAddress) {
    this.virtual_addr = virtualAddress;
  }

  //TODO:test
  public int[] build() {

    // Make sure IAT will fit in given address space)
    Aligner.incr_check_overflow(virtual_addr, total_size());

    Ubuf strm = new Ubuf();

    // Thunks
    for (ImageThunkData64 thunk : thunks) {
      if (thunk.is_sentinel()) {
        strm.o8(0);
      } else {
        strm.o8(thunk.Function + virtual_addr);
      }
    }

    // Image import descriptors (for each DLL one import descriptor)
    for (ImageImportDescriptor desc : descs) {
      if (desc.OriginalFirstThunk != 0) { // it's not a sentinel descriptor
        desc.OriginalFirstThunk += virtual_addr;
        desc.FirstThunk += virtual_addr;
        desc.Name += virtual_addr;
      }
      desc.write(strm);
    }

    // Original thunks
    for (ImageThunkData64 thunk : thunks) {
      if (thunk.is_sentinel()) {
        strm.o8(0);
      } else {
        strm.o8(thunk.Function + virtual_addr);
      }
    }

    // Directory
    for (ImportDll dll : import_dlls) {
      strm.oUtf(dll.getName());
      strm.o1(0);
      strm.o1(0);
      for (ImageImportByName proc : dll.getImports()) {
        strm.o2(proc.getHint());
        strm.oUtf(proc.getName());
        strm.o1(0);
      }
    }

    return strm.toBytes();
  }

  //TODO:test
  public long symbol(String name) {
    /// Note: the original code from evmc.
    ///
    /// for (int i = 0; i < import_dlls.size(); i += 1) {
    ///   int slot = import_dlls.get(i).import_slot(name);
    /// 
    ///   if (slot != -1) {
    /// 
    ///     int imp_sofar = 0;
    ///     for (int j = 0; j < i; j += 1) {
    ///       ImportDll dll = import_dlls.get(j);
    ///       imp_sofar += dll.getImports().size() + 1;
    ///     }
    /// 
    ///     return virtual_addr + (imp_sofar + slot) * Sizeofs.SIZEOF_QWORD;
    /// 
    ///   }
    /// }

    long addr = find_import_brute_force(name);
    if (addr != -1) {
      return virtual_addr + addr;
    }

    throw new RuntimeException("extern procedure was not found: " + name);
  }

  /// Note: by using this very version everything becomes clean and precise.
  /// And it is quite understandable what is going on...
  ///
  /// TODO: two functions with the same name in different dlls???
  /// TODO: the function has the same name as dll itself???
  ///
  public long find_import_brute_force(String name) {
    long bytes_offset = 0;
    for (ImportDll dll : import_dlls) {
      for (ImageImportByName proc : dll.getImports()) {
        if (proc.getName().equals(name)) {
          return bytes_offset;
        }
        bytes_offset += Sizeofs.SIZEOF_QWORD; // go to the next thunk
      }
      bytes_offset += Sizeofs.SIZEOF_QWORD; // sentinel
    }
    return -1;
  }

  /// TODO: it is possible to optimize the resolver by using a prepared map of all names and their offset.
  ///
  /// private Map<String, Long> offset_map(List<ImportDll> dlls) {
  ///   Map<String, Long> hmap = new LinkedHashMap<>();
  ///   long bytes_offset = 0;
  ///   for (ImportDll dll : dlls) {
  ///     for (ImageImportByName proc : dll.getImports()) {
  ///       hmap.put(proc.getName(), bytes_offset);
  ///       bytes_offset += Sizeofs.SIZEOF_QWORD;
  ///     }
  ///     hmap.put(dll.getName() + "_sentinel_" + UUID.randomUUID().toString(), bytes_offset);
  ///     bytes_offset += Sizeofs.SIZEOF_QWORD;
  ///   }
  ///   return hmap;
  /// }

}
