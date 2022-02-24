package pewriter;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class Asm86Gen {

  final String header = ".intel_syntax noprefix\n" + ".bss\n" + ".data\n" + ".text\n" + ".global main\n main:\n\n";

  private List<String> readf(String fname) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(fname));

    List<String> lines = new ArrayList<String>();
    String maybe = br.readLine();
    while (maybe != null) {
      String line = maybe.trim();
      if (!line.isEmpty()) {
        lines.add(line);
      }
      maybe = br.readLine();
    }

    br.close();
    return lines;
  }

  @SuppressWarnings("unused")
  private void merge() throws IOException {
    FileWriter fout = new FileWriter(dir() + "/temps/datas.json");
    fout.write("[\n");

    final String imax = "0x7fffffff";
    final String imaxle = "ff ff ff 7f";

    final String dir = dir();
    List<String> asm = readf(dir + "/temps/asm.txt");
    List<String> dis = readf(dir + "/temps/disasm.txt");
    assert asm.size() == dis.size();

    // mov rax,0x7fffffff
    // 1004010e0: 48 c7 c0 ff ff ff 7f mov rax,0x7fffffff

    int len = asm.size();
    for (int i = 0; i < len; i += 1) {
      check(asm.get(i).trim().toLowerCase(), dis.get(i).trim().toLowerCase());

      String asmLine = asm.get(i).trim().toLowerCase();
      String bytes = parse(dis.get(i).trim().toLowerCase());

      final int insLen = bytes.split(" ").length;

      if (asmLine.contains(imax)) {
        assertTrue(bytes.endsWith(" " + imaxle));

        asmLine = asmLine.replace(imax, "@i32");

        final int delta = bytes.length() - imaxle.length();
        bytes = bytes.substring(0, delta).trim();
      }

      String result = String.format("{ \"key\": \"%s\", \"opc\": \"%s\", \"size\": %d }", asmLine, bytes, insLen);
      fout.write(result);

      if (i + 1 < len) {
        fout.write(",");
      }
      fout.write("\n");
    }

    fout.write("]\n");
    fout.close();
  }

  private void check(String asm, String disasm) {
    String opc = "";
    for (char c : asm.toCharArray()) {
      if (c == ' ') {
        break;
      }
      opc += c;
    }
    if (!disasm.contains(" " + opc)) {
      String msg = String.format("cannot find the original opcode [%s] from the asm line [%s] in the disasm line [%s]",
          opc, asm, disasm);
      throw new RuntimeException(msg);
    }
  }

  private int indexOfOpcode(String in) {
    String[][] opcodes = allOpcodes();

    for (String list[] : opcodes) {
      for (String o : list) {
        String opc = " " + o.toString();
        int indexOf = in.indexOf(opc);
        if (indexOf != -1) {
          return indexOf;
        }
      }
    }
    throw new RuntimeException("there's no opcode in the string: " + in);
  }

  private String[][] allOpcodes() {
    String opcodes[][] = new String[][] { opcodes0(), opcodes1(), opcodes2(), setcOpcodes(), movzsxOpcodes(),
        shiftOpcodes(), new String[] { "lea" } };
    return opcodes;
  }

  private String[] setcOpcodes() {
    // return new String[] { "seta", "setae", "setb", "setbe", "setc", "sete",
    // "setg", "setge", "setl", "setle", "setna",
    // "setnae", "setnb", "setnbe", "setnc", "setne", "setng", "setnge", "setnl",
    // "setnle", "setno", "setnp", "setns",
    // "setnz", "seto", "setp", "setpe", "setpo", "sets", "setz", };

    return new String[] { "sete", "setne", };
  }

  private String[] opcodes0() {
    // "leave",
    // "nop",
    return new String[] { "ret", "cwd", "cdq", "cqo" };
  }

  private String[] opcodes1() {
    return new String[] { "push", "pop", "inc", "dec", "neg", "not" };
  }

  private String[] opcodes2() {
    return new String[] { "mov", "add", "or", "adc", "sbb", "and", "sub", "xor", "cmp", };
  }

  private String[] movzsxOpcodes() {
    return new String[] { "movsx", "movzx" };
  }

  /// The shift arithmetic left (SAL) and shift logical left (SHL) instructions
  /// perform the same operation;
  /// they shift the bits in the destination operand to the left (toward more
  /// significant bit locations).
  private String[] shiftOpcodes() {
    return new String[] { "shl", "shr", "sar", };
  }

  private String parse(String disLine) {
    disLine = disLine.replaceAll("\t", " ");
    disLine = disLine.toLowerCase();
    disLine = disLine.trim();

    // 1004010e0: 48 c7 c0 ff ff ff 7f mov rax,0x7fffffff
    assertTrue(disLine.charAt(9) == ':');
    int opcodePos = indexOfOpcode(disLine);
    return disLine.substring(10, opcodePos).trim();
  }

  @SuppressWarnings("unused")
  private void gen() throws IOException {
    FileWriter fw = new FileWriter(dir() + "/temps/generated.txt");
    fw.write(header);

    String regs8[] = { "al", "cl", "dl", "bl", "spl", "bpl", "sil", "dil", "r8b", "r9b", "r10b", "r11b", "r12b", "r13b",
        "r14b", "r15b", };

    String regs16[] = { "ax", "cx", "dx", "bx", "sp", "bp", "si", "di", "r8w", "r9w", "r10w", "r11w", "r12w", "r13w",
        "r14w", "r15w", };

    String regs64[] = { "rax", "rcx", "rdx", "rbx", "rsp", "rbp", "rsi", "rdi", "r8", "r9", "r10", "r11", "r12", "r13",
        "r14", "r15" };

    String regs64Rip[] = { "rax", "rcx", "rdx", "rbx", "rsp", "rbp", "rsi", "rdi", "r8", "r9", "r10", "r11", "r12",
        "r13", "r14", "r15", "rip" };

    // the main thing
    for (String o : opcodes2()) {
      for (String dst : regs64) {
        fw.write(String.format("%s %s,%s\n", o, dst, "0x7fffffff"));

        for (String src : regs64) {
          fw.write(String.format("%s %s,%s\n", o, dst, src));
          fw.write(String.format("%s %s,[%s]\n", o, dst, src));
          fw.write(String.format("%s [%s],%s\n", o, dst, src));
          fw.write(String.format("%s %s,[%s+0x7fffffff]\n", o, dst, src));
          fw.write(String.format("%s [%s+0x7fffffff],%s\n", o, dst, src));
        }
      }
    }

    // movzx, movsx
    String lowregs[][] = { regs8, regs16 };
    for (String o : movzsxOpcodes()) {
      for (String reglist[] : lowregs) {
        for (String dst : regs64) {
          for (String src : reglist) {
            fw.write(String.format("%s %s,%s\n", o, dst, src));
          }
        }
      }
    }

    // push, pop, etc
    for (String o : opcodes1()) {
      for (String dst : regs64) {
        fw.write(String.format("%s %s\n", o, dst));
      }
    }

    // setCC
    for (String o : setcOpcodes()) {
      for (String dst : regs8) {
        fw.write(String.format("%s %s\n", o, dst));
      }
    }

    // nops
    for (String o : opcodes0()) {
      fw.write(String.format("%s\n", o));
    }

    // lea
    for (String dst : regs64) {
      for (String src : regs64Rip) {
        fw.write(String.format("lea %s,[%s]\n", dst, src));
        fw.write(String.format("lea %s,[%s+0x7fffffff]\n", dst, src));
      }
    }

    // shifts
    for (String o : shiftOpcodes()) {
      for (String dst : regs64) {
        fw.write(String.format("%s %s,cl\n", o, dst));
      }
    }

    // other
    fw.write("push 0x7fffffff\n");
    for (String o : new String[] { "push", "pop" })
      for (String dst : regs64) {
        fw.write(String.format("%s [%s]\n", o, dst));
      }

    fw.close();

    printOpcodes();
  }

  private void printOpcodes() {
    List<String> opcodes = new ArrayList<>();
    for (String list[] : allOpcodes()) {
      for (String o : list) {
        opcodes.add(o);
      }
    }
    Collections.sort(opcodes);
    for (String o : opcodes) {
      System.out.printf("%s,\n", o);
    }
  }

  private String dir() {
    return System.getProperty("user.dir");
  }

  @Test
  public void test() throws IOException {
    //gen();
    //merge();
  }

}
