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

import static edu.mit.jverbnet.util.Checks.NotNull;
import static edu.mit.jverbnet.util.Checks.NotNullEmptyOrBlank;

import java.util.HashMap;
import java.util.Map;

import edu.mit.jverbnet.data.IVerbnetType;
import edu.mit.jverbnet.data.ThematicRoleType;
import edu.mit.jverbnet.data.VerbnetTypes;

/**
 * Argument types. The values in this enum correspond to the elements of
 * &lt;xsd:simpleType name="argType"&gt; in the Verbnet xsd file.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public enum ArgType implements IVerbnetType {
	
	Constant     ("Constant",     ArgTypeConstant.class),
	Event        ("Event",        ArgTypeEvent.class),
	ThemRole     ("ThemRole",     ThematicRoleType.class),
	VerbSpecific ("VerbSpecific", ArgTypeVerbSpecific.class);
	
	/** 
	 * The name of the xsd:simpleType entry that describes this verbnet type in the XSD file.
	 *
	 * @since JVerbnet 1.0.0
	 */
	public static final String XSD_TYPE_NAME = "argType";
			
	// final fields
	private final String id;
	private final Class<? extends ISemanticArgType> subclassType;
	
	/**
	 * Enum constructor that creates a new event argument type.
	 * 
	 * @param id
	 *            The id of the value
	 * @since JVerbnet 1.0.0
	 * @throws NullPointerException
	 *             if either value is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if either value is empty or all whitespace
	 */
	ArgType(String id, Class<? extends ISemanticArgType> subclassType){
		// check arguments
		id = NotNullEmptyOrBlank.check("id", id);
		NotNull.check("subclassType", subclassType);
		
		// assign fields
		this.id = id;
		this.subclassType = subclassType;
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVerbnetType#getID()
	 */
	public String getID(){
		return id;
	}
	
	/** 
	 * Returns the associated subclass type for this argument type.
	 *
	 * @return  the associated subclass type for this argument type
	 * @since JVerbnet 1.0.0
	 */
	public Class<? extends ISemanticArgType> getSubclassType(){
		return subclassType;
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
	public static ArgType getById(String id){
		NotNullEmptyOrBlank.check("id", id);
		ArgType result = idMap.get(id.toLowerCase());
		VerbnetTypes.printIdWarnings(ArgType.class, result, id);
		return result;
	}
	
	// the id map for the getById method
	private static Map<String, ArgType> idMap;
	
	// the following code initializes the id map
	static {
		ArgType[] values = values();
		idMap = new HashMap<String, ArgType>(values.length);
		ArgType conflict;
		for(ArgType t : values){
			conflict = idMap.put(t.getID().toLowerCase(), t);
			if(conflict != null)
				throw new IllegalStateException("The constants " + t.name() + " and " + conflict.name() + " of the enum " + ArgType.class.getSimpleName() + " have identical normalized ids: " + t.getID().toLowerCase());
		}
	}

}
