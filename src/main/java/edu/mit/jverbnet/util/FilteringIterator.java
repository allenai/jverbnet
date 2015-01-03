/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.util;

import static edu.mit.jverbnet.util.Checks.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that filters the output of another iterator.
 * 
 * This implementation does not support the {@link #remove()} method; if that
 * method is called it throws {@link UnsupportedOperationException}.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public abstract class FilteringIterator<E> implements Iterator<E> {
	
	// unchanging fields
	private final Iterator<E> backingItr;
	
	// changing fields
	private E next;
	
	/**
	 * Creates a new filtering iterator that uses the specified iterator to
	 * supply its elements.
	 * 
	 * @param backingItr
	 *            the iterator from which the filtering iterator gets its
	 *            elements; may not be <code>null</code>
	 * @throws NullPointerException
	 *             if the specified iterator is <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public FilteringIterator(Iterator<E> backingItr){
		NotNull.check("backingItr", backingItr);
		this.backingItr = backingItr;
		advance();
	}

	
	/**
	 * Finds the next valid element from the backing iterator.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	protected void advance() {
		next = null;
		E element;
		while(backingItr.hasNext()){
			element = backingItr.next();
			if(include(element)){
				next = element;
				break;
			}
		}
	}
	
	/**
	 * Returns <code>true</code> if the specified element should be returned by
	 * the filtering iterator; <code>false</code> if the element should be
	 * filtered.
	 * 
	 * @param element
	 *            the item to be checked
	 * @since JVerbnet 1.0.0
	 */
	protected abstract boolean include(E element);


	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return next != null;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.util.Iterator#next()
	 */
	public E next() {
		if(next == null)
			throw new NoSuchElementException();
		E result = next;
		advance();
		return result;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
