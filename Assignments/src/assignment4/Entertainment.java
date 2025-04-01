package assignment4;
import tester.*;

interface IEntertainment {
  // compute the total price of this Entertainment
  double totalPrice();

  // computes the minutes of entertainment of this IEntertainment
  int duration();

  // produce a String that shows the name and price of this IEntertainment
  String format();

  // is this IEntertainment the same as that one?
  boolean sameEntertainment(IEntertainment that);

  boolean sameMagazine(Magazine magazine);
  boolean sameTVSeries(TVSeries tvSeries);
  boolean samePodcast(Podcast podcast);
}


abstract class AEntertainment implements IEntertainment {
  String name;
  double price;
  int installments;
  
  AEntertainment(String name, double price, int installments) {
    this.name = name;
    this.price = price;
    this.installments = installments;
  }
  
  public double totalPrice() {
    return this.price * this.installments;
  }
  
  public int duration() {
    return this.minPerUnit() * this.installments;
  }
  
  public abstract int minPerUnit();

  public String format() {
    return this.name + ", " + this.price + ".";
  }
  
  public abstract boolean sameEntertainment(IEntertainment that);
  
  public boolean sameMagazine(Magazine magazine) {
    return false;
  }
  
  public boolean sameTVSeries(TVSeries tvSeries) {
    return false;
  }
  
  public boolean samePodcast(Podcast podcast) {
    return false;
  }
}

class Magazine extends AEntertainment {
  String genre;
  int pages;

  Magazine(String name, double price, String genre, int pages, int installments) {
    super(name, price, installments);
    this.genre = genre;
    this.pages = pages;
  }

  public boolean sameEntertainment(IEntertainment that) {
    return that.sameMagazine(this);
  }
  
  @Override
  public boolean sameMagazine(Magazine other) {
    return this.name == other.name 
        && this.price == other.price 
        && this.installments == other.installments
        && this.genre == other.genre
        && this.pages == other.pages;
  }

  public int minPerUnit() {
    return 5 * this.pages;
  }

}

class TVSeries extends AEntertainment {
  String corporation;

  TVSeries(String name, double price, int installments, String corporation) {
    super(name, price, installments);
    this.corporation = corporation;
  }

  // is this TVSeries the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameTVSeries(this);
  }

  public boolean sameTVSeries(TVSeries other) {
    return this.name == other.name 
        && this.price == other.price 
        && this.installments == other.installments
        && this.corporation == other.corporation;
  }
  
  public int minPerUnit() {
    return 50;
  }

}

class Podcast extends AEntertainment {
  String name;
  double price; // represents price per issue
  int installments; // number of episodes in this Podcast

  Podcast(String name, double price, int installments) {
    super(name, price, installments);
  }

  // is this Podcast the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.samePodcast(this);
  }
  
  @Override
  public boolean samePodcast(Podcast other) {
    return this.name == other.name 
        && this.price == other.price 
        && this.installments == other.installments;
  }

  public int minPerUnit() {
    return 50;
  }

}

class ExamplesEntertainment {
  IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  IEntertainment marieClaire = new Magazine("Marie Claire", 7.99, "Fashion", 100, 12);
  
  IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  IEntertainment severance = new TVSeries("Severance", 3.0, 8, "Apple TV");
  
  IEntertainment serial = new Podcast("Serial", 0.0, 8);
  IEntertainment farose = new Podcast("People You May Know", 0.1, 100);

  // testing total price method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 2.55 * 12, .0001)
        && t.checkInexact(this.marieClaire.totalPrice(), 7.99 * 12, .0001)
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25 * 13, .0001)
        && t.checkInexact(this.severance.totalPrice(), 3.0 * 8, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001)
        && t.checkInexact(this.farose.totalPrice(), 0.1 * 100, .0001);
  }
  
  boolean testDuration(Tester t) {
    return t.checkExpect(rollingStone.duration(), 5 * 60 * 12)
        && t.checkExpect(houseOfCards.duration(), 50 * 13)
        && t.checkExpect(serial.duration(), 50 * 8);
  }
  
  boolean testFormat(Tester t) {
    return t.checkExpect(rollingStone.format(), "Rolling Stone, 2.55.")
        && t.checkExpect(severance.format(), "Severance, 3.0.")
        && t.checkExpect(farose.format(), "People You May Know, 0.1.");
  }
  
  boolean testSameEntertainment(Tester t) {
    return t.checkExpect(rollingStone.sameEntertainment(rollingStone), true)
        && t.checkExpect(houseOfCards.sameEntertainment(houseOfCards), true)
        && t.checkExpect(serial.sameEntertainment(serial), true)
        && t.checkExpect(rollingStone.sameEntertainment(marieClaire), false)
        && t.checkExpect(rollingStone.sameEntertainment(farose), false)
        && t.checkExpect(severance.sameEntertainment(farose), false);
  }

}