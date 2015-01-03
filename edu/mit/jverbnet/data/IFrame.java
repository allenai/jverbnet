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

import edu.mit.jverbnet.data.semantics.ISemanticDesc;
import edu.mit.jverbnet.data.syntax.ISyntaxDesc;

/** 
 * A frame from a verb class.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface IFrame {
	
	/**
	 * Returns the verb class to which this frame belongs
	 * 
	 * @return the verb class to which this frame belongs
	 * @since JVerbnet 1.0.0
	 */
	public IVerbClass getVerbClass();
	
	/**
	 * Returns the description number of the frame. Usually something like "0.2", but may be <code>null</code>.
	 * 
	 * @return the description number of the frame, possibly <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public String getDescriptionNumber();
	
	/**
	 * Returns the primary type for this frame. Never <code>null</code>.
	 * 
	 * @return the non-<code>null</code> primary type for this frame
	 * @since JVerbnet 1.0.0
	 */
	public FrameType getPrimaryType();
	
	/**
	 * Returns the secondary type for this frame. May be <code>null</code>.
	 * 
	 * @return the secondary type for this frame
	 * @since JVerbnet 1.0.0
	 */
	public FrameType getSecondaryType();
	
	/** 
	 * Returns the xtag for this frame.  Usually something like "0.2", but may be <code>null</code>.
	 *
	 * @return xtag for this frame, possibly <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public String getXTag();
	
	/**
	 * Returns a list of examples for this frame. May be empty, but not
	 * <code>null</code>.
	 * 
	 * @return the non-<code>null</code>, possibly empty list of examples for
	 *         this frame
	 * @since JVerbnet 1.0.0
	 */
	public List<String> getExamples();
	
	/**
	 * Returns the syntactic descriptor for this frame. Never <code>null</code>.
	 * 
	 * @return the non-<code>null</code> syntactic descriptor for this frame.
	 * @since JVerbnet 1.0.0
	 */
	public ISyntaxDesc getSyntax();
	
	/**
	 * Returns the semantic descriptor for this frame. Never <code>null</code>.
	 * 
	 * @return the non-<code>null</code> semantic descriptor for this frame.
	 * @since JVerbnet 1.0.0
	 */
	public ISemanticDesc getSemantics();
	
	/** 
	 * Allows an immutable frame to be constructed piece by piece
	 *
	 * @author Mark A. Finlayson
	 * @version 1.0.0
	 * @since JVerbnet 1.0.0
	 */
	public interface IFrameBuilder extends IFrame {
		
		/** 
		 * Sets the description number.
		 *
		 * @param descNum the description number
		 * @since JVerbnet 1.0.0
		 */
		public void setDescriptionNumber(String descNum);
		
		/** 
		 * Sets the primary type.
		 *
		 * @param type the primary type
		 * @since JVerbnet 1.0.0
		 */
		public void setPrimaryType(FrameType type);
		
		/** 
		 * Sets the secondary type.
		 *
		 * @param type the secondary type
		 * @since JVerbnet 1.0.0
		 */
		public void setSecondaryType(FrameType type);
		
		/** 
		 * Sets the xtag.
		 *
		 * @param xtag the xtag
		 * @since JVerbnet 1.0.0
		 */
		public void setXTag(String xtag);
		
		/** 
		 * Sets the syntactic descriptor.
		 *
		 * @param syntax the syntactic descriptor.
		 * @since JVerbnet 1.0.0
		 */
		public void setSyntax(ISyntaxDesc syntax);
		
		/** 
		 * Sets the semantic descriptor.
		 *
		 * @param semantics the semantic descriptor
		 * @since JVerbnet 1.0.0
		 */
		public void setSemantics(ISemanticDesc semantics);
		
		/**
		 * Creates a frame from the information contained in the builder. If the
		 * builder does not have enough information to construct a complete
		 * frame, it will thrown an exception.
		 * 
		 * @param parent
		 *            the parent of the frame
		 * @return the new frame, if it can be created
		 * @since JVerbnet 1.0.0
		 */
		public IFrame create(IVerbClass parent);
	}

}
