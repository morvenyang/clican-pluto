package com.chinatelecom.xysq.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ForumTopic implements Parcelable{
	
	private Long id;

	private User submitter;

	private String title;

	private Date createTime;

	private Date modifyTime;

	private String content;

	private List<String> images;
	
	private int postNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getSubmitter() {
		return submitter;
	}

	public void setSubmitter(User submitter) {
		this.submitter = submitter;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public int getPostNum() {
		return postNum;
	}

	public void setPostNum(int postNum) {
		this.postNum = postNum;
	}
	
	public static final Parcelable.Creator<ForumTopic> CREATOR = new Parcelable.Creator<ForumTopic>() {
		public ForumTopic createFromParcel(Parcel in) {
			return new ForumTopic(in);
		}

		public ForumTopic[] newArray(int size) {
			return new ForumTopic[size];
		}
	};

	public ForumTopic() {
		
	}
	private ForumTopic(Parcel in) {
		id = in.readLong();
		submitter = in.readParcelable(this.getClass().getClassLoader());
		title = in.readString();
		createTime = new Date(in.readLong());
		modifyTime = new Date(in.readLong());
		content = in.readString();
		images = new ArrayList<String>();
		in.readStringList(images);
		postNum=in.readInt();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeParcelable(submitter, flags);
		dest.writeString(title);
		dest.writeLong(createTime.getTime());
		dest.writeLong(modifyTime.getTime());
		dest.writeString(content);
		dest.writeStringList(images);
		dest.writeInt(postNum);
	}

}
