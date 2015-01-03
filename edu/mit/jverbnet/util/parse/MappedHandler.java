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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import edu.mit.jverbnet.util.Checks;

/**
 * A handler which processes tags internal to its tagged block by assigning
 * handlers to qNames.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class MappedHandler<T> extends DefaultHandler implements ITaggedHandler<T>, ITaggedBlockTaskHandler {
	
	// notifications
	public static final String messageParserNotSet = "The parser for this handler is not set";
	public static final String messageIgnoringElementBlockExternal = "Ignoring element outside of TAG tagged block: ";
	public static final String messageUnmappedElementBlockInternal = "Found unmapped element inside TAG tagged block: ";
	
	// notification-creation objects
	protected static final Matcher messageMatcherIgnoreExternal = Pattern.compile("TAG").matcher(messageIgnoringElementBlockExternal);
	protected static final Matcher messageMatcherIgnoreInternal = Pattern.compile("TAG").matcher(messageUnmappedElementBlockInternal);

	// the tag that this handler handles
	private final String tag;
	// tagged block tasks
	private final List<ITaggedBlockTaskHandler> taggedBlockTasks = new LinkedList<ITaggedBlockTaskHandler>();

	 // The xml reader being used by this handler.
	private XMLReader parser = null;
	// the parent of this handler, if any
	private ContentHandler parent = null;
	// the parent of this handler, if any, cast as a IHasParserHandler, if possible
	private IHasParserHandler parentHasP = null;
	// null means the list hasn't been initialized
	private Map<String, ContentHandler> elementMap = null;
	// flag for whether this handler should report as warnings unmapped elements
	private boolean reportWarnings = true;
	// xml locator
	private Locator locator;
	
	/**
	 * Creates a new mapped handler which is assigned to the specified tag.
	 * The parser and parent for this handler are set to <code>null</code>.
	 * 
	 * @param tag
	 *            the tag for this handler; may not be <code>null</code>, empty,
	 *            or all whitespace; leading and trailing whitespace will be
	 *            trimmed
	 * @throws NullPointerException
	 *             if the specified tag is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the tag is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public MappedHandler(String tag) {
		this(null, null, tag);
	}

	/**
	 * Creates a new mapped handler which uses the specified parser and is
	 * assigned to the specified tag. The parent of this handler is set to
	 * <code>null</code>.
	 * 
	 * @param parser
	 *            the parser for this handler; may not be <code>null</code>
	 * @param tag
	 *            the tag for this handler; may not be <code>null</code>, empty,
	 *            or all whitespace; leading and trailing whitespace will be
	 *            trimmed
	 * @throws NullPointerException
	 *             if the specified parser or tag is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the tag is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public MappedHandler(XMLReader parser, String tag) {
		this(parser, null, tag);
	}
	
	/**
	 * Convenience constructor. Constructs a new handler with the specified
	 * parent, and the same parser as the parent.
	 * 
	 * @param parent
	 *            the parent of this handler, which has a parser
	 * @param tag
	 *            the xml tag which this handler handles; may not be
	 *            <code>null</code>, empty, or all whitespace
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the tag is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public MappedHandler(IHasParserHandler parent, String tag){
		this((parent == null ? null : parent.getParser()), parent, tag);
	}
	
	/**
	 * Full control constructor. Constructs a new handler with the specified
	 * parser, parent, and tag.
	 * 
	 * @param parser
	 *            the parser for this handler
	 * @param parent
	 *            the parent of this handler, which has a parser
	 * @param tag
	 *            the xml tag which this handler handles; may not be
	 *            <code>null</code>, empty, or all whitespace; the tag will be
	 *            trimmed of extra whitespace.
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the tag is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public MappedHandler(XMLReader parser, ContentHandler parent, String tag){
		// check arguments
		tag = NotNullEmptyOrBlank.check("tag", tag);

		// assign fields
		this.parent = parent;
		if(parent instanceof IHasParserHandler)
			this.parentHasP = (IHasParserHandler)parent;
		this.parser = parser;
		this.tag = tag;
		
		// make sure initial state is clear
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.IHasParserHandler#getParser()
	 */
	public XMLReader getParser(){
		return (parentHasP != null) ? 
				parentHasP.getParser() :
					parser;
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.IHasParserHandler#setParser(org.xml.sax.XMLReader)
	 */
	public void setParser(XMLReader parser) {
		this.parser = parser;
	}

	/**
	 * Returns the parent of this child handler. If the 
	 * 
	 * @return the non-<code>null</code> parent of this child handler
	 * @since JVerbnet 1.0.0
	 */
	public ContentHandler getParent(){
		return parent;
	}
	
	/**
	 * Throws an exception if the parser is not set.
	 * 
	 * @throws IllegalStateException
	 *             if this method is called and the parser is not set
	 * @since JVerbnet 1.0.0
	 */
	protected void checkParserSet(){
		if(getParser() == null)
			throw new IllegalStateException(messageParserNotSet);
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedHandler#setParent(org.xml.sax.ContentHandler)
	 */
	public void setParent(ContentHandler parent) {
		this.parent = parent;
		this.parentHasP = (parent instanceof IHasParserHandler) ? (IHasParserHandler)parent : null;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedHandler#getTag()
	 */
	public String getTag(){
		return tag;
	}
	
	/**
	 * Returns <code>true</code> if the handler is currently reporting unmapped
	 * tags to the error handler (or to standard error, if no error handler is
	 * available).
	 * 
	 * @return <code>true</code> if unmapped tags will be reported to the error
	 *         stream; <code>false</code> otherwise
	 * @since JVerbnet 1.0.0
	 */
	public boolean reportWarnings(){
		return reportWarnings;
	}
	
	/**
	 * Sets whether the handler reports unampped tags to the error stream.
	 * 
	 * @param value
	 *            <code>true</code> if the handler should report unmapped tags;
	 *            <code>false</code> otherwise
	 * @since JVerbnet 1.0.0
	 */
	public void setReportWarnings(boolean value){
		reportWarnings = value;
		for(ContentHandler h : getHandlerMappings().values())
			if(h instanceof MappedHandler)
				((MappedHandler<?>)h).setReportWarnings(value);
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see org.xml.sax.helpers.DefaultHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	/**
	 * Returns the document locator for this handler; may be <code>null</code>
	 * 
	 * @return the possibly <code>null</code> document locator for this handler
	 * @since JVerbnet 1.0.0
	 */
	public Locator getDocumentLocator(){
		return locator;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedHandler#getTaggedBlockTasks()
	 */
	public List<ITaggedBlockTaskHandler> getTaggedBlockTasks() {
		return taggedBlockTasks;
	}

	/**
	 * Returns the set of tag to handler mappings for this mapped handler. This
	 * method is called a lot, so it should probably just return a pointer to a
	 * field that is constructed elsewhere (e.g., in the object constructor).
	 * 
	 * @return the tag-to-handler map
	 * @since JVerbnet 1.0.0
	 */
	public final Map<String, ContentHandler> getHandlerMappings() {
		if(elementMap == null){
			Map<String, ContentHandler> map = new HashMap<String, ContentHandler>();
			initHandlerMap(map);
			if(map.isEmpty()){
				map = Collections.emptyMap();
			} else if (map.size() == 1){
				Entry<String, ContentHandler> e = map.entrySet().iterator().next();
				map = Collections.singletonMap(e.getKey(), e.getValue());
			} else {
				map = Collections.unmodifiableMap(new HashMap<String, ContentHandler>(map));
			}
			elementMap = map;
		}
		return elementMap;
	}
	
	/**
	 * Adds handlers to the element map. Subclasses may override this to provide
	 * handlers for different tags.
	 * 
	 * @param map
	 *            the map to which handlers should be added
	 * @since JVerbnet 1.0.0
	 */
	protected void initHandlerMap(Map<String, ContentHandler> map){
		// do nothing;
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedHandler#clear()
	 */
	public final void clear() {
		// clear handler state
		tagStack.clear();
		clearLocal();
		// clear children's states
		for(ContentHandler h : getHandlerMappings().values())
			if(h instanceof ITaggedHandler<?>)
				((ITaggedHandler<?>)h).clear();
	}
	
	/**
	 * Subclasses may override this method to add code to be invoked when
	 * {@link #clear()} is called.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	protected void clearLocal(){
		// do nothing 
	}

	/**
	 * The stack of tags encountered inside the tagged block (including the
	 * tagged block tag itself)
	 * 
	 * @since JVerbnet 1.0.0
	 */
	protected Stack<String> tagStack = new Stack<String>();

	/**
	 * Returns <code>true</code> if the handler currently thinks it is inside of
	 * its assigned tagged block.
	 * 
	 * @return <code>true</code> if the handler currently thinks it is inside of
	 *         its assigned tagged block; <code>false</code> otherwise
	 * @since JVerbnet 1.0.0
	 */
	public boolean isInsideTaggedBlock(){
		return !tagStack.isEmpty();
	}

	/**
	 * Handle a start element. If the start element is assigned tag of this
	 * handler, and the handler is not already inside the assigned block, then
	 * enter the assigned block by running the
	 * {@link #startTaggedBlock(String, String, String, Attributes)} method.
	 * Otherwise, try to find a mapped handler to handle the element.
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
	 * @throws IllegalStateException
	 *             if the parser has not been set when this method is called
	 * @since JVerbnet 1.0.0
	 */
	public final void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		
		// make sure parser is set
		checkParserSet();
		
		// first case: we are not inside the tagged block
		if(!isInsideTaggedBlock()){
			
			// if it's the right tag, enter the tagged block
			if(getTag().equals(qName)){
				tagStack.push(qName);
				getParser().setContentHandler(this);
				
				// run tagged block tasks
				startTaggedBlock(uri, localName, qName, attrs);
				for(ITaggedBlockTaskHandler h : taggedBlockTasks)
					h.startTaggedBlock(uri, localName, qName, attrs);

				return;
				
			// otherwise report element to error stream
			} else {
				reportWarning(messageMatcherIgnoreExternal.replaceAll(getTag()) + qName);
			}
		// second case: we are inside the tagged block	
		} else {
			
			// see if this element is mapped
			ContentHandler child = getHandlerMappings().get(qName);
			
			// if the element is mapped
			if(child != null){
				// if the child is a tagged handler, clear it
				if(child instanceof ITaggedHandler)
					((ITaggedHandler<?>)child).clear();
				// hand over control to the child; the expectation is that the child
				// will set itself as the handler for the parser, and when it exits
				// its block will hand control back to the parent
				child.startElement(uri, localName, qName, attrs);
			// if the element is not mapped
			} else {
				// add to tag stack so we know when to exit the block
				tagStack.push(qName);
				// report a warning
				reportWarning(messageMatcherIgnoreInternal.replaceAll(getTag()) + qName);
				// allow subclasses to do something with unmapped elements
				handleUnmappedStartElement(uri, localName, qName, attrs);
			}
		}
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedBlockTaskHandler#startTaggedBlock(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startTaggedBlock(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		// do nothing
	}

	/** 
	 * Subclasses may override this method to handle unmapped start tags.
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
	protected void handleUnmappedStartElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		// do nothing
	}

	/**
	 * Handle an end element. If the end element is the end element of the assigned block, then
	 * exit the assigned block by running the
	 * {@link #endTaggedBlock(String, String, String)} method.
	 * Otherwise, try to find a mapped handler to handle the element.
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
	 * @throws SAXException
	 *             any SAX exception, possibly wrapping another exception
	 * @throws IllegalStateException
	 *             if the parser has not been set when this method is called
	 * @since JVerbnet 1.0.0
	 */
	@Override
	public final void endElement(String uri, String localName, String qName) throws SAXException {
		
		// make sure parser is set
		checkParserSet();

		
		// first case: we are inside the tagged block
		if(isInsideTaggedBlock()){
			
			// we've reached the end of a tagged block
			if(tagStack.peek().equals(qName)){
				tagStack.pop();

				// we're exiting the tagged block
				if(tagStack.isEmpty()){
					if(getParent() != null)
						getParser().setContentHandler(getParent());
					
					// run tagged block tasks
					endTaggedBlock(uri, localName, qName);
					for(ITaggedBlockTaskHandler h : getTaggedBlockTasks())
						h.endTaggedBlock(uri, localName, qName);
					
				} else {
					// report unmapped end element
					handleUnmappedEndElement(uri, localName, qName);
				}

			// we have reached the end tag of an unmapped element
			} else {
				throw new SAXException("Unmatched end tag: " + qName);
			}
			
		// second case: we are not inside the tagged block
		} else {
			// do nothing; we already reported the unhandled element
		}
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedBlockTaskHandler#endTaggedBlock(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
		// do nothing
	}

	/** 
	 * Subclasses may override this method to handle end tags.
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
	 * @throws SAXException
	 *             any SAX exception, possibly wrapping another exception
	 * @since JVerbnet 1.0.0
	 */
	protected void handleUnmappedEndElement(String uri, String localName, String qName) throws SAXException {
		// do nothing
	}
	
	/**
	 * Reports a warning. Tries to report the warning first to the parser's
	 * error handler, if any. If there is none, it reports the warning to
	 * {@link System#err}.
	 * 
	 * @param message
	 *            the message to report
	 * @since JVerbnet 1.0.0
	 */
	protected void reportWarning(String message) {
		// don't do anything if the flag is not set
		if(!reportWarnings)
			return;
		
		// here's the message
		SAXParseException e = new SAXParseException(message, locator);
		
		// report the error
		XMLReader parser = getParser();
		ErrorHandler errorHandler = (parser == null) ? null : parser.getErrorHandler();
		if(errorHandler == null){
			e.printStackTrace();
		} else {
			try {
				errorHandler.warning(e);
			} catch (SAXException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.ITaggedHandler#getElement()
	 */
	public T getElement(){
		try {
			return doGetElement();
		} catch(Throwable t){
			StringWriter sb = new StringWriter();
			Locator l = getDocumentLocator();
			if(l != null){
				sb.append("line ");
				sb.append(Integer.toString(l.getLineNumber()));
				sb.append(", column ");
				sb.append(Integer.toString(l.getColumnNumber()));
				sb.append(" (publicId: ");
				sb.append(l.getPublicId());
				sb.append(", systemId: ");
				sb.append(l.getSystemId());
				sb.append("), ");
			} else {
				sb.append("Unknown location, ");
			}
			sb.append(" caused by:\n");
			t.printStackTrace(new PrintWriter(sb));
			throw new RuntimeException(sb.toString());
		}
	}

	/**
	 * If subclasses intend to use the {@link #getElement()} method, they should
	 * override this method to provide the code that constructs the object
	 * corresponding to the tagged block.
	 * 
	 * @return the object corresponding to the tagged block
	 * @since JVerbnet 1.0.0
	 */
	protected T doGetElement() {
		// throw an exception if this method is called and the subclasses does not override
		return Checks.<T>thisMethodShouldNeverBeCalled();
	}

}
