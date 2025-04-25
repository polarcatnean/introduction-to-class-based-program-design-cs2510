package lecture25iterators;

import java.util.Iterator;

class IListIterator<T> implements Iterator<T> {
  IList<T> items;
  
  IListIterator(IList<T> items) {
    this.items = items;
  }

  public boolean hasNext() {
    return this.items.isCons();
  }

  public T next() {
    ConsList<T> itemsAsCons = this.items.asCons();
    T answer = itemsAsCons.first;
    this.items = itemsAsCons.rest;
    return answer;
  }
  
}


interface IList<T> extends Iterable<T> {
  boolean isCons();
  ConsList<T> asCons();
}

class MtList<T> implements IList<T> {

  public boolean isCons() {
    return false;
  }

  public ConsList<T> asCons() {
    throw new RuntimeException("Can't use this method on MtList");
  }

  @Override
  public Iterator<T> iterator() {
    return new IListIterator<T>(this);
  }
  
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  public boolean isCons() {
    return true;
  }

  public ConsList<T> asCons() {
    return this;
  }

  @Override
  public Iterator<T> iterator() {
    return new IListIterator<T>(this);
  }
}

