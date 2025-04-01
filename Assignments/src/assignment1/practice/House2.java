/*
Exercise 5.3 Consider a revision of the problem in exercise 3.1:

. . . Develop a program that assists real estate agents. The program
deals with listings of available houses. . . . . . .

Make examples of listings. Develop a data definition for listings of houses.
Implement the definitionwith classes. Translate the examples into objects.
*/


// a list of houses
package assignment1.practice;

interface ILoH { }

// the empty list
class MTListing implements ILoH {
  MTListing(){};
}

// adding a house to a list
class HouseListing implements ILoH {
  House fst;
  ILoH rst;
  
  HouseListing(House fst, ILoH rst) {
    this.fst = fst;
    this.rst = rst;
  }
}

// an individual house
// classes already defined in House.java

//class House {
//  String kind;
//  int rooms;
//  int price;  // in USD
//  Address address;
// 
//  House(String kind, int rooms, int price, Address address) {
//    this.kind = kind;
//    this.rooms = rooms;
//    this.price = price;
//    this.address = address;
//  }
//}
//
//class Address {
//  int streetNumber;
//  String streetName;
//  String city;
//  
//  Address(int streetNumber, String streetName, String city) {
//    this.streetNumber = streetNumber;
//    this.streetName = streetName;
//    this.city = city;
//  }
//}

class ExamplesHouseListings {
  ExamplesHouseListings() {};
  
  Address maplest23 = new Address(23, "Maple Street", "Brooklin");
  Address joyerd5 = new Address(5, "Joye Road", "Newton");
  Address winslow83 = new Address(83, "Winslow Road", "Waltham");
  
  House maple = new House("Ranch", 7, 375000, maplest23);
  House joye = new House("Colonial", 9, 450000, joyerd5);
  House winslow = new House("Cape", 6, 235000, winslow83);
  
  ILoH empty = new MTListing();
  ILoH l1 = new HouseListing(this.maple, empty);
  ILoH l2 = new HouseListing(this.joye, l1);
  ILoH l3 = new HouseListing(this.winslow, l2);
  ILoH all = new HouseListing(this.winslow, new HouseListing(this.joye, new HouseListing (this.maple, empty)));
}