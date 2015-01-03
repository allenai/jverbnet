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

import edu.mit.jverbnet.data.IFrame;

/**
 * A semantic description for an {@link IFrame}.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface ISemanticDesc {

	/**
	 * Returns the list of predicates for this semantic description.
	 * 
	 * @return the list of predicates for this semantic description
	 * @since JVerbnet 1.0.0
	 */
	public List<IPredicateDesc> getPredicates();

}
