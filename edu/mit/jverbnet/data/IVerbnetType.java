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

import edu.mit.jverbnet.util.IHasID;

/**
 * A verbnet type is (generally) a type that is found in the verbnet XSD file.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface IVerbnetType extends IHasID {

	/**
	 * Name of the field holding the name of the XSD type name.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public static final String FIELD_NAME_XSD_TYPE_NAME = "XSD_TYPE_NAME";

	/**
	 * Name of the method that returns an object of this type given a particular
	 * id.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public static final String METHOD_NAME_getById = "getById";

}
