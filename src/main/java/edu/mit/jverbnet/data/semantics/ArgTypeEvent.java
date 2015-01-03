/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.data.semantics;

import static edu.mit.jverbnet.util.Checks.NotNullEmptyOrBlank;

import java.util.HashMap;
import java.util.Map;

import edu.mit.jverbnet.data.VerbnetTypes;

/**
 * The subtypes of event argument type. The values in this enum correspond to
 * the elements of &lt;xsd:simpleType name="argEventType"&gt; in the Verbnet xsd
 * file.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public enum ArgTypeEvent implements ISemanticArgType {
	
	E       ("E"),
	E0      ("E0"),
	E1      ("E1"),
	duringE ("during(E)"),
	duringE0("during(E0)"),
	duringE1("during(E1)"),
	endE    ("end(E)"),
	endE0   ("end(E0)"),
	endE1   ("end(E1)"),
	resultE ("result(E)"),
	resultE0("result(E0)"),
	resultE1("result(E1)"),
	startE  ("start(E)"),
	startE0 ("start(E0)"),
	startE1 ("start(E1)");
	
	/** 
	 * The name of the xsd:simpleType entry that describes this verbnet type in the XSD file.
	 *
	 * @since JVerbnet 1.0.0
	 */
	public static final String XSD_TYPE_NAME = "argEventType";
	
	// final fields
	private final String id;
	
	/**
	 * Enum constructor that creates a new event argument type.
	 * 
	 * @param id
	 *            The id of the value
	 * @since JVerbnet 1.0.0
	 * @throws NullPointerException
	 *             if the id is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the id is empty or all whitespace
	 */
	ArgTypeEvent(String id){
		this.id = NotNullEmptyOrBlank.check("id", id);
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVerbnetType#getID()
	 */
	public String getID(){
		return id;
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.semantics.ISemanticArgType#getArgType()
	 */
	public ArgType getArgType() {
		return ArgType.Event;
	}
	
	/**
	 * Returns the object corresponding to the specified xsd name. The id is
	 * matched to values without regard to case. If no value has the specified
	 * id, the method returns null.
	 * 
	 * If the {@link VerbnetTypes#isPrintingIdWarnings()} flag is set, the method
	 * will print a warning to standard out if there is no value with the
	 * specified id, or if the specified id is not exactly identical to the
	 * value's id (i.e., differs in case).
	 * 
	 * @param id
	 *            the id of the type value as found in the xsd file and in the
	 *            xml data files.
	 * @return the type corresponding to the specified xsd name, or
	 *         <code>null</code> if none
	 * @throws NullPointerException
	 *             if the id is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the id is empty or all whitespace
	 * @since JVerbnet 1.0.0
	 */
	public static ArgTypeEvent getById(String id){
		NotNullEmptyOrBlank.check("id", id);
		ArgTypeEvent result = idMap.get(id.toLowerCase());
		VerbnetTypes.printIdWarnings(ArgTypeEvent.class, result, id);
		return result;
	}
	
	// the id map for the getById method
	private static Map<String, ArgTypeEvent> idMap;
	
	// the following code initializes the id map
	static {
		ArgTypeEvent[] values = values();
		idMap = new HashMap<String, ArgTypeEvent>(values.length);
		ArgTypeEvent conflict;
		for(ArgTypeEvent t : values){
			conflict = idMap.put(t.getID().toLowerCase(), t);
			if(conflict != null)
				throw new IllegalStateException("The constants " + t.name() + " and " + conflict.name() + " of the enum " + ArgTypeEvent.class.getSimpleName() + " have identical normalized ids: " + t.getID().toLowerCase());
		}
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getID();
	}

}
