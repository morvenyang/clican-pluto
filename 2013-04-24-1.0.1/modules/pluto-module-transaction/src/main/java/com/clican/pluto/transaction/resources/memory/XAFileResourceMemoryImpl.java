/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.transaction.resources.memory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.transaction.resources.XAFileResource;

public class XAFileResourceMemoryImpl implements XAFileResource {

	private final static Log log = LogFactory
			.getLog(XAFileResourceMemoryImpl.class);

	private ThreadLocal<Xid> xidThreadLocal = new ThreadLocal<Xid>();

	private File file;

	private Xid lockedXid;

	private Map<Xid, byte[]> oldDataMapping = new HashMap<Xid, byte[]>();

	private Map<Xid, byte[]> modifiedDataMapping = new HashMap<Xid, byte[]>();

	public XAFileResourceMemoryImpl(File file) {
		this.file = file;
	}

	public XAFileResourceMemoryImpl() {

	}

	public void setFile(File file) {
		this.file = file;
	}

	public OutputStream getOutputStream() throws XAException,
			FileNotFoundException {
		if (xidThreadLocal.get() == null) {
			throw new XAException();
		}
		OutputStream os = new OutputStream() {
			@Override
			public void close() throws IOException {

			}

			@Override
			public void flush() throws IOException {

			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				if (lockedXid != xidThreadLocal.get()) {
					throw new IOException(
							"This file ["
									+ file
									+ "] has been locked by anthoer thread, you can't write it");
				}
				lockedXid = xidThreadLocal.get();
				byte[] data = modifiedDataMapping.get(xidThreadLocal.get());
				int srcOff = 0;
				if (data == null) {
					data = new byte[len];
				} else {
					byte[] old = data;
					data = new byte[data.length + len];
					srcOff = old.length;
					for (int i = 0; i < old.length; i++) {
						data[i] = old[i];
					}
				}
				for (int i = srcOff; i < data.length; i++) {
					data[i] = b[i - srcOff];
				}
				modifiedDataMapping.put(xidThreadLocal.get(), data);
			}

			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}

			@Override
			public void write(int b) throws IOException {
				write(new byte[] { (byte) b });
			}
		};
		return os;
	}

	public InputStream getInputStream() throws XAException,
			FileNotFoundException {
		if (xidThreadLocal.get() == null) {
			throw new XAException();
		}
		byte[] data = oldDataMapping.get(xidThreadLocal.get());
		if (data == null) {
			throw new FileNotFoundException();
		}
		return new ByteArrayInputStream(data);
	}

	public void commit(Xid xid, boolean onePhase) throws XAException {
		byte[] data = modifiedDataMapping.get(xid);
		OutputStream os = null;
		try {
			if (data == null) {
				file.deleteOnExit();
			} else {
				os = new FileOutputStream(file);
				os.write(data);
			}
		} catch (Exception e) {
			rollback(xid);
			throw new XAException(XAException.XA_HEURRB);
		} finally {
			lockedXid = null;
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
	}

	public void end(Xid xid, int flags) throws XAException {
		xidThreadLocal.remove();
		oldDataMapping.remove(xid);
		modifiedDataMapping.remove(xid);
	}

	public void forget(Xid xid) throws XAException {

	}

	public int getTransactionTimeout() throws XAException {
		return 0;
	}

	public boolean isSameRM(XAResource xares) throws XAException {
		if (!(xares instanceof XAFileResourceMemoryImpl)) {
			return false;
		}
		if (file.equals(((XAFileResourceMemoryImpl) xares).file)) {
			return true;
		}
		return false;
	}

	public int prepare(Xid xid) throws XAException {

		InputStream is = null;
		try {
			if (file.exists()) {
				is = new FileInputStream(file);
				byte[] data = new byte[is.available()];
				is.read(data);
				oldDataMapping.put(xid, data);
			} else {
				oldDataMapping.put(xid, null);
			}
		} catch (Exception e) {
			log.error("", e);
			throw new XAException(XAException.XA_RBBASE);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
		return XA_OK;
	}

	public Xid[] recover(int flag) throws XAException {
		return new Xid[0];
	}

	public void rollback(Xid xid) throws XAException {
		byte[] data = oldDataMapping.get(xid);
		OutputStream os = null;
		try {
			if (data == null) {
				file.deleteOnExit();
			} else {
				os = new FileOutputStream(file);
				os.write(data);
			}
		} catch (Exception e) {
			throw new XAException(XAException.XA_HEURHAZ);
		} finally {
			lockedXid = null;
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}

	}

	public boolean setTransactionTimeout(int seconds) throws XAException {
		return false;
	}

	public void start(Xid xid, int flags) throws XAException {
		if (xidThreadLocal.get() != null) {
			log.warn("The xid has been binded");
		}
		xidThreadLocal.set(xid);
	}
}

// $Id$