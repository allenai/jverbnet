/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.data.selection;

import static edu.mit.jverbnet.util.Checks.NotNull;
import static edu.mit.jverbnet.util.Checks.UnmodifiableMaskNullWithEmpty;
import static edu.mit.jverbnet.util.Checks.allElementsAre;
import static edu.mit.jverbnet.util.Checks.allKeysAndValuesAre;

import java.util.List;
import java.util.Map;

import edu.mit.jverbnet.data.IVerbnetType;

/** 
 * A set of selectional restrictions.
 *
 * @param <T> the verbnet type found in this restriction object
 * @author Mark A. Finlayson
 * @version 1.0.0
 * @since JVerbnet 1.0.0
 */
public class SelRestrictions<T extends IVerbnetType> implements ISelRestrictions<T> {
	
	// the default empty selection restriction object
	private static final SelRestrictions<IVerbnetType> EMPTY_RESTRICTIONS = new SelRestrictions<IVerbnetType>(null, null, null);
	
	/** 
	 * Returns the default empty selection restriction object.
	 *
	 * @return  the default empty selection restriction object.
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IVerbnetType> SelRestrictions<T> emptyRestrictions(){
		return (SelRestrictions<T>)EMPTY_RESTRICTIONS;
	}
	
	// unchanging fields
	private final Logic logic;
	private final Map<T, Boolean> typeRestrs;
	private final List<ISelRestrictions<T>> subRestrs;

	/**
	 * Creates a new set of selectional restrictions with the specified
	 * arguments.
	 * 
	 * @param logic
	 *            the logic of the restriction; may be <code>null</code>
	 * @param typeRestrs
	 *            the individual type restrictions
	 * @param subRestrs
	 *            the subrestrictions
	 * @since JVerbnet 1.0.0
	 */
	public SelRestrictions(Logic logic, Map<T, Boolean> typeRestrs, List<ISelRestrictions<T>> subRestrs){
		// check arguments
		typeRestrs = allKeysAndValuesAre(NotNull, "typeRestrs", typeRestrs, UnmodifiableMaskNullWithEmpty);
		subRestrs = allElementsAre(NotNull, "subRestrs", subRestrs, UnmodifiableMaskNullWithEmpty);
		
		// if logic is non-null, the list or map must be non-empty
		int elements = typeRestrs.size() + subRestrs.size();
		if(logic != null && elements < 1)
			throw new IllegalArgumentException("If the logic is non-null, there must be at least two restriction elements");
		
		// assign fields
		this.logic = logic;
		this.typeRestrs = typeRestrs;
		this.subRestrs = subRestrs;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.selection.ISelRestrictions#getLogic()
	 */
	public Logic getLogic() {
		return logic;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.selection.ISelRestrictions#getTypeRestrictions()
	 */
	public Map<T, Boolean> getTypeRestrictions() {
		return typeRestrs;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.selection.ISelRestrictions#getSubSelRestrictions()
	 */
	public List<ISelRestrictions<T>> getSubSelRestrictions() {
		return subRestrs;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.selection.ISelRestrictions#isEmpty()
	 */
	public boolean isEmpty() {
		if(logic != null)
			return false;
		if(!typeRestrs.isEmpty())
			return false;
		if(!subRestrs.isEmpty())
			return false;
		return true;
	}

}
