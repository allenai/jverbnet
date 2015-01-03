/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.data;

import static edu.mit.jverbnet.util.Checks.NotNullEmptyOrBlank;

import java.util.HashMap;
import java.util.Map;

import edu.mit.jverbnet.data.semantics.ArgType;
import edu.mit.jverbnet.data.semantics.ISemanticArgType;
import edu.mit.jverbnet.data.syntax.INounPhraseType;

/**
 * Selection restriction value types. The values in this enum correspond to
 * the elements of &lt;xsd:simpleType name="themRoleType"&gt; in the Verbnet xsd
 * file.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class ThematicRoleType implements INounPhraseType, ISemanticArgType {

	/** 
	 * The name of the xsd:simpleType entry that describes this verbnet type in the XSD file.
	 *
	 * @since JVerbnet 1.0.0
	 */
	public static final String XSD_TYPE_NAME = "themRoleType";
	
	// final fields
	private final String id;
	
	/**
	 * Constructor that creates a new frame type.
	 * 
	 * @param id
	 *            The id of the value
	 * @since JVerbnet 1.0.0
	 * @throws NullPointerException
	 *             if the id is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the id is empty or all whitespace
	 */
	protected ThematicRoleType(String id){
		this.id = NotNullEmptyOrBlank.check("id", id);
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.IHasID#getID()
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
		return ArgType.ThemRole;
	}
	
	// id map
	private static final Map<String, ThematicRoleType> idMap = new HashMap<String, ThematicRoleType>();

	/**
	 * Returns the object corresponding to the specified xsd name. The id is
	 * matched to values without regard to case. If no value has the specified
	 * id, a new value is created with that id and returned.
	 * 
	 * If the {@link VerbnetTypes#isPrintingIdWarnings()} flag is set, the method
	 * will print a warning to standard out if there is a value with the
	 * specified id was created, or if the specified id is not identical to the
	 * returned value's id.
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
	public static ThematicRoleType getById(String id){
		NotNullEmptyOrBlank.check("id", id);
		ThematicRoleType result = idMap.get(id.toLowerCase());
		
		if(result == null){
			result = new ThematicRoleType(id);
			idMap.put(id.toLowerCase(), result);
		}
		
		// print warnings
		if(!result.getID().equals(id))
			VerbnetTypes.printIdNormalizationWarning(ThematicRoleType.class, id);
		
		return result;
	}

	private transient String toStr;
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(toStr == null)
			toStr = toString(this);
		return toStr;
	}

	/**
	 * Formats a thematic role type into a string suitable for display within a
	 * predicate string.
	 * 
	 * @param themRole
	 *            the thematic role to be formatted; may not be
	 *            <code>null</code>
	 * @return the string representing this thematic role
	 * @throws NullPointerException
	 *             if the argument is <code>null</code>
	 * @since JVerbnet 1.2.0
	 */
	public static String toString(ThematicRoleType themRole){
		String id = themRole.getID();
		StringBuilder sb = new StringBuilder();
		sb.append(Character.toUpperCase(id.charAt(0)));
		int idx = id.lastIndexOf('_');
		if(idx > -1)
			sb.append(id.substring(idx+1));
		return sb.toString();
	}

}
