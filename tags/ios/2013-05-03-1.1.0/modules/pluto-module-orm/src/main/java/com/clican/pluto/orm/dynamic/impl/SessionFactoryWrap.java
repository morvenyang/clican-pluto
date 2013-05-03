/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

import com.clican.pluto.common.exception.PlutoException;
import com.clican.pluto.orm.dynamic.inter.SessionFactoryUpdate;

@SuppressWarnings("rawtypes") 
public class SessionFactoryWrap implements SessionFactory, SessionFactoryUpdate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -88564051995165244L;

	private SessionFactory sessionFactory;

	private LocalWrapSessionFactoryBean localSessionFactoryBean;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactoryWrap(SessionFactory sessionFactory,
			LocalWrapSessionFactoryBean localSessionFactoryBean) {
		this.sessionFactory = sessionFactory;
		this.localSessionFactoryBean = localSessionFactoryBean;
	}

	public Configuration update() throws PlutoException {
		try {
			this.sessionFactory.close();
			localSessionFactoryBean.afterPropertiesSet();
			return localSessionFactoryBean.getConfiguration();
		} catch (Exception e) {
			throw new PlutoException(e);
		}

	}

	public void close() throws HibernateException {
		sessionFactory.close();
	}

	public void evict(Class persistentClass) throws HibernateException {
		sessionFactory.evict(persistentClass);
	}

	public void evict(Class persistentClass, Serializable id)
			throws HibernateException {
		sessionFactory.evict(persistentClass, id);
	}

	public void evictCollection(String roleName) throws HibernateException {
		sessionFactory.evictCollection(roleName);
	}

	public void evictCollection(String roleName, Serializable id)
			throws HibernateException {
		sessionFactory.evictCollection(roleName, id);
	}

	public void evictEntity(String entityName) throws HibernateException {
		sessionFactory.evictEntity(entityName);
	}

	public void evictEntity(String entityName, Serializable id)
			throws HibernateException {
		sessionFactory.evictEntity(entityName, id);
	}

	public void evictQueries() throws HibernateException {
		sessionFactory.evictQueries();
	}

	public void evictQueries(String cacheRegion) throws HibernateException {
		sessionFactory.evictQueries(cacheRegion);
	}

	public Map getAllClassMetadata() throws HibernateException {
		return sessionFactory.getAllClassMetadata();
	}

	public Map getAllCollectionMetadata() throws HibernateException {
		return sessionFactory.getAllCollectionMetadata();
	}

	public ClassMetadata getClassMetadata(Class persistentClass)
			throws HibernateException {
		return sessionFactory.getClassMetadata(persistentClass);
	}

	public ClassMetadata getClassMetadata(String entityName)
			throws HibernateException {
		return sessionFactory.getClassMetadata(entityName);
	}

	public CollectionMetadata getCollectionMetadata(String roleName)
			throws HibernateException {
		return sessionFactory.getCollectionMetadata(roleName);
	}

	public Session getCurrentSession() throws HibernateException {
		return sessionFactory.getCurrentSession();
	}

	public Set getDefinedFilterNames() {
		return sessionFactory.getDefinedFilterNames();
	}

	public FilterDefinition getFilterDefinition(String filterName)
			throws HibernateException {
		return sessionFactory.getFilterDefinition(filterName);
	}

	public Statistics getStatistics() {
		return sessionFactory.getStatistics();
	}

	public boolean isClosed() {
		return sessionFactory.isClosed();
	}

	public Session openSession() throws HibernateException {
		return sessionFactory.openSession();
	}

	public Session openSession(Connection connection) {
		return sessionFactory.openSession(connection);
	}

	public Session openSession(Interceptor interceptor)
			throws HibernateException {
		return sessionFactory.openSession(interceptor);
	}

	public Session openSession(Connection connection, Interceptor interceptor) {
		return sessionFactory.openSession(connection, interceptor);
	}

	public StatelessSession openStatelessSession() {
		return sessionFactory.openStatelessSession();
	}

	public StatelessSession openStatelessSession(Connection connection) {
		return sessionFactory.openStatelessSession(connection);
	}

	public Reference getReference() throws NamingException {
		return sessionFactory.getReference();
	}

}

// $Id: SessionFactoryWrap.java 556 2009-06-16 04:30:01Z clican $