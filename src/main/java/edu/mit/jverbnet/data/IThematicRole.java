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

import edu.mit.jverbnet.data.selection.ISelRestrictions;
import edu.mit.jverbnet.data.selection.SemRestrType;

/**
 * A thematic role for a verb class.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface IThematicRole {

	/**
	 * Returns the verb class to which this thematic role belongs
	 * 
	 * @return the verb class to which this thematic role belongs
	 * @since JVerbnet 1.0.0
	 */
	public IVerbClass getVerbClass();

	/**
	 * Returns the type of this thematic role, never <code>null</code>.
	 * 
	 * @return the non-<code>null</code> type of this thematic role
	 * @since JVerbnet 1.0.0
	 */
	public ThematicRoleType getType();

	/**
	 * Returns the selectional restrictions of this thematic role. Will never be
	 * <code>null</code>.
	 * 
	 * @return the non-<code>null</code> selectional restrictions of this
	 *         thematic role
	 * @since JVerbnet 1.0.0
	 */
	public ISelRestrictions<SemRestrType> getSelRestrictions();
	
	/**
	 * An object allows an immutable member
	 * object to built up and then created.
	 * 
	 * @author Mark A. Finlayson
	 * @version 1.2.0
	 * @since JVerbnet 1.0.0
	 */
	public interface IThematicRoleBuilder extends IThematicRole {
		
		/**
		 * Sets the type for this thematic role.
		 * 
		 * @param type
		 *            the type for this thematic role
		 * @since JVerbnet 1.0.0
		 */
		public void setType(ThematicRoleType type);
		
		/**
		 * Sets the selectional restrictions for this thematic role
		 * 
		 * @param selRestrs
		 *            the selectional restrictions for this thematic role
		 * @since JVerbnet 1.0.0
		 */
		public void setSelRestrictions(ISelRestrictions<SemRestrType> selRestrs);

		/**
		 * Creates a thematic role from the information contained in the builder. If
		 * the builder does not have enough information to construct a complete
		 * thematic role, will thrown an exception.
		 * 
		 * @param parent
		 *            the parent of the thematic role
		 * @return the new thematic role, if it can be created
		 * @since JVerbnet 1.0.0
		 */
		public IThematicRole create(IVerbClass parent);
		
		
	}

}
