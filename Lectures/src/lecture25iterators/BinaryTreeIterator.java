package lecture25iterators;

import java.util.ArrayList;
import java.util.Iterator;

import tester.Tester;

// Using Deque as a QUEUE, where items are added at the end and removed from the front.
class BreadthFirstIterator<T> implements Iterator<T> {
  Deque<IBinaryTree<T>> worklist;
  
  BreadthFirstIterator(IBinaryTree<T> source) {
   this.worklist = new Deque<IBinaryTree<T>>();
   this.addIfNotLeaf(source);
  }
  
  //EFFECT: only adds the given binary-tree if it's not a leaf
  void addIfNotLeaf(IBinaryTree<T> bt) {
    if (bt.isNode()) {
      this.worklist.addAtTail(bt);
      // System.out.println("Adding to worklist: " + bt);
      // System.out.println("Updated worklist: " + this.worklist.details());
    }
  }
  
  public boolean hasNext() {
   return this.worklist.size() > 0;  // worklist is not empty
  }
  
  public T next() {
    // System.out.println("BFS worklist: " + this.worklist.details());
   // get the first item on the worklist
   BTNode<T> firstItem = this.worklist.removeFromHead().asNode();
   
   // add children to worklist
   this.addIfNotLeaf(firstItem.left);
   this.addIfNotLeaf(firstItem.right);
   
   return firstItem.data;
  }
}

// = Depth-First Traversal 
// produces the node, then recursively produces the left subtree of the node, then the right subtree
// Using Deque as a STACK
class PreOrderIterator<T> implements Iterator<T> {
  Deque<IBinaryTree<T>> worklist;
  
  PreOrderIterator(IBinaryTree<T> source) {
    this.worklist = new Deque<IBinaryTree<T>>();
    this.addIfNotLeaf(source);
   }
  //EFFECT: only adds the given binary-tree if it's not a leaf AT the FRONT
  void addIfNotLeaf(IBinaryTree<T> bt) {
    if (bt.isNode()) {
      this.worklist.addAtHead(bt);
    }
  }

  @Override
  public boolean hasNext() {
    return this.worklist.size() > 0;
  }

  @Override
  public T next() {
    // get the first item & remove it from worklist
    // we know all Node<T>s in Deque worklist are BTNode<T> (not leaf)
    BTNode<T> item = this.worklist.removeFromHead().asNode();  
    
    // add children to the front of worklist
    this.addIfNotLeaf(item.right);
    this.addIfNotLeaf(item.left);   // we want LEFT child at FRONT

    return item.data;
  }
}

// produces all the children of a node before producing the node itself (Lt, Rt, root)
class PostOrderIterator<T> implements Iterator<T> {
  Deque<IBinaryTree<T>> worklist;
  ArrayList<T> visited;
  
  PostOrderIterator(IBinaryTree<T> source) {
    this.worklist = new Deque<IBinaryTree<T>>();
    this.visited = new ArrayList<T>();
    // this.constructWorkList(source);
    this.addIfNotLeaf(source);
    }
   
  // construct the worklist first
  // EFFECT: modify this worklist by adding children at frontmost (Lt, Rt, root)
  void constructWorkList(IBinaryTree<T> tree) {
    if (tree.isNode()) {
      BTNode<T> node = tree.asNode();
      
      // Add current node after its children, but reversed due to stack
      this.worklist.addAtHead(tree);                      // Root last
      System.out.println("Adding: " + node.data);
      
      constructWorkList(node.right);                      // Right second
      constructWorkList(node.left);                       // Left first
    } 
    else { // tree is Leaf
      // do nothing = terminate
      return;
    }
  }
  
  // EFFECT: only adds the given binary-tree if it's not a leaf
  void addIfNotLeaf(IBinaryTree<T> bt) {
    if (bt.isNode()) {
      this.worklist.addAtHead(bt);
    }
  }

  @Override
  public boolean hasNext() {
    return this.worklist.size() > 0;
  }

  @Override
  public T next() {
    BTNode<T> item = this.worklist.removeFromHead().asNode();
    // another method: add while traversing
    // if item already visited -> produce it without adding children to worklist
    if (this.visited.contains(item.data)) {
      return item.data;
    }

    // else (node with children)
    this.visited.add(item.data);
    addIfNotLeaf(item);
    addIfNotLeaf(item.right);   
    addIfNotLeaf(item.left);   
    
    return next();
  }
  
}

// produces the left subtree of a node, then the node, then recursively produces the right subtree
// (Lt, root, Rt)
class InOrderIterator<T> implements Iterator<T> {
  Deque<IBinaryTree<T>> worklist;
  ArrayList<T> visited;

  InOrderIterator(IBinaryTree<T> source) {
    this.worklist = new Deque<IBinaryTree<T>>();
    this.visited = new ArrayList<T>();
    // this.constructWorkList(source);
    this.addIfNotLeaf(source);
   }
  
   // construct the worklist first
   // EFFECT: modify this worklist by adding (Lt, root, Rt)
   void constructWorkList(IBinaryTree<T> tree) {
     if (tree.isNode()) {
       BTNode<T> node = tree.asNode();

       constructWorkList(node.right);                    // Right last
       this.worklist.addAtHead(tree);                    // Root second
       System.out.println("Adding: " + node.data);
       constructWorkList(node.left);                     // Left first
     }
     else { // tree is Leaf
       // do nothing = terminate
     }
   }
   
