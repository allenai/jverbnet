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

import static edu.mit.jverbnet.util.Checks.trimAndMaskNull;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.mit.jverbnet.data.IWordnetKey;
import edu.mit.jverbnet.data.Member.MemberBuilder;
import edu.mit.jverbnet.data.WordnetKey;
import edu.mit.jverbnet.util.parse.IHasParserHandler;
import edu.mit.jverbnet.util.parse.MappedHandler;

/** 
 * Handles Verbnet XML blocks tagged with {@value #XML_TAG_MEMBER}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class MemberHandler extends MappedHandler<MemberBuilder> {
	
	public static final String XML_TAG_MEMBERS = "MEMBERS";
	public static final String XML_TAG_MEMBER = "MEMBER";
	
	// assignable fields
	private String name;
	private Map<IWordnetKey, Boolean> wnTypes;
	private List<String> groupings;

	/** 
	 * Creates a new MemberHandler with no parent or parser.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public MemberHandler() {
		this(null, null);
	}

	/**
	 * Creates a new MemberHandler with the specified parent.
	 * 
	 * @param parent
	 *            the parent of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public MemberHandler(IHasParserHandler parent) {
		this((parent == null ? null : parent.getParser()), parent);
	}
	
	/**
	 * Creates a new MemberHandler with the specified parser.
	 * 
	 * @param parser
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public MemberHandler(XMLReader parser) {
		this(parser, null);
	}
	
	/**
	 * Creates a new MemberHandler with the specified parser and parent.
	 * 
	 * @param parser
	 *            the parent of the handler; may be <code>null</code>
	 * @param parent
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public MemberHandler(XMLReader parser, ContentHandler parent) {
		super(parser, parent, XML_TAG_MEMBER);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	@Override
	protected void clearLocal() {
		name = null;
		wnTypes = new LinkedHashMap<IWordnetKey, Boolean>();
		groupings = new LinkedList<String>();
	}

	
	private final Pattern listPattern = Pattern.compile("\\s+"); 
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#startTaggedBlock(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startTaggedBlock(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		name = attrs.getValue("name");
		
		// wn
		String wnList = attrs.getValue("wn").trim();
		String[] wnStrs = listPattern.split(wnList);
		IWordnetKey wnKey;
		boolean hasQmark;
		for(String wnStr : wnStrs){
			if(wnStr.length() == 0)
				continue;
			if((hasQmark = wnStr.charAt(0) == '?'))
				wnStr = wnStr.substring(1);
			wnKey = WordnetKey.parseKey(wnStr);
			wnTypes.put(wnKey, hasQmark);
		}
		
		// groupings
		String gList = trimAndMaskNull(attrs.getValue("grouping"), "");
		String[] gStrs = listPattern.split(gList);
		for(String gStr : gStrs)
			if(gStr.length() > 0)
				groupings.add(gStr);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public MemberBuilder doGetElement() {
		MemberBuilder result = new MemberBuilder();
		result.setName(name);
		result.getWordnetTypes().putAll(wnTypes);
		result.getGroupings().addAll(groupings);
		return result;
	}

}
