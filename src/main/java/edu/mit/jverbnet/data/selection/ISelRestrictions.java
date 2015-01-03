/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.data.selection;

import java.util.List;
import java.util.Map;

import edu.mit.jverbnet.data.IVerbnetType;

/**
 * Represents a set of selection restrictions. Selection restrictions may
 * operate over any type extending {@link IVerbnetType}. However, in the
 * canonical verbnet, they operate only over objects that implement
 * {@link IRestrType}.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface ISelRestrictions<T extends IVerbnetType> {

	/**
	 * Returns either <code>null</code> or the logic used to combine subelements
	 * of this instance.
	 * 
	 * @return the logic used to combine subelements of this description.
	 * @since JVerbnet 1.0.0
	 */
	public Logic getLogic();

	/**
	 * Returns a map of type restrictions for this instance.
	 * 
	 * @return a non-<code>null</code>, but possibly empty map of type
	 *         restrictions for this instance.
	 * @since JVerbnet 1.0.0
	 */
	public Map<T, Boolean> getTypeRestrictions();

	/**
	 * Returns a list of sub restrictions for this instance.
	 * 
	 * @return a non-<code>null</code>, but possibly empty list of sub
	 *         restrictions for this instance.
	 * @since JVerbnet 1.0.0
	 */
	public List<ISelRestrictions<T>> getSubSelRestrictions();

	/**
	 * Returns <code>true</code> if this restriction instance is equivalent to
	 * the empty restriction instance; namely, it has no restrictions.
	 * 
	 * @return <code>true</code> if this instance imposes no restrictions
	 * @since JVerbnet 1.0.0
	 */
	public boolean isEmpty();

	/**
	 * The two different kinds of combination logic that may be used in a
	 * restriction object.
	 * 
	 * @author Mark A. Finlayson
	 * @version 1.0.0
	 * @since JVerbnet 1.0.0
	 */
	public enum Logic {

		AND, OR;
	}

}
