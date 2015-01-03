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

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.mit.jverbnet.data.Frame.FrameBuilder;
import edu.mit.jverbnet.data.FrameType;
import edu.mit.jverbnet.data.semantics.ISemanticDesc;
import edu.mit.jverbnet.data.syntax.ISyntaxDesc;
import edu.mit.jverbnet.util.parse.CDataHandler;
import edu.mit.jverbnet.util.parse.IHasParserHandler;
import edu.mit.jverbnet.util.parse.ListHandler;
import edu.mit.jverbnet.util.parse.MappedHandler;

/** 
 * Handles Verbnet XML blocks tagged with {@value #XML_TAG_FRAME}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class FrameHandler extends MappedHandler<FrameBuilder> {
	
	public static final String XML_TAG_FRAMES = "FRAMES";
	public static final String XML_TAG_FRAME = "FRAME";
	public static final String XML_TAG_DESCRIPTION = "DESCRIPTION";
	public static final String XML_TAG_EXAMPLES = "EXAMPLES";
	public static final String XML_TAG_EXAMPLE = "EXAMPLE";
	
	// the handler for the examples list element
	private final ListHandler<String> examplesHandler = new ListHandler<String>(this, XML_TAG_EXAMPLES, new CDataHandler(XML_TAG_EXAMPLE));
	
	// assignable fields
	private String descNum;
	private FrameType primaryType;
	private FrameType secondaryType;
	private String xTag;
	private ISyntaxDesc syntaxDesc;
	private ISemanticDesc semanticDesc;

	/** 
	 * Creates a new FrameHandler with no parent or parser.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public FrameHandler() {
		this(null, null);
	}

	/**
	 * Creates a new FrameHandler with the specified parent.
	 * 
	 * @param parent
	 *            the parent of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public FrameHandler(IHasParserHandler parent) {
		this((parent == null ? null : parent.getParser()), parent);
	}
	
	/**
	 * Creates a new FrameHandler with the specified parser.
	 * 
	 * @param parser
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public FrameHandler(XMLReader parser) {
		this(parser, null);
	}
	
	/**
	 * Creates a new FrameHandler with the specified parser and parent.
	 * 
	 * @param parser
	 *            the parent of the handler; may be <code>null</code>
	 * @param parent
	 *            the parser of the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public FrameHandler(XMLReader parser, ContentHandler parent) {
		super(parser, parent, XML_TAG_FRAME);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#initHandlerMap(java.util.Map)
	 */
	@Override
	protected void initHandlerMap(Map<String, ContentHandler> map) {
		
		MappedHandler<Object> descHandler = new MappedHandler<Object>(this, XML_TAG_DESCRIPTION) {
			@Override
			public void startTaggedBlock(String uri, String localName, String qName, Attributes attrs) throws SAXException {
				descNum = attrs.getValue("descriptionNumber");
				String primaryTypeStr = attrs.getValue("primary");
				String secondaryTypeStr = attrs.getValue("secondary");
				xTag = attrs.getValue("xtag");
				
				// get types
				primaryType = FrameType.getById(primaryTypeStr);
				if(secondaryTypeStr.length() != 0)
					secondaryType = FrameType.getById(secondaryTypeStr);
			}
		};
		
		SyntaxDescHandler syntaxHandler = new SyntaxDescHandler(this){
			@Override
			public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
				syntaxDesc = doGetElement();
			}
		};
		
		SemanticDescHandler semanticsHandler = new SemanticDescHandler(this){
			@Override
			public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
				semanticDesc = doGetElement();
			}
		};
		
		// assign handlers
		map.put(descHandler.getTag(), descHandler);
		map.put(examplesHandler.getTag(), examplesHandler);
		map.put(syntaxHandler.getTag(), syntaxHandler);
		map.put(semanticsHandler.getTag(), semanticsHandler);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#clearLocal()
	 */
	@Override
	protected void clearLocal() {
		descNum = null;
		primaryType = null;
		secondaryType = null;
		xTag = null;
		syntaxDesc = null;
		semanticDesc = null;
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.parse.MappedHandler#doGetElement()
	 */
	public FrameBuilder doGetElement() {
		FrameBuilder result = new FrameBuilder();
		result.setDescriptionNumber(descNum);
		result.setPrimaryType(primaryType);
		result.setSecondaryType(secondaryType);
		result.setXTag(xTag);
		result.getExamples().addAll(examplesHandler.doGetElement());
		result.setSyntax(syntaxDesc);
		result.setSemantics(semanticDesc);
		return result;
	}

}
