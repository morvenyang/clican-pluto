/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.impl;

import java.io.IOException;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;


public class LocalWrapSessionFactoryBean
		extends
		org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean {

	private SessionFactoryWrap sessionFactoryWrap;

	private DynamicClassLoader dynamicClassLoader;

	private String[] packagesToScan;

	private static final String RESOURCE_PATTERN = "**/*.class";

	private TypeFilter[] entityTypeFilters = new TypeFilter[] {
			new AnnotationTypeFilter(Entity.class, false),
			new AnnotationTypeFilter(Embeddable.class, false),
			new AnnotationTypeFilter(MappedSuperclass.class, false) };

	public void setDynamicClassLoader(DynamicClassLoader dynamicClassLoader) {
		this.dynamicClassLoader = dynamicClassLoader;
	}

	@Override
	protected SessionFactory wrapSessionFactoryIfNecessary(SessionFactory rawSf) {
		if (this.sessionFactoryWrap == null) {
			sessionFactoryWrap = new SessionFactoryWrap(rawSf, this);
		} else {
			sessionFactoryWrap.setSessionFactory(rawSf);
		}
		return sessionFactoryWrap;
	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	@Override
	protected SessionFactory buildSessionFactory() throws Exception {
		setBeanClassLoader(dynamicClassLoader);
		return super.buildSessionFactory();
	}

	@Override
	protected void scanPackages(AnnotationConfiguration config) {
		if (this.packagesToScan != null) {
			try {
				ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(
						dynamicClassLoader);
				for (String pkg : this.packagesToScan) {
					String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
							+ ClassUtils.convertClassNameToResourcePath(pkg)
							+ RESOURCE_PATTERN;
					Resource[] resources = resourcePatternResolver
							.getResources(pattern);
					MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(
							resourcePatternResolver);
					for (Resource resource : resources) {
						if (resource.isReadable()) {
							MetadataReader reader = readerFactory
									.getMetadataReader(resource);
							String className = reader.getClassMetadata()
									.getClassName();
							if (matchesFilter(reader, readerFactory)) {
								config
										.addAnnotatedClass(resourcePatternResolver
												.getClassLoader().loadClass(
														className));
							}
						}
					}
				}
			} catch (IOException ex) {
				throw new MappingException(
						"Failed to scan classpath for unlisted classes", ex);
			} catch (ClassNotFoundException ex) {
				throw new MappingException(
						"Failed to load annotated classes from classpath", ex);
			}
		}
	}

	private boolean matchesFilter(MetadataReader reader,
			MetadataReaderFactory readerFactory) throws IOException {
		if (this.entityTypeFilters != null) {
			for (TypeFilter filter : this.entityTypeFilters) {
				if (filter.match(reader, readerFactory)) {
					return true;
				}
			}
		}
		return false;
	}

}

// $Id: LocalWrapSessionFactoryBean.java 556 2009-06-16 04:30:01Z clican $