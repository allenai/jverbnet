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

import static edu.mit.jverbnet.util.Checks.IsNull;
import static edu.mit.jverbnet.util.Checks.IsNullOrEmpty;
import static edu.mit.jverbnet.util.Checks.NotNull;
import static edu.mit.jverbnet.util.Checks.NotNullEmptyOrBlank;
import static edu.mit.jverbnet.util.Checks.UnmodifiableMaskNullWithEmpty;
import static edu.mit.jverbnet.util.Checks.allElementsAre;
import static edu.mit.jverbnet.util.Checks.thisMethodShouldNeverBeCalled;
import static edu.mit.jverbnet.util.Checks.trimAndMask;

import java.util.LinkedList;
import java.util.List;

import edu.mit.jverbnet.data.semantics.ISemanticDesc;
import edu.mit.jverbnet.data.syntax.ISyntaxDesc;
import edu.mit.jverbnet.data.syntax.SyntaxDesc;

/** 
 * Default implementation of the {@link IFrame} interface.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class Frame implements IFrame {
	
	// unchanging fields
	private final IVerbClass parent;
	private final String descNum;
	private final FrameType firstType;
	private final FrameType secondType;
	private final String xTag;
	private final List<String> examples;
	private final ISyntaxDesc syntaxDesc;
	private final ISemanticDesc semDesc;

	/**
	 * Creates a new frame with the specified parameters
	 * 
	 * @param parent
	 *            the parent of this frame; may not be <code>null</code>
	 * @param descNum
	 *            the description number; may be <code>null</code>
	 * @param firstType
	 *            the primary type; may not be <code>null</code>
	 * @param secondType
	 *            the secondary type; may be null
	 * @param xTag
	 *            the xtag; may be <code>null</code>
	 * @param examples
	 *            the examples for the frame; may be <code>null</code> or empty
	 * @param syntaxDesc
	 *            the syntactic descriptor; may be <code>null</code>
	 * @param semDesc
	 *            the semantic descriptor; may not be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public Frame(IVerbClass parent, String descNum, FrameType firstType, FrameType secondType, String xTag, List<String> examples, ISyntaxDesc syntaxDesc, ISemanticDesc semDesc){
		// check arguments
		NotNull.check("parent", parent);
		descNum = trimAndMask(NotNull, descNum, IsNullOrEmpty, null);
		NotNull.check("firstType", firstType);
		xTag = trimAndMask(NotNull, xTag, IsNullOrEmpty, null);
		examples = allElementsAre(NotNullEmptyOrBlank, "examples", examples, UnmodifiableMaskNullWithEmpty);
		syntaxDesc = IsNull.mask(syntaxDesc, SyntaxDesc.EMPTY_SYNTAX);
		NotNull.check("semDesc", semDesc);
		
		// assign fields
		this.parent = parent;
		this.descNum = descNum;
		this.firstType = firstType;
		this.secondType = secondType;
		this.xTag = xTag;
		this.examples = examples;
		this.syntaxDesc = syntaxDesc;
		this.semDesc = semDesc;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IFrame#getVerbClass()
	 */
	public IVerbClass getVerbClass() {
		return parent;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IFrame#getDescriptionNumber()
	 */
	public String getDescriptionNumber() {
		return descNum;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IFrame#getPrimaryType()
	 */
	public FrameType getPrimaryType() {
		return firstType;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IFrame#getSecondaryType()
	 */
	public FrameType getSecondaryType() {
		return secondType;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IFrame#getXTag()
	 */
	public String getXTag() {
		return xTag;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IFrame#getExamples()
	 */
	public List<String> getExamples() {
		return examples;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IFrame#getSyntax()
	 */
	public ISyntaxDesc getSyntax() {
		return syntaxDesc;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IFrame#getSemantics()
	 */
	public ISemanticDesc getSemantics() {
		return semDesc;
	}
	
	/** 
	 * Default implementation of the frame builder interface.
	 *
	 * @author Mark A. Finlayson
	 * @version 1.2.0
	 * @since JVerbnet 1.1.0
	 */
	public static class FrameBuilder implements IFrameBuilder {
		
		private String descNum;
		private FrameType primaryType;
		private FrameType secondaryType;
		private String xTag;
		private ISyntaxDesc syntaxD;
		private ISemanticDesc semanticD;
		private List<String> examples = new LinkedList<String>();
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame#getVerbClass()
		 */
		public IVerbClass getVerbClass() {
			return thisMethodShouldNeverBeCalled();
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame#getDescriptionNumber()
		 */
		public String getDescriptionNumber() {
			return descNum;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame#getPrimaryType()
		 */
		public FrameType getPrimaryType() {
			return primaryType;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame#getSecondaryType()
		 */
		public FrameType getSecondaryType() {
			return secondaryType;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame#getXTag()
		 */
		public String getXTag() {
			return xTag;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame#getExamples()
		 */
		public List<String> getExamples() {
			return examples;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame#getSyntax()
		 */
		public ISyntaxDesc getSyntax() {
			return syntaxD;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame#getSemantics()
		 */
		public ISemanticDesc getSemantics() {
			return semanticD;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame.IFrameBuilder#setDescriptionNumber(java.lang.String)
		 */
		public void setDescriptionNumber(String descNum) {
			this.descNum = descNum;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame.IFrameBuilder#setPrimaryType(edu.mit.jverbnet.data.FrameType)
		 */
		public void setPrimaryType(FrameType type) {
			this.primaryType = type;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame.IFrameBuilder#setSecondaryType(edu.mit.jverbnet.data.FrameType)
		 */
		public void setSecondaryType(FrameType type) {
			this.secondaryType = type;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame.IFrameBuilder#setXTag(java.lang.String)
		 */
		public void setXTag(String xTag) {
			this.xTag = xTag;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame.IFrameBuilder#setSyntax(edu.mit.jverbnet.data.syntax.ISyntaxDesc)
		 */
		public void setSyntax(ISyntaxDesc syntax) {
			this.syntaxD = syntax;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame.IFrameBuilder#setSemantics(edu.mit.jverbnet.data.semantics.ISemanticDesc)
		 */
		public void setSemantics(ISemanticDesc semantics) {
			this.semanticD = semantics;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IFrame.IFrameBuilder#create(edu.mit.jverbnet.data.IVerbClass)
		 */
		public Frame create(IVerbClass parent) {
			return new Frame(parent, descNum, primaryType, secondaryType, xTag, examples, syntaxD, semanticD);
		}
		
	}

}
