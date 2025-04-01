package assignment3;
import tester.*; 
import java.awt.Color;
import javalib.worldimages.*;
import javalib.funworld.*;      // the abstract World class and the big-bang library
// import javalib.worldcanvas.WorldCanvas;

interface ITree {
  // renders this ITree to a picture
  WorldImage draw();
  
  // returns true if any of the twigs in the tree (stem/branch) are pointing downwards
  // leftTheta > 180 deg && rightTheta < 0 deg
  boolean isDrooping();
  
  // returns the width of this tree
  double getWidth();
  double getLWidth();
  double getRWidth();
  
  //  takes the current tree and a given tree and produces a Branch using the given arguments
  ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree);
  ITree rotate(double theta);

}

// TODO
abstract class ATree implements ITree {
  
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, 
        this.rotate(leftTheta), otherTree.rotate(rightTheta));
  }
  
  public abstract WorldImage draw();
  public abstract boolean isDrooping();
}

class Leaf implements ITree {
  int size; // represents the radius of the leaf
  Color color; // the color to draw it

  Leaf(int size, Color color) {
    this.size = size;
    this.color = color;
  }
  
  public WorldImage draw() {
    return new CircleImage(this.size, OutlineMode.SOLID, this.color);
  }

  public double getWidth() {
    return this.getLWidth() + this.getRWidth();
  }
  
  public double getLWidth() {
    return this.size;
  }
  
  public double getRWidth() {
    return this.size;
  }

  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, 
        this.rotate(leftTheta), otherTree.rotate(rightTheta));
  }

  public ITree rotate(double theta) {
    return this;
  }

  public boolean isDrooping() {
    return false;
  }

}

class Stem implements ITree {
  int length; // How long this stick is
  double theta; // The angle (in degrees) of this stem, relative to the +x axis
  ITree tree; // The rest of the tree

  Stem(int length, double theta, ITree tree){
    this.length = length;
    this.theta = theta;
    this.tree = tree;
  }
  
  public WorldImage draw() {
    int x = (int) (length * Math.cos(Math.toRadians(theta)));
    int y = (int) (length * Math.sin(Math.toRadians(theta)));
    
    return new OverlayImage(tree.draw(), new LineImage(new Posn(-x, y), Color.BLACK).movePinhole(x/2, -y/2))
        .movePinhole(-x, y);
  }

  public double getWidth() {
    return this.tree.getWidth();
  }
  
  public double getLWidth() {
    return this.tree.getLWidth();
  }
  
  public double getRWidth() {
    return this.tree.getRWidth();
  }

  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, 
        this.rotate(leftTheta), otherTree.rotate(rightTheta));
  }
  
  public ITree rotate(double theta) {
    return new Stem(this.length, this.theta - (90 - theta), this.tree.rotate(theta));
  }

  public boolean isDrooping() {
    return this.isDownwards(theta) || this.tree.isDrooping();
  }

  public boolean isDownwards(double theta) {
    // 180 to 360 or 0 to -180
    return (180 < theta && theta < 360) || (0 > theta && theta > -180);
  }
}

class Branch implements ITree {
  // How long the left and right branches are
  int leftLength;
  int rightLength;
  // The angle (in degrees) of the two branches, relative to the +x axis,
  double leftTheta;
  double rightTheta;
  // The remaining parts of the tree
  ITree left;
  ITree right;

  Branch(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree left, ITree right) {
    this.leftLength = leftLength;
    this.rightLength = rightLength;
    this.leftTheta = leftTheta;
    this.rightTheta = rightTheta;
    this.left = left;
    this.right = right;
  }
  
  public WorldImage draw() {
    return new OverlayImage(new Stem(leftLength, leftTheta, left).draw(), 
        new Stem(rightLength, rightTheta, right).draw());
  }
  
  private int getX(int l, double theta) {
    return (int) (l * Math.cos(Math.toRadians(theta)));
  }

  public double getWidth() {
    return this.getLWidth() + this.getRWidth();
  }

  public double getLWidth() {
    return Math.abs(this.getX(leftLength, leftTheta)) + this.left.getLWidth();
  }
  
  public double getRWidth() {
    return Math.abs(this.getX(rightLength, rightTheta)) + this.right.getRWidth();
  }

  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, 
        this.rotate(leftTheta), otherTree.rotate(rightTheta));
  }
  
  public ITree rotate(double theta) {
    return new Branch(this.leftLength, this.rightLength, 
                      this.leftTheta - (90 - theta), this.rightTheta - (90 - theta), 
                      this.left.rotate(theta), this.right.rotate(theta));
  }

  public boolean isDrooping() {
    return (this.isDownwards(leftTheta) || this.isDownwards(rightTheta))
        || this.left.isDrooping() || this.right.isDrooping();
  }

  public boolean isDownwards(double theta) {
    // 180 to 360 or 0 to -180
    return (180 < theta && theta < 360) || (0 > theta && theta > -180);
  }

}

