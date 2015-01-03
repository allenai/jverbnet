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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import edu.mit.jverbnet.data.syntax.AuxNounPhraseType;
import edu.mit.jverbnet.data.syntax.INounPhraseType;
import edu.mit.jverbnet.util.IHasID;


/** 
 * Utility class for verbnet types
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class VerbnetTypes {
	
	public static final String plus = "+";
	public static final String minus = "-";
	
	/**
	 * Invokes the static getById method on a given verbnet type. If the
	 * specified id is not found, prints a warning to standard err.
	 * 
	 * @param cls
	 *            the verbnet type from which to retrieve the value
	 * @param id
	 *            the id of the type to retrieve
	 * @return the retrieved object, or <code>null</code> if none found
	 * @throws NullPointerException
	 *             if the class is <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public static <T extends IVerbnetType> T getById(Class<T> cls, String id){
		T result = getTypeByIdNoWarnings(cls, id);
		printIdWarnings(cls, result, id);
		return result;
	}
	
	/**
	 * Invokes the static getById method on a given verbnet type. 
	 * 
	 * @param cls
	 *            the verbnet type from which to retrieve the value
	 * @param id
	 *            the id of the type to retrieve
	 * @return the retrieved object, or <code>null</code> if none found
	 * @throws NullPointerException
	 *             if the class is <code>null</code>
	 *             @throws IllegalStateException if the class does not have an accessible getById method
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IVerbnetType> T getTypeByIdNoWarnings(Class<T> cls, String id){
		try {
			Method method = cls.getMethod(IVerbnetType.METHOD_NAME_getById, String.class);
			if(!Modifier.isStatic(method.getModifiers()))
				throw new IllegalStateException(IVerbnetType.METHOD_NAME_getById + " method on " + cls.getName() + " is not static");
			return (T)method.invoke(null, id);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} 
	}
	
	/**
	 * Retrieves the value of the static field containing the XSD type name for
	 * a verbnet type.
	 * 
	 * @param c
	 *            the type for which the name should be retrieved
	 * @return the value of the field
	 * @throws IllegalStateException
	 *             if the class does not have an accessible xsd type name field
	 * @since JVerbnet 1.0.0
	 */
	public static String getXSDSimpleTypeName(Class<? extends IVerbnetType> c){
		
		// obtain the field
		Field field = null;
		try {
			field = c.getField(IVerbnetType.FIELD_NAME_XSD_TYPE_NAME);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		
		// get the value
		try {
			return (String)field.get(null);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} 
	}

	/**
	 * Returns the selection restriction for a particular string value. When
	 * passed "+", returns <code>true</code>; when passed "-", returns false.
	 * Otherwise, throws an exception
	 * 
	 * @param value
	 *            either "+" or "-"
	 * @return <code>true</code> when the value is "+"; <code>false</code> when
	 *         the value is "-"
	 * @throws IllegalArgumentException
	 *             if the specified string is not "+" or "-"
	 * @since JVerbnet 1.0.0
	 */
	public static boolean getSelectionRestriction(String value){
		if(value == null)
			return true;
		if(plus.equals(value))
			return true;
		if(minus.equals(value))
			return false;
		throw new IllegalArgumentException("Illegal selection restriction value: " + value);
	}

	/**
	 * Returns the noun phrase type for the specified id.
	 * 
	 * @param id
	 *            the id of the noun phrase type to find.
	 * @return the noun phrase type found
	 * @since JVerbnet 1.0.0
	 */
	public static INounPhraseType getNounPhraseTypeById(String id){
		INounPhraseType result = AuxNounPhraseType.getById(id);
		if(result != null)
			return result;
		return ThematicRoleType.getById(id);
	}

	// flag for printing id warnings
	private static boolean isPrintingIdWarnings = false;


	/** 
	 * Returns the value of the isPringingIdWarnings flag.
	 *
	 * @return <code>true</code> if warnings are being printed to standard err; <code>false</code> if not.
	 * @since JVerbnet 1.0.0
	 */
	public static boolean isPrintingIdWarnings(){
		return isPrintingIdWarnings;
	}

	/** 
	 * Sets the isPrintingIdWarnings flag.
	 *
	 * @since JVerbnet 1.0.0
	 */
	public static void setPrintIdWarnings(boolean value){
		isPrintingIdWarnings = value;
	}

	/**
	 * Prints a warning to standard err if the type is <code>null</code> or the
	 * type's id does not match the specified id exactly (e.g., it differs in
	 * case).
	 * 
	 * @param cls
	 *            the class of the type
	 * @param type
	 *            the object with the id
	 * @param id
	 *            the id to check
	 * @since JVerbnet 1.0.0
	 */
	public static void printIdWarnings(Class<? extends IHasID> cls, IHasID type, String id){
		if(type == null){
			printIdUnknownWarning(cls, id);
		} else if(!id.equals(type.getID())){
			printIdNormalizationWarning(cls, id);
		}
	}

	/**
	 * Prints a warning to standard err that the specified id is unknown for the
	 * specified type
	 * 
	 * @param cls
	 *            the class of the type
	 * @param id
	 *            the id to check
	 * @since JVerbnet 1.0.0
	 */
	public static void printIdUnknownWarning(Class<? extends IHasID> cls, String id) {
		if(isPrintingIdWarnings)
			System.err.println("Unknown id '" + id + "' for type " + cls.getSimpleName());
	}

	/**
	 * Prints a warning to standard err if the type's id does not match the
	 * specified id exactly (e.g., it differs in case).
	 * 
	 * @param type
	 *            the object with the id
	 * @param id
	 *            the id to check
	 * @since JVerbnet 1.0.0
	 */
	public static void printIdNormalizationWarning(Class<? extends IHasID> type, String id) {
		if(isPrintingIdWarnings)
			System.err.println("Normalization of id '" + id + "' was required to retrieve the value for type " + type.getSimpleName());
	}
	
}
