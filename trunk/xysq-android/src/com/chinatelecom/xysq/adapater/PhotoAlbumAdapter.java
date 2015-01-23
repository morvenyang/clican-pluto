package com.chinatelecom.xysq.adapater;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.activity.PhotoActivity;
import com.chinatelecom.xysq.bean.PhotoAlbum;

public class PhotoAlbumAdapter extends BaseAdapter {
	private List<PhotoAlbum> albumList;
	private Activity activity;

	public PhotoAlbumAdapter(List<PhotoAlbum> list, Activity activity) {
		this.albumList = list;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return albumList.size();
	}

	@Override
	public Object getItem(int position) {
		return albumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView =  LayoutInflater.from(activity).inflate(
					R.layout.photo_album_row, null);
		}
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.photo_album_row_item_image);
		TextView nameView = (TextView) convertView
				.findViewById(R.id.photo_album_row_item_name);
		/** 通过ID 获取缩略图 */
		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(activity
				.getContentResolver(), albumList.get(position).getBitmap(),
				Thumbnails.MICRO_KIND, null);
		imageView.setImageBitmap(bitmap);
		nameView.setText(albumList.get(position).getName() + " ( "
				+ albumList.get(position).getCount() + " )");
		final PhotoAlbum album = albumList.get(position);
		convertView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity,PhotoActivity.class);
				intent.putExtra("album", album);
				activity.startActivity(intent);
			}
		});
		return convertView;
	}

}
