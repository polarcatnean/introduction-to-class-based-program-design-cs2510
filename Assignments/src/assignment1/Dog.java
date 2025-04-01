package assignment1;
class Dog {
  String name;
  String breed;
  int yob;  // year of birth given as a four digit number
  String state;  // two letter abbr.
  boolean hypoallergenic;  // true if dog is hypoallergenic
  
  Dog(String name, String breed, int yob, String state, boolean hypoallergenic) {
    this.name = name;
    this.breed = breed;
    this.yob = yob;
    this.state = state;
    this.hypoallergenic = hypoallergenic;
  }
}

class ExamplesDog {
  ExamplesDog() {}
  
  Dog huffle = new Dog("Hufflepuff", "Wheaten Terrier", 2012, "TX", true);
  Dog pearl = new Dog("Pearl", "Labrador Retriever", 2016, "MA", false);
}