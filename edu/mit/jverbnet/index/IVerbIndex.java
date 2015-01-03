/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.index;

import java.util.Iterator;
import java.util.Set;

import edu.mit.jverbnet.data.IMember;
import edu.mit.jverbnet.data.IVerbClass;
import edu.mit.jverbnet.data.IVersion;
import edu.mit.jverbnet.data.IWordnetKey;
import edu.mit.jverbnet.util.IHasLifecycle;

/** 
 * A verb index, usually Verbnet.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface IVerbIndex extends IHasLifecycle, Iterable<IVerbClass> {

	/**
	 * The version of the index. If the version cannot be determined, should
	 * return {@link IVersion#UNKNOWN}. If the version has not yet been
	 * determined, and the implementation does not determine the version
	 * automatically when called (e.g., the implementation requires a call to
	 * {@link #setVersion(IVersion)}) the method will throw an exception.
	 * 
	 * @throws IllegalStateException
	 *             if the version needs to be set
	 * @return the version of the index, or {@link IVersion#UNKNOWN}
	 * @since JVerbnet 1.0.0
	 */
	public IVersion getVersion();
	
	/**
	 * Sets the version for this index. May called at most once. If it is called
	 * a second time, it will throw an exception.
	 * 
	 * @throws NullPointerException
	 *             if the specified version is <code>null</code>
	 * @throws IllegalStateException
	 *             if the method is called twice
	 * @param v
	 *            the version to use for this index; may not be
	 *            <code>null</code>
	 * @since JVerbnet 1.1.0
	 */
	public void setVersion(IVersion v);
	
	/**
	 * Returns the verb class with the specified id, or <code>null</code> if no
	 * such verb class can be found in the index. Ids are usually of the form
	 * 'verb-##.#', e.g., 'accompany-51.7'.
	 * 
	 * @param id
	 *            the id of the class
	 * @return the verb with the specified id, or <code>null</code> if none
	 * @throws NullPointerException
	 *             if the specified id is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the specified id is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public IVerbClass getVerb(String id);
	
	/**
	 * Returns the root verb class with the specified id, or <code>null</code> if no
	 * such verb class can be found in the index. Ids are usually of the form
	 * 'verb-##.#', e.g., 'accompany-51.7'.
	 * 
	 * @param id
	 *            the id of the class
	 * @return the verb with the specified id, or <code>null</code> if none
	 * @throws NullPointerException
	 *             if the specified id is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the specified id is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public IVerbClass getRootVerb(String id);
	
	/**
	 * Returns all members in the index with the specified wordnet key.
	 * 
	 * A <code>null</code> key indicates all members that have no key specified.
	 * 
	 * @param key
	 *            the key for which members should be retrieved; may be
	 *            <code>null</code>
	 * @return a set of members with the specified wordnet keys, or an empty set
	 *         if none; will never return <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public Set<IMember> getMembers(IWordnetKey key);
	
	/**
	 * Returns all members in the index with the specified grouping.
	 * 
	 * A <code>null</code>, empty, or all whitespace grouping indicates all
	 * members that have no grouping specified.
	 * 
	 * @param grouping
	 *            the grouping for which members should be retrieved; may be
	 *            <code>null</code>, empty, or all whitespace.
	 * @return a set of members with the specified grouping, or an empty set if
	 *         none; will never return <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public Set<IMember> getMembers(String grouping);
	
	/**
	 * Returns an iterator over all verb classes in the index, in the order they
	 * are encountered in the data files.
	 * 
	 * @return an iterator over all verb classes in the index
	 * @since JVerbnet 1.0.0
	 */
	public Iterator<IVerbClass> iterator();
	
	/**
	 * Returns an iterator over all root verb classes in the index, in the order they
	 * are encountered in the data files.
	 * 
	 * @return an iterator over all root verb classes in the index
	 * @since JVerbnet 1.0.0
	 */
	public Iterator<IVerbClass> iteratorRoots();
	
	/**
	 * Returns an iterator over all wordnet keys in the index, in their natural
	 * order. The iterator will not return the 'null' wordnet key.
	 * 
	 * @return an iterator over all wordnet keys in the index
	 * @since JVerbnet 1.0.0
	 */
	public Iterator<IWordnetKey> iteratorWordnetKeys();
	
	/**
	 * Returns an iterator over all groupings in the index, in their natural
	 * order. The iterator will not return the 'null' grouping.
	 * 
	 * @return an iterator over all wordnet keys in the index
	 * @since JVerbnet 1.0.0
	 */
	public Iterator<String> iteratorGroups();

}
