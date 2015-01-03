/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.parse;

import static edu.mit.jverbnet.util.Checks.NotNull;
import static edu.mit.jverbnet.util.Checks.NotNullEmptyOrBlank;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.mit.jverbnet.data.VerbnetTypes;
import edu.mit.jverbnet.data.semantics.ArgType;
import edu.mit.jverbnet.data.semantics.ISemanticArgType;
import edu.mit.jverbnet.util.parse.IHasParserHandler;
import edu.mit.jverbnet.util.parse.MappedHandler;

/** 
 * Handles Verbnet XML blocks tagged with {@value #XML_TAG_ARG}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class ArgHandler extends MappedHandler<ISemanticArgType> {
	
	public static final String XML_TAG_ARGS = "ARGS";
	public static final String XML_TAG_ARG = "ARG";
	
	// parsing fields
	private ISemanticArgType semArgType = null;
	
	/** 
	 * Creates a new ArgHandler with no parent or parser.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public ArgHandler() {
		this(null, null);
	}

	/**
	 * Creates a new ArgHandler with the specified parent.
	 * 
	 * @param parent
	 *            the parent of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public ArgHandler(IHasParserHandler parent) {
		this((parent == null ? null : parent.getParser()), parent);
	}
	
	/**
	 * Creates a new ArgHandler with the specified parser.
	 * 
	 * @param parser
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public ArgHandler(XMLReader parser) {
		this(parser, null);
	}
	
	/**
	 * Creates a new ArgHandler with the specified parser and parent.
	 * 
	 * @param parser
	 *            the parent of the handler; may be <code>null</code>
	 * @param parent
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public ArgHandler(XMLReader parser, ContentHandler parent) {
		super(parser, parent, XML_TAG_ARG);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	protected void clearLocal() {
		semArgType = null;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#startTaggedBlock(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startTaggedBlock(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		String argTypeStr = attrs.getValue("type");
		ArgType argType = ArgType.getById(argTypeStr);
		String subclassId = attrs.getValue("value");
		
		NotNull.check("argType", argType);
		NotNullEmptyOrBlank.check("subclassId", subclassId);
		if(argType == null)
			throw new NullPointerException("Unknown value '" + argTypeStr + "' for " + ArgType.class.getSimpleName());
		
		semArgType = VerbnetTypes.getById(argType.getSubclassType(), subclassId);
		if(semArgType == null)
			throw new NullPointerException("Unknown value '" + subclassId + "' for " + argType.getSubclassType().getSimpleName());
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public ISemanticArgType doGetElement() {
		return semArgType;
	}

}
