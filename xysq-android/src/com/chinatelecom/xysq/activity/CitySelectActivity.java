package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.CityExpandableListAdapter;
import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.Area;
import com.chinatelecom.xysq.http.AreaRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class CitySelectActivity extends Activity implements HttpCallback,
		OnGroupClickListener, BDLocationListener {

	private ProgressBar progressBar;

	private Map<String, List<Area>> areaMap = new LinkedHashMap<String, List<Area>>();

	private LocationClient locationClient;

	@SuppressLint("UseSparseArrays")
	private Map<Integer, String> parentPosiMap = new HashMap<Integer, String>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_select);
		progressBar = (ProgressBar) findViewById(R.id.citySelect_progressBar);
		locationClient = ((XysqApplication) getApplication()).locationClient;
		locationClient.registerLocationListener(this);
		this.loadCityData();
	}

	
	@Override
	protected void onStop() {
		locationClient.stop();
		super.onStop();
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		String province = location.getProvince();
		String city = location.getCity();
		Log.d("XYSQ", "定位到当前省市:"+province+"城市:"+city);
		locationClient.stop();
	}
	
	private void loadCityData() {
		locationClient.start();
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		String areaName = KeyValueUtils.getStringValue(this,
				Constants.AREA_NAME);
		Long areaId = KeyValueUtils.getLongValue(this, Constants.AREA_ID);
		TextView selectedCityTextView = (TextView) this
				.findViewById(R.id.citySelect_selectedCityTextView);
		if (StringUtils.isNotEmpty(areaName) && areaId != null) {
			selectedCityTextView.setText(areaName);
		} else {
			selectedCityTextView.setText("请选择城市");
		}
		AreaRequest.queryCityAreas(this);
	}

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
			long arg3) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object data) {
		progressBar.setVisibility(View.INVISIBLE);
		List<Area> areas = (List<Area>) data;
		areaMap.clear();
		parentPosiMap.clear();
		if (areas != null && areas.size() > 0) {
			for (Area area : areas) {
				String firstChar = area.getShortPinyin().substring(0, 1)
						.toUpperCase(Locale.ENGLISH);
				if (!areaMap.containsKey(firstChar)) {
					areaMap.put(firstChar, new ArrayList<Area>());
					parentPosiMap.put(parentPosiMap.size(), firstChar);
				}
				areaMap.get(firstChar).add(area);
			}
			ExpandableListView cityListView = (ExpandableListView) findViewById(R.id.citySelect_cityListView);
			cityListView.setDividerHeight(2);
			cityListView.setGroupIndicator(null);
			cityListView.setClickable(true);
			CityExpandableListAdapter adapter = new CityExpandableListAdapter(
					areaMap,
					parentPosiMap,
					this,
					(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
			cityListView.setAdapter(adapter);
			for (int i = 0; i < adapter.getGroupCount(); i++) {
				cityListView.expandGroup(i);
			}
			cityListView.setOnGroupClickListener(this);
		}
		//locationClient.requestLocation();
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

}
