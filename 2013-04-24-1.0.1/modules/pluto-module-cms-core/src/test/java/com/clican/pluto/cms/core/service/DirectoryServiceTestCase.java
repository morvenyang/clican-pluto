/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import org.jmock.Expectations;

import com.clican.pluto.cms.core.BaseTestCase;
import com.clican.pluto.cms.dao.DirectoryDao;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.IDirectory;
import com.clican.pluto.orm.dynamic.inter.ITemplateDirectorySiteRelation;

public class DirectoryServiceTestCase extends BaseTestCase {

	private DirectoryService directoryService;

	private ClassLoaderUtil classLoaderUtil;

	private DirectoryDao directoryDao;

	public void setDirectoryService(DirectoryService directoryService) {
		this.directoryService = directoryService;
		classLoaderUtil = this.context.mock(ClassLoaderUtil.class);
		directoryDao = this.context.mock(DirectoryDao.class);
		this.directoryService.setClassLoaderUtil(classLoaderUtil);
		this.directoryService.setDirectoryDao(directoryDao);
	}

	public void testAppendDirectory() throws Exception {
		final String name = "root";
		final IDirectory result = new IDirectory() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 4645740417113237029L;

			public int getIssueMode() {
				// TODO Auto-generated method stub
				return 0;
			}

			public Set<IDirectory> getChildren() {
				// TODO Auto-generated method stub
				return null;
			}

			public Calendar getCreateTime() {
				// TODO Auto-generated method stub
				return null;
			}

			public Long getId() {
				// TODO Auto-generated method stub
				return null;
			}

			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}

			public IDirectory getParent() {
				// TODO Auto-generated method stub
				return null;
			}

			public String getPath() {
				// TODO Auto-generated method stub
				return null;
			}

			public IDirectory getReference() {
				// TODO Auto-generated method stub
				return null;
			}

			

			public Calendar getUpdateTime() {
				// TODO Auto-generated method stub
				return null;
			}

			public void setChildren(Set<IDirectory> children) {
				// TODO Auto-generated method stub

			}

			public void setCreateTime(Calendar createTime) {
				// TODO Auto-generated method stub

			}

			public void setId(Long id) {
				// TODO Auto-generated method stub

			}

			public void setName(String name) {
				// TODO Auto-generated method stub

			}

			public void setParent(IDirectory parent) {
				// TODO Auto-generated method stub

			}

			public void setPath(String path) {
				// TODO Auto-generated method stub

			}

			public void setReference(IDirectory reference) {
				// TODO Auto-generated method stub

			}


			public void setUpdateTime(Calendar updateTime) {
				// TODO Auto-generated method stub

			}

			public Set<ITemplateDirectorySiteRelation> getTemplateDirectorySiteRelationSet() {
				return null;
			}

			public void setTemplateDirectorySiteRelationSet(
					Set<ITemplateDirectorySiteRelation> templateDirectorySiteRelationSet) {
				
			}

           

		};
		context.checking(new Expectations() {
			{
				one(classLoaderUtil).newDirectory(null, name);
				result.setPath(name);
				will(returnValue(result));
				one(directoryDao).save(result);
			}
		});
		Serializable directory = directoryService.appendDirectory(null, name);
		assertNotNull(directory);
	}
}

// $Id$