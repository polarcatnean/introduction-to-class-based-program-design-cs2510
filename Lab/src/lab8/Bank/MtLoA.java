package lab8.Bank;

// Represents the empty List of Accounts
public class MtLoA implements ILoA {  
  MtLoA() {}
  
  public ILoA remove(int acctNo) {
    return this;
  }

  public void deposit(int amount, int acctNo) {
    throw new RuntimeException("Account number not found.");
  }

  public void withdraw(int amount, int acctNo) {
    throw new RuntimeException("Account number not found.");   
  }
}

