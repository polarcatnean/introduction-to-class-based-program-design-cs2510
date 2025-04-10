package lab6;

import tester.Tester;

// a json value
interface JSON {
  <T> T accept(JSONVisitor<T> visitor);}
 
// no value
class JSONBlank implements JSON {
  public <T> T accept(JSONVisitor<T> visitor) {
    return visitor.visitBlank(this);
  }
}
 
// a number
class JSONNumber implements JSON {
  int number;
 
  JSONNumber(int number) {
    this.number = number;
  }

  public <T> T accept(JSONVisitor<T> visitor) {
    return visitor.visitNumber(this);
  }
}
 
// a boolean
class JSONBool implements JSON {
  boolean bool;
 
  JSONBool(boolean bool) {
    this.bool = bool;
  }

  public <T> T accept(JSONVisitor<T> visitor) {
    return visitor.visitBoolean(this);
  }
}
 
// a string
class JSONString implements JSON {
  String str;
 
  JSONString(String str) {
    this.str = str;
  }

  public <T> T accept(JSONVisitor<T> visitor) {
    return visitor.visitString(this);
  }
}

// a list of JSON values
class JSONList implements JSON {
  IList<JSON> values;

  JSONList(IList<JSON> values) {
    this.values = values;
  }

  public <T> T accept(JSONVisitor<T> visitor) {
    return visitor.visitList(this);
  }
}

//a list of JSON pairs
class JSONObject implements JSON {
  IList<Pair<String, JSON>> pairs;

  JSONObject(IList<Pair<String, JSON>> pairs) {
    this.pairs = pairs;
  }

  public <T> T accept(JSONVisitor<T> visitor) {
    return visitor.visitObject(this);
  }
}

// generic pairs
class Pair<X, Y> {
  X x;
  Y y;

  Pair(X x, Y y) {
    this.x = x;
    this.y = y;
  }
}


interface JSONVisitor<T> {
  T visitBlank(JSONBlank b);
  T visitNumber(JSONNumber n);
  T visitBoolean(JSONBool b);
  T visitString(JSONString s);
  T visitList(JSONList jsonList);
  T visitObject(JSONObject o);
}

class JSONToNumber implements JSONVisitor<Integer>, IFunc<JSON, Integer> {
  public Integer apply(JSON json) {
    return json.accept(this);
  }
  
  public Integer visitBlank(JSONBlank b) {
    return 0;
  }

  public Integer visitNumber(JSONNumber n) {
    return n.number;
  }

  public Integer visitBoolean(JSONBool b) {
    return b.bool ? 1 : 0;
  }

  public Integer visitString(JSONString s) {
    return s.str.length();
  }

  public Integer visitList(JSONList list) {
    // TODO Check
    return new Utils().sumAll(list.values.map(new JSONToNumber()));
  }

  public Integer visitObject(JSONObject o) {
    // TODO Check
    return o.pairs.map(new GetValue())
                  .map(new JSONToNumber())
                  .foldr(new Sum(), 0);
  }
}

// returns the first JSON value it finds in a pair with that string as the keyword.
class JSONFind implements JSONVisitor<JSON>, IFunc<JSON, JSON> {
  String key;
  
  JSONFind(String key) {
    this.key = key;
  }

  public JSON apply(JSON input) {
    return input.accept(this);
  }
  
  public JSON visitBlank(JSONBlank b) {
    return b;
  }

  public JSON visitNumber(JSONNumber n) {
    return new JSONBlank();
  }

  public JSON visitBoolean(JSONBool b) {
    return new JSONBlank();
  }

  public JSON visitString(JSONString s) {
    return new JSONBlank();
  }

  public JSON visitList(JSONList jsonList) {
    return jsonList.values.findSolutionOrElse(this, new IsNotBlank(), new JSONBlank());
  }

  public JSON visitObject(JSONObject o) {
    // Fx is a function that consumes Pair<String, JSON> and produces JSON
    return o.pairs.findSolutionOrElse(new GetValueOf(this.key), new IsNotBlank(), new JSONBlank());
  }
}

class ExamplesJSON {
  
  JSON jb = new JSONBlank();
  JSON jn1 = new JSONNumber(7);
  JSON jn2 = new JSONNumber(2);
  JSON jb1 = new JSONBool(true);
  JSON jb2 = new JSONBool(false);
  JSON js1 = new JSONString("cat");
  JSON js2 = new JSONString("I'm hungry.");
  
  IList<JSON> list1 = new ConsList<JSON>(jb, 
                       new ConsList<JSON>(jn1, 
                        new ConsList<JSON>(jb1,
                          new ConsList<JSON>(js1, new MtList<JSON>()))));
  
  IList<Integer> list2 = new ConsList<Integer>(0, 
                          new ConsList<Integer>(7, 
                           new ConsList<Integer>(1,
                             new ConsList<Integer>(3, new MtList<Integer>()))));
  
  JSON jl1 = new JSONList(list1);
  IList<JSON> list3 = new ConsList<JSON>(jl1, 
                        new ConsList<JSON>(jn1, 
                         new ConsList<JSON>(jb1,
                           new ConsList<JSON>(js1, new MtList<JSON>()))));
  IList<Integer> list4 = new ConsList<Integer>(11, 
                          new ConsList<Integer>(7, 
                           new ConsList<Integer>(1,
                             new ConsList<Integer>(3, new MtList<Integer>()))));
  
