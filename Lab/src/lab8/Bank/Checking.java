package lab8.Bank;

// Represents a checking account
public class Checking extends Account {
  int minimum; // The minimum account balance allowed

  public Checking(int accountNum, int balance, String name, int minimum) {
    super(accountNum, balance, name);
    this.minimum = minimum;
  }

  int withdraw(int amount) {
    int remaining = this.balance - amount;
    if (remaining < this.minimum) {
      throw new RuntimeException(amount + " is not available");
    }
    this.balance = remaining;
    return this.balance;
  }
}
