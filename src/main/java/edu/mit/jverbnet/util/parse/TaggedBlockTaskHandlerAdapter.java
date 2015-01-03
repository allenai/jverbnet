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
 * A default implementation of the {@link ITaggedBlockTaskHandler} interface, that does nothing.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class TaggedBlockTaskHandlerAdapter implements ITaggedBlockTaskHandler {
	
	// singleton instance
	private static TaggedBlockTaskHandlerAdapter instance;
	
	/**
	 * Returns the singleton instance of this object, creating it if necessary.
	 * 
	 * @return the singleton instance
	 * @since JVerbnet 1.0.0
	 */
	public static TaggedBlockTaskHandlerAdapter getInstance(){
		if(instance == null)
			synchronized(NullErrorHandler.class){
				if(instance == null)
					instance = new TaggedBlockTaskHandlerAdapter();
			}
		return instance;
	}
	
	/**
	 * The constructor is marked protected so that this class may be subclassed,
	 * but not directly instantiated.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	protected TaggedBlockTaskHandlerAdapter(){}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedBlockTaskHandler#startTaggedBlock(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startTaggedBlock(String uri, String localName, String qName,
			Attributes attrs) throws SAXException {
		// do nothing
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedBlockTaskHandler#endTaggedBlock(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endTaggedBlock(String uri, String localName, String qName)
			throws SAXException {
		// do nothing
	}

}
