package pe.imports;

public class ImageImportByName {

  private final int hint; // WORD, u16
  private final String name;

  public ImageImportByName(String name, int hint) {
    this.name = name;
    this.hint = hint;
  }

  public String getName() {
    return name;
  }

  public int getHint() {
    return hint;
  }

}
