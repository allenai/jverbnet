/***************************************************************************
 * JVerbnet v1.2.0
 * Copyright (c) 2012 Massachusetts Institute of Technology
 * 
 * JVerbnet is distributed under the terms of the Creative Commons 
 * Attribution 3.0 Unported License, which means it may be freely used for 
 * all purposes, as long as proper acknowledgment is made.  See the license 
 * file included with this distribution for more details.
 ****************************************************************************/

package edu.mit.jverbnet.util.parse;

import java.util.List;

import org.xml.sax.ContentHandler;

/**
 * A handler that is designed to process a block of XML designated with a
 * particular tag.
 * 
 * @param <T>
 *            The type of element into which the tagged block is transformed
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface ITaggedHandler<T> extends IHasParserHandler {
	
	/**
	 * Returns the tag that is handled by this handler. Will never be
	 * <code>null</code> or empty, and will be trimmed of leading and trailing
	 * whitespace.
	 * 
	 * @return the non-<code>null</code>, non-empty, trimmed tag that is handled
	 *         by this handler
	 * @since JVerbnet 1.0.0
	 */
	public String getTag();
	
	/**
	 * Returns the parent of this child handler. If the handler has no parent,
	 * will return <code>null</code>.
	 * 
	 * @return the possibly <code>null</code> parent of this child handler
	 * @since JVerbnet 1.0.0
	 */
	public ContentHandler getParent();

	/**
	 * Sets the parent for this handler. The new parent may be <code>null</code>
	 * 
	 * @param parent
	 *            the new parent for the handler; may be <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public void setParent(ContentHandler parent);
	
	/**
	 * Returns the list of task handlers that are run when the tagged block is
	 * entered and exited. The list may be modified. Tasks are run in order.
	 * 
	 * @return the list of tasks to be run when entering and exiting the tagged
	 *         block
	 * @since JVerbnet 1.0.0
	 */
	public List<ITaggedBlockTaskHandler> getTaggedBlockTasks();
	
	/**
	 * Clears the state of the handler. This method should be called before
	 * control of the parser is passed to this handler.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public void clear();

	/**
	 * Returns the object representing the data in the parsed tagged block.
	 * 
	 * @return the object representing the data in the parsed tagged block
	 * @since JVerbnet 1.0.0
	 */
	public T getElement();

}
