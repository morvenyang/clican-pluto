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

import com.clican.pluto.transaction.resources.XAFileSetResource;

public class XAFileSetResourceMemoryImpl implements XAFileSetResource {

	private final static Log log = LogFactory
			.getLog(XAFileResourceMemoryImpl.class);

	private ThreadLocal<Xid> xidThreadLocal = new ThreadLocal<Xid>();

	private File directory;

	private Xid lockedXid;

	private Map<Xid, Map<File, byte[]>> oldDataMapping = new HashMap<Xid, Map<File, byte[]>>();

	private Map<Xid, Map<File, byte[]>> modifiedDataMapping = new HashMap<Xid, Map<File, byte[]>>();

	public XAFileSetResourceMemoryImpl(File directory) {
		this.directory = directory;
	}

	public XAFileSetResourceMemoryImpl() {

	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public OutputStream getOutputStream(final File file) throws XAException,
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
				if (lockedXid != null && lockedXid != xidThreadLocal.get()) {
					throw new IOException(
							"This directory ["
									+ directory
									+ "] has been locked by anthoer thread, you can't write it");
				}
				lockedXid = xidThreadLocal.get();
				byte[] data = modifiedDataMapping.get(xidThreadLocal.get())
						.get(file);
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
				modifiedDataMapping.get(xidThreadLocal.get()).put(file, data);
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

	public InputStream getInputStream(File file) throws XAException,
			FileNotFoundException {
		if (xidThreadLocal.get() == null) {
			throw new XAException();
		}
		byte[] data = modifiedDataMapping.get(xidThreadLocal.get()).get(file);
		if (data == null) {
			data = oldDataMapping.get(xidThreadLocal.get()).get(file);
		}
		if (data == null) {
			throw new FileNotFoundException();
		}
		return new ByteArrayInputStream(data);
	}

	public void delete(File file) throws XAException {
		if (lockedXid != null && lockedXid != xidThreadLocal.get()) {
			throw new XAException("This directory [" + directory
					+ "] has been locked by anthoer thread, you can't write it");
		}
		lockedXid = xidThreadLocal.get();
		modifiedDataMapping.get(xidThreadLocal.get()).put(file, null);
	}

	public void commit(Xid xid, boolean onePhase) throws XAException {
		for (File file : modifiedDataMapping.get(xid).keySet()) {
			OutputStream os = null;
			try {
				byte[] data = modifiedDataMapping.get(xid).get(file);
				if (data == null) {
					file.deleteOnExit();
				} else {
					os = new FileOutputStream(file);
					os.write(modifiedDataMapping.get(xid).get(file));
				}
			} catch (Exception e) {
				rollback(xid);
				throw new XAException(XAException.XA_HEURRB);
			} finally {
				lockedXid = null;
				modifiedDataMapping.remove(xid);
				oldDataMapping.remove(xid);
				if (os != null) {
					try {
						os.close();
					} catch (Exception e) {
						log.error("", e);
					}
				}
			}
		}
	}

	public void end(Xid xid, int flags) throws XAException {
		xidThreadLocal.remove();
	}

	public void forget(Xid xid) throws XAException {

	}

	public int getTransactionTimeout() throws XAException {
		return 0;
	}

	public boolean isSameRM(XAResource xares) throws XAException {
		if (!(xares instanceof XAFileSetResourceMemoryImpl)) {
			return false;
		}
		if (directory.equals(((XAFileSetResourceMemoryImpl) xares).directory)) {
			return true;
		}
		return false;
	}

	public int prepare(Xid xid) throws XAException {
		InputStream is = null;
		try {
			for (File f : modifiedDataMapping.get(xid).keySet()) {
				if (f.exists()) {
					is = new FileInputStream(f);
					byte[] data = new byte[is.available()];
					is.read(data);
					oldDataMapping.get(xid).put(f, data);
				} else {
					oldDataMapping.get(xid).put(f, null);
				}
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
		for (File file : oldDataMapping.get(xid).keySet()) {
			OutputStream os = null;
			try {
				byte[] data = oldDataMapping.get(xid).get(file);
				if (data != null) {
					os = new FileOutputStream(file);
					os.write(oldDataMapping.get(xid).get(file));
				} else {
					file.deleteOnExit();
				}
			} catch (Exception e) {
				throw new XAException(XAException.XA_HEURHAZ);
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (Exception e) {
						log.error("", e);
					}
				}
				lockedXid = null;
				modifiedDataMapping.remove(xid);
				oldDataMapping.remove(xid);
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
		modifiedDataMapping.put(xid, new HashMap<File, byte[]>());
		oldDataMapping.put(xid, new HashMap<File, byte[]>());
	}

}

// $Id$