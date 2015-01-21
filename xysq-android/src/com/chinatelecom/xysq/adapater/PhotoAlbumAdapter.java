package com.chinatelecom.xysq.adapater;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.PhotoAlbum;

public class PhotoAlbumAdapter extends BaseAdapter {
	private List<PhotoAlbum> aibumList;
	private Context context;

	public PhotoAlbumAdapter(List<PhotoAlbum> list, Context context) {
		this.aibumList = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return aibumList.size();
	}

	@Override
	public Object getItem(int position) {
		return aibumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView =  LayoutInflater.from(context).inflate(
					R.layout.photo_album_row, null);
		}
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.photo_album_row_item_image);
		TextView nameView = (TextView) convertView
				.findViewById(R.id.photo_album_row_item_name);
		/** 通过ID 获取缩略图 */
		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context
				.getContentResolver(), aibumList.get(position).getBitmap(),
				Thumbnails.MICRO_KIND, null);
		imageView.setImageBitmap(bitmap);
		nameView.setText(aibumList.get(position).getName() + " ( "
				+ aibumList.get(position).getCount() + " )");
		return convertView;
	}

}