  // EFFECT: only adds the given binary-tree if it's not a leaf
    void addIfNotLeaf(IBinaryTree<T> bt) {
      if (bt.isNode()) {
        this.worklist.addAtHead(bt);
      }
    }
  
  @Override
  public boolean hasNext() {
    return this.worklist.size() > 0;
  }

  @Override
  // Left -> Root -> Right
  public T next() {
    System.out.println("Current worklist: " + this.worklist.details());
    System.out.println("Current visited: " + this.visited);
    BTNode<T> item = this.worklist.removeFromHead().asNode();
    System.out.println("Current item: " + item.data);
    // another method: add while traversing
    // if item already visited -> produce it without adding children to worklist
    if (this.visited.contains(item.data)) {
      System.out.println("PRODUCE: " + item.data);
      return item.data;
    }

    // else (node with children)
    this.visited.add(item.data);
    System.out.println("add " + item.data + " in visited");
    addIfNotLeaf(item.right);
    System.out.println("add to worklist - " + item.right.toString());
    addIfNotLeaf(item);
    System.out.println("add to worklist - " + item.data);
    addIfNotLeaf(item.left);   
    System.out.println("add to worklist - " + item.left.toString());
    
    return next();
  }
  
}

interface IBinaryTree<T> {
  boolean isNode();
  BTNode<T> asNode();
  String toString();
}

class BTLeaf<T> implements IBinaryTree<T> {
  public boolean isNode() { return false; } 
  
  public BTNode<T> asNode() {
    throw new UnsupportedOperationException("Not a BTNode!");
  }
  
  @Override
  public String toString() {
    return "Leaf.";
  }
}

class BTNode<T> implements IBinaryTree<T> {
  T data;
  IBinaryTree<T> left;
  IBinaryTree<T> right;
  
  BTNode(T data, IBinaryTree<T> left, IBinaryTree<T> right) {
    this.data = data;
    this.left = left;
    this.right = right;
  }
  
  public boolean isNode() { return true; } 
  public BTNode<T> asNode() { return this; }
  
  @Override
  public String toString() {
    return this.data.toString();
  }

}


class BinaryTreeExamples {
  IBinaryTree<String> tree1;
  
  void init() {
    tree1 =
        new BTNode<String>("A",
          new BTNode<String>("B",
            new BTNode<String>("D", new BTLeaf<String>(), new BTLeaf<String>()),
            new BTNode<String>("E", new BTLeaf<String>(), new BTLeaf<String>())
          ),
          new BTNode<String>("C",
            new BTNode<String>("F", new BTLeaf<String>(), new BTLeaf<String>()),
            new BTNode<String>("G", new BTLeaf<String>(), new BTLeaf<String>())
          )
        );
  }
  
  void testDFT(Tester t) {
    init();
    PreOrderIterator<String> DFSIter = new PreOrderIterator<String>(tree1);
    
    t.checkExpect(DFSIter.next(), "A");
    t.checkExpect(DFSIter.next(), "B");
    t.checkExpect(DFSIter.next(), "D");
    t.checkExpect(DFSIter.next(), "E");
    t.checkExpect(DFSIter.next(), "C");
    t.checkExpect(DFSIter.next(), "F");
    t.checkExpect(DFSIter.next(), "G");
  }
  
  void testBFT(Tester t) {
    init();
    BreadthFirstIterator<String> BFSIter = new BreadthFirstIterator<String>(tree1);
    
    t.checkExpect(BFSIter.next(), "A");
    t.checkExpect(BFSIter.next(), "B");
    t.checkExpect(BFSIter.next(), "C");
    t.checkExpect(BFSIter.next(), "D");
    t.checkExpect(BFSIter.next(), "E");
    t.checkExpect(BFSIter.next(), "F");
    t.checkExpect(BFSIter.next(), "G");
  }
  
  
  void testPostOrder(Tester t) {
    init();
    PostOrderIterator<String> postOrderIter = new PostOrderIterator<String>(tree1);
    
    // System.out.println("post-order traversal: " + postOrderIter.worklist.details());
    t.checkExpect(postOrderIter.next(), "D");
    t.checkExpect(postOrderIter.next(), "E");
    t.checkExpect(postOrderIter.next(), "B");
    t.checkExpect(postOrderIter.next(), "F");
    t.checkExpect(postOrderIter.next(), "G");
    t.checkExpect(postOrderIter.next(), "C");
    t.checkExpect(postOrderIter.next(), "A");
  }
  
  void testInOrder(Tester t) {
    init();
    InOrderIterator<String> inOrderIter = new InOrderIterator<String>(tree1);
    
    // System.out.println("in-order traversal, next: " + inOrderIter.next());   
    t.checkExpect(inOrderIter.next(), "D");
    t.checkExpect(inOrderIter.next(), "B");
    t.checkExpect(inOrderIter.next(), "E");
    t.checkExpect(inOrderIter.next(), "A");
    t.checkExpect(inOrderIter.next(), "F");
    t.checkExpect(inOrderIter.next(), "C");
    t.checkExpect(inOrderIter.next(), "G");
  }
}
