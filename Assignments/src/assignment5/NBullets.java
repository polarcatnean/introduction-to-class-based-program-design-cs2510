package assignment5;


import tester.*;                
import javalib.worldimages.*;
import javalib.funworld.*;     
import java.awt.Color;                                       
import java.util.Random;


class MyPosn extends Posn {
  MyPosn(int x, int y) {
    super(x, y);
  }

  MyPosn(Posn p) {
    this(p.x, p.y);
  }
  
  MyPosn add(MyPosn other) {
    return new MyPosn(this.x + other.x, this.y + other.y);
  }
  
  boolean isOffScreen(int width, int height) {
    return this.x < 0 || this.x > width
        || this.y < 0 || this.y > height;
  }

  public MyPosn move(int speed, MyPosn velocity) {
    return new MyPosn(this.x + (velocity.x * speed),
                      this.y + (velocity.y * speed));
  }

  int moveX(int speed, MyPosn velocity) {
    return this.x + (speed * velocity.x);
  }

  int moveY(int speed, MyPosn velocity) {
    return this.y + (speed * velocity.y);
  }
  
  boolean isInRadius(MyPosn other, int r1, int r2) {
    // both items have a radius
    return Math.hypot(this.x - other.x, this.y - other.y) <= r1 + r2;
  }

  public String info() {
    return "(x:" + this.x + ")(y:" + this.y + ")";
  }

  public boolean isStationary() {
    return this.x == 0 && this.y == 0;
  }
}

abstract class AItem {
  MyPosn p;
  int radius;
  int speed;
  MyPosn velocity;  // = speed * vector
  Color color;
  static final int WIDTH = 500;
  static final int HEIGHT = 300;
  
  AItem(MyPosn p, int radius, int speed, MyPosn velocity, Color color) {
    this.p = p;
    this.radius = radius;
    this.speed = speed;
    this.velocity = velocity;
    this.color = color;
  }
  
  public String info() {
    return "(POS: " + this.p.info() + ", "
    + "size:" + this.radius + "px. "
    + "velocity: " + this.velocity.info()
    + ") ";
  }
  
  WorldImage draw() {
    return new CircleImage(this.radius, OutlineMode.SOLID, this.color);
  }
  
  WorldScene placeOnScene(WorldScene s) {
    return s.placeImageXY(this.draw(), this.p.x, this.p.y);
  }
  
  boolean isOffScreen() {
    return this.p.isOffScreen(WIDTH, HEIGHT);
  }
  
  // is this item in the radius of other item
  public boolean isInRadius(MyPosn other, int otherRadius) {
    return this.p.isInRadius(other, this.radius, otherRadius);
  }
  
}

class Ship extends AItem {
  static final int SHIP_RADIUS = HEIGHT / 30;  // 10 pixels
  static final int SHIP_SPEED = 4;
  static final Color SHIP_COLOR = Color.CYAN;
  
//  static int x = (new Random().nextInt(2) == 0) ? 0 : GameState.WIDTH;
//  static MyPosn velocity = (x == 0) ? new MyPosn(1, 0) : new MyPosn(-1, 0);
//  static MyPosn shipPosn = new MyPosn(x,
//                               (int) ((GameState.HEIGHT / 7.0) + new Random().nextInt(GameState.HEIGHT) * 5 / 7.0));
  
  Ship(MyPosn p, MyPosn velocity) {
    super(p, SHIP_RADIUS, SHIP_SPEED, velocity, SHIP_COLOR);
  }
  
  // TODO test
//  Ship() {
//    this(Ship.shipPosn, Ship.velocity);
//  }

  // METHODS
  Ship move() {   
    return new Ship(this.p.move(Ship.SHIP_SPEED, this.velocity), this.velocity);
  }

  boolean isHitBy(Bullet bullet) {
    return bullet.isInRadius(this.p, Ship.SHIP_RADIUS);
  }
  
  // is this ship hit by any bullet in the list given
  boolean isHitBy(ILoBullet bullets) {
    return bullets.anyBulletHits(this);
  }
}

