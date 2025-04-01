/* IMenu is one of
 *  - Soup
 *  - Salad
 *  - Sandwich
 */
package lab1;

interface IMenu {
}

class Soup implements IMenu {
  String name;
  int price;   // in cents
  boolean vegetarian;
}

class Salad implements IMenu {
  String name;
  String dressing;
  int price;   
  boolean vegetarian;
}

class Sandwich implements IMenu {
  String name;
  String bread;
  String filling1;
  String filling2;
  int price;
}

