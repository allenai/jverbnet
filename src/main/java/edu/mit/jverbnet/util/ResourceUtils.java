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

import static edu.mit.jverbnet.util.Checks.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

/** 
 * A few utilities relating to Files and URLs.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public class ResourceUtils {

	/**
	 * Transforms a URL into a File. The URL must use the 'file' protocol and
	 * must be in a UTF-8 compatible format as specified in
	 * {@link java.net.URLDecoder}.
	 * 
	 * @return a file pointing to the same place as the url, or
	 *         <code>null</code> if the url does not use the 'file' protocol
	 * @throws NullPointerException
	 *             if the url is <code>null</code>
	 * @throws IOException
	 *             if there is an IO problem
	 * @since JVerbnet 1.0.0
	 */
	public static File toFileChecked(URL url) throws IOException {
		NotNull.check("url", url);
		if(url.getProtocol().equals("file"))
			return toFile(url);
		return null;
	}
	
	/**
	 * Transforms a URL into a File. The URL must use the 'file' protocol and
	 * must be in a UTF-8 compatible format as specified in
	 * {@link java.net.URLDecoder}.
	 * 
	 * @return a file pointing to the same place as the url
	 * @throws NullPointerException
	 *             if the url is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the url does not use the 'file' protocol
	 * @throws IOException
	 *             if there is an IO problem
	 * @since JVerbnet 1.0.0
	 */
	public static File toFile(URL url) throws IOException {
		NotNull.check("url", url);
		if(!url.getProtocol().equals("file")) 
			throw new IllegalArgumentException("URL source must use 'file' protocol");
		return new File(URLDecoder.decode(url.getPath(), "UTF-8"));
	}
	
	/**
	 * Transforms a file into a URL.
	 * 
	 * @param file
	 *            the file to be transformed
	 * @return a URL representing the file
	 * @throws NullPointerException
	 *             if the specified file is <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public static URL toURL(File file) {
		NotNull.check("file", file);
		try{
			URI uri = new URI("file", "//", file.toURL().getPath() , null);
			return new URL("file", null, uri.getRawPath());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

}
