package com.company;

import java.lang.Comparable;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.LinkedList;

public class SortedLinkedList<E> extends LinkedList<E> {
	private final Comparator<E> cmp;

    public SortedLinkedList(Comparator<E> compare) {
        cmp = compare;
    }
    
    public SortedLinkedList() {
        this(new DefaultComparator<>());
    }
    
    private static class DefaultComparator<E> implements Comparator<E> {
		@SuppressWarnings("unchecked")
        public int compare(E obj1, E obj2) {			
            return ((Comparable<E>) obj1).compareTo(obj2);
        }
    }
	
	public void sortedAdd(E e) {
		ListIterator<E> it = listIterator(0);
		if (!it.hasNext()) {
			it.add(e);
			return;
		}
		
		E elt;
		do {
			elt = it.next();
		} while (it.hasNext() && cmp.compare(e, elt) > 0);
		
		if (cmp.compare(e, elt) < 0)
			it.previous();
		it.add(e);
	}
}