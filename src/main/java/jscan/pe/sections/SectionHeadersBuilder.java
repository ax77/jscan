package jscan.pe.sections;

import java.util.ArrayList;
import java.util.List;

import jscan.pe.ImageSectionHeader;
import jscan.pe.constants.Alignment;
import jscan.pe.constants.Sizeofs;
import jscan.utils.Aligner;

public class SectionHeadersBuilder {
  private List<SectionHeaderDesc> descs;

  public SectionHeadersBuilder() {
    this.descs = new ArrayList<>();
  }

  public void add(String name, SectionSize size, long flags) {
    SectionHeaderDesc d = new SectionHeaderDesc(name, size, flags);
    descs.add(d);
  }

  public List<ImageSectionHeader> build() {
    List<ImageSectionHeader> hdrs = new ArrayList<>(descs.size());

    long raw = Sizeofs.sizeofAllHeaders(descs.size());
    long virtual = Aligner.align(raw, Alignment.SECTION_ALIGNMENT);

    for (SectionHeaderDesc s : descs) {
      ImageSectionHeader header = new ImageSectionHeader(s.name);
      header.Characteristics = s.flags;

      header.VirtualSize = s.size.virtual;
      header.SizeOfRawData = Aligner.align(s.size.raw, Alignment.FILE_ALIGNMENT);

      header.VirtualAddress = virtual;
      header.PointerToRawData = raw;

      virtual = Aligner.incr_check_overflow(virtual, Aligner.align(s.size.virtual, Alignment.SECTION_ALIGNMENT));
      raw = Aligner.incr_check_overflow(raw, Aligner.align(s.size.raw, Alignment.FILE_ALIGNMENT));

      hdrs.add(header);
    }

    return hdrs;
  }
}
