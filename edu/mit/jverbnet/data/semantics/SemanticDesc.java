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

import java.util.List;

/** 
 * Default implementation of {@link ISemanticDesc}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class SemanticDesc implements ISemanticDesc {
	
	// unchanging fields
	private final List<IPredicateDesc> predList;

	/**
	 * Creates a new semantic description with the specified list of predicates
	 * 
	 * @param predList
	 *            the list of preducates for this semantic description; may not
	 *            be <code>null</code>, may not contain <code>null</code>, and
	 *            must contain at least one element.
	 * @since JVerbnet 1.0.0
	 */
	public SemanticDesc(List<IPredicateDesc> predList){
		minSizeAndNotNull(1).check("predList", predList);
		predList = allElementsAre(NotNull, "predList", predList, UnmodifiableMaskNullWithEmpty);
		this.predList = predList;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.semantics.ISemanticDesc#getPredicates()
	 */
	public List<IPredicateDesc> getPredicates() {
		return predList;
	}

}
