package lecture13AbsBehaviours;
import tester.*;

interface ICompareRunners {
  // returns true if r1 comes before r2 according to this ordering
   boolean comesBefore(Runner r1, Runner r2);
}

// To compute a three-way/value comparison between two Runners
interface IRunnerComparator {
  // Returns a negative number if r1 comes before r2 in this order
  // Returns zero              if r1 is tied with r2 in this order
  // Returns a positive number if r1 comes after  r2 in this order
  int compare(Runner r1, Runner r2);
}

class CompareByTime implements IRunnerComparator {
//  public boolean comesBefore(Runner r1, Runner r2) {
//    return r1.time < r2.time;
//  }
  
  public int compare(Runner r1, Runner r2) {
    return r1.time - r2.time;
  }
}

class CompareByName implements IRunnerComparator {
  public int compare(Runner r1, Runner r2) {
    return r1.name.compareTo(r2.name);
  }
  
}

class ReverseComparator implements IRunnerComparator {
  IRunnerComparator comp; 
  
  ReverseComparator(IRunnerComparator comp) {
    this.comp = comp;
  }
  
  public int compare(Runner r1, Runner r2) {
    return - this.comp.compare(r1, r2);  // or this.comp.compare(r2, r1);
  }

}


interface IRunnerPredicate {
  boolean apply(Runner r);
}

class IsMale implements IRunnerPredicate {
  IsMale() {}
  
  public boolean apply(Runner r) {
    return r.isMale;
  }
}

class IsFemale implements IRunnerPredicate {
  IsFemale() {}
  
  public boolean apply(Runner r) {
    return !r.isMale;
  }
}

class IsPosUnder50 implements IRunnerPredicate {
  IsPosUnder50() {}
  
  public boolean apply(Runner r) {
    return r.pos < 50;
  }
}

class IsUnder40 implements IRunnerPredicate {
  public boolean apply(Runner r) {
    return r.age < 40;
  }
}

class AndPredicate implements IRunnerPredicate {
  IRunnerPredicate left;
  IRunnerPredicate right;
  
  AndPredicate(IRunnerPredicate left, IRunnerPredicate right) {
    this.left = left;
    this.right = right;
  }
  
  public boolean apply(Runner r) {
    return this.left.apply(r) && this.right.apply(r);
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

interface ILoRunner {
  // instead of passing a function to a function / making a higher order function
  // we pass an OBJECT that has the criteria method inside of it
  ILoRunner find(IRunnerPredicate p);
  
  ILoRunner sortBy(IRunnerComparator comp);
  ILoRunner insertBy(IRunnerComparator comp, Runner r);
  
  // Finds the fastest Runner in this list of Runners
  Runner findWinner();
  // Finds the first Runner in this list of Runners
  Runner getFirst();
  
  // find the minimum runner according to any comparison, by keeping track of the minimum seen so far
  Runner findMin(IRunnerComparator comp);
  Runner findMinHelp(IRunnerComparator comp, Runner acc);

  Runner findMax(IRunnerComparator comp);
}

class MtLoRunner implements ILoRunner {
  public ILoRunner find(IRunnerPredicate p) {
    return this;
  }

  public ILoRunner sortBy(IRunnerComparator comp) {
    return this;
  }

  public ILoRunner insertBy(IRunnerComparator comp, Runner r) {
    return new ConsLoRunner(r, this);
  }

  public Runner findWinner() {
    throw new RuntimeException("No winner of an empty list of Runners");
  }

  public Runner getFirst() {
    throw new RuntimeException("No first of an empty list of Runners");
  }

  public Runner findMin(IRunnerComparator comp) {
    throw new RuntimeException("No minimum runner available in this list!");
  }

  public Runner findMinHelp(IRunnerComparator comp, Runner acc) {
    return acc;
  }

  public Runner findMax(IRunnerComparator comp) {
    throw new RuntimeException("No maximum runner available in this list!");
  }
}

class ConsLoRunner implements ILoRunner {
  Runner first;
  ILoRunner rest;
  
  ConsLoRunner(Runner first, ILoRunner rest) {
    this.first = first;
    this.rest = rest;
  }
  
  public ILoRunner find(IRunnerPredicate p) {
    if (p.apply(this.first)) {
      return new ConsLoRunner(this.first, this.rest.find(p));
    }
    return this.rest.find(p);
  }

  public ILoRunner sortBy(IRunnerComparator comp) {
    return this.rest.sortBy(comp).insertBy(comp, this.first);
  }

  public ILoRunner insertBy(IRunnerComparator comp, Runner r) {
    if (comp.compare(this.first, r) < 0) {
      return new ConsLoRunner(this.first, this.rest.insertBy(comp, r));
    }
    return new ConsLoRunner(r, this);
  }

  public Runner findWinner() {
    return this.findMin(new CompareByTime());
  }

  public Runner getFirst() {
    return this.first;
  }

  public Runner findMin(IRunnerComparator comp) {
    return this.rest.findMinHelp(comp, this.first);
  }

  public Runner findMinHelp(IRunnerComparator comp, Runner acc) {
    if (comp.compare(acc, this.first) < 0) {
      return this.rest.findMinHelp(comp, acc);
    }
    return this.rest.findMinHelp(comp, this.first);
  }

  public Runner findMax(IRunnerComparator comp) {
    return this.findMin(new ReverseComparator(comp));
  }
  
}

class RunnersExamples {
  
  Runner johnny = new Runner("Kelly", 100, 999, true, 30, 360);
  Runner frank = new Runner("Shorter", 32, 888, true, 245, 130);
  Runner bill = new Runner("Rogers", 36, 777, true, 119, 129);
  Runner joan = new Runner("Benoit", 29, 444, false, 18, 155);

  ILoRunner mtlist = new MtLoRunner();
  ILoRunner list1 = new ConsLoRunner(johnny, new ConsLoRunner(joan, mtlist));
  ILoRunner list2 = new ConsLoRunner(frank, new ConsLoRunner(bill, list1));
  
  ILoRunner fRunners = new ConsLoRunner(joan, mtlist);
  ILoRunner mRunners = new ConsLoRunner(frank, 
                       new ConsLoRunner(bill, 
                       new ConsLoRunner(johnny, mtlist))); 
  
  boolean testRunnerFind(Tester t) {
    return t.checkExpect(list2.find(new IsMale()), mRunners)
        && t.checkExpect(list2.find(new IsFemale()), fRunners)
        && t.checkExpect(list2.find(new IsPosUnder50()), list1)
        && t.checkExpect(list2.find(new IsUnder40()), new ConsLoRunner(frank, 
                                                      new ConsLoRunner(bill,    
                                                      new ConsLoRunner(joan, mtlist))))
        && t.checkExpect(list2.find(new AndPredicate(new IsMale(), new IsUnder40())), 
            new ConsLoRunner(frank, new ConsLoRunner(bill, mtlist)));
  }
  
  boolean testFindMin(Tester t) {
    return t.checkExpect(list2.findMin(new CompareByTime()), bill)
        && t.checkExpect(list2.findMin(new CompareByName()), joan);
  }
  
  boolean testFindMax(Tester t) {
    return t.checkExpect(list2.findMax(new CompareByTime()), johnny)
        && t.checkExpect(list2.findMax(new CompareByName()), frank);
  }
}