class TreeWorld extends World {
  WorldImage tree;
  int WIDTH = 400;
  int HEIGHT = 300;

  TreeWorld(ITree tree) {
    this.tree = tree.draw();
  }

  public WorldScene makeScene() {
    return getEmptyScene().placeImageXY(new VisiblePinholeImage(tree, Color.RED),
        (WIDTH / 2), (HEIGHT / 2));
  }

  public boolean render() {
    return bigBang(WIDTH, HEIGHT);
  }
}

class ExamplesTrees {
  
  ITree leaf1 = new Leaf(10, Color.RED);
  ITree leaf2 = new Leaf(15, Color.BLUE);
  ITree leaf3 = new Leaf(15, Color.GREEN);
  ITree leaf4 = new Leaf(8, Color.ORANGE);
  
  int xl = (int) (30 * Math.cos(Math.toRadians(135)));
  int yl = (int) (30 * Math.sin(Math.toRadians(135)));
  int xr = (int) (30 * Math.cos(Math.toRadians(40)));
  int yr = (int) (30 * Math.sin(Math.toRadians(40)));
  WorldImage leftLine = new LineImage(new Posn(-xl, yl), Color.BLACK).movePinhole(xl/2, -yl/2);
  WorldImage rightLine = new LineImage(new Posn(-xr, yr), Color.BLACK).movePinhole(xr/2, -yr/2);
  WorldImage lBranch = new OverlayImage(leaf1.draw(), leftLine).movePinhole(-xl, yl);
  WorldImage rBranch = new OverlayImage(leaf2.draw(), rightLine).movePinhole(-xr, yr);
  WorldImage test = new OverlayImage(lBranch, rBranch);
  
  ITree tree1 = new Branch(30, 30, 135, 40, leaf1, leaf2);
  ITree tree2 = new Branch(30, 30, 115, 65, leaf3, leaf4);
  
  ITree stem1 = new Stem(40, 90, tree1);
  ITree stem2 = new Stem(50, 90, tree2);
  
  ITree branch1 = new Branch(40, 50, 150, 30, tree1, tree2);
  ITree branch2 = new Branch(40, 50, 150, 30, tree2, tree1);
  ITree tree3 = new Branch(80, 80, 160, 20, branch1, branch2);
  
  ITree combined1 = tree1.combine(40, 50, 150, 30, tree2);
  ITree stem3 = new Stem(50, 90, combined1);
  
  // for visually seeing results; plug whatever tree you want instead
  boolean testShowTree(Tester t) {
    ITree toDraw = stem3.combine(40, 50, 150, 30, tree2);
    new TreeWorld(toDraw).render();
    
    return true;
  }
  
  boolean testImages(Tester t) {
    return t.checkExpect(new RectangleImage(30, 20, OutlineMode.SOLID, Color.GRAY),
                         new RectangleImage(30, 20, OutlineMode.SOLID, Color.GRAY));
  } 
  
//  boolean testDrawTree(Tester t) {
//    WorldCanvas c = new WorldCanvas(500, 500);
//    WorldScene s = new WorldScene(500, 500);
//    
//    return c.drawScene(s.placeImageXY(tree1.combine(40, 50, 150, 30, tree2).draw(), 250, 250))
//        && c.show();
//  } 
  
  boolean testGetWidth(Tester t) {
    return t.checkInexact(leaf1.getWidth(), 20.0, 0.01)
        && t.checkInexact(tree1.getWidth(), Math.abs(xl) + Math.abs(xr) + 10.0 + 15.5, 0.01)
        && t.checkInexact(branch1.getWidth(), (int) Math.abs(30 * Math.cos(Math.toRadians(135))) + (int) Math.abs(40 * Math.cos(Math.toRadians(150)))
                                            + (int) Math.abs(50 * Math.cos(Math.toRadians(30))) +  (int) Math.abs(30 * Math.cos(Math.toRadians(65)))
                                            + 10.0 + 8.0, 0.01);
  }
  
  boolean testIsDrooping(Tester t) {
    return t.checkExpect(leaf1.isDrooping(), false)
        && t.checkExpect(stem1.isDrooping(), false)
        && t.checkExpect(branch1.isDrooping(), false)
        && t.checkExpect(tree1.combine(40, 50, 150, 30, tree2).isDrooping(), true);

  }
}