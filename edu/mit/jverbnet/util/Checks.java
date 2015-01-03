/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/** 
 * Utilities for checking variables.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class Checks {
	
	/** 
	 * The default variable name to use in the exceptions thrown by this class,
	 * if the specific variable name is null, empty, or all whitespace.
	 *
	 * @since JVerbnet 1.0.0
	 */
	public static final String genericVarName = "the variable";
	
	/**
	 * Normalizes a variable name to either a trimmed version of itself, or returns the
	 * generic variable name.
	 * 
	 * @param varName
	 *            the variable name to normalized
	 * @return the normalized name
	 * @since JVerbnet 1.0.0
	 */
	public static String normalizeVarName(String varName){
		if(varName == null)
			return genericVarName;
		varName = varName.trim();
		if(varName.length() == 0)
			return genericVarName;
		return varName;
	}
	
	/**
	 * A condition object defines a boolean condition over a set of objects.
	 * 
	 * @param <S>
	 *            the superclass of the objects dealt with by this condition
	 * @author Mark A. Finlayson
	 * @version 1.0.0
	 * @since JVerbnet 1.0.0
	 */
	public interface ICondition<S> {
		
		/**
		 * Returns <code>true</code> if the value satisfies the condition;
		 * <code>false</code> otherwise.
		 * 
		 * @param value
		 *            the value to be tested
		 * @return <code>true</code> if the value passes the condition;
		 *         <code>false</code> otherwise
		 * @since JVerbnet 1.0.0
		 */
		public <T extends S> boolean is(T value);
		
		/**
		 * Returns <code>true</code> if a <code>null</code> passes the
		 * condition; <code>false</code> otherwise.
		 * 
		 * @return <code>true</code> if a <code>null</code> passes the
		 *         condition; <code>false</code> otherwise
		 * @since JVerbnet 1.0.0
		 */
		public boolean allowsNulls();
	}
	
	/**
	 * A check object provides the ability to throw an exception when a
	 * specified object satisfies the condition.
	 * 
	 * @param <S>
	 *            the superclass of the objects dealt with by this check
	 * @author Mark A. Finlayson
	 * @version 1.0.0
	 * @since JVerbnet 1.0.0
	 */
	public interface ICheck<S> extends ICondition<S> {
		
		/**
		 * Throws an exception if the specified object does not satisfy the
		 * condition.
		 * 
		 * @param varName
		 *            the variable name to be used in the exception message; if
		 *            the name is <code>null</code>, empty, or all whitespace,
		 *            the generic variable name is used.
		 * @param value
		 *            the value to check
		 * @return the object specified; some checks return a modified version
		 *         of the object (e.g., a trimmed string)
		 * @throws NullPointerException
		 *             if the specified value is <code>null</code>, and nulls
		 *             are not allowed
		 * @throws IllegalArgumentException
		 *             if the specified value is does not meet the condition
		 * @since JVerbnet 1.0.0
		 */
		public <T extends S> T check(String varName, T value);

	}
	
	/**
	 * A mask object provides the ability to substitute different values for
	 * a specified object if it matches the condition.
	 * 
	 * @param <S>
	 *            the superclass of the objects dealt with by this mask
	 * @author Mark A. Finlayson
	 * @version 1.0.0
	 * @since JVerbnet 1.0.0
	 */
	public interface IMask<S> extends ICondition<S> {
		
		/**
		 * Returns the mask value if the specified value satisfies the
		 * condition; otherwise, the original value.
		 * 
		 * @param value
		 *            the value to be masked, if necessary
		 * @param mask
		 *            the mask value
		 * @return the mask value if the specified value satisfies the
		 *         condition; otherwise, the original value.
		 * @since JVerbnet 1.0.0
		 */
		public <T extends S> T mask(T value, T mask);
	}
	
	/** 
	 * A reallocation strategy defines if and how a collection should be reallocated.
	 *
	 * @author Mark A. Finlayson
	 * @version 1.0.0
	 * @since JVerbnet 1.0.0
	 */
	public interface IReallocationStrategy {
		public <T> List<T> reallocate(List<T> list);
		public <T> Set<T> reallocate(Set<T> set);
		public <T> SortedSet<T> reallocate(SortedSet<T> set);
		public <K, V> Map<K, V> reallocate(Map<K, V> map);
		public <K, V> SortedMap<K, V> reallocate(SortedMap<K, V> map);
	}
	
	/** 
	 * Abstract implementation of a condition.
	 *
	 * @param <S> the supertype of the objects dealt with by this condition
	 * @author Mark A. Finlayson
	 * @version 1.0.0
	 * @since JVerbnet 1.0.0
	 */
	public static abstract class Condition<S> implements ICondition<S> {
		
		// final field
		private final boolean allowsNulls;

		/**
		 * Creates a new condition object
		 * 
		 * @param allowsNulls
		 *            the value to be returned by the {@link #allowsNulls()}
		 *            method
		 * @since JVerbnet 1.0.0
		 */
		public Condition(boolean allowsNulls){
			this.allowsNulls = allowsNulls;
		}
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#allowsNulls()
		 */
		public boolean allowsNulls(){
			return allowsNulls;
		}
	}
	
	/**
	 * Abstract implementation of a check. 
	 * 
	 * @param <S>
	 *            the supertype of the objects dealt with by this check
	 * @author Mark A. Finlayson
	 * @version 1.0.0
	 * @since JVerbnet 1.0.0
	 */
	public static abstract class Check<S> extends Condition<S> implements ICheck<S> {

		/**
		 * Creates a new check object.
		 * 
		 * @param allowsNulls
		 *            the value to be returned by the {@link #allowsNulls()}
		 *            method
		 * @since JVerbnet 1.0.0
		 */
		public Check(boolean allowsNulls) {
			super(allowsNulls);
		}

	}
	
	/**
	 * Returns a NotNull mask object cast to the appropriate type.
	 * 
	 * @return a NotNull mask object cast to the appropriate type
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> Mask<T> notNull(){
		return (Mask<T>)NotNull;
	}
	
	/**
	 * Returns an IsNull mask object cast to the appropriate type.
	 * 
	 * @return an IsNull mask object cast to the appropriate type
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> Mask<T> isNull(){
		return (Mask<T>)IsNull;
	}
	
	/**
	 * Abstract implementation of a mask.
	 * 
	 * @param <S>
	 *            the supertype of the objects dealt with by this mask
	 * @author Mark A. Finlayson
	 * @version 1.0.0
	 * @since JVerbnet 1.0.0
	 */
	public static abstract class Mask<S> extends Check<S> implements IMask<S> {

		/**
		 * Creates a new mask object.
		 * 
		 * @param allowsNulls
		 *            the value to be returned by the {@link #allowsNulls()}
		 *            method
		 * @since JVerbnet 1.0.0
		 */
		public Mask(boolean allowsNulls) {
			super(allowsNulls);
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.IMask#mask(java.lang.Object, java.lang.Object)
		 */
		public <T extends S> T mask(T value, T mask) {
			return is(value) ?
					mask :
						value;
		}
		
	}
	
	/** 
	 * A mask/check with that deals with objects.  The condition is "not null".
	 *
	 * @since JVerbnet 1.0.0
	 */
	public static final Mask<Object> NotNull = new Mask<Object>(false) {
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T> boolean is(T value) {
			return value != null;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T> T check(String varName, T value) {
			if(value == null)
				throw new NullPointerException(normalizeVarName(varName) + " may not be null");
			return value;
		}
	};
	
	/** 
	 * A mask/check with that deals with objects.  The condition is "is null".
	 *
	 * @since JVerbnet 1.0.0
	 */
	public static final Mask<Object> IsNull = new Mask<Object>(true){
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T> boolean is(T value) {
			return value == null;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T> T check(String varName, T value) {
			if(value != null)
				throw new IllegalArgumentException(normalizeVarName(varName) + " must be null");
			return null;
		}
		
	};

	/**
	 * A mask/check with that deals with strings. The condition is "is empty", which
	 * means a string that is non-<code>null</code> and of length zero.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("all")
	public static final Mask<String> IsEmpty = new Mask<String>(false) {
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends String> boolean is(T value) {
			return value != null && value.length() == 0;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends String> T check(String varName, T value) {
			// null not ok
			if(value == null)
				throw new IllegalArgumentException(normalizeVarName(varName) + " must be empty (it is null)");
			// not empty not ok
			if(value.length() != 0)
				throw new IllegalArgumentException(normalizeVarName(varName) + " must be empty (its value is '" + value + "')");
			return value;
		}

	};
	
	/**
	 * A mask/check with that deals with strings. The condition is "is blank",
	 * which means a string that is non-<code>null</code>, greater than length
	 * zero, and all whitespace.
	 * 
	 * If the {@link ICheck#check(String, Object)} method returns, it returns
	 * the original string.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("all")
	public static final Mask<String> IsBlank = new Mask<String>(false) {
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends String> boolean is(T value) {
			return value != null && 
					value.length() > 0 && 
					value.trim().length() == 0;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends String> T check(String varName, T value) {
			// null not ok
			if(value == null)
				throw new IllegalArgumentException(normalizeVarName(varName) + " must be all whitespace (it is null)");
			// empty not ok
			if(value.length() == 0)
				throw new IllegalArgumentException(normalizeVarName(varName) + " must be all whitespace (it is empty)");
			// non-whitespace characters not ok
			if(value.trim().length() != 0)
				throw new IllegalArgumentException(normalizeVarName(varName) + " must be all whitespace (its value is '" + value + "')");
			return value;
		}

	};
	
	/**
	 * A mask/check with that deals with strings. The condition is "is null or empty",
	 * which means a string that is <code>null</code> or of length zero.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("all")
	public static final Mask<String> IsNullOrEmpty = new Mask<String>(true) {
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends String> boolean is(T value) {
			return value == null || 
					value.length() == 0;
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends String> T check(String varName, T value) {
			// null ok
			if(value == null)
				return value;
			// empty ok
			if(value.length() == 0)
				return value;
			// all whitespace-specified message
			if(value.trim().length() == 0)
				throw new IllegalArgumentException(normalizeVarName(varName) + " must be null or empty (it is all whitespace");
			// other strings message
			throw new IllegalArgumentException(normalizeVarName(varName) + " must be null or empty (its value is '" + value + "')");
		}

	};
	
	/**
	 * A mask/check with that deals with strings. The condition is
	 * "is null or blank", which means a string that is <code>null</code> or
	 * greater than length zero and contains only whitespace.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("all")
	public static final Mask<String> IsNullOrBlank = new Mask<String>(true) {
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends String> boolean is(T value) {
			return value == null ||
					(value.length() != 0 && 
					value.trim().length() == 0);
		}
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends String> T check(String varName, T value) {
			// null ok
			if(value == null)
				return value;
			if(value.length() == 0)
				throw new IllegalArgumentException(normalizeVarName(varName) + " must be null or all whitespace (it is empty)");
			// non-whitespace characters not ok
			if(value.trim().length() != 0)
				throw new IllegalArgumentException(normalizeVarName(varName) + " must be null or all whitespace (its value is '" + value + "')");
			return value;
		}

	};
	
	/**
	 * A mask/check with that deals with strings. The condition is
	 * "is empty or blank", which means a string that is of length zero or of
	 * greater than length zero and contains only whitespace.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("all")
	public static final Mask<String> IsEmptyOrBlank = new Mask<String>(false) {
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends String> boolean is(T value) {
			if(value == null)
				return false;
			if(value.length() == 0)
				return true;
			if(value.trim().length() == 0)
				return true;
			return false;
		}
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends String> T check(String varName, T value) {
			// null not ok
			if(value == null)
				throw new IllegalArgumentException(normalizeVarName(varName) + " must be empty or all whitespace (it is null)");
			// non-whitespace characters not ok
			if(value.trim().length() != 0)
				throw new IllegalArgumentException(normalizeVarName(varName) + " must be empty or all whitespace (its value is '" + value + "')");
			return value;
		}

	};
	
	/**
	 * A mask/check with that deals with strings. The condition is
	 * "is null, empty or blank", which means a string that is null; is of
	 * length zero; or of greater than length zero and contains only whitespace.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("all")
	public static final Mask<String> IsNullEmptyOrBlank = new Mask<String>(true) {
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends String> boolean is(T value) {
			if(value == null)
				return true;
			if(value.trim().length() == 0)
				return true;
			return false;
		}
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends String> T check(String varName, T value) {
			// null ok
			if(value == null)
				return value;
			// empty or blank ok
			if(value.trim().length() == 0)
				return value;
			throw new IllegalArgumentException(normalizeVarName(varName) + " must be null, empty or all whitespace (its value is '" + value + "')");
		}

	};

	/**
	 * A mask/check with that deals with strings. The condition is "not empty",
	 * which means a string that is not of length zero.
	 * 
	 * If the {@link ICheck#check(String, Object)} method returns, it returns a
	 * trimmed version of the original string, or <code>null</code> if the
	 * original value was <code>null</code>.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("all")
	public static final Mask<String> NotEmpty = new Mask<String>(true) {
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends String> boolean is(T value) {
			return !IsEmpty.is(value);
		}
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends String> T check(String varName, T value) {
			// null is ok
			if(value == null)
				return value;
			// any characters at all is ok
			if(value.length() != 0)
				return (T)value.trim();
			throw new IllegalArgumentException(normalizeVarName(varName) + " may not be empty");
		}

	};
	
	/**
	 * A mask/check with that deals with strings. The condition is "not blank",
	 * which means a string that is not of length greater than zero and contains
	 * only whitespace.
	 * 
	 * If the {@link ICheck#check(String, Object)} method returns, it returns a
	 * trimmed version of the original string, or <code>null</code> if the
	 * original value was <code>null</code>.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("all")
	public static final Mask<String> NotBlank = new Mask<String>(true) {
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends String> boolean is(T value) {
			return !IsBlank.is(value);
		}
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends String> T check(String varName, T value) {
			// null is ok
			if(value == null)
				return value;
			// empty is ok
			if(value.length() == 0)
				return value;
			// non-blank is ok
			value = (T)value.trim();
			if(value.length() != 0)
				return value;
			throw new IllegalArgumentException(normalizeVarName(varName) + " may not be all whitespace");

		}

	};
	
	/**
	 * A mask/check with that deals with strings. The condition is "not blank",
	 * which means a string that is not of length greater than zero and contains
	 * only whitespace.
	 * 
	 * If the {@link ICheck#check(String, Object)} method returns, it returns a
	 * trimmed version of the original string.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("all")
	public static final Mask<String> NotNullOrEmpty = new Mask<String>(false) {
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends String> boolean is(T value) {
			return !IsNullOrEmpty.is(value);
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends String> T check(String varName, T value) {
			// null not ok
			if(value == null)
				throw new IllegalArgumentException(normalizeVarName(varName) + " may not be null or empty (it is null)");
			// empty not ok
			if(value.length() == 0)
				throw new IllegalArgumentException(normalizeVarName(varName) + " may not be null or empty (it is empty)");
			return (T)value.trim();
		}

	};
	
	/**
	 * A mask/check with that deals with strings. The condition is "not blank",
	 * which means a string that is not of length greater than zero and contains
	 * only whitespace.
	 * 
	 * If the {@link ICheck#check(String, Object)} method returns, it returns a
	 * trimmed version of the original string.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("all")
	public static final Mask<String> NotNullOrBlank = new Mask<String>(false) {
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends String> boolean is(T value) {
			return !IsNullOrBlank.is(value);
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends String> T check(String varName, T value) {
			// null not ok
			if(value == null)
				throw new IllegalArgumentException(normalizeVarName(varName) + " may not be null or all whitespace (it is null)");
			String trimmed = value.trim();
			// blank not ok
			if(value.length() != 0 && trimmed.length() == 0)
				throw new IllegalArgumentException(normalizeVarName(varName) + " may not be null or all whitespace (it is " + (value == null ? "null" : "empty") + ")");
			return (T)trimmed;
		}
	};
	
	/**
	 * A mask/check with that deals with strings. The condition is "not blank",
	 * which means a string that is not of length greater than zero and contains
	 * only whitespace.
	 * 
	 * If the {@link ICheck#check(String, Object)} method returns, it returns a
	 * trimmed version of the original string, or <code>null</code> if the
	 * original value was <code>null</code>.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("all")
	public static final Mask<String> NotEmptyOrBlank = new Mask<String> (true) {
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends String> boolean is(T value) {
			return !IsEmptyOrBlank.is(value);
		}
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends String> T check(String varName, T value) {
			// null is ok
			if(value == null)
				return value;
			// empty not ok
			if(value.length() == 0)
				throw new IllegalArgumentException(normalizeVarName(varName) + " may not be empty or all whitespace (it is empty)");
			// blank not ok
			String trimmed = value.trim();
			if(trimmed.length() == 0)
				throw new IllegalArgumentException(normalizeVarName(varName) + " may not be empty or all whitespace (it is all whitespace)");
			return (T)trimmed;
		}

	};
	
	/**
	 * A mask/check with that deals with strings. The condition is "not blank",
	 * which means a string that is not of length greater than zero and contains
	 * only whitespace.
	 * 
	 * If the {@link ICheck#check(String, Object)} method returns, it returns a
	 * trimmed version of the original string, or <code>null</code> if the
	 * original value was <code>null</code>.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	@SuppressWarnings("all")
	public static final Mask<String> NotNullEmptyOrBlank = new Mask<String>(false) {
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends String> boolean is(T value) {
			return !IsNullEmptyOrBlank.is(value);
		}

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends String> T check(String varName, T value) {
			varName = normalizeVarName(varName);
			if(value == null)
				throw new IllegalArgumentException(varName + " may not be empty or all whitespace (it is null)");
			if(value.length() == 0)
				throw new IllegalArgumentException(varName + " may not be empty or all whitespace (it is empty)");
			value = (T)value.trim();
			if(value.length() == 0)
				throw new IllegalArgumentException(varName + " may not be empty or all whitespace (it is all whitespace)");
			return value;
		}

	};
	
	/**
	 * Trims the string if it matches the condition.
	 * 
	 * @param test
	 *            the condition to test; if the condition object is null, the
	 *            {@link #NotNull} object is used.
	 * @param value
	 *            the string to test
	 * @return a trimmed version of the string if the condition is met (or
	 *         <code>null</code>, if the original string was <code>null</code>);
	 *         otherwise the original object
	 * @since JVerbnet 1.0.0
	 */
	public static String trim(ICondition<Object> test, String value){
		if(test == null)
			test = notNull();
		if(test.allowsNulls())
			throw new IllegalArgumentException("The trim method may not use a condition that allow null");
		return test.is(value) ?
				value.trim() :
					value;
	}

	/**
	 * First trims the string if it matches the condition, then applies the mask
	 * to the resulting value.
	 * 
	 * @param test
	 *            the condition to test; if the condition object is null, the
	 *            {@link #NotNull} object is used.
	 * @param value
	 *            the string to test
	 * @param mask
	 *            the mask to apply to the trimmed string
	 * @param maskValue
	 *            the mask value to substitute if necessary
	 * @return a trimmed and masked version of the original string
	 * @since JVerbnet 1.0.0
	 */
	public static String trimAndMask(ICondition<Object> test, String value, IMask<String> mask, String maskValue){
		if(test == null)
			test = notNull();
		if(mask == null)
			mask = isNull();
		String trim = trim(test, value);
		return mask.mask(trim, maskValue);
	}

	/**
	 * Convenience implementation of the most common use case of the
	 * {@link #trimAndMask(ICondition, String, IMask, String)} method.
	 * 
	 * @param value
	 *            the value to be trimmed then masked
	 * @param maskValue
	 *            the mask value
	 * @return a trimmed version of the string, or the empty string if the
	 *         string was <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public static String trimAndMaskNull(String value, String maskValue){
		return trimAndMask(null, value, null, maskValue);
	}


	/**
	 * A mask/check object that deals with collections. The condition is
	 * "empty", meaning non-<code>null</code> and of size zero.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public static final Mask<Collection<?>> IsEmptyCol = new Mask<Collection<?>>(true){
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends Collection<?>> boolean is(T value) {
			if(value == null)
				return false;
			return value.isEmpty();
		}
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends Collection<?>> T check(String varName, T value) {
			if(value == null)
				throw new IllegalArgumentException(normalizeVarName(varName) + " collection must not be null");
			if(!value.isEmpty())
				throw new IllegalArgumentException(normalizeVarName(varName) + " collection must be empty (it has " + value.size() + " elements)");
			return value;
		}
	};
	
	/**
	 * A mask/check object that deals with collections. The condition is
	 * "null or empty", meaning the collection must be <code>null</code> or of
	 * size zero.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public static final Mask<Collection<?>> IsNullOrEmptyCol = new Mask<Collection<?>> (false){
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends Collection<?>> boolean is(T value) {
			if(value == null)
				return true;
			if(value.isEmpty())
				return true;
			return false;
		}
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends Collection<?>> T check(String varName, T value) {
			if(!is(value))
				throw new IllegalArgumentException(normalizeVarName(varName) + " collection must be null or empty (it has " + value.size() + " elements)");
			return value;
		}
	};
		
	/**
	 * A mask/check object that deals with collections. The condition is
	 * "not empty", meaning of not of size zero. A <code>null</code> collection
	 * is not considered of size zero.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public static final Mask<Collection<?>> NotEmptyCol = new Mask<Collection<?>>(true){
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends Collection<?>> boolean is(T value) {
			return !IsEmptyCol.is(value);
		}
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends Collection<?>> T check(String varName, T value) {
			if(is(value))
				throw new IllegalArgumentException("The collection may not be empty");
			return value;
		}
	};
	
	/**
	 * A mask/check object that deals with collections. The condition is
	 * "not null or empty", meaning the collection must be not <code>null</code>
	 * and have at least one element.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public static final Mask<Collection<?>> NotNullOrEmptyCol = new Mask<Collection<?>> (false){
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICondition#is(java.lang.Object)
		 */
		public <T extends Collection<?>> boolean is(T value) {
			return !IsNullOrEmptyCol.is(value);
		}
		
		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.util.Checks.ICheck#check(java.lang.String, java.lang.Object)
		 */
		public <T extends Collection<?>> T check(String varName, T value) {
			if(value == null)
				throw new IllegalArgumentException(normalizeVarName(varName) + " collection may not be null or empty (it is null)");
			if(value.isEmpty())
				throw new IllegalArgumentException(normalizeVarName(varName) + " collection may not be null or empty (it is empty)");
			return value;
		}
	};
	
	// internal implementation for mask object
	private static class IsMinSizeAndNotNull extends Mask<Collection<?>> {
		
		private final int minSize;
		
		private IsMinSizeAndNotNull(int minSize){
			super(false);
			if(minSize < 0)
				minSize = 0;
			this.minSize = minSize;
		}

		public <T extends Collection<?>> boolean is(T value) {
			if(value == null)
				return false;
			return value.size() >= minSize;
		}

		public <T extends Collection<?>> T check(String varName, T value) {
			return null;
		}
	}
	
	// internal implementation for mask object
	private static class IsMaxSizeAndNotNull extends Mask<Collection<?>> {
		
		private final int maxSize;
		
		private IsMaxSizeAndNotNull(int maxSize){
			super(false);
			if(maxSize < 0)
				maxSize = 0;
			this.maxSize = maxSize;
		}

		public <T extends Collection<?>> boolean is(T value) {
			if(value == null)
				return false;
			return value.size() <= maxSize;
		}

		public <T extends Collection<?>> T check(String varName, T value) {
			return null;
		}
	}
	
	// keep only this many IsMinSize or IsMaxSize objects around
	private static final int MAX_ENTRIES = 128;
	
	// map for storing instances of IsMinSize
	@SuppressWarnings("serial")
	private static final Map<Integer, IsMinSizeAndNotNull> minSizeMap = new LinkedHashMap<Integer, IsMinSizeAndNotNull>(16, 0.75f, true) {
		protected boolean removeEldestEntry(Map.Entry<Integer, IsMinSizeAndNotNull> eldest) {
			return size() > MAX_ENTRIES;
		}
	};
	
	// map for storing instances of IsMaxSize
	@SuppressWarnings("serial")
	private static final Map<Integer, IsMaxSizeAndNotNull> maxSizeMap = new LinkedHashMap<Integer, IsMaxSizeAndNotNull>(16, 0.75f, true) {
		protected boolean removeEldestEntry(Map.Entry<Integer, IsMaxSizeAndNotNull> eldest) {
			return size() > MAX_ENTRIES;
		}
	};
	
	/**
	 * Returns a mask object whose condition is that the collection be non-
	 * <code>null</code> and have a size equal to or greater than the specified
	 * size.
	 * 
	 * @param size
	 *            the minimum size of the collection
	 * @return a mask object whose condition is that the collection be non-
	 *         <code>null</code> and have a size equal to or greater than the
	 *         specified size
	 * @since JVerbnet 1.0.0
	 */
	public static final Mask<Collection<?>> minSizeAndNotNull(int size){
		IsMinSizeAndNotNull result = minSizeMap.get(size);
		if(result == null){
			result = new IsMinSizeAndNotNull(size);
			minSizeMap.put(size, result);
		}
		return result;
	}
	
	/**
	 * Returns a mask object whose condition is that the collection be non-
	 * <code>null</code> and have a size less than or equal to the specified
	 * size.
	 * 
	 * @param size
	 *            the maximum size of the collection
	 * @return a mask object whose condition is that the collection be non-
	 *         <code>null</code> and have a size less than or equal to the
	 *         specified size
	 * @since JVerbnet 1.0.0
	 */
	public static final Mask<Collection<?>> maxSizeAndNotNull(int size){
		IsMaxSizeAndNotNull result = maxSizeMap.get(size);
		if(result == null){
			result = new IsMaxSizeAndNotNull(size);
			maxSizeMap.put(size, result);
		}
		return result;
	}

	/** 
	 * A reallocation strategy that does nothing.
	 *
	 * @since JVerbnet 1.0.0
	 */
	public static final IReallocationStrategy DoNotReallocate = new IReallocationStrategy() {
		
		public <T> List<T> reallocate(List<T> list) {
			return list;
		}
		
		public <T> Set<T> reallocate(Set<T> set) {
			return set;
		}
	
		public <T> SortedSet<T> reallocate(SortedSet<T> set) {
			return set;
		}
	
		public <K, V> Map<K, V> reallocate(Map<K, V> map) {
			return map;
		}
	
		public <K, V> SortedMap<K, V> reallocate(SortedMap<K, V> map) {
			return map;
		}
	};

	/**
	 * Reallocates collections as modifiable collections. <code>null</code>
	 * collections are passed back as <code>null</code>s.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public static final IReallocationStrategy ModifiableLeaveNulls = new IReallocationStrategy(){
		
		public <T> List<T> reallocate(List<T> list) {
			if(list == null)
				return null;
			return new ArrayList<T>(list);
		}
	
		public <T> Set<T> reallocate(Set<T> set) {
			if(set == null)
				return null;
			if(set instanceof LinkedHashSet)
				return new LinkedHashSet<T>(set);
			return new HashSet<T>(set);
		}
	
		public <T> SortedSet<T> reallocate(SortedSet<T> set) {
			if(set == null)
				return null;
			return new TreeSet<T>(set);
		}
	
		public <K, V> Map<K, V> reallocate(Map<K, V> map) {
			if(map == null)
				return null;
			if(map instanceof LinkedHashMap)
				return new LinkedHashMap<K,V>(map);
			return new HashMap<K,V>(map);
		}
	
		public <K, V> SortedMap<K, V> reallocate(SortedMap<K, V> map) {
			if(map == null)
				return null;
			return new TreeMap<K,V>(map);
		}
	};

	/**
	 * Reallocates collections as unmodifiable collections. <code>null</code>
	 * collections are passed back as <code>null</code>s.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public static final IReallocationStrategy UnmodifiableLeaveNulls = new IReallocationStrategy() {
		
		public <T> List<T> reallocate(List<T> list) {
			if(list == null)
				return null;
			if(list.isEmpty())
				return Collections.emptyList();
			if(list.size() == 1)
				return Collections.singletonList(list.iterator().next());
			return Collections.unmodifiableList(new ArrayList<T>(list));
		}
	
		public <T> Set<T> reallocate(Set<T> set) {
			if(set == null)
				return null;
			if(set.isEmpty())
				return Collections.emptySet();
			if(set.size() == 1)
				return Collections.singleton(set.iterator().next());
			if(set instanceof LinkedHashSet)
				return Collections.unmodifiableSet(new LinkedHashSet<T>(set));
			return Collections.unmodifiableSet(new HashSet<T>(set));
		}
	
		public <T> SortedSet<T> reallocate(SortedSet<T> set) {
			if(set == null)
				return null;
			return Collections.unmodifiableSortedSet(new TreeSet<T>(set));
		}
	
		public <K, V> Map<K, V> reallocate(Map<K, V> map) {
			if(map == null)
				return null;
			if(map.isEmpty())
				return Collections.emptyMap();
			if(map.size() == 1){
				Entry<K,V> e = map.entrySet().iterator().next();
				return Collections.singletonMap(e.getKey(), e.getValue());
			}
			if(map instanceof LinkedHashMap)
				return Collections.unmodifiableMap(new LinkedHashMap<K,V>(map));
			return Collections.unmodifiableMap(new HashMap<K,V>(map));
		}
	
		public <K, V> SortedMap<K, V> reallocate(SortedMap<K, V> map) {
			if(map == null)
				return null;
			return Collections.unmodifiableSortedMap(new TreeMap<K,V>(map));
		}
	};

	/**
	 * Reallocates collections as modifiable collections. <code>null</code>
	 * collections are passed back as modifiable empty collections.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public static final IReallocationStrategy ModifiableMaskNullWithEmpty = new IReallocationStrategy() {
		
		public <T> List<T> reallocate(List<T> list) {
			if(list == null)
				return new ArrayList<T>();
			return new ArrayList<T>(list);
		}
	
		public <T> Set<T> reallocate(Set<T> set) {
			if(set == null)
				return new HashSet<T>();
			if(set instanceof LinkedHashSet)
				return new LinkedHashSet<T>(set);
			return new HashSet<T>(set);
		}
	
		public <T> SortedSet<T> reallocate(SortedSet<T> set) {
			if(set == null)
				return new TreeSet<T>();
			return new TreeSet<T>(set);
		}
	
		public <K, V> Map<K, V> reallocate(Map<K, V> map) {
			if(map == null)
				return new HashMap<K,V>();
			if(map instanceof LinkedHashMap)
				return new LinkedHashMap<K,V>(map);
			return new HashMap<K,V>(map);
		}
	
		public <K, V> SortedMap<K, V> reallocate(SortedMap<K, V> map) {
			if(map == null)
				return new TreeMap<K,V>();
			return new TreeMap<K,V>(map);
		}
	};

	/**
	 * Reallocates collections as unmodifiable collections. <code>null</code>
	 * collections are passed back as unmodifiable empty collections.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public static final IReallocationStrategy UnmodifiableMaskNullWithEmpty = new IReallocationStrategy() {
		
		public <T> List<T> reallocate(List<T> list) {
			if(list == null || list.isEmpty())
				return Collections.emptyList();
			if(list.size() == 1)
				return Collections.singletonList(list.iterator().next());
			return Collections.unmodifiableList(new ArrayList<T>(list));
		}
	
		public <T> Set<T> reallocate(Set<T> set) {
			if(set == null || set.isEmpty())
				return Collections.emptySet();
			if(set.size() == 1)
				return Collections.singleton(set.iterator().next());
			if(set instanceof LinkedHashSet)
				return Collections.unmodifiableSet(new LinkedHashSet<T>(set));
			return Collections.unmodifiableSet(new HashSet<T>(set));
		}
	
		public <T> SortedSet<T> reallocate(SortedSet<T> set) {
			if(set == null)
				Collections.unmodifiableSortedSet(new TreeSet<T>());
			return Collections.unmodifiableSortedSet(new TreeSet<T>(set));
		}
	
		public <K, V> Map<K, V> reallocate(Map<K, V> map) {
			if(map == null || map.isEmpty())
				return Collections.emptyMap();
			if(map.size() == 1){
				Entry<K,V> e = map.entrySet().iterator().next();
				return Collections.singletonMap(e.getKey(), e.getValue());
			}
			if(map instanceof LinkedHashMap)
				return Collections.unmodifiableMap(new LinkedHashMap<K,V>(map));
			return Collections.unmodifiableMap(new HashMap<K,V>(map));
		}
	
		public <K, V> SortedMap<K, V> reallocate(SortedMap<K, V> map) {
			if(map == null)
				return Collections.unmodifiableSortedMap(new TreeMap<K,V>());
			return Collections.unmodifiableSortedMap(new TreeMap<K,V>(map));
		}
	};

	

	// 
	// Collection methods
	//

	/**
	 * Runs the check object over all elements in the list. Returns a list
	 * reallocated according to the specified reallocation strategy. The
	 * strategy may be <code>null</code>, meaning the {@link #DoNotReallocate}
	 * strategy is used.
	 * 
	 * @param test
	 *            the test to be applied
	 * @param varName
	 *            the variable name to be used in the exception message; if the
	 *            name is <code>null</code>, empty, or all whitespace, the
	 *            generic variable name is used.
	 * @param list
	 *            the list to check
	 * @param r
	 *            the reallocation strategy to use; if <code>null</code>, the
	 *            DoNotReallocate strategy is used
	 * @return the reallocated list
	 * @since JVerbnet 1.0.0
	 */
	public static <S,E extends S> List<E> allElementsAre(ICheck<S> test, String varName, List<E> list, IReallocationStrategy r) {
		if(r == null)
			r = DoNotReallocate;
		String eName = "element of " + varName;
		if(list != null)
			for(E e : list)
				test.check(eName, e);
		return r.reallocate(list);
	}
	
	/**
	 * Runs the check object over all elements in the set. Returns a set
	 * reallocated according to the specified reallocation strategy. The
	 * strategy may be <code>null</code>, meaning the {@link #DoNotReallocate}
	 * strategy is used. 
	 * 
	 * @param test
	 *            the test to be applied
	 * @param varName
	 *            the variable name to be used in the exception message; if the
	 *            name is <code>null</code>, empty, or all whitespace, the
	 *            generic variable name is used.
	 * @param set
	 *            the set to check
	 * @param r
	 *            the reallocation strategy to use; if <code>null</code>, the
	 *            DoNotReallocate strategy is used
	 * @return the reallocated set
	 * @since JVerbnet 1.0.0
	 */
	public static <S,E extends S> Set<E> allElementsAre(ICheck<S> test, String varName, Set<E> set, IReallocationStrategy r) {
		if(r == null)
			r = DoNotReallocate;
		String eName = "element of " + varName;
		if(set != null)
			for(E e : set)
				test.check(eName, e);
		return r.reallocate(set);
	}
	
	/**
	 * Runs the check object over all elements in the set. Returns a set
	 * reallocated according to the specified reallocation strategy. The
	 * strategy may be <code>null</code>, meaning the {@link #DoNotReallocate}
	 * strategy is used. 
	 * 
	 * @param test
	 *            the test to be applied
	 * @param varName
	 *            the variable name to be used in the exception message; if the
	 *            name is <code>null</code>, empty, or all whitespace, the
	 *            generic variable name is used.
	 * @param set
	 *            the set to check
	 * @param r
	 *            the reallocation strategy to use; if <code>null</code>, the
	 *            DoNotReallocate strategy is used
	 * @return the reallocated set
	 * @since JVerbnet 1.0.0
	 */
	public static <S,E extends S> SortedSet<E> allElementsAre(ICheck<S> test, String varName, SortedSet<E> set, IReallocationStrategy r) {
		if(r == null)
			r = DoNotReallocate;
		String eName = "element of " + varName;
		if(set != null)
			for(E e : set)
				test.check(eName, e);
		return r.reallocate(set);
	}
	
	/**
	 * Runs the check object over all keys in the map. Returns a map
	 * reallocated according to the specified reallocation strategy. The
	 * strategy may be <code>null</code>, meaning the {@link #DoNotReallocate}
	 * strategy is used.
	 * 
	 * @param test
	 *            the test to be applied
	 * @param varName
	 *            the variable name to be used in the exception message; if the
	 *            name is <code>null</code>, empty, or all whitespace, the
	 *            generic variable name is used.
	 * @param map
	 *            the map to check
	 * @param r
	 *            the reallocation strategy to use; if <code>null</code>, the
	 *            DoNotReallocate strategy is used
	 * @return the reallocated set
	 * @since JVerbnet 1.0.0
	 */
	public static <S,K extends S,V> Map<K,V> allKeysAre(ICheck<S> test, String varName, Map<K,V> map, IReallocationStrategy r) {
		if(r == null)
			r = DoNotReallocate;
		if(map != null){
			String kName = "key of " + varName;
			for(Entry<K,V> e : map.entrySet())
				test.check(kName, e.getKey());
		}
		return r.reallocate(map);
	}
	
	/**
	 * Runs the check object over all keys in the map. Returns a map
	 * reallocated according to the specified reallocation strategy. The
	 * strategy may be <code>null</code>, meaning the {@link #DoNotReallocate}
	 * strategy is used.
	 * 
	 * @param test
	 *            the test to be applied
	 * @param varName
	 *            the variable name to be used in the exception message; if the
	 *            name is <code>null</code>, empty, or all whitespace, the
	 *            generic variable name is used.
	 * @param map
	 *            the map to check
	 * @param r
	 *            the reallocation strategy to use; if <code>null</code>, the
	 *            DoNotReallocate strategy is used
	 * @return the reallocated set
	 * @since JVerbnet 1.0.0
	 */
	public static <S,K extends S,V> SortedMap<K,V> allKeysAre(ICheck<S> test, String varName, SortedMap<K,V> map, IReallocationStrategy r) {
		if(r == null)
			r = DoNotReallocate;
		if(map != null){
			String kName = "key of " + varName;
			for(Entry<K,V> e : map.entrySet())
				test.check(kName, e.getKey());
		}
		return r.reallocate(map);
	}
	
	/**
	 * Runs the check object over all values in the map. Returns a map
	 * reallocated according to the specified reallocation strategy. The
	 * strategy may be <code>null</code>, meaning the {@link #DoNotReallocate}
	 * strategy is used.
	 * 
	 * @param test
	 *            the test to be applied
	 * @param varName
	 *            the variable name to be used in the exception message; if the
	 *            name is <code>null</code>, empty, or all whitespace, the
	 *            generic variable name is used.
	 * @param map
	 *            the map to check
	 * @param r
	 *            the reallocation strategy to use; if <code>null</code>, the
	 *            DoNotReallocate strategy is used
	 * @return the reallocated set
	 * @since JVerbnet 1.0.0
	 */
	public static <S,K,V extends S> Map<K,V> allValuesAre(ICheck<S> test, String varName, Map<K,V> map, IReallocationStrategy r) {
		if(r == null)
			r = DoNotReallocate;
		if(map != null){
			String vName = "value of " + varName;
			for(Entry<K,V> e : map.entrySet())
				test.check(vName, e.getValue());
		}
		return r.reallocate(map);
	}
	
	/**
	 * Runs the check object over all values in the map. Returns a map
	 * reallocated according to the specified reallocation strategy. The
	 * strategy may be <code>null</code>, meaning the {@link #DoNotReallocate}
	 * strategy is used.
	 * 
	 * @param test
	 *            the test to be applied
	 * @param varName
	 *            the variable name to be used in the exception message; if the
	 *            name is <code>null</code>, empty, or all whitespace, the
	 *            generic variable name is used.
	 * @param map
	 *            the map to check
	 * @param r
	 *            the reallocation strategy to use; if <code>null</code>, the
	 *            DoNotReallocate strategy is used
	 * @return the reallocated set
	 * @since JVerbnet 1.0.0
	 */
	public static <S,K,V extends S> SortedMap<K,V> allValuesAre(ICheck<V> test, String varName, SortedMap<K,V> map, IReallocationStrategy r) {
		if(r == null)
			r = DoNotReallocate;
		if(map != null){
			String vName = "value of " + varName;
			for(Entry<K,V> e : map.entrySet())
				test.check(vName, e.getValue());
		}
		return r.reallocate(map);
	}
	
	
	/**
	 * Runs the check object over all keys and values in the map. Returns a map
	 * reallocated according to the specified reallocation strategy. The
	 * strategy may be <code>null</code>, meaning the {@link #DoNotReallocate}
	 * strategy is used.
	 * 
	 * @param test
	 *            the test to be applied
	 * @param varName
	 *            the variable name to be used in the exception message; if the
	 *            name is <code>null</code>, empty, or all whitespace, the
	 *            generic variable name is used.
	 * @param map
	 *            the map to check
	 * @param r
	 *            the reallocation strategy to use; if <code>null</code>, the
	 *            DoNotReallocate strategy is used
	 * @return the reallocated set
	 * @since JVerbnet 1.0.0
	 */
	public static <S,K extends S,V extends S> Map<K,V> allKeysAndValuesAre(ICheck<S> test, String varName, Map<K,V> map, IReallocationStrategy r) {
		if(r == null)
			r = DoNotReallocate;
		if(map != null){
			String kName = "key of " + varName;
			String vName = "value of " + varName;
			for(Entry<K,V> e : map.entrySet()){
				test.check(kName, e.getKey());
				test.check(vName, e.getValue());
			}
		}
		return r.reallocate(map);
	}
	
	/**
	 * Runs the check object over all keys and values in the map. Returns a map
	 * reallocated according to the specified reallocation strategy. The
	 * strategy may be <code>null</code>, meaning the {@link #DoNotReallocate}
	 * strategy is used.
	 * 
	 * @param test
	 *            the test to be applied
	 * @param varName
	 *            the variable name to be used in the exception message; if the
	 *            name is <code>null</code>, empty, or all whitespace, the
	 *            generic variable name is used.
	 * @param map
	 *            the map to check
	 * @param r
	 *            the reallocation strategy to use; if <code>null</code>, the
	 *            DoNotReallocate strategy is used
	 * @return the reallocated set
	 * @since JVerbnet 1.0.0
	 */
	public static <S,K extends S,V extends S> SortedMap<K,V> allKeysAndValuesAre(ICheck<S> test, String varName, SortedMap<K,V> map, IReallocationStrategy r) {
		if(r == null)
			r = DoNotReallocate;
		if(map != null){
			String kName = "key of " + varName;
			String vName = "value of " + varName;
			for(Entry<K,V> e : map.entrySet()){
				test.check(kName, e.getKey());
				test.check(vName, e.getValue());
			}
		}
		return r.reallocate(map);
	}
	
	/**
	 * Throws an exception if called. The exception message includes a pointer
	 * to the source code where the method was invoked.
	 * 
	 * @throws IllegalStateException
	 *             if this method is called
	 * @since JVerbnet 1.0.0
	 */
	public static <T> T thisMethodShouldNeverBeCalled() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement target = null;
		for(StackTraceElement e : stackTrace){
			if(Thread.class.getName().equals(e.getClassName()))
				continue;
			if(Check.class.getName().equals(e.getClassName()))
				continue;
			target = e;
			break;
		}
		throw new IllegalStateException("This method should never be called: " + (target == null ? "unknown" : target));
	}
	
	/**
	 * Throws an exception if called. The exception message includes a pointer
	 * to the source code where the method was invoked.
	 * 
	 * @throws IllegalStateException
	 *             if this method is called
	 * @since JVerbnet 1.0.0
	 */
	public static <T> T thisLineShouldNeverBeCalled() throws IllegalStateException {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement target = null;
		for(StackTraceElement e : stackTrace){
			if(Thread.class.getName().equals(e.getClassName()))
				continue;
			if(Check.class.getName().equals(e.getClassName()))
				continue;
			target = e;
			break;
		}
		throw new IllegalStateException("This line should never be executed: " + (target == null ? "unknown" : target));
	}
	
}
