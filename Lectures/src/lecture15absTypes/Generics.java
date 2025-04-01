/* “how do we make function objects work with union data types?” 
 * "VISITOR" pattern
 *   = double-dispatch with function objects
 *   
 *  visitor = a function object for union data
 */  

package lecture15absTypes;
import tester.Tester;


// Represents a function object defined over Shapes that returns a Double
interface IVisitorShape<R> {
  R visitCircle(Circle circle);
  R visitRect(Rect rect);
  R visitSquare(Square square);
}

class ShapeArea implements IVisitorShape<Double>, IFunc<IShape, Double> {
  public Double visitCircle(Circle circle) {
    return Math.PI * circle.radius * circle.radius;
  }

  public Double visitRect(Rect rect) {
    return rect.w * rect.h * 1.0;
  }

  public Double visitSquare(Square square) {
    return Math.pow(square.size, 2);
  }

  public Double apply(IShape shape) {
    return shape.accept(this);
  }
}

class ShapeColour implements IVisitorShape<String>, IFunc<IShape, String> {
  public String visitCircle(Circle circle) {
    return circle.color;
  }

  public String visitRect(Rect rect) {
    return rect.color;
  }

  public String visitSquare(Square square) {
    return square.color;
  }

  public String apply(IShape shape) {
    return shape.accept(this);
  }
}

interface IShape {
  // To return the result of applying the given function to this shape
  // return type is anything the visitor returns
  <R> R accept(IVisitorShape<R> visitor);
}

class Circle implements IShape {
  int x, y;
  int radius;
  String color;
  Circle(int x, int y, int r, String color) {
    this.x = x;
    this.y = y;
    this.radius = r;
    this.color = color;
  }

  public <R> R accept(IVisitorShape<R> visitor) {
    return visitor.visitCircle(this);
  }
}
class Rect implements IShape {
  int x, y, w, h;
  String color;
  Rect(int x, int y, int w, int h, String color) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.color = color;
  }

  public <R> R accept(IVisitorShape<R> visitor) {
    return visitor.visitRect(this);
  }
}

class Square implements IShape {
  int x, y, size;
  String color;
  Square(int x, int y, int size, String color) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.color = color;
  }

  public <R> R accept(IVisitorShape<R> visitor) {
    return visitor.visitSquare(this);
  }
}

class Book {
  String title;
  String author;
  int price;
  
  Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }
}

class Runner {
  String name;
  int age;
  int bib;          // bib number
  boolean isMale;
  int pos;          // starting position
  int time;         // in min

  Runner(String name, int age, int bib, boolean isMale, int pos, int time) {
    this.name = name;
    this.age = age;
    this.bib = bib;
    this.isMale = isMale;
    this.pos = pos;
    this.time = time;
  }
}

interface IPred<T> {
  boolean apply(T t);
}

class BookByAuthor implements IPred<Book> {

  public boolean apply(Book b) {
    return false;
  }
  
}

interface IComparator<T> {
  int compare(T t1, T t2);
}

// U can't be int, double, or boolean (primitive types)
// Java only permits class/interface types as the type parameters to generics
// use <Integer>, <Double>, <Boolean> instead (just "wrappers" for primitives)
interface IList<T> {
  IList<T> filter(IPred<T> pred);
  IList<T> sort(IComparator<T> comp);
  IList<T> insert(IComparator<T> comp, T t);
  int length();
  
  // map; apply function f to all the elements
  // “In IList<T>, map is a method parameterized by U, that takes a function from T values to U values,
  // and produces an IList<U> as a result.”
  <U> IList<U> map(IFunc<T, U> f);
  
  <U> U foldr(IFunc2<T, U, U> f, U base);
}

// “An MtList of T is a list of T.” 
class MtList<T> implements IList<T> {
  
  public IList<T> filter(IPred<T> pred) {
    return this;
  }

  public IList<T> sort(IComparator<T> comp) {
    return this;
  }

  public IList<T> insert(IComparator<T> comp, T t) {
    return new ConsList<T>(t, this);
  }

  public int length() {
    return 0;
  }

  public <U> IList<U> map(IFunc<T, U> f) {
    return new MtList<U>();
  }

  public <U> U foldr(IFunc2<T, U, U> f, U base) {
    return base;
  }

}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  
  public IList<T> filter(IPred<T> pred) {
    if (pred.apply(this.first)) {
      return new ConsList<T>(this.first, this.rest.filter(pred));
    }
    return this.rest.filter(pred);
  }

  public IList<T> sort(IComparator<T> comp) {
    return this.rest.sort(comp).insert(comp, this.first);
  }

  public IList<T> insert(IComparator<T> comp, T t) {
    if (comp.compare(t, this.first) < 0) {
      return new ConsList<T>(t, this);
    }
    return new ConsList<T>(this.first, this.rest.insert(comp, t));
  }

  public int length() {
    return 1 + this.length();
  }

  public <U> IList<U> map(IFunc<T, U> f) {
    return new ConsList<U>(f.apply(this.first), this.rest.map(f));
  }

  public <U> U foldr(IFunc2<T, U, U> f, U base) {
    return f.apply(this.first, this.rest.foldr(f, base));
  }
}

// generic in TWO type parameters
interface IFunc<A, R> {
  R apply(A arg);
}

class RunnerName implements IFunc<Runner, String> {
  public String apply(Runner r) {
    return r.name;
  }
}

class CirclePerimeter implements IFunc<Circle, Double> {
  public Double apply(Circle c) {
    return 2.0 * Math.PI * c.radius;
  }
}

// Interface for two-argument function-objects with signature [A1, A2 -> R]
interface IFunc2<A1, A2, R> {
  R apply(A1 arg1, A2 arg2);  // why not A1, R -> R (X Y -> Y)
}

class SumPricesOfBooks implements IFunc2<Book, Integer, Integer> {
  public Integer apply(Book B, Integer sum) {
    return B.price + sum;
  }
}

class Utils {
  Integer totalPrice(IList<Book> books) {
    return books.foldr(new SumPricesOfBooks(), 0);
  }
  
}

class ExamplesIList {
  
  IList<Circle> circs = new ConsList<Circle>(new Circle(3, 3, 3, "black"), new MtList<Circle>());
  IList<Double> circPerims = circs.map(new CirclePerimeter());
  
  IList<IShape> shapes = new ConsList<IShape>(new Circle(0, 0, 10, "red"), 
                            new ConsList<IShape>(new Rect(0, 0, 10, 10, "green"),
                               new MtList<IShape>()));
  
  IList<Double> expectedList = new ConsList<Double>(314.15, 
                                  new ConsList<Double>(100.00, 
                                    new MtList<Double>()));
  
  IList<String> expectedList1 = new ConsList<String>("red", 
                                  new ConsList<String>("green", new MtList<String>()));
  
  IShape c1 = new Circle(0, 0, 10, "red");
  
  boolean testIShapeMap(Tester t) {
    return t.checkInexact(shapes.map(new ShapeArea()), expectedList, 0.01)
        && t.checkExpect(shapes.map(new ShapeColour()), expectedList1);
  }
}