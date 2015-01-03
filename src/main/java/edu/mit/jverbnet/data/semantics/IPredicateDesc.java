/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.data.semantics;

import java.util.List;

/**
 * A predicate description.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface IPredicateDesc {

	/**
	 * The type of the predicate. Will never return <code>null</code>.
	 * 
	 * @return the non-<code>null</code> type of the predicate
	 * @since JVerbnet 1.0.0
	 */
	public PredicateType getValue();

	/**
	 * Returns the boolean value of this predicate.
	 * 
	 * @return the boolean value of this predicate
	 * @since JVerbnet 1.0.0
	 */
	public boolean getBool();

	/**
	 * Returns a list of semantic arguments, which will never be
	 * <code>null</code>, never contain <code>null</code>, and will always have
	 * at least one element.
	 * 
	 * @return the list of semantic arguments
	 * @since JVerbnet 1.0.0
	 */
	public List<ISemanticArgType> getArgumentTypes();

}
