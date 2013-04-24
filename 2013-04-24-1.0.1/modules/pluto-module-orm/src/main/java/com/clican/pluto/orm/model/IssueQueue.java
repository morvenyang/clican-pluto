/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.orm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.clican.pluto.orm.enumeration.IssueStatus;

@Entity
@Table(name="ISSUE_QUEUE")
public class IssueQueue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5977755398765597135L;

	private Long id;
	
	private Date createDate;
	
	private Date updateDate;
	
	private Long siteId;
	
	private Long templateId;
	
	private Long dataModelId;
	
	private String dataModelName;
	
	private String relativePath;
	
	private IssueStatus issueStatus;
	
	private String localTempPath;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CREATE_DATE", nullable=false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATE_DATE", nullable=false)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "SITE_ID", nullable=false)
	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	@Column(name = "TEMPLATE_ID", nullable=false)
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	@Column(name = "DATA_MODEL_ID", nullable=false)
	public Long getDataModelId() {
		return dataModelId;
	}

	public void setDataModelId(Long dataModelId) {
		this.dataModelId = dataModelId;
	}

	@Column(name = "DATA_MODEL_NAME", nullable=false)
	public String getDataModelName() {
		return dataModelName;
	}

	public void setDataModelName(String dataModelName) {
		this.dataModelName = dataModelName;
	}

	@Column(name = "RELATIVE_PATH", nullable=false)
	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	@Column(name = "ISSUE_STATUS")
    @Type(type = "com.clican.pluto.common.support.hibernate.LabelAndValueType", parameters = { @Parameter(name = "enumClass", value = "com.clican.pluto.orm.enumeration.IssueStatus") })
	public IssueStatus getIssueStatus() {
		return issueStatus;
	}

	public void setIssueStatus(IssueStatus issueStatus) {
		this.issueStatus = issueStatus;
	}

	@Column(name="LOCAL_TEMP_PATH")
	public String getLocalTempPath() {
		return localTempPath;
	}

	public void setLocalTempPath(String localTempPath) {
		this.localTempPath = localTempPath;
	}
	
	
	
}


//$Id$