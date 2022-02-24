package pe.sections;

import static pe.constants.SectionFlag.IMAGE_SCN_CNT_CODE;
import static pe.constants.SectionFlag.IMAGE_SCN_CNT_INITIALIZED_DATA;
import static pe.constants.SectionFlag.IMAGE_SCN_MEM_EXECUTE;
import static pe.constants.SectionFlag.IMAGE_SCN_MEM_READ;
import static pe.constants.SectionFlag.IMAGE_SCN_MEM_WRITE;

public class SectionsIndexesLight {
  public static final int TEXT = 0;
  public static final int DATA = 1;
  public static final int IDATA = 2;

  public static long flags(int i) {

    if (i == TEXT) {
      return IMAGE_SCN_CNT_CODE | IMAGE_SCN_MEM_EXECUTE | IMAGE_SCN_MEM_READ;
    }
    if (i == DATA) {
      return IMAGE_SCN_CNT_INITIALIZED_DATA | IMAGE_SCN_MEM_READ | IMAGE_SCN_MEM_WRITE;
    }

    if (i == IDATA) {
      return IMAGE_SCN_CNT_INITIALIZED_DATA | IMAGE_SCN_MEM_READ;
    }

    throw new RuntimeException("unknown index: " + i);
  }
}
