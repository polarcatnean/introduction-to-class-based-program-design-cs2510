package assignment3.practice;

// page 238

interface ISalesItem {
  
}

abstract class Item implements ISalesItem {
  int originalPrice;
  
  Item(int originalPrice) {
    this.originalPrice = originalPrice;
  }
}

class DeepDiscount extends Item { 
  
  DeepDiscount(int originalPrice) {
    super(originalPrice);
  }

}

class RegularDiscount extends Item { 
  int discountPercentage;

  RegularDiscount(int originalPrice, int discountPercentage) {
    super(originalPrice);
    this.discountPercentage = discountPercentage;
  }
}