package pe.sections;

public class SectionSize {
  public long virtual;
  public long raw;

  public SectionSize(long raw) {
    this.virtual = raw;
    this.raw = raw;
  }

  public SectionSize(long virtual, long raw) {
    this.virtual = virtual;
    this.raw = raw;
  }

}
