
package jscan.pe.constants;

public interface SectionFlag {
  public static final long IMAGE_SCN_RESERVED_1 = 0x00000000L;
  public static final long IMAGE_SCN_RESERVED_2 = 0x00000001L;
  public static final long IMAGE_SCN_RESERVED_3 = 0x00000002L;
  public static final long IMAGE_SCN_RESERVED_4 = 0x00000004L;
  public static final long IMAGE_SCN_TYPE_NO_PAD = 0x00000008L;
  public static final long IMAGE_SCN_RESERVED_5 = 0x00000010L;
  public static final long IMAGE_SCN_CNT_CODE = 0x00000020L;
  public static final long IMAGE_SCN_CNT_INITIALIZED_DATA = 0x00000040L;
  public static final long IMAGE_SCN_CNT_UNINITIALIZED_DATA = 0x00000080L;
  public static final long IMAGE_SCN_LNK_OTHER = 0x00000100L;
  public static final long IMAGE_SCN_LNK_INFO = 0x00000200L;
  public static final long IMAGE_SCN_RESERVED_6 = 0x00000400L;
  public static final long IMAGE_SCN_LNK_REMOVE = 0x00000800L;
  public static final long IMAGE_SCN_LNK_COMDAT = 0x00001000L;
  public static final long IMAGE_SCN_GPREL = 0x00008000L;
  public static final long IMAGE_SCN_MEM_PURGEABLE = 0x00020000L;
  public static final long IMAGE_SCN_MEM_16BIT = 0x00020000L;
  public static final long IMAGE_SCN_MEM_LOCKED = 0x00040000L;
  public static final long IMAGE_SCN_MEM_PRELOAD = 0x00080000L;
  public static final long IMAGE_SCN_ALIGN_1BYTES = 0x00100000L;
  public static final long IMAGE_SCN_ALIGN_2BYTES = 0x00200000L;
  public static final long IMAGE_SCN_ALIGN_4BYTES = 0x00300000L;
  public static final long IMAGE_SCN_ALIGN_8BYTES = 0x00400000L;
  public static final long IMAGE_SCN_ALIGN_16BYTES = 0x00500000L;
  public static final long IMAGE_SCN_ALIGN_32BYTES = 0x00600000L;
  public static final long IMAGE_SCN_ALIGN_64BYTES = 0x00700000L;
  public static final long IMAGE_SCN_ALIGN_128BYTES = 0x00800000L;
  public static final long IMAGE_SCN_ALIGN_256BYTES = 0x00900000L;
  public static final long IMAGE_SCN_ALIGN_512BYTES = 0x00A00000L;
  public static final long IMAGE_SCN_ALIGN_1024BYTES = 0x00B00000L;
  public static final long IMAGE_SCN_ALIGN_2048BYTES = 0x00C00000L;
  public static final long IMAGE_SCN_ALIGN_4096BYTES = 0x00D00000L;
  public static final long IMAGE_SCN_ALIGN_8192BYTES = 0x00E00000L;
  public static final long IMAGE_SCN_LNK_NRELOC_OVFL = 0x01000000L;
  public static final long IMAGE_SCN_MEM_DISCARDABLE = 0x02000000L;
  public static final long IMAGE_SCN_MEM_NOT_CACHED = 0x04000000L;
  public static final long IMAGE_SCN_MEM_NOT_PAGED = 0x08000000L;
  public static final long IMAGE_SCN_MEM_SHARED = 0x10000000L;
  public static final long IMAGE_SCN_MEM_EXECUTE = 0x20000000L;
  public static final long IMAGE_SCN_MEM_READ = 0x40000000L;
  public static final long IMAGE_SCN_MEM_WRITE = 0x80000000L;
}
