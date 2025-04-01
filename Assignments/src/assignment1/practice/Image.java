package assignment1.practice;
import tester.Tester;

// represent information about an image
class Image { 
  int width; // in pixels
  int height; // in pixels
  String source;

  Image(int width, int height, String source) {
    this.width = width;
    this.height = height;
    this.source = source;
  }

  // determine the area (in pixels) of this image 
  int area() {
    return this.width * this.height;
  }

  // produce a string according to the size of this image
  String sizeString() {
    if (this.area() <= 10000) {
      return "small";
    }
    else if (this.area() <= 1000000) {
      return "medium";
    }
    else {
      return "large";
    }
  }
}

class ExamplesImage {
  ExamplesImage() {}

  Image i1 = new Image(100, 100, "sm");
  Image i2 = new Image(1000, 1000, "med");
  Image i3 = new Image(2000, 1000, "lrg");

  boolean testArea(Tester t) {
    return t.checkExpect(i1.area(), 10000)
        && t.checkExpect(i2.area(), 1000000);
  }

  boolean testSizeString(Tester t) {
    return t.checkExpect(i1.sizeString(), "small")
        && t.checkExpect(i2.sizeString(), "medium")
        && t.checkExpect(i3.sizeString(), "large");
  }
}