package jscan.buffers;

import java.io.FileOutputStream;
import java.io.IOException;

import jscan.utils.Aligner;

public class Ubuf implements IDataWriter {

  static class Uv {
    long v;
    int sz;

    public Uv(long v, int sz) {
      boolean sizeIsOk = sz == 1 || sz == 2 || sz == 3 || sz == 4 || sz == 8;
      if (!sizeIsOk) {
        throw new NumberFormatException("Unexpected size: " + sz);
      }

      this.v = v;
      this.sz = sz;
    }

    @Override
    public String toString() {
      String pref = "u8";
      if (sz == 2) {
        pref = "u16";
      }
      if (sz == 4) {
        pref = "u32";
      }
      if (sz == 8) {
        pref = "u64";
      }
      return pref + ":" + v;
    }
  }

  /// The main idea is simple enough.
  /// We have to slightly check whether the digit is unsigned, and whether it fits the range.
  /// It is easy with u8/u16/u32, but a little annoying with u64.
  /// Why do we not to use a byte-buffer instead?
  /// Because of the debugging.
  /// When you see a bunch of negative bytes and cannot understand what is going on here.
  ///

  private Uv[] buf;
  private int count;
  private int alloc;
  private int bytes;

  public Ubuf() {
    this.alloc = 8;
    this.buf = emptyTab(this.alloc);
  }

  private Uv[] emptyTab(int need) {
    Uv[] nbuf = new Uv[need];
    for (int i = 0; i < need; i += 1) {
      nbuf[i] = new Uv(0, 1);
    }
    return nbuf;
  }

  public long val(int at) {
    return buf[at].v;
  }

  private void out(Uv u) {

    if (this.count >= this.alloc) {
      this.alloc *= 2;
      Uv[] nbuf = emptyTab(this.alloc);

      for (int i = 0; i < this.count; i += 1) {
        nbuf[i] = buf[i];
      }
      this.buf = nbuf;
    }

    this.buf[this.count] = u;
    this.count += 1;
    this.bytes += u.sz;
  }

  /// u8
  public void o1(int v) {
    //TODO:
    lim(v, Limits.u8_MAX);
    out(new Uv(v, 1));
  }

  /// u16
  public void o2(int v) {
    lim(v, Limits.u16_MAX);
    out(new Uv(v, 2));
  }

  /// u32
  public void o4(long v) {
    lim(v, Limits.u32_MAX);
    out(new Uv(v, 4));
  }

  /// i32
  public void oi4(long v) {
    ilim(v, Limits.i32_MIN, Limits.i32_MAX);
    out(new Uv(v, 4));
  }

  public void o3(long v) {
    lim(v, Limits.u32_MAX);
    out(new Uv(v, 3));
  }

  /// u64
  /// TODO: how to handle u64 with a BigDecimal?
  public void o8(long v) {
    chkNeg(v);
    out(new Uv(v, 8));
  }

  public void oArr(int[] bytes) {
    for (int i = 0; i < bytes.length; i += 1) {
      int b = bytes[i];
      o1(b);
    }
  }

  private void chkNeg(long v) {
    if (v < 0) {
      throw new NumberFormatException("Negative value, unexpected: " + v);
    }
  }

  private void lim(long v, long max) {
    chkNeg(v);
    if (v > max) {
      throw new NumberFormatException("Value is out of range: " + v + ", limit is: " + max);
    }
  }

  private void ilim(long v, long min, long max) {
    if (v > max) {
      throw new NumberFormatException("Value is out of range: " + v + ", limit is: " + max);
    }
    if (v < min) {
      throw new NumberFormatException("Value is out of range: " + v + ", limit is: " + max);
    }
  }

  public int count() {
    return count;
  }

  public int bytes() {
    return bytes;
  }

  public void alignUp(int i) {
    if (i <= 0) {
      throw new RuntimeException("Negative or zero alignment factor.");
    }
    if (i % 2 != 0) {
      throw new RuntimeException("The alignment factor is not a ^2 multiplier.");
    }

    int top = Aligner.align(bytes, i);
    if (top < bytes) {
      throw new RuntimeException("Unexpected, the alignment is not fit enough the count of bytes. idn.");
    }

    int delta = top - bytes;
    for (int j = 0; j < delta; j += 1) {
      o1(0);
    }
  }

