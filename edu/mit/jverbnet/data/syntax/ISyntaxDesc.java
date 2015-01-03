/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.data.syntax;

import java.util.List;

/**
 * A description of the syntax a of a verb.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface ISyntaxDesc {

	/**
	 * A list of syntax argument descriptors before the verb. The list
	 * will be non-<code>null</code>, but may be empty.
	 * 
	 * @return a non-null, but possibly empty, list of syntax argument
	 *         descriptors
	 * @since JVerbnet 1.2.0
	 */
	public List<ISyntaxArgDesc> getPreVerbDescriptors();

	/**
	 * A list of syntax argument descriptors after the verb. The list
	 * will be non-<code>null</code>, but may be empty.
	 * 
	 * @return a non-null, but possibly empty, list of syntax argument
	 *         descriptors
	 * @since JVerbnet 1.2.0
	 */
	public List<ISyntaxArgDesc> getPostVerbDescriptors();

}
