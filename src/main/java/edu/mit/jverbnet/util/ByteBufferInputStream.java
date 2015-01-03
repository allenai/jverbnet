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
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/** 
 * Adapts a {@link ByteBuffer} to an {@link InputStream}.
 *
 * @author Mark A. Finlayson
 * @version 1.2.0	
 * @since JVerbnet 1.0.0
 */
public class ByteBufferInputStream extends InputStream {
	
	// fields
	protected ByteBuffer buffer;
	protected RandomAccessFile raFile;
	protected int mark = -1;
	
	/**
	 * Creates a new input stream that wraps the specified byte buffer.
	 * 
	 * @param buffer
	 *            the byte buffer to be wrapped; may not be <code>null</code>
	 * @throws NullPointerException
	 *             if the specified buffer is <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public ByteBufferInputStream(ByteBuffer buffer) {
		NotNull.check("buffer", buffer);
		this.buffer = buffer;
		
	}
	
	/**
	 * Creates a new input stream that wraps the a byte buffer attached to the specified file.
	 * 
	 * @param file
	 *            the file on which the stream should be opened; may not be <code>null</code>
	 * @throws NullPointerException
	 *             if the specified file is <code>null</code>
	 * @since JVerbnet 1.0.0
	 */
	public ByteBufferInputStream(File file) throws IOException {
		raFile = new RandomAccessFile(file, "r");
		FileChannel channel = raFile.getChannel();
		buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
	}
	
	/**
	 * Throws an exception if the stream has been closed
	 * 
	 * @throws IOException
	 *             if the stream has been closed
	 * @since JVerbnet 1.0.0
	 */
	protected void checkBuffer() throws IOException {
		if(buffer == null)
			throw new IOException("Stream has been closed");
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		checkBuffer();
		return buffer.get();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.io.InputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException {
		checkBuffer();
		return read(b, 0, b.length);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		checkBuffer();
		NotNull.check("b", b);
		if(off < 0 || len < 0 || len > b.length-off)
			throw new IndexOutOfBoundsException();
		if(len == 0)
			return 0;
		len = Math.min(buffer.remaining(), len);
		if(len == 0)
			return -1;
		buffer.get(b, off, len);
		return len;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.io.InputStream#skip(long)
	 */
	@Override
	public long skip(long n) throws IOException {
		checkBuffer();
		if(n <= 0)
			return 0;
		int skip = Math.min(buffer.remaining(), (int)n);
		buffer.position(buffer.position() + skip);
		return skip;
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.io.InputStream#available()
	 */
	@Override
	public int available() throws IOException {
		checkBuffer();
		return buffer.remaining();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.io.InputStream#close()
	 */
	@Override
	public void close() throws IOException {
		buffer = null;
		if(raFile != null)
			raFile.close();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.io.InputStream#mark(int)
	 */
	@Override
	public synchronized void mark(int readlimit) {
		mark = buffer.position();
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.io.InputStream#reset()
	 */
	@Override
	public synchronized void reset() throws IOException {
		if(mark == -1)
			throw new IOException("mark not set");
		buffer.position(mark);
	}

	/* 
	 * (non-Javadoc) 
	 *
	 * @see java.io.InputStream#markSupported()
	 */
	@Override
	public boolean markSupported() {
		return true;
	}
	
}
