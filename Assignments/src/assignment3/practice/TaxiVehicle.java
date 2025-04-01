package assignment3.practice;

// page 238

interface ITaxiVehicle {
  
}

class Taxi {
  int idNum;
  int passengers;
  int pricePerMile;
  
  Taxi(int idNum, int passengers, int pricePerMile) {
    this.idNum = idNum;
    this.passengers = passengers;
    this.pricePerMile = pricePerMile;
  }
}

class Cab extends Taxi {
  Cab(int idNum, int passengers, int pricePerMile) {
    super(idNum, passengers, pricePerMile);
  }  
}

class Limo extends Taxi {
  int minRental;
  
  Limo(int idNum, int passengers, int pricePerMile, int minRental) {
    super(idNum, passengers, pricePerMile);
    this.minRental = minRental;
  }
}

class Van extends Taxi {
  boolean access;
  
  Van(int idNum, int passengers, int pricePerMile, boolean access) {
    super(idNum, passengers, pricePerMile);
    this.access = access;
  }
}