package pe;

import pe.constants.Magics;
import buffers.IDataWriter;

/// typedef struct _IMAGE_DOS_HEADER {
///   uint16_t e_magic;
///   uint16_t e_cblp;
///   uint16_t e_cp;
///   uint16_t e_crlc;
///   uint16_t e_cparhdr;
///   uint16_t e_minalloc;
///   uint16_t e_maxalloc;
///   uint16_t e_ss;
///   uint16_t e_sp;
///   uint16_t e_csum;
///   uint16_t e_ip;
///   uint16_t e_cs;
///   uint16_t e_lfarlc;
///   uint16_t e_ovno;
///   uint16_t e_res[4];
///   uint16_t e_oemid;
///   uint16_t e_oeminfo;
///   uint16_t e_res2[10];
///   uint32_t e_lfanew;
/// } IMAGE_DOS_HEADER, *PIMAGE_DOS_HEADER;

public class ImageDosHeader {

  /*uint16_t*/ int e_magic;
  /*uint16_t*/ int e_cblp;
  /*uint16_t*/ int e_cp;
  /*uint16_t*/ int e_crlc;
  /*uint16_t*/ int e_cparhdr;
  /*uint16_t*/ int e_minalloc;
  /*uint16_t*/ int e_maxalloc;
  /*uint16_t*/ int e_ss;
  /*uint16_t*/ int e_sp;
  /*uint16_t*/ int e_csum;
  /*uint16_t*/ int e_ip;
  /*uint16_t*/ int e_cs;
  /*uint16_t*/ int e_lfarlc;
  /*uint16_t*/ int e_ovno;
  /*uint16_t*/ int e_res[];
  /*uint16_t*/ int e_oemid;
  /*uint16_t*/ int e_oeminfo;
  /*uint16_t*/ int e_res2[];
  /*uint32_t*/ long e_lfanew;

  public ImageDosHeader() {
    this.e_magic = Magics.IMAGE_DOS_SIGNATURE; // 0x5A4D -> MZ
    this.e_cblp = 0x0090;
    this.e_cp = 0x0003;
    this.e_cparhdr = 0x0004;
    this.e_maxalloc = 0xFFFF;
    this.e_sp = 0x00B8;
    this.e_lfarlc = 0x0040;
    this.e_lfanew = 0x00000080; // Offset for PE header

    this.e_res = new int[4];
    this.e_res2 = new int[10];
  }

  public void write(IDataWriter dw) {
    dw.o2(e_magic);
    dw.o2(e_cblp);
    dw.o2(e_cp);
    dw.o2(e_crlc);
    dw.o2(e_cparhdr);
    dw.o2(e_minalloc);
    dw.o2(e_maxalloc);
    dw.o2(e_ss);
    dw.o2(e_sp);
    dw.o2(e_csum);
    dw.o2(e_ip);
    dw.o2(e_cs);
    dw.o2(e_lfarlc);
    dw.o2(e_ovno);

    for (int i = 0; i < e_res.length; i++) {
      dw.o2(e_res[i]);
    }

    dw.o2(e_oemid);
    dw.o2(e_oeminfo);

    for (int i = 0; i < e_res2.length; i++) {
      dw.o2(e_res2[i]);
    }

    dw.o4(e_lfanew);
  }

}
