package com.chinatelecom.xysq.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PhotoItem implements Parcelable {
	
	private int photoID;
	private boolean select;
	private Bitmap bitmap;
	private String filePath;
	private String fileName;

	public PhotoItem(int id, String filePath,String fileName) {
		photoID = id;
		this.filePath = filePath;
		this.fileName = fileName;
		select = false;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public int getPhotoID() {
		return photoID;
	}

	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "PhotoItem [photoID=" + photoID + ", select=" + select + "]";
	}

	public static final Parcelable.Creator<PhotoItem> CREATOR = new Parcelable.Creator<PhotoItem>() {
		public PhotoItem createFromParcel(Parcel in) {
			return new PhotoItem(in);
		}

		public PhotoItem[] newArray(int size) {
			return new PhotoItem[size];
		}
	};

	private PhotoItem(Parcel in) {
		photoID = in.readInt();
		select = in.readInt() == 0 ? false : true;
		filePath = in.readString();
		fileName = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(photoID);
		dest.writeInt(select ? 1 : 0);
		dest.writeString(filePath);
		dest.writeString(fileName);
	}
}
