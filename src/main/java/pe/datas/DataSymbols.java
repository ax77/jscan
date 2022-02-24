package pe.datas;

import java.util.HashMap;
import java.util.Map;

import buffers.Ubuf;
import pe.imports.ISymbol;
import utils.Aligner;

public class DataSymbols implements ISymbol {
  public long current_offset;
  public long virtual_addr;
  private Map<String, Long> symbols;
  private Ubuf strm;

  public DataSymbols() {
    this.strm = new Ubuf();
    this.symbols = new HashMap<>();
  }

  public void add(String sym) {
    checkDuplicates(sym);

    strm.oUtf(sym);
    strm.o1(0);

    symbols.put(sym, current_offset);
    current_offset += strm.bytes();
  }

  private void checkDuplicates(String sym) {
    if (symbols.containsKey(sym)) {
      throw new RuntimeException("symbol is already present: " + sym);
    }
  }

  public int[] build() {
    return strm.toBytes();
  }

  public void set_rva(long addr) {
    virtual_addr = addr;
  }

  public int raw_size() {
    return strm.bytes();
  }

  public int virtual_size() {
    return strm.bytes();
  }

  public long symbol(String name) {
    Long needle = symbols.get(name);
    if (needle != null) {
      return Aligner.incr_check_overflow(needle, virtual_addr);
    }
    throw new RuntimeException("symbol with given name does not exist: " + name);
  }
}
