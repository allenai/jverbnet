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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation of the <code>IWordnetType</code> interface.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class WordnetKey implements IWordnetKey {
	
	/** 
	 * Regular expression pattern for matching a wordnet key string.
	 *
	 * @since JVerbnet 1.1.0
	 */
	public static final Pattern regex = Pattern.compile("(\\p{ASCII}+)%(\\d):(\\d\\d):(\\d\\d)(:\\p{ASCII}+:(\\d\\d))?"); 
	
	// final instance fields
	private final String lemma;
	private final int ssType;
	private final int lexID;
	private final int lexFileNum;

	/**
	 * Constructs a new wordnet type (a.k.a., sense key).
	 * 
	 * @param lemma
	 *            the lemma for the sense key
	 * @param lexFileNum
	 *            the lexical file number of the sense key. Must be between 0
	 *            and 99, inclusive, according to the Wordnet documentation.
	 * @param lexID
	 *            the lexical id of the sense key. Must be between 0 and 15,
	 *            inclusive, according to the Wordnet documentation.
	 * @since JVerbnet 1.0.0
	 */
	public WordnetKey(String lemma, int ssType, int lexFileNum, int lexID){
		// check arguments
		lemma = NotNullEmptyOrBlank.check("lemma", lemma);
		if(ssType < 1 || ssType > 5)
			throw new IllegalArgumentException("ssType is out of range: " + lexFileNum);
		// lexical file numbers are between 00 and 99, inclusive
		if(lexFileNum < 0 || lexFileNum > 99)
			throw new IllegalArgumentException("lexical file number is out of range: " + lexFileNum);
		// lexical ids are between 0 and 15, inclusive
		if(lexID < 0 || lexID > 15)
			throw new IllegalArgumentException("lexical id is out of range: " + lexID);

		// assign fields
		this.lemma = lemma;
		this.ssType = ssType;
		this.lexFileNum = lexFileNum;
		this.lexID = lexID;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IWordnetKey#getLemma()
	 */
	public String getLemma() {
		return lemma;
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IWordnetKey#getSynsetType()
	 */
	public int getSynsetType() {
		return ssType;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IWordnetKey#getLexicalFileNumber()
	 */
	public int getLexicalFileNumber() {
		return lexFileNum;
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IWordnetKey#getLexicalID()
	 */
	public int getLexicalID(){
		return lexID;
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IWordnetKey key) {
		int cmp;
		
		// first sort alphabetically by lemma
		cmp = this.getLemma().compareTo(key.getLemma());
		if(cmp != 0)
			return cmp;
		
		// then sort by synset type
		cmp = Float.compare(this.getSynsetType(), key.getSynsetType());
		if(cmp != 0)
			return cmp;
		
		// then sort by lex_filenum
		cmp = Float.compare(this.getLexicalFileNumber(), key.getLexicalFileNumber());
		if(cmp != 0)
			return cmp;
		
		// then sort by lex_id
		return Float.compare(this.getLexicalID(), key.getLexicalID());
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return toString(this);
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return hashCode(this);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if(!(obj instanceof WordnetKey))
			return false;
		final WordnetKey other = (WordnetKey)obj;
		if(!lemma.equals(other.getLemma()))
			return false;
		if(lexID != other.getLexicalID())
			return false;
		if(2 != other.getSynsetType())
			return false;
		if(lexFileNum != other.getLexicalFileNumber())
			return false;
		return true;
	}
	
	/**
	 * Calculates a hash code for a wordnet type object.
	 * 
	 * @param key
	 *            the wordnet type for which to calculate a hash code
	 * @return the has code for the object
	 * @since JVerbnet 1.0.0
	 */
	public static int hashCode(IWordnetKey key){
		final int prime = 31;
		int result = 1;
		result = prime * result + key.getLemma().hashCode();
		result = prime * result + key.getLexicalID();
		result = prime * result + key.getSynsetType();
		result = prime * result + key.getLexicalFileNumber();
		return result;
	}

	/**
	 * Returns a a string representation of the specified sense key object.
	 * 
	 * @param key
	 *            the sense key to be encoded as a string
	 * @return the string representation of the sense key
	 * @throws NullPointerException
	 *             if the specified key is <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public static String toString(IWordnetKey key){
		
		// figure out appropriate size
		// allocate builder
		StringBuilder sb = new StringBuilder(key.getLemma().length() + 8);
		
		// make numbers
		String lexID = Integer.toString(key.getLexicalID());
		String lexFileNum = Integer.toString(key.getLexicalFileNumber());
		
		// do lemma and type
		sb.append(key.getLemma().toLowerCase());
		sb.append('%');
		sb.append(key.getSynsetType());
		sb.append(':');
		
		// do numbers
		if(lexFileNum.length() < 2)
			sb.append('0');
		sb.append(lexFileNum);
		sb.append(':');
		if(lexID.length() < 2)
			sb.append('0');
		sb.append(lexID);
		
		return sb.toString();
	}

	/**
	 * Parses a string into a Wordnet type object.
	 * 
	 * @param key
	 *            the string to be transformed into a Wordnet type object
	 * @return the Wordnet type object representing this String
	 * @throws IllegalArgumentException
	 *             if the string is not a properly formatted Wordnet type
	 * @since JVerbnet 1.0.0
	 */
	public static WordnetKey parseKey(String key) {
		String trimmedKey = NotNullEmptyOrBlank.check("key", key);
		Matcher m = regex.matcher(trimmedKey);
		if(!m.matches())
			throw new IllegalArgumentException("Unable to parse wordnet key: " + key);
		String lemma = m.group(1);
		int ssType = Integer.parseInt(m.group(2));
		int lexFileNum = Integer.parseInt(m.group(3));
		int lexID = Integer.parseInt(m.group(4));
		return new WordnetKey(lemma, ssType, lexFileNum, lexID);
	}

}
