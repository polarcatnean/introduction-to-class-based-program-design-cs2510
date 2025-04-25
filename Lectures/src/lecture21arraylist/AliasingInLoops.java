package lecture21arraylist;

import java.util.ArrayList;

import tester.Tester;

class Book {
  String title;
  Author author;
  
  Book(String title, Author author) {
    this.title = title;
    this.author = author;
  }
  
  //EFFECT: Capitalises this book's title
  void capitaliseTitle() {
   this.title = this.title.toUpperCase();
  }
}

class Author {
  String name;
  int yob;
  
  Author(String name, int yob) {
    this.name = name;
    this.yob = yob;
  }
}

class ArrayUtils2 {
  // Capitalises the titles of all books in the given ArrayList
  void capitaliseTitles_bad(ArrayList<Book> books) {
    for (Book b : books) {
      b = new Book(b.title.toUpperCase(), b.author);  // here we just set b => 0x40 (new Object)
                                                      // we didn't modify anything at 0x20
      
      // b is bound as an alias to each item of the list in turn. eg b => 0x20
      // any modifications to b itself will not persist after the loop
      
      // also we pass a "reference" 
    }
  }
  
  // EFFECT: Modifies all the books in the given ArrayList, to capitalize their titles
  void capitaliseTitles_good(ArrayList<Book> books) {
   for (Book b : books) {
     // b.title.toUpperCase();  
     // also bad; toUpperCase returns a new String, doesn't modify the existing String
     
     // b.capitaliseTitle();           //works
     b.title = b.title.toUpperCase();  //works
   }
  }
  
  //EFFECT: Modifies all the books in the given ArrayList, to capitalize their titles
  void capitalizeTitles_ok(ArrayList<Book> books) {
   for (int i = 0; i < books.size(); i = i + 1) {
     // get the old book...
     Book oldB = books.get(i);
     // ... construct the new book ...
     Book newB = new Book(oldB.title.toUpperCase(), oldB.author);
     // and set it in place of the old book, at the current index
     books.set(i, newB);
   }
  }
}

class ExamplesCapitalise {
  void testCapitalizeTitles_bad(Tester t) {
    // Initialize data:
    Author mf = new Author("Matthias Felleisen", 1953);
    Book htdp = new Book("How to Design Programs", mf);
    ArrayList<Book> books = new ArrayList<Book>();
    books.add(htdp);
    // Modify it
    (new ArrayUtils2()).capitaliseTitles_bad(books);
    // Test for changes
    // t.checkExpect(books.get(0).title, "HOW TO DESIGN PROGRAMS");
  }
  
  void testCapitalizeTitles_good(Tester t) {
    // Initialize data:
    Author mf = new Author("Matthias Felleisen", 1953);
    Book htdp = new Book("How to Design Programs", mf);
    ArrayList<Book> books = new ArrayList<Book>();
    books.add(htdp);
    // Modify it
    (new ArrayUtils2()).capitaliseTitles_good(books);
    // Test for changes
    t.checkExpect(books.get(0).title, "HOW TO DESIGN PROGRAMS");
  }
}