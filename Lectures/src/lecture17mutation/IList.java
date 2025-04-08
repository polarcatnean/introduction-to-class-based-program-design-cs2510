package lecture17mutation;


//Represents functions of signature A -> R, 
interface IFunc<A, R> {
  R apply(A input);
}

interface IFunc2<A1, A2, R> {
  R apply(A1 arg1, A2 arg2);
}

interface IPred<T> {
  boolean apply(T arg);
}



// generic list
interface IList<T> {
  // map over a list, and produce a new list with a (possibly different)
  // element type
  <U> IList<U> map(IFunc<T, U> f);
  
  <U> U foldr(IFunc2<T, U, U> f, U base);
  
  // finds the first element in the list where the result of function applied to that element passes the predicate, 
  // and then returns that result. If no such element is found, backup is returned.
  <U> U findSolutionOrElse(IFunc<T, U> convert, IPred<U> pred, U backup);

  <U> U find(IPred<T> pred, IFunc<T, U> f, U backup);

}

// empty generic list
class MtList<T> implements IList<T> {
  public <U> IList<U> map(IFunc<T, U> f) {
    return new MtList<U>();
  }

  public <U> U foldr(IFunc2<T, U, U> f, U base) {
    return base;
  }

  public <U> U findSolutionOrElse(IFunc<T, U> convert, IPred<U> pred, U backup) {
    return backup;
  }

  public <U> U find(IPred<T> pred, IFunc<T, U> f, U backup) {
    return backup;
  }

}

// non-empty generic list
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  public <U> IList<U> map(IFunc<T, U> f) {
    return new ConsList<U>(f.apply(this.first), this.rest.map(f));
  }

  public <U> U foldr(IFunc2<T, U, U> f, U base) {
    return f.apply(this.first, this.rest.foldr(f, base));
  }

  public <U> U findSolutionOrElse(IFunc<T, U> convert, IPred<U> pred, U backup) {
    U solution = convert.apply(this.first);
    if (pred.apply(solution)) {
      return solution;
    }
    return this.rest.findSolutionOrElse(convert, pred, backup);
  }

  public <U> U find(IPred<T> pred, IFunc<T, U> f, U backup) {
    if (pred.apply(this.first)) {
      return f.apply(this.first);
    }
    return this.rest.find(pred, f, backup);
  }

}