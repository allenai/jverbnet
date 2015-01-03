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

import static edu.mit.jverbnet.util.Checks.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/** 
 * A mapped handler that specifically handles lists of elements.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class ListHandler<T> extends MappedHandler<List<T>> {
	
	// the element handler
	private final ITaggedHandler<? extends T> listElementHandler;

	// this contains the elements parsed from the SAX stream
	private List<T> elements = new LinkedList<T>();
	private boolean allowNullElements = false;
	
	/**
	 * Creates a new CDATA handler with the specified tag.
	 * 
	 * @param tag
	 *            the tag for the block that this handler handles; may not be
	 *            <code>null</code>, empty, or all whitespace.
	 * @param elementHandler
	 *            the handler for the elements of the list
	 * @throws NullPointerException
	 *             if the specified tag is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the specified tag is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public ListHandler(String tag, ITaggedHandler<? extends T> elementHandler) {
		this(null, null, tag, elementHandler);
	}

	/**
	 * Creates a new CDATA handler with the specified parser and tag.
	 * 
	 * @param parser
	 *            the parser for this handler; may be <code>null</code>
	 * @param tag
	 *            the tag for the block that this handler handles; may not be
	 *            <code>null</code>, empty, or all whitespace.
	 * @param elementHandler
	 *            the handler for the elements of the list
	 * @throws NullPointerException
	 *             if the specified tag is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the specified tag is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public ListHandler(XMLReader parser, String tag, ITaggedHandler<? extends T> elementHandler) {
		this(parser, null, tag, elementHandler);
	}

	/**
	 * Creates a new CDATA handler with the specified parent and tag.
	 * 
	 * @param parent
	 *            the parent for this handler; may be <code>null</code>
	 * @param tag
	 *            the tag for the block that this handler handles; may not be
	 *            <code>null</code>, empty, or all whitespace.
	 * @param elementHandler
	 *            the handler for the elements of the list
	 * @throws NullPointerException
	 *             if the specified tag is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the specified tag is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public ListHandler(IHasParserHandler parent, String tag, ITaggedHandler<? extends T> elementHandler) {
		this(parent.getParser(), parent, tag, elementHandler);
	}

	/**
	 * Creates a new CDATA handler with the specified parser, parent and tag.
	 * 
	 * @param parser
	 *            the parser for this handler; may be <code>null</code>
	 * @param parent
	 *            this handler's parent; may be <code>null</code>
	 * @param tag
	 *            the tag for the block that this handler handles; may not be
	 *            <code>null</code>, empty, or all whitespace.
	 * @param elementHandler
	 *            the handler for the elements of the list
	 * @throws NullPointerException
	 *             if the specified tag is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the specified tag is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public ListHandler(XMLReader parser, ContentHandler parent, String tag, ITaggedHandler<? extends T> elementHandler) {
		super(parser, parent, tag);
		NotNull.check("elementHandler", elementHandler);
		
		// add tagged block end task 
		elementHandler.setParent(this);
		elementHandler.getTaggedBlockTasks().add(new TaggedBlockTaskHandlerAdapter(){
			@Override
			public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
				T elem = listElementHandler.getElement();
				if(elem == null && !allowNullElements)
					throw new NullPointerException("elements may not be null");
				elements.add(elem);
			}});
		
		this.listElementHandler = elementHandler;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#initHandlerMap(java.util.Map)
	 */
	protected void initHandlerMap(Map<String, ContentHandler> map) {
		map.put(listElementHandler.getTag(), listElementHandler);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	public void clearLocal() {
		elements = new LinkedList<T>();
	}
	
	/**
	 * Sets whether this handler allows <code>null</code> elements in its list,
	 * or whether it will throw an exception when <code>null</code> elements are
	 * encountered.
	 * 
	 * @param value
	 *            <code>true</code> if null elements are allowed;
	 *            <code>false</code> otherwise
	 * @since JVerbnet 1.0.0
	 */
	public void setAllowNullElements(boolean value){
		this.allowNullElements = value;
	}
	
	/**
	 * Returns the status of the allowNullElements flag: <code>true</code> if
	 * <code>null</code> elements are currently allowed; false otherwise.
	 * 
	 * @return <code>true</code> if <code>null</code> elements are allowed;
	 *         <code>false</code> otherwise
	 * @since JVerbnet 1.0.0
	 */
	public boolean allowsNullElements(){
		return allowNullElements;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public List<T> doGetElement() {
		return elements;
	}
	
}
