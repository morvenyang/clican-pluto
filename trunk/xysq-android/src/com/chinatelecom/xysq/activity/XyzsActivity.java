package com.chinatelecom.xysq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chinatelecom.xysq.R;

public class XyzsActivity extends BaseActivity {

	@Override
	protected String getPageName() {
		return "小翼助手";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xyzs);
		ImageView huodong1 = (ImageView)this.findViewById(R.id.xyzs_huodong1);
		huodong1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(XyzsActivity.this, YyyIndexActivity.class);
				XyzsActivity.this.startActivity(intent);
			}
		});
	}
}
