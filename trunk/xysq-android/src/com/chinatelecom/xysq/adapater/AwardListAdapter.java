package com.chinatelecom.xysq.adapater;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.Award;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class AwardListAdapter extends BaseAdapter {

	private List<Award> awardList;

	private LayoutInflater inflater;

	private Activity activity;

	public AwardListAdapter(List<Award> awardList, Activity activity,
			LayoutInflater inflater) {
		this.awardList = awardList;
		this.activity = activity;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return awardList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.award_row, null);
		}
		TextView name = (TextView) convertView
				.findViewById(R.id.award_row_name);
		TextView cost = (TextView) convertView
				.findViewById(R.id.award_row_cost);
		Button action = (Button) convertView.findViewById(R.id.award_row_action);
		final Award award = awardList.get(position);
		name.setText(award.getName());
		cost.setText(award.getCost()+"");
		if(award.getAmount()<=0){
			action.setText("暂时无货");
			action.setEnabled(false);
		}else{
			action.setText("我要兑换");
			action.setEnabled(true);
			action.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					
				}
				
			});
		}
		return convertView;
	}

}
