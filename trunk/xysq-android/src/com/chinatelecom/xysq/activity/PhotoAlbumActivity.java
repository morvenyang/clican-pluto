package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.PhotoAlbumAdapter;
import com.chinatelecom.xysq.bean.PhotoAlbum;
import com.chinatelecom.xysq.bean.PhotoItem;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.PhotoRequest;

public class PhotoAlbumActivity extends Activity implements HttpCallback {

	// 设置获取图片的字段信
	private static final String[] STORE_IMAGES = {
			MediaStore.Images.Media.DISPLAY_NAME, // 显示的名字
			MediaStore.Images.Media.LATITUDE, // 维度
			MediaStore.Images.Media.LONGITUDE, // 经度
			MediaStore.Images.Media._ID, // id
			MediaStore.Images.Media.BUCKET_ID, // dir id 目录
			MediaStore.Images.Media.BUCKET_DISPLAY_NAME // dir name 目录名字
	};

	private ListView photoAlbumListView;
	
	private ProgressBar progressBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_album);
		Button backButton = (Button) findViewById(R.id.photo_album_backButton);
		photoAlbumListView = (ListView) findViewById(R.id.photo_album_listView);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		progressBar = (ProgressBar)findViewById(R.id.photo_album_progressBar);
		loadPhotoAlbum();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object data) {
		progressBar.setVisibility(View.INVISIBLE);
		List<PhotoAlbum> albumList = (List<PhotoAlbum>)data;
		photoAlbumListView.setAdapter(new PhotoAlbumAdapter(albumList,this));
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

	private void loadPhotoAlbum() {
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		PhotoRequest.loadPhtotAlbum(this, this);
		
	}

}
