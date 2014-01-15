package com.ikidstv.quiz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.ikidstv.quiz.enumeration.TemplateId;

@Table(name = "TEMPLATE")
@Entity
public class Template {

	private Long id;
	private String name;
	private String samplePicture;
	private String descPicture;
	
	private boolean iphone;
	private boolean ipad;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "SAMPLE_PICTURE",length=1024)
	public String getSamplePicture() {
		return samplePicture;
	}
	public void setSamplePicture(String samplePicture) {
		this.samplePicture = samplePicture;
	}
	@Column(name = "DESC_PICTURE",length=1024)
	public String getDescPicture() {
		return descPicture;
	}
	public void setDescPicture(String descPicture) {
		this.descPicture = descPicture;
	}
	
	@Column(name = "IPHONE")
	@Type(type="yes_no")
	public boolean isIphone() {
		return iphone;
	}
	public void setIphone(boolean iphone) {
		this.iphone = iphone;
	}
	@Column(name = "IPAD")
	@Type(type="yes_no")
	public boolean isIpad() {
		return ipad;
	}
	public void setIpad(boolean ipad) {
		this.ipad = ipad;
	}
	
	@Transient
	public TemplateId getTemplateId(){
		return TemplateId.getTemplateIdById(this.id);
	}
	
}
