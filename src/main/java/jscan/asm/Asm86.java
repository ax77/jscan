package jscan.asm;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jscan.buffers.Ubuf;
import jscan.pe.constants.Sizeofs;
import jscan.pe.datas.DataSymbols;
import jscan.pe.imports.ISymbol;
import jscan.pe.imports.ImportSymbols;

public class Asm86 {
  /// The combinations are:
  ///
  /// * I) add, sub, and, or, xor, cmp, mov
  /// opc reg64,reg64
  /// opc reg64,[reg64]
  /// opc [reg64],reg64
  /// opc reg64,[reg64+@i32]
  /// opc [reg64+@i32],reg64
  /// opc reg64,@i32
  ///
  /// * II) push, pop, inc, dec, neg, not
  /// opc reg64
  ///
  /// * III) leave, ret, nop, cwd, cdq, cqo
  /// opc
  ///
  /// * IV) setCC -> seta, setae, setb, setbe, setc, sete, setg, setge, setl,
  /// setle, setna,
  /// setnae, setnb, setnbe, setnc, setne, setng, setnge, setnl, setnle, setno,
  /// setnp, setns, setnz, seto, setp, setpe, setpo, sets, setz,
  /// opc,reg8
  ///
  /// * V) movsx, movzx
  /// opc reg64,reg8
  /// opc reg64,reg16
  ///
  /// * VI) lea
  /// lea reg64,[reg64]
  /// lea reg64,[reg64+@i32]
  ///
  private final Map<String, AsmDatas> mapping;

  // jumps
  private Set<String> labels; // to check all labels are unique
  private Map<String, Long> labelsOffset;
  private List<AsmLine> lines;

  public Asm86() throws StreamReadException, DatabindException, IOException {
    this.mapping = buildMapping();
    this.labels = new HashSet<>();
    this.labelsOffset = new HashMap<>();
    this.lines = new ArrayList<>();
  }

  // main api
  public int bytesCount() {
    int n = 0;
    for (AsmLine line : lines) {
      if (line.isExucutable()) {
        n += line.bytes.bytes();
      }
    }
    return n;
  }

  public int[] toBytes() {
    int buf[] = new int[bytesCount()];
    int off = 0;
    for (AsmLine line : lines) {
      if (line.isExucutable()) {
        for (int b : line.bytes.toBytes()) {
          buf[off++] = b;
        }
      }
    }
    return buf;
  }

  public String printBytes() {
    StringBuilder sb = new StringBuilder();
    for (AsmLine b : lines) {
      if (b.isLabel()) {
        continue; // label stub
      }
      sb.append(b.bytes.printBytes(false));
      sb.append("\n");
    }
    return sb.toString();
  }

  public String printBytesNewArray() {
    StringBuilder sb = new StringBuilder();
    sb.append("int tmp_buffer[] = { ");
    for (AsmLine b : lines) {
      if (b.isLabel()) {
        continue; // label stub
      }
      sb.append(b.bytes.printBytes(true));
      sb.append("\n");
    }
    sb.append(" };");
    return sb.toString();
  }

  public String printInstr() {
    StringBuilder sb = new StringBuilder();
    for (AsmLine s : lines) {
      sb.append(s.repr);
      sb.append("\n");
    }
    return sb.toString();
  }

  public String printBytesInstr() {
    StringBuilder sb = new StringBuilder();

    for (AsmLine line : lines) {
      String bytes = line.isExucutable() ? line.bytes.printBytes(false) : "";
      String instr = line.repr;
      long address = line.offset;
      sb.append(String.format("%09d: %-26s %s\n", address, bytes, instr));
    }

    return sb.toString();
  }

  private void line(Ubuf buffer, String repr) {
    lines.add(new AsmLine(buffer, repr));
  }

