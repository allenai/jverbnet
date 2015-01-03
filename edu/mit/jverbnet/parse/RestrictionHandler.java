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

import java.util.Collections;
import java.util.Map.Entry;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.mit.jverbnet.data.IVerbnetType;
import edu.mit.jverbnet.data.VerbnetTypes;
import edu.mit.jverbnet.util.parse.IHasParserHandler;
import edu.mit.jverbnet.util.parse.MappedHandler;

/** 
 * Handles Verbnet XML blocks tagged with {@value #XML_TAG_SELRESTR} or {@value #XML_TAG_SYNRESTR}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class RestrictionHandler<T extends IVerbnetType> extends MappedHandler<Entry<T, Boolean>> {
	
	public static final String XML_TAG_SELRESTR = "SELRESTR";
	public static final String XML_TAG_SYNRESTR = "SYNRESTR";
	
	// unchanging fields
	private final Class<T> verbnetTypeClass;
	
	// assignable fields
	private T type;
	private Boolean value;
	
	/** 
	 * Creates a new RestrictionHandler with no parent or parser.
	 * 
	 * @param isSelRestr
	 *            whether this is a semantic restriction (<code>true</code>) or
	 *            syntactic restriction (<code>false</code>).
	 * @param verbnetTypeClass
	 *            the verbnet type used in this restriction
	 * @since JVerbnet 1.0.0
	 */
	public RestrictionHandler(boolean isSelRestr, Class<T> verbnetTypeClass) {
		this(null, null, isSelRestr, verbnetTypeClass);
	}

	/**
	 * Creates a new RestrictionHandler with the specified parent.
	 * 
	 * @param parent
	 *            the parent of the handler; may be <code>null</code>
	 * @param isSelRestr
	 *            whether this is a semantic restriction (<code>true</code>) or
	 *            syntactic restriction (<code>false</code>).
	 * @param verbnetTypeClass
	 *            the verbnet type used in this restriction
	 * @since JVerbnet 1.0.0
	 */
	public RestrictionHandler(IHasParserHandler parent, boolean isSelRestr, Class<T> verbnetTypeClass) {
		this((parent == null ? null : parent.getParser()), parent, isSelRestr, verbnetTypeClass);
	}
	
	/**
	 * Creates a new RestrictionHandler with the specified parser.
	 * 
	 * @param parser
	 *            the parser of the handler; may be <code>null</code>
	 * @param isSelRestr
	 *            whether this is a semantic restriction (<code>true</code>) or
	 *            syntactic restriction (<code>false</code>).
	 * @param verbnetTypeClass
	 *            the verbnet type used in this restriction
	 * @since JVerbnet 1.0.0
	 */
	public RestrictionHandler(XMLReader parser, boolean isSelRestr, Class<T> verbnetTypeClass) {
		this(parser, null, isSelRestr, verbnetTypeClass);
	}
	
	/**
	 * Creates a new RestrictionHandler with the specified parser and parent.
	 * 
	 * @param parser
	 *            the parent of the handler; may be <code>null</code>
	 * @param parent
	 *            the parser of the handler; may be <code>null</code>
	 * @param isSelRestr
	 *            whether this is a semantic restriction (<code>true</code>) or
	 *            syntactic restriction (<code>false</code>).
	 * @param verbnetTypeClass
	 *            the verbnet type used in this restriction
	 * @since JVerbnet 1.0.0
	 */
	public RestrictionHandler(XMLReader parser, ContentHandler parent, boolean isSelRestr, Class<T> verbnetTypeClass) {
		super(parser, parent, isSelRestr ? 
				XML_TAG_SELRESTR : 
					XML_TAG_SYNRESTR);
		NotNull.check("verbnetTypeClass", verbnetTypeClass);
		this.verbnetTypeClass = verbnetTypeClass;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	@Override
	protected void clearLocal() {
		type = null;
		value = null;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#startTaggedBlock(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startTaggedBlock(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		String typeStr = attrs.getValue("type");
		type = VerbnetTypes.getById(verbnetTypeClass, typeStr);
		value = VerbnetTypes.getSelectionRestriction(attrs.getValue("Value"));
		if(type == null)
			throw new NullPointerException("Unknown value '" + typeStr + "' for type " + verbnetTypeClass.getSimpleName());
		NotNull.check("value", value);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public Entry<T, Boolean> doGetElement() {
		return Collections.singletonMap(type, value).entrySet().iterator().next();
	}

}
