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

import static edu.mit.jverbnet.util.Checks.IsNullEmptyOrBlank;
import static edu.mit.jverbnet.util.Checks.NotNull;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.mit.jverbnet.data.IVerbnetType;
import edu.mit.jverbnet.data.selection.ISelRestrictions;
import edu.mit.jverbnet.data.selection.ISelRestrictions.Logic;
import edu.mit.jverbnet.data.selection.SelRestrictions;
import edu.mit.jverbnet.util.parse.IHasParserHandler;
import edu.mit.jverbnet.util.parse.ITaggedHandler;
import edu.mit.jverbnet.util.parse.LazyForwardingHandler;
import edu.mit.jverbnet.util.parse.MappedHandler;
import edu.mit.jverbnet.util.parse.TaggedBlockTaskHandlerAdapter;

/** 
 * Handles Verbnet XML blocks tagged with {@value #XML_TAG_SELRESTRS} or {@value #XML_TAG_SYNRESTRS}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class SelRestrictionsHandler<T extends IVerbnetType> extends MappedHandler<ISelRestrictions<T>> {
	
	public static final String XML_TAG_SELRESTRS = "SELRESTRS";
	public static final String XML_TAG_SYNRESTRS = "SYNRESTRS";
	
	// unchanging fields
	private final boolean isSelRestr;
	private final Class<T> verbnetTypeClass;
	
	private Logic logic;
	private Map<T, Boolean> restrictionMap;
	private List<ISelRestrictions<T>> subRestrs;
	
	/**
	 * Creates a new SelRestrictionsHandler with no parent or parser.
	 * 
	 * @param isSelRestr
	 *            whether this is a semantic restriction (<code>true</code>) or
	 *            syntactic restriction (<code>false</code>).
	 * @param verbnetTypeClass
	 *            the verbnet type used in this restriction
	 * @since JVerbnet 1.0.0
	 */
	public SelRestrictionsHandler(boolean isSelRestr, Class<T> verbnetTypeClass) {
		this(null, null, isSelRestr, verbnetTypeClass);
	}

	/**
	 * Creates a new SelRestrictionsHandler with the specified parent.
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
	public SelRestrictionsHandler(IHasParserHandler parent, boolean isSelRestr, Class<T> verbnetTypeClass) {
		this((parent == null ? null : parent.getParser()), parent, isSelRestr, verbnetTypeClass);
	}
	
	/**
	 * Creates a new SelRestrictionsHandler with the specified parser.
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
	public SelRestrictionsHandler(XMLReader parser, boolean isSelRestr, Class<T> verbnetTypeClass) {
		this(parser, null, isSelRestr, verbnetTypeClass);
	}
	
	/**
	 * Creates a new SelRestrictionsHandler with the specified parser and parent.
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
	public SelRestrictionsHandler(XMLReader parser, ContentHandler parent, boolean isSelRestr, Class<T> verbnetTypeClass) {
		super(parser, parent, isSelRestr ? 
				XML_TAG_SELRESTRS : 
					XML_TAG_SYNRESTRS);
		NotNull.check("verbnetTypeClass", verbnetTypeClass);
		this.isSelRestr = isSelRestr;
		this.verbnetTypeClass = verbnetTypeClass;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#initHandlerMap(java.util.Map)
	 */
	@Override
	protected void initHandlerMap(Map<String, ContentHandler> map) {
		
		RestrictionHandler<T> restrHandler = new RestrictionHandler<T>(this, isSelRestr, verbnetTypeClass){
			@Override
			public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
				Entry<T, Boolean> e = doGetElement();
				restrictionMap.put(e.getKey(), e.getValue());
			}
		};
		
		final ITaggedHandler<ISelRestrictions<T>> subRestrsHandler = new LazyForwardingHandler<ISelRestrictions<T>>(this, getTag()) {
			@Override
			protected ITaggedHandler<ISelRestrictions<T>> createBackingHandler() {
				return new SelRestrictionsHandler<T>(isSelRestr, verbnetTypeClass);
			}
		};
		subRestrsHandler.getTaggedBlockTasks().add(new TaggedBlockTaskHandlerAdapter(){
			@Override
			public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
				subRestrs.add(subRestrsHandler.getElement());
			}
		});
				
		map.put(restrHandler.getTag(), restrHandler);
		map.put(subRestrsHandler.getTag(), subRestrsHandler);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	@Override
	protected void clearLocal() {
		logic = null;
		restrictionMap = new LinkedHashMap<T, Boolean>();
		subRestrs = new LinkedList<ISelRestrictions<T>>();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#startTaggedBlock(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startTaggedBlock(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		String logicStr = attrs.getValue("logic");
		if(IsNullEmptyOrBlank.is(logicStr)){
			logic = null;
		} else {
			logic = Logic.valueOf(logicStr.toUpperCase());
		}
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public ISelRestrictions<T> doGetElement() {
		// nothing to do
		if(restrictionMap.isEmpty() && subRestrs.isEmpty())
			return null;
		return new SelRestrictions<T>(logic, restrictionMap, subRestrs);
	}

}
