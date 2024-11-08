/* HashTableChained.java */

package dict;
import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
  protected int size;
  protected int numBuckets;
  protected DList[] table; 
  private static final float MAX_LOADFACTOR = 1;
  private static final float MIN_LOADFACTOR = 0.25f;



  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend 
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    // Your solution here.
    numBuckets = prevPrime(sizeEstimate*2);
    makeEmpty();
  }

  private float getLoadFactor(){
    return (float) size/numBuckets;
  }

  private void resize(float factor){
    int oldSize = numBuckets;
    DList[] oldTable = table;
    numBuckets = (int) (numBuckets * factor);
    makeEmpty();
    for (int i = 0; i < oldSize; i++) {
      DList currList = oldTable[i];
      DListNode currNode = currList.front();
      while (currNode != null) {
        this.insert(((Entry) currNode.item).key,((Entry) currNode.item).value);
        currNode = currList.next(currNode);
      }
    }
  }

  private int prevPrime(int num) {
    while (!(isPrime(num))) {
      num--;
    }
    return num;
  }

  private int nextPrime(int num) {
    while (!(isPrime(num))) {
      num++;
    }
    return num;
  }

  private boolean isPrime(int num) {
    int divisor = 2;
    while (divisor < num) {
      if ((num % divisor) == 0) {
        return false;
      }
      divisor++;
    }
    return true;
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    // Your solution here.
    numBuckets = 101;
    makeEmpty();
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
    // Replace the following line with your solution.
    int p = nextPrime(numBuckets*2);
    return Math.abs((329*code + 531) % p) % numBuckets;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    // Replace the following line with your solution.
    return size;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    // Replace the following line with your solution.
    if (size == 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    // Replace the following line with your solution.
    Entry e = new Entry();
    e.key = key;
    e.value = value;
    int index = compFunction(key.hashCode());
    table[index].insertBack(e);
    size++;
    if (this.getLoadFactor() >= MAX_LOADFACTOR){
      resize(2);
    }
    return e;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
    // Replace the following line with your solution.
    if (isEmpty()) {
      return null;
    }
    int index = compFunction(key.hashCode());
    DList d = table[index];
    DListNode node = d.front();
    while (node != null) {
      Object k = ((Entry) node.item).key();
      if (k.equals(key)) {
        return (Entry) node.item;
      }
      node = d.next(node);
    }
    return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
    // Replace the following line with your solution.
    if (isEmpty()) {
      return null;
    }
    int index = compFunction(key.hashCode());
    DList d = table[index];
    DListNode node = d.front();
    while (node != null) {
      Object k = ((Entry) node.item).key();
      if (k.equals(key)) {
        d.remove(node);
        size--;
        if (this.getLoadFactor() <= MIN_LOADFACTOR){
          resize(0.5f);
        }
        return (Entry) node.item;
      }
      node = d.next(node);
    }
    return null;
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
    // Your solution here.
    table = new DList[numBuckets];
    size = 0;
    for (int i = 0; i < numBuckets; i++) {
      table[i] = new DList();
    }
  }

  

}
