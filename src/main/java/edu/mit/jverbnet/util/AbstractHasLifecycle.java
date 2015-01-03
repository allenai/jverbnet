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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract implementation of an object that has a lifecycle. This class's
 * methods are thread safe.
 * 
 * Subclasses should put their actual lifecycle code in the {@link #doOpen()},
 * {@link #doIsOpen()}, and {@link #doClose()} methods. Synchronization happens
 * in the abstract class, so subclasses do not need to worry about that.
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public abstract class AbstractHasLifecycle implements IHasLifecycle {
	
	// this lock controls the lifecycle
	private final Lock lifecycleLock = new ReentrantLock();
	
	/** 
	 * Throws an exception if the object is closed
	 *
	 * @since JVerbnet 1.0.0
	 */
	protected final void checkOpen(){
		if(!isOpen())
			throw new ObjectClosedException();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.IHasLifecycle#open()
	 */
	public final boolean open() throws IOException {
		boolean result = false;
		try{
			lifecycleLock.lock();
			if(isOpen())
				return true;
			result = doOpen();
			return result;
		} finally {
			if(!result)
				close();
			lifecycleLock.unlock();
		}
	}
	
	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.IHasLifecycle#isOpen()
	 */
	public final boolean isOpen() {
		try{
			lifecycleLock.lock();
			return doIsOpen();
		} finally {
			lifecycleLock.unlock();
		}
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see edu.mit.jverbnet.util.IHasLifecycle#close()
	 */
	public final void close() {
		try{
			lifecycleLock.lock();
			if(!isOpen())
				return;
			doClose();
		} finally {
			lifecycleLock.unlock();
		}
	}

	/**
	 * Subclasses should put initialization code in this method. If
	 * initialization fails, the method should not attempt to cleanup; it should
	 * just return <code>false</code> or throw an exception. The abstract
	 * lifecycle object takes care of calling the {@link #doClose()} method if
	 * initialization fails.
	 * 
	 * @return <code>true</code> if initialization succeeds; <code>false</code>
	 *         otherwise
	 * @throws IOException
	 *             if there is an IO error while initializing
	 * @since JVerbnet 1.0.0
	 */
	protected abstract boolean doOpen() throws IOException;
	
	/**
	 * Subclasses should put code that determines whether the object is open in
	 * here. Returns <code>true</code> if the object is open; <code>false</code>
	 * otherwise.
	 * 
	 * @return <code>true</code> if the object is open; <code>false</code>
	 *         otherwise.
	 * @since JVerbnet 1.0.0
	 */
	protected abstract boolean doIsOpen();

	/**
	 * Subclasses should put tear-down, closing code in this method. This method
	 * should be able to clean up after a failed attempt to {@link #open()} the
	 * object.
	 * 
	 * @since JVerbnet 1.0.0
	 */
	protected abstract void doClose();

}
