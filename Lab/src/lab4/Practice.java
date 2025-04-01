package lab4;

import tester.Tester;

// 1 [Maybe Int]
interface IMaybeInt {}

class NoInt implements IMaybeInt {
  NoInt() {};
}

class SomeInt implements IMaybeInt {
  int n;
  
  SomeInt(int n) {
    this.n = n;
  }
}


// 2
interface ILoInt {
  // produces the integer (or a value indicating the list was empty) 
  // that appears in the longest consecutive sublist.
  int longestConsecutive();
  int longestConsecutiveHelper(int prevInt, int count, int longestInt, int longestCount);
  
}

class MtLoInt implements ILoInt {
  MtLoInt() {}

  public int longestConsecutive() {
    return Integer.MIN_VALUE;  // -2^31 in Java
  }

  public int longestConsecutiveHelper(int prevInt, int count, int longestInt, int longestCount) {
    if (count > longestCount) {
      return prevInt;
    }
    return longestInt;
  };
}

class ConsLoInt implements ILoInt {
  int first;
  ILoInt rest;
  
  ConsLoInt(int first, ILoInt rest) {
    this.first = first;
    this.rest = rest;
  }

  public int longestConsecutive() {
    // fields: ...this.first... ...this.rest... 
    // methods: ...this.rest.longestConsecutive()... ...this.rest.longestConsecutiveHelper()...
    return this.rest.longestConsecutiveHelper(this.first, 1, this.first, 1);
  }

  public int longestConsecutiveHelper(int prevInt, int count, int longestInt, int longestCount) {
    // TODO Auto-generated method stub
    if (this.first == prevInt) {
      return this.rest.longestConsecutiveHelper(prevInt, count + 1 , longestInt, longestCount);
    }
    if (count > longestCount) {
      return this.rest.longestConsecutiveHelper(this.first, 1, prevInt, count);
    }
    return this.rest.longestConsecutiveHelper(this.first, 1, longestInt, longestCount);
  }
}


class ExamplesLoInt {
  ILoInt empty = new MtLoInt();
  ILoInt list1 = new ConsLoInt(1,
      new ConsLoInt(1, new ConsLoInt(5, new ConsLoInt(5, new ConsLoInt(5, new ConsLoInt(4,
          new ConsLoInt(3, new ConsLoInt(4, new ConsLoInt(4, new ConsLoInt(4, empty))))))))));
  ILoInt list2 = new ConsLoInt(1, empty);
  ILoInt list3 = new ConsLoInt(2, new ConsLoInt(1, empty));
  ILoInt list4 = new ConsLoInt(2, new ConsLoInt(1, new ConsLoInt(1, empty)));
  ILoInt list5 = new ConsLoInt(1, new ConsLoInt(1, new ConsLoInt(2, empty)));
  ILoInt list6 = new ConsLoInt(-3,
      new ConsLoInt(-3, new ConsLoInt(-2, new ConsLoInt(-1, new ConsLoInt(-1, new ConsLoInt(-1, empty))))));

  
  boolean testLongestConsecutive(Tester t) {
    return t.checkExpect(empty.longestConsecutive(), Integer.MIN_VALUE)  // empty list
        && t.checkExpect(list2.longestConsecutive(), 1)  // one element
        && t.checkExpect(list3.longestConsecutive(), 2)  // all elements unique (earlier one wins)
        && t.checkExpect(list6.longestConsecutive(), -1) // negative numbers
        && t.checkExpect(list4.longestConsecutive(), 1)  // longest sublist at the end
        && t.checkExpect(list1.longestConsecutive(), 5)  // longest sublist in the middle
        && t.checkExpect(list5.longestConsecutive(), 1); // longest sublist at the front
  }
}

