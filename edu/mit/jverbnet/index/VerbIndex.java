/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.index;

import static edu.mit.jverbnet.util.Checks.IsNullEmptyOrBlank;
import static edu.mit.jverbnet.util.Checks.NotNull;
import static edu.mit.jverbnet.util.Checks.NotNullEmptyOrBlank;
import static edu.mit.jverbnet.util.Checks.UnmodifiableMaskNullWithEmpty;
import static edu.mit.jverbnet.util.Checks.thisLineShouldNeverBeCalled;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.mit.jverbnet.data.IMember;
import edu.mit.jverbnet.data.IVerbClass;
import edu.mit.jverbnet.data.IVersion;
import edu.mit.jverbnet.data.IWordnetKey;
import edu.mit.jverbnet.data.Version;
import edu.mit.jverbnet.parse.VerbClassHandler;
import edu.mit.jverbnet.util.AbstractHasLifecycle;
import edu.mit.jverbnet.util.ByteBufferInputStream;
import edu.mit.jverbnet.util.FilteringIterator;
import edu.mit.jverbnet.util.ResourceUtils;
import edu.mit.jverbnet.util.parse.MappedHandler;
import edu.mit.jverbnet.util.parse.NullErrorHandler;

/** 
 * Default implementation of the {@link IVerbIndex} interface.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class VerbIndex extends AbstractHasLifecycle implements IVerbIndex {
	
	// final fields
	private final URL url;

	// changable fields
	private boolean isOpen = false;
	private ErrorHandler errorHandler = NullErrorHandler.getInstance();
	private IVersion version;
	private Map<String, IVerbClass> verbsById;
	private Map<IWordnetKey, Set<IMember>> membersByKeys;
	private Map<String, Set<IMember>> membersByGroups;
	private Set<IMember> membersWithNoKey;
	private Set<IMember> membersWithNoGroup;
	
	/**
	 * Creates a new verb index that uses the data at the specified location.
	 * The file may be a directory (which holds verbnet xml files), or a single
	 * xml file containing all the verbnet data.
	 * 
	 * @param file
	 *            the file or directory containing the verbnet data
	 * @since JVerbnet 1.0.0
	 */
	public VerbIndex(File file){
		this(ResourceUtils.toURL(file));
	}
	
	/**
	 * Creates a new verb index that uses the data at the specified location.
	 * The url may point to a directory (which holds verbnet xml files), or a single
	 * xml stream/file containing all the verbnet data.
	 * 
	 * @param url
	 *            the file or directory containing the verbnet data
	 * @since JVerbnet 1.0.0
	 */
	public VerbIndex(URL url){
		NotNull.check("url", url);
		this.url = url;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.index.IVerbIndex#getVersion()
	 */
	public IVersion getVersion() {
		if(version == null)
			version = determineVersion();
		return version;
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.index.IVerbIndex#setVersion(edu.mit.jverbnet.data.IVersion)
	 */
	public void setVersion(IVersion newVersion) {
		if(version != null)
			throw new IllegalStateException("version has already been set");
		if(newVersion == null)
			throw new NullPointerException("version may not be set to null");
		this.version = newVersion;
	}

	/**
	 * Determines the version of verbnet, as best is able. If the version cannot
	 * be determined, returns {@link IVersion#UNKNOWN}.
	 * 
	 * @return the version of verbnet in use, or {@link IVersion#UNKNOWN}
	 * @since JVerbnet 1.0.0
	 */
	protected IVersion determineVersion(){
		String path = url.getPath();
		MatchResult r = null;
		Matcher m = Version.regex.matcher(path);
		while(m.find())
			r = m.toMatchResult();
		if(r == null)
			return IVersion.UNKNOWN;
		return Version.parseVersionProtected(r.group(0));
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.AbstractHasLifecycle#doOpen()
	 */
	@Override
	protected boolean doOpen() throws IOException {
		
		Map<String, IVerbClass> verbsById = initVerbByIdMap();
		if(verbsById == null)
			return false;
		
		// populate collections
		Map<IWordnetKey, Set<IMember>> membersByKeys = new TreeMap<IWordnetKey, Set<IMember>>();
		Map<String, Set<IMember>> membersByGroups = new TreeMap<String, Set<IMember>>();
		Set<IMember> membersWithNoKey = new LinkedHashSet<IMember>();
		Set<IMember> membersWithNoGroup = new LinkedHashSet<IMember>();
		for(IVerbClass vc : verbsById.values())
			for(IMember m : vc.getMembers()){
				if(m.getWordnetTypes().isEmpty()){
					membersWithNoKey.add(m);
				} else {
					for(IWordnetKey key : m.getWordnetTypes().keySet())
						getSetForKey(membersByKeys, key).add(m);
				}
				if(m.getGroupings().isEmpty()){
					membersWithNoGroup.add(m);
				} else {
					for(String group : m.getGroupings())
						getSetForKey(membersByGroups, group).add(m);
				}
			}
		
		// make unmodifiable
		membersByKeys = compressAndMakeUnmodifiable(membersByKeys);
		membersByGroups = compressAndMakeUnmodifiable(membersByGroups);
		membersWithNoKey = UnmodifiableMaskNullWithEmpty.reallocate(membersWithNoKey);
		membersWithNoGroup = UnmodifiableMaskNullWithEmpty.reallocate(membersWithNoGroup);
		
		// assign fields
		this.verbsById = verbsById;
		this.membersByKeys = membersByKeys;
		this.membersByGroups = membersByGroups;
		this.membersWithNoKey = membersWithNoKey;
		this.membersWithNoGroup = membersWithNoGroup;
		
		// report success
		isOpen = true;
		return true;
	}
	
	/**
	 * Sets the XML error handler that will be used by the index when parsing
	 * XML files. This setting will not take effect until the next time the
	 * index is opened
	 * 
	 * @param eh
	 *            the error handler to be used; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public void setErrorHandler(ErrorHandler eh){
		errorHandler = (eh == null) ? 
				NullErrorHandler.getInstance() : 
					eh;
	}

	/**
	 * Initialization code for reading the verbnet XML.
	 * 
	 * @return a map of verb class Ids to verb classes
	 * @throws IOException
	 *             if there is an IO error
	 * @since JVerbnet 1.0.0
	 */
	protected Map<String, IVerbClass> initVerbByIdMap() throws IOException {
		
		final Map<String, IVerbClass> result = new LinkedHashMap<String, IVerbClass>();
		
		// create parser
		XMLReader parser = null;
		try {
			parser = XMLReaderFactory.createXMLReader();
			parser.setFeature("http://xml.org/sax/features/namespaces", false);
			parser.setFeature("http://xml.org/sax/features/validation", false);
			parser.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			parser.setErrorHandler(errorHandler);
		} catch(SAXException e){
			throw new RuntimeException(e);
		}
		
		// create handler
		MappedHandler<IVerbClass> handler = new VerbClassHandler(parser, true){
			@Override
			public void endTaggedBlock(String uri, String localName, String qName) throws SAXException {
				putVerbClass(getElement());
			}
			protected void putVerbClass(IVerbClass vc){
				result.put(vc.getID(), vc);
				for(IVerbClass subvc : vc.getSubclasses())
					putVerbClass(subvc);
			}
		};
		parser.setContentHandler(handler);
		
		// parse data
		File file = ResourceUtils.toFileChecked(url);
		if(file == null || file.isFile()){
			if(file == null){
				// case 1: url is not a file
				if(parse(parser, new BufferedInputStream(url.openStream()), url.getPath()))
					return null;
			} else {
				// case 2: file points to a single file
				if(parse(parser, new ByteBufferInputStream(file), file.getName()))
					return null;
			}
		} else if(file.isDirectory()) {
			// case 3: file points to a directory
			File[] xmlFiles = file.listFiles(new FileFilter() {
				public boolean accept(File f) {
					return f.getName().endsWith(".xml");
				}
			});
			for(File xmlFile : xmlFiles)
				if(parse(parser, new ByteBufferInputStream(xmlFile), xmlFile.getName()))
					return null;
		} else {
			thisLineShouldNeverBeCalled();
		}
		
		// compress and make umodifiable
		return Collections.unmodifiableMap(new LinkedHashMap<String, IVerbClass>(result));
		
	}
	
	/**
	 * Parses an XML input stream.
	 * 
	 * @param parser
	 *            the parser to use; may not be <code>null</code>
	 * @param in
	 *            the input stream from which to read the data
	 * @return <code>true</code> if the parse failed and the initialization
	 *         should halt; <code>false</code> otherwise
	 * @throws IOException
	 *             if there is an IO error when parsing
	 * @throws {@link NullPointerException} if the parser or input stream are
	 *         null
	 * @since JVerbnet 1.0.0
	 */
	protected boolean parse(XMLReader parser, InputStream in, String filename) throws IOException{
		try {
			parser.parse(new InputSource(in));
		} catch (Throwable t) {
			StringWriter sb = new StringWriter();
			sb.append("Parsing problem: ");
			sb.append(filename == null ? "unknown file" : filename);
			sb.append(" (version ");
			sb.append(getVersion().toString());
			sb.append(")");
			System.err.println(sb.toString());
			t.printStackTrace();
			return true;
		} finally  {
			in.close();
		}
		return false;
	}

	/**
	 * Utility method for filling in a map of keys to sets. The map is queried
	 * for the set indexed under the specified key; if none is found, the set is
	 * created and added. The set is returned.
	 * 
	 * @param map
	 *            the map to query for the set indexed by the key
	 * @param key
	 *            the key under which to look for the set
	 * @return the set in the map for the key; never <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	protected <K,V> Set<V> getSetForKey(Map<K, Set<V>> map, K key){
		Set<V> result = map.get(key);
		if(result == null){
			result = new LinkedHashSet<V>();
			map.put(key, result);
		}
		return result;
	}

	/**
	 * Utility method for reallocating and making unmodifiable a map of sets.
	 * 
	 * @param map
	 *            the map whose set values should be reallocated and made
	 *            unmodifiable; the map itself is made unmodifiable
	 * @return the new unmodifiable map with unmodifiable set values
	 * @since JVerbnet 1.0.0
	 */
	protected <K,V> Map<K, Set<V>> compressAndMakeUnmodifiable(Map<K, Set<V>> map) {
		for(Entry<K, Set<V>> e : map.entrySet())
			e.setValue(Collections.unmodifiableSet(new LinkedHashSet<V>(e.getValue())));
		return Collections.unmodifiableMap(map);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.AbstractHasLifecycle#doIsOpen()
	 */
	@Override
	protected boolean doIsOpen() {
		return isOpen;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.AbstractHasLifecycle#doClose()
	 */
	@Override
	protected void doClose() {
		isOpen = false;
		verbsById = null;
		membersByKeys = null;
		membersByGroups = null;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.index.IVerbIndex#getVerb(java.lang.String)
	 */
	public IVerbClass getVerb(String id) {
		checkOpen();
		NotNullEmptyOrBlank.check("id", id);
		return verbsById.get(id);
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.index.IVerbIndex#getRootVerb(java.lang.String)
	 */
	public IVerbClass getRootVerb(String id) {
		IVerbClass verb = getVerb(id);
		return (verb != null && verb.isRoot()) ?
				verb : 
					null;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.index.IVerbIndex#getMembers(edu.mit.jverbnet.data.IWordnetKey)
	 */
	public Set<IMember> getMembers(IWordnetKey key) {
		checkOpen();
		if(key == null)
			return membersWithNoKey;
		Set<IMember> result = membersByKeys.get(key);
		if(result == null)
			return Collections.emptySet();
		return result;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.index.IVerbIndex#getMembers(java.lang.String)
	 */
	public Set<IMember> getMembers(String grouping) {
		checkOpen();
		grouping = IsNullEmptyOrBlank.mask(grouping, null);
		if(grouping == null)
			return membersWithNoGroup;
		Set<IMember> result = membersByGroups.get(grouping);
		if(result == null)
			return Collections.emptySet();
		return result;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.index.IVerbIndex#iterator()
	 */
	public Iterator<IVerbClass> iterator() {
		checkOpen();
		return verbsById.values().iterator();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.index.IVerbIndex#iteratorRoots()
	 */
	public Iterator<IVerbClass> iteratorRoots() {
		// no need to check open, as this is done in call to getVerbIterator()
		return new FilteringIterator<IVerbClass>(iterator()) {
			@Override
			protected boolean include(IVerbClass e) {
				return e.isRoot();
			}
		};
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.index.IVerbIndex#iteratorWordnetKeys()
	 */
	public Iterator<IWordnetKey> iteratorWordnetKeys() {
		checkOpen();
		return membersByKeys.keySet().iterator();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.index.IVerbIndex#iteratorGroups()
	 */
	public Iterator<String> iteratorGroups() {
		checkOpen();
		return membersByGroups.keySet().iterator();
	}

}
