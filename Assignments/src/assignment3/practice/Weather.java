package assignment3.practice;

import tester.Tester;

interface IRecording {
  String asString();
  String unit();
  
}

abstract class ARecording implements IRecording {
  int high;
  int today;
  int low;

  ARecording(int high, int today, int low) {
    this.high = high;    //historical high
    this.today = today;
    this.low = low;      //historical low
  }
  
  int dHigh() { 
    return this.high - this.today;
  }

  int dLow() { 
    return this.today - this.low;
  }
  
  public String asString() {
    return String.valueOf(low)
        .concat("-")
        .concat(String.valueOf(high))
        .concat(this.unit());
  }
  
  boolean newLow() {
    return this.today < this.low;
  }
  
  boolean newHigh() {
    return this.today > this.high;
  }

  public abstract String unit();
  
}


abstract class ATemperature extends ARecording {
  ATemperature(int high, int today, int low) {
    super(high, today, low);
  }

  public String unit() {
    return "degrees "
        .concat(this.name());
  }

  abstract String name();
  abstract boolean heatWarning();
}

class Celsius extends ATemperature {
  Celsius(int high, int today, int low) {
    super(high, today, low);
  }
  
  String name() {
    return "Celsius";
  }
  
  int celsiusToFahrenheit(int c) {
    return (c * 9/5) + 32;
  }
  
  Fahrenheit celsiusToFahrenheit() {
    return new Fahrenheit(this.celsiusToFahrenheit(high),
        this.celsiusToFahrenheit(today),
        this.celsiusToFahrenheit(low));
  }

  boolean heatWarning() {
    return this.today > 35;
  }
}

class Fahrenheit extends ATemperature {
  Fahrenheit(int high, int today, int low) {
    super(high, today, low);
  }
  
  String name() {
    return "Fahrenheit";
  }
  
  int fahrenheitToCelsius(int f) {
    return (f - 32) * 5/9;
  }
  
  Celsius fahrenheitToCelsius() {
    return new Celsius(this.fahrenheitToCelsius(high),
        this.fahrenheitToCelsius(today),
        this.fahrenheitToCelsius(low));
  }

  boolean heatWarning() {
    return this.today > 95;
  }
}


class Pressure extends ARecording {
  Pressure(int high, int today, int low) {
    super(high, today, low);
  }

  public String unit() {
    return "hPa";
  }
  
  boolean lowPressure() {
    return this.today < 1010;
  }
  
  boolean highPressure() {
    return this.today > 1020;
  }
}


//recording precipitation measurements [in mm]
class Precipitation extends ARecording {
  Precipitation(int high, int today) {
    super(high, today, 0);
  }

  //override asString to report a maximum value
  public String asString() {
    return "up to "
        .concat(String.valueOf(high))
        .concat(this.unit());
  }

  //required method
  public String unit() {
    return "mm";
  }
}

class WindSpeed extends ARecording {
  String direction;
  
  WindSpeed(int high, int today, int low, String direction) {
    super(high, today, low);
    this.direction = direction;
  }

  public String unit() {
    return "km/h";
  }
}


class ExamplesRecordings {
  
  Celsius feb16 = new Celsius(-9, -11, -14);
  Celsius feb19 = new Celsius(-14, -11, -17);
  
  boolean testNewHigh(Tester t) {
    return t.checkExpect(feb19.newHigh(), true)
        && t.checkExpect(feb16.newHigh(), false);
  }
}