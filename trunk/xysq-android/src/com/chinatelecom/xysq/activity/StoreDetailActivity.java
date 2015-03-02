package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.ImagePagerAdapter;
import com.chinatelecom.xysq.bean.Store;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.StoreRequest;

public class StoreDetailActivity extends BaseActivity implements HttpCallback {

	private ViewPager imageViewPager;

	private ProgressBar progressBar;

	private TextView titleTextView;
	private TextView descriptionTextView;
	private TextView addressTextView;
	private TextView phoneNumberTextView;

	private Long storeId;
	private List<String> images;
	private ImagePagerAdapter adapter;
	private Button callButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_detail);
		storeId = this.getIntent().getLongExtra("storeId", -1);
		imageViewPager = (ViewPager) findViewById(R.id.store_detail_imageViewPager);
		progressBar = (ProgressBar) findViewById(R.id.store_detail_progressBar);
		Button backButton = (Button) findViewById(R.id.store_detail_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titleTextView = (TextView) findViewById(R.id.store_detail_titleTextView);
		descriptionTextView = (TextView) findViewById(R.id.store_detail_descriptionTextView);
		addressTextView = (TextView) findViewById(R.id.store_detail_addressTextView);
		phoneNumberTextView = (TextView) findViewById(R.id.store_detail_phoneNumberTextView);
		callButton = (Button) findViewById(R.id.store_detail_callButton);
		images = new ArrayList<String>();
		adapter = new ImagePagerAdapter(images, this);
		imageViewPager.setAdapter(adapter);
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		StoreRequest.queryStore(this, storeId);
	}

	@Override
	protected String getPageName() {
		return "商家详情";
	}

	@Override
	public void success(String url, Object data) {
		final Store store = (Store) data;
		titleTextView.setText(store.getName());
		descriptionTextView.setText("描述:" + store.getDescription());
		addressTextView.setText("地址:" + store.getAddress());
		phoneNumberTextView.setText("电话:" + store.getTel());
		if(StringUtils.isNotEmpty(store.getTel())){
			callButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					 String number = "tel:" + store.getTel().trim();
				      Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number)); 
				      startActivity(callIntent);
				}
			});
		}
		
		this.images.clear();
		this.images.addAll(store.getImages());
		adapter.notifyDataSetChanged();
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

}
