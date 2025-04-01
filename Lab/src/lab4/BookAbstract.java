package lab4;
import tester.Tester;

interface IBook {
  // consumes the number that represents today in the library date-recording system 
  // and produces the number of days this book is overdue.
  int daysOverdue(int today);
  boolean isOverdue(int day);
  
  // fees 10c/d for ref&reg, 20c/d for audio
  int computeFine(int day);
}

abstract class ABook implements IBook {
  String title;
  int dayTaken;
  
  ABook(String title, int dayTaken) {
    this.title = title;
    this.dayTaken = dayTaken;
  }
  
  public int daysOverdue(int today) {
    return today - this.dayTaken - 14 ;
  }
  
  public boolean isOverdue(int day) {
    return this.daysOverdue(day) > 0;
  }
  
  public int computeFine(int day) {
    if (this.daysOverdue(day) < 0) {
      return 0;
    }
    return this.daysOverdue(day) * 10;
  }
}

class Book extends ABook {
  String author;
  
  Book(String title, String author, int dayTaken) {
    super(title, dayTaken);
    this.author = author;
  }
}

class RefBook extends ABook {
  RefBook(String title, int dayTaken) {
    super(title, dayTaken);
  }
  
  @Override 
  public int daysOverdue(int today) {
    return today - this.dayTaken - 2;
  }
}

class AudioBook extends ABook {
  String author;
  
  AudioBook(String title, String author, int dayTaken) {
    super(title, dayTaken);
    this.author = author;
  }
  
  @Override
  public int computeFine(int day) {
    if (this.daysOverdue(day) < 0) {
      return 0;
    }
    return this.daysOverdue(day) * 20;
  }
}

class ExamplesBooks {
  
  IBook book1 = new Book("Onyx Storm", "Rebecca Yarros", 7778);
  IBook audio1 = new AudioBook("The Let Them Theory", "Mel Robbins", 8820);
  IBook ref1 = new RefBook("Midnight Black", 8828);
  
  boolean testDaysOverdue(Tester t) {
    return t.checkExpect(book1.daysOverdue(7792), 0)
        && t.checkExpect(book1.daysOverdue(7778), -14)
        && t.checkExpect(book1.daysOverdue(7800), 8)
        && t.checkExpect(ref1.daysOverdue(8838), 8);
  }
  
  boolean testIsOverdue(Tester t) {
    return t.checkExpect(book1.isOverdue(7792), false)
        && t.checkExpect(book1.isOverdue(7783), false)
        && t.checkExpect(book1.isOverdue(7800), true)
        && t.checkExpect(ref1.isOverdue(8831), true)
        && t.checkExpect(ref1.isOverdue(8829), false);
  }
  
  boolean testComputeFine(Tester t) {
    return t.checkExpect(book1.computeFine(7792), 0)
        && t.checkExpect(book1.computeFine(7793), 10)
        && t.checkExpect(book1.computeFine(7790), 0)
        && t.checkExpect(audio1.computeFine(8836), 40)
        && t.checkExpect(ref1.computeFine(8838), 80);
  }
}