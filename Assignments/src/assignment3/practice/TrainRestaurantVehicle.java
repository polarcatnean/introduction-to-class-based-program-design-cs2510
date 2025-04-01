package assignment3.practice;

// train schedule
class Schedule {}
class Route {}
class Stops {}

class Train {
  Schedule s;
  Route r;
  
  Train(Schedule s, Route r){
    this.s = s;
    this.r = r;
  }
}

class ExpressTrain extends Train {
  Stops st;
  String name;
  
  ExpressTrain(Schedule s, Route r, Stops st, String name) {
    super(s, r);
    this.st = st;
    this.name = name;
  }
}

// restaurant guides
class Place {}

class Restaurant {
  String name;
  String price;
  Place place;
  
  Restaurant(String name, String price, Place place) {
    this.name = name;
    this.price = price;
    this.place = place;
  }
}

class ChineseRestaurant extends Restaurant { 
  boolean usesMSG;
  
  ChineseRestaurant(String name, String price, Place place, boolean usesMSG) {
    super(name, price, place);
    this.usesMSG = usesMSG;
  }
}

// vehicle management
class Vehicle { 
  int mileage;
  int price;
  
  Vehicle(int mileage, int price) {
    this.mileage = mileage;
    this.price = price;
  }
}

class Sedan extends Vehicle {
  Sedan(int mileage, int price) {
    super(mileage, price);
  }
}