class Bullet extends AItem {
  static final int INT_BULLET_RADIUS = 5;
  static final int MAX_BULLET_RADIUS = 10;
  static final int BULLET_SPEED = 8;
  static final Color BULLET_COLOR = Color.PINK;

  int n;  // nthExplosion, starting from 1 (player fired)
  int i;  // 0 <= i <= nthExplosion,
  // double theta = i * 360.0/(n + 1);       // angle for velocity vector
  // int radius;  // Bullets should grow in size along with n, 2 px larger than radius of the past one
                
  Bullet(MyPosn p, int radius, MyPosn velocity, int n, int i) {
    super(p, radius, BULLET_SPEED, velocity, BULLET_COLOR);
    this.n = n;
    this.i = i;
  }
  
  Bullet(MyPosn p, int radius, MyPosn velocity, int n) {
    this(p, radius, velocity, n, n);
  }
  
  // starting bullet
  Bullet() {  
    this(new MyPosn(GameState.WIDTH / 2, GameState.HEIGHT), INT_BULLET_RADIUS, new MyPosn(0, -BULLET_SPEED), 1, 0);
  }
  
  Bullet move() {   
    return new Bullet(this.p.move(1, this.velocity), this.radius, this.velocity, this.n, this.i);
  }
  
  boolean hitShip(Ship ship) {
    return ship.isInRadius(this.p, this.radius);
  }
  
  boolean hitAnyShip(ILoShip ships) {
    return ships.isHitBy(this);
  }
  
  // if this bullet hit any ship, produce a listof Bullet resulting from explosion
  ILoBullet explode(ILoShip ships) {
    // TODO
    if (this.hitAnyShip(ships)) {
      return this.explodeBullets(this.n);
    }
    return new ConsLoBullet(this, new MtLoBullet());
  }
  
  private MyPosn calculateVelocity(int n, int i) {
    double theta = i * 360.0 / (n + 1);
    return new MyPosn((int) (Bullet.BULLET_SPEED * Math.cos(Math.toRadians(theta))),
                      (int) (Bullet.BULLET_SPEED * Math.sin(Math.toRadians(theta))));
  }
  
  private int calculateRadius(int n) {
    return n <= 5 ? n * 2 : Bullet.MAX_BULLET_RADIUS; 
  }
  
 // make a list of bullet from i = n to i = 0
  public ILoBullet explodeBullets(int i) {
    if (i == 0) {
      return new ConsLoBullet(this.makeNextBullet(i), new MtLoBullet());
    }
    return new ConsLoBullet(this.makeNextBullet(i), this.explodeBullets(i - 1));
  }
  
  private Bullet makeNextBullet(int i) {
    // should variables be used for better readability?
    return new Bullet(this.p, this.calculateRadius(this.n + 1), this.calculateVelocity(this.n, i), this.n + 1, i);
  }

}

// TODO try putting this in Ship class
class Utils {
 // Ships should spawn at either the left or right ends of the screen, and then move across the screen
 // Where ships can spawn: Not in the top or bottom seventh of the screen
  Ship makeShip() {
    int x = (new Random().nextInt(2) == 0) ? 0 : GameState.WIDTH;
    MyPosn velocity = (x == 0) ? new MyPosn(1, 0) : new MyPosn(-1, 0);
    MyPosn shipPosn = new MyPosn(x,
                                 (int) ((GameState.HEIGHT / 7.0) + new Random().nextInt(GameState.HEIGHT) * 5 / 7.0));
    return new Ship(shipPosn, velocity);
  }
  
}

interface ILoShip {
  // Number of ships to spawn: between 1 and 3 inclusive, uniform distribution
  int SHIP_SPAWN_NUM = (int) (new Random().nextInt(3)) + 1;
  ILoShip spawnShips();
  ILoShip spawnShipsHelper(int num); 
  
  ILoShip moveAll();
  WorldScene placeAll(WorldScene s);
  
