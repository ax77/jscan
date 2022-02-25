package jscan.pe;

import jscan.buffers.IDataWriter;

public class PE64 {
  private final ImageDosHeader imageDosHeader;
  private final DosStub dosStub;
  private final ImageNtHeader64 imageNtHeader64;

  public PE64(ImageDosHeader imageDosHeader, DosStub dosStub, ImageNtHeader64 imageNtHeader64) {
    this.imageDosHeader = imageDosHeader;
    this.dosStub = dosStub;
    this.imageNtHeader64 = imageNtHeader64;
  }

  public void write(IDataWriter dw) {
    imageDosHeader.write(dw);
    dosStub.write(dw);
    imageNtHeader64.write(dw);
  }

}
