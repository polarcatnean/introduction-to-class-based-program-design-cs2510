package assignment3.practice;

interface ICurrency {
  int add(int amount);
  String getAmount();
}

abstract class AMoney implements ICurrency{
  int amount;
  
  AMoney(int amount) {
    this.amount = amount;
  }
  
  public int add(int amount) {
    return this.amount + amount;
  }
  
  public String getAmount() {
    return String.valueOf(amount).concat(" ").concat(this.currency());
  }

  protected abstract String currency();
}

class Dollar extends AMoney {
  Dollar(int amount) {
    super(amount);
  }

  protected String currency() {
    return "Dollar";
  }
}

class Euro extends AMoney {
  Euro(int amount) {
    super(amount);
  }

  protected String currency() {
    return "Euro";
  }
}

class Pound extends AMoney {
  Pound(int amount) {
    super(amount);
  }

  protected String currency() {
    return "Pound";
  }
}
