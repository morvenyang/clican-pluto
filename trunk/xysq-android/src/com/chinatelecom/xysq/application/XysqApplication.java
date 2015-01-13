package com.chinatelecom.xysq.application;

import android.app.Application;
import android.os.Vibrator;

import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.chinatelecom.xysq.listener.LocationListener;

public class XysqApplication extends Application {
	public LocationClient locationClient;
	public GeofenceClient mGeofenceClient;
	public LocationListener locationListener;

	public Vibrator mVibrator;

	@Override
	public void onCreate() {
		super.onCreate();
		locationClient = new LocationClient(this.getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("gcj02");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		locationClient.setLocOption(option);
	}

}
