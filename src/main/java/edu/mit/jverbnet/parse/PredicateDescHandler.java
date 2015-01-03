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

import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.mit.jverbnet.data.semantics.ISemanticArgType;
import edu.mit.jverbnet.data.semantics.PredicateDesc;
import edu.mit.jverbnet.data.semantics.PredicateType;
import edu.mit.jverbnet.util.parse.IHasParserHandler;
import edu.mit.jverbnet.util.parse.ListHandler;
import edu.mit.jverbnet.util.parse.MappedHandler;

/** 
 * Handles Verbnet XML blocks tagged with {@value #XML_TAG_PRED}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class PredicateDescHandler extends MappedHandler<PredicateDesc> {

	public static final String XML_TAG_PRED = "PRED";
	public static final String BOOLEAN_VALUE_NEGATIVE = "!";
	
	// model fields
	private PredicateType predType;
	private boolean bool;
	private MappedHandler<List<ISemanticArgType>> argListHandler; 

	/** 
	 * Creates a new PredicateDescHandler with no parent or parser.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public PredicateDescHandler() {
		this(null, null);
	}

	/**
	 * Creates a new PredicateDescHandler with the specified parent.
	 * 
	 * @param parent
	 *            the parent of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public PredicateDescHandler(IHasParserHandler parent) {
		this((parent == null ? null : parent.getParser()), parent);
	}
	
	/**
	 * Creates a new PredicateDescHandler with the specified parser.
	 * 
	 * @param parser
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public PredicateDescHandler(XMLReader parser) {
		this(parser, null);
	}
	
	/**
	 * Creates a new PredicateDescHandler with the specified parser and parent.
	 * 
	 * @param parser
	 *            the parent of the handler; may be <code>null</code>
	 * @param parent
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public PredicateDescHandler(XMLReader parser, ContentHandler parent) {
		super(parser, parent, XML_TAG_PRED);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#initHandlerMap(java.util.Map)
	 */
	@Override
	protected void initHandlerMap(Map<String, ContentHandler> map) {
		argListHandler = new ListHandler<ISemanticArgType>(this, ArgHandler.XML_TAG_ARGS, new ArgHandler());
		map.put(argListHandler.getTag(), argListHandler);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	@Override
	protected void clearLocal() {
		predType = null;
		bool = true;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#startTaggedBlock(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startTaggedBlock(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		predType = PredicateType.getById(attrs.getValue("value"));
		bool = !BOOLEAN_VALUE_NEGATIVE.equals(attrs.getValue("bool"));
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public PredicateDesc doGetElement() {
		return new PredicateDesc(predType, bool, argListHandler.getElement());
	}

}
