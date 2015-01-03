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

/** 
 * A Verbnet version.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface IVersion extends Comparable<IVersion> {
	
	/** 
	 * Standard "unknown" version number
	 *
	 * @since JVerbnet 1.0.0
	 */
	public static final Version UNKNOWN = new Version(0,0,0,"unknown");
	
	/** 
	 * Returns the major version number, i.e., the '1' in '1.7.2'.
	 *
	 * @return the major version number, never negative
	 * @since JVerbnet 1.0.0
	 */
	public int getMajorVersion();
	
	/** 
	 * Returns the minor version number, i.e., the '7' in '1.7.2'.
	 *
	 * @return the minor version number, never negative
	 * @since JVerbnet 1.0.0
	 */
	public int getMinorVersion();

	/** 
	 * Returns the bugfix version number, i.e., the '2' in '1.7.2'.
	 *
	 * @return the bugfix version number, never negative
	 * @since JVerbnet 1.0.0
	 */
	public int getBugfixVersion();
	
	/**
	 * Returns the version qualifier, i.e., the 'abc' in '1.7.2.abc'. The
	 * qualifer is never <code>null</code>, but may be empty.
	 * 
	 * @return the version qualifier, non-<code>null</code>, potentially empty
	 * @since JVerbnet 1.0.0
	 */
	public String getQualifier();


}
