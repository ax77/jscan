package jscan.pe;

import jscan.buffers.IDataWriter;

public class DosStub {
  private final int[] stub;

  /// This actually an u8 byte-array, and
  /// we will handle it by writing bytes, not ints...
  ///
  public DosStub() {

    //@formatter:off
    int dos_stub[] = 
     {0x0e,             // push cs
      0x1f,             // pop ds
      0xba, 0x0e, 0x00, // mov dx, 0x000E
      0xb4, 0x09,       // mov ah, 9
      0xcd, 0x21,       // int 0x21
      0xb8, 0x01, 0x4c, // mov ax, 0x4C01
      0xcd, 0x21,       // int 0x21
      'T',  'h',  'i',  's', ' ', 'p', 'r', 'o', 'g', 'r',
      'a',  'm',  ' ',  'c', 'a', 'n', 'n', 'o', 't', ' ',
      'b',  'e',  ' ',  'r', 'u', 'n', ' ', 'i', 'n', ' ',
      'D',  'O',  'S',  ' ', 'm', 'o', 'd', 'e', '.', '\r',
      '\r', '\n', '$',  0,   0,   0,   0,   0,   0,   0};
    this.stub = dos_stub;
    //@formatter:on

  }

  public void write(IDataWriter dw) {
    for (int i = 0; i < stub.length; i += 1) {
      dw.o1(stub[i]);
    }
  }

}
