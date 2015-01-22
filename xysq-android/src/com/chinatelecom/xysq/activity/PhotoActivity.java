package com.chinatelecom.xysq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.PhotoAdappter;
import com.chinatelecom.xysq.bean.PhotoAlbum;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.PhotoRequest;

public class PhotoActivity extends Activity implements HttpCallback {
	private GridView gv;
	private PhotoAlbum album;
	private PhotoAdappter adapter;
	private TextView tv;
	private int chooseNum = 0;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo);
		Button backButton = (Button) findViewById(R.id.photo_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		progressBar = (ProgressBar) findViewById(R.id.photo_progressBar);
		tv = (TextView) findViewById(R.id.photo_chooseNum);
		album = (PhotoAlbum) getIntent().getExtras().get("album");
		/** 获取已经选择的图片 **/
		for (int i = 0; i < album.getBitList().size(); i++) {
			if (album.getBitList().get(i).isSelect()) {
				chooseNum++;
			}
		}
		tv.setText(chooseNum + "");
		gv = (GridView) findViewById(R.id.photo_gridView);
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		PhotoRequest.prepareThumbnail(this, this, album);

	}

	@Override
	public void success(String url, Object data) {
		PhotoAlbum album = (PhotoAlbum) data;
		adapter = new PhotoAdappter(this, album);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(gvItemClickListener);
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

	private OnItemClickListener gvItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (album.getBitList().get(position).isSelect()) {
				album.getBitList().get(position).setSelect(false);
				chooseNum--;
			} else {
				album.getBitList().get(position).setSelect(true);
				chooseNum++;
			}
			tv.setText(chooseNum + "");
			adapter.notifyDataSetChanged();
		}
	};
}
