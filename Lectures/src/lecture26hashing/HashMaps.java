package lecture26hashing;

import java.util.HashMap;

import tester.Tester;

// Represents one word in a dictionary, together with its definition
// Binary search trees of DictEntry, ordered alphabetically by word is more ideal
class HashMaps {
  String word;
  String meaning;
}

// Represents a Wiki entry
// we need the ability to quickly access an entry by its URL, 
// to quickly edit an entry, creating and deleting entries, 
// we have to worry about the cost of maintaining the sort order
class WikiEntry {
  String url;
  String contents;
}

// GOAL: design a data structure that gives us fast access to items when looked up by a key
// the ability to add, remove, and modify items, 
// and preferably does not require that we impose a sort order on the keys at all

/* 
 * ArrayLists where the value for key 𝑘 can be found at index 𝑠𝑢𝑚𝑚𝑎𝑟𝑖𝑧𝑒(𝑘)
 * where 𝑠𝑢𝑚𝑚𝑎𝑟𝑖𝑧𝑒 is some function that takes a key and produces a non-negative integer.
 * 
 * A hash code is simply a summarization of a piece of data as a number
 * and the hashCode method is a hash function that computes this hash code for us
*/

class ExampleHashMaps {
  void testHashMaps(Tester t) {
    HashMap<String, String> rooms = new HashMap<String, String>();
    // Put all the data into the hashtable
    rooms.put("Ben Lerner", "WVH314");
    rooms.put("Leena Razzaq", "WVH310B");
    rooms.put("Olin Shivers", "WVH308");
    rooms.put("Matthias Felleisen", "WVH308B");
    // Get the data
    t.checkExpect(rooms.get("Ben Lerner"), "WVH314");
    t.checkExpect(rooms.get("Olin Shivers"), "WVH310B");
    // Check that some data is present
    t.checkExpect(rooms.containsKey("Leena Razzaq"), true);
    t.checkExpect(rooms.containsKey("Amal Ahmed"), false);
    // Data that isn't present will return null
    t.checkExpect(rooms.get("Amal Ahmed"), null);
  }
}


