/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.util.parse;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/** 
 * A handler that transforms CDATA into a String.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class CDataHandler extends MappedHandler<String> {
	
	private StringBuilder sb;
	
	/**
	 * Creates a new CDATA handler with the specified tag.
	 * 
	 * @param tag
	 *            the tag for the block that this handler handles; may not be
	 *            <code>null</code>, empty, or all whitespace.
	 * @throws NullPointerException
	 *             if the specified tag is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the specified tag is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public CDataHandler(String tag) {
		super(tag);
	}

	/**
	 * Creates a new CDATA handler with the specified parser, parent
	 * and tag.
	 * 
	 * @param parser
	 *            the parser for this handler; may be <code>null</code>
	 * @param tag
	 *            the tag for the block that this handler handles; may not be
	 *            <code>null</code>, empty, or all whitespace.
	 * @throws NullPointerException
	 *             if the specified tag is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the specified tag is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public CDataHandler(XMLReader parser, String tag) {
		super(parser, tag);
	}

	/**
	 * Creates a new CDATA handler with the specified parser, parent
	 * and tag.
	 * 
	 * @param parent
	 *            this handler's parent; may be <code>null</code>
	 * @param tag
	 *            the tag for the block that this handler handles; may not be
	 *            <code>null</code>, empty, or all whitespace.
	 * @throws NullPointerException
	 *             if the specified tag is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the specified tag is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public CDataHandler(IHasParserHandler parent, String tag) {
		super(parent, tag);
	}

	/**
	 * Creates a new CDATA handler with the specified parser, parent
	 * and tag.
	 * 
	 * @param parser
	 *            the parser for this handler; may be <code>null</code>
	 * @param parent
	 *            this handler's parent; may be <code>null</code>
	 * @param tag
	 *            the tag for the block that this handler handles; may not be
	 *            <code>null</code>, empty, or all whitespace.
	 * @throws NullPointerException
	 *             if the specified tag is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the specified tag is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public CDataHandler(XMLReader parser, ContentHandler parent, String tag) {
		super(parser, parent, tag);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	@Override
	protected void clearLocal() {
		sb = new StringBuilder();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
		// ignore characters outside of the block
		if(!isInsideTaggedBlock())
			return;
		// ignore characters not at the level of the tagged block
		if(tagStack.size() != 1)
			return;
		sb.append(ch, start, length);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public String doGetElement() {
		return sb.toString();
	}

}
