// to represent a person
package lab1;

class Person {
	String name;
	int age; // in years
	String gender;
	Address address;
	
	// the constructor
	Person(String name, int age, String gender, Address address) {
	  this.name = name;
	  this.age = age;
	  this.gender = gender;
	  this.address = address;
	}
}

class Address {
  String city;
  String state;
  
  Address(String city, String state) {
    this.city = city;
    this.state = state;
  }
}


class ExamplesPersons {
  ExamplesPersons() {}
  
  Address tim_add = new Address("Boston", "MA");
  Address kate_add = new Address("Warwick", "RI");
  Address rebecca_add = new Address("Nashua", "NH");
  
  Person tim = new Person("Tim", 23, "Male", this.tim_add);
  Person kate = new Person("Kate", 22, "Female", this.kate_add);
  Person rebecca = new Person("Rebecca", 31, "Non-binary", this.rebecca_add);
  
}