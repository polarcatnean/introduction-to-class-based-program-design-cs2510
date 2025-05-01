package lab10;


class Runner {
  int age;
  String name;

  Runner(int age, String name) {
    this.age = age;
    this.name = name;
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Runner)) { return false; }
    
    Runner other = (Runner) o;
    return this.age == other.age && this.name.equals(other.name);
  }
  
  @Override
  public int hashCode() {
    return this.age + this.name.length();
  }
}