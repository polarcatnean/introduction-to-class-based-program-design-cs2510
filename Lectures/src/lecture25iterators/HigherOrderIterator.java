package lecture25iterators;

import java.util.ArrayList;
import java.util.Iterator;
import tester.Tester;


class FibonacciIterator implements Iterator<Integer> {
  int prevValue;
  int currentValue;
  FibonacciIterator() {
    this.prevValue = 0;
    this.currentValue = 1;
  };

  public boolean hasNext() {
    return true;  // fibo sequence goes on indefinitely...
  }

  public Integer next() {
    int answer = this.prevValue + this.currentValue;
    
    // update values
    this.prevValue = this.currentValue;
    this.currentValue = answer;
    
    return answer;
  }  
}

// Higher-order Iterator
class EveryOtherElement<T> implements Iterator<T> {
  Iterator<T> source;
  EveryOtherElement(Iterator<T> source) {
    this.source = source;
  }

  public boolean hasNext() {
    return this.source.hasNext();
  }

  public T next() {
    T answer = this.source.next();
    
    if (this.source.hasNext()) {
      this.source.next();  // if there is a next element, have the source "skip" the next value
                           // by calling next(), it increments over currentIndex + 1
    }    
    return answer;
  }  
}

class TakeN<T> implements Iterator<T> {
  Iterator<T> source;
  int totalElements;
  int elementsTakenSofar;
  
  TakeN(Iterator<T> source, int totalElements) {
    this.source = source;
    this.totalElements = totalElements;
    this.elementsTakenSofar = 0;
  }

  public boolean hasNext() {
    return this.source.hasNext() 
        && this.elementsTakenSofar <= this.totalElements;
  }

  public T next() {
    this.elementsTakenSofar = this.elementsTakenSofar + 1;
    
    return this.source.next();
  }  
}

class AlternatingIterator<T> implements Iterator<T> {
  Iterator<T> first;
  Iterator<T> second;
  int currentIndex;
  
  AlternatingIterator(Iterator<T> first, Iterator<T> second) {
    this.first = first;
    this.second = second;
    this.currentIndex = 0;  // starting index = 0
  }

  public boolean hasNext() {
    if (currentIndex % 2 == 0) {
      // use an item from first iterator first
      return this.first.hasNext() || this.second.hasNext();   
    }
    return this.second.hasNext();
  }

  public T next() {
    // TODO account for when two iterators don't have the same length
    if (currentIndex % 2 == 0 && this.first.hasNext()) {
      currentIndex++;
      return this.first.next();
    }
    else {
      currentIndex++;
      return this.second.next();
    }
  }
  
}


class HigherOrderIteratorExamples {
  
  void testFiboIterator(Tester t) {
    int sum = 0;
    Iterator<Integer> fiboIterator = new FibonacciIterator();
    // sum of fibo numbers 
    for (int i = 0; i < 5; i++) {
      if (fiboIterator.hasNext()) {
        sum = sum + fiboIterator.next();
      }
    }
    t.checkExpect(sum, 19);  // sum (1 2 3 5 8)   
    
    // fibo number at a certain index
    int answer = 0;
    Iterator<Integer> fiboIterator2 = new FibonacciIterator();
    for (int i = 0; i < 5; i++) {
      if (fiboIterator2.hasNext()) {
        answer = fiboIterator2.next();
      }
    }
    t.checkExpect(answer, 8);  // 0 1 | 1 2 3 5 8
  }
  
  void testAltIterator(Tester t) {
    ArrayList<Integer> oddArr = new ArrayList<Integer>();
    oddArr.add(1); oddArr.add(3); oddArr.add(5); // oddArr.add(7);
    ArrayList<Integer> evenArr = new ArrayList<Integer>();
    evenArr.add(2); evenArr.add(4); evenArr.add(6); evenArr.add(8);  
    
    Iterator<Integer> oddIter = new ArrayListIterator<Integer>(oddArr);
    Iterator<Integer> evenIter = new ArrayListIterator<Integer>(evenArr);
    Iterator<Integer> altIterator = new AlternatingIterator<Integer>(oddIter, evenIter);
    
    ArrayList<Integer> result = new ArrayList<Integer>();
    ArrayList<Integer> expected1 = new ArrayList<Integer>();
    expected1.add(1); expected1.add(2); expected1.add(3); expected1.add(4); expected1.add(5);
    expected1.add(6); expected1.add(8);
    
    while (altIterator.hasNext()) {
      result.add(altIterator.next());
    }
    t.checkExpect(result, expected1);
  }
}


