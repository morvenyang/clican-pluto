package com.chinatelecom.xysq.application;

import org.apache.commons.lang.StringUtils;

import android.app.Application;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.UserRequest;
import com.chinatelecom.xysq.listener.LocationListener;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

public class XysqApplication extends Application implements HttpCallback {
	public LocationClient locationClient;
	public GeofenceClient mGeofenceClient;
	public LocationListener locationListener;

	public Vibrator mVibrator;

	private User user;
	
	private IWXAPI api;

	@Override
	public void onCreate() {
		super.onCreate();
		this.registerToWx();
		locationClient = new LocationClient(this.getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("gcj02");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		locationClient.setLocOption(option);
		MobclickAgent.openActivityDurationTrack(false);
		String userName = KeyValueUtils.getStringValue(this, Constants.USER_NAME);
		String password = KeyValueUtils.getStringValue(this, Constants.PASSWORD);
		if(StringUtils.isNotEmpty(userName)&&StringUtils.isNotEmpty(password)){
			UserRequest.login(userName, password, this);
		}
	}
	
	
	private void registerToWx(){
		api = WXAPIFactory.createWXAPI(this, "wx1baf50d8f6be0acd",true);
		boolean result = api.registerApp("wx1baf50d8f6be0acd");
		Log.d("XYSQ", "register to wx:"+result);
	}

	@Override
	public void success(String url, Object data) {
		user = (User)data;
	}

	@Override
	public void failure(String url, int code, String message) {
		Log.d("XYSQ", "自动登录失败");
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		if(user!=null){
			KeyValueUtils.setStringValue(this, Constants.USER_NAME, user.getMsisdn());
			KeyValueUtils.setStringValue(this, Constants.PASSWORD, user.getPassword());
		}
	}

}
