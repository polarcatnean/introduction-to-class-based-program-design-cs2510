package lab10;

import tester.Tester;

class StringCreator {
  String str;
  Stack<String> history;
  
  StringCreator() { 
    this.str = "";
    this.history = new Stack<String>();
  }
  
  StringCreator(String str) { this.str = str; }
  
  // EFFECT: adds a character to the end of the string
  void add(Character c) {
    this.history.push(this.str);
    this.str = this.str.concat(c.toString());
  }
  
  // EFFECT: removes the last character of the string
  void remove() {
    if (this.str.isEmpty()) {
      throw new RuntimeException("Cannot remove from an empty string");
    }
    this.history.push(this.str);
    this.str = this.str.substring(0, this.str.length() - 1);
  }
  
  // returns the current string
  String getString() {
    return this.str;
  }
  
  // EFFECT: undoes the last operation done on the string
  void undo() {
    if (!history.isEmpty()) {
      this.str = this.history.pop();
    }
  }
}


class ExampleStringCreator {
  
  void testRun(Tester t) {
    StringCreator creator = new StringCreator();
    t.checkExpect(creator.getString(),"");
    creator.add('c');
    creator.add('d');
    t.checkExpect(creator.getString(),"cd");
    creator.add('e');
    t.checkExpect(creator.getString(),"cde");
    creator.remove();
    creator.remove();
    t.checkExpect(creator.getString(),"c");
    creator.undo(); //undoes the removal of 'd'
    t.checkExpect(creator.getString(),"cd");
    creator.undo(); //undoes the removal of 'e'
    creator.undo(); //undoes the addition of 'e'
    t.checkExpect(creator.getString(),"cd");
    creator.add('a');
    t.checkExpect(creator.getString(),"cda");
    creator.undo(); //undoes the addition of 'a'
    creator.undo(); //undoes the addition of 'd'
    creator.undo(); //undoes the addition of 'c'
    t.checkExpect(creator.getString(),"");
    creator.undo(); //no effect, there is nothing to undo
    t.checkExpect(creator.getString(),"");
}
}