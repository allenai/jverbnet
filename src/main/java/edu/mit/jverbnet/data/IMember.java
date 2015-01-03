/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.data;

import java.util.List;
import java.util.Map;

/**
 * A member of a verb class.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface IMember {

	/**
	 * Returns the verb class to which this member belongs
	 * 
	 * @return the verb class to which this member belongs
	 * @since JVerbnet 1.0.0
	 */
	public IVerbClass getVerbClass();

	/**
	 * Returns the name of this member. Will not be <code>null</code>, empty, or
	 * all whitespace.
	 * 
	 * @return the name of this member
	 * @since JVerbnet 1.0.0
	 */
	public String getName();

	/**
	 * Returns a non-<code>null</code>, but possibly empty, map of Wordnet
	 * types. Each wordnet type is mapped to a boolean which indicates if the
	 * wordnet key is prefixed with a question mark, '?'.
	 * 
	 * @return a non-<code>null</code>, but possibly empty, map of Wordnet types
	 *         to question mark prefixes
	 * @since JVerbnet 1.1.0
	 */
	public Map<IWordnetKey, Boolean> getWordnetTypes();

	/**
	 * Returns a non-<code>null</code>, but possibly empty, list of Propbank
	 * ids.
	 * 
	 * @return a non-<code>null</code>, but possibly empty, list of Propbank ids
	 * @since JVerbnet 1.0.0
	 */
	public List<String> getGroupings();

	/**
	 * A class that returns modifiable lists, that allows an immutable member
	 * object to built up and then created.
	 * 
	 * @author Mark A. Finlayson
	 * @version 1.2.0
	 * @since JVerbnet 1.0.0
	 */
	public interface IMemberBuilder extends IMember {

		/**
		 * Sets the name for this member.
		 * 
		 * @param name
		 *            the name for this member
		 * @since JVerbnet 1.0.0
		 */
		public void setName(String name);

		/**
		 * Creates a member from the information contained in the builder. If
		 * the builder does not have enough information to construct a complete
		 * member, will thrown an exception.
		 * 
		 * @param parent
		 *            the parent of the member
		 * @return the new member, if it can be created
		 * @since JVerbnet 1.0.0
		 */
		public IMember create(IVerbClass parent);

	}

}
