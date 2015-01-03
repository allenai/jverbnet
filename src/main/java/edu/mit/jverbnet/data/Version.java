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

import static edu.mit.jverbnet.util.Checks.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default, concrete implementation of the {@link IVersion} interface. This
 * class, much like the {@link Integer} class, caches instances, which should be
 * created via the {@code createVersion} methods.
 * <p>
 * This version object takes an optional bugfix version number and string qualifier.
 * The qualifier may only contain characters are that are valid Java 
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class Version implements IVersion {
	
	// only create one instance of any version
	private static final Map<Integer, Version> versionCache = new HashMap<Integer, Version>();
	
	// Colorado Verbnet versions
	public static final Version ver31  = getVersion(3,1,0); 
	public static final Version ver32  = getVersion(3,2,0); 
	
	/** 
	 * Regular expression pattern for matching a version string.
	 *
	 * @since JVerbnet 1.1.0
	 */
	public static final Pattern regex = Pattern.compile("(\\d+)\\.(\\d+)(.(\\d+)(.\\p{javaJavaIdentifierPart}+)?)?");

	// final instance fields
	private final int major;
	private final int minor;
	private final int bugfix;
	private final String qualifier;
	private transient String toString;

	/**
	 * Creates a new version object with the specified version numbers.
	 * <p>
	 * Clients should normally obtain instances of this class via the static
	 * {@code getVersion} methods.
	 * 
	 * @param major
	 *            the major version number, i.e., the '1' in 1.2.3
	 * @param minor
	 *            the minor version number, i.e., the '2' in 1.2.3
	 * @param bugfix
	 *            the bugfix version number, i.e., the '3' in 1.2.3
	 * @throws IllegalArgumentException
	 *             if any of the version numbers are negative
	 * @since JVerbnet 1.0.0
	 */
	public Version(int major, int minor, int bugfix){
		this(major, minor, bugfix, null);
	}
	
	/**
	 * Creates a new version object with the specified version numbers.
	 * <p>
	 * Clients should normally obtain instances of this class via the static
	 * {@code getVersion} methods.
	 * 
	 * @param major
	 *            the major version number, i.e., the '1' in 1.2.3.q
	 * @param minor
	 *            the minor version number, i.e., the '2' in 1.2.3.q
	 * @param bugfix
	 *            the bugfix version number, i.e., the '3' in 1.2.3.q
	 * @param qualifier
	 *            the version qualifier, i.e., the 'q' in 1.2.3.q
	 * @throws IllegalArgumentException
	 *             if any of the version numbers are negative, or the qualifier
	 *             is not a legal qualifier
	 * @since JVerbnet 1.0.0
	 */
	public Version(int major, int minor, int bugfix, String qualifier){
		qualifier = checkVersion(major, minor, bugfix, qualifier);
		
		// field assignments
		this.major = major;
		this.minor = minor;
		this.bugfix = bugfix;
		this.qualifier = qualifier;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVersion#getMajorVersion()
	 */
	public int getMajorVersion() {
		return major;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVersion#getMinorVersion()
	 */
	public int getMinorVersion() {
		return minor;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVersion#getBugfixVersion()
	 */
	public int getBugfixVersion() {
		return bugfix;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IVersion#getQualifier()
	 */
	public String getQualifier() {
		return qualifier;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return hashCode(major, minor, bugfix, qualifier);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj) 
			return true;
		if(obj == null) 
			return false;
		if(!(obj instanceof Version)) 
			return false;
		final Version other = (Version)obj;
		if(major != other.major) 
			return false;
		if(minor != other.minor) 
			return false;
		if(bugfix != other.bugfix) 
			return false;
		if(!qualifier.equals(other.qualifier))
			return false;
		return true;
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (toString == null)
			toString = makeVersionString(major, minor, bugfix, qualifier);
		return toString;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IVersion o) {
		return compare(this, o);
	}

	/**
	 * Compares the versions for order. Returns a negative integer, zero, or a
	 * positive integer as the first version is less than, equal to, or greater
	 * than the second version.
	 * 
	 * @return a negative integer, zero, or a positive integer as the first
	 *         version is less than, equal to, or greater than the second
	 *         version
	 * @since JVerbnet 1.0.0
	 */
	public static int compare(IVersion one, IVersion two) {
		NotNull.check("one", one);
		NotNull.check("two", two);
		
		int cmp;
		
		// major
		cmp = Double.compare(one.getMajorVersion(), two.getMajorVersion());
		if(cmp != 0)
			return cmp;

		// minor
		cmp = Double.compare(one.getMinorVersion(), two.getMinorVersion());
		if(cmp != 0)
			return cmp;

		// bugfix
		cmp = Double.compare(one.getBugfixVersion(), two.getBugfixVersion());
		if(cmp != 0)
			return cmp;

		// qualifier
		return one.getQualifier().compareTo(two.getQualifier());
	
	}

	/**
	 * Checks the supplied version numbers. Throws an
	 * {@link IllegalArgumentException} if they do not define a legal version.,
	 * 
	 * @param major
	 *            the major version number
	 * @param minor
	 *            the minor version number
	 * @param bugfix
	 *            the bugfix version number
	 * @param qualifier
	 *            the qualifier to check
	 * @return the <code>null</code>-masked qualifier
	 * @throws IllegalArgumentException
	 *             if the supplied arguments do not identify a legal version
	 * @since JVerbnet 1.0.0
	 */
	public static String checkVersion(int major, int minor, int bugfix, String qualifier){
		checkVersionNumber(major, minor, bugfix);
		return checkQualifier(qualifier);
	}

	/**
	 * Checks the supplied version numbers. Throws an
	 * {@link IllegalArgumentException} if the version numbers are not valid
	 * (that is, any are below zero).
	 * 
	 * @param major
	 *            the major version number
	 * @param minor
	 *            the minor version number
	 * @param bugfix
	 *            the bugfix version number
	 * @throws IllegalArgumentException
	 *             if any of the supplied numbers are negative
	 * @since JVerbnet 1.0.0
	 */
	public static void checkVersionNumber(int major, int minor, int bugfix){
		if(isIllegalVersionNumber(major, minor, bugfix)) 
			throw new IllegalArgumentException("Illegal version number: " + makeVersionString(major, minor, bugfix, null));
	}
	
	/**
	 * Checks the specified qualifier for legality. Throws an
	 * {@link IllegalArgumentException} if it is not a legal qualifier.
	 * 
	 * @param qualifier
	 *            the qualifier to check
	 * @return the <code>null</code>-masked qualifier
	 * @see #isIllegalQualifier(String)
	 * @since JVerbnet 1.0.0
	 */
	public static String checkQualifier(String qualifier) {
		if(qualifier == null)
			return "";
		if(isIllegalQualifier(qualifier)) 
			throw new IllegalArgumentException("Illegal version qualifier: " + qualifier);
		return qualifier;
	}

	/**
	 * Returns <code>true</code> if the arguments identify a legal version;
	 * <code>false</code> otherwise.
	 * 
	 * @param major
	 *            the major version number
	 * @param minor
	 *            the minor version number
	 * @param bugfix
	 *            the bugfix version number
	 * @param qualifier
	 *            the version qualifier
	 * @return <code>true</code> if the arguments identify a legal version;
	 *         <code>false</code> otherwise.
	 * @since JVerbnet 1.0.0
	 */
	public static boolean isIllegalVersion(int major, int minor, int bugfix, String qualifier){
		if(isIllegalVersionNumber(major, minor, bugfix))
			return true;
		if(isIllegalQualifier(qualifier))
			return true;
		return false;
	}
	
	/**
	 * Returns true if any of three numbers are negative
	 * 
	 * @param major
	 *            the major version number
	 * @param minor
	 *            the minor version number
	 * @param bugfix
	 *            the bugfix version number
	 * @return <code>true</code> if all the numbers are non-negative;
	 *         <code>false</code> otherwise
	 * @since JVerbnet 1.0.0
	 */
	public static boolean isIllegalVersionNumber(int major, int minor, int bugfix){
		if(major < 0)
			return true;
		if(minor < 0)
			return true;
		if(bugfix < 0)
			return true;
		return false;
	}

	/**
	 * Returns <code>false</code>if the specified qualifier is legal, namely, if
	 * the string is either the empty string, or contains only characters that
	 * are found in valid java identifiers.
	 * 
	 * @see Character#isJavaIdentifierPart(char)
	 * @param qualifier
	 *            the qualifier to check
	 * @return <code>true</code> if not a legal qualifier; <code>false</code>
	 *         otherwise
	 * @throws NullPointerException
	 *             if the specified string is <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public static boolean isIllegalQualifier(String qualifier){
		for(int i = 0; i < qualifier.length(); i++)
			if(!Character.isJavaIdentifierPart(qualifier.charAt(i)))
				return true;
		return false;
	}
	
	/**
	 * Creates and caches, or retrieves from the cache, a version object
	 * corresponding to the specified numbers.
	 * 
	 * @param major
	 *            the major version number
	 * @param minor
	 *            the minor version number
	 * @param bugfix
	 *            the bugfix version number
	 * @return the cached version object corresponding to these numbers
	 * @since JVerbnet 1.0.0
	 */
	public static Version getVersion(int major, int minor, int bugfix){
		return getVersion(major, minor, bugfix, null);
	}

	/**
	 * Creates and caches, or retrieves from the cache, a version object
	 * corresponding to the specified numbers.
	 * 
	 * @param major
	 *            the major version number
	 * @param minor
	 *            the minor version number
	 * @param bugfix
	 *            the bugfix version number
	 * @param qualifier
	 *            the version qualifier
	 * @return the cached version object corresponding to these numbers
	 * @throws IllegalArgumentException
	 *             if the version numbers and qualifier are not legal
	 * @since JVerbnet 1.0.0
	 */
	public static Version getVersion(int major, int minor, int bugfix, String qualifier){
		qualifier = checkVersion(major, minor, bugfix, qualifier);
		int hash = hashCode(major, minor, bugfix, qualifier);
		Version version = versionCache.get(hash);
		if(version == null){
			version = new Version(major, minor, bugfix, qualifier);
			versionCache.put(version.hashCode(), version);
		}
		return version;
	}

	/**
	 * Creates a version string for the specified version numbers.  If a version's
	 * bugfix number is 0, and if the qualifier is null or empty, the string produced is of the form "x.y".  I
	 * 
	 * @param major
	 *            the major version number, i.e., the '1' in 1.2.3.q
	 * @param minor
	 *            the minor version number, i.e., the '2' in 1.2.3.q
	 * @param bugfix
	 *            the bugfix version number, i.e., the '3' in 1.2.3.q
	 * @param qualifier
	 *            the version qualifier, i.e., the 'q' in 1.2.3.q
	 * @return a string representing the specified version
	 * @throws IllegalArgumentException
	 * @since JVerbnet 1.0.0
	 */
	public static String makeVersionString(int major, int minor, int bugfix, String qualifier){
		qualifier = checkQualifier(qualifier);
		boolean hasQualifier = qualifier != null && qualifier.length() > 0;
		StringBuilder sb = new StringBuilder();
		sb.append(Integer.toString(major));
		sb.append('.');
		sb.append(Integer.toString(minor));
		if(bugfix > 0 || hasQualifier){
			sb.append('.');
			sb.append(Integer.toString(bugfix));
		}
		if(hasQualifier){
			sb.append('.');
			sb.append(qualifier);
		}
		return sb.toString();
	}
	
	/**
	 * Calculates the hash code for a version object with the specified version
	 * numbers.
	 * 
	 * @param major
	 *            the major version number, i.e., the '1' in 1.2.3.q
	 * @param minor
	 *            the minor version number, i.e., the '2' in 1.2.3.q
	 * @param bugfix
	 *            the bugfix version number, i.e., the '3' in 1.2.3.q
	 * @param qualifier
	 *            the version qualifier, i.e., the 'q' in 1.2.3.q
	 * @throws IllegalArgumentException
	 *             if the specified parameters do not identify a legal version
	 * @return the hash code for the specified version
	 * @since JVerbnet 1.0.0
	 */
	public static int hashCode(int major, int minor, int bugfix, String qualifier){
		qualifier = checkVersion(major, minor, bugfix, qualifier);
		final int prime = 31;
		int result = 1;
		result = prime * result + major;
		result = prime * result + minor;
		result = prime * result + bugfix;
		result = prime * result + qualifier.hashCode();
		return result;
	}

	/**
	 * Tries to transform the specified character sequence into a version
	 * object. If it cannot, returns <code>null</code>
	 * 
	 * @param verStr
	 *            the sequence of characters to be transformed
	 * @return the version, or <code>null</code> if the character sequence is
	 *         not a valid version
	 * @since JVerbnet 1.0.0
	 */
	public static Version parseVersionProtected(CharSequence verStr){
		if(verStr == null) 
			return null;
		
		Matcher m = regex.matcher(verStr.toString().trim());
		if(!m.matches())
			return null;
		
		String majorStr = m.group(1);
		int major = Integer.parseInt(majorStr);
		
		String minorStr = m.group(2);
		int minor = Integer.parseInt(minorStr);
		
		int bugfix = 0;
		String bugfixStr = m.group(4);
		if(bugfixStr != null)
			bugfix = Integer.parseInt(bugfixStr);
		
		if(isIllegalVersionNumber(major, minor, bugfix))
			return null;
		
		String qualifier = m.group(5);
		if(qualifier != null && isIllegalQualifier(qualifier))
				return null;
		
		return getVersion(major, minor, bugfix, qualifier);
	}
	
	/**
	 * Tries to transform the specified character sequence into a version
	 * object.
	 * 
	 * @param verStr
	 *            the sequence of characters to be transformed
	 * @return the version
	 * @throws NullPointerException
	 *             if the character sequence is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the character sequence does not correspond to a legal
	 *             version
	 * @since JVerbnet 1.0.0
	 */
	public static Version parseVersion(CharSequence verStr){
		NotNull.check("verStr", verStr);
		Version ver = parseVersionProtected(verStr);
		if(ver == null)
			throw new IllegalArgumentException("Unable to parse version str: " + verStr);
		return ver;
	}
	
	// internal cache of declared version
	private static List<Version> versions;
	
	/**
	 * Emulates the Enum.values() function.
	 * 
	 * @return all the static data type instances listed in the class, in the
	 *         order they are declared.
	 * @since JVerbnet 1.0.0
	 */
	public static List<Version> values(){
		if(versions == null){
			
			// get all the fields containing ContentType
			Field[] fields = Version.class.getDeclaredFields();
			List<Field> instanceFields = new ArrayList<Field>(); 
			for(Field field : fields)
				if(field.getGenericType() == Version.class)
					instanceFields.add(field);
			
			// this is the backing set
			List<Version> hidden = new ArrayList<Version>(instanceFields.size());
			
			// fill in the backing set
			Version dataType;
			for(Field field : instanceFields){
				try{
					dataType = (Version)field.get(null);
					if(dataType != null) 
						hidden.add(dataType);
				} catch(IllegalAccessException e){
					// Ignore
				}
			}
			
			// make the value set unmodifiable
			versions = Collections.unmodifiableList(hidden);
		}
			
		return versions;
	}
}
