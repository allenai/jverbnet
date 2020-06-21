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

import static edu.mit.jverbnet.util.Checks.IsNull;
import static edu.mit.jverbnet.util.Checks.NotNull;
import static edu.mit.jverbnet.util.Checks.NotNullEmptyOrBlank;
import static edu.mit.jverbnet.util.Checks.trim;

import java.util.HashMap;
import java.util.Map;

import edu.mit.jverbnet.data.IVerbnetType;
import edu.mit.jverbnet.data.VerbnetTypes;

/**
 * Syntactic argument types. The values in this enum correspond to the named
 * elements of xsd:choice subelement of the SYNTAX portion of the Verbnet xsd
 * file.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public enum SyntaxArgType implements IVerbnetType {

	NP  ("NP",   "Noun Phrase", VALUE_RULE.REQUIRED),
	ADV ("ADV",  "Adverb",      VALUE_RULE.OPTIONAL),
	ADJ ("ADJ",  "Adjective",   VALUE_RULE.PROHIBITED),
	PREP("PREP", "Preposition", VALUE_RULE.OPTIONAL),
	LEX ("LEX",  "Lexical",     VALUE_RULE.REQUIRED);

	/**
	 * The name of the xsd:simpleType entry that describes this verbnet type in the XSD file.
	 *
	 * @since JVerbnet 1.0.0
	 */
	public static final String XSD_TYPE_NAME = "argType";

	// final fields
	private final String id;
	private final String name;
	private final VALUE_RULE vRule;

	/**
	 * Enum constructor that creates a new event argument type.
	 *
	 * @param name
	 *            The human-readable name of the type.
	 * @since JVerbnet 1.0.0
	 * @throws NullPointerException
	 *             if either value is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if either value is empty or all whitespace
	 */
	SyntaxArgType(String id, String name, VALUE_RULE vRule){
		// check arguments
		id = NotNullEmptyOrBlank.check("id", id);
		name = NotNullEmptyOrBlank.check("name", name);
		NotNull.check("vRule", vRule);

		// assign fields
		this.id = id;
		this.name = name;
		this.vRule = vRule;
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
	 * Returns a human-readable name for this argument type, suitable for
	 * display in a UI.
	 *
	 * @return a human-readable name for this argument type, suitable for
	 *         display in a UI.
	 * @since JVerbnet 1.0.0
	 */
	public String getName(){
		return name;
	}

	/**
	 * Returns the value rule for this argument type.
	 *
	 * @return the non-<code>null</code> value rule for this type
	 * @since JVerbnet 1.0.0
	 */
	public VALUE_RULE getValueRule(){
		return vRule;
	}

	// the id map for the getById method
	private static Map<String, SyntaxArgType> idMap;

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
	public static SyntaxArgType getById(String id){
		NotNullEmptyOrBlank.check("id", id);
		SyntaxArgType result = idMap.get(id.toLowerCase());
		VerbnetTypes.printIdWarnings(AuxNounPhraseType.class, result, id);
		return result;
	}

	// the following code initializes the id map
	static {
		SyntaxArgType[] values = values();
		idMap = new HashMap<String, SyntaxArgType>(values.length);
		SyntaxArgType conflict;
		for(SyntaxArgType t : values){
			conflict = idMap.put(t.getID().toLowerCase(), t);
			if(conflict != null)
				throw new IllegalStateException("The constants " + t.name() + " and " + conflict.name() + " of the enum " + SyntaxArgType.class.getSimpleName() + " have identical normalized ids: " + t.getID().toLowerCase());
		}
	}

	/**
	 * Checks that the value meets the expectations of the type.
	 *
	 * @author Mark A. Finlayson
	 * @version 1.0.0
	 * @since JVerbnet 1.0.0
	 */
	public enum VALUE_RULE {

		/**
		 * The value must be <code>null</code>.
		 *
		 * @since JVerbnet 1.0.0
		 */
		PROHIBITED {
			@Override
			public String checkValue(String value) {
				IsNull.check("value", value);
				return null;
			}
		},

		/**
		 * The value may be anything.
		 *
		 * @since JVerbnet 1.0.0
		 */
		OPTIONAL {
			@Override
			public String checkValue(String value) {
				return trim(NotNull, value);
			}
		},

		/**
		 * The value must not be <code>null</code>, empty, or blank.
		 *
		 * @since JVerbnet 1.0.0
		 */
		REQUIRED {
			@Override
			public String checkValue(String value) {
				return NotNullEmptyOrBlank.check("value", value);
			}
		};

		/**
		 * Checks that the string value meets the condition.
		 *
		 * @param value
		 *            the value to be checked
		 * @throws Exception
		 *             if the value does not meet the condition
		 * @return the original value, if <code>null</code>, or the trimmed
		 *         value
		 * @since JVerbnet 1.0.0
		 */
		public abstract String checkValue(String value);
	}

}
