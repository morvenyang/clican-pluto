/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.resource;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 用来在Spring中配置Map类型的属性
 *
 * @author dapeng.zhang
 *
 */
public class MapResource extends Properties{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8389215743553190456L;

	private final static Log log = LogFactory.getLog(MapResource.class);

	public void setResource(String resource) {
		AutoDecisionResource res = new AutoDecisionResource(resource);
		this.setResource(res);
	}

	private void setResource(Resource resource) {
		Reader reader = null;
		try {
			reader = new InputStreamReader(resource.getInputStream(), "utf-8");
			load(reader);
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {

				}
			}
		}
	}

	@Override
	public synchronized String get(Object key) {
		return (String) super.get(key);
	}

	public String get(String key, String defaultValue) {
		if (containsKey(key)) {
			return get(key);
		} else {
			return defaultValue;
		}
	}

	public boolean getBoolean(String key) {
		String s = get(key);
		if (s == null || "".equals(s)) {
			throw new IllegalArgumentException(
					"Setting must be an boolean (values:true/false/yes/no/on/off) : "
							+ key);
		}
		s = s.trim().toLowerCase();
		return "true".equals(s) || "on".equals(s) || "yes".equals(s);
	}

	public boolean getBoolean(String key, boolean defval) {
		String s = get(key);
		if (s == null || "".equals(s)) {
			return defval;
		}
		s = s.trim().toLowerCase();
		return "true".equals(s) || "on".equals(s) || "yes".equals(s);
	}

	public Object getClassInstance(String key) {
		String s = (String) get(key);
		if (s == null || "".equals(s)) {
			throw new IllegalArgumentException("Setting " + key
					+ " must be a valid classname  : " + key);
		}
		try {
			return Class.forName(s).newInstance();
		} catch (ClassNotFoundException nfe) {
			throw new IllegalArgumentException(s
					+ ": invalid class name for key " + key, nfe);
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(s
					+ ": class could not be reflected " + s, e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(s
					+ ": class could not be reflected " + s, e);
		}
	}

	public Object getClassInstance(String key, Object defaultinstance) {
		return (containsKey(key) ? getClassInstance(key) : defaultinstance);
	}

	public double getDouble(String key) {
		String s = get(key);
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Property must be an double value :" + key);
		}
	}

	public double getDouble(String key, long defval) {
		String s = get(key);
		if (s == null) {
			return defval;
		}
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Property must be an double value :" + key);
		}
	}

	public void setDouble(String key, double val) {
		put(key, Double.toString(val));
	}

	public float getFloat(String key) {
		String s = get(key);
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Property must be an float value :" + key);
		}
	}

	public float getFloat(String key, float defval) {
		String s = get(key);
		if (s == null) {
			return defval;
		}
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Property must be an float value :" + key);
		}
	}

	public void setFloat(String key, float val) {
		put(key, Float.toString(val));
	}

	public int getInt(String key) {
		String s = get(key);
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Property must be an integer value :" + key);
		}
	}

	public int getInt(String key, int defval) {
		String s = get(key);
		if (s == null) {
			return defval;
		}
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Property must be an integer value :" + key);
		}
	}

	public void setInt(String key, int val) {
		put(key, Integer.toString(val));
	}

	public long getLong(String key) {
		String s = get(key);
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Property must be an long value :" + key);
		}
	}

	public long getLong(String key, long defval) {
		String s = get(key);
		if (s == null) {
			return defval;
		}
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Property must be an long value :" + key);
		}
	}

	public void setLong(String key, long val) {
		put(key, Long.toString(val));
	}

	public URL getURL(String key) {
		try {
			return new URL(get(key));
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Property " + key
					+ " must be a valid URL (" + get(key) + ")");
		}
	}

}

// $Id$