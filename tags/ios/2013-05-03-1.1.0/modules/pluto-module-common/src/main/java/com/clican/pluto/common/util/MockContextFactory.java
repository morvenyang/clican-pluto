/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

public class MockContextFactory implements InitialContextFactory {

	private static Map<String, Object> context = new HashMap<String, Object>();

	public Context getInitialContext(Hashtable<?, ?> environment)
			throws NamingException {

		return new Context() {

			public Object addToEnvironment(String propName, Object propVal)
					throws NamingException {
				throw new NamingException("Unsupported");
			}

			public void bind(Name name, Object obj) throws NamingException {
			}

			public void bind(String name, Object obj) throws NamingException {
				context.put(name, obj);

			}

			public void close() throws NamingException {

			}

			public Name composeName(Name name, Name prefix)
					throws NamingException {
				throw new NamingException("Unsupported");
			}

			public String composeName(String name, String prefix)
					throws NamingException {
				throw new NamingException("Unsupported");
			}

			public Context createSubcontext(Name name) throws NamingException {
				return this;
			}

			public Context createSubcontext(String name) throws NamingException {
				return this;
			}

			public void destroySubcontext(Name name) throws NamingException {

			}

			public void destroySubcontext(String name) throws NamingException {
			}

			public Hashtable<?, ?> getEnvironment() throws NamingException {
				return null;
			}

			public String getNameInNamespace() throws NamingException {
				return null;
			}

			public NameParser getNameParser(Name name) throws NamingException {
				return null;
			}

			public NameParser getNameParser(String name) throws NamingException {
				throw new NamingException("Unsupported");
			}

			public NamingEnumeration<NameClassPair> list(Name name)
					throws NamingException {
				throw new NamingException("Unsupported");
			}

			public NamingEnumeration<NameClassPair> list(String name)
					throws NamingException {
				throw new NamingException("Unsupported");
			}

			public NamingEnumeration<Binding> listBindings(Name name)
					throws NamingException {
				throw new NamingException("Unsupported");
			}

			public NamingEnumeration<Binding> listBindings(String name)
					throws NamingException {
				throw new NamingException("Unsupported");
			}

			public Object lookup(Name name) throws NamingException {
				throw new NamingException("Unsupported");
			}

			public Object lookup(String name) throws NamingException {
				return context.get(name);
			}

			public Object lookupLink(Name name) throws NamingException {
				throw new NamingException("Unsupported");
			}

			public Object lookupLink(String name) throws NamingException {
				return context.get(name);
			}

			public void rebind(Name name, Object obj) throws NamingException {
				throw new NamingException("Unsupported");

			}

			public void rebind(String name, Object obj) throws NamingException {
				context.put(name, obj);

			}

			public Object removeFromEnvironment(String propName)
					throws NamingException {
				throw new NamingException("Unsupported");
			}

			public void rename(Name oldName, Name newName)
					throws NamingException {
				throw new NamingException("Unsupported");

			}

			public void rename(String oldName, String newName)
					throws NamingException {
				throw new NamingException("Unsupported");

			}

			public void unbind(Name name) throws NamingException {
				// TODO Auto-generated method stub

			}

			public void unbind(String name) throws NamingException {
				context.remove(name);

			}
		};
	}

}


//$Id$