  public void set_rva(final long virtualAddress, ImportSymbols imports, DataSymbols datas) {
    // 1) apply offsets
    long offset = virtualAddress;
    for (AsmLine line : lines) {
      line.offset = offset;
      if (!line.isLabel()) {
        offset += line.bytes.bytes();
      }
    }

    // 2) resolve labels
    for (AsmLine line : lines) {
      if (line.isLabel()) {
        labelsOffset.put(line.label, line.offset);
      }
    }

    long bytesnow = 0;

    // 3) resolve jumps
    for (int i = 0; i < lines.size(); i++) {

      final AsmLine line = lines.get(i);

      if (line.isLabel()) {
        continue;
      }

      bytesnow += line.bytes.toBytes().length;

      final int jmpStub[] = new int[] { 0xE9, 0x00, 0x00, 0x00, 0x00 };

      if (line.isJmpLabel()) {

        // TODO: je, jne
        // opcode + i32
        final long instrSize = jmpStub.length;

        final long labelOffset = labelsOffset.get(line.label);
        final long instrOffset = line.offset;
        final long offsetToTheTarget = labelOffset - instrOffset - instrSize;

        // rewrite the addreess
        Ubuf strm = new Ubuf();
        if (line.opc == Opc.jmp) {
          strm.o1(0xE9);
        } else if (line.opc == Opc.call) {
          strm.o1(0xE8);
        } else {
          throw new RuntimeException("unimplemented jmp opcode: " + line.opc);
        }

        strm.oi4(offsetToTheTarget);
        line.bytes = strm;
      }

      else {

        // TODO: clean this.
        // call dll function
        // load symbol

        Ubuf lineBufExact = line.bytes;
        int lineBuf[] = lineBufExact.toBytes();

        if (lineBuf[0] == 0xff && lineBuf[1] == 0x15) {
          final String symname = line.repr;
          long abs = imports.symbol(symname);
          long rip = virtualAddress + bytesnow;
          long rel_addr = abs - rip;

          Ubuf realbuf = new Ubuf();
          realbuf.o1(0xff);
          realbuf.o1(0x15);
          realbuf.oi4(rel_addr);
          line.bytes = realbuf;
          line.repr = "call " + String.format("[rip+%d] # %s", rel_addr, symname);

        }

        else if (lineBuf[0] == 0x48 && lineBuf[1] == 0x8d) {
          final String symname = line.repr;
          long abs = datas.symbol(symname);
          long rip = virtualAddress + bytesnow;
          long rel_addr = abs - rip;

          Ubuf realbuf = new Ubuf();
          realbuf.o1(lineBuf[0]);
          realbuf.o1(lineBuf[1]);
          realbuf.o1(lineBuf[2]);
          realbuf.oi4(rel_addr);
          line.bytes = realbuf;
          line.repr = "lea rcx" + String.format("[rip+%d] # %s", rel_addr, symname); // TODO: normal description :)
        }
      }

    }
  }

  // keys
  private String k_reg_reg(Opc opc, Reg64 dst, Reg64 src) {
    return opc + " " + dst.toString() + "," + src.toString();
  }

  private String k_reg_mem(Opc opc, Reg64 dst, Reg64 src) {
    return opc + " " + dst.toString() + ",[" + src.toString() + "]";
  }

  private String k_mem_reg(Opc opc, Reg64 dst, Reg64 src) {
    return opc + " [" + dst.toString() + "]," + src.toString();
  }

  private String k_reg_i32(Opc opc, Reg64 dst) {
    return opc + " " + dst.toString() + ",@i32";
  }

  // combinations

  public void reg_reg(Opc opc, Reg64 dst, Reg64 src) {
    final String key = k_reg_reg(opc, dst, src);
    final Ubuf buffer = buildBufferHdr(key);
    line(buffer, opc + " " + dst + "," + src);
  }

  public void reg_mem(Opc opc, Reg64 dst, Reg64 src) {
    final String key = k_reg_mem(opc, dst, src);
    line(buildBufferHdr(key), opc + " " + dst + ",[" + src + "]");
  }

  public void mem_reg(Opc opc, Reg64 dst, Reg64 src) {
    final String key = k_mem_reg(opc, dst, src);
    line(buildBufferHdr(key), opc + " [" + dst + "]," + src);
  }

  public void reg_i32(Opc opc, Reg64 dst, int imm32) {
    final String key = k_reg_i32(opc, dst);
    final Ubuf buffer = buildBufferHdr(key);
    buffer.oi4(imm32);
    line(buffer, opc + " " + dst + "," + imm32);
  }

  public void gen_op0(Opc opc) {
    final String key = opc.toString();
    final Ubuf buffer = buildBufferHdr(key);
    line(buffer, key);
  }

  public void gen_op1(Opc opc, Reg64 dst) {
    final String key = opc + " " + dst.toString();
    final Ubuf buffer = buildBufferHdr(key);
    line(buffer, opc + " " + dst);
  }

  public void gen_op1(Opc opc, int imm32) {
    final String key = opc + " @i32";
    final Ubuf buffer = buildBufferHdr(key);
    buffer.oi4(imm32);
    line(buffer, opc + " " + String.format("%d", imm32));
  }

  public String make_label(String name) {
    if (labels.contains(name)) {
      throw new RuntimeException("label is not unique: " + name);
    }
    labels.add(name);
    lines.add(new AsmLine(name));
    return name;
  }

  public void jmp(String label) {

    Ubuf buffer = new Ubuf();
    buffer.o1(0xE9);
    buffer.o4(0);

    lines.add(new AsmLine(Opc.jmp, buffer, label, "jmp " + label));
  }

