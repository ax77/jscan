package literals;

import java.util.ArrayList;
import java.util.List;

import buffers.Limits;

public abstract class IntLiteralTypeRepr {

  static class MinMax {
    public final long min;
    public final long max;
    public final IntLiteralType type;

    public MinMax(long min, long max, IntLiteralType type) {
      this.min = min;
      this.max = max;
      this.type = type;
    }
  }

  public static IntLiteralType reprSigned(long v) {
    List<MinMax> mm = new ArrayList<>();
    mm.add(new MinMax(Limits.i8_MIN, Limits.i8_MAX, IntLiteralType.I8));
    mm.add(new MinMax(Limits.i16_MIN, Limits.i16_MAX, IntLiteralType.I16));
    mm.add(new MinMax(Limits.i32_MIN, Limits.i32_MAX, IntLiteralType.I32));
    mm.add(new MinMax(Limits.i64_MIN, Limits.i64_MAX, IntLiteralType.I64));

    for (MinMax e : mm) {
      if (v >= e.min && v <= e.max) {
        return e.type;
      }
    }

    throw new RuntimeException(String.format("cannot decide the range for the given number: %d", v));
  }

  public static IntLiteralType reprUnsigned(long v) {
    assert v >= 0;

    List<MinMax> mm = new ArrayList<>();
    mm.add(new MinMax(0, Limits.u8_MAX, IntLiteralType.U8));
    mm.add(new MinMax(0, Limits.u16_MAX, IntLiteralType.U16));
    mm.add(new MinMax(0, Limits.u32_MAX, IntLiteralType.U32));

    for (MinMax e : mm) {
      if (v <= e.max) {
        return e.type;
      }
    }

    throw new RuntimeException(String.format("cannot decide the range for the given number: %d", v));
  }

}
