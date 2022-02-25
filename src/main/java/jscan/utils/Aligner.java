package jscan.utils;

public abstract class Aligner {

  public static int align(int value, int alignment) {
    if (alignment <= 0) {
      throw new RuntimeException("negative or zero alignment.");
    }
    int mod = value % alignment;
    if (mod != 0) {
      return value + alignment - mod;
    }
    return value;
  }

  public static long align(long value, long alignment) {
    if (alignment <= 0) {
      throw new RuntimeException("negative or zero alignment.");
    }
    long mod = value % alignment;
    if (mod != 0) {
      return value + alignment - mod;
    }
    return value;
  }

  public static int incr_check_overflow(int base, int offset) {
    if (base + offset < base) {
      throw new RuntimeException("int addition: address overflow, base=" + base + ", offset=" + offset);
    }
    return base + offset;
  }

  public static long incr_check_overflow(long base, long offset) {
    if (base + offset < base) {
      throw new RuntimeException("long addition: address overflow, base=" + base + ", offset=" + offset);
    }
    return base + offset;
  }
}
