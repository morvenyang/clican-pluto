/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.bean;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.clican.pluto.common.resource.AutoDecisionResource;

public class ExcelExecBean extends ExecBean {

	/**
	 * excel读写的文件
	 */
	private String resource;

	/**
	 * excel读写的文件在<code>ProcessorContext</code>中的属性名称
	 */
	private String resourceVarName;

	/**
	 * 是否是执行excel:read
	 */
	private boolean read = true;

	/**
	 * 对应的在excel文件中sheet的名称
	 */
	private String sheetName;

	/**
	 * 要输出那几列,只对excel:write有效
	 */
	private String[] columns;

	/**
	 * 需要输出的列在<code>ProcessorContext</code>中的属性名称
	 */
	private String columnsVarName;

	/**
	 * sheet名称在<code>ProcessorContext</code>中的属性名称
	 */
	private String sheetVarName;

	/**
	 * 从excel中读入数据的时候对应的列需要用到的类型映射
	 */
	private Map<String, String> typeMap;

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public Map<String, String> getTypeMap() {
		return typeMap;
	}

	public void setTypeMap(Map<String, String> typeMap) {
		this.typeMap = typeMap;
	}

	public String getColumnsVarName() {
		return columnsVarName;
	}

	public void setColumnsVarName(String columnsVarName) {
		this.columnsVarName = columnsVarName;
	}

	public String getSheetVarName() {
		return sheetVarName;
	}

	public void setSheetVarName(String sheetVarName) {
		this.sheetVarName = sheetVarName;
	}

	public InputStream getInputStream() throws FileNotFoundException {
		return new AutoDecisionResource(resource).getInputStream();
	}

	public OutputStream getOutputStream() throws FileNotFoundException {
		return new AutoDecisionResource(resource).getOutputStream();
	}

	public String getResourceVarName() {
		return resourceVarName;
	}

	public void setResourceVarName(String resourceVarName) {
		this.resourceVarName = resourceVarName;
	}

}

// $Id: ExcelExecBean.java 16256 2010-07-16 08:35:53Z wei.zhang $