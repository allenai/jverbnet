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

/** 
 * The main object in a verb index
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface IVerbClass {
	
	/**
	 * Returns the id of the verbnet class.
	 * 
	 * @return the id of this verbnet class; neither empty, all whitespace, or
	 *         <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public String getID();
	
	/**
	 * Returns the parent class of this verb class, or <code>null</code> if this
	 * class is a root class.
	 * 
	 * @return the parent class of this verb class, or <code>null</code> if this
	 *         class is a root class.
	 * @since JVerbnet 1.0.0
	 */
	public IVerbClass getParent();
	
	/**
	 * Sets the parent of this verb class. The parent may be set to
	 * <code>null</code>. The parent may only be set once: the method will throw
	 * an exception if it is called a second time.
	 * 
	 * @param parent
	 *            the parent of the class; may be <code>null</code>, in which
	 *            case the class will be a root class
	 * @throws IllegalStateException
	 *             if the method is called a second time
	 * @since JVerbnet 1.0.0
	 */
	public void setParent(IVerbClass parent);
	
	/**
	 * Returns <code>true</code> if this verb class has no parent;
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this verb class has no parent;
	 *         <code>false</code> otherwise.
	 * @since JVerbnet 1.0.0
	 */
	public boolean isRoot();
	
	/**
	 * Returns the members of this verb class. Will never return a
	 * <code>null</code> or empty list.
	 * 
	 * @return the non-<code>null</code>, non-empty list of members of this verb
	 *         class.
	 * @since JVerbnet 1.0.0
	 */
	public List<IMember> getMembers();
	
	/**
	 * Returns the thematic roles of this verb class. Will never return a
	 * <code>null</code> or empty list.
	 * 
	 * @return the non-<code>null</code>, non-empty list of thematic roles of this verb
	 *         class.
	 * @since JVerbnet 1.0.0
	 */
	public List<IThematicRole> getThematicRoles();
	
	/**
	 * Returns the frames this verb class. Will never return a
	 * <code>null</code> or empty list.
	 * 
	 * @return the non-<code>null</code>, non-empty list of frames of this verb
	 *         class.
	 * @since JVerbnet 1.0.0
	 */
	public List<IFrame> getFrames();
	
	/**
	 * Returns the subclasses of this verb class. Will never return a
	 * <code>null</code> list, but it may be empty. Each verb class in the
	 * returned list will return this object via its {@link #getParent()}
	 * method.
	 * 
	 * @return the non-<code>null</code> list of subclasses of this verb class.
	 * @since JVerbnet 1.0.0
	 */
	public List<IVerbClass> getSubclasses();

}
