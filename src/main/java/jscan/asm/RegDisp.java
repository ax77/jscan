package jscan.asm;

public class RegDisp {
  private final Reg64 reg;
  private final int disp32;

  public RegDisp(Reg64 reg, int disp32) {
    this.reg = reg;
    this.disp32 = disp32;
  }

  public Reg64 getReg() {
    return reg;
  }

  public int getDisp32() {
    return disp32;
  }

}