  public int[] toBytes() {

    int result[] = new int[bytes];
    int offset = 0;

    for (int i = 0; i < count; i += 1) {
      Uv u = buf[i];
      long v = u.v;

      if (u.sz == 1) {
        result[offset++] = (int) (v & 0xff);
      }
      if (u.sz == 2) {
        result[offset++] = (int) (v & 0xff);
        result[offset++] = (int) (v >>> 8 & 0xff);
      }
      if (u.sz == 3) {
        result[offset++] = (int) (v & 0xff);
        result[offset++] = (int) (v >>> 8 & 0xff);
        result[offset++] = (int) (v >>> 16 & 0xff);
      }
      if (u.sz == 4) {
        result[offset++] = (int) (v & 0xff);
        result[offset++] = (int) (v >>> 8 & 0xff);
        result[offset++] = (int) (v >>> 16 & 0xff);
        result[offset++] = (int) (v >>> 24 & 0xff);
      }
      if (u.sz == 8) {
        result[offset++] = (int) (v & 0xff);
        result[offset++] = (int) (v >>> 8 & 0xff);
        result[offset++] = (int) (v >>> 16 & 0xff);
        result[offset++] = (int) (v >>> 24 & 0xff);
        result[offset++] = (int) (v >>> 32 & 0xff);
        result[offset++] = (int) (v >>> 40 & 0xff);
        result[offset++] = (int) (v >>> 48 & 0xff);
        result[offset++] = (int) (v >>> 56 & 0xff);
      }
    }

    return result;
  }

  public String printBytes(boolean ox) {
    StringBuilder sb = new StringBuilder();
    int bytes[] = toBytes();
    for (int i = 0; i < bytes.length; i += 1) {
      int b = bytes[i];
      if (ox) {
        sb.append(String.format("0x%02x,", b));
      } else {
        sb.append(String.format("%02x ", b));
      }
      if ((i + 1) % 8 == 0) {
        sb.append("\n");
      }
    }
    return sb.toString().trim();
  }

  /// public byte[] toBytes() {
  ///   int x[] = toU8Bytes();
  ///   byte b[] = new byte[x.length];
  ///   for (int i = 0; i < x.length; i += 1) {
  ///     b[i] = (byte) (x[i] & 255);
  ///   }
  ///   return b;
  /// }

  public void fout(String filename) throws IOException {
    FileOutputStream fos = new FileOutputStream(filename);
    int[] x = toBytes();
    for (int i = 0; i < x.length; i += 1) {
      fos.write(x[i]);
    }
    fos.flush();
    fos.close();
  }

  @Override
  public String toString() {
    return "Ubuf{" + ", count=" + count + ", alloc=" + alloc + ", bytes=" + bytes + '}';
  }

  @Override
  public void oUtf(String s) {
    final byte[] content = s.getBytes();
    for (byte b : content) {
      o1(b);
    }
    //o1(0);
  }

  @Override
  public void oUtf(String s, int len) {
    byte[] content = s.getBytes();
    int i = 0;
    for (; i < content.length && i < len; i++) {
      o1(content[i]);
    }
    for (; i < len; i++) {
      o1(0);
    }
  }

}

/// CHAR_BIT    8                     Defines the number of bits in a byte.
/// SCHAR_MIN   -128                  Defines the minimum value for a signed char.
/// SCHAR_MAX   +127                  Defines the maximum value for a signed char.
/// UCHAR_MAX   255                   Defines the maximum value for an unsigned char.
/// CHAR_MIN    -128                  Defines the minimum value for type char and its value will be equal to SCHAR_MIN if char represents negative values, otherwise zero.
/// CHAR_MAX    +127                  Defines the value for type char and its value will be equal to SCHAR_MAX if char represents negative values, otherwise UCHAR_MAX.
/// MB_LEN_MAX  16                    Defines the maximum number of bytes in a multi-byte character.
/// SHRT_MIN    -32768                Defines the minimum value for a short int.
/// SHRT_MAX    +32767                Defines the maximum value for a short int.
/// USHRT_MAX   65535                 Defines the maximum value for an unsigned short int.
/// INT_MIN     -2147483648           Defines the minimum value for an int.
/// INT_MAX     +2147483647           Defines the maximum value for an int.
/// UINT_MAX    4294967295            Defines the maximum value for an unsigned int.
/// LONG_MIN    -9223372036854775808  Defines the minimum value for a long int.
/// LONG_MAX    +9223372036854775807  Defines the maximum value for a long int.
/// ULONG_MAX   18446744073709551615  Defines the maximum value for an unsigned long int.
