/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.data.syntax;

import static edu.mit.jverbnet.util.Checks.NotNullEmptyOrBlank;

import java.util.HashMap;
import java.util.Map;

import edu.mit.jverbnet.data.VerbnetTypes;

/**
 * Selection restriction value types. The values in this enum correspond to
 * the elements of &lt;xsd:simpleType name="auxnpType"&gt; in the Verbnet xsd
 * file.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public enum AuxNounPhraseType implements INounPhraseType {

    Oblique ("Oblique"),
    Oblique1("Oblique1"),
    Oblique2("Oblique2"),
    NP      ("NP");

	/** 
	 * The name of the xsd:simpleType entry that describes this verbnet type in the XSD file.
	 *
	 * @since JVerbnet 1.0.0
	 */
	public static final String XSD_TYPE_NAME = "auxnpType";
	
	// final fields
	private final String id;

	/**
	 * Enum constructor that creates a new event argument type.
	 * 
	 * @param id
	 *            the id of the type
	 * @since JVerbnet 1.0.0
	 */
	AuxNounPhraseType(String id){
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
	public static AuxNounPhraseType getById(String id){
		NotNullEmptyOrBlank.check("id", id);
		AuxNounPhraseType result = idMap.get(id.toLowerCase());
		VerbnetTypes.printIdWarnings(AuxNounPhraseType.class, result, id);
		return result;
	}
	
	// the id map for the getById method
	private static Map<String, AuxNounPhraseType> idMap;
	
	// the following code initializes the id map
	static {
		AuxNounPhraseType[] values = values();
		idMap = new HashMap<String, AuxNounPhraseType>(values.length);
		AuxNounPhraseType conflict;
		for(AuxNounPhraseType t : values){
			conflict = idMap.put(t.getID().toLowerCase(), t);
			if(conflict != null)
				throw new IllegalStateException("The constants " + t.name() + " and " + conflict.name() + " of the enum " + AuxNounPhraseType.class.getSimpleName() + " have identical normalized ids: " + t.getID().toLowerCase());
		}
	}
	
}
