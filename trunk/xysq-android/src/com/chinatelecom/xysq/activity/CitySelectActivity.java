package com.chinatelecom.xysq.activity;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.CityExpandableListAdapter;
import com.chinatelecom.xysq.bean.Area;
import com.chinatelecom.xysq.http.AreaRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class CitySelectActivity extends Activity implements HttpCallback,OnChildClickListener,OnGroupClickListener {

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

	@Override
	public boolean onChildClick(ExpandableListView listView, View view, int arg2,
			int arg3, long arg4) {
		return true;
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
		if (areas != null && areas.size() > 0) {
			ExpandableListView cityListView = (ExpandableListView) findViewById(R.id.citySelect_cityListView);
			cityListView.setDividerHeight(2);
			cityListView.setGroupIndicator(null);
			cityListView.setClickable(true);
			CityExpandableListAdapter adapter = new CityExpandableListAdapter(areas,this,(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
			cityListView.setAdapter(adapter);
			for (int i = 0; i < adapter.getGroupCount(); i++) {
				cityListView.expandGroup(i);
			}
			cityListView.setOnChildClickListener(this);
			cityListView.setOnGroupClickListener(this);
		}
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

}
