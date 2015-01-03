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

import edu.mit.jverbnet.data.ThematicRole.ThematicRoleBuilder;
import edu.mit.jverbnet.data.ThematicRoleType;
import edu.mit.jverbnet.data.selection.ISelRestrictions;
import edu.mit.jverbnet.data.selection.SemRestrType;
import edu.mit.jverbnet.util.parse.IHasParserHandler;
import edu.mit.jverbnet.util.parse.MappedHandler;

/** 
 * Handles Verbnet XML blocks tagged with {@value #XML_TAG_THEMROLE}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class ThematicRoleHandler extends MappedHandler<ThematicRoleBuilder> {
	
	public static final String XML_TAG_THEMROLE = "THEMROLE";
	public static final String XML_TAG_THEMROLES = "THEMROLES";
	
	// assignable fields
	private ThematicRoleType type;
	private ISelRestrictions<SemRestrType> selRestrs;

	/** 
	 * Creates a new ThematicRoleHandler with no parent or parser.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public ThematicRoleHandler() {
		this(null, null);
	}

	/**
	 * Creates a new ThematicRoleHandler with the specified parent.
	 * 
	 * @param parent
	 *            the parent of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public ThematicRoleHandler(IHasParserHandler parent) {
		this((parent == null ? null : parent.getParser()), parent);
	}
	
	/**
	 * Creates a new ThematicRoleHandler with the specified parser.
	 * 
	 * @param parser
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public ThematicRoleHandler(XMLReader parser) {
		this(parser, null);
	}
	
	/**
	 * Creates a new ThematicRoleHandler with the specified parser and parent.
	 * 
	 * @param parser
	 *            the parent of the handler; may be <code>null</code>
	 * @param parent
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public ThematicRoleHandler(XMLReader parser, ContentHandler parent) {
		super(parser, parent, XML_TAG_THEMROLE);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#initHandlerMap(java.util.Map)
	 */
	@Override
	protected void initHandlerMap(Map<String, ContentHandler> map) {
		SelRestrictionsHandler<SemRestrType> selRestrsHandler = new SelRestrictionsHandler<SemRestrType>(this, true, SemRestrType.class){
			@Override
			public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
				if(selRestrs != null)
					throw new IllegalStateException("Selectional restrictions already set for this thematic role block");
				selRestrs = doGetElement();
			}
		};
		map.put(selRestrsHandler.getTag(), selRestrsHandler);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	@Override
	protected void clearLocal() {
		type = null;
		selRestrs = null;
	}

	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#startTaggedBlock(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startTaggedBlock(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		type = ThematicRoleType.getById(attrs.getValue("type"));
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public ThematicRoleBuilder doGetElement() {
		ThematicRoleBuilder result = new ThematicRoleBuilder();
		result.setType(type);
		result.setSelRestrictions(selRestrs);
		return result;
	}

}
