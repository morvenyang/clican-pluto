package com.chinatelecom.xysq.adapater;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.activity.CitySelectActivity;
import com.chinatelecom.xysq.bean.Area;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class CityListAdapter extends BaseAdapter {

	private List<Area> areaList;

	private LayoutInflater inflater;

	private CitySelectActivity activity;

	public CityListAdapter(List<Area> areaList, CitySelectActivity activity,
			LayoutInflater inflater) {
		this.areaList = areaList;
		this.activity = activity;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return areaList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.city_select_row, null);
		}
		TextView cityName = (TextView) convertView
				.findViewById(R.id.city_select_row_titleTextView);

		final Area area = areaList.get(position);
		cityName.setText(area.getName());

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (area != null) {
					KeyValueUtils.setStringValue(activity, Constants.AREA_NAME,
							area.getName());
					KeyValueUtils.setLongValue(activity, Constants.AREA_ID,
							area.getId());
				}
				activity.finish();
			}
		});
		return convertView;
	}

}
