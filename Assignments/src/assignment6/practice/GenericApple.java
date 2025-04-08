package assignment6.practice;

interface IComp {
  boolean lessThan(IComp other);
}

class Apple implements IComp {
  int weight;
  int ripeness;

  public boolean lessThan(IComp other) {
    // TODO Auto-generated method stub
    return false;
  }
  
}

class Orange implements IComp {
  int weight;
  int ripeness;

  public boolean lessThan(IComp other) {
    // TODO Auto-generated method stub
    return false;
  }
}