  ILoShip update(boolean spawn, ILoBullet bullets);
  ILoShip removeOffScreen();
  ILoShip removeHits(ILoBullet bullets);
  
  int countShipsHit(ILoBullet bullets);
  int countShipsHelper(int count, ILoBullet bullets);
  
  boolean isHitBy(Bullet bullet);
}

abstract class ALoShip implements ILoShip {
  public abstract ILoShip moveAll();
  public abstract WorldScene placeAll(WorldScene s);

  public abstract ILoShip removeOffScreen();
  public abstract ILoShip removeHits(ILoBullet bullets);
  
  public abstract boolean isHitBy(Bullet bullet);
  
  public ILoShip spawnShips() {
    return this.spawnShipsHelper(SHIP_SPAWN_NUM);
  }
  
  public ILoShip spawnShipsHelper(int num) {
    Ship newShip = new Utils().makeShip();
    if (num > 1) {
      return new ConsLoShip(newShip, this).spawnShipsHelper(num - 1);
    }
    return new ConsLoShip(newShip, this);
  }
  
  public ILoShip update(boolean spawn, ILoBullet bullets) {
    if (spawn) {
      System.out.println("Spawning ships.");
      return this.spawnShips().removeHits(bullets).moveAll().removeOffScreen();
    }
    return this.removeHits(bullets).moveAll().removeOffScreen();
  }
}

class MtLoShip extends ALoShip {

  public WorldScene placeAll(WorldScene s) {
    return s;
  }
  
  public ILoShip moveAll() {
    return this;
  }
  
  public ILoShip removeOffScreen() {
    return this;
  }

  public ILoShip removeHits(ILoBullet bullets) {
    return this;
  }

  public int countShipsHit(ILoBullet bullets) {
    return 0;
  }
  
  public int countShipsHelper(int count, ILoBullet bullets) {
    //System.out.println("TOTAL HITS: " + count);
    return count;
  }

  public boolean isHitBy(Bullet bullet) {
    return false;
  }
  
}

class ConsLoShip extends ALoShip {
  Ship first;
  ILoShip rest;
  
  ConsLoShip(Ship first, ILoShip rest) {
    this.first = first;
    this.rest = rest;
  }
  
  public WorldScene placeAll(WorldScene s) {
    return this.rest.placeAll(this.first.placeOnScene(s));
  }

  public ILoShip removeOffScreen() {
    if (this.first.isOffScreen()) {
      return this.rest.removeOffScreen()
;    }
    return new ConsLoShip(this.first, this.rest.removeOffScreen());
  }
  
  public ILoShip removeHits(ILoBullet bullets) {
//    System.out.println("removeHits");
//    System.out.println(this.first.info() + this.first);
    if (this.first.isHitBy(bullets)) {
      System.out.println("REMOVED!" + this.first.info());
      return this.rest.removeHits(bullets);
    }
    return new ConsLoShip(this.first, this.rest.removeHits(bullets));
  }
  
  public int countShipsHit(ILoBullet bullets) {
    return this.countShipsHelper(0, bullets);
  }
  
  public int countShipsHelper(int count, ILoBullet bullets) {
//    System.out.println("countShipsHelper");
//    System.out.println(this.first.info() + this.first);
    if (this.first.isHitBy(bullets)) {
      System.out.println("HIT!" + this.first.info());
      return this.rest.countShipsHelper(count + 1, bullets);
    }
    return this.rest.countShipsHelper(count, bullets);
  }

  public ILoShip moveAll() {
    return new ConsLoShip(this.first.move(), this.rest.moveAll());
  }

  public boolean isHitBy(Bullet bullet) {
    return this.first.isHitBy(bullet) || this.rest.isHitBy(bullet);
  }
}

interface ILoBullet {
  String info();
  ILoBullet fireBullet();
  WorldScene placeAll(WorldScene s);
  ILoBullet moveAll();
  ILoBullet explodeAll(ILoShip ships);
  ILoBullet removeOffScreen();
  ILoBullet removeStationary();
  
  // does any of the bullets in this list hit the given ship
  boolean anyBulletHits(Ship ship);

