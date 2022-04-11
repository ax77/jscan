package pewriter;

import static jscan.asm.Opc.add;
import static jscan.asm.Opc.mov;
import static jscan.asm.Opc.pop;
import static jscan.asm.Opc.push;
import static jscan.asm.Opc.ret;
import static jscan.asm.Opc.sub;
import static jscan.asm.Reg64.r8;
import static jscan.asm.Reg64.r9;
import static jscan.asm.Reg64.rax;
import static jscan.asm.Reg64.rbp;
import static jscan.asm.Reg64.rcx;
import static jscan.asm.Reg64.rdx;
import static jscan.asm.Reg64.rsp;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import jscan.asm.Asm86;
import jscan.pe.PeMainWriter;
import jscan.pe.datas.DataSymbols;
import jscan.pe.imports.ImageImportByName;
import jscan.pe.imports.ImportDll;
import jscan.pe.imports.ImportSymbols;

public class PeWriterTestApi {

  private ImportSymbols construct_iat() {
    ImportSymbols imports = new ImportSymbols();

    final ImportDll kernelDLL = new ImportDll("KERNEL32.dll");
    kernelDLL.add_procedure(new ImageImportByName("ExitProcess", 0x120));

    final ImportDll msvcrtDLL = new ImportDll("msvcrt.dll");
    msvcrtDLL.add_procedure(new ImageImportByName("printf", 0x48b));
    msvcrtDLL.add_procedure(new ImageImportByName("scanf", 0x49b));
    msvcrtDLL.add_procedure(new ImageImportByName("strlen", 0));

    imports.add_dll(kernelDLL);
    imports.add_dll(msvcrtDLL);

    imports.prepare();
    return imports;
  }

  final String fmt = "%d %d %d";

  private DataSymbols construct_datas() {

    DataSymbols datas = new DataSymbols();
    datas.add(fmt);
    return datas;
  }

  private Asm86 construct_code() throws StreamReadException, DatabindException, IOException {

    Asm86 asm = new Asm86();
    String label = "label";

    // main:
    asm.gen_op1(push, rbp);
    asm.reg_reg(mov, rbp, rsp);
    asm.reg_i32(sub, rsp, 64);

    // code+
    // Windows calling: rcx,rdx,r8,r9
    // Unix calling   : rdi,rsi,rdx,rcx,r8,r9
    asm.gen_op1(push, 2005);
    asm.gen_op1(push, 2006);
    asm.gen_op1(push, 2007);
    asm.gen_op1(pop, r9);
    asm.gen_op1(pop, r8);
    asm.gen_op1(pop, rdx);
    asm.load(rcx, fmt);
    asm.call("printf");
    asm.reg_i32(mov, rax, 34);
    asm.jmp(label);
    asm.reg_i32(mov, rax, 52);
    // code-

    asm.make_label(label);
    asm.reg_i32(add, rsp, 64);
    asm.gen_op1(pop, rbp);
    asm.gen_op0(ret);

    return asm;
  }

  @Test
  public void write() throws IOException {

    DataSymbols datas = construct_datas();

    ImportSymbols imports = construct_iat();

    Asm86 code = construct_code();

    PeMainWriter writer = new PeMainWriter(datas, imports, code);
    writer.write("/data/test_pe/pewriter5.exe");

  }

}
