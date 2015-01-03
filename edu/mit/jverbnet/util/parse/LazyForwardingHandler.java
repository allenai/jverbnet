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

import static edu.mit.jverbnet.util.Checks.NotNullEmptyOrBlank;

import java.util.AbstractList;
import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * A handler that does not instantiate its backing handler until the backing
 * handler is actually needed. This class is useful for handling potentially
 * infinitely recursive XML structures.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public abstract class LazyForwardingHandler<T> implements ITaggedHandler<T> {
	
	// unchanging fields
	private final String tag;
	private final ForwardingTaskList blockTasks = new ForwardingTaskList();
	
	// changeable fields
	private boolean clearPending;
	private Locator locator; 
	private XMLReader parser;
	private ContentHandler parent;
	
	// the backing handler
	private ITaggedHandler<T> backingHandler = null;
	
	/**
	 * Creates a new lazy forwarding handler that uses the specified tag.
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
	public LazyForwardingHandler(String tag){
		this(null, null, tag);
	}
	
	/**
	 * Creates a new lazy forwarding handler with the specified parser and tag.
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
	public LazyForwardingHandler(XMLReader parser, String tag){
		this(parser, null, tag);
	}
	
	/**
	 * Creates a new lazy forwarding handler with the specified parent and tag.
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
	public LazyForwardingHandler(IHasParserHandler parent, String tag){
		this((parent == null ? null : parent.getParser()), parent, tag);
	}
	
	/**
	 * Creates a new lazy forwarding handler with the specified parser, parent
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
	public LazyForwardingHandler(XMLReader parser, ContentHandler parent, String tag){
		tag = NotNullEmptyOrBlank.check("tag", tag);
		
		this.parser = parser;
		this.parent = parent;
		this.tag = tag;
	}
	
	/**
	 * Returns the backing handler, instantiating it if necessary. This method
	 * is thread safe.
	 * 
	 * @return the backing handler
	 * @since JVerbnet 1.0.0
	 */
	public final ITaggedHandler<T> getBackingHandler(){
		if(backingHandler == null){
			synchronized(this){
				if(backingHandler == null){
					ITaggedHandler<T> backingHandler = createBackingHandler();
					if(!backingHandler.getTag().equals(getTag()))
						throw new IllegalStateException("The backing handler does not have the same tag as the forwarding handler: forwarding tag='" + getTag() + "', backing tag='" + backingHandler.getTag() + "'");
					backingHandler.setDocumentLocator(locator);
					backingHandler.setParser(parser);
					backingHandler.setParent(parent);
					backingHandler.getTaggedBlockTasks().addAll(blockTasks.getInternalList());
					if(clearPending)
						backingHandler.clear();
					this.backingHandler = backingHandler;
				}
			}

		}
		return backingHandler;
	}

	/**
	 * Subclasses should implement this method to create the backing handler.
	 * The backing handler should have the same tag as the forwarding handler.
	 * 
	 * @return a new instance of the backing handler
	 * @since JVerbnet 1.0.0
	 */
	protected abstract ITaggedHandler<T> createBackingHandler();

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
		if(backingHandler != null)
			backingHandler.setDocumentLocator(locator);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.IHasParserHandler#getParser()
	 */
	public XMLReader getParser() {
		return backingHandler == null ?
				parser :
					backingHandler.getParser();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.IHasParserHandler#setParser(org.xml.sax.XMLReader)
	 */
	public void setParser(XMLReader parser) {
		this.parser = parser;
		if(backingHandler != null)
			backingHandler.setParser(parser);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedHandler#getTag()
	 */
	public String getTag() {
		return (backingHandler == null) ?
				tag :
					backingHandler.getTag();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedHandler#getParent()
	 */
	public ContentHandler getParent() {
		return backingHandler == null ?
				parent :
					backingHandler.getParent();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedHandler#setParent(org.xml.sax.ContentHandler)
	 */
	public void setParent(ContentHandler parent) {
		this.parent = parent;
		if(backingHandler != null)
			backingHandler.setParent(parent);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedHandler#getTaggedBlockTasks()
	 */
	public List<ITaggedBlockTaskHandler> getTaggedBlockTasks() {
		return blockTasks;
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedHandler#clear()
	 */
	public void clear() {
		if(backingHandler == null){
			clearPending = true;
		} else {
			backingHandler.clear();
		}
	}
	
	// ****
	// all methods below here cause the backing handler to be instantiated
	// ****

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedHandler#getElement()
	 */
	public T getElement() {
		return getBackingHandler().getElement();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		getBackingHandler().startDocument();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		getBackingHandler().endDocument();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		getBackingHandler().startPrefixMapping(prefix, uri);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		getBackingHandler().endPrefixMapping(prefix);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		getBackingHandler().startElement(uri, localName, qName, atts);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		getBackingHandler().endElement(uri, localName, qName);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		getBackingHandler().characters(ch, start, length);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		getBackingHandler().ignorableWhitespace(ch, start, length);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	public void processingInstruction(String target, String data) throws SAXException {
		getBackingHandler().processingInstruction(target, data);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(String name) throws SAXException {
		getBackingHandler().skippedEntity(name);
	}

	/**
	 * This task list keeps track of tasks before the backing handler is
	 * instantiated.
	 * 
	 * @author Mark A. Finlayson
	 * @version 1.2.0
	 * @since JVerbnet 1.0.0
	 */
	protected class ForwardingTaskList extends AbstractList<ITaggedBlockTaskHandler> {
		
		protected List<ITaggedBlockTaskHandler> internalList = new LinkedList<ITaggedBlockTaskHandler>();
		
		/**
		 * Returns the current task list for the lazy handler; either the
		 * temporary task list, or, if the backing handler has been
		 * instantiated, the task list of the backing handler.
		 * 
		 * @return the current task list for the lazy handler
		 * @since JVerbnet 1.0.0
		 */
		protected List<ITaggedBlockTaskHandler> getBackingList(){
			return backingHandler == null ?
				internalList :
					backingHandler.getTaggedBlockTasks();
		}
		
		/** 
		 * Returns the internal list for this task list.
		 *
		 * @return the internal list for this task list.
		 * @since JVerbnet 1.0.0
		 */
		protected List<ITaggedBlockTaskHandler> getInternalList(){
			return internalList;
		}
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see java.util.AbstractList#get(int)
		 */
		@Override
		public ITaggedBlockTaskHandler get(int index) {
			return getBackingList().get(index);
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see java.util.AbstractCollection#size()
		 */
		@Override
		public int size() {
			return getBackingList().size();
		}


		/* 
		 * (non-Javadoc) 
		 *
		 * @see java.util.AbstractList#set(int, java.lang.Object)
		 */
		@Override
		public ITaggedBlockTaskHandler set(int index,
				ITaggedBlockTaskHandler element) {
			return getBackingList().set(index, element);
		}


		/* 
		 * (non-Javadoc) 
		 *
		 * @see java.util.AbstractList#add(int, java.lang.Object)
		 */
		@Override
		public void add(int index, ITaggedBlockTaskHandler element) {
			getBackingList().add(index, element);
		}


		/* 
		 * (non-Javadoc) 
		 *
		 * @see java.util.AbstractList#remove(int)
		 */
		@Override
		public ITaggedBlockTaskHandler remove(int index) {
			return getBackingList().remove(index);
		} 
	
	}

}
