package assignment4;
import tester.Tester;

class BagelRecipe { // weight in oz
  double flour;  
  double water;
  double yeast;
  double malt;
  double salt;
  
  BagelRecipe(double flour, double water, double yeast, double malt, double salt) {
    if (new Utils().validFlourWaterRatio(flour, water)) {
      this.flour = flour;
      this.water = water;
    }
    
    if (new Utils().validYeastMaltRatio(yeast, malt)) {
      this.yeast = yeast;
      this.malt = malt;
    }
    
    if (new Utils().validSaltYeastRatio(flour, yeast, salt)) {
      this.salt = salt;
    }
  }
  
  BagelRecipe(double flour, double yeast) {
    this(flour, flour, yeast, yeast, flour / 20 - yeast);
  }
  
  BagelRecipe(double flourInCups, double yeastInTsps, double saltInTsps) {
    this(flourInCups * 4.25, flourInCups * 4.25, 
        yeastInTsps * 5 / 48, yeastInTsps * 5 / 48,        
        saltInTsps * 10 / 48);
  }
  
  // returns true if the same ingredients have the same weights to within 0.001 ounces.
  boolean sameRecipe(BagelRecipe other) {
    return this.flour - other.flour < 0.001
        && this.water - other.water < 0.001
        && this.yeast - other.yeast < 0.001
        && this.malt - other.malt < 0.001
        && this.salt - other.salt < 0.001;
  }
}

class Utils {
  
  boolean validRatio(double flour, double water, double yeast, double malt, double salt) {
    if (flour == water && yeast == malt && salt + yeast == flour / 20) {
      return true;
    }
    else {
      throw new IllegalArgumentException("Invalid ratio.");
    }
  }
  
  boolean validFlourWaterRatio(double flour, double water) {
    if (flour == water) {
      return true;
    }
    throw new IllegalArgumentException("Invalid flour/water ratio.");
  }
  
  boolean validYeastMaltRatio(double yeast, double malt) {
    if (yeast == malt) {
      return true;
    }
    throw new IllegalArgumentException("Invalid yeast/malt ratio.");
  }
  
  boolean validSaltYeastRatio(double flour, double yeast, double salt) {
    if (salt + yeast - (flour / 20) < 0.1) {
      return true;
    }
    throw new IllegalArgumentException("Invalid salt/yeast ratio.");
  }
}

class ExamplesBagel {
  
   BagelRecipe recipe1 = new BagelRecipe(300.0, 300.0, 6.0, 6.0, 9.0);
   BagelRecipe recipe2 = new BagelRecipe(300.0, 3.0);
   BagelRecipe recipe3 = new BagelRecipe(3.25, 2.0, 2.3);
   
   BagelRecipe recipe1m = new BagelRecipe(300.0, 6.0);
   
   boolean testConstructorByWeight(Tester t) {
     return t.checkConstructorException(new IllegalArgumentException("Invalid flour/water ratio."),
         "assignment4.BagelRecipe", 
         440.0, 300.0, 6.0, 6.0, 6.0);
   }
   
   boolean testConstructorByVolume(Tester t) {
     return t.checkConstructorException(new IllegalArgumentException("Invalid salt/yeast ratio."),
         "assignment4.BagelRecipe", 
         3.5, 2.0, 6.0);
   }
   
   boolean testSameRecipe(Tester t) {
     return t.checkExpect(recipe1.sameRecipe(recipe1m), true)
         && t.checkExpect(recipe1m.sameRecipe(recipe1), true)
         && t.checkExpect(recipe1.sameRecipe(recipe2), false);
   }
}