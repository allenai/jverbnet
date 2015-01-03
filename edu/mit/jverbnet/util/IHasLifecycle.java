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

import java.io.IOException;

/** 
 * An object with a lifecycle.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface IHasLifecycle {
	
	/**
	 * This opens the object by performing any required initialization steps. If
	 * this method returns <code>false</code>, then subsequent calls to
	 * {@link #isOpen()} will return <code>false</code>.
	 * 
	 * @return <code>true</code> if there were no errors in initialization;
	 *         <code>false</code> otherwise.
	 * @throws IOException
	 *             if there was IO error while performing initialization
	 * @since JVerbnet 1.0.0
	 */
	public boolean open() throws IOException;

	/**
	 * Returns <code>true</code> if the object is open, that is, ready to
	 * be used; returns <code>false</code> otherwise
	 * 
	 * @return <code>true</code> if the object is open; <code>false</code>
	 *         otherwise
	 * @since JVerbnet 1.0.0
	 */
	public boolean isOpen();
	
	/**
	 * This closes the object by disposing of data backing objects or
	 * connections. If the object is already closed, or in the process of
	 * closing, this method does nothing (although, if the object is in the
	 * process of closing, it may block until closing is complete).
	 * 
	 * @since JVerbnet 1.0.0
	 */
	public void close();

	/**
	 * Indicates that the object was not open when some method was called
	 * requiring it to be open.
	 * 
	 * @author Mark A. Finlayson
	 * @version 1.2.0
	 * @since JVerbnet 1.0.0
	 */
	public class ObjectClosedException extends RuntimeException {

		// serial version id
		private static final long serialVersionUID = 1613455386724719314L;

		/**
		 * Constructs a new exception with <code>null</code> as its detail
		 * message. The cause is not initialized, and may subsequently be
		 * initialized by a call to {@link #initCause}.
		 * 
		 * @since JVerbnet 1.0.0
		 */
		public ObjectClosedException() {
			super();
		}

		/**
		 * Constructs a new exception with the specified detail message. The cause
		 * is not initialized, and may subsequently be initialized by a call to
		 * {@link #initCause}.
		 * 
		 * @param message
		 *            the detail message. The detail message is saved for later
		 *            retrieval by the {@link #getMessage()} method.
		 * @since JVerbnet 1.0.0
		 */
		public ObjectClosedException(String message) {
			super(message);
		}

		/**
		 * Constructs a new exception with the specified detail message and cause.
		 * <p>
		 * Note that the detail message associated with <code>cause</code> is
		 * <i>not</i> automatically incorporated in this runtime exception's detail
		 * message.
		 * 
		 * @param message
		 *            the detail message (which is saved for later retrieval by the
		 *            {@link #getMessage()} method).
		 * @param cause
		 *            the cause (which is saved for later retrieval by the
		 *            {@link #getCause()} method). (A {@code null} value is
		 *            permitted, and indicates that the cause is nonexistent or
		 *            unknown.)
		 * @since JVerbnet 1.0.0
		 */
		public ObjectClosedException(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * Constructs a new exception with the specified cause and a detail message
		 * of {@code (cause==null ? null : cause.toString())} (which typically
		 * contains the class and detail message of {@code cause}). This
		 * constructor is useful for runtime exceptions that are little more than
		 * wrappers for other throwables.
		 * 
		 * @param cause
		 *            the cause (which is saved for later retrieval by the
		 *            {@link #getCause()} method). (A {@code null} value is
		 *            permitted, and indicates that the cause is nonexistent or
		 *            unknown.)
		 * @since JVerbnet 1.0.0
		 */
		public ObjectClosedException(Throwable cause) {
			super(cause);
		}
	}
}
