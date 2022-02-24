package pe.imports;

/// typedef struct _IMAGE_THUNK_DATA64 {
///   union {
///       ULONGLONG ForwarderString;
///       ULONGLONG Function;
///       ULONGLONG Ordinal;
///       ULONGLONG AddressOfData;
///   } u1;
/// } IMAGE_THUNK_DATA64;
/// typedef IMAGE_THUNK_DATA64 *PIMAGE_THUNK_DATA64;

public class ImageThunkData64 {

  /// Note: actually, we do not need this class.
  /// Because we have to write a single u64.
  /// But: for convenience, and understanding of what is going on we keep it.

  public long Function;

  public ImageThunkData64() {
  }

  public ImageThunkData64(long function) {
    Function = function;
  }

  public boolean is_sentinel() {
    return Function == 0;
  }
}
