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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** 
 * An error handler that does nothing.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class NullErrorHandler implements ErrorHandler {
	
	// singleton instance
	private static NullErrorHandler instance;
	
	/**
	 * Returns the singleton instance of this object, creating it if necessary.
	 * 
	 * @return the singleton instance
	 * @since JVerbnet 1.0.0
	 */
	public static NullErrorHandler getInstance(){
		if(instance == null)
			synchronized(NullErrorHandler.class){
				if(instance == null)
					instance = new NullErrorHandler();
			}
		return instance;
	}
	
	/**
	 * The constructor is marked protected so that this class may be subclassed,
	 * but not directly instantiated.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	protected NullErrorHandler(){}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	public void warning(SAXParseException exception) throws SAXException {
		// do nothing
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	public void error(SAXParseException exception) throws SAXException {
		// do nothing
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	public void fatalError(SAXParseException exception) throws SAXException {
		// do nothing
	}

}
