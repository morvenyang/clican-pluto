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
import android.view.View.OnClickListener;
import android.widget.Button;
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

	private Map<String,Area> areaNameMap = new HashMap<String,Area>();
	
	private LocationClient locationClient;
	
	private boolean containSetCity=false;;

	@SuppressLint("UseSparseArrays")
	private Map<Integer, String> parentPosiMap = new HashMap<Integer, String>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_select);
		progressBar = (ProgressBar) findViewById(R.id.citySelect_progressBar);
		locationClient = ((XysqApplication) getApplication()).locationClient;
		locationClient.registerLocationListener(this);
		Button backButton = (Button)findViewById(R.id.citySelect_backButton);
		backButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				CitySelectActivity.this.finish();
			}
		});
		
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
		
		String fullName = province+"/"+city;
		if(!containSetCity&&areaNameMap.containsKey(fullName)){
			Area area = areaNameMap.get(fullName);
			TextView selectedCityTextView = (TextView) this
					.findViewById(R.id.citySelect_selectedCityTextView);
			TextView selectedCityLabelTextView = (TextView) this
					.findViewById(R.id.citySelect_selectedCityLabelTextView);
			selectedCityLabelTextView.setText("GPS定位");
			selectedCityTextView.setText(area.getName());
			KeyValueUtils.setStringValue(this, Constants.AREA_NAME, area.getName());
			KeyValueUtils.setLongValue(this, Constants.AREA_ID, area.getId());
		}
		locationClient.stop();
		progressBar.setVisibility(View.INVISIBLE);
	}
	
	private void loadCityData() {
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		String areaName = KeyValueUtils.getStringValue(this,
				Constants.AREA_NAME);
		Long areaId = KeyValueUtils.getLongValue(this, Constants.AREA_ID);
		TextView selectedCityTextView = (TextView) this
				.findViewById(R.id.citySelect_selectedCityTextView);
		TextView selectedCityLabelTextView = (TextView) this
				.findViewById(R.id.citySelect_selectedCityLabelTextView);
		if (StringUtils.isNotEmpty(areaName) && areaId != null) {
			selectedCityLabelTextView.setText("已选择城市");
			selectedCityTextView.setText(areaName);
			containSetCity=true;
		} else {
			containSetCity=false;
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
				areaNameMap.put(area.getFullName(), area);
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
		if(!containSetCity){
			locationClient.start();
		}
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

}
