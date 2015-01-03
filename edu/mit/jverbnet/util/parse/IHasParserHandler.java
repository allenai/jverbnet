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
import org.xml.sax.XMLReader;

/**
 * A content handler that has a parser assigned to it.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface IHasParserHandler extends ContentHandler {

	/**
	 * Returns the parser for this handler. May return <code>null</code>.
	 * 
	 * @return the possibly <code>null</code> parser for this handler
	 * @since JVerbnet 1.0.0
	 */
	public XMLReader getParser();

	/**
	 * Sets the parser for this handler; may be set to <code>null</code>
	 * 
	 * @param parser
	 *            the new parser for this handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public void setParser(XMLReader parser);

}
