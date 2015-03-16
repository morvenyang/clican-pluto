package com.chinatelecom.xysq.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class AnnouncementAndNotice implements Parcelable {

	private Long id;

	private String title;

	private String content;

	private Date createTime;

	private Date modifyTime;

	private String noticeCategory;

	private String innerModule;

	private List<String> contents;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getNoticeCategory() {
		return noticeCategory;
	}

	public void setNoticeCategory(String noticeCategory) {
		this.noticeCategory = noticeCategory;
	}

	public String getInnerModule() {
		return innerModule;
	}

	public void setInnerModule(String innerModule) {
		this.innerModule = innerModule;
	}

	public List<String> getContents() {
		return contents;
	}

	public void setContents(List<String> contents) {
		this.contents = contents;
	}

	public static final Parcelable.Creator<AnnouncementAndNotice> CREATOR = new Parcelable.Creator<AnnouncementAndNotice>() {
		public AnnouncementAndNotice createFromParcel(Parcel in) {
			return new AnnouncementAndNotice(in);
		}

		public AnnouncementAndNotice[] newArray(int size) {
			return new AnnouncementAndNotice[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(title);
		dest.writeString(content);
		dest.writeString(innerModule);
		dest.writeLong(modifyTime.getTime());
		dest.writeStringList(contents);
	}

	public AnnouncementAndNotice() {

	}

	private AnnouncementAndNotice(Parcel in) {
		id = in.readLong();
		title = in.readString();
		content = in.readString();
		innerModule = in.readString();
		modifyTime = new Date(in.readLong());
		contents = new ArrayList<String>();
		in.readStringList(contents);
	}

}
