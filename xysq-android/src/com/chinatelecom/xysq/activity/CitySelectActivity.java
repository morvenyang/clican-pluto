package com.chinatelecom.xysq.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.CityListAdapter;
import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.Area;
import com.chinatelecom.xysq.http.AreaRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class CitySelectActivity extends BaseActivity implements HttpCallback,
		BDLocationListener {

	private ProgressBar progressBar;

	private Map<String, Area> areaNameMap = new HashMap<String, Area>();

	private LocationClient locationClient;

	private boolean containSetCity = false;;

	@Override
	protected String getPageName() {
		return "城市选择";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_select);
		progressBar = (ProgressBar) findViewById(R.id.citySelect_progressBar);
		locationClient = ((XysqApplication) getApplication()).locationClient;
		locationClient.registerLocationListener(this);
		Button backButton = (Button) findViewById(R.id.citySelect_backButton);
		backButton.setOnClickListener(new OnClickListener() {
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
		Log.d("XYSQ", "定位到当前省市:" + province + "城市:" + city);

		String fullName = province + "/" + city;
		if (!containSetCity && areaNameMap.containsKey(fullName)) {
			Area area = areaNameMap.get(fullName);
			TextView selectedCityTextView = (TextView) this
					.findViewById(R.id.citySelect_selectedCityTextView);
			TextView selectedCityLabelTextView = (TextView) this
					.findViewById(R.id.citySelect_selectedCityLabelTextView);
			selectedCityLabelTextView.setText("GPS定位");
			selectedCityTextView.setText(area.getName());
			KeyValueUtils.setStringValue(this, Constants.AREA_NAME,
					area.getName());
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
			containSetCity = true;
		} else {
			containSetCity = false;
			selectedCityTextView.setText("请选择城市");
		}
		AreaRequest.queryCityAreas(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object data) {
		List<Area> areas = (List<Area>) data;
		if (areas != null && areas.size() > 0) {
			for (Area area : areas) {
				areaNameMap.put(area.getFullName(), area);
			}
			ListView cityListView = (ListView) findViewById(R.id.citySelect_cityListView);
			cityListView.setDividerHeight(1);
			CityListAdapter adapter = new CityListAdapter(
					areas,
					this,
					(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
			cityListView.setAdapter(adapter);

			cityListView.setAdapter(adapter);
		}
		if (!containSetCity) {
			locationClient.start();
		} else {
			progressBar.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

}
