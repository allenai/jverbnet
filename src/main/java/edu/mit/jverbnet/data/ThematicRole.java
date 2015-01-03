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

import static edu.mit.jverbnet.util.Checks.NotNull;
import static edu.mit.jverbnet.util.Checks.thisMethodShouldNeverBeCalled;

import edu.mit.jverbnet.data.selection.ISelRestrictions;
import edu.mit.jverbnet.data.selection.SelRestrictions;
import edu.mit.jverbnet.data.selection.SemRestrType;

/** 
 * Default implementation of the {@link IThematicRole} interface.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class ThematicRole implements IThematicRole {
	
	// unchanging fields
	private final IVerbClass parent;
	private final ThematicRoleType type;
	private final ISelRestrictions<SemRestrType> selRestrs;

	/**
	 * Creates a new thematic role with the specified parameters
	 * 
	 * @param parent
	 *            the verb class to which this thematic role belongs
	 * @param type
	 *            the type of the thematic role
	 * @param selRestrs
	 *            the selectional restrictions
	 * @since JVerbnet 1.0.0
	 */
	public ThematicRole(IVerbClass parent, ThematicRoleType type,  ISelRestrictions<SemRestrType> selRestrs){
		NotNull.check("parent", parent);
		NotNull.check("type", type);
		if(selRestrs == null)
			selRestrs = SelRestrictions.emptyRestrictions();
		this.parent = parent;
		this.type = type;
		this.selRestrs = selRestrs;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IThematicRole#getVerbClass()
	 */
	public IVerbClass getVerbClass() {
		return parent;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IThematicRole#getType()
	 */
	public ThematicRoleType getType() {
		return type;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IThematicRole#getSelRestrictions()
	 */
	public ISelRestrictions<SemRestrType> getSelRestrictions() {
		return selRestrs;
	}
	
	/** 
	 * Default implementation of the IThematicRoleBuilder interface.
	 *
	 * @author Mark A. Finlayson
	 * @version 1.2.0
	 * @since JVerbnet 1.0.0
	 */
	public static class ThematicRoleBuilder implements IThematicRoleBuilder {
		
		// fields
		private ThematicRoleType type;
		private ISelRestrictions<SemRestrType> selRestrs;
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IMember#getVerbClass()
		 */
		public IVerbClass getVerbClass() {
			return thisMethodShouldNeverBeCalled();
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IThematicRole#getType()
		 */
		public ThematicRoleType getType() {
			return type;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IThematicRole#getSelRestrictions()
		 */
		public ISelRestrictions<SemRestrType> getSelRestrictions() {
			return selRestrs;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IThematicRole.IThematicRoleBuilder#setType(edu.mit.jverbnet.data.ThematicRoleType)
		 */
		public void setType(ThematicRoleType type) {
			this.type = type;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IThematicRole.IThematicRoleBuilder#setSelRestrictions(edu.mit.jverbnet.data.selection.ISelRestrictions)
		 */
		public void setSelRestrictions(ISelRestrictions<SemRestrType> selRestrs) {
			this.selRestrs = selRestrs;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IThematicRole.IThematicRoleBuilder#create(edu.mit.jverbnet.data.IVerbClass)
		 */
		public ThematicRole create(IVerbClass parent) {
			return new ThematicRole(parent, type, selRestrs);
		}
		
	}

}
