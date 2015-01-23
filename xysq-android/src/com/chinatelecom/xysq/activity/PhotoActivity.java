package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.PhotoAdapter;
import com.chinatelecom.xysq.bean.PhotoAlbum;
import com.chinatelecom.xysq.bean.PhotoItem;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.PhotoRequest;

public class PhotoActivity extends Activity implements HttpCallback {
	private GridView photoGridView;
	private GridView photoSelectedGridView;
	private PhotoAlbum album;
	private PhotoAdapter photoAdapter;
	private PhotoAdapter photoSelectedAdapter;
	private TextView tv;
	private int chooseNum = 0;
	private ProgressBar progressBar;
	private ArrayList<PhotoItem> selectedBitList;
	private Button doneButton;

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
		album =  getIntent().getParcelableExtra("album");
		/** 获取已经选择的图片 **/
		for (int i = 0; i < album.getBitList().size(); i++) {
			if (album.getBitList().get(i).isSelect()) {
				chooseNum++;
			}
		}
		tv.setText(chooseNum + "");
		photoGridView = (GridView) findViewById(R.id.photo_gridView);
		photoGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		photoSelectedGridView = (GridView) findViewById(R.id.photo_selected_gridView);
		photoSelectedGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		doneButton= (Button)findViewById(R.id.photo_done_button);
		doneButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent resultData = new Intent();
				resultData.putParcelableArrayListExtra("selectedBitList", selectedBitList);
				setResult(Activity.RESULT_OK, resultData);
				finish();
			}
		});
		selectedBitList = new ArrayList<PhotoItem>();
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		PhotoRequest.prepareThumbnail(this, this, album);

	}

	@Override
	public void success(String url, Object data) {
		PhotoAlbum album = (PhotoAlbum) data;
		photoAdapter = new PhotoAdapter(this, album.getBitList());
		photoGridView.setAdapter(photoAdapter);
		photoGridView.setOnItemClickListener(photoItemClickListener);

		photoSelectedAdapter = new PhotoAdapter(this, this.selectedBitList);
		photoSelectedGridView.setAdapter(photoSelectedAdapter);
		photoSelectedGridView
				.setOnItemClickListener(photoSelectedItemClickListener);
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

	private OnItemClickListener photoSelectedItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			PhotoItem photoItem = selectedBitList.remove(position);
			photoItem.setSelect(false);
			chooseNum--;
			tv.setText(chooseNum + "");
			photoSelectedAdapter.notifyDataSetChanged();
			photoAdapter.notifyDataSetChanged();
		}
	};

	private OnItemClickListener photoItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			PhotoItem photoItem = album.getBitList().get(position);
			if (photoItem.isSelect()) {
				photoItem.setSelect(false);
				selectedBitList.remove(photoItem);
				chooseNum--;
			} else {
				if (selectedBitList.size() >= 4) {
					Toast.makeText(PhotoActivity.this, "最多选择4个图片",
							Toast.LENGTH_SHORT).show();
					return;
				}
				photoItem.setSelect(true);
				selectedBitList.add(photoItem);
				chooseNum++;
			}
			tv.setText(chooseNum + "");
			photoSelectedAdapter.notifyDataSetChanged();
			photoAdapter.notifyDataSetChanged();
		}
	};
}
