package pe.sections;

public class SectionHeaderDesc {
  public String name; // 8 bytes
  public SectionSize size;
  public long flags;

  public SectionHeaderDesc(String name, SectionSize size, long flags) {
    this.name = name;
    this.size = size;
    this.flags = flags;
  }

}
