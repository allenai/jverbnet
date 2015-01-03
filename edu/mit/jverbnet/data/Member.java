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
import static edu.mit.jverbnet.util.Checks.NotNullEmptyOrBlank;
import static edu.mit.jverbnet.util.Checks.UnmodifiableMaskNullWithEmpty;
import static edu.mit.jverbnet.util.Checks.allElementsAre;
import static edu.mit.jverbnet.util.Checks.allKeysAndValuesAre;
import static edu.mit.jverbnet.util.Checks.thisMethodShouldNeverBeCalled;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** 
 * Concrete, default implementation of the {@link IMember} interface.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class Member implements IMember {
	
	// unchanging fields
	private final IVerbClass verbCls;
	private final String name;
	private final Map<IWordnetKey, Boolean> keys;
	private final List<String> groupings;

	/**
	 * Creates a new member with the specified name, keys, and groupings. The
	 * keys and groupings lists may not contain <code>null</code>, empty
	 * strings, or all whitespace strings.
	 * 
	 * @param name
	 *            the name; may not be <code>null</code>, empty, or all
	 *            whitespace
	 * @param keys
	 *            the keys map may be <code>null</code> or empty, but may not
	 *            contain <code>null</code>
	 * @param groupings
	 *            the groupings list may not be <code>null</code>, and may not
	 *            contain <code>null</code>, empty, or all whitespace Strings
	 * @throws NullPointerException
	 *             if the name is <code>null</code>, or any list element is
	 *             <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the name is empty or all whitespace, or the groupings list
	 *             contains empty or all whitespace Strings
	 * @since JVerbnet 1.1.0
	 */
	public Member(IVerbClass verbCls, String name, Map<IWordnetKey, Boolean> keys, List<String> groupings){
		NotNull.check("verbCls", verbCls);
		name = NotNullEmptyOrBlank.check("name", name);
		keys = allKeysAndValuesAre(NotNull, "keys", keys, UnmodifiableMaskNullWithEmpty);
		groupings = allElementsAre(NotNullEmptyOrBlank, "groupings", groupings, UnmodifiableMaskNullWithEmpty);
		
		this.verbCls = verbCls;
		this.name = name;
		this.keys = keys;
		this.groupings = groupings;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IMember#getVerbClass()
	 */
	public IVerbClass getVerbClass() {
		return verbCls;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IMember#getName()
	 */
	public String getName() {
		return name;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IMember#getWordnetTypes()
	 */
	public Map<IWordnetKey, Boolean> getWordnetTypes() {
		return keys;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.data.IMember#getGroupings()
	 */
	public List<String> getGroupings() {
		return groupings;
	}
	
	/** 
	 * Default implementation of the IMemberBuilder interface.
	 *
	 * @author Mark A. Finlayson
	 * @version 1.2.0
	 * @since JVerbnet 1.0.0
	 */
	public static class MemberBuilder implements IMemberBuilder {
		
		// fields
		private String name;
		private Map<IWordnetKey, Boolean> keys = new LinkedHashMap<IWordnetKey, Boolean>();
		private List<String> groupings = new LinkedList<String>();
		

		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IMember#getVerbClass()
		 */
		public IVerbClass getVerbClass() {
			return thisMethodShouldNeverBeCalled();
		}


		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IMember.IMemberBuilder#getName()
		 */
		public String getName() {
			return name;
		}


		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IMember.IMemberBuilder#setName(java.lang.String)
		 */
		public void setName(String name) {
			this.name = name;
		}


		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IMember.IMemberBuilder#getWordnetTypes()
		 */
		public Map<IWordnetKey, Boolean> getWordnetTypes() {
			return keys;
		}


		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IMember.IMemberBuilder#getGroupings()
		 */
		public List<String> getGroupings() {
			return groupings;
		}


		/* 
		 * (non-Javadoc) 
		 *
		 * @see edu.mit.jverbnet.data.IMember.IMemberBuilder#create(edu.mit.jverbnet.data.IVerbClass)
		 */
		public IMember create(IVerbClass parent) {
			return new Member(parent, name, keys, groupings);
		}
		
	}

}
