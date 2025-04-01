/* Sameness
 *  - reflexivity (a=a)
 *  - symmetry (a=b if b=a)
 *  - transitivity (if a=b and b=c then a=c)
 *  - totality: compare any two objects of the same type, and obtain a correct answer
 * 
 * New tools for detecting sameness:
 *  - casting
 *  - instanceof
 *  - double-dispatch
 */
package lecture11sameness;
import tester.Tester;

interface IShape {
  boolean sameShape(IShape that);
  boolean sameCircle(Circle that);
  boolean sameRect(Rect that);
  boolean sameSquare(Square that);
  boolean sameTriangle(Triangle that);

  boolean sameCombo(Combo that);
}

abstract class AShape implements IShape {
  int x;
  int y;
  
  AShape(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  public abstract boolean sameShape(IShape that);
  
  public boolean sameCircle(Circle that) { return false; }
  public boolean sameRect(Rect that) { return false; }
  public boolean sameSquare(Square that) { return false; }
  public boolean sameTriangle(Triangle that) { return false; }
  public boolean sameCombo(Combo that) { return false; }
}

class Circle extends AShape {
  int radius;
  
  Circle(int x, int y, int radius) {
    super(x, y);
    this.radius = radius;
  }

  public boolean sameShape(IShape that) {
    return that.sameCircle(this);
  }
  
  @Override
  public boolean sameCircle(Circle that) {
    return this.x == that.x &&
           this.y == that.y &&
           this.radius == that.radius;
  }
}

class Rect extends AShape {
  int w, h;
  
  Rect(int x, int y, int w, int h) {
    super(x, y);
    this.w = w;
    this.h = h;
  }

  public boolean sameShape(IShape that) {
    return that.sameRect(this);
  }
  
  @Override
  public boolean sameRect(Rect that) {
    return this.x == that.x &&
           this.y == that.y &&
           this.w == that.w &&
           this.h == that.h;
  }
  
  
}

class Square extends Rect {
  Square(int x, int y, int size) {
    super(x, y, size, size);
  }
  
  public boolean sameShape(IShape that) {
    return that.sameSquare(this);
  }
  
  @Override
  public boolean sameSquare(Square that) {
    return this.x == that.x &&
           this.y == that.y &&
           this.w == that.w;
  }
  
  @Override
  public boolean sameRect(Rect that) { return false; }
}

class Triangle extends AShape{
  int base;
  int height;
  
  Triangle(int x, int y, int base, int height) {
    super(x, y);
    this.base = base;
    this.height = height;
  }
  
  public boolean sameShape(IShape that) {
    return that.sameTriangle(this);
  }
  
  public boolean sameTriangle(Triangle that) {
    return this.x == that.x 
        && this.y == that.y
        && this.base == that.base
        && this.height == that.height;
  }
}

class Combo implements IShape {
  IShape left;
  IShape right;
  
  Combo(IShape left, IShape right) {
    this.left = left;
    this.right = right;
  }

  public boolean sameShape(IShape that) {
    return that.sameCombo(this);
  }
  
  public boolean sameCombo(Combo that) {
    return this.left.sameShape(that.left) && this.right.sameShape(that.right);
  }

  public boolean sameCircle(Circle that) { return false; }
  public boolean sameRect(Rect that) { return false; }
  public boolean sameSquare(Square that) { return false; }
  public boolean sameTriangle(Triangle that) { return false; }
  
  
}

class ExamplesShapes {
  
  IShape c1 = new Circle(3, 4, 5);
  IShape c2 = new Circle(4, 5, 6);
  IShape c3 = new Circle(3, 4, 5);
  
  IShape r1 = new Rect(3, 4, 5, 5);
  IShape r2 = new Rect(4, 5, 6, 7);
  IShape r3 = new Rect(3, 4, 5, 5);
  
  IShape s1 = new Square(3, 4, 5);
  IShape s2 = new Square(4, 5, 6);
  IShape s3 = new Square(3, 4, 5);
  
  IShape t1 = new Triangle(3, 4 ,5, 5);
  IShape t2 = new Triangle(3, 4 ,5, 5);

  boolean testSameness(Tester t) {
    return t.checkExpect(c1.sameShape(c2), false) 
        && t.checkExpect(c2.sameShape(c1), false)
        && t.checkExpect(c1.sameShape(c3), true) 
        && t.checkExpect(c3.sameShape(c1), true)

        && t.checkExpect(r1.sameShape(r2), false) 
        && t.checkExpect(r2.sameShape(r1), false)
        && t.checkExpect(r1.sameShape(r3), true)   
        && t.checkExpect(r3.sameShape(r1), true)

        && t.checkExpect(s1.sameShape(s2), false) 
        && t.checkExpect(s2.sameShape(s1), false)
        && t.checkExpect(s1.sameShape(s3), true) 
        && t.checkExpect(s3.sameShape(s1), true)
        
        && t.checkExpect(t1.sameShape(t2), true)
        && t.checkExpect(t2.sameShape(t1), true)

        // comparing different shapes
        && t.checkExpect(r1.sameShape(c1), false)
        && t.checkExpect(c1.sameShape(r1), false)
        
        && t.checkExpect(t1.sameShape(r1), false)
        && t.checkExpect(t1.sameShape(s1), false)
        && t.checkExpect(t1.sameShape(c1), false)
        && t.checkExpect(r1.sameShape(t1), false)
        && t.checkExpect(s1.sameShape(t1), false)
        && t.checkExpect(c1.sameShape(t1), false)
        
        // Comparing a Square with a Rect of a different size
        && t.checkExpect(s1.sameShape(r2), false) 
        && t.checkExpect(r2.sameShape(s1), false)
        // Comparing a Square with a Rect of the same size
        && t.checkExpect(s1.sameShape(r1), false) 
        && t.checkExpect(r1.sameShape(s1), false);
  }
}