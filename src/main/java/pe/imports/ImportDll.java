package pe.imports;

import java.util.ArrayList;
import java.util.List;

public class ImportDll {
  private final String name;
  private List<ImageImportByName> imports;

  public ImportDll(String name) {
    this.name = name;
    this.imports = new ArrayList<>();
  }

  public void add_procedure(ImageImportByName proc) {
    for (ImageImportByName e : imports) {
      if (e.getName().equals(proc.getName())) {
        throw new RuntimeException("Procedure is already present in DLL: " + proc.getName());
      }
    }
    imports.add(proc);
  }

  public String getName() {
    return name;
  }

  public List<ImageImportByName> getImports() {
    return imports;
  }

  //TODO:test
  public int import_slot(String name) {
    for (int offset = 0; offset < imports.size(); offset += 1) {
      ImageImportByName proc = imports.get(offset);
      if (proc.getName().equals(name)) {
        return offset;
      }
    }
    return -1;
  }
}
