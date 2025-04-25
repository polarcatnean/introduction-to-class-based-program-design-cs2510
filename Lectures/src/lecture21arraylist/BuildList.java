package lecture21arraylist;

import java.util.ArrayList;
import tester.Tester;

class arrayUtils {
  // Racket’s build-list function
  // take a number 𝑛 and a function object, and produce an ArrayList that results from 
  // invoking that function object on all numbers from 0 to 𝑛−1
  <U> ArrayList<U> buildList(int n, IFunc<Integer, U> func) {
    ArrayList<U> result = new ArrayList<U>();  
    
    // in buildList we very much care about counting off exactly 𝑛 items
    for (int i = 0; i < n; i = i + 1) {
      result.add(func.apply(i));   
    }
   
    return result;
  }
  
  <T, U> ArrayList<U> map(ArrayList<T> arr, IFunc<T, U> func) {
    ArrayList<U> result = new ArrayList<U>();
    
    // in map we do not care how many items are there. We go through the array
    for (T t : arr) {
      result.add(func.apply(t));
    }
    return result;
  }

}

class IntToString implements IFunc<Integer, String> {
  public String apply(Integer i) {
    return i.toString();
  }
}

class ExamplesArrayList {
  
  void testBuildList(Tester t) {
    ArrayList<String> list1 = new arrayUtils().buildList(5, new IntToString());
    ArrayList<String> expected = new ArrayList<String>();
    expected.add("0"); expected.add("1"); expected.add("2"); expected.add("3"); expected.add("4");
    t.checkExpect(list1, expected);
  }
  
}