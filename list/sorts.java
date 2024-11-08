
package list;

/* ListSorts.java */

import list.*;

public class sorts {

  private final static int SORTSIZE = 1000000;

  /**
   *  makeQueueOfQueues() makes a queue of queues, each containing one item
   *  of q.  Upon completion of this method, q is empty.
   *  @param q is a LinkedQueue of objects.
   *  @return a LinkedQueue containing LinkedQueue objects, each of which
   *    contains one object from q.
   **/
  public static LinkedQueue makeQueueOfQueues(LinkedQueue q) {
    // Replace the following line with your solution.
	  LinkedQueue LQueue = new LinkedQueue();
	  int size = q.size();
	  try{
		  for (int i = 0; i < size; ++i) {
			  LinkedQueue LinkedQ = new LinkedQueue();
			  LinkedQ.enqueue(q.dequeue());
	          LQueue.enqueue(LinkedQ);    
		  }
	    } catch(QueueEmptyException e) {
	    	System.out.println("Empty Quene");
	    	}
	  return LQueue;
  }

  /**
   *  mergeSortedQueues() merges two sorted queues into a third.  On completion
   *  of this method, q1 and q2 are empty, and their items have been merged
   *  into the returned queue.
   *  @param q1 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @param q2 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @return a LinkedQueue containing all the Comparable objects from q1 
   *   and q2 (and nothing else), sorted from smallest to largest.
   **/
  public static LinkedQueue mergeSortedQueues(LinkedQueue q1, LinkedQueue q2) {
    // Replace the following line with your solution.
	  LinkedQueue MSQueues = new LinkedQueue();
	  try {  
	  while (q1.size() > 0 && q2.size() > 0) {
		  Comparable c1 = (Comparable) q1.front();	
		  Comparable c2 = (Comparable) q2.front();  
		  int c = c1.compareTo(c2);
		  if (c <= 0) {
			  MSQueues.enqueue(q1.dequeue());}
		  else {
			  MSQueues.enqueue(q2.dequeue());
	        } 
	    }
	  while (q1.size() > 0) {
		  MSQueues.enqueue(q1.dequeue());
	      }   
	  while (q2.size() > 0) {
		  MSQueues.enqueue(q2.dequeue());
		  } 
	  }
	  catch(QueueEmptyException e) {
		  System.out.println("Empty Quene");
	      }
	  return MSQueues;
  }

  /**
   *  partition() partitions qIn using the pivot item.  On completion of
   *  this method, qIn is empty, and its items have been moved to qSmall,
   *  qEquals, and qLarge, according to their relationship to the pivot.
   *  @param qIn is a LinkedQueue of Comparable objects.
   *  @param pivot is a Comparable item used for partitioning.
   *  @param qSmall is a LinkedQueue, in which all items less than pivot
   *    will be enqueued.
   *  @param qEquals is a LinkedQueue, in which all items equal to the pivot
   *    will be enqueued.
   *  @param qLarge is a LinkedQueue, in which all items greater than pivot
   *    will be enqueued.  
   **/   
  public static void partition(LinkedQueue qIn, Comparable pivot, 
                               LinkedQueue qSmall, LinkedQueue qEquals, 
                               LinkedQueue qLarge) {
	    // Your solution here.
	    while (qIn.size() > 0) {
	      try {
	    	  int c = pivot.compareTo(qIn.front());
	    	  if (c < 0) {
	          qLarge.enqueue(qIn.dequeue());
	        } else if (c > 0) {
	          qSmall.enqueue(qIn.dequeue());
	        } else {
	          qEquals.enqueue(qIn.dequeue());
	        }
	      } catch(QueueEmptyException e) {
	    	  System.out.println("Empty Quene");
	      }
	    }
  }

  /**
   *  mergeSort() sorts q from smallest to largest using mergesort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void mergeSort(LinkedQueue q) {
    // Your solution here.
	  if (q.size() <= 1) {
	      return;    
	  }
	  LinkedQueue LQueue = makeQueueOfQueues(q);
	  try {
		  while (LQueue.size() > 1) {
			  LinkedQueue LQueue1 = null;
			  LinkedQueue LQueue2 = null;
			  
			  LQueue1 = (LinkedQueue)LQueue.dequeue();
	          LQueue2 = (LinkedQueue)LQueue.dequeue();

	          LinkedQueue p = mergeSortedQueues(LQueue1, LQueue2);
	          LQueue.enqueue(p);    
		  }
		  q.append((LinkedQueue)LQueue.dequeue());  
	  }
	  catch(QueueEmptyException e) {
		  System.out.println("Empty Quene");  
	  }
  }

  /**
   *  quickSort() sorts q from smallest to largest using quicksort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void quickSort(LinkedQueue q) {
    // Your solution here.
	  if (q.size() <= 1) {
	      return;    
	  }
	  int i = ((int)(Math.random() * 100000)) % q.size() + 1;
	  Comparable pivot = (Comparable) q.nth(i);
	  LinkedQueue Small = new LinkedQueue();
	  LinkedQueue Equal = new LinkedQueue();
	  LinkedQueue Large = new LinkedQueue();
	  partition(q, pivot, Small, Equal, Large);
	  quickSort(Small);
	  quickSort(Large);
	  q.append(Small);
	  q.append(Equal);
	  q.append(Large);
  }

  /**
   *  makeRandom() builds a LinkedQueue of the indicated size containing
   *  Integer items.  The items are randomly chosen between 0 and size - 1.
   *  @param size is the size of the resulting LinkedQueue.
   **/
  public static LinkedQueue makeRandom(int size) {
    LinkedQueue q = new LinkedQueue();
    for (int i = 0; i < size; i++) {
      q.enqueue(new Integer((int) (size * Math.random())));
    }
    return q;
  }

  /**
   *  main() performs some tests on mergesort and quicksort.  Feel free to add
   *  more tests of your own to make sure your algorithms works on boundary
   *  cases.  Your test code will not be graded.
   **/
  

}
