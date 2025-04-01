package assignment1;
interface IHousing {
  
}

class Hut implements IHousing {
  int capacity;
  int population;
  
  Hut(int capacity, int population) {
    this.capacity = capacity;
    this.population = population;  // must be less than capacity
  }
}

class Inn implements IHousing {
  String name;
  int capacity;
  int population;  // must be less than capacity
  int stalls;
  
  Inn(String name, int capacity, int population, int stalls) {
    this.name = name;
    this.capacity = capacity;
    this.population = population;
    this.stalls = stalls;
  }
}

class Castle  implements IHousing {
  String name;
  String family_name;
  int population; 
  int carriage_house;
  
  Castle(String name, String family_name, int population, int carriage_house) {
    this.name = name;
    this.family_name = family_name;
    this.population = population;
    this.carriage_house = carriage_house;
  }
}


interface ITransport {
  
}

class Horse implements ITransport {
  IHousing from;
  IHousing to;
  String name;
  String colour;
  
  Horse(IHousing from, IHousing to, String name, String colour) {
    this.from = from;
    this.to = to;
    this.name = name;
    this.colour = colour;
  }
}

class Carriage implements ITransport {
  IHousing from;   // only travel from Inns to Castles or vice versa
  IHousing to;
  double tonnage;

  Carriage(IHousing from, IHousing to, double tonnage) {
    this.from = from;
    this.to = to;
    this.tonnage = tonnage;
  }
}


class ExamplesTravel {
  ExamplesTravel() {}
  
  IHousing hovel = new Hut(5, 1);
  IHousing winterfell = new Castle("Winterfell", "Stark", 500, 6);
  IHousing crossroads = new Inn("Inn At The Crossroads", 40, 20, 12);
  
  ITransport horse1 = new Horse(this.hovel, this.winterfell, "Ablette", "brown");
  ITransport horse2 = new Horse(this.winterfell, this.hovel, "Roach", "brown");
  
  ITransport carriage1 = new Carriage(this.crossroads, this.winterfell, 10);
  ITransport carriage2 = new Carriage(this.winterfell, this.crossroads, 20);
}
