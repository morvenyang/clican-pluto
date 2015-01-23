package com.chinatelecom.xysq.activity;

import android.app.Activity;

import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.User;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends Activity {

	protected void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("SplashScreen"); //统计页面
	    MobclickAgent.onResume(this);          //统计时长
	}
	protected void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("SplashScreen"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}
	
	protected abstract String getPageName();
	
	protected User getUser(){
		XysqApplication application = (XysqApplication)this.getApplication();
		return application.getUser();
	}
}