  ILoBullet update(ILoShip ships);
  ILoBullet join(ILoBullet other);
}

abstract class ALoBullet implements ILoBullet {
  public abstract ILoBullet removeOffScreen();
  public abstract boolean anyBulletHits(Ship ship);
  public abstract ILoBullet moveAll();
  public abstract ILoBullet explodeAll(ILoShip ships);
  public abstract ILoBullet removeStationary();
  public abstract ILoBullet join(ILoBullet other);
  
  
  public ILoBullet fireBullet() {
    return new ConsLoBullet(new Bullet(), this);
  }
  
  public ILoBullet update(ILoShip ships) {
    return this.explodeAll(ships).moveAll().removeOffScreen().removeStationary();
  }
}

class MtLoBullet extends ALoBullet {
  
  public String info() {
    return "empty";
  }
  
  public WorldScene placeAll(WorldScene s) {
    return s;
  }
  
  public ILoBullet moveAll() {
    return this;
  }
  
  public ILoBullet removeOffScreen() {
    return this;
  }

  public boolean anyBulletHits(Ship ship) {
    return false;
  }

  public ILoBullet explodeAll(ILoShip ships) {
    return this;
  }

  public ILoBullet join(ILoBullet other) {
    return other;
  }

  public ILoBullet removeStationary() {
    return this;
  }

}

class ConsLoBullet extends ALoBullet {
  Bullet first;
  ILoBullet rest;
  
  ConsLoBullet(Bullet first, ILoBullet rest) {
    this.first = first;
    this.rest = rest;
  }
  
  public String info() {
    return this.first.info() + this.rest.info();
  }

  public WorldScene placeAll(WorldScene s) {
    return this.rest.placeAll(this.first.placeOnScene(s));
  }
  
  public ILoBullet moveAll() {
    return new ConsLoBullet(this.first.move(), this.rest.moveAll());
  }
  
  public ILoBullet removeOffScreen() {
    if (this.first.isOffScreen()) {
      return this.rest.removeOffScreen();
    }
    return new ConsLoBullet(this.first, this.rest.removeOffScreen());
  }

  public boolean anyBulletHits(Ship ship) {
    return this.first.hitShip(ship) || this.rest.anyBulletHits(ship);
  }

  // remove all the exploded bullet(s) and add new bullets from the explosion(s)
  public ILoBullet explodeAll(ILoShip ships) {
      return this.first.explode(ships).join(this.rest.explodeAll(ships));
  }

  // reverse the order of this list and append with the other 
  public ILoBullet join(ILoBullet other) {
    return this.rest.join(new ConsLoBullet(this.first, other)) ;
  }

  public ILoBullet removeStationary() {
    if (this.first.velocity.isStationary()) {
      return this.rest.removeStationary();
    }
    return new ConsLoBullet(this.first, this.rest.removeStationary());
  }

}

class GameState extends World {
  static int WIDTH = 500;
  static int HEIGHT = 300;
  double TICK_RATE = 1.0 / 28.0;
  int currentTick;
  
  int SHIP_SPAWN_FREQ = 28;   // default: 28 (every 1 sec)
  int SHIP_SPAWN_NUM = (int) (new Random().nextInt(3)) + 1;
  
  static int FONT_SIZE = 13;
  static Color FONT_COLOR = Color.BLACK;
  
  int remainingBullets;
  int shipsDestroyed;
  
  ILoShip shipList;
  ILoBullet bulletList;
  
  // constructor
  GameState(int currentTick, ILoShip shipList, ILoBullet bulletList, int remainingBullets, int shipsDestroyed) {
    this.currentTick = currentTick;
    this.shipList = shipList;
    this.bulletList = bulletList;
    this.remainingBullets = remainingBullets;
    this.shipsDestroyed = shipsDestroyed;
  }
  
  GameState(int remainingBullets) {
    this(0, new MtLoShip(), new MtLoBullet(), remainingBullets, 0);
  }
  
