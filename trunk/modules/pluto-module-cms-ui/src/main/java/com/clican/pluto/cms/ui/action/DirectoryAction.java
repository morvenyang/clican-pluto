/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import com.clican.pluto.cms.core.service.DirectoryService;
import com.clican.pluto.orm.dynamic.inter.IDirectory;

@Scope(ScopeType.PAGE)
@Name("directoryAction")
public class DirectoryAction extends BaseAction {

	@In("#{directoryService}")
	private DirectoryService directoryService;

	@In(required = false)
	@Out(required = false)
	private IDirectory rootDirectory;

	private String newDirectoryName;

	private IDirectory parentDirectory;

	@Factory("rootDirectory")
	public void loadRootDirectory() {
		rootDirectory = directoryService.loadDirectory("/root");
		if (rootDirectory == null) {
			directoryService.appendDirectory(null, "root");
			rootDirectory = directoryService.loadDirectory("/root");
		}
	}

	public void save() {
		IDirectory directory = directoryService.appendDirectory(
				parentDirectory, newDirectoryName);
		parentDirectory.getChildren().add(directory);
		parentDirectory = null;
		newDirectoryName = null;
	}

	public void refresh() {
		loadRootDirectory();
	}

	public void delete(Serializable directory) {
	}

	public void update(Serializable directory) {
		// TODO Auto-generated method stub

	}

	public String getNewDirectoryName() {
		return newDirectoryName;
	}

	public void setNewDirectoryName(String newDirectoryName) {
		this.newDirectoryName = newDirectoryName;
	}

	public IDirectory getParentDirectory() {
		return parentDirectory;
	}

	public void setParentDirectory(IDirectory parentDirectory) {
		this.parentDirectory = parentDirectory;
	}

}

// $Id$