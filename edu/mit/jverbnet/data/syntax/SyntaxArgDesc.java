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

import static edu.mit.jverbnet.util.Checks.NotNull;

import edu.mit.jverbnet.data.selection.IRestrType;
import edu.mit.jverbnet.data.selection.ISelRestrictions;
import edu.mit.jverbnet.data.selection.SelRestrictions;
import edu.mit.jverbnet.data.syntax.SyntaxArgType.VALUE_RULE;
import edu.mit.jverbnet.util.Checks;

/** 
 * Default implementation of {@link ISyntaxArgDesc}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class SyntaxArgDesc implements ISyntaxArgDesc {
	
	// unchanging fields
	private final ISyntaxDesc parent;
	private final SyntaxArgType type;
	private final String value;
	private final INounPhraseType npType;
	private final ISelRestrictions<? extends IRestrType> selRestrs;

	/**
	 * Creates a new syntax argument description with the specified parameters.
	 * May be used to create all syntax arguments other than those of the
	 * {@link SyntaxArgType#NP} type.
	 * 
	 * @param parent the syntax descriptor parent of this argument; may not be <code>null</code>
	 * @param type
	 *            the type; may not be <code>null</code> or
	 *            {@link SyntaxArgType#NP}
	 * @param value
	 *            the value; must meet the restrictions of the type or an
	 *            exception is thrown; see
	 *            {@link VALUE_RULE#checkValue(String)}.
	 * @param selRestrs
	 *            the selectional restrictions for this argument; may be
	 *            <code>null</code>
	 * @since JVerbnet 1.2.0
	 */
	public SyntaxArgDesc(ISyntaxDesc parent, SyntaxArgType type, String value, ISelRestrictions<?  extends IRestrType> selRestrs){
		this(parent, type, value, null, selRestrs);
	}
	
	/**
	 * Creates a new syntax argument description of the {@link SyntaxArgType#NP}
	 * type with the specified parameters.
	 * 
	 * @param parent
	 *            the syntax descriptor parent of this argument; may not be
	 *            <code>null</code>
	 * @param type
	 *            the noun phrase type
	 * @param value
	 *            the value; must meet the restrictions of the type or an
	 *            exception is thrown; see {@link VALUE_RULE#checkValue(String)}
	 *            .
	 * @param selRestrs
	 *            the selectional restrictions for this argument; may be
	 *            <code>null</code>
	 * @since JVerbnet 1.2.0
	 */
	public SyntaxArgDesc(ISyntaxDesc parent, INounPhraseType type, String value, ISelRestrictions<? extends IRestrType> selRestrs){
		this(parent, SyntaxArgType.NP, value, type, selRestrs);
	}
	
	/**
	 * Creates a new syntax argument description with full control.
	 * 
	 * @param parent
	 *            the syntax descriptor parent of this argument; may not be
	 *            <code>null</code>
	 * @param type
	 *            the type; may not be <code>null</code>
	 * @param value
	 *            the value; must meet the restrictions of the type or an
	 *            exception is thrown; see {@link VALUE_RULE#checkValue(String)}
	 * @param npType
	 *            must be non-<code>null</code> if the type is
	 *            {@link SyntaxArgType#NP}; otherwise must be <code>null</code>
	 * @param selRestrs
	 *            the selectional restrictions for this argument; may be
	 *            <code>null</code>
	 * @throws NullPointerException
	 *             if type is <code>null</code>; or if the type requires the
	 *             value not to be <code>null</code> and the value is
	 *             <code>null</code>
	 * @since JVerbnet 1.2.0
	 */
	public SyntaxArgDesc(ISyntaxDesc parent, SyntaxArgType type, String value, INounPhraseType npType, ISelRestrictions<? extends IRestrType> selRestrs){
		// check arguments
		NotNull.check("parent", parent);
		NotNull.check("type", type);
		value = type.getValueRule().checkValue(value);
		if(selRestrs == null)
			selRestrs = SelRestrictions.emptyRestrictions();
		
		// check type/npType compatibility
		if(type == SyntaxArgType.NP && npType == null)
			throw new IllegalArgumentException("Must specific npType if argument type is NP");
		if(type != SyntaxArgType.NP && npType != null)
			throw new IllegalArgumentException("May not specific npType if argument type is not NP");
		
		// assign fields
		this.parent = parent;
		this.type = type;
		this.value = value;
		this.npType = npType;
		this.selRestrs = selRestrs;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc#getParent()
	 */
	public ISyntaxDesc getParent() {
		return parent;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc#getType()
	 */
	public SyntaxArgType getType() {
		return type;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc#getValue()
	 */
	public String getValue() {
		return value;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc#getNounPhraseType()
	 */
	public INounPhraseType getNounPhraseType() {
		return npType;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc#getSelRestrictions()
	 */
	public ISelRestrictions<? extends IRestrType> getSelRestrictions() {
		return selRestrs;
	}
	
	/** 
	 * Default implementation of syntax arg descriptor builder interface.
	 *
	 * @author Mark A. Finlayson
	 * @version 1.2.0
	 * @since JVerbnet 1.2.0
	 */
	public static class SyntaxArgDescBuilder implements ISyntaxArgDescBuilder {
		
		private SyntaxArgType type;
		private String value;
		private INounPhraseType npType;
		private ISelRestrictions<? extends IRestrType> selRestrs;

		/**
		 * Creates a new builder with the specified values. Any value may be set
		 * to <code>null</code> on construction.
		 * 
		 * @since JVerbnet 1.2.0
		 */
		public SyntaxArgDescBuilder(SyntaxArgType type, String value, INounPhraseType npType, ISelRestrictions<? extends IRestrType> selRestrs) {
			this.type = type;
			this.value = value;
			this.npType = npType;
			this.selRestrs = selRestrs;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc#getParent()
		 */
		public ISyntaxDesc getParent() {
			return Checks.thisMethodShouldNeverBeCalled();
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc#getType()
		 */
		public SyntaxArgType getType() {
			return type;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc#getValue()
		 */
		public String getValue() {
			return value;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc#getNounPhraseType()
		 */
		public INounPhraseType getNounPhraseType() {
			return npType;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc#getSelRestrictions()
		 */
		public ISelRestrictions<? extends IRestrType> getSelRestrictions() {
			return selRestrs;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc.ISyntaxArgDescBuilder#setType(edu.mit.jverbnet.data.syntax.SyntaxArgType)
		 */
		public void setType(SyntaxArgType type) {
			this.type = type;
			if(type != SyntaxArgType.NP)
				this.npType = null;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc.ISyntaxArgDescBuilder#setValue(java.lang.String)
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc.ISyntaxArgDescBuilder#setNounPhrasetype(edu.mit.jverbnet.data.syntax.INounPhraseType)
		 */
		public void setNounPhrasetype(INounPhraseType npType) {
			if(npType == null)
				return;
			this.type = SyntaxArgType.NP;
			this.npType = npType;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc.ISyntaxArgDescBuilder#setSelRestrictions(edu.mit.jverbnet.data.selection.ISelRestrictions)
		 */
		public void setSelRestrictions(ISelRestrictions<? extends IRestrType> restrs) {
			this.selRestrs = restrs;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.syntax.ISyntaxArgDesc.ISyntaxArgDescBuilder#create(edu.mit.jverbnet.data.syntax.ISyntaxDesc)
		 */
		public ISyntaxArgDesc create(ISyntaxDesc parent) {
			return new SyntaxArgDesc(parent, type, value, npType, selRestrs);
		}
		
	}

}
