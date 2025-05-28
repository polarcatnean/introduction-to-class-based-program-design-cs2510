package lecture31dijkstra;

import java.util.Deque;
import java.util.ArrayDeque;

// Represents a mutable collection of items
interface ICollection<T> {
  // Is this collection empty?
  boolean isEmpty();

  // EFFECT: adds the item to the collection with the given priority
  void add(T item, int priority);
  
  // Returns the first item of the collection
  // EFFECT: removes that first item
  T remove();

  // produce weight so far (first item's weight) to be used in findPath()
  int getWeightSoFar();
}



class Stack<T> implements ICollection<T> {
  Deque<WeightedItem<T>> items;

  Stack() {
    this.items = new ArrayDeque<WeightedItem<T>>();
  }

  public boolean isEmpty() {
    return this.items.isEmpty();
  }

  public T remove() {
    return this.items.removeFirst().data;
  }

  public void add(T item, int priority) {
    this.items.push(new WeightedItem<T>(item, 0));    
  }
  
  public void add(T item) {
    add(item, 0);
  }

  public int getWeightSoFar() {
    return items.getFirst().weight;
  }

}

class Queue<T> implements ICollection<T> {
  Deque<WeightedItem<T>> items;

  Queue() {
    this.items = new ArrayDeque<WeightedItem<T>>();
  }

  public boolean isEmpty() {
    return this.items.isEmpty();
  }

  public T remove() {
    return this.items.removeFirst().data;
  }

  public void add(T item, int priority) {
    items.addLast(new WeightedItem<T>(item, 0));    
  }
  
  public void add(T item) {
    add(item, 0);
  }

  public int getWeightSoFar() {
    return items.getFirst().weight;
  }

}

