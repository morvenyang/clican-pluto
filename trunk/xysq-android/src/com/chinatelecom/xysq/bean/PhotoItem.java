package com.chinatelecom.xysq.bean;

import java.io.Serializable;

import android.graphics.Bitmap;

public class PhotoItem implements Serializable {
	private static final long serialVersionUID = 8682674788506891598L;
	private int photoID;
	private boolean select;
	private Bitmap bitmap;

	public PhotoItem(int id) {
		photoID = id;
		select = false;
	}

	public PhotoItem(int id, boolean flag) {
		photoID = id;
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

	@Override
	public String toString() {
		return "PhotoItem [photoID=" + photoID + ", select=" + select + "]";
	}
}
