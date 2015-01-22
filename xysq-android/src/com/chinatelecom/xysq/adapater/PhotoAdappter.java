package com.chinatelecom.xysq.adapater;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

import com.chinatelecom.xysq.bean.PhotoAlbum;
import com.chinatelecom.xysq.layout.PhotoGridItem;

public class PhotoAdappter extends BaseAdapter   {
	private Context context;
	private PhotoAlbum album;
	public PhotoAdappter(Context context, PhotoAlbum album) {
		this.context = context;
		this.album = album;
	}

	@Override
	public int getCount() {
		return album.getBitList().size();
	}

	@Override
	public Object getItem(int position) {
		return album.getBitList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoGridItem item;
		if(convertView == null){
			item = new PhotoGridItem(context);
			 item.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,  
                     LayoutParams.MATCH_PARENT));
		}else{
			item = (PhotoGridItem)convertView;
		}
		// 通过ID 加载缩略图
		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),  album.getBitList().get(position).getPhotoID(), Thumbnails.MICRO_KIND, null);
		item.SetBitmap(bitmap);
        boolean flag = album.getBitList().get(position).isSelect();
		item.setChecked(flag);
		return item;
	}
}
