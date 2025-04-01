// page 229
package assignment3.practice;

import tester.Tester;

interface IVehicle {
  // compute the cost of refueling this vehicle, given the current price of fuel
  double cost(double price);
}

abstract class AVehicle implements IVehicle { 
  double tank; // gallons

  AVehicle(double tank) {
    this.tank = tank;
  }
  
  public double cost(double price) {
    return this.tank * price;
  }
}

class Car extends AVehicle {
  Car(double tank) {
    super(tank);
  }
}

class Truck extends AVehicle {
  Truck(double tank) {
    super(tank);
  }
}

class Bus extends AVehicle {
  Bus(double tank) {
    super(tank);
  }
}


class ExamplesVehicles {
  
  IVehicle car1 = new Car(10.0);
  IVehicle truck1 = new Truck(56.7);
  IVehicle bus1 = new Bus(40.0);
  
  boolean testCost(Tester t) {
    return t.checkInexact(car1.cost(10.0), 100.0, 0.01)
        && t.checkInexact(truck1.cost(20.0), 1134.0, 0.01)
        && t.checkInexact(bus1.cost(30.0), 1200.0, 0.01);
  }
}