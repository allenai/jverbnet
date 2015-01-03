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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.mit.jverbnet.data.semantics.IPredicateDesc;
import edu.mit.jverbnet.data.semantics.SemanticDesc;
import edu.mit.jverbnet.util.parse.IHasParserHandler;
import edu.mit.jverbnet.util.parse.MappedHandler;

/** 
 *  Handles Verbnet XML blocks tagged with {@value #XML_TAG_SEMANTICS}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class SemanticDescHandler extends MappedHandler<SemanticDesc> {
	
	public static final String XML_TAG_SEMANTICS = "SEMANTICS";
	
	// instance fields
	private List<IPredicateDesc> predList;

	/** 
	 * Creates a new SemanticDescHandler with no parent or parser.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public SemanticDescHandler() {
		this(null, null);
	}

	/**
	 * Creates a new SemanticDescHandler with the specified parent.
	 * 
	 * @param parent
	 *            the parent of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public SemanticDescHandler(IHasParserHandler parent) {
		this((parent == null ? null : parent.getParser()), parent);
	}
	
	/**
	 * Creates a new SemanticDescHandler with the specified parser.
	 * 
	 * @param parser
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public SemanticDescHandler(XMLReader parser) {
		this(parser, null);
	}
	
	/**
	 * Creates a new SemanticDescHandler with the specified parser and parent.
	 * 
	 * @param parser
	 *            the parent of the handler; may be <code>null</code>
	 * @param parent
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public SemanticDescHandler(XMLReader parser, ContentHandler parent) {
		super(parser, parent, XML_TAG_SEMANTICS);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#initHandlerMap(java.util.Map)
	 */
	@Override
	protected void initHandlerMap(Map<String, ContentHandler> map) {
		PredicateDescHandler predHandler = new PredicateDescHandler(this){
			@Override
			public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
				predList.add(getElement());
			}
		};
		map.put(predHandler.getTag(), predHandler);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	@Override
	protected void clearLocal() {
		predList = new LinkedList<IPredicateDesc>();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public SemanticDesc doGetElement() {
		return new SemanticDesc(predList);
	}

}