  Pair<String, JSON> p1 = new Pair<String, JSON>("cat", jb);
  Pair<String, JSON> p2 = new Pair<String, JSON>("Mali", jn1);
  Pair<String, JSON> p3 = new Pair<String, JSON>("Kamin", jb1);
  
  IList<Pair<String, JSON>> lop0 = new MtList<Pair<String,JSON>>();
  IList<Pair<String, JSON>> lop1 = new ConsList<Pair<String, JSON>>(p1, new MtList<Pair<String, JSON>>());
  IList<Pair<String, JSON>> lop2 = new ConsList<Pair<String, JSON>>(p2, lop1);
  IList<Pair<String, JSON>> lop3 = new ConsList<Pair<String, JSON>>(p3, lop2);
  
  JSON jo0 = new JSONObject(lop0);
  JSON jo1 = new JSONObject(lop1);
  JSON jo2 = new JSONObject(lop2);
  JSON jo3 = new JSONObject(lop3);
  
  IList<JSON> list5 = new ConsList<JSON>(jb, 
                        new ConsList<JSON>(jn1, 
                            new ConsList<JSON>(jb1,
                              new ConsList<JSON>(js1, new ConsList<JSON>(jo3, new MtList<JSON>())))));
  JSON jlwithObj = new JSONList(list5);
  
  boolean testJSONToNumber(Tester t) {
    return t.checkExpect(list1.map(new JSONToNumber()), list2)
        && t.checkExpect(new JSONToNumber().apply(jo1), 0)
        && t.checkExpect(new JSONToNumber().apply(jo2), 7)
        && t.checkExpect(new JSONToNumber().apply(jo3), 8);
  }
  
  boolean testFindSolutionOrElse(Tester t) {
    return t.checkExpect(list1.findSolutionOrElse(new JSONToNumber(), new MoreThan(10), -1), -1)
        && t.checkExpect(list3.findSolutionOrElse(new JSONToNumber(), new MoreThan(10), -1), 11)
        && t.checkExpect(list1.findSolutionOrElse(new JSONToNumber(), new MoreThan(5), -1), 7);
  }
  
  boolean testJSONFind(Tester t) {
    return t.checkExpect(js1.accept(new JSONFind("cat")), new JSONBlank())
        && t.checkExpect(jo0.accept(new JSONFind("cat")), new JSONBlank())
        && t.checkExpect(jo1.accept(new JSONFind("cat")), jb)
        && t.checkExpect(jo3.accept(new JSONFind("Mali")), jn1)
        && t.checkExpect(jo3.accept(new JSONFind("Som")), new JSONBlank())
        && t.checkExpect(jlwithObj.accept(new JSONFind("Mali")), jn1);  
  }
  
  boolean testJSONFind2(Tester t) {
    JSON blank = new JSONBlank();
    JSON boolTrue = new JSONBool(true);
    JSON boolFalse = new JSONBool(false);
    JSON hello = new JSONString("Hello");
    JSON obj = new JSONObject(new ConsList<>(new Pair<>("x", new JSONBlank()),
                                new ConsList<>(new Pair<>("seven", new JSONNumber(7)), 
                                   new ConsList<>(new Pair<>("string", new JSONString("nowergh")), new MtList<>()))));
    
    JSON lstWithObj = new JSONList(new ConsList<>(hello, 
                        new ConsList<>(blank,
                          new ConsList<>(obj, new MtList<>()))));
    JSON nestedObjXBefore = new JSONObject(new ConsList<>(new Pair<>("Aha!", blank),
                                            new ConsList<>(new Pair<>("seven", boolFalse), 
                                              new ConsList<>(new Pair<>("obj", obj), new MtList<>()))));
    JSON nestedObjXAfter = new JSONObject(new ConsList<>(new Pair<>("obj", obj),
                                            new ConsList<>(new Pair<>("string", boolFalse), 
                                              new ConsList<>(new Pair<>("Aha!", blank), new MtList<>()))));
    JSON nestedObjXInside = new JSONObject(new ConsList<>(new Pair<>("Aha!", blank),
                                            new ConsList<>(new Pair<>("string", obj), 
                                                new ConsList<>(new Pair<>("seven", boolFalse), new MtList<>()))));

    return t.checkExpect(new JSONFind("x").apply(boolTrue), new JSONBlank()) 
        && t.checkExpect(new JSONFind("string").apply(obj), new JSONString("nowergh")) 
        && t.checkExpect(new JSONFind("string").apply(lstWithObj), new JSONString("nowergh")) 
        && t.checkExpect(new JSONFind("seven").apply(nestedObjXBefore), boolFalse) 
        && t.checkExpect(new JSONFind("string").apply(nestedObjXAfter), new JSONString("nowergh"))
        && t.checkExpect(new JSONFind("seven").apply(nestedObjXAfter), new JSONNumber(7)) 
        && t.checkExpect(nestedObjXAfter.accept(new JSONFind("seven")), new JSONNumber(7))
        && t.checkExpect(new JSONFind("string").apply(nestedObjXInside), obj);
  }
}