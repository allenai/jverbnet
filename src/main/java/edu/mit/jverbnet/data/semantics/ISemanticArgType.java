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

import edu.mit.jverbnet.data.IVerbnetType;

/**
 * A verbnet type that represents subtypes of a particular {@link ArgType}
 * value.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface ISemanticArgType extends IVerbnetType {

	/**
	 * Returns the argument type of which this type is the subtype. Never
	 * <code>null</code>.
	 * 
	 * @return the non-<code>null</code> argument type of which this type is a
	 *         specialization
	 * @since JVerbnet 1.0.0
	 */
	public ArgType getArgType();
	
}
