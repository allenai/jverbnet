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

import edu.mit.jverbnet.data.selection.IRestrType;
import edu.mit.jverbnet.data.selection.ISelRestrictions;

/**
 * Description of a syntactic argument.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface ISyntaxArgDesc {
	
	/**
	 * Returns the parent of this argument
	 * 
	 * @return the parent of this argument, never <code>null</code>.
	 * @since JVerbnet 1.2.0
	 */
	public ISyntaxDesc getParent();

	/**
	 * Returns the type of the syntactic argument. Will never be
	 * <code>null</code>.
	 * 
	 * @return the non-<code>null</code> type of the syntactic argument
	 * @since JVerbnet 1.0.0
	 */
	public SyntaxArgType getType();

	/**
	 * Returns the value of the syntactic argument. May be <code>null</code>.
	 * 
	 * @return the possibly <code>null</code> value of the syntactic argument
	 * @since JVerbnet 1.0.0
	 */
	public String getValue();

	/**
	 * If the {@link #getType()} method returns {@link SyntaxArgType#NP}, this
	 * method will return a noun phrase type. Otherwise it will return
	 * <code>null</code>.
	 * 
	 * @return <code>null</code> if the type of the argument is not a noun
	 *         phrase; otherwise a noun phrase type
	 * @since JVerbnet 1.0.0
	 */
	public INounPhraseType getNounPhraseType();

	/**
	 * Returns the selectional restrictions for this argument. Will never return
	 * <code>null</code>.
	 * 
	 * @return the selection restrictions for this argument
	 * @since JVerbnet 1.0.0
	 */
	public ISelRestrictions<? extends IRestrType> getSelRestrictions();

	/**
	 * Allows an immutable syntax argument to constructed piece by piece, and
	 * then instantiated with an argument to its parent.
	 * 
	 * @author Mark A. Finlayson
	 * @version 1.2.0
	 * @since JVerbnet 1.2.0
	 */
	public interface ISyntaxArgDescBuilder extends ISyntaxArgDesc {

		/**
		 * Sets the type of the syntactic argument.
		 * 
		 * @param type
		 *            the type of the syntactic argument
		 * @since JVerbnet 1.2.0
		 */
		public void setType(SyntaxArgType type);

		/**
		 * Sets the value of the syntactic argument
		 * 
		 * @param value
		 *            the value of the syntactic argument
		 * @since JVerbnet 1.2.0
		 */
		public void setValue(String value);

		/**
		 * Sets the noun phrase type of the argument. If non-<code>null</code>,
		 * this argument also sets the type to {@link SyntaxArgType#NP}.
		 * 
		 * @param type
		 *            the noun phrase type of the argument
		 * @since JVerbnet 1.2.0
		 */
		public void setNounPhrasetype(INounPhraseType type);

		/**
		 * Sets the selectional restrictions for this argument
		 * 
		 * @param restrs
		 *            the selectional restrictions for this argument
		 * @since JVerbnet 1.2.0
		 */
		public void setSelRestrictions(ISelRestrictions<? extends IRestrType> restrs);

		/**
		 * Creates a syntax arg descriptor from the information contained in the
		 * builder. If the builder does not have enough information to construct
		 * a complete descriptor, it will thrown an exception.
		 * 
		 * @param parent
		 *            the parent of the descriptor
		 * @return the new descriptor, if it can be created
		 * @since JVerbnet 1.2.0
		 */
		public ISyntaxArgDesc create(ISyntaxDesc parent);

	}

}
