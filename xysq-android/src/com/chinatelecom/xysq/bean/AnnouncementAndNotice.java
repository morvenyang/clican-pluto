package com.chinatelecom.xysq.bean;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class AnnouncementAndNotice implements Parcelable {

	private Long id;

	private String title;

	private String content;

	private Date createTime;

	private Date modifyTime;

	private String noticeCategory;

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
		dest.writeLong(modifyTime.getTime());
	}

	public AnnouncementAndNotice(){
		
	}
	private AnnouncementAndNotice(Parcel in) {
		id = in.readLong();
		title = in.readString();
		content = in.readString();
		modifyTime = new Date(in.readLong());
	}

}
