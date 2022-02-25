package jscan.buffers;

public abstract class Limits {

  public static final long u8_MAX = 255;
  public static final long u16_MAX = 65535;
  public static final long u32_MAX = 4294967295L;

  public static final long i8_MIN = -128;
  public static final long i8_MAX = +127;

  public static final long i16_MIN = -32768;
  public static final long i16_MAX = +32767;

  public static final long i32_MIN = -2147483648;
  public static final long i32_MAX = +2147483647;

  public static final long i64_MIN = 0x8000000000000000L;
  public static final long i64_MAX = +0x7fffffffffffffffL;

  public static final float f32_MIN = 0x0.000002P-126f; // 1.4e-45f
  public static final float f32_MAX = 0x1.fffffeP+127f; // 3.4028235e+38f

  public static final double f64_MIN = 0x0.0000000000001P-1022; // 4.9e-324
  public static final double f64_MAX = 0x1.fffffffffffffP+1023; // 1.7976931348623157e+308
}
