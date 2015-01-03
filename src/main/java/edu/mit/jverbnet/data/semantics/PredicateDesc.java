/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.data.semantics;

import static edu.mit.jverbnet.util.Checks.NotNull;
import static edu.mit.jverbnet.util.Checks.UnmodifiableMaskNullWithEmpty;
import static edu.mit.jverbnet.util.Checks.allElementsAre;
import static edu.mit.jverbnet.util.Checks.minSizeAndNotNull;

import java.util.Iterator;
import java.util.List;

/** 
 * Default implementation of {@link IPredicateDesc}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class PredicateDesc implements IPredicateDesc {
	
	// unchanging fields
	private final PredicateType value;
	private final boolean bool;
	private final List<ISemanticArgType> argTypes;

	/**
	 * Creates a new predicate description with the specified parameters
	 * 
	 * @param value
	 *            the type of this predicate
	 * @param bool
	 *            the boolean value for this predicate
	 * @param argTypes
	 *            may not be <code>null</code>, may not contain
	 *            <code>null</code>, and must have at least one element
	 * @since JVerbnet 1.0.0
	 */
	public PredicateDesc(PredicateType value, boolean bool, List<ISemanticArgType> argTypes){
		// check arguments
		NotNull.check("value", value);
		minSizeAndNotNull(1).check("argTypes", argTypes);
		argTypes = allElementsAre(NotNull, "argTypes", argTypes, UnmodifiableMaskNullWithEmpty);
		
		// assign fields
		this.value = value;
		this.bool = bool;
		this.argTypes = argTypes;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.semantics.IPredicateDesc#getValue()
	 */
	public PredicateType getValue() {
		return value;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.semantics.IPredicateDesc#getBool()
	 */
	public boolean getBool() {
		return bool;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.semantics.IPredicateDesc#getArgumentTypes()
	 */
	public List<ISemanticArgType> getArgumentTypes() {
		return argTypes;
	}
	
	// cache the string form
	private transient String toStr; 
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		if(toStr == null)
			toStr = toString(this);
		return toStr;
	}
	
	/**
	 * Produces a standard string format for a predicate descriptor.
	 * 
	 * @param pred
	 *            the predicate for which the string should be constructed
	 * @return the string representing the predicate
	 * @throws NullPointerException
	 *             if the specified predicate is <code>null</code>
	 * @since JVerbnet 1.2.0
	 */
	public static String toString(IPredicateDesc pred){
		StringBuilder sb = new StringBuilder();
		if(!pred.getBool())
			sb.append('!');
		sb.append(pred.getValue().getID());
		sb.append('(');
		for(Iterator<ISemanticArgType> i = pred.getArgumentTypes().iterator(); i.hasNext(); ){
			sb.append(i.next().toString());
			if(i.hasNext())
				sb.append(',');
		}
		sb.append(')');
		return sb.toString();
	}

}
