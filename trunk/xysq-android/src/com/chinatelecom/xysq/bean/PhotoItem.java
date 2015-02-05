package com.chinatelecom.xysq.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PhotoItem implements Parcelable {
	
	private boolean select;
	private Bitmap bitmap;
	private String filePath;
	private String fileName;
	private boolean showEmpty;

	public PhotoItem(String filePath,String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
		select = false;
	}
	
	public PhotoItem(boolean showEmpty) {
		this.showEmpty = showEmpty;
		select = false;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
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

	public boolean isShowEmpty() {
		return showEmpty;
	}

	public void setShowEmpty(boolean showEmpty) {
		this.showEmpty = showEmpty;
	}

	@Override
	public String toString() {
		return "PhotoItem [select=" + select + "]";
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
		select = in.readInt() == 0 ? false : true;
		filePath = in.readString();
		fileName = in.readString();
		showEmpty = in.readInt()==0?false:true;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(select ? 1 : 0);
		dest.writeString(filePath);
		dest.writeString(fileName);
		dest.writeInt(showEmpty?1:0);
	}
}
