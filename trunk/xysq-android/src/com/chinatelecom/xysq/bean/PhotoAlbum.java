package com.chinatelecom.xysq.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


public class PhotoAlbum implements Parcelable{
	/**
	 * 
	 */
	private String name;   //相册名字
	private String count; //数量
	private int  bitmap;  // 相册第一张图片
	
	private List<PhotoItem> bitList = new ArrayList<PhotoItem>();
	
	public PhotoAlbum() {
	}
	
	
	public PhotoAlbum(String name, String count, int bitmap) {
		super();
		this.name = name;
		this.count = count;
		this.bitmap = bitmap;
	}


	public List<PhotoItem> getBitList() {
		return bitList;
	}


	public void setBitList(List<PhotoItem> bitList) {
		this.bitList = bitList;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public int getBitmap() {
		return bitmap;
	}
	public void setBitmap(int bitmap) {
		this.bitmap = bitmap;
	}
	@Override
	public String toString() {
		return "PhotoAibum [name=" + name + ", count=" + count + ", bitmap="
				+ bitmap + ", bitList=" + bitList + "]";
	}

	public static final Parcelable.Creator<PhotoAlbum> CREATOR = new Parcelable.Creator<PhotoAlbum>() {
		public PhotoAlbum createFromParcel(Parcel in) {
			return new PhotoAlbum(in);
		}

		public PhotoAlbum[] newArray(int size) {
			return new PhotoAlbum[size];
		}
	};

	private PhotoAlbum(Parcel in) {
		name=in.readString();
		count = in.readString();
		bitmap = in.readInt();
		in.readTypedList(bitList, PhotoItem.CREATOR);
	}

	
	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);		
		dest.writeString(count);	
		dest.writeInt(bitmap);
		dest.writeTypedList(bitList);
	}
	
	
}
