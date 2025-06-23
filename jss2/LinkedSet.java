//********************************************************************
//  LinkedSet.java       Authors: Lewis/Chase
//
//  Represents a linked implementation of a set.
//********************************************************************
package jss2;
import jss2.exceptions.*;

import java.util.*;

public class LinkedSet<T> implements SetADT<T>, Iterable<T>
{
  private static Random rand = new Random();
  private int count;  // the current number of elements in the set 
  private LinearNode<T> contents; 

  /******************************************************************
    Creates an empty set.
  ******************************************************************/
  public LinkedSet()
  {
    count = 0;
    
    contents = null;
  }

  /******************************************************************
    Adds the specified element to this set if it is not already
    present.
  ******************************************************************/
  public void add (T element)
  {
    if (!(contains(element)))
    {
      LinearNode<T> node = new LinearNode<T> (element);
      node.setNext(contents);
      contents = node;
      count++;
    }
  }

  /******************************************************************
    Adds the contents of the parameter to this set.
  ******************************************************************/
  public void addAll (SetADT<T> set)
  {
	  Iterator<T> scan = set.iterator();
	  while (scan.hasNext())
		  add((T) scan.next());
  }

  /******************************************************************
    Removes and returns a random element from this set. Throws
    an EmptySetException if the set contains no elements.
  ******************************************************************/
  public T removeRandom() throws EmptySetException
  {
    LinearNode<T> previous, current;
    T result = null;

    if (isEmpty())
      throw new EmptySetException();
    
    int choice = rand.nextInt(count) + 1;
    
    if (choice == 1)
    {
      result = contents.getElement();
      contents = contents.getNext();
    }
    else
    {
      previous = contents;
      for (int skip=2; skip < choice; skip++)
        previous = previous.getNext();
      current = previous.getNext();
      result = current.getElement();
      previous.setNext(current.getNext());
    }
      
    count--;

    return result;
  }

  /******************************************************************
    Removes and returns the specified element from this set.
    Throws an EmptySetException if the set is empty and a
    NoSuchElemetnException if the target is not in the set.
  ******************************************************************/
  public T remove (T target) throws EmptySetException,
                                     NoSuchElementException
  {
    boolean found = false;
    LinearNode<T> previous, current;
    T result = null;

    if (isEmpty())
      throw new EmptySetException();

    if (contents.getElement().equals(target))
    {
      result = contents.getElement();
      contents = contents.getNext();
    }
    else
    {
      previous = contents;
      current = contents.getNext();
      for (int look=0; look < count && !found; look++)
        if (current.getElement().equals(target))
          found = true;
        else
        {
          previous = current;
          current = current.getNext();
        }

      if (!found)
        throw new NoSuchElementException();

        result = current.getElement();
        previous.setNext(current.getNext());
    
    }
    
    count--;
    
    return result;
  }
  
  /******************************************************************
    Returns a new set that is the union of this set and the
    parameter.
  ******************************************************************/
  public SetADT<T> union (SetADT<T> set)
  {
    LinkedSet<T> both = new LinkedSet<T>();
    both.addAll(this);
    both.addAll(set);
    return both;

  }
  
  /******************************************************************
    Returns true if this set contains the specified target
    element.
  ******************************************************************/
  public boolean contains (T target)
  {
	 LinearNode<T> current;
	 if (isEmpty()) return false;
	 int i; 
	 current=contents; 
	 while(current!=null)
	 {if (current.getElement().equals(target)) return true;
	      current = current.getNext();
	 }
	 return false;
  }
  
  /******************************************************************
    Returns true if this set contains exactly the same elements
    as the parameter.
  ******************************************************************/
  public boolean equals (SetADT<T> set)
  {
	  if (set.size()!=this.size()) return false;
	  Iterator<T> scan = set.iterator();
	  while (scan.hasNext())
		  if (!contains(scan.next()))return false;
	  LinearNode<T> current=contents;
	  while(current!=null)
	   {if (!set.contains(current.getElement())) return false;
	   current=current.getNext();
	   }
	  return true;
  }

  /******************************************************************
    Returns true if this set is empty and false otherwise. 
  ******************************************************************/
  public boolean isEmpty()
  {
	  return (count==0);
  }
 
  /******************************************************************
    Returns the number of elements currently in this set.
  ******************************************************************/
  public int size()
  {
	  return count;
  }

  /******************************************************************
    Returns an iterator for the elements currently in this set.
  ******************************************************************/
  public Iterator<T> iterator()
  {
    return new LinkedIterator<T> (contents, count);
  }

  /******************************************************************
    Returns a string representation of this set. 
  ******************************************************************/
  public String toString()
  {
	  LinearNode<T> current=contents;
	  String cad="";
	  while (current!=null)
	  {
		  cad+=current.getElement().toString()+"\n";
		  current=current.getNext();
	  }
	  return cad;
  }
  public SetADT<T> difference (SetADT<T> set)
  {
    LinkedSet<T> both = new LinkedSet<T>();
    LinearNode<T> current=contents;
    
    if (set==null) throw new EmptySetException();
    while (current!=null)
    {
    	if(!set.contains(current.getElement())) both.add(current.getElement());
        current=current.getNext();
    }       
    return both;
  }
  public SetADT<T> intersection (SetADT<T> set)
  {
    LinkedSet<T> both = new LinkedSet<T>();
    LinearNode<T> current=contents;
    T temp;
    if (set==null || count==0) return null;
    int count2 = set.size();
    if (count<=count2)
    while (current!=null)
    {if(set.contains(current.getElement())) both.add(current.getElement());
    current=current.getNext();
    }
    else
     {
       Iterator<T> scan = set.iterator();
       while (scan.hasNext())
       {
    	  temp = scan.next();
          if (contains(temp)) both.add (temp);
       }
     }   
    return both;
  }
}
