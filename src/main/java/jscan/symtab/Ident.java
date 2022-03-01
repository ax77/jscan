package jscan.symtab;

import jscan.preproc.Sym;

public final class Ident {

  private final String name;
  private Sym sym;
  private int ns;

  public Ident(String name) {
    this.name = name;
    this.ns = 0;
  }

  public Ident(String name, int ns) {
    this.name = name;
    this.ns = ns;
  }

  public Sym getSym() {
    return sym;
  }

  public void defineSym(Sym newsym) {

    if (getSym() != null) {
      if (getSym().incorrectRedefined(newsym)) {

        System.out
            .println(newsym.getLocation() + " warning: " + newsym.getName() + " redefined: \n" + newsym.buildRedefInfo());

        System.out.println(getSym().getLocation() + " note: this is the location of previous definition: \n"
            + getSym().buildRedefInfo());
      }
    }

    this.sym = newsym;
  }

  public void undefSym() {
    this.sym = null;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {
    return "Ident [name=" + name + ", sym=" + sym + "]";
  }

  // XXX:XXX: do __NOT__ compare ident by namespace !!! 
  //

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Ident other = (Ident) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  public int getNs() {
    return ns;
  }

  public void setNs(int ns) {
    this.ns = ns;
  }

  public boolean isBuiltin() {
    return ns != 0;
  }

  public void setSymNull() {
    this.sym = null;
  }

}
