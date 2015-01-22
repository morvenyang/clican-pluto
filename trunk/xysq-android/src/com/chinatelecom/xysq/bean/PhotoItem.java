package com.chinatelecom.xysq.bean;

import java.io.Serializable;

import android.graphics.Bitmap;

public class PhotoItem implements Serializable {
	private static final long serialVersionUID = 8682674788506891598L;
	private int photoID;
	private boolean select;
	private Bitmap bitmap;
	private String filePath;

	public PhotoItem(int id,String filePath) {
		photoID = id;
		this.filePath = filePath;
		select = false;
	}

	public PhotoItem(int id, boolean flag,String filePath) {
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
}