  @Override
  public WorldScene makeScene() {
    WorldScene emptyScene = new WorldScene(WIDTH, HEIGHT);
    return showInfo(bulletList.placeAll(shipList.placeAll(emptyScene)));
  }
  
  private WorldScene showInfo(WorldScene scene) {
    WorldImage score = new TextImage("Score : " + this.shipsDestroyed, FONT_SIZE + 2, Color.RED);
    WorldImage shots = new TextImage("Bullets : " + this.remainingBullets, FONT_SIZE, FONT_COLOR);
    WorldImage info = new AboveAlignImage(AlignModeX.RIGHT, score, shots);

    return scene.placeImageXY(info, WIDTH - 45, 20);
  }
  
  public World onTick() {
    boolean spawn = this.timeToSpawn();
    ILoShip updatedShipList = shipList.update(spawn, bulletList);
    ILoBullet updatedBulletList = bulletList.update(shipList);
    int updatedShipsDestroyed = this.shipsDestroyed + shipList.countShipsHit(bulletList);
    
    if (spawn) {
      System.out.println("Current tick: " + this.currentTick + " SpawnShip: " + spawn);
      System.out.println("Bullet: " + this.bulletList.info());
    }
    // need to optimise, not sensitive
    return new GameState(currentTick + 1, 
                         updatedShipList,
                         updatedBulletList, 
                         remainingBullets,
                         updatedShipsDestroyed);
  }
  
  public GameState onKeyEvent(String key) {
    if (key.equals(" ") && this.remainingBullets > 0) {
      this.bulletList = this.bulletList.fireBullet();
      
      System.out.println("Bullet fired! Remaining: " + (this.remainingBullets - 1));
      System.out.println("Bullet: " + this.bulletList.info());
      
      return new GameState(this.currentTick, this.shipList, 
          this.bulletList, this.remainingBullets - 1, 
          shipsDestroyed);
    }
    else {
      return this;
    }
  }

  @Override
  public WorldEnd worldEnds() {
    if (this.remainingBullets <= 0 && bulletList instanceof MtLoBullet) {
      return new WorldEnd(true, this.makeEndScene());
    }
    else {
      return new WorldEnd(false, this.makeEndScene());
    }
  }

  public WorldScene makeEndScene() {
    WorldScene endScene = this.makeScene();
    return endScene.placeImageXY(new TextImage("Game Over", 24, FontStyle.BOLD, Color.red), WIDTH/2, HEIGHT/2);
  }
  
  public boolean startGame() {
    return super.bigBang(WIDTH, HEIGHT, TICK_RATE);
  }
  
  public boolean timeToSpawn() {
    return this.currentTick % SHIP_SPAWN_FREQ == 0;
  }
}

class ExampleNBullets {
  ExampleNBullets() {};

  MyPosn vLt = new MyPosn(-1, 0);
  MyPosn vRt = new MyPosn(1, 0);
  
  int SHIP_SPAWN_NUM = (int) (new Random().nextInt(3)) + 1;
  int x = (new Random().nextInt(2) == 0) ? 0 : GameState.WIDTH;
  MyPosn SHIP_POSN = new MyPosn(x, 
                               (int) ((GameState.HEIGHT / 7.0) + new Random().nextInt(GameState.HEIGHT) * 5 / 7.0));
  
  MyPosn pos1 = new MyPosn(250, 150);
  MyPosn pos2 = new MyPosn(250, 138);
  MyPosn pos3 = new MyPosn(250, 130);
  MyPosn startingV = new MyPosn(0, -1);
  
  // starting hit box: 12 px 
  Ship s1 = new Ship(pos1, vLt);
  ILoShip sEmpty = new MtLoShip();
  ILoShip shiplist1 = new MtLoShip().spawnShips();
  ILoShip sList1 = new ConsLoShip(s1, sEmpty);
  
