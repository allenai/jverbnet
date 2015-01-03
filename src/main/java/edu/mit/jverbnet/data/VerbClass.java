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

import static edu.mit.jverbnet.util.Checks.NotEmptyOrBlank;
import static edu.mit.jverbnet.util.Checks.NotNull;
import static edu.mit.jverbnet.util.Checks.UnmodifiableMaskNullWithEmpty;
import static edu.mit.jverbnet.util.Checks.allElementsAre;
import static edu.mit.jverbnet.util.Checks.minSizeAndNotNull;

import java.util.LinkedList;
import java.util.List;

import edu.mit.jverbnet.data.IFrame.IFrameBuilder;
import edu.mit.jverbnet.data.IMember.IMemberBuilder;
import edu.mit.jverbnet.data.IThematicRole.IThematicRoleBuilder;

/** 
 * Default implementation of the {@link IVerbClass} interface
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class VerbClass implements IVerbClass {
	
	// unchanging fields
	private final String id;
	private final List<IMember> members;
	private final List<IThematicRole> roles;
	private final List<IFrame> frames;
	private final List<IVerbClass> subclasses;
	
	// changeable fields
	private boolean isParentSet = false;
	private IVerbClass parent = null;

	/**
	 * Creates a new verb class with the specified parameters
	 * 
	 * @param id
	 *            the id of the verb class; may not be <code>null</code>, empty,
	 *            or all whitespace
	 * @param memberBs
	 *            the builders that will create the members;
	 * @param roleBs
	 *            the builders that will create the thematic roles; may not be
	 *            <code>null</code>, may not contains <code>null</code>, and
	 *            must have at least one element
	 * @param frameBs
	 *            the builders that will create the frames; may not be
	 *            <code>null</code>, may not contains <code>null</code>, and
	 *            must have at least one element
	 * @param subclasses
	 *            the subclasses of this verb class; should not have their
	 *            parents set; may be <code>null</code> or empty
	 * @since JVerbnet 1.0.0
	 */
	public VerbClass(String id, List<IMemberBuilder> memberBs, List<IThematicRoleBuilder> roleBs, List<IFrameBuilder> frameBs, List<IVerbClass> subclasses){
		// check arguments;
		id = NotEmptyOrBlank.check("id", id);
		minSizeAndNotNull(1).check("builders", memberBs);
		minSizeAndNotNull(1).check("roles", roleBs);
		minSizeAndNotNull(1).check("frames", frameBs);
		
		memberBs = allElementsAre(NotNull, "builders", memberBs, null);
		roleBs = allElementsAre(NotNull, "roles", roleBs, null);
		frameBs = allElementsAre(NotNull, "frames", frameBs, null);
		subclasses = allElementsAre(NotNull, "subclasses", subclasses, UnmodifiableMaskNullWithEmpty);
				
		// create from builders
		List<IMember> members = new LinkedList<IMember>();
		List<IThematicRole> roles = new LinkedList<IThematicRole>();
		List<IFrame> frames = new LinkedList<IFrame>();
		for(IMemberBuilder b : memberBs)
			members.add(b.create(this));
		for(IThematicRoleBuilder b : roleBs)
			roles.add(b.create(this));
		for(IFrameBuilder b : frameBs)
			frames.add(b.create(this));
		
		// set parent on subclasses
		for(IVerbClass child : subclasses)
			child.setParent(this);
		
		// assign fields;
		this.id = id;
		this.members = UnmodifiableMaskNullWithEmpty.reallocate(members);
		this.roles = UnmodifiableMaskNullWithEmpty.reallocate(roles);
		this.frames = UnmodifiableMaskNullWithEmpty.reallocate(frames);
		this.subclasses = subclasses;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVerbClass#getID()
	 */
	public String getID() {
		return id;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVerbClass#getParent()
	 */
	public IVerbClass getParent() {
		checkParentSet(true);
		return parent;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVerbClass#isRoot()
	 */
	public boolean isRoot() {
		checkParentSet(true);
		return parent == null;
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVerbClass#setParent(edu.mit.jverbnet.data.IVerbClass)
	 */
	public synchronized void setParent(IVerbClass parent) {
		checkParentSet(false);
		isParentSet = true;
		this.parent = parent;
	}

	/**
	 * Throws an exception if the isParentSet flag is not the expected value.
	 * 
	 * @param expectedValue
	 *            the expected value of the isParentSet flag
	 * @since JVerbnet 1.0.0
	 */
	protected void checkParentSet(boolean expectedValue){
		if(isParentSet != expectedValue)
			throw new IllegalStateException("Parent is " + (isParentSet ? "already" : "not") + " set");
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVerbClass#getMembers()
	 */
	public List<IMember> getMembers() {
		return members;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVerbClass#getThematicRoles()
	 */
	public List<IThematicRole> getThematicRoles() {
		return roles;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVerbClass#getFrames()
	 */
	public List<IFrame> getFrames() {
		return frames;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVerbClass#getSubclasses()
	 */
	public List<IVerbClass> getSubclasses() {
		return subclasses;
	}

}
