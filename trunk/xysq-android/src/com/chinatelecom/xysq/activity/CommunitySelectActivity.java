package com.chinatelecom.xysq.activity;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.Area;
import com.chinatelecom.xysq.http.ClientRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class CommunitySelectActivity extends Activity implements HttpCallback{

	private ProgressBar progressBar;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.community_select);
		progressBar = (ProgressBar) findViewById(R.id.cs_progressBar);
		this.loadCommunityData();
	}

	private Area findFirstArea(List<Area> areas){
		for(Area area:areas){
			if(area.getCommunities()!=null&&area.getCommunities().size()>0){
				return area;
			}else{
				if(area.getAreas()!=null&&area.getAreas().size()>0){
					return this.findFirstArea(area.getAreas());
				}else{
					continue;
				}
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void success(String url,Object object) {
		progressBar.setVisibility(View.INVISIBLE);
		List<Area> areas = (List<Area>)object;
		Area firstArea = this.findFirstArea(areas);
		String areaName = KeyValueUtils.getValue(this, Constants.AREA_NAME);
		String areaId = KeyValueUtils.getValue(this, Constants.AREA_ID);
		if(StringUtils.isEmpty(areaName)||StringUtils.isEmpty(areaId)){
			Button changeArea = (Button)findViewById(R.id.cs_changeArea);
			changeArea.setText(firstArea.getName());
		}
	}

	@Override
	public void failure(String url,int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

	private void loadCommunityData() {
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		String areaName = KeyValueUtils.getValue(this, Constants.AREA_NAME);
		String areaId = KeyValueUtils.getValue(this, Constants.AREA_ID);
		if(StringUtils.isNotEmpty(areaName)&&StringUtils.isNotEmpty(areaId)){
			Button changeArea = (Button)findViewById(R.id.cs_changeArea);
			changeArea.setText(areaName);
		}
		ClientRequest.queryAreaAndCommunity(this);
		Button changeCommunityButton = (Button) findViewById(R.id.cs_changeArea);
		changeCommunityButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("CommunitySelectActivity", "changeAreaButton is clicked");
			}
		});
	}
}
