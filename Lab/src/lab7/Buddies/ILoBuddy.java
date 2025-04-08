package lab7.Buddies;

// represents a list of Person's buddies
interface ILoBuddy {
  ILoBuddy append(ILoBuddy buddies);
  int count();
  
  // is the given person in this list
  boolean contains(Person p);
  boolean hasDirectBuddy(Person p);
  
  int countCommonBuddies(ILoBuddy other);
  
  boolean reachable(Person that, ILoBuddy visited);
  ILoBuddy getExtendedBuddies(ILoBuddy visited);
}
