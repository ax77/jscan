package jscan.preproc.preprocess.args;

public class ArgInfo {
  private ArgInfoVararg info;

  public ArgInfo() {
    this.info = ArgInfoVararg.noinfo;
  }

  public ArgInfoVararg getInfo() {
    return info;
  }

  public void setInfo(ArgInfoVararg info) {
    this.info = info;
  }

  @Override
  public String toString() {
    return "ArgInfo [info=" + info + "]";
  }

}