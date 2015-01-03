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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.jverbnet.data.syntax.ISyntaxArgDesc.ISyntaxArgDescBuilder;

/** 
 * Default implementation of {@link ISyntaxDesc}. 
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class SyntaxDesc implements ISyntaxDesc {
	
	/** 
	 * Default empty syntax object.
	 *
	 * @since JVerbnet 1.0.0
	 */
	public static final SyntaxDesc EMPTY_SYNTAX = new SyntaxDesc(null, null);
	
	// unchanging fields
	private final List<ISyntaxArgDesc> preArgs;
	private final List<ISyntaxArgDesc> postArgs;

	/**
	 * Creates a new syntax descriptor with the specified sets of arguments.
	 * 
	 * @param preArgBs
	 *            the list of first argument descriptors; may be
	 *            <code>null</code> or empty, but may not contain
	 *            <code>null</code>
	 * @param postArgBs
	 *            the list of second argument descriptors; may be
	 *            <code>null</code> or empty, but may not contain
	 *            <code>null</code>
	 * @since JVerbnet 1.2.0
	 */
	public SyntaxDesc(List<ISyntaxArgDescBuilder> preArgBs, List<ISyntaxArgDescBuilder> postArgBs){
		
		// pre-verb arguments
		List<ISyntaxArgDesc> preArgs;
		if(preArgBs == null || preArgBs.isEmpty()){
			preArgs = Collections.emptyList();
		} else {
			preArgs = new ArrayList<ISyntaxArgDesc>(preArgBs.size());
			for(ISyntaxArgDescBuilder argB : preArgBs)
				preArgs.add(argB.create(this));
			preArgs = Collections.unmodifiableList(preArgs);
		}
		
		// post-verb arguments
		List<ISyntaxArgDesc> postArgs;
		if(postArgBs == null || postArgBs.isEmpty()){
			postArgs = Collections.emptyList();
		} else {
			postArgs = new ArrayList<ISyntaxArgDesc>(postArgBs.size());
			for(ISyntaxArgDescBuilder argB : postArgBs)
				postArgs.add(argB.create(this));
			postArgs = Collections.unmodifiableList(postArgs);
		}
		
		// assign fields
		this.preArgs = preArgs;
		this.postArgs = postArgs;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.syntax.ISyntaxDesc#getPreVerbDescriptors()
	 */
	public List<ISyntaxArgDesc> getPreVerbDescriptors() {
		return preArgs;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.syntax.ISyntaxDesc#getPostVerbDescriptors()
	 */
	public List<ISyntaxArgDesc> getPostVerbDescriptors() {
		return postArgs;
	}

}