  public void call_label(String label) {

    Ubuf buffer = new Ubuf();
    buffer.o1(0xE8);
    buffer.o4(0);

    lines.add(new AsmLine(Opc.call, buffer, label, "call " + label));
  }

  /// Imports, datas

  public void call(String sym) {
    Ubuf buffer = new Ubuf();
    buffer.o1(0xFF);
    buffer.o1(0x15);
    buffer.o4(0);
    line(buffer, sym);
  }

  public void load(Reg64 reg, String dataSym) {
    final String key = "lea " + reg.toString() + ",[rip+@i32]";
    Ubuf buffer = buildBufferHdr(key);
    buffer.o4(0);
    line(buffer, dataSym);
  }

  // builders

  private int[] buildBytes(String from) {
    assert from.length() >= 2;

    // 48 01 c0
    String splitten[] = from.split(" ");
    int res[] = new int[splitten.length];
    int off = 0;

    for (String s : splitten) {
      res[off++] = Integer.parseInt(s, 16);
    }

    return res;
  }

  private Ubuf buildBufferHdr(String key) {
    Ubuf buffer = new Ubuf();

    AsmDatas data = mapping.get(key);
    if (data == null) {
      throw new RuntimeException("cannot find mapping for key:" + key);
    }

    int bytes[] = buildBytes(data.opc);
    for (int b : bytes) {
      buffer.o1(b);
    }

    return buffer;
  }

  // dto's

  private List<AsmDatas> readDatas() throws IOException, StreamReadException, DatabindException {
    ObjectMapper mapper = new ObjectMapper();
    List<AsmDatas> datas = Arrays.asList(mapper.readValue(Paths.get("data/asm/datas.json").toFile(), AsmDatas[].class));
    return datas;
  }

  private Map<String, AsmDatas> buildMapping() throws StreamReadException, DatabindException, IOException {
    List<AsmDatas> datas = readDatas();
    Map<String, AsmDatas> mapping = new HashMap<String, AsmDatas>();

    for (AsmDatas x : datas) {
      AsmDatas old = mapping.put(x.getKey(), x);
      if (old != null) {
        throw new RuntimeException("overriden values are not allowed in this context: " + old.toString());
      }
    }

    return mapping;
  }

  @JsonPropertyOrder({ "key, opc, size" })
  static class AsmDatas {

    /// This class is just a simple DTO, and nothing else...
    ///
    /// { "key": "add rax,rax", "opc": "48 01 c0", "size": 3 },

    private String key;
    private String opc;
    private int size;

    public AsmDatas() {
    }

    public AsmDatas(String key, String opc, int size) {
      this.key = key;
      this.opc = opc;
      this.size = size;
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getOpc() {
      return opc;
    }

    public void setOpc(String opc) {
      this.opc = opc;
    }

    public int getSize() {
      return size;
    }

    public void setSize(int size) {
      this.size = size;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      result = prime * result + ((opc == null) ? 0 : opc.hashCode());
      result = prime * result + size;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      AsmDatas other = (AsmDatas) obj;
      if (key == null) {
        if (other.key != null)
          return false;
      } else if (!key.equals(other.key))
        return false;
      if (opc == null) {
        if (other.opc != null)
          return false;
      } else if (!opc.equals(other.opc))
        return false;
      if (size != other.size)
        return false;
      return true;
    }

    @Override
    public String toString() {
      return "AsmDatas [key=" + key + ", opc=" + opc + ", size=" + size + "]";
    }

  }

  // 1) label:
  // 2) jmp wherever
  // 3) plain code
  static enum AsmLineKind {
      code, label_only, jmp_label,
  }

  static class AsmLine {
    public final AsmLineKind kind;
    public String repr; // toString()

    public long offset;
    public Ubuf bytes;
    public String label;
    public Opc opc;

    public AsmLine(String label) {
      this.kind = AsmLineKind.label_only;
      this.label = label;
      this.repr = label + ":";
    }

    public AsmLine(Ubuf bytes, String repr) {
      this.kind = AsmLineKind.code;
      this.bytes = bytes;
      this.repr = repr;
    }

    public AsmLine(Opc opc, Ubuf bytes, String label, String repr) {
      this.kind = AsmLineKind.jmp_label;
      this.bytes = bytes;
      this.label = label;
      this.repr = repr;
      this.opc = opc;
    }

    public boolean isJmpLabel() {
      return kind == AsmLineKind.jmp_label;
    }

    public boolean isExucutable() {
      return kind == AsmLineKind.code || isJmpLabel();
    }

    public boolean isLabel() {
      return kind == AsmLineKind.label_only;
    }

    @Override
    public String toString() {
      return repr;
    }
  }

}
