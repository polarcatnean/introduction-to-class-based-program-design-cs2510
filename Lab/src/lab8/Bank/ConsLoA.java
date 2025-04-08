package lab8.Bank;


// Represents a non-empty List of Accounts...
public class ConsLoA implements ILoA {
  Account first;
  ILoA rest;

  public ConsLoA(Account first, ILoA rest) {
    this.first = first;
    this.rest = rest;
  }

  /* Template
   *  Fields:
   *    ... this.first ...         --- Account
   *    ... this.rest ...          --- ILoA
   *
   *  Methods:
   *
   *  Methods for Fields:
   *
   */


  public ILoA remove(int acctNo) {
    if (this.first.accountNum == acctNo) {
      return this.rest;
    }
    return new ConsLoA(this.first, this.rest.remove(acctNo));
  }

  public void deposit(int amount, int acctNo) {
    if (this.first.accountNum == acctNo) {
      this.first.deposit(amount);
    }
    else {
      this.rest.deposit(amount, acctNo);
    }
  }

  public void withdraw(int amount, int acctNo) {
    if (this.first.accountNum == acctNo) {
      this.first.withdraw(amount);
    }
    else {
      this.rest.withdraw(amount, acctNo);
    }
  }

 
    
}