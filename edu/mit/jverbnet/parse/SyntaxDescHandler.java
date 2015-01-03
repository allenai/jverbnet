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

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.mit.jverbnet.data.syntax.ISyntaxArgDesc.ISyntaxArgDescBuilder;
import edu.mit.jverbnet.data.syntax.SyntaxArgType;
import edu.mit.jverbnet.data.syntax.SyntaxDesc;
import edu.mit.jverbnet.util.parse.IHasParserHandler;
import edu.mit.jverbnet.util.parse.MappedHandler;

/** 
 * Handles Verbnet XML blocks tagged with {@value #XML_TAG_SYNTAX}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class SyntaxDescHandler extends MappedHandler<SyntaxDesc> {
	
	public static final String XML_TAG_SYNTAX = "SYNTAX";
	public static final String XML_TAG_VERB = "VERB";
	
	// local fields
	private boolean isAfterVerb;
	private List<ISyntaxArgDescBuilder> preArgs;
	private List<ISyntaxArgDescBuilder> postArgs;
	
	/** 
	 * Creates a new SyntaxDescHandler with no parent or parser.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public SyntaxDescHandler() {
		this(null, null);
	}

	/**
	 * Creates a new SyntaxDescHandler with the specified parent.
	 * 
	 * @param parent
	 *            the parent of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public SyntaxDescHandler(IHasParserHandler parent) {
		this((parent == null ? null : parent.getParser()), parent);
	}
	
	/**
	 * Creates a new SyntaxDescHandler with the specified parser.
	 * 
	 * @param parser
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public SyntaxDescHandler(XMLReader parser) {
		this(parser, null);
	}
	
	/**
	 * Creates a new SyntaxDescHandler with the specified parser and parent.
	 * 
	 * @param parser
	 *            the parent of the handler; may be <code>null</code>
	 * @param parent
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public SyntaxDescHandler(XMLReader parser, ContentHandler parent) {
		super(parser, parent, XML_TAG_SYNTAX);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#initHandlerMap(java.util.Map)
	 */
	@Override
	protected void initHandlerMap(Map<String, ContentHandler> map) {
		
		// arguments
		SyntaxArgDescHandler argHandler;
		for(SyntaxArgType t : SyntaxArgType.values()){
			argHandler = new SyntaxArgDescHandler(this, t){
				@Override
				public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
					ISyntaxArgDescBuilder e = doGetElement();
					if(isAfterVerb){
						postArgs.add(e);
					} else {
						preArgs.add(e);
					}
				}
			};
			map.put(argHandler.getTag(), argHandler);
		}

		// verb
		MappedHandler<Object> verbHandler = new MappedHandler<Object>(this, XML_TAG_VERB) {
			@Override
			public void startTaggedBlock(String uri, String localName, String qName, Attributes attrs) throws SAXException {
				if(isAfterVerb)
					throw new IllegalStateException("Duplicate instance of " + XML_TAG_VERB + " tag");
				isAfterVerb = true;
			}
		};
		map.put(verbHandler.getTag(), verbHandler);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	@Override
	protected void clearLocal() {
		isAfterVerb = false;
		preArgs = new LinkedList<ISyntaxArgDescBuilder>();
		postArgs = new LinkedList<ISyntaxArgDescBuilder>();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public SyntaxDesc doGetElement() {
		return new SyntaxDesc(preArgs, postArgs);
	}

}
