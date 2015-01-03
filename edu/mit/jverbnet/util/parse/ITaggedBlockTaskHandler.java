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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/** 
 * A task to be run when entering and exiting a tagged block.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface ITaggedBlockTaskHandler {
	
	/**
	 * This method is used to run code when entering a tagged block.
	 * 
	 * @param uri
	 *            the Namespace URI, or the empty string if the element has no
	 *            Namespace URI or if Namespace processing is not being
	 *            performed
	 * @param localName
	 *            the local name (without prefix), or the empty string if
	 *            Namespace processing is not being performed
	 * @param qName
	 *            the qualified name (with prefix), or the empty string if
	 *            qualified names are not available
	 * @param attrs
	 *            the attributes attached to the element. If there are no
	 *            attributes, it shall be an empty Attributes object. The value
	 *            of this object after startElement returns is undefined
	 * @throws SAXException
	 *             any SAX exception, possibly wrapping another exception
	 * @since JVerbnet 1.0.0
	 */
	public void startTaggedBlock(String uri, String localName, String qName, Attributes attrs) throws SAXException;
	
	/**
	 * This method is used to run when exiting a tagged block.
	 * 
	 * @param uri
	 *            the Namespace URI, or the empty string if the element has no
	 *            Namespace URI or if Namespace processing is not being
	 *            performed
	 * @param localName
	 *            the local name (without prefix), or the empty string if
	 *            Namespace processing is not being performed
	 * @param qName
	 *            the qualified XML name (with prefix), or the empty string if
	 *            qualified names are not available
	 * @throws SAXException
	 *             any SAX exception, possibly wrapping another exception
	 * @since JVerbnet 1.0.0
	 */
	public void endTaggedBlock(String uri, String localName, String qName) throws SAXException;

}