  Bullet b1 = new Bullet(pos2, 5, startingV, 1);   // hit s1
  Bullet b2 = new Bullet(pos3, 5, startingV, 1);   // hit s1
  Bullet b3 = new Bullet(pos3, 5, startingV, 3);   // does not hit s1
  Bullet b4 = new Bullet(pos3, 5, startingV, 4);
  Bullet b1e1 = new Bullet(b1.p, 4, new MyPosn(1, 0), b1.n+1, 0);
  Bullet b1e2 = new Bullet(b1.p, 4, new MyPosn(-1, 0), b1.n+1, 1);
  Bullet b1e1e1 = new Bullet(b1e1.p, 6, new MyPosn((int)Math.cos(Math.toRadians(0)), (int)Math.sin(Math.toRadians(0))), b1e1.n+1, 0);
  Bullet b1e1e2 = new Bullet(b1e1.p, 6, new MyPosn((int)Math.cos(Math.toRadians(120)), (int)Math.sin(Math.toRadians(120))), b1e1.n+1, 1);
  Bullet b1e1e3 = new Bullet(b1e1.p, 6, new MyPosn((int)Math.cos(Math.toRadians(240)), (int)Math.sin(Math.toRadians(240))), b1e1.n+1, 2);
  
  ILoBullet bEmpty = new MtLoBullet();
  ILoBullet bList1 = new ConsLoBullet(b1, bEmpty);
  ILoBullet bList2 = new ConsLoBullet(b2, bEmpty);
  ILoBullet bList3 = new ConsLoBullet(b1, new ConsLoBullet(b2, bEmpty)); 
  ILoBullet bList4 = new ConsLoBullet(b3, new ConsLoBullet(b4, bEmpty)); 
  ILoBullet bList5 = new ConsLoBullet(b1, new ConsLoBullet(b3, bEmpty)); 
  
  boolean testShowGame(Tester t) {
    new GameState(15).startGame();
    return true;
  }
  
  boolean testExplode(Tester t) {
    return t.checkExpect(bEmpty.explodeAll(shiplist1), bEmpty)
        && t.checkExpect(bList1.explodeAll(sList1), new ConsLoBullet(b1e1, new ConsLoBullet(b1e2, bEmpty)))
        && t.checkExpect(bList5.explodeAll(sList1), new ConsLoBullet(b1e1, new ConsLoBullet(b1e2, 
                                                      new ConsLoBullet(b3, bEmpty))));
  }
  
  boolean testShipSpawnNum(Tester t) {
    return t.checkOneOf(SHIP_SPAWN_NUM, 1, 2, 3);
  }
  
  boolean testRandomShipPosn(Tester t) {
    return t.checkExpect(SHIP_POSN.x == 0 || SHIP_POSN.x == GameState.WIDTH, true)
        && t.checkExpect(SHIP_POSN.y >= GameState.HEIGHT / 7.0 && SHIP_POSN.y <= GameState.HEIGHT * 6 / 7.0, true);
  }
  
  boolean testIsHitBy(Tester t) {
    return t.checkExpect(s1.isHitBy(b1), true)     // bullet hits directly under ship
        && t.checkExpect(s1.isHitBy(b2), false);   // just before bullet hits under ship
  }
  
  boolean testExplodeBullet(Tester t) {
    return t.checkExpect(b1.explodeBullets(b1.n), new ConsLoBullet(b1e2,
                                                    new ConsLoBullet(b1e1, bEmpty)))
        && t.checkExpect(b1e1.explodeBullets(b1e1.n), new ConsLoBullet(b1e1e3,
                                                        new ConsLoBullet(b1e1e2, 
                                                           new ConsLoBullet(b1e1e1, bEmpty))));
  }
  
  boolean testJoin(Tester t) {
    return t.checkExpect(bEmpty.join(bList1), bList1)
        && t.checkExpect(bList1.join(bEmpty), bList1)
        && t.checkExpect(bList1.join(bList2), new ConsLoBullet(b2, bList1))
        && t.checkExpect(bList2.join(bList1), new ConsLoBullet(b1, bList2))
        && t.checkExpect(bList3.join(bList4), new ConsLoBullet(b2, new ConsLoBullet(b1, bList4)));
  }
}