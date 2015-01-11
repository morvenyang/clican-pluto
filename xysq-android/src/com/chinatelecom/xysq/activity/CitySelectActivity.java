package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.Area;
import com.chinatelecom.xysq.http.AreaRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class CitySelectActivity extends Activity implements HttpCallback {

	private ProgressBar progressBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_select);
		progressBar = (ProgressBar) findViewById(R.id.citySelect_progressBar);
		this.loadCityData();
	}

	private void loadCityData() {
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		String areaName = KeyValueUtils.getValue(this, Constants.AREA_NAME);
		String areaId = KeyValueUtils.getValue(this, Constants.AREA_ID);
		TextView selectedCityTextView = (TextView) this
				.findViewById(R.id.citySelect_selectedCityTextView);
		if (StringUtils.isNotEmpty(areaName) && StringUtils.isNotEmpty(areaId)) {
			selectedCityTextView.setText(areaName);
		} else {
			selectedCityTextView.setText("请选择城市");
		}
		AreaRequest.queryCityAreas(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object data) {
		progressBar.setVisibility(View.INVISIBLE);
		List<Area> areas = (List<Area>) data;
		if (areas != null && areas.size() > 0) {
			ExpandableListView cityListView = (ExpandableListView) findViewById(R.id.citySelect_cityListView);
			cityListView.setDividerHeight(2);
			cityListView.setGroupIndicator(null);
			cityListView.setClickable(false);

			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			for (Area area:areas) {
			    Map<String, String> datum = new HashMap<String, String>();
			    datum.put("initials", area.getShortPinyin().substring(0,1).toUpperCase(Locale.ENGLISH));
			    datum.put("name", area.getName());
			    dataList.add(datum);
			}
			SimpleAdapter adapter = new SimpleAdapter(this, dataList,
			                                          android.R.layout.simple_list_item_2,
			                                          new String[] {"initials", "date"},
			                                          new int[] {android.R.id.text1,
			                                                     android.R.id.text2});
			cityListView.setAdapter(adapter);
		}
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

}
