package com.chinatelecom.xysq.adapater;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.activity.CitySelectActivity;
import com.chinatelecom.xysq.bean.Area;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class CityExpandableListAdapter extends BaseExpandableListAdapter {

	private Map<String, List<Area>> areaMap = new LinkedHashMap<String, List<Area>>();

	@SuppressLint("UseSparseArrays")
	private Map<Integer, String> parentPosiMap = new HashMap<Integer, String>();

	private LayoutInflater inflater;

	private CitySelectActivity activity;

	public CityExpandableListAdapter(Map<String, List<Area>> areaMap,Map<Integer, String> parentPosiMap, CitySelectActivity activity,
			LayoutInflater inflater) {
		this.areaMap = areaMap;
		this.parentPosiMap = parentPosiMap;
		this.activity = activity;
		this.inflater = inflater;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		String firstChart = parentPosiMap.get(groupPosition);
		final List<Area> areas = areaMap.get(firstChart);
		TextView textView = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.city_select_group, null);
		}

		textView = (TextView) convertView
				.findViewById(R.id.citySelect_groupTextView);
		textView.setText(areas.get(childPosition).getName());

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Area area = areas.get(childPosition);
				if(area!=null){
					KeyValueUtils.setStringValue(activity, Constants.AREA_NAME,area.getName());
					KeyValueUtils.setLongValue(activity, Constants.AREA_ID,area.getId());
				}
				activity.finish();
			}
		});

		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.city_select_row, null);
		}

		((CheckedTextView) convertView).setText(parentPosiMap
				.get(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded);

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		String firstChar = parentPosiMap.get(groupPosition);
		return areaMap.get(firstChar).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return parentPosiMap.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
