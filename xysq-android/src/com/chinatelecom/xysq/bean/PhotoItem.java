package com.chinatelecom.xysq.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PhotoItem implements Parcelable {
	
	private int photoID;
	private boolean select;
	private Bitmap bitmap;
	private String filePath;

	public PhotoItem(int id, String filePath) {
		photoID = id;
		this.filePath = filePath;
		select = false;
	}

	public PhotoItem(int id, boolean flag, String filePath) {
		photoID = id;
		this.filePath = filePath;
		select = flag;
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
		bitmap = in.readParcelable(this.getClass().getClassLoader());
		filePath = in.readString();
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
		dest.writeParcelable(bitmap, Bitmap.PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(filePath);
	}
}
