package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.CommunityListAdapter;
import com.chinatelecom.xysq.bean.Community;
import com.chinatelecom.xysq.http.AreaRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class CommunitySelectActivity extends BaseActivity implements HttpCallback,
		OnClickListener {

	private ProgressBar progressBar;

	private List<Community> communityList;

	private Button searchButton;

	private EditText searchEditText;

	private ListView communityListView;
	
	private TextView communitySelectTitleTextView;

	@Override
	protected String getPageName() {
		return "小区选择";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.community_select);
		progressBar = (ProgressBar) findViewById(R.id.communitySelect_progressBar);
		communitySelectTitleTextView = (TextView)findViewById(R.id.communitySelect_titleTextView);
		Button changeArea = (Button) findViewById(R.id.communitySelect_changeAreaButton);
		communitySelectTitleTextView.setOnClickListener(this);
		changeArea.setOnClickListener(this);

		this.searchButton = (Button) findViewById(R.id.communitySelect_backButton);
		this.searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CommunitySelectActivity.this.finish();
			}
		});

		Button searchButton = (Button) findViewById(R.id.communitySelect_searchButton);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchCommunities();
			}
		});
		searchButton.setEnabled(false);

		this.searchEditText = (EditText) findViewById(R.id.communitySelect_searchEditText);
		this.searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				searchCommunities();
			}
		});

		this.communityListView = (ListView) findViewById(R.id.communitySelect_communityListView);
		this.loadCommunityData();
	}

	private void searchCommunities() {
		Log.d("XYSQ", "search communities");
		if (this.communityList == null || this.communityList.size() == 0) {
			return;
		}
		String keywrod = searchEditText.getText().toString();
		List<Community> filtedCommunities = new ArrayList<Community>();
		for (Community c : communityList) {
			if (keywrod == null || c.getName().contains(keywrod)
					|| c.getPinyin().contains(keywrod)
					|| c.getShortPinyin().contains(keywrod)) {
				filtedCommunities.add(c);
			}
		}
		this.communityListView
				.setAdapter(new CommunityListAdapter(
						filtedCommunities,
						this,
						(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)));
		this.searchButton.setEnabled(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object object) {
		progressBar.setVisibility(View.INVISIBLE);
		this.communityList = (List<Community>) object;
		searchCommunities();
	}

	@Override
	protected void onResume() {
		loadCommunityData();
		super.onResume();
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

	private void loadCommunityData() {
		String areaName = KeyValueUtils.getStringValue(this,
				Constants.AREA_NAME);
		Long areaId = KeyValueUtils.getLongValue(this, Constants.AREA_ID);
		if (StringUtils.isNotEmpty(areaName) && areaId != null) {
			this.communitySelectTitleTextView.setText(areaName);
			progressBar.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			AreaRequest.queryCommunityByArea(this, areaId);
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(CommunitySelectActivity.this,
				CitySelectActivity.class);
		Log.d("CommunitySelectActivity", "changeAreaButton is clicked");
		startActivity(intent);
		
	}
}
