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

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.mit.jverbnet.data.VerbnetTypes;
import edu.mit.jverbnet.data.selection.IRestrType;
import edu.mit.jverbnet.data.selection.ISelRestrictions;
import edu.mit.jverbnet.data.selection.PrepRestrType;
import edu.mit.jverbnet.data.selection.SemRestrType;
import edu.mit.jverbnet.data.selection.SynRestrType;
import edu.mit.jverbnet.data.syntax.INounPhraseType;
import edu.mit.jverbnet.data.syntax.SyntaxArgDesc.SyntaxArgDescBuilder;
import edu.mit.jverbnet.data.syntax.SyntaxArgType;
import edu.mit.jverbnet.util.parse.IHasParserHandler;
import edu.mit.jverbnet.util.parse.MappedHandler;

/** 
 * Handles Verbnet XML blocks tagged with the id values of {@link SyntaxArgType}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class SyntaxArgDescHandler extends MappedHandler<SyntaxArgDescBuilder> {
	
	// unchangeable fields
	private final SyntaxArgType type;
	
	// local fields
	private String value;
	private INounPhraseType npType;
	private ISelRestrictions<? extends IRestrType> selRestrs;
	
	/**
	 * Creates a new SyntaxArgDescHandler with no parent or parser.
	 * 
	 * @param type
	 *            the syntactic argument type to be handled
	 * @throws NullPointerException
	 *             if the argument type is <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public SyntaxArgDescHandler(SyntaxArgType type) {
		this(null, null, type);
	}

	/**
	 * Creates a new SyntaxArgDescHandler with the specified parent.
	 * 
	 * @param parent
	 *            the parent of the handler; may be <code>null</code>
	 * @param type
	 *            the syntactic argument type to be handled
	 * @throws NullPointerException
	 *             if the argument type is <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public SyntaxArgDescHandler(IHasParserHandler parent, SyntaxArgType type) {
		this((parent == null ? null : parent.getParser()), parent, type);
	}
	
	/**
	 * Creates a new SyntaxArgDescHandler with the specified parser.
	 * 
	 * @param parser
	 *            the parser of the handler; may be <code>null</code>
	 * @param type
	 *            the syntactic argument type to be handled
	 * @throws NullPointerException
	 *             if the argument type is <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public SyntaxArgDescHandler(XMLReader parser, SyntaxArgType type) {
		this(parser, null, type);
	}
	
	/**
	 * Creates a new SyntaxArgDescHandler with the specified parser and parent.
	 * 
	 * @param parser
	 *            the parent of the handler; may be <code>null</code>
	 * @param parent
	 *            the parser of the handler; may be <code>null</code>
	 * @param type
	 *            the syntactic argument type to be handled
	 * @throws NullPointerException
	 *             if the argument type is <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public SyntaxArgDescHandler(XMLReader parser, ContentHandler parent, SyntaxArgType type) {
		super(parser, parent, type.getID());
		this.type = type;  
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#initHandlerMap(java.util.Map)
	 */
	@Override
	protected void initHandlerMap(Map<String, ContentHandler> map) {
		if(type == SyntaxArgType.NP){
			
			// handles the SELRESTRS tag
			SelRestrictionsHandler<SemRestrType> selRestrHandler = new SelRestrictionsHandler<SemRestrType>(this, true, SemRestrType.class){
				@Override
				public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
					if(selRestrs != null)
						throw new IllegalArgumentException("The selection restrictions have already been specified for this syntax argument");
					selRestrs = doGetElement();
				}
			};
			// handles the SYNRESTRS tag
			SelRestrictionsHandler<SynRestrType> semRestrHandler = new SelRestrictionsHandler<SynRestrType>(this, false, SynRestrType.class){
				@Override
				public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
					if(selRestrs != null)
						throw new IllegalArgumentException("The selection restrictions have already been specified for this syntax argument");
					selRestrs = doGetElement();
				}
			};

			// map the tags
			map.put(selRestrHandler.getTag(), selRestrHandler);
			map.put(semRestrHandler.getTag(), semRestrHandler);
			
		}
		
		if(type == SyntaxArgType.PREP){
			// handles the SEMRESTRS tag
			SelRestrictionsHandler<PrepRestrType> prepRestrHandler = new SelRestrictionsHandler<PrepRestrType>(this, true, PrepRestrType.class){
				@Override
				public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
					if(selRestrs != null)
						throw new IllegalArgumentException("The selection restrictions have already been specified for this syntax argument");
					selRestrs = doGetElement();
				}
			};
			map.put(prepRestrHandler.getTag(), prepRestrHandler);
		}
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	@Override
	protected void clearLocal() {
		value = null;
		npType = null;
		selRestrs = null;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#startTaggedBlock(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startTaggedBlock(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		String value = attrs.getValue("value");
		value = type.getValueRule().checkValue(value);
		if(type == SyntaxArgType.NP)
			npType = VerbnetTypes.getNounPhraseTypeById(value);
		this.value = value;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public SyntaxArgDescBuilder doGetElement() {
		return new SyntaxArgDescBuilder(type, value, npType, selRestrs);
	}

}
