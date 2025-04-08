package lab7.Buddies;

// represents an empty list of Person's buddies
class MTLoBuddy implements ILoBuddy {
  
  public boolean contains(Person p) {
    return false;
  }

  public ILoBuddy append(ILoBuddy other) {
    return other;
  }

  public int count() {
    return 0;
  }

  public int countCommonBuddies(ILoBuddy other) {
    return 0;
  }

  public boolean hasDirectBuddy(Person p) {
    return false;
  }

  public boolean reachable(Person that, ILoBuddy visited) {
    // todo empty
    return false;
  }

  public ILoBuddy getExtendedBuddies(ILoBuddy visited) {
    // todo empty
    return visited;
  }


}
