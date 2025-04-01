/* 
;; An IceCream is one of:
;; -- EmptyServing
;; -- Scooped
 
;;An EmptyServing is a (make-empty-serving Boolean)
(define-struct empty-serving (cone))
 
;;A Scooped is a (make-scooped IceCream String)
(define-struct scooped (more flavor))

 */


/*             +------------+
 *             | IIceCream  |
 *             +------------+
 *             +------------+
 *                    |
 *                    |
 *                   / \ 
 *                   ---
 *                    |
 *        ----------------------------
 *        |                           |
 *   +--------------+       +----------------+
 *   | EmptyServing |       | Scooped        |
 *   +--------------+       +----------------+
 *   | boolean cone |       | IceCream more  |
 *   +--------------+       | String flavour |
 *                          +----------------+
 */
package assignment1;

interface IIceCream {}

class EmptyServing implements IIceCream {
  boolean cone;  // true if there is cone
  
  EmptyServing(boolean cone) {
    this.cone = cone;
  }
}

class Scooped implements IIceCream {
  IIceCream more;
  String flavour;
  
  Scooped(IIceCream more, String flavour) {
    this.more = more;
    this.flavour = flavour;
  }
}

class ExamplesIceCream {
  ExamplesIceCream() {}
  
  IIceCream cup = new EmptyServing(false);
  IIceCream cone = new EmptyServing(true);
  
  IIceCream caramel_swirl = new Scooped(this.cup, "caramel swirl");
  IIceCream black_raspberry = new Scooped(this.caramel_swirl, "black raspberry");
  IIceCream coffee = new Scooped(this.black_raspberry, "coffee");
  
  IIceCream strawberry = new Scooped(this.cone, "strawberry");
  IIceCream vanilla = new Scooped(this.strawberry, "vanilla");
  
  IIceCream order1 = new Scooped(this.coffee, "mint chip");
  IIceCream order2 = new Scooped(this.vanilla, "chocolate");
  
}
