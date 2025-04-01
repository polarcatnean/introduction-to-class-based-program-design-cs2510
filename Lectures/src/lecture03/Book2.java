package lecture03;
import tester.*;

/*
  +---------------+
  | Book          | 
  +---------------+
  | String title  |
  | String author |
  | int price     | 
  +---------------+  
*/


// to represent a book in a bookstore
class Book2 {
    String title;
    String author;
    int price;

    // the constructor
    Book2(String title, String author, int price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    /* TEMPLATE:
       Fields:
       ... this.title ...         -- String
       ... this.author ...        -- String
       ... this.price ...         -- int
       
       Methods:
       ... this.salePrice(int) ... -- int
       ... this.reducePrice() ...  -- Book
    */

    // Compute the sale price of this Book given using 
    // the given discount rate (as a percentage)
    int salePrice(int discount) {
        return this.price - (this.price * discount) / 100;
    }
    
    // produce a book like this one, but with the price reduced by 20%
    Book2 reducePrice(){
        return new Book2(this.title, this.author, this.salePrice(20));
    }
}

// examples and tests for the classes that represent
// books and authors
class ExamplesBooks2 {
    ExamplesBooks2() {}

    // examples of books
    Book2 htdp = new Book2("HtDP", "FFK", 60);
    Book2 beaches = new Book2("Beaches", "PC", 20);

    // test the method salePrice for the class Book
    boolean testSalePrice(Tester t) {
        return t.checkExpect(this.htdp.salePrice(30), 42)
            && t.checkExpect(this.beaches.salePrice(20), 16);
    }

    // test the method reducePrice for the class Book
    boolean testReducePrice(Tester t) {
        return t.checkExpect(this.htdp.reducePrice(), 
                             new Book2("HtDP", "FFK", 48))
            && t.checkExpect(this.beaches.reducePrice(), 
                             new Book2("Beaches", "PC", 16));
    }
}
