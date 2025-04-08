package lab7.Buddies;

// represents a list of Person's buddies
class ConsLoBuddy implements ILoBuddy {
  Person first;
  ILoBuddy rest;

  ConsLoBuddy(Person first, ILoBuddy rest) {
    this.first = first;
    this.rest = rest;
  }

  public int count() {
    return 1 + rest.count();
  }
  
  public ILoBuddy append(ILoBuddy other) {
    return this.rest.append(new ConsLoBuddy(this.first, other));
  }
  
  public boolean contains(Person p) {
    return this.first.samePerson(p) || this.rest.contains(p);
  }
  
  //is the given person a direct buddy of anyone in this list
  public boolean hasDirectBuddy(Person p) {
   return this.first.hasDirectBuddy(p) || this.rest.hasDirectBuddy(p);
 }

  // count common buddies in this list and the given other list
  public int countCommonBuddies(ILoBuddy other) {
    if (other.contains(this.first)) {
      return 1 + this.rest.countCommonBuddies(other);
    }
    return this.rest.countCommonBuddies(other);
  }

  public boolean reachable(Person that, ILoBuddy visited) {
    // going through each buddy in todo list
    return this.first.reachable(that, this.rest, visited);
  }

  public ILoBuddy getExtendedBuddies(ILoBuddy visited) {
    // going through todo
    return this.first.getExtendedBuddies(this.rest, visited);
  }


}
