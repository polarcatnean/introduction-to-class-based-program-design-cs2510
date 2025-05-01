package lab10;

import java.util.ArrayList;
import java.util.Arrays;

import tester.Tester;

class Stack<T> {
  Deque<T> contents;
  
  Stack() {
    this.contents = new Deque<T>();
  }
  
  Stack(Deque<T> contents) {
    this.contents = contents;
  }
  
  // METHODS: 
  // adds an item to the stack
  void push(T item) {
    this.contents.addAtHead(item);
  } 
  
  boolean isEmpty() {
    return this.contents.size() == 0;
  }
  
  // removes and returns the top of the stack
  T pop() {
    return this.contents.removeFromHead();
  } 
}

class Utils {
  <T> ArrayList<T> reverse(ArrayList<T> source) {
    Stack<T> stack = new Stack<T>();
    ArrayList<T> result = new ArrayList<T>();
    
    for (T item : source) {
      stack.push(item);
    }
    
    while (!stack.isEmpty()) {
      result.add(stack.pop());
    }
    
    return result;
    
  }
}

class ExamplesStack {
  Utils u = new Utils();
  
  void testReverse(Tester t) {
    ArrayList<Integer> nums1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    ArrayList<Integer> nums2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
    
    t.checkExpect(u.reverse(nums1), nums2);
  }
}