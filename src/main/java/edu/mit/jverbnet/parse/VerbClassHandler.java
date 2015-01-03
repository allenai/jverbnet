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

import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.mit.jverbnet.data.IFrame.IFrameBuilder;
import edu.mit.jverbnet.data.IMember.IMemberBuilder;
import edu.mit.jverbnet.data.IThematicRole.IThematicRoleBuilder;
import edu.mit.jverbnet.data.IVerbClass;
import edu.mit.jverbnet.data.VerbClass;
import edu.mit.jverbnet.util.parse.IHasParserHandler;
import edu.mit.jverbnet.util.parse.ITaggedHandler;
import edu.mit.jverbnet.util.parse.LazyForwardingHandler;
import edu.mit.jverbnet.util.parse.ListHandler;
import edu.mit.jverbnet.util.parse.MappedHandler;


/**
 * Handles Verbnet XML blocks tagged with {@value #XML_TAG_VNCLASS} or {@value
 * #XML_TAG_VNSUBCLASS}.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class VerbClassHandler extends MappedHandler<IVerbClass> {
	
	public static final String XML_TAG_VNCLASS = "VNCLASS";
	public static final String XML_TAG_VNSUBCLASS = "VNSUBCLASS";
	public static final String XML_TAG_SUBCLASSES = "SUBCLASSES";
	
	// unchanging fields
	private final boolean isRootHandler;
	private final ITaggedHandler<List<IMemberBuilder>> memberHandler = new ListHandler<IMemberBuilder>(this, MemberHandler.XML_TAG_MEMBERS, new MemberHandler());
	private final ITaggedHandler<List<IThematicRoleBuilder>> themroleHandler = new ListHandler<IThematicRoleBuilder>(this, ThematicRoleHandler.XML_TAG_THEMROLES, new ThematicRoleHandler());
	private final ITaggedHandler<List<IFrameBuilder>> frameHandler = new ListHandler<IFrameBuilder>(this, FrameHandler.XML_TAG_FRAMES, new FrameHandler());
	private final ITaggedHandler<List<IVerbClass>> subclassHandler = new ListHandler<IVerbClass>(this, XML_TAG_SUBCLASSES, new LazyForwardingHandler<IVerbClass>(XML_TAG_VNSUBCLASS) {
		protected ITaggedHandler<IVerbClass> createBackingHandler() {
			return new VerbClassHandler(false);
		}
	});
	
	
	// object fields
	private String id;
	
	/**
	 * Creates a new VerbClassHandler with no parent or parser.
	 * 
	 * @param isRootClass
	 *            whether or not this handler is managing root classes or
	 *            subclasses
	 * @since JVerbnet 1.0.0
	 */
	public VerbClassHandler(boolean isRootClass) {
		this(null, null, isRootClass);
	}

	/**
	 * Creates a new VerbClassHandler with the specified parent.
	 * 
	 * @param parent
	 *            the parent of the handler; may be <code>null</code>
	 * @param isRootClass
	 *            whether or not this handler is managing root classes or
	 *            subclasses
	 * @since JVerbnet 1.0.0
	 */
	public VerbClassHandler(IHasParserHandler parent, boolean isRootClass) {
		this((parent == null ? null : parent.getParser()), parent, isRootClass);
	}
	
	/**
	 * Creates a new VerbClassHandler with the specified parser.
	 * 
	 * @param parser
	 *            the parser of the handler; may be <code>null</code>
	 * @param isRootClass
	 *            whether or not this handler is managing root classes or
	 *            subclasses
	 * @since JVerbnet 1.0.0
	 */
	public VerbClassHandler(XMLReader parser, boolean isRootClass) {
		this(parser, null, isRootClass);
	}
	
	/**
	 * Creates a new VerbClassHandler with the specified parser and parent.
	 * 
	 * @param parser
	 *            the parent of the handler; may be <code>null</code>
	 * @param parent
	 *            the parser of the handler; may be <code>null</code>
	 * @param isRootClass
	 *            whether or not this handler is managing root classes or
	 *            subclasses
	 * @since JVerbnet 1.0.0
	 */
	public VerbClassHandler(XMLReader parser, ContentHandler parent, boolean isRootClass){
		super(parser, parent, isRootClass ? XML_TAG_VNCLASS : XML_TAG_VNSUBCLASS);
		isRootHandler = isRootClass;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#initHandlerMap(java.util.Map)
	 */
	@Override
	protected void initHandlerMap(Map<String, ContentHandler> map) {
		map.put(memberHandler.getTag(), memberHandler);
		map.put(themroleHandler.getTag(), themroleHandler);
		map.put(frameHandler.getTag(), frameHandler);
		map.put(subclassHandler.getTag(), subclassHandler);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	public void clearLocal(){
		id = null;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#startTaggedBlock(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startTaggedBlock(String uri, String localName, 	String qName, Attributes attrs) throws SAXException {
		id = attrs.getValue("ID");
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public IVerbClass doGetElement() {
		IVerbClass verb = new VerbClass(id, memberHandler.getElement(), themroleHandler.getElement(), frameHandler.getElement(), subclassHandler.getElement());
		if(isRootHandler)
			verb.setParent(null);
		return verb;
	}